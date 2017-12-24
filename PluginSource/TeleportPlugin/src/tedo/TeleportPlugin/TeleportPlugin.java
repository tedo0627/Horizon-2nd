package tedo.TeleportPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class TeleportPlugin extends PluginBase implements Listener{

	private Config config, home, warp;
	@SuppressWarnings("rawtypes")
	private HashMap<String, HashMap> homedata = new HashMap<String, HashMap>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, HashMap> warpdata = new HashMap<String, HashMap>();

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getDataFolder().mkdirs();
		this.config = new Config(new File(this.getDataFolder(), "config.yml"),Config.YAML,
		new LinkedHashMap<String, Object>() {
			{
				put("読み込むワールド", "world:seichi");
			}
		}
		);
		this.warp = new Config(new File(this.getDataFolder(), "warp.yml"),Config.YAML);
		this.home = new Config(new File(this.getDataFolder(), "home.yml"),Config.YAML);
		String level = (String) this.config.get("読み込むワールド");
		String[]levels = level.split(":");
		for (int i = 0; i < levels.length; i++) {
			getServer().loadLevel(levels[i]);
		}
		this.home.getAll().forEach((name, data) -> this.homedata.put(name, (HashMap) data));
		this.warp.getAll().forEach((name, data) -> this.warpdata.put(name, (HashMap) data));
	}

	public void onDisable() {
		this.homedata.forEach((name, data) -> this.home.set(name, data));
		this.home.save();
		this.warpdata.forEach((name, data) -> this.warp.set(name, data));
		this.warp.save();
	}

	@SuppressWarnings("rawtypes")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		if (!(this.homedata.containsKey(name))) {
			Position p = getServer().getDefaultLevel().getSafeSpawn();
			String x = String.valueOf(p.x);
			String y = String.valueOf(p.y);
			String z = String.valueOf(p.z);
			String level = p.getLevel().getName();
			HashMap<String, HashMap> data = new HashMap<String, HashMap>();
			HashMap<String, String> pos = new HashMap<String, String>();
			pos.put("x", x);
			pos.put("y", y);
			pos.put("z", z);
			pos.put("level", level);
			data.put("main", pos);
			data.put("sub1", pos);
			data.put("sub2", pos);
			data.put("sub3", pos);
			this.homedata.put(name, data);
		}
	}

	@SuppressWarnings("rawtypes")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Block block = event.getBlock();
		if (block.getId() == 63 || block.getId() == 68) {
			double x = block.x;
			double y = block.y;
			double z = block.z;
			Level level = block.getLevel();
			BlockEntity sign = level.getBlockEntity(new Vector3(x, y, z));
			if (sign instanceof BlockEntitySign) {
				String[] text = ((BlockEntitySign) sign).getText();
				Player player = event.getPlayer();
				if (text[0].equals("[wtp]")) {
					try {
						if (text[1] != null && !(text[1].equals(""))) {
							Level l = getServer().getLevelByName(text[1]);
							if (l != null) {
								player.teleport(l.getSafeSpawn());
								player.sendPopup("§bテレポートしました");
								event.setCancelled();
							}else{
								if (getServer().loadLevel(text[1])) {
									l = getServer().getLevelByName(text[1]);
									player.teleport(l.getSafeSpawn());
									player.sendPopup("§bテレポートしました");
									event.setCancelled();
								}else{
									player.sendPopup("§bこのワールドは存在しません");
									event.setCancelled();
								}
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e){

					}
				}else if (text[0].equals("[warp]")) {
					try {
						if (text[1] != null) {
							if (this.warpdata.containsKey(text[1])) {
								HashMap data = this.warpdata.get(text[1]);
								Level l = getServer().getLevelByName((String) data.get("level"));
								player.teleport(new Position(Double.valueOf( (String) data.get("x")), Double.valueOf( (String) data.get("y")), Double.valueOf( (String) data.get("z")), l));
								player.sendPopup("§bテレポートしました");
								event.setCancelled();
							}else{
								player.sendPopup("§bそのワープポイントは存在しません");
								event.setCancelled();
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e){

					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		switch(command.getName()){
			case "warp":
				if (sender instanceof Player) {
					try{
						if(args[0] != null){
							switch (args[0]) {
								case "add":
									if (sender.isOp()) {
										try{
											if (args[1] != null) {
												Player player = (Player) sender;
												String x = String.valueOf(player.x);
												String y = String.valueOf(player.y);
												String z = String.valueOf(player.z);
												String level = player.getLevel().getName();
												HashMap<String, String> data = new HashMap<String, String>();
												data.put("x", x);
												data.put("y", y);
												data.put("z", z);
												data.put("level", level);
												this.warpdata.put(args[1], data);
												sender.sendMessage("§a>>§bワープポイントを作成しました");
											}
										}
										catch(ArrayIndexOutOfBoundsException e){
											sender.sendMessage("§a>>§b/warp add [名前]");
										}
									}else{
										sender.sendMessage("§a>>§b貴方はこのコマンドを使えません");
									}
								break;

								case "del":
									if (sender.isOp()) {
										try{
											if (args[1] != null) {
												if (this.warpdata.containsKey(args[1])) {
													this.warpdata.remove(args[1]);
													if (this.warp.exists(args[1])) {
														this.warp.remove(args[1]);
														this.warp.save();
													}
													sender.sendMessage("§a>>§bワープポイントを削除しました");
												}else{
													sender.sendMessage("§a>>§bそのワープポイントは存在しません");
												}
											}
										}
										catch(ArrayIndexOutOfBoundsException e){
											sender.sendMessage("§a>>§b/warp del [名前]");
										}
									}else{
										sender.sendMessage("§a>>§b貴方はこのコマンドを使えません");
									}
								break;

								case "list":
									sender.sendMessage("§a>>§bワープリスト");
									this.warpdata.forEach((name, data) -> sender.sendMessage("§a>>§b" + name + " | x : " + data.get("x") + " y : " + data.get("y") + " z : " + data.get("z") + " World : " + data.get("level")));
								break;

								default:
									if (this.warpdata.containsKey(args[0])) {
										HashMap data = this.warpdata.get(args[0]);
										Level l = getServer().getLevelByName((String) data.get("level"));
										((Entity) sender).teleport(new Position(Double.valueOf( (String) data.get("x")), Double.valueOf( (String) data.get("y")), Double.valueOf( (String) data.get("z")), l));
										((Player) sender).sendPopup("§bテレポートしました");
									}else{
										sender.sendMessage("§a>>§b/warp [名前]     | ワープポイントにワープします");
										if (sender.isOp()) {
											sender.sendMessage("§a>>§b/warp add [名前] | ワープポイントを追加します");
											sender.sendMessage("§a>>§b/warp del [名前] | ワープポイントを削除します");
										}
										sender.sendMessage("§a>>§b/warp list       | ワープポイントのリストを表示します");
									}
								break;
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e){
						sender.sendMessage("§a>>§b/warp [名前]     | ワープポイントにワープします");
						if (sender.isOp()) {
							sender.sendMessage("§a>>§b/warp add [名前] | ワープポイントを追加します");
							sender.sendMessage("§a>>§b/warp del [名前] | ワープポイントを削除します");
						}
						sender.sendMessage("§a>>§b/warp list       | ワープポイントのリストを表示します");
					}
				}else{
					sender.sendMessage("§a>>§bこのコマンドはコンソールから実行できません");
				}
			break;

			case "alltp":
				if (sender instanceof Player) {
					Player player = (Player) sender;
					double x = player.x;
					double y = player.y;
					double z = player.z;
					Level level = player.getLevel();
					getServer().getOnlinePlayers().forEach((UUID, p) -> p.teleport(new Position(x, y, z, level)));
					((Player) sender).sendPopup("§bワープしました");
				}else{
					sender.sendMessage("§a>>§bこのコマンドはコンソールから実行できません");
				}
			break;

			case "wtp":
				if (sender instanceof Player) {
					try{
						if (args[0] != null) {
							Level level = getServer().getLevelByName(args[0]);
							if (level != null) {
								((Player) sender).teleport(level.getSafeSpawn());
								((Player) sender).sendPopup("§bワープしました");
							}else{
								sender.sendMessage("§a>>§bそのワールドは存在しません");
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e){
						sender.sendMessage("§a>>§b/wtp [ワールド名]");
					}
				}else{
					sender.sendMessage("§a>>§bこのコマンドはコンソールから実行できません");
				}
			break;

			case "wlist":
				sender.sendMessage("§a>>§bワールドリスト");
				getServer().getLevels().forEach((i, level) -> sender.sendMessage("§a>>§b" + level.getName()));
			break;

			case "spawn":
				if (sender instanceof Player) {
					((Entity) sender).teleport(((Position) sender).getLevel().getSafeSpawn());
					((Player) sender).sendPopup("§bワープしました");
				}else{
					sender.sendMessage("§a>>§bこのコマンドはコンソールから実行できません");
				}
			break;

			case "hub":
				if (sender instanceof Player) {
					((Entity) sender).teleport(getServer().getDefaultLevel().getSafeSpawn());
					((Player) sender).sendPopup("§bワープしました");
				}else{
					sender.sendMessage("§a>>§bこのコマンドはコンソールから実行できません");
				}
			break;

			case "generate":
				try {
					if (args[0] != null) {
						getServer().generateLevel(args[0]);
						sender.sendMessage("§a>>§bワールドを作成しました");
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/generate [ワールド名]");
				}
			break;

			case "home":
				try {
					if (args[0] != null) {
						String name = sender.getName().toLowerCase();
						HashMap data = this.homedata.get(name);
						Player player = (Player) sender;
						String x = String.valueOf(player.x);
						String y = String.valueOf(player.y);
						String z = String.valueOf(player.z);
						String level = player.getLevel().getName();
						HashMap<String, String> pos = new HashMap<String, String>();
						HashMap po;
						Level le;
						switch (args[0]) {
							case "set":
								pos.put("x", x);
								pos.put("y", y);
								pos.put("z", z);
								pos.put("level", level);
								data.put("main", pos);
								this.homedata.put(name, data);
								player.sendMessage("§a>>§bホームポイントをセットしました");
							break;

							case "warp":
								po = (HashMap) data.get("main");
								le = getServer().getLevelByName((String) po.get("level"));
								player.teleport(new Position(Double.valueOf( (String) po.get("x")), Double.valueOf( (String) po.get("y")), Double.valueOf( (String) po.get("z")), le));
								player.sendPopup("§bテレポートしました");
							break;

							case "sub1":
								try {
									if (args[1] != null) {
										switch (args[1]) {
											case "set":
												pos.put("x", x);
												pos.put("y", y);
												pos.put("z", z);
												pos.put("level", level);
												data.put("sub1", pos);
												this.homedata.put(name, data);
												player.sendMessage("§a>>§bサブホームポイント1をセットしました");
											break;

											case "warp":
												po = (HashMap) data.get("sub1");
												le = getServer().getLevelByName((String) po.get("level"));
												player.teleport(new Position(Double.valueOf( (String) po.get("x")), Double.valueOf( (String) po.get("y")), Double.valueOf( (String) po.get("z")), le));
												player.sendPopup("§bテレポートしました");
											break;

											default:
												sender.sendMessage("§a>>§b/home sub1 set");
												sender.sendMessage("§a>>§b/home sub1 warp");
											break;
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/home sub1 set");
									sender.sendMessage("§a>>§b/home sub1 warp");
								}
							break;

							case "sub2":
								try {
									if (args[1] != null) {
										switch (args[1]) {
											case "set":
												pos.put("x", x);
												pos.put("y", y);
												pos.put("z", z);
												pos.put("level", level);
												data.put("sub2", pos);
												this.homedata.put(name, data);
												player.sendMessage("§a>>§bサブホームポイント2をセットしました");
											break;

											case "warp":
												po = (HashMap) data.get("sub2");
												le = getServer().getLevelByName((String) po.get("level"));
												player.teleport(new Position(Double.valueOf( (String) po.get("x")), Double.valueOf( (String) po.get("y")), Double.valueOf( (String) po.get("z")), le));
												player.sendPopup("§bテレポートしました");
											break;

											default:
												sender.sendMessage("§a>>§b/home sub2 set");
												sender.sendMessage("§a>>§b/home sub2 warp");
											break;
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/home sub2 set");
									sender.sendMessage("§a>>§b/home sub2 warp");
								}
							break;

							case "sub3":
								try {
									if (args[1] != null) {
										switch (args[1]) {
											case "set":
												pos.put("x", x);
												pos.put("y", y);
												pos.put("z", z);
												pos.put("level", level);
												data.put("sub3", pos);
												this.homedata.put(name, data);
												player.sendMessage("§a>>§bサブホームポイント3をセットしました");
											break;

											case "warp":
												po = (HashMap) data.get("sub3");
												le = getServer().getLevelByName((String) po.get("level"));
												player.teleport(new Position(Double.valueOf( (String) po.get("x")), Double.valueOf( (String) po.get("y")), Double.valueOf( (String) po.get("z")), le));
												player.sendPopup("§bテレポートしました");
											break;

											default:
												sender.sendMessage("§a>>§b/home sub3 set");
												sender.sendMessage("§a>>§b/home sub3 warp");
											break;
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/home sub3 set");
									sender.sendMessage("§a>>§b/home sub3 warp");
								}
							break;

							default:
								sender.sendMessage("§a>>§b/home set       | メインホームポイントを設定します");
								sender.sendMessage("§a>>§b/home sub1 set  | サブホームポイント1を設定します");
								sender.sendMessage("§a>>§b/home sub2 set  | サブホームポイント2を設定します");
								sender.sendMessage("§a>>§b/home sub3 set  | サブホームポイント3を設定します");
								sender.sendMessage("§a>>§b/home warp      | メインホームポイントにテレポートします");
								sender.sendMessage("§a>>§b/home sub1 warp | サブホームポイント1にテレポートします");
								sender.sendMessage("§a>>§b/home sub2 warp | サブホームポイント2にテレポートします");
								sender.sendMessage("§a>>§b/home sub3 warp | サブホームポイント3にテレポートします");
							break;
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/home set       | メインホームポイントを設定します");
					sender.sendMessage("§a>>§b/home sub1 set  | サブホームポイント1を設定します");
					sender.sendMessage("§a>>§b/home sub2 set  | サブホームポイント2を設定します");
					sender.sendMessage("§a>>§b/home sub3 set  | サブホームポイント3を設定します");
					sender.sendMessage("§a>>§b/home warp      | メインホームポイントにテレポートします");
					sender.sendMessage("§a>>§b/home sub1 warp | サブホームポイント1にテレポートします");
					sender.sendMessage("§a>>§b/home sub2 warp | サブホームポイント2にテレポートします");
					sender.sendMessage("§a>>§b/home sub3 warp | サブホームポイント3にテレポートします");
				}
			break;
		}
		return false;
	}
}
