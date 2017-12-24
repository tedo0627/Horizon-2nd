package tedo.setMaxPlayers;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.plugin.PluginBase;

public class setMaxPlayers extends PluginBase implements Listener{

	public HashMap<String, Boolean> data = new HashMap<String, Boolean>();
	public HashMap<String, Boolean> ops = new HashMap<String, Boolean>();

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getOps().getAll().forEach((name, t) -> {
			ops.put(name, true);
		});
	}

	@EventHandler
	public void onDataPacketReceive(DataPacketReceiveEvent event) {
		DataPacket pk = event.getPacket();
		if (pk instanceof LoginPacket) {
			String name = ((LoginPacket) pk).username.toLowerCase();
			if (!this.ops.containsKey(name)) {
				if (getServer().getOnlinePlayers().size() >= 30) {
					if (!this.data.containsKey(name)) {
						this.data.put(name, true);
						getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
							@Override
							public void run() {
								resetTask(name);
							}
						}, 20 * 60);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		if (this.data.containsKey(name)) {
			if (this.data.get(name)) {
				this.data.put(name, false);
				player.kick("§aサーバーの人数が最大です(30人)\n§b1分間入れても入れなくなりました", false);
			}else{
				player.kick("§a貴方はまだサーバーに入ることができません", false);
			}
		}
	}

	public void resetTask(String name) {
		this.data.remove(name);
	}
}
