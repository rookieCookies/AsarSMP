package asar_development.asarsmp.registerers;

import asar_development.asarsmp.AsarSMP;
import asar_development.asarsmp.managers.filemanager.FileID;
import asar_development.asarsmp.managers.filemanager.FileManager;
import asar_development.util.Item;
import asar_development.util.Misc;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class CompileItems {

    private CompileItems() {
        throw new IllegalStateException("Function class");
    }

    public static void run() {
        AsarSMP instance = AsarSMP.getInstance();
        FileManager fileManager = instance.getFileManager();
        ConfigurationSection itemFile = fileManager.getFile(FileID.ITEMS).getFileConfiguration();
        Map<String, Object> sec = itemFile.getValues(false);
        for (String path : sec.keySet()) {
            String itemMaterialString = itemFile.getString(path + ".material", "stone");
            Material itemMaterial = Material.matchMaterial(itemMaterialString);
            if (itemMaterial == null) {
                itemMaterial = Material.STONE;
            }
            ItemStack item = new ItemStack(itemMaterial);
            Item.hideAttributes(item);
            ItemMeta itemMeta = item.getItemMeta();
            assert itemMeta != null;
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();

            String displayName = Misc.coloured(itemFile.getString(path + ".display_name"));
            String type = itemFile.getString(path + ".type", "item");
            data.set(ItemDataID.ITEM_TYPE.key(), PersistentDataType.STRING, type);
            itemMeta.setDisplayName(displayName);
            item.setItemMeta(itemMeta);
            addStat(item, path + ".stats.attack_damage", Attribute.GENERIC_ATTACK_DAMAGE);
            addStat(item, path + ".stats.max_health", Attribute.GENERIC_MAX_HEALTH);
            addStat(item, path + ".stats.armor", Attribute.GENERIC_ARMOR);
            addStat(item, path + ".stats.armor_toughness", Attribute.GENERIC_ARMOR_TOUGHNESS);
            addStat(item, path + ".stats.knockback", Attribute.GENERIC_ATTACK_KNOCKBACK);
            addStat(item, path + ".stats.attack_speed", Attribute.GENERIC_ATTACK_SPEED);
            addStat(item, path + ".stats.movement_speed", Attribute.GENERIC_MOVEMENT_SPEED);
            addStat(item, path + ".stats.flight_speed", Attribute.GENERIC_FLYING_SPEED);
            addStat(item, path + ".stats.knockback_resistance", Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            itemMeta = item.getItemMeta();
            List<String> loreList = new ArrayList<>();

            // Adding lore
            List<String> lore = itemFile.getStringList(path + ".lore");
            if (!lore.isEmpty()) {
                for (String str : lore) {
                    loreList.add(Misc.coloured(str));
                }
            }

            // Finishing touches
            int customModelData = itemFile.getInt(path + ".custom_model_data", itemFile.getInt(path + ".cmd", 0));
            itemMeta.setCustomModelData(customModelData);
            itemMeta.setLore(loreList);
            item.setItemMeta(itemMeta);

            // Coloring leather items
            if (item.getType() == Material.LEATHER_HELMET ||
                    item.getType() == Material.LEATHER_CHESTPLATE ||
                    item.getType() == Material.LEATHER_LEGGINGS ||
                    item.getType() == Material.LEATHER_BOOTS) {
                LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                var r = itemFile.getInt(path + ".color.r", 0);
                var g = itemFile.getInt(path + ".color.g", 0);
                var b = itemFile.getInt(path + ".color.b", 0);
                meta.setColor(Color.fromRGB(r, g, b));
                item.setItemMeta(meta);
            }
            var glowing = itemFile.getBoolean(path + ".glowing");
            if (glowing) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            }
            NBTItem itemNBT = new NBTItem(item);
            itemNBT.setString("item_id", path);
            Item.saveItem(itemNBT.getItem());
        }
    }
    private static void addStat(ItemStack item, String path, Attribute attribute) {
        AsarSMP instance = AsarSMP.getInstance();
        FileManager fileManager = instance.getFileManager();
        ConfigurationSection itemFile = fileManager.getFile(FileID.ITEMS).getFileConfiguration();
        if (!itemFile.contains(path + ".value")) {
            return;
        }
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        AttributeModifier.Operation operation = switch (itemFile.getString(path + ".operation", "add")) {
            case "scalar" -> AttributeModifier.Operation.ADD_SCALAR;
            case "multiply" -> AttributeModifier.Operation.MULTIPLY_SCALAR_1;
            case "add" -> AttributeModifier.Operation.ADD_NUMBER;
            default -> throw new IllegalArgumentException(
                    "Invalid operation at " + (path + ".operation").replace(".", " > ")
            );
        };
        EquipmentSlot slot = switch (itemFile.getString(path + ".slot", "head")) {
            case "head", "h" -> EquipmentSlot.HEAD;
            case "chestplate", "c", "chest" -> EquipmentSlot.CHEST;
            case "leggings", "legs", "l" -> EquipmentSlot.LEGS;
            case "boots", "b", "f" -> EquipmentSlot.FEET;
            case "offhand", "off" -> EquipmentSlot.OFF_HAND;
            case "hand", "mh" -> EquipmentSlot.HAND;
            default -> throw new IllegalArgumentException(
                    "Invalid slot value at " + (path + ".slot").replace(".", " > ")
            );
        };
        double value = itemFile.getDouble(path + ".value", 0);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "value", value, operation, slot);
        itemMeta.addAttributeModifier(attribute, modifier);
        item.setItemMeta(itemMeta);
    }
}
