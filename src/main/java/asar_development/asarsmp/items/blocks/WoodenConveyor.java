package asar_development.asarsmp.items.blocks;

import asar_development.asarsmp.AsarSMP;
import asar_development.asarsmp.items.base.Blocks;
import asar_development.asarsmp.items.base.OrientedBlock;
import asar_development.util.Config;
import asar_development.util.Item;
import asar_development.util.Misc;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;

public class WoodenConveyor extends OrientedBlock implements Listener {
    public WoodenConveyor() {
        super("wooden_conveyor");
        new BukkitRunnable() {
            @Override
            public void run() {
                ConfigurationSection configurationSection = getConfig();
                for (Map.Entry<String, Object> entry : configurationSection.getValues(false).entrySet()) {
                    Location baseLoc = Misc.stringToLocation(entry.getKey());
                    World baseWorld = baseLoc.getWorld();
                    if (baseWorld == null) {
                        configurationSection.set(entry.getKey(), null);
                        AsarSMP.getInstance().getLogger().severe("There was an error while trying to fetch the location in block data, removing the value");
                        continue;
                    }
                    Vector directionVector;
                    switch (getDirection(entry.getKey())) {
                        case "E" -> directionVector = new Vector(1, 0, 0);
                        case "W" -> directionVector = new Vector(-1, 0, 0);
                        case "S" -> directionVector = new Vector(0, 0, 1);
                        case "N" -> directionVector = new Vector(0, 0, -1);
                        default -> throw new IllegalArgumentException();
                    }
                    directionVector.multiply(Config.getDouble("custom_items.wooden_conveyor.speed"));
                    for (Entity entity : baseWorld.getNearbyEntities(baseLoc.add(.5, 1.1, .5), .5, 0.1, .5)) {
                        entity.setVelocity(entity.getVelocity().add(directionVector));
                    }
                }
            }
        }.runTaskTimer(AsarSMP.getInstance(), 0, Config.getLong("custom_items.wooden_conveyor.period"));
    }
}
