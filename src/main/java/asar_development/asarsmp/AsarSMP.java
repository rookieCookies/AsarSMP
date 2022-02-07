package asar_development.asarsmp;

import asar_development.asarsmp.commands.BlockDataCommand;
import asar_development.asarsmp.commands.DiscordCommand;
import asar_development.asarsmp.commands.EmojisCommand;
import asar_development.asarsmp.commands.getitem.GetItemCommand;
import asar_development.asarsmp.commands.getitem.GetItemTabComplete;
import asar_development.asarsmp.commands.server.ServerCommand;
import asar_development.asarsmp.commands.server.ServerTabComplete;
import asar_development.asarsmp.events.*;
import asar_development.asarsmp.items.StoneNeedle;
import asar_development.asarsmp.items.blocks.*;
import asar_development.asarsmp.managers.filemanager.FileID;
import asar_development.asarsmp.managers.filemanager.FileManager;
import asar_development.asarsmp.registerers.CompileItems;
import asar_development.asarsmp.registerers.RegisterRecipes;
import asar_development.util.Config;
import asar_development.util.Misc;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class AsarSMP extends JavaPlugin {
    private static AsarSMP instance;
    private FileManager fileManager;
    private Map<String, ItemStack> items;
    private Map<String, Entity> tempEntity;
    private Map<String, Object> tempData;
    private final Random rng = new Random();
    private LuckPerms luckPerms;
    private Chat chat;
    private BukkitTask broadcast;
    private ArrayList<Listener> events;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        instance = this;
        items = new HashMap<>();
        tempData = new HashMap<>();
        tempEntity = new HashMap<>();
        luckPerms = LuckPermsProvider.get();

        createFiles();
        registerCommands();
        registerEvents();
        registerTimedBroadcast();
        registerItems();
        registerBlocks();
        new RegisterRecipes();
        setupChat();

        long time = System.currentTimeMillis() - start;
        String pluginName = getDescription().getName();
        String enableMessage = String.format("%s was been successfully enabled! (%dms)", pluginName, time);
        getLogger().info(enableMessage);
    }
    @Override
    public void onDisable() {
        for (Entity i : tempEntity.values()) {
            i.remove();
        }
        fileManager.getFile(FileID.BLOCKS).save();
    }

    public void registerTimedBroadcast() {
        long start = System.currentTimeMillis();
        if (broadcast != null) {
            broadcast = null;
        }
        List<String> messageList = Config.getStringList("broadcast.messages");
        long period = Config.getLong("broadcast.period");
        long initialDelay = Config.getLong("broadcast.initial_delay");
        broadcast = new BukkitRunnable() {
            @Override
            public void run() {
                int index = rng.nextInt(messageList.size());
                Bukkit.broadcastMessage(Misc.coloured(messageList.get(index)));
            }
        }.runTaskTimer(instance, initialDelay, period);

        long time = System.currentTimeMillis() - start;
        String completeMessage = String.format("Registered timed broadcasts successfully! (%dms)", time);
        getLogger().info(completeMessage);
    }
    private void createFiles() {
        long start = System.currentTimeMillis();
        saveDefaultConfig();
        fileManager = new FileManager(this);
        fileManager.addFile(FileID.ITEMS, fileManager.create("file_management.items_file", "items"));
        fileManager.addFile(FileID.RECIPES, fileManager.create("file_management.recipes_file", "recipes"));
        fileManager.addFile(FileID.BLOCKS, fileManager.create("file_management.block_data", "data_file"));

        long time = System.currentTimeMillis() - start;
        String completeMessage = String.format("Registered all files successfully! (%dms)", time);
        getLogger().info(completeMessage);
    }

    @SuppressWarnings("ConstantConditions")
    public void registerCommands() {
        long start = System.currentTimeMillis();
        this.getCommand("discord").setExecutor(new DiscordCommand());
        this.getCommand("blockdata").setExecutor(new BlockDataCommand());
        this.getCommand("emojis").setExecutor(new EmojisCommand());
        this.getCommand("get").setExecutor(new GetItemCommand());
        this.getCommand("get").setTabCompleter(new GetItemTabComplete());
        this.getCommand("server").setExecutor(new ServerCommand());
        this.getCommand("server").setTabCompleter(new ServerTabComplete());

        long time = System.currentTimeMillis() - start;
        String completeMessage = String.format("Registered all commands successfully! (%dms)", time);
        getLogger().info(completeMessage);
    }
    public void registerEvents() {
        long start = System.currentTimeMillis();
        getServer().getPluginManager().registerEvents(new OnPlayerBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerConsume(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new EventNoteblockUpdate(), this);

        long time = System.currentTimeMillis() - start;
        String completeMessage = String.format("Registered all events successfully! (%dms)", time);
        getLogger().info(completeMessage);
    }
    public void registerItems() {
        long start = System.currentTimeMillis();
        CompileItems.run();
        registerEvent(
                new StoneNeedle()
        );
        long time = System.currentTimeMillis() - start;
        String completeMessage = String.format("Registered all items successfully! (%dms)", time);
        getLogger().info(completeMessage);
    }
    public void registerBlocks() {
        long start = System.currentTimeMillis();
        registerEvent(
                new WoodenSpike(),
                new WoodenConveyor(),
                new StoneCraftingTable(),
                new StoneChest(),
                new CharcoalKiln(),
                new BasicCobblestoneGenerator()
        );
        long time = System.currentTimeMillis() - start;
        String completeMessage = String.format("Registered all items successfully! (%dms)", time);
        getLogger().info(completeMessage);
    }
    private void registerEvent(Listener... listeners) {
        for (Listener listener : listeners) {
            registerEvent(listener);
        }
    }
    private void registerEvent(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
    public void unregisterAllEvents() {
        HandlerList.unregisterAll();
    }

    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            throw new NullPointerException("Can't identify a Chat Hook");
        }
        chat = rsp.getProvider();
    }
    public Map<String, Object> getTempData() {
        return tempData;
    }
    public Map<String, Entity> getTempEntity() {
        return tempEntity;
    }
    public Chat getChat() {
        return chat;
    }
    public LuckPerms getLuckPerms() {
        return luckPerms;
    }
    public FileManager getFileManager() {
        return fileManager;
    }
    public Random getRng() {
        return rng;
    }
    public Map<String, ItemStack> getItems() {
        return items;
    }
    public static AsarSMP getInstance() {
        return instance;
    }
}
