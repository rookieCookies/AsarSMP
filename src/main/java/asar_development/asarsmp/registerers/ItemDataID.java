package asar_development.asarsmp.registerers;

import asar_development.asarsmp.AsarSMP;
import org.bukkit.NamespacedKey;

@SuppressWarnings("unused")
public enum ItemDataID {
    ITEM_TYPE,
    RARITY,
    WEAPON_DAMAGE_1,
    WEAPON_DAMAGE_2,
    DAMAGE,
    HEALTH,
    DEFENSE,
    TEX,
    SPEED,
    LIFE_STEAL,
    BREAK_POWER,
    PICKAXE_TYPE,
    COOLDOWN,
    CREATOR,
    CREATOR_DISCORD,
    CRYSTAL_ID,
    CRYSTAL_COOLDOWN();

    private final NamespacedKey key;

    ItemDataID() {
        key = new NamespacedKey(AsarSMP.getInstance(), this.toString());
    }

    public NamespacedKey key() { return key; }
}
