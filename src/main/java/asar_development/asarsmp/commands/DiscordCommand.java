package asar_development.asarsmp.commands;

import asar_development.util.Config;
import asar_development.util.Misc;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DiscordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        TextComponent bars = parse("&9------------------------------");
        TextComponent empty = parse("&9                                               ");
        TextComponent discordMessage = parse("&3 Our discord server                    ");
        sender.spigot().sendMessage(bars);
        sender.spigot().sendMessage(empty);
        sender.spigot().sendMessage(discordMessage);
        sender.spigot().sendMessage(empty);
        sender.spigot().sendMessage(bars);
        return true;
    }
    TextComponent parse(String msg) {
        TextComponent i = new TextComponent(Misc.coloured(msg));
        String discordLink = Config.getString("commands.discord.link");
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, discordLink);

        String discordHover = Config.getString("commands.discord.hover");
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(discordHover));

        i.setHoverEvent(hoverEvent);
        i.setClickEvent(clickEvent);
        return i;
    }
}
