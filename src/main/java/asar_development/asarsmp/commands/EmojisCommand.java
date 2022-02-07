package asar_development.asarsmp.commands;

import asar_development.util.Config;
import asar_development.util.Misc;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmojisCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        int page = args.length != 0 && !"0".equals(args[0]) ? Integer.parseInt(args[0]) : 1;
        int pageSize = Config.getInt("commands.emojis.page_size");
        List<String> emojis = Config.getStringList("emojis");
        sender.sendMessage("");
        if ((page - 1) * pageSize > emojis.size()) {
            page = (int) Math.ceil((double) emojis.size() / pageSize);
        }
        int maxPages = (int) Math.ceil((double) emojis.size() / pageSize);
        String topLine = String.format("#e6a4ed-------------- &d%d/%d #e6a4ed--------------", page, maxPages);
        sender.sendMessage(Misc.coloured(topLine));
        for (int i = (page - 1) * pageSize; i < emojis.size() && i < page * pageSize; i++) {
            String s = emojis.get(i);
            int o = s.indexOf(" | ", s.indexOf(" | ") + 3);
            String replacement = s.substring(0, s.indexOf(" | "));
            String syntax = s.substring(s.indexOf(" | ") + 3, o);
            String message = String.format("#eb99f2 - [&f%s#eb99f2] | #f5a2f5%s", replacement, syntax);
            sender.sendMessage(Misc.coloured(message));
        }
        TextComponent previousPage = new TextComponent(Misc.coloured("#e6a4ed←-------------"));
        ClickEvent previousPageClickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/emojis " + (page - 1));
        previousPage.setClickEvent(previousPageClickEvent);

        Text previousPageHoverText = new Text(Misc.coloured("&dPrevious Page"));
        HoverEvent previousPageHoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, previousPageHoverText);
        previousPage.setHoverEvent(previousPageHoverEvent);

        TextComponent nextPage = new TextComponent(Misc.coloured("#e6a4ed-------------→"));
        ClickEvent nextPageClickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/emojis " + (page + 1));
        nextPage.setClickEvent(nextPageClickEvent);

        Text nextPageHoverText = new Text(Misc.coloured("&dNext Page"));
        HoverEvent nextPageHoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, nextPageHoverText);
        nextPage.setHoverEvent(nextPageHoverEvent);

        ComponentBuilder builder = new ComponentBuilder();

        builder.append(previousPage);
        builder.append(Misc.coloured(String.format(" &e%d/%d ", page, maxPages)));
        builder.append(nextPage);
        sender.spigot().sendMessage(builder.create());

        return true;
    }
}
