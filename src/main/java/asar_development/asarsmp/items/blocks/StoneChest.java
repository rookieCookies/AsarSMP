package asar_development.asarsmp.items.blocks;

import asar_development.asarsmp.AsarSMP;
import asar_development.asarsmp.items.base.OrientedBlock;
import asar_development.util.Config;
import asar_development.util.Item;
import asar_development.util.Misc;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StoneChest extends OrientedBlock implements Listener {
    public StoneChest() {
        super("stone_chest");
    }

    @Override
    public void place(BlockPlaceEvent e) {
        super.place(e);
        if (!isCorrectItem(e.getItemInHand())) {
            return;
        }
        ConfigurationSection configurationSection = getConfig();
        String location = Misc.locationToString(e.getBlock().getLocation());
        configurationSection.set(location + ".user", null);
        configurationSection.set(location + ".storage", new ArrayList<String>());
    }

    @Override
    public void pickUp(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK ||
                e.getClickedBlock() == null ||
                !isThisBlock(e.getClickedBlock())) {
            return;
        }
        ConfigurationSection configurationSection = getConfig();
        if (!isOwner(e.getPlayer(), e.getClickedBlock())) {
            e.setCancelled(true);
            return;
        }
        e.getClickedBlock().setType(Material.AIR);
        e.getPlayer().playSound(e.getPlayer(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.2F, 1);
        e.getPlayer().getWorld().dropItem(e.getClickedBlock().getLocation().add(new Vector(0.5, 1, 0.5)), Item.getItem(getId()));
        for (String item : configurationSection.getStringList(Misc.locationToString(e.getClickedBlock().getLocation()) + ".storage")) {
            String itemID = item.substring(item.indexOf("-") + 1, item.indexOf("|"));
            int amount = Integer.parseInt(item.substring(0, item.indexOf("-")));
            e.getClickedBlock().getWorld().dropItem(e.getClickedBlock().getLocation().add(new Vector(0.5, 1, 0.5)), Objects.requireNonNull(Item.getItem(itemID, amount)));
        }
        configurationSection.set(Misc.locationToString(e.getClickedBlock().getLocation()), null);
    }

    @Override
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }
        boolean setCancelled = openInventory(e.getPlayer(), e.getClickedBlock().getLocation());
        if (setCancelled) {
            e.setUseInteractedBlock(Event.Result.DENY);
            e.setUseItemInHand(Event.Result.DENY);
        }
    }

    public boolean openInventory(HumanEntity p, Location loc) {
        ConfigurationSection configurationSection = getConfig();
        if (!isThisBlock(loc.getBlock())) {
            return false;
        }
        if (!isOwner((Player) p, loc)) {
            return true;
        }
        configurationSection.set(Misc.locationToString(loc) + ".user", p.getUniqueId().toString());
        Inventory inv = Bukkit.createInventory(null, Math.min(Config.getInt("custom_items.stone_chest.size", 1), 6) * 9, Misc.coloured(Config.getString("custom_items.stone_chest.title", "&7Stone Chest")));
        for (String item : configurationSection.getStringList(Misc.locationToString(loc) + ".storage")) {
            String itemID = item.substring(item.indexOf("-") + 1, item.indexOf("|"));
            int slot = Integer.parseInt(item.substring(item.indexOf("|") + 1));
            int amount = Integer.parseInt(item.substring(0, item.indexOf("-")));
            inv.setItem(slot, Item.getItem(itemID, amount));
        }
        PersistentDataContainer pdc = p.getPersistentDataContainer();
        pdc.set(new NamespacedKey(AsarSMP.getInstance(), "current_inventory"), PersistentDataType.STRING, Misc.locationToString(loc));
        p.openInventory(inv);
        return true;
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {
        updateFiles((Player) e.getWhoClicked(), e.getInventory(), e.getView());
    }

    @EventHandler
    public void inventoryDragEvent(InventoryDragEvent e) {
        updateFiles((Player) e.getWhoClicked(), e.getInventory(), e.getView());
    }
    public boolean updateFiles(Player p, Inventory inventory, InventoryView view) {
        if (!isStoneChest(view)) {
            return true;
        }
        ConfigurationSection configurationSection = getConfig();
        PersistentDataContainer pdc = p.getPersistentDataContainer();
        String currentInventoryPath = pdc.get(new NamespacedKey(AsarSMP.getInstance(), "current_inventory"), PersistentDataType.STRING);
        if (currentInventoryPath == null) {
            return false;
        }
        if ((!isThisBlock(Misc.stringToLocation(currentInventoryPath)))
                || (!isOwner(p, Misc.stringToLocation(currentInventoryPath)))) {
            return false;
        }
        List<String> list = new ArrayList<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                int index = 0;
                for (ItemStack i : inventory.getContents()) {
                    if (i != null) {
                        list.add(i.getAmount() + "-" + Item.getIDOfItem(i) + "|" + index);
                    }
                    index++;
                }
                configurationSection.set(currentInventoryPath + ".storage", list);
            }
        }.runTaskLater(AsarSMP.getInstance(), 1);
        return true;
    }

    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent e) {
        if (!isStoneChest(e.getView())) {
            return;
        }
        ConfigurationSection configurationSection = getConfig();
        PersistentDataContainer pdc = e.getPlayer().getPersistentDataContainer();
        String currentInventoryPath = pdc.get(new NamespacedKey(AsarSMP.getInstance(), "current_inventory"), PersistentDataType.STRING);
        if (currentInventoryPath == null) {
            return;
        }
        if ((!isThisBlock(Misc.stringToLocation(currentInventoryPath)))
                || (!isOwner((Player) e.getPlayer(), Misc.stringToLocation(currentInventoryPath)))) {
            return;
        }
        configurationSection.set(currentInventoryPath + ".user", null);
    }

    private boolean isOwner(Player p, Block block) {
        return isOwner(p, block.getLocation());
    }
    private boolean isOwner(Player p, Location loc) {
        return p.getUniqueId().toString().equals(getConfig().getString(Misc.locationToString(loc) + ".user")) || getConfig().getString(Misc.locationToString(loc) + ".user") == null;
    }
    private static boolean isStoneChest(InventoryView view) {
        return Misc.coloured(Config.getString("custom_items.stone_chest.title", "&7Stone Chest")).equals(view.getTitle());
    }
}
