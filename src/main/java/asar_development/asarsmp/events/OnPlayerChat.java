package asar_development.asarsmp.events;

import asar_development.asarsmp.AsarSMP;
import asar_development.util.Misc;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnPlayerChat implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        String finalMessage = Misc.coloured(String.format("&7 - %s%s%s&f: ", AsarSMP.getInstance().getChat().getPlayerPrefix(e.getPlayer()), e.getPlayer().getDisplayName(), AsarSMP.getInstance().getChat().getPlayerSuffix(e.getPlayer())));
        finalMessage += e.getPlayer().hasPermission("server.chat.coloured") ? Misc.coloured(e.getMessage()) : e.getMessage();
        finalMessage = Misc.addEmoji(e.getPlayer(), finalMessage);
        Bukkit.broadcastMessage(finalMessage);
    }
}
