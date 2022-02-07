package asar_development.asarsmp.events;

import asar_development.asarsmp.AsarSMP;
import asar_development.util.Misc;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(Misc.coloured(String.format("&7[&c&l-&7] &f%s%s%s #242222(%d/%d)", AsarSMP.getInstance().getChat().getPlayerPrefix(e.getPlayer()), e.getPlayer().getDisplayName(), AsarSMP.getInstance().getChat().getPlayerSuffix(e.getPlayer()), AsarSMP.getInstance().getServer().getOnlinePlayers().size() - 1, AsarSMP.getInstance().getServer().getMaxPlayers())));
    }

}
