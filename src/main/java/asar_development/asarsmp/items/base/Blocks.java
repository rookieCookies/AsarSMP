package asar_development.asarsmp.items.base;

import asar_development.asarsmp.AsarSMP;
import asar_development.asarsmp.managers.filemanager.FileID;
import asar_development.asarsmp.managers.filemanager.ManagedFile;
import asar_development.util.Item;
import asar_development.util.Misc;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("BukkitListenerImplemented")
public class Blocks {
    private final String id;
    private final NoteBlock blockData;

    public Blocks(String id) {
        this.id = id;
        blockData = getNoteData();
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        place(e);
    }
    @EventHandler
    public void clickEvent(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            // player is sneaking
            if (e.getPlayer().isSneaking()) {
                onLeftClickSneaking(e);
                return;
            }
            onLeftClick(e);
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getPlayer().isSneaking()) {
                onRightClickSneaking(e);
                return;
            }
            onRightClick(e);
        }
    }

    public void onLeftClick(PlayerInteractEvent e) {
        pickUp(e);
    }
    public void onLeftClickSneaking(PlayerInteractEvent e) {
        /*
        Override this method if you want to use the left click sneaking feature.
        */
    }
    public void onRightClick(PlayerInteractEvent e) {
        /*
        Override this method if you want to use the right click feature.
        */
    }
    public void onRightClickSneaking(PlayerInteractEvent e) {
        /*
        Override this method if you want to use the right click sneaking feature.
        */
    }

    public void place(BlockPlaceEvent e) {
        if (!isCorrectItem(e.getItemInHand())) {
            return;
        }
        ConfigurationSection configurationSection = getConfig();
        e.getBlock().setType(Material.NOTE_BLOCK);
        e.getBlock().setBlockData(blockData);
        String blockLocationString = Misc.locationToString(e.getBlock().getLocation());
        configurationSection.set(blockLocationString, e.getPlayer().getUniqueId().toString());
    }
    public void pickUp(PlayerInteractEvent e) {
        ConfigurationSection configurationSection = getConfig();
        Block clickedBlock = e.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        if (!isThisBlock(clickedBlock)) {
            return;
        }
        e.getClickedBlock().setType(Material.AIR);
        e.getPlayer().playSound(e.getPlayer(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.2F, 1);
        e.getPlayer().getWorld().dropItem(clickedBlock.getLocation().add(0.5D, 1, 0.5D), Item.getItem(id));
        configurationSection.set(Misc.locationToString(e.getClickedBlock().getLocation()), null);
    }
    static NoteBlock getNoteData() {
        Block noteBlock = Bukkit.getWorlds().get(0).getBlockAt(0, 255, 0);
        noteBlock.setType(Material.NOTE_BLOCK);
        NoteBlock noteBlockNoteData = (NoteBlock) noteBlock.getBlockData();
        if (!AsarSMP.getInstance().getTempData().containsKey("blocks")) {
            AsarSMP.getInstance().getTempData().put("blocks", -1);
        }
        int totalNotes = (int) AsarSMP.getInstance().getTempData().get("blocks");
        totalNotes++;
        Note note;
        if (0 <= totalNotes && totalNotes <= 25) {
            note = new Note(totalNotes);
            noteBlockNoteData.setPowered(false);
            noteBlockNoteData.setInstrument(Instrument.BANJO);
        } else if (25 < totalNotes && totalNotes <= 50) {
            note = new Note(totalNotes - 25);
            noteBlockNoteData.setPowered(true);
            noteBlockNoteData.setInstrument(Instrument.BANJO);
        } else if (50 < totalNotes && totalNotes <= 75) {
            note = new Note(totalNotes - 50);
            noteBlockNoteData.setPowered(false);
            noteBlockNoteData.setInstrument(Instrument.BASS_DRUM);
        } else if (75 < totalNotes && totalNotes <= 100) {
            note = new Note(totalNotes - 75);
            noteBlockNoteData.setPowered(true);
            noteBlockNoteData.setInstrument(Instrument.BASS_DRUM);
        } else if (100 < totalNotes && totalNotes <= 125) {
            note = new Note(totalNotes - 100);
            noteBlockNoteData.setPowered(false);
            noteBlockNoteData.setInstrument(Instrument.BIT);
        } else if (125 < totalNotes && totalNotes <= 150) {
            note = new Note(totalNotes - 125);
            noteBlockNoteData.setPowered(true);
            noteBlockNoteData.setInstrument(Instrument.BIT);
        } else if (150 < totalNotes && totalNotes <= 175) {
            note = new Note(totalNotes - 150);
            noteBlockNoteData.setPowered(false);
            noteBlockNoteData.setInstrument(Instrument.BASS_GUITAR);
        } else if (175 < totalNotes && totalNotes <= 200) {
            note = new Note(totalNotes - 175);
            noteBlockNoteData.setPowered(true);
            noteBlockNoteData.setInstrument(Instrument.BASS_GUITAR);
        } else if (200 < totalNotes && totalNotes <= 225) {
            note = new Note(totalNotes - 200);
            noteBlockNoteData.setPowered(false);
            noteBlockNoteData.setInstrument(Instrument.BELL);
        } else if (225 < totalNotes && totalNotes <= 250) {
            note = new Note(totalNotes - 225);
            noteBlockNoteData.setPowered(true);
            noteBlockNoteData.setInstrument(Instrument.BELL);
        } else if (250 < totalNotes && totalNotes <= 275) {
            note = new Note(totalNotes - 250);
            noteBlockNoteData.setPowered(false);
            noteBlockNoteData.setInstrument(Instrument.CHIME);
        } else if (275 < totalNotes && totalNotes <= 300) {
            note = new Note(totalNotes - 275);
            noteBlockNoteData.setPowered(true);
            noteBlockNoteData.setInstrument(Instrument.CHIME);
        } else if (300 < totalNotes && totalNotes <= 325) {
            note = new Note(totalNotes - 300);
            noteBlockNoteData.setPowered(false);
            noteBlockNoteData.setInstrument(Instrument.COW_BELL);
        } else if (325 < totalNotes && totalNotes <= 350) {
            note = new Note(totalNotes - 325);
            noteBlockNoteData.setPowered(true);
            noteBlockNoteData.setInstrument(Instrument.COW_BELL);
        } else if (350 < totalNotes && totalNotes <= 375) {
            note = new Note(totalNotes - 350);
            noteBlockNoteData.setPowered(false);
            noteBlockNoteData.setInstrument(Instrument.DIDGERIDOO);
        } else if (375 < totalNotes && totalNotes <= 400) {
            note = new Note(totalNotes - 375);
            noteBlockNoteData.setPowered(true);
            noteBlockNoteData.setInstrument(Instrument.DIDGERIDOO);
        } else {
            throw new IllegalArgumentException("Too many blocks, please contact to the plugin developer to add more blocks");
        }
        noteBlockNoteData.setNote(note);
        noteBlockNoteData.setPowered(false);
        noteBlock.setBlockData(noteBlockNoteData);
        AsarSMP.getInstance().getTempData().put("blocks", totalNotes);
        return noteBlockNoteData;
    }

    public boolean isCorrectItem(ItemStack item) {
        return (getId().equals(Item.getIDOfItem(item)));
    }
    public boolean isThisBlock(Block block) {
        return isThisBlock(block.getLocation());
    }
    public boolean isThisBlock(String str) {
        return isThisBlock(Misc.stringToLocation(str));
    }

    public boolean isThisBlock(Location location) {
        if (location == null) {
            return false;
        }
        ConfigurationSection configurationSection = getConfig();
        String stringLocation = Misc.locationToString(location);
        return configurationSection != null &&
                configurationSection.contains(stringLocation);
    }

    public ConfigurationSection getConfig() {
        ManagedFile blocksMFile = AsarSMP.getInstance().getFileManager().getFile(FileID.BLOCKS);
        FileConfiguration blocksConfig = blocksMFile.getFileConfiguration();
        ConfigurationSection configurationSection = blocksConfig.getConfigurationSection(getId());
        if (configurationSection == null) {
            configurationSection = blocksConfig.createSection(getId());
        }
        return configurationSection;
    }
    public String getId() {
        return id;
    }
    @SuppressWarnings("unused")
    public NoteBlock getBlockData() {
        return blockData;
    }
}
