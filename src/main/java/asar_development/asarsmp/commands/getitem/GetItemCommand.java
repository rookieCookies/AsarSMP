package asar_development.asarsmp.commands.getitem;

import asar_development.asarsmp.AsarSMP;
import asar_development.util.Misc;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckForNull;
import java.util.Map;

public class GetItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Misc.getMessage("insufficient_arguments"));
            return false;
        }
        var loop = 1;
        if (args.length == 2 && !"0".equals(args[1])) {
            loop = Integer.parseInt(args[1]);
        }
        var player = (Player) sender;
        if ("*".equals(args[0])) {
            Map<String, ItemStack> itemsFile = AsarSMP.getInstance().getItems();
            for (ItemStack item : itemsFile.values()) {
                item.setAmount(loop);
                player.getInventory().addItem(item);
            }
            return true;
        }
        @CheckForNull
        ItemStack item = AsarSMP.getInstance().getItems().get(args[0]);
        if (item == null) {
            sender.sendMessage(Misc.getMessage("invalid_arguments"));
            return false;
        }
        item.setAmount(loop);
        player.getInventory().addItem(item);
        return true;
    }
}
