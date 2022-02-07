package asar_development.asarsmp.commands.server;

import asar_development.asarsmp.AsarSMP;
import asar_development.asarsmp.registerers.RegisterRecipes;
import asar_development.util.Misc;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ServerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            ((Player) sender).performCommand("/server help");
            return false;
        }
        switch (args[0]) {
            case "help" -> commandHelp(sender);
            case "reload" -> commandReload(sender);
            case "save" -> commandSave(sender);
            default -> ((Player) sender).performCommand("/server help");
        }
        AsarSMP.getInstance().reloadConfig();
        return true;
    }
    private static void commandHelp(CommandSender sender) {
        sender.sendMessage(Misc.coloured("&C&LCOMING SOON!"));
    }
    private static void commandReload(CommandSender sender) {
        sender.sendMessage(Misc.coloured("&aReloading config..."));
        AsarSMP.getInstance().reloadConfig();
        sender.sendMessage(Misc.coloured("&aRe-assigning broadcasts..."));
        AsarSMP.getInstance().registerTimedBroadcast();
        sender.sendMessage(Misc.coloured("&aRematching emojis..."));
        Misc.reloadEmojis();
        sender.sendMessage(Misc.coloured("&aUnregistering all events..."));
        AsarSMP.getInstance().unregisterAllEvents();
        sender.sendMessage(Misc.coloured("&aUnregistering events..."));
        AsarSMP.getInstance().registerEvents();
        sender.sendMessage(Misc.coloured("&aRe-registering items..."));
        AsarSMP.getInstance().registerItems();
        sender.sendMessage(Misc.coloured("&aRe-registering blocks..."));
        AsarSMP.getInstance().registerBlocks();
        sender.sendMessage(Misc.coloured("&aRe-registering commands..."));
        AsarSMP.getInstance().registerCommands();
        sender.sendMessage(Misc.coloured("&aRe-registering recipes..."));
        new RegisterRecipes();
        sender.sendMessage(Misc.coloured("&aSuccessfully reloaded many features, some features require a restart to function correctly!"));
    }
    private static void commandSave(CommandSender sender) {
        sender.sendMessage(Misc.coloured("&aSaving all item data disk..."));
        AsarSMP.getInstance().getFileManager().saveAll();
    }
}
