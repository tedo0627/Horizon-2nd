package tedo.AutoServerRestart;

import java.io.File;
import java.util.LinkedHashMap;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class AutoServerRestart extends PluginBase{

	private Config config;
	private int time;

	@SuppressWarnings("deprecation")
	public void onEnable() {
		this.getDataFolder().mkdirs();
		this.config = new Config(new File(this.getDataFolder(), "config.yml"),Config.YAML,
			new LinkedHashMap<String, Object>() {
				{
					put("再起動までの時間(秒)", 3600);
				}
			}
		);
		this.time = this.config.getInt("再起動までの時間(秒)");
		getServer().getScheduler().scheduleRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				task();
			}
		}, 20);
	}

	public void task() {
		if (this.time == 0) {
			getServer().getOnlinePlayers().forEach((UUID, player) -> {
				player.kick("§bサーバーが再起動します", false);
			});
			getServer().shutdown();
		}else if (this.time % 300 == 0) {
			getServer().broadcastMessage("§a>>§b再起動まで残り" + String.valueOf(this.time / 60) + "分");
		}else if (this.time == 60) {
			getServer().broadcastMessage("§a>>§b再起動まで残り1分");
		}else if (this.time == 30) {
			getServer().broadcastMessage("§a>>§b再起動まで残り30秒");
		}else if (this.time == 10) {
			getServer().broadcastMessage("§a>>§b再起動まで残り10秒");
		}else if (this.time == 5) {
			getServer().broadcastMessage("§a>>§b再起動まで残り5秒");
		}else if (this.time == 4) {
			getServer().broadcastMessage("§a>>§b再起動まで残り4秒");
		}else if (this.time == 3) {
			getServer().broadcastMessage("§a>>§b再起動まで残り3秒");
		}else if (this.time == 2) {
			getServer().broadcastMessage("§a>>§b再起動まで残り2秒");
		}else if (this.time == 1) {
			getServer().broadcastMessage("§a>>§b再起動まで残り1秒");
		}
		this.time--;
	}
}
