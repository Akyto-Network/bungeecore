package kezuk.akyto;

import kezuk.akyto.listener.ServerListener;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class BungeeCore extends Plugin {
	
	private ProxyServer server;
	
	@Override
	public void onEnable() {
	    this.server = getProxy();
	    getProxy().getPluginManager().registerListener(this, new ServerListener(this));
	}

}
