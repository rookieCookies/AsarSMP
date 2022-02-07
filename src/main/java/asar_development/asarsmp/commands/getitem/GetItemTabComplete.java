package asar_development.asarsmp.commands.getitem;

import asar_development.asarsmp.AsarSMP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetItemTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            for (var i = 0; i < 9; i++) {
                list.add(String.valueOf(i + 1));
            }
        }
        if (args.length > 1) {
            return list;
        }
        list.add("*");
        Map<String, ItemStack> itemsFile = AsarSMP.getInstance().getItems();
        for (String a : itemsFile.keySet()) {
            if (!"".equals(args[0]) && !a.contains(args[0])) {
                continue;
            }
            list.add(a);
        }
        return list;
    }
}
