package asar_development.asarsmp.events;

import asar_development.asarsmp.AsarSMP;
import asar_development.util.Item;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class UpdatePermissions implements Listener {
    @EventHandler
    public void onPlayerHeldItemChange(PlayerItemHeldEvent e) {
        updatePerm(e.getPlayer(), e.getPlayer().getInventory().getItem(e.getNewSlot()));
        Item.refreshInventory(e.getPlayer().getInventory());
    }
    @EventHandler
    public void onInventoryClick(InventoryInteractEvent e) {
        updatePerm((Player) e.getWhoClicked());
        Item.refreshInventory(e.getWhoClicked().getInventory());
    }

    private static void updatePerm(Player p) {
        updatePerm(p, p.getInventory().getItemInMainHand());
    }
    private static void updatePerm(Player p, ItemStack item) {
        String permissionToAssign = "item." + Item.getIDOfItem(item).replace("_", "-");
        String oldPermission = p.getPersistentDataContainer().getOrDefault(new NamespacedKey(AsarSMP.getInstance(), "item_permission"), PersistentDataType.STRING, "default");
        p.getPersistentDataContainer().set(new NamespacedKey(AsarSMP.getInstance(), "item_permission"), PersistentDataType.STRING, permissionToAssign);
        User user = AsarSMP.getInstance().getLuckPerms().getPlayerAdapter(Player.class).getUser(p);
        PermissionNode permissionNode = PermissionNode.builder(permissionToAssign).build();
        PermissionNode oldPermissionNode = PermissionNode.builder(oldPermission).build();
        user.data().remove(oldPermissionNode);
        user.data().add(permissionNode);
        AsarSMP.getInstance().getLuckPerms().getUserManager().saveUser(user);
    }
}
