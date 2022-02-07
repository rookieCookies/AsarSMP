package asar_development.asarsmp.items.blocks;

import asar_development.asarsmp.AsarSMP;
import asar_development.asarsmp.items.base.Blocks;
import asar_development.asarsmp.managers.filemanager.FileID;
import asar_development.asarsmp.managers.filemanager.ManagedFile;
import asar_development.util.Item;
import asar_development.util.Misc;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StoneComposter extends Blocks implements Listener {
    public StoneComposter() {
        super("stone_composter");
        ConfigurationSection configurationSection = getConfig();
        for (Map.Entry<String, Object> entry : configurationSection.getValues(false).entrySet()) {
            String key = entry.getKey();
            Location loc = Misc.stringToLocation(key);
            spawnArmorStand(loc);
        }
    }
    @Override
    public void place(BlockPlaceEvent e) {
        super.place(e);
        if (!isCorrectItem(e.getItemInHand())) {
            return;
        }
        ConfigurationSection configurationSection = getConfig();
        String location = Misc.locationToString(e.getBlock().getLocation());
        configurationSection.set(location + ".stage", 0);
        configurationSection.set(location + ".storage", new ArrayList<String>());
        spawnArmorStand(e.getBlock().getLocation());
    }

    @Override
    public void pickUp(PlayerInteractEvent e) {
        super.pickUp(e);
        removeArmorStand(Objects.requireNonNull(e.getClickedBlock()).getLocation());
    }

    @Override
    public void onRightClick(PlayerInteractEvent e) {
        addItem(e);
    }

    private void addItem(PlayerInteractEvent e) {
        Location loc = Objects.requireNonNull(e.getClickedBlock()).getLocation();
        String path = Misc.locationToString(loc);
        ConfigurationSection configurationSection = getConfig();
        if (!isThisBlock(path)) {
            return;
        }
        List<String> list = configurationSection.getStringList(path + ".storage");
        if (!"dirt".equals(Item.getIDOfItem(e.getItem()))) {
            return;
        }
        list.add(Item.getIDOfItem(e.getItem()));
        configurationSection.set(path + ".stage", list.size());
        configurationSection.set(path + ".storage", list);
        spawnArmorStand(loc);
        e.setCancelled(true);
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - 1);
        e.getPlayer().getInventory().setItemInMainHand(item);
        e.getPlayer().playSound(e.getPlayer(), Sound.BLOCK_COMPOSTER_FILL, 1, 1);
        finish(e);
    }

    private void finish(PlayerInteractEvent e) {
        System.out.println("Finish function called");
        Location loc = Objects.requireNonNull(e.getClickedBlock()).getLocation();
        String path = Misc.locationToString(loc);
        ConfigurationSection configurationSection = getConfig();
        if (!isThisBlock(path)) {
            return;
        }
        System.out.println("Stage one");
        int stage = configurationSection.getInt(path + ".stage");
        if (stage < 4) {
            return;
        }
        System.out.println("Stage two");
        configurationSection.set(path + ".storage", null);
        configurationSection.set(path + ".stage", 0);
        e.getClickedBlock().getWorld().dropItem(e.getClickedBlock().getLocation().add(new Vector(0.5, 1, 0.5)), Item.getItem("grass_block"));
        e.getPlayer().playSound(e.getPlayer(), Sound.BLOCK_COMPOSTER_READY, 1, 1);
        System.out.println("Finish function called");
    }

    private static void removeArmorStand(Location loc) {
        if (AsarSMP.getInstance().getTempEntity().containsKey(Misc.locationToString(loc))) {
            AsarSMP.getInstance().getTempEntity().get(Misc.locationToString(loc)).remove();
            AsarSMP.getInstance().getTempEntity().remove(Misc.locationToString(loc));
        }
    }
    private void spawnArmorStand(Location loc) {
        ManagedFile blocksMFile = AsarSMP.getInstance().getFileManager().getFile(FileID.BLOCKS);
        FileConfiguration blocksConfig = blocksMFile.getFileConfiguration();
        ConfigurationSection configurationSection = blocksConfig.getConfigurationSection(getId());
        loc.add(new Vector(0.5, 1, 0.5));
        ArmorStand label = (ArmorStand) Objects.requireNonNull(loc.getWorld()).spawnEntity(loc, EntityType.ARMOR_STAND);
        loc.add(new Vector(-0.5, -1, -0.5));
        label.setGravity(false);
        label.setCustomNameVisible(true);
        label.setInvulnerable(true);
        label.setMarker(true);
        label.setVisible(false);
        if (configurationSection == null) {
            configurationSection = blocksConfig.createSection(getId());
        }
        ConfigurationSection config = configurationSection.getConfigurationSection(Misc.locationToString(loc));
        if (config == null) {
            config = configurationSection.createSection(Misc.locationToString(loc));
        }
        int stage = config.getInt("stage", 0);
        String text;
        switch (stage) {
            case 1 -> text = "&c25%";
            case 2 -> text = "&650%";
            case 3 -> text = "&e75%";
            default -> text = "&40%";
        }
        label.setCustomName(Misc.coloured(text));
        removeArmorStand(loc);
        AsarSMP.getInstance().getTempEntity().put(Misc.locationToString(loc), label);
    }
}
