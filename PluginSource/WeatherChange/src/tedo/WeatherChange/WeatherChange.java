package tedo.WeatherChange;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.level.ThunderChangeEvent;
import cn.nukkit.event.level.WeatherChangeEvent;
import cn.nukkit.plugin.PluginBase;

public class WeatherChange extends PluginBase implements Listener{

	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		event.setCancelled();
	}

	@EventHandler
	public void onThunderChange(ThunderChangeEvent event) {
		event.setCancelled();
	}
}
