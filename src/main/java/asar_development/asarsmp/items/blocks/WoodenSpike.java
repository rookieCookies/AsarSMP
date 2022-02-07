package asar_development.asarsmp.items.blocks;

import asar_development.asarsmp.AsarSMP;
import asar_development.asarsmp.items.base.Blocks;
import asar_development.util.Config;
import asar_development.util.Misc;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;

public class WoodenSpike extends Blocks implements Listener {
    public WoodenSpike() {
        super("wooden_spike");
        new BukkitRunnable() {
            @Override
            public void run() {
                ConfigurationSection configurationSection = getConfig();
                double idleDamage = Config.getDouble("custom_items.wooden_spike.idle_damage", 2);
                for (Map.Entry<String, Object> entry : configurationSection.getValues(false).entrySet()) {
                    Location baseLoc = Misc.stringToLocation(entry.getKey());
                    World baseWorld = baseLoc.getWorld();
                    if (baseWorld == null) {
                        configurationSection.set(entry.getKey(), null);
                        AsarSMP.getInstance().getLogger().severe("There was an error while trying to fetch the location in block data, removing the value");
                        continue;
                    }
                    Entity stand = baseWorld.spawnEntity(baseLoc.add(0, 500, 0), EntityType.ARMOR_STAND);
                    for (Entity entity : baseLoc.getWorld().getNearbyEntities(baseLoc.add(.5, 1.1, .5), .5, 0.1, .5)) {
                        ((LivingEntity) entity).damage(idleDamage, stand);
                    }
                    stand.remove();
                }
            }
        }.runTaskTimer(AsarSMP.getInstance(), 0, Config.getLong("custom_items.wooden_spike.idle_period", 2));
    }
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Location loc = e.getEntity().getLocation().getBlock().getLocation();
        loc.add(new Vector(0, -1, 0));
        ConfigurationSection configurationSection = getConfig();
        if (configurationSection == null || !configurationSection.contains(Misc.locationToString(loc))) {
            return;
        }
        float fallDistance = e.getEntity().getFallDistance();
        Entity stand = e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation().add(0, 500, 0), EntityType.ARMOR_STAND);
        ((LivingEntity) e.getEntity()).damage(e.getDamage() + (fallDistance * Config.getDouble("custom_items.wooden_spike.fall_damage", 1)), stand);
        stand.remove();
        e.setDamage(0);
    }
}
