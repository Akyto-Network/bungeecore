package kezuk.akyto.listener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import kezuk.akyto.BungeeCore;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerListener implements Listener {
	
	private BungeeCore main;
	
	public ServerListener(final BungeeCore core) { this.main = core; }
	
	@EventHandler
	public void onPing(ProxyPingEvent e) {
	    ServerPing ping = e.getResponse();
	    ServerInfo practice = this.main.getServer().getServerInfo("practice");
		final String barCode = new String("|".getBytes(), StandardCharsets.UTF_8);
	    if (practice.getMotd().contains("whitelisted")) {
	    	e.getResponse().setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes('&', "          &7• &cAkyto &7৷ &4&lNetwork &7•\n&c» &fWe are currently cooking right now...")));
		    ServerPing.Protocol vers = ping.getVersion();
		    vers.setName(ChatColor.RED.toString() + ChatColor.ITALIC + "Whitelisted");
		    vers.setProtocol(9999);	
	    }
	    if (!practice.getMotd().contains("whitelisted")) e.getResponse().setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes('&', "          &7• &cAkyto &7৷ &4&lNetwork &7•\n&c» &fJoin our &f&lV2.0 &fright now!")));
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
		String serverIP = getMachineIP();
		System.out.println("[Bungee] Machine IP: " + serverIP);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		try {
			out.writeUTF("MACHINE_IP");
			out.writeUTF(serverIP);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (ServerInfo server : this.main.getProxy().getServers().values()) {
			server.sendData("BungeeCord", byteStream.toByteArray());
		}
	}

    private boolean isLocalhost(InetSocketAddress address) {
        InetAddress inetAddress = address.getAddress();
        return inetAddress.isLoopbackAddress() || inetAddress.isAnyLocalAddress();
    }

	private String getMachineIP() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				if (iface.isLoopback() || !iface.isUp() || iface.isVirtual()) continue;

				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();
					if (!addr.isLoopbackAddress() && !addr.isAnyLocalAddress() && addr.getHostAddress().indexOf(":") == -1) {
						return addr.getHostAddress(); // IPv4 only
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return "unknown";
	}
}