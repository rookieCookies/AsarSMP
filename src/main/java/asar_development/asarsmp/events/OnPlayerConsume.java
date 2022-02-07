package asar_development.asarsmp.events;

import asar_development.util.Config;
import asar_development.util.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.PlayerInventory;

public class OnPlayerConsume implements Listener {
    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        PlayerInventory inv = p.getInventory();
        e.getItem().setAmount(e.getItem().getAmount() - 1);

        String consumedItemID = Item.getIDOfItem(inv.getItemInMainHand());
        p.setFoodLevel(p.getFoodLevel() + Config.getInt(String.format("custom_items.food.%s.food_level", consumedItemID)));
        p.setSaturation(p.getSaturation() + Config.getInt(String.format("custom_items.food.%s.saturation", consumedItemID)));

        switch (consumedItemID) {
            case "flesh":
                p.setHealth(p.getHealth() + Config.getInt(String.format("custom_items.food.%s.instant_health", consumedItemID)));
        }
    }
}
