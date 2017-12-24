package tedo.ElytraBoost;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerToggleGlideEvent;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class ElytraBoost extends PluginBase implements Listener{

	private Config config;
	private HashMap<String, Boolean> level = new HashMap<String, Boolean>();

	@SuppressWarnings("deprecation")
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getDataFolder().mkdirs();
		this.config = new Config(new File(this.getDataFolder(), "config.yml"),Config.YAML,
			new LinkedHashMap<String, Object>() {
				{
					put("ブーストが使えるワールド", "sigen:seichi");
				}
			}
		);
		String[] datas = ((String) this.config.get("ブーストが使えるワールド")).split(":");
		for (int i = 0; i < datas.length; i++) {
			this.level.put(datas[i], true);
		}
	}

	@EventHandler
	public void onPlayerToggleGlide(PlayerToggleGlideEvent event) {
		Player player = event.getPlayer();
		if (event.isGliding()) {
			String level = player.getLevel().getName();
			if (this.level.containsKey(level)) {
				getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						task(player);
					}
				}, 1);
			}
		}
	}

	public void task(Player player) {
		if (player.isGliding()) {
			String level = player.getLevel().getName();
			if (this.level.containsKey(level)) {
				if (player.pitch <= -20) {
					Vector3 pos = player.getDirectionVector();
					double x = pos.x;
					double y = pos.y;
					double z = pos.z;
					player.setMotion(new Vector3(x * 1.6, y, z * 1.6));
				}
				getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						task(player);
					}
				}, 5);
			}
		}
	}
}
