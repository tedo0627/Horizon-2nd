package tedo.AreaProtect;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class AreaProtect extends PluginBase implements Listener{

	private Config data, config;
	private HashMap<String, Area> datas = new HashMap<String, Area>();

	private HashMap<String, Integer[]> pos1 = new HashMap<String, Integer[]>();
	private HashMap<String, Integer[]> pos2 = new HashMap<String, Integer[]>();

	private HashMap<String, Boolean> level = new HashMap<String, Boolean>();

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getDataFolder().mkdirs();
		this.data = new Config(new File(this.getDataFolder(), "data.yml"),Config.YAML);
		this.config = new Config(new File(this.getDataFolder(), "confg.yml"),Config.YAML,
			new LinkedHashMap<String, Object>() {
				{
					put("ブロックを編集できないワールド", "tsts:pvp");
				}
			}
		);
		this.data.getAll().forEach((name, data) -> {
			addArea(name, (HashMap) data);
		});
		String d = (String) this.config.get("ブロックを編集できないワールド");
		String[] datas = d.split(":");
		for (int i = 0; i < datas.length; i++) {
			this.level.put(datas[i], true);
		}
	}

	public void onDisable() {
		this.datas.forEach((name, data) ->{
			HashMap<String, String> d = new HashMap<String, String>();
			d.put("level", data.level);
			d.put("x1", String.valueOf(data.x1));
			d.put("y1", String.valueOf(data.y1));
			d.put("z1", String.valueOf(data.z1));
			d.put("x2", String.valueOf(data.x2));
			d.put("y2", String.valueOf(data.y2));
			d.put("z2", String.valueOf(data.z2));
			this.data.set(name, d);
		});
		this.data.save();
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		String level = player.getLevel().getName();
		if (!(this.level.containsKey(level) && !(player.isOp()))) {
			Block block = event.getBlock();
			int x = (int) block.x;
			int y = (int) block.y;
			int z = (int) block.z;
			if (player.getInventory().getItemInHand().getId() == 294) {
				if (player.isOp()) {
					String name = player.getName().toLowerCase();
					Integer[] i =  new Integer[3];
					i[0] = x;
					i[1] = y;
					i[2] = z;
					this.pos1.put(name, i);
					player.sendMessage("§a>>§b一つ目の地点を決めました");
					event.setCancelled();
				}
			}
			if (!(event.isCancelled())) {
				if (!(player.isOp())) {
					this.datas.forEach((name, area) -> {
						if (area.isPosition(x, y, z, level)) {
							event.setCancelled();
						}
					});
				}
			}
		}else{
			event.setCancelled();
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		String level = player.getLevel().getName();
		if (!(this.level.containsKey(level) && !(player.isOp()))) {
			Block block = event.getBlock();
			int x = (int) block.x;
			int y = (int) block.y;
			int z = (int) block.z;
			if (player.getInventory().getItemInHand().getId() == 294) {
				if (player.isOp()) {
					String name = player.getName().toLowerCase();
					Integer[] i =  new Integer[3];
					i[0] = x;
					i[1] = y;
					i[2] = z;
					this.pos2.put(name, i);
					player.sendMessage("§a>>§b二つ目の地点を決めました");
					event.setCancelled();
				}
			}
			if (!(player.isOp())) {
				this.datas.forEach((name, area) -> {
					if (area.isPosition(x, y, z, level)) {
						event.setCancelled();
					}
				});
			}
		}else{
			event.setCancelled();
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		String level = player.getLevel().getName();
		if (!(this.level.containsKey(level) && !(player.isOp()))) {
			Block block = event.getBlock();
			int x = (int) block.x;
			int y = (int) block.y;
			int z = (int) block.z;
			if (!(event.isCancelled())) {
				if (!(player.isOp())) {
					this.datas.forEach((name, area) -> {
						if (area.isPosition(x, y, z, level)) {
							event.setCancelled();
						}
					});
				}
			}
		}else{
			event.setCancelled();
		}
	}

	@SuppressWarnings("rawtypes")
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		switch(command.getName()){
			case "area":
				try{
					String name = sender.getName().toLowerCase();
					if(args[0] != null){
						switch (args[0]) {
							case "set":
								try {
									if (args[1] != null) {
										if (this.pos1.containsKey(name)) {
											if (this.pos2.containsKey(name)) {
												HashMap data = preparePosition((Player) sender);
												addArea(args[1], data);
												sender.sendMessage("§a>>§b保護エリアを作成しました");
											}else{
												sender.sendMessage("§a>>§b二つ目の地点を決めてください");
											}
										}else{
											sender.sendMessage("§a>>§b一つ目の地点を決めてください");
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/area set [名前]");
								}
							break;

							case "del":
								try {
									if (args[1] != null) {
										if (isArea(args[1])) {
											delArea(args[1]);
											sender.sendMessage("§a>>§b保護エリアを削除しました");
										}else{
											sender.sendMessage("§a>>§bそのエリアは存在しません");
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/area del [名前]");
								}
							break;

							case "list":
								sender.sendMessage("§a>>§b保護エリアリスト");
								this.datas.forEach((n, area) -> sender.sendMessage("§a>>§b" + n + " | 一つ目の座標 | x : " + String.valueOf(area.x1) + " y : " + String.valueOf(area.y1) + " z : " + String.valueOf(area.z1) + " | 二つ目の座標 | x : " + String.valueOf(area.x2) + " y : " + String.valueOf(area.y2) + " z : " + String.valueOf(area.z2)));
							break;

							default:
								sender.sendMessage("§a>>§b/area set [名前] | 保護エリアを設定します");
								sender.sendMessage("§a>>§b/area del [名前] | 保護エリアを削除します");
								sender.sendMessage("§a>>§b/area list       | 保護エリアのリストを表示します");
							break;
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/area set [名前] | 保護エリアを設定します");
					sender.sendMessage("§a>>§b/area del [名前] | 保護エリアを削除します");
					sender.sendMessage("§a>>§b/area list       | 保護エリアのリストを表示します");
				}
			break;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public Area getArea(HashMap data) {
		return new Area(data);
	}

	public boolean isArea(String name) {
		if (this.datas.containsKey(name)) {
			return true;
		}else{
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	public void addArea(String name, HashMap data) {
		Area area = new Area(data);
		this.datas.put(name, area);
	}

	public void delArea(String name) {
		this.datas.remove(name);
		if (this.data.exists(name)) {
			this.config.remove(name);
			this.config.save();
		}
	}

	@SuppressWarnings("rawtypes")
	public HashMap preparePosition(Player player) {
		String name = player.getName().toLowerCase();
		String level = player.getLevel().getName();
		Integer[] pos1 = this.pos1.get(name);
		Integer[] pos2 = this.pos2.get(name);
		int x1 = pos1[0];
		int y1 = pos1[1];
		int z1 = pos1[2];
		int x2 = pos2[0];
		int y2 = pos2[1];
		int z2 = pos2[2];
		int ts;
		if (x1 < x2) {
			ts = x1;
			x1 = x2;
			x2 = ts;
		}
		if (y1 < y2) {
			ts = y1;
			y1 = y2;
			y2 = ts;
		}
		if (z1 < z2) {
			ts = z1;
			z1 = z2;
			z2 = ts;
		}
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("level", level);
		data.put("x1", String.valueOf(x1));
		data.put("y1", String.valueOf(y1));
		data.put("z1", String.valueOf(z1));
		data.put("x2", String.valueOf(x2));
		data.put("y2", String.valueOf(y2));
		data.put("z2", String.valueOf(z2));
		return data;
	}
}
