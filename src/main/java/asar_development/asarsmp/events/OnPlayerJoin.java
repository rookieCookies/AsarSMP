package asar_development.asarsmp.events;

import asar_development.asarsmp.AsarSMP;
import asar_development.util.Misc;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(Misc.coloured(String.format("&7[&a&l+&7] &f%s%s%s #242222(%d/%d)", AsarSMP.getInstance().getChat().getPlayerPrefix(e.getPlayer()), e.getPlayer().getDisplayName(), AsarSMP.getInstance().getChat().getPlayerSuffix(e.getPlayer()), AsarSMP.getInstance().getServer().getOnlinePlayers().size(), AsarSMP.getInstance().getServer().getMaxPlayers())));
    }
}
