package tedo.EconomySystemJob;

import java.io.File;
import java.util.LinkedHashMap;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import tedo.EconomySystemAPI.EconomySystemAPI;

public class EconomySystemJob extends PluginBase implements Listener{

	public long b, p;
	public Config config;
	public EconomySystemAPI economy;

	@SuppressWarnings("deprecation")
	public void onEnable(){
		this.economy = (EconomySystemAPI) getServer().getPluginManager().getPlugin("EconomySystemAPI");
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getDataFolder().mkdirs();
		this.config = new Config(new File(this.getDataFolder(), "confg.yml"),Config.YAML,
			new LinkedHashMap<String, Object>() {
				{
					put("ブロックを破壊したときに渡す金額", 10);
					put("ブロックを設置したときに渡す金額", 10);
				}
			}
		);
		int b = (int) this.config.get("ブロックを破壊したときに渡す金額");
		int p = (int) this.config.get("ブロックを設置したときに渡す金額");
		this.b = (long) b;
		this.p = (long) p;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		this.economy.addMoney(name, this.b);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		this.economy.addMoney(name, this.p);
	}
}
