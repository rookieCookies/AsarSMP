package asar_development.asarsmp.items;

import asar_development.util.Config;
import asar_development.util.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class StoneNeedle implements Listener {
    @EventHandler
    public void onLeftClickOnBlock(PlayerInteractEvent e) {
        if (!"stone_needle".equals(Item.getIDOfItem(e.getPlayer().getInventory().getItemInMainHand())) ||
                (e.getAction() != Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        e.getPlayer().damage(Config.getDouble("custom_items.stone_needle.use_damage"));
    }
    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        if (!"stone_needle".equals(Item.getIDOfItem(e.getPlayer().getInventory().getItemInMainHand())) ||
                (e.getAction() != Action.RIGHT_CLICK_AIR) ||
                (!e.getPlayer().isSneaking())) {
            return;
        }
        e.getPlayer().swingMainHand();
        e.getPlayer().damage(Config.getDouble("custom_items.stone_needle.flesh_damage"));
        e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), Item.getItem("flesh"));
    }
}
