package tedo.KeepInventory;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.plugin.PluginBase;

public class KeepInventory extends PluginBase implements Listener{

	public void onEnable() {
		this.getServer().getPluginManager().registerEvents((Listener) this, this);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setKeepInventory(true);
	}
}
