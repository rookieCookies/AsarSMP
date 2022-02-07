package asar_development.asarsmp.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventNoteblockUpdate implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractWithNoteblock(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            System.out.println(e.getClass().getSimpleName() + ": e.getClickedBlock() == null");
            return;
        }
        if (e.getClickedBlock().getType() != Material.NOTE_BLOCK) {
            System.out.println(e.getClass().getSimpleName() + ": e.getClickedBlock().getType() != Material.NOTE_BLOCK");
            return;
        }
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            System.out.println(e.getClass().getSimpleName() + ": e.getPlayer().getGameMode() == GameMode.CREATIVE");
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block aboveBlock = event.getBlock().getLocation().add(0, 1, 0).getBlock();
        if (aboveBlock.getType() == Material.NOTE_BLOCK) {
            updateAndCheck(event.getBlock().getLocation());
            event.setCancelled(true);
        }
        if (event.getBlock().getType() == Material.NOTE_BLOCK) {
            event.setCancelled(true);
        }
        if (event.getBlock().getType().toString().toLowerCase().contains("sign")) {
            return;
        }
        event.getBlock().getState().update(true, false);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Block clickedBlock = e.getClickedBlock();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK ||
                clickedBlock == null ||
                clickedBlock.getType() != Material.NOTE_BLOCK ||
                e.getItem() == null ||
                !e.getItem().getType().isBlock() ||
                e.useInteractedBlock() == Event.Result.DENY ||
                e.useItemInHand() == Event.Result.DENY) {
            return;
        }
        e.setCancelled(true);
        System.out.println("Place block");
        Block blockToBePlaced = e.getClickedBlock().getRelative(e.getBlockFace());
        blockToBePlaced.setType(e.getItem().getType());
        BlockPlaceEvent newEvent = new BlockPlaceEvent(
                blockToBePlaced,
                blockToBePlaced.getState(),
                e.getClickedBlock(),
                e.getItem(),
                e.getPlayer(),
                true,
                e.getHand()
        );
        Bukkit.getPluginManager().callEvent(newEvent);
        e.getItem().setAmount(e.getItem().getAmount() - 1);
    }

    public void updateAndCheck(Location loc) {
        Block b = loc.add(0, 1, 0).getBlock();
        if (b.getType() == Material.NOTE_BLOCK) {
            b.getState().update(true, true);
        }
        Location nextBlock = b.getLocation().add(0, 1, 0);
        if (nextBlock.getBlock().getType() == Material.NOTE_BLOCK) {
            updateAndCheck(b.getLocation());
        }
    }
}
