package asar_development.asarsmp.items.blocks;

import asar_development.asarsmp.items.base.Blocks;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class StoneCraftingTable extends Blocks implements Listener {
    public StoneCraftingTable() {
        super("stone_crafting_table");
    }
    @Override
    public void onRightClick(PlayerInteractEvent e) {
        ConfigurationSection configurationSection = getConfig();
        Block clickedBlock = e.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        if (!isThisBlock(clickedBlock)) {
            return;
        }
        e.getPlayer().openWorkbench(null, true);
        e.setCancelled(true);
    }
}
