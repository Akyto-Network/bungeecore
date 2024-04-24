package kezuk.akyto.listener;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import kezuk.akyto.BungeeCore;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerListener implements Listener {
	
	private BungeeCore main;
	
	public ServerListener(final BungeeCore core) { this.main = core; }
	
	@EventHandler
	public void onPing(ProxyPingEvent e) {
	    ServerPing ping = e.getResponse();
	    ServerInfo practicena = this.main.getServer().getServerInfo("practicena");
	    if (practicena.getMotd().contains("whitelisted")) {
	    	e.getResponse().setDescriptionComponent(new TextComponent("              " + ChatColor.GRAY + " • " + ChatColor.DARK_GRAY + ChatColor.BOLD + "Akyto" + ChatColor.GRAY + "│" + ChatColor.WHITE + ChatColor.BOLD + " Network" + ChatColor.GRAY + " • " + ChatColor.RED + ChatColor.BOLD + "NA\n" + ChatColor.RED + " » " + ChatColor.GRAY + "Currently under development, coming soon" + ChatColor.WHITE + "..."));
		    ServerPing.Protocol vers = ping.getVersion();
		    vers.setName(ChatColor.GOLD.toString() + ChatColor.ITALIC + "Whitelisted");
		    vers.setProtocol(9999);	
	    }
	    if (!practicena.getMotd().contains("whitelisted")) e.getResponse().setDescriptionComponent(new TextComponent("            " + ChatColor.GRAY + " • " + ChatColor.DARK_GRAY + ChatColor.BOLD + "Akyto" + ChatColor.GRAY + "│" + ChatColor.WHITE + ChatColor.BOLD + " Network" + ChatColor.GRAY + " • " + ChatColor.RED + ChatColor.BOLD + "NA\n" + ChatColor.RED + " » " + ChatColor.GRAY + "The return of the real fight is " + ChatColor.WHITE + "here!"));
	    e.setResponse(ping);
	}
	
    @EventHandler
    public void onPlayerPreLogin(PreLoginEvent event) {
        PendingConnection connection = event.getConnection();
        InetSocketAddress address = connection.getAddress();
        if (isLocalhost(address)) {
            event.setCancelled(true);
            event.setCancelReason("You cannot connected on Akyto with your localhost please connect with akyto.club");
        }
    }

    private boolean isLocalhost(InetSocketAddress address) {
        InetAddress inetAddress = address.getAddress();
        return inetAddress.isLoopbackAddress() || inetAddress.isAnyLocalAddress();
    }
}