package tedo.AntiSpam;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;

public class AntiSpam extends PluginBase implements Listener{

	private HashMap<String, Integer> chat = new HashMap<String, Integer>();

	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
		getServer().getScheduler().scheduleRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				isKick();
			}
		}, 20 * 15);
	}

	@EventHandler
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		this.chat.put(name, 0);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		if (this.chat.containsKey(name)) {
			this.chat.remove(name);
		}
	}

	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		this.chat.put(name, this.chat.get(name) + 1);
		if (this.chat.get(name) >= 10) {
			player.kick("§cチャットおよびコマンドを打った回数が\n§c一定時間内に10回以上計測されたためkickしました", false);
			event.setCancelled();
		}
	}

	@EventHandler
	public void PlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		this.chat.put(name, this.chat.get(name) + 1);
		if (this.chat.get(name) >= 10) {
			player.kick("§cチャットおよびコマンドを打った回数が\n§c一定時間内に10回以上計測されたためkickしました", false);
			event.setCancelled();
		}
	}

	public void isKick() {
		getServer().getOnlinePlayers().forEach((UUID, player) -> {
			String name = player.getName();
			this.chat.put(name, 0);
		});
	}
}
