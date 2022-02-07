package asar_development.util;

import asar_development.asarsmp.AsarSMP;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Item {
    Item() { throw new IllegalStateException("Utility class"); }

    public static ItemStack refreshItem(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return null;
        }
        String itemID = getIDOfItem(item);
        ItemStack newItem = getItem(itemID);
        newItem.setAmount(item.getAmount());
        return newItem;
    }
    @SuppressWarnings("unused")
    public static void refreshInventory(PlayerInventory inv) {
        for (var i = 0; i < 35; i++) {
            if (inv.getItem(i) == null) {
                continue;
            }
            inv.setItem(i, refreshItem(inv.getItem(i)));
        }
        inv.setHelmet(refreshItem(inv.getHelmet()));
        inv.setChestplate(refreshItem(inv.getChestplate()));
        inv.setLeggings(refreshItem(inv.getLeggings()));
        inv.setBoots(refreshItem(inv.getBoots()));
        inv.setItemInOffHand(refreshItem(inv.getItemInOffHand()));
    }
    @SuppressWarnings("unused")
    public static float getFloatFromItem(ItemStack item, String nameSpace) {
        if (item == null) {
            return 0F;
        }
        var itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return 0F;
        }
        var container = itemMeta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(AsarSMP.getInstance(), nameSpace);
        Float returnValue = container.get(namespacedKey, PersistentDataType.FLOAT);
        if (returnValue == null) {
            returnValue = 0F;
        }
        return returnValue;
    }
    public static String getIDOfItem(ItemStack item) {
        String defaultText = "default";
        if (item == null || item.getType() == Material.AIR) {
            return defaultText;
        }
        NBTItem itemNBT = new NBTItem(item);
        return itemNBT.getString("item_id");
    }
    @SuppressWarnings("unused")
    public static String getTypeOfItem(ItemStack item) {
        if (item == null) {
            return null;
        }
        var itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return null;
        }
        var container = itemMeta.getPersistentDataContainer();
        return container.get(new NamespacedKey(AsarSMP.getInstance(), "item_type"), PersistentDataType.STRING);
    }

    public static ItemStack hideAttributes(ItemStack item) {
        ItemStack newItem;
        newItem = item;
        ItemMeta itemMeta = newItem.getItemMeta();
        if (itemMeta == null) {
            return item;
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setUnbreakable(true);
        newItem.setItemMeta(itemMeta);
        return newItem;
    }
    @SuppressWarnings("unused")
    public static @NotNull ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        var item = new ItemStack(material, 1);
        final var meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Misc.coloured(name));
        var colouredLore = new ArrayList<String>();
        for (String l : lore) {
            colouredLore.add(Misc.coloured(l));
        }
        meta.setLore(colouredLore);
        item.setItemMeta(meta);
        return hideAttributes(item);
    }
    public static void saveItem(ItemStack item) {
        AsarSMP.getInstance().getItems().put(getIDOfItem(item), item);
    }

    public static ItemStack getItem(String id) {
        return getItem(id, 1);
    }
    public static ItemStack getItem(String id, int amount) {
        AsarSMP instance = AsarSMP.getInstance();
        if ("air".equals(id)) {
            return new ItemStack(Material.AIR);
        }
        ItemStack item = instance.getItems().get(id);
        if (item == null) {
            String errorMessage = String.format("Failed to fetch the item with the ID of %s", id);
            AsarSMP.getInstance().getLogger().severe(errorMessage);
            return getItem("default");
        }
        item.setAmount(amount);
        return item;
    }
}
