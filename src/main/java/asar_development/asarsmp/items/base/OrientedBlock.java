package asar_development.asarsmp.items.base;

import asar_development.util.Misc;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class OrientedBlock extends Blocks {
    private final BlockData blockDataDirectionNorth;
    private final BlockData blockDataDirectionSouth;
    private final BlockData blockDataDirectionEast;
    private final BlockData blockDataDirectionWest;

    public OrientedBlock(String id) {
        super(id);
        blockDataDirectionNorth = getBlockData();
        blockDataDirectionSouth = getNoteData();
        blockDataDirectionEast = getNoteData();
        blockDataDirectionWest = getNoteData();
    }

    @Override
    public void place(BlockPlaceEvent e) {
        if (!isCorrectItem(e.getItemInHand())) {
            return;
        }
        ConfigurationSection configurationSection = getConfig();
        e.getBlock().setType(Material.NOTE_BLOCK);
        e.getBlock().setBlockData(blockDataDirectionEast);
        Location baseLocation = e.getBlock().getLocation();
        if (baseLocation.add(0, 0, -1).getBlock().equals(e.getBlockAgainst())) {
            e.getBlock().setBlockData(blockDataDirectionSouth);
        } else if (baseLocation.add(0, 0, 2).getBlock().equals(e.getBlockAgainst())) {
            e.getBlock().setBlockData(blockDataDirectionNorth);
        } else if (baseLocation.add(1, 0, -1).getBlock().equals(e.getBlockAgainst())) {
            e.getBlock().setBlockData(blockDataDirectionWest);
        }
        String blockLocationString = Misc.locationToString(e.getBlock().getLocation());
        configurationSection.set(blockLocationString + ".direction", "N");
    }

//    @Override
//    public void onRightClickSneaking(PlayerInteractEvent e) {
//        changeDirection(e);
//    }

//    private void changeDirection(PlayerInteractEvent e) {
//        String blockLocationString = Misc.locationToString(Objects.requireNonNull(e.getClickedBlock()).getLocation());
//        String currentDirection = getDirection(blockLocationString);
//        String returnDirection;
//        switch (currentDirection) {
//            case "N" ->  returnDirection = "W";
//            case "W" ->  returnDirection = "S";
//            case "S" ->  returnDirection = "E";
//            case "E" ->  returnDirection = "N";
//            default -> throw new IllegalArgumentException("Unknown direction value");
//        }
//        getConfig().set(blockLocationString + ".direction", returnDirection);
//        updateDirection(e.getClickedBlock());
//    }
//    private void updateDirection(Block block) {
//        switch (getConfig().getString(Misc.locationToString(block.getLocation()) + ".direction", "N")) {
//            case "N" -> block.setBlockData(blockDataDirectionNorth);
//            case "S" -> block.setBlockData(blockDataDirectionSouth);
//            case "E" -> block.setBlockData(blockDataDirectionEast);
//            case "W" -> block.setBlockData(blockDataDirectionWest);
//        }
//    }

    public String getDirection(String blockLocationString) {
        return getConfig().getString(blockLocationString + ".direction", "N");
    }
}
