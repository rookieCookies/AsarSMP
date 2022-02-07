package asar_development.asarsmp.events;

import asar_development.asarsmp.AsarSMP;
import asar_development.util.Item;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public class OnPlayerBlockBreak implements Listener {
    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        e.setCancelled(true);
        e.setDropItems(false);
        switch (e.getBlock().getType().toString().toLowerCase(Locale.ROOT)) {
            case "stone" -> stone(e);
            case "cobblestone" -> cobbleStone(e);
            case "gravel" -> gravel(e);
            case "dirt" -> dirt(e);
            case "grass_block" -> grassBlock(e);
            case "sand" -> sand(e);
            case "torch" -> torch(e);
            default -> e.getPlayer().sendMessage("Unexpected block mined, please contact to the owners!");
        }
    }
    private static void stone(BlockBreakEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
            e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Objects.requireNonNull(Item.getItem("cobble", AsarSMP.getInstance().getRng().nextInt(4) + 1)));
            e.setCancelled(false);
            return;
        }
        switch (Item.getIDOfItem(e.getPlayer().getInventory().getItemInMainHand())) {
            case "stone_needle" -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Objects.requireNonNull(Item.getItem("cobble", 16)));
        }
        e.setCancelled(false);
    }
    private static void cobbleStone(BlockBreakEvent e) {
        switch (Item.getIDOfItem(e.getPlayer().getInventory().getItemInMainHand())) {
            case "stone_hammer" -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Objects.requireNonNull(Item.getItem("gravel")));
        }
        e.setCancelled(false);
    }
    private static void gravel(BlockBreakEvent e) {
        switch (Item.getIDOfItem(e.getPlayer().getInventory().getItemInMainHand())) {
            case "stone_hammer" -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Objects.requireNonNull(Item.getItem("dirt")));
            default -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Objects.requireNonNull(Item.getItem("gravel")));
        }
        e.setCancelled(false);
    }
    private static void dirt(BlockBreakEvent e) {
        switch (Item.getIDOfItem(e.getPlayer().getInventory().getItemInMainHand())) {
            case "stone_hammer" -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Objects.requireNonNull(Item.getItem("sand")));
            case "stone_needle" -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Objects.requireNonNull(Item.getItem("fertilizer")));
            default -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Objects.requireNonNull(Item.getItem("dirt")));
        }
        e.setCancelled(false);
    }
    private static void grassBlock(BlockBreakEvent e) {
        switch (Item.getIDOfItem(e.getPlayer().getInventory().getItemInMainHand())) {
            case "stone_hammer" -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Objects.requireNonNull(Item.getItem("oak_sapling")));
            default -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Objects.requireNonNull(Item.getItem("grass_block")));
        }
        e.setCancelled(false);
    }
    private static void sand(BlockBreakEvent e) {
        switch (Item.getIDOfItem(e.getPlayer().getInventory().getItemInMainHand())) {
            case "stone_hammer" -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Objects.requireNonNull(Item.getItem("stone_dust", AsarSMP.getInstance().getRng().nextInt(30) + 1)));
            default -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Objects.requireNonNull(Item.getItem("sand")));
        }
        e.setCancelled(false);
    }
    private static void torch(BlockBreakEvent e) {
        e.setCancelled(false);
    }

}
