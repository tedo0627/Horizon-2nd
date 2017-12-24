package tedo.SeichiSystemPlugin;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.entity.EntitySpawnEvent;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBow;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.SetPlayerGameTypePacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Config;
import tedo.GatyaSystem.GatyaSystem;
import tedo.ItemStorage.ItemStorage;
import tedo.SeichiSystemPlugin.skill.skill1;
import tedo.SeichiSystemPlugin.skill.skill10;
import tedo.SeichiSystemPlugin.skill.skill2;
import tedo.SeichiSystemPlugin.skill.skill3;
import tedo.SeichiSystemPlugin.skill.skill4;
import tedo.SeichiSystemPlugin.skill.skill5;
import tedo.SeichiSystemPlugin.skill.skill6;
import tedo.SeichiSystemPlugin.skill.skill7;
import tedo.SeichiSystemPlugin.skill.skill8;
import tedo.SeichiSystemPlugin.skill.skill9;


public class SeichiSystemPlugin extends PluginBase implements Listener{

	public Config config, player, config2;
	public ItemStorage item;
	public GatyaSystem gatya;
	public int page;
	public HashMap<String, Integer> top30 = new HashMap<String, Integer>();
	public HashMap<String, Integer> data = new HashMap<String, Integer>();
	public HashMap<String, Long> bossbar = new HashMap<String, Long>();
	public HashMap<String, Boolean> level = new HashMap<String, Boolean>();
	public HashMap<String, Boolean> fly = new HashMap<String, Boolean>();
	public HashMap<String, Boolean> cooltime = new HashMap<String, Boolean>();
	public HashMap<String, Integer> limit = new HashMap<String, Integer>();

	public HashMap<String, Integer> block = new HashMap<String, Integer>();
	public HashMap<String, Integer> skilln = new HashMap<String, Integer>();
	public HashMap<String, Boolean> skill = new HashMap<String, Boolean>();
	public HashMap<String, Boolean> usedskill = new HashMap<String, Boolean>();

	public HashMap<String, Integer> asault = new HashMap<String, Integer>();

	@SuppressWarnings({ "deprecation" })
	public void onEnable(){
		this.item = (ItemStorage) getServer().getPluginManager().getPlugin("ItemStorage");
		this.gatya = (GatyaSystem) getServer().getPluginManager().getPlugin("GatyaSystem");
		try {
			if (this.item !=  null) {
			}
			if (this.gatya != null) {
			}
			this.getServer().getPluginManager().registerEvents(this, this);
			this.getDataFolder().mkdirs();
			File file = new File(this.getDataFolder().getPath() + "\\data");
			file.mkdirs();
			this.config = new Config(new File(this.getDataFolder(), "config.yml"),Config.YAML,
				new LinkedHashMap<String, Object>() {
					{
						put("スキルが使えるワールド", "sigen:seichi");
						put("flyが使えるワールド", "sigen:seichi");
					}
				}
			);
			this.config2 = new Config(new File(this.getDataFolder(), "uplimit.yml"));
			this.player =  new Config(new File(this.getDataFolder(), "data.yml"),Config.YAML);
			String[] datas1 = ((String) this.config.get("スキルが使えるワールド")).split(":");
			for (int i = 0; i < datas1.length; i++) {
				this.level.put(datas1[i], true);
			}
			String[] datas2 = ((String) this.config.get("flyが使えるワールド")).split(":");
			for (int i = 0; i < datas2.length; i++) {
				this.fly.put(datas2[i], true);
			}
			this.player.getAll().forEach((name, data) -> {
				if (data != null) {
					this.data.put(name, (Integer) data);
				}else{
					this.data.put(name, (Integer) 0);
					this.player.set(name, (Integer) 0);
					this.player.save();
				}
			});
			this.config2.getAll().forEach((name, up) -> {
				this.limit.put(name, (Integer) up);
			});

			getServer().getScheduler().scheduleRepeatingTask(this, new Runnable() {//セーブタスク
				@Override
				public void run() {
					getServer().broadcastMessage("§a>>§bオートセーブを開始します");
					save();
					getServer().broadcastMessage("§a>>§bオートセーブが終了しました");
				}
			}, 20 * 60 * 10);
			getServer().getScheduler().scheduleRepeatingTask(this, new Runnable() {//採掘エフェタスク
				@Override
				public void run() {
					task1();
				}
			}, 20 * 60);
			getServer().getScheduler().scheduleRepeatingTask(this, new Runnable() {//アサルトタスク
				@Override
				public void run() {
					Asault();
				}
			}, 3);
			getServer().getScheduler().scheduleRepeatingTask(this, new Runnable() {//30分間ランキングタスク
				@Override
				public void run() {
					top();
				}
			}, 20 * 60 * 30);
		}
		catch(ArrayIndexOutOfBoundsException e){
			getServer().getLogger().error("APIのプラグインが見つかりません");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	public void onDisable() {
		save();
	}

	@EventHandler
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		if (!(this.block.containsKey(name))) {
			this.block.put(name, 0);
			this.skill.put(name, false);
			this.skilln.put(name, 0);
			this.top30.put(name, 0);
			this.cooltime.put(name, true);
		}
		this.usedskill.put(name, true);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		if (!(this.data.containsKey(name))) {
			Integer i = 0;
			this.data.put(name, i);
			Item item1 = gatya.addEnchant(Item.get(278, 0, 1), 15, 5);
			player.getInventory().addItem(gatya.addEnchant(item1, 17, 5));
			player.getInventory().addItem(Item.get(364, 0, 32));
			player.getInventory().addItem(gatya.setNameTag(Item.get(280, 0, 1), "§bいろいろできる便利な木の棒"));
			player.getInventory().setArmorItem(1,Item.get(444,0,1));
			player.getInventory().sendArmorContents(player);
			player.getInventory().sendContents(player);
		}
		LevelSystem l;
		if (this.limit.containsKey(name)) {
			l = new LevelSystem(this.data.get(name), this.limit.get(name));
		}else{
			l = new LevelSystem(this.data.get(name));
		}
		this.bossbar.put(name, player.createBossBar(getBossText(player), l.getProportion()));
		getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				joinTask(player);
			}
		}, 5);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		this.player.set(name, this.data.get(name));
		this.player.save();
	}

	@EventHandler
	public void onBlockUpdate(BlockUpdateEvent event) {
		Block block = event.getBlock();
		int id = block.getId();
		if (this.level.containsKey(block.getLevel().getName())) {
			if (id == 8 || id == 9 || id == 10 || id  == 11) {
				event.setCancelled();
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Level level = player.getLevel();
		double x = player.x;
		double y = player.y;
		double z = player.z;
		if (this.level.containsKey(level.getName())) {
			int id = level.getBlock(new Vector3(x, y - 1, z)).getId();
			double x1, y1, z1;
			if (id == 8 || id == 9) {
				for (x1 = x + 4; x1 > x - 5; x1--) {
					for (z1 = z + 4; z1 > z - 5; z1--) {
						for (y1 = y - 1; y1 > 0; y1--) {
							Block before = level.getBlock(new Vector3(x1, y1, z1));
							if (before.getId() == 8 || before.getId() == 9) {
								Block block = Block.get(79);
								BlockPlaceEvent ev = new BlockPlaceEvent(player, before, before, before, Item.get(block.getId(), block.getDamage(), 1));
								getServer().getPluginManager().callEvent(ev);
								if (!ev.isCancelled()) {
									level.setBlock(new Vector3(x1, y1, z1), block);
								}
							}else{
								y1 = 0;
							}
						}
					}
				}
			}else if (id == 10 || id == 11) {
				for (x1 = x + 4; x1 > x - 5; x1--) {
					for (z1 = z + 4; z1 > z - 5; z1--) {
						for (y1 = y - 1; y1 > 0; y1--) {
							Block before = level.getBlock(new Vector3(x1, y1, z1));
							if (before.getId() == 10 || before.getId() == 11) {
								Block block = Block.get(45);
								BlockPlaceEvent ev = new BlockPlaceEvent(player, before, before, before, Item.get(block.getId(), block.getDamage(), 1));
								getServer().getPluginManager().callEvent(ev);
								if (!ev.isCancelled()) {
									level.setBlock(new Vector3(x1, y1, z1), block);
								}
							}else{
								y1 = 0;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		if (player.isSurvival()) {
			if (this.usedskill.get(name)) {
				if (this.cooltime.get(name)) {
					String level = player.getLevel().getName();
					Item[] a = new Item[1];
					a[0] = Item.get(0);
					event.setDrops(a);
					if (this.level.containsKey(level)) {
						event.setCancelled();
						if (canBlockBreak(event)) {
							if (this.skill.get(name)) {
								useSkill(event);
							}else{
								handBlockBreak(event, true);
							}
							LevelSystem l;
							if (this.limit.containsKey(name)) {
								l = new LevelSystem(this.data.get(name), this.limit.get(name));
							}else{
								l = new LevelSystem(this.data.get(name));
							}
							player.updateBossBar(getBossText(player), l.getProportion(), this.bossbar.get(name));
						}else{
							player.sendTip("§b上から掘って下さい");
						}
					}else{
						event.setCancelled();
						handBlockBreak(event, false);
					}
				}else{
					event.setCancelled();
					player.sendPopup("§bクールタイム中です");
				}
				player.getInventory().sendContents(player);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityLevelChange(EntityLevelChangeEvent event) {
		String level = event.getTarget().getName();
		if (!(this.fly.containsKey(level))) {
			Player player = (Player) event.getEntity();
			if (player.isSurvival()) {
				if (player.getAllowFlight()) {
					player.setAllowFlight(false);
					SetPlayerGameTypePacket pk = new SetPlayerGameTypePacket();
					pk.gamemode = 0;
					player.dataPacket(pk);
					player.sendMessage("§a>>§bこのワールドはfly機能を使えないため、fly機能を終了させました");
				}
			}
		}
	}

	@EventHandler
	public void onEntity(EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		if (level.containsKey(entity.level.getName())) {
			if (entity.getName().equals("FallingSand")) {
				entity.close();
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Block block = event.getBlock();
		if (this.level.containsKey(block.getLevel().getName()) && block.y == 6 && block.getId() == 44) {
			event.setCancelled();
			return;
		}
		if (block.y == 5 || block.y== 6) {
			Player player = event.getPlayer();
			String name = player.getName().toLowerCase();
			Level level = player.getLevel();
			if (this.level.containsKey(level.getName())) {
				if (this.skill.get(name)) {
					if (this.skilln.get(name) == 4) {
						if (player.getInventory().getItemInHand().isTool()) {
							new skill4(player, block, this);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityArrow) {
			Vector3 pos = entity.add(entity.lastMotionX / 4, entity.lastMotionY / 4, entity.lastMotionZ / 4);
			Level level = entity.getLevel();
			Block block = level.getBlock(pos);
			if (((EntityArrow)entity).shootingEntity instanceof Player) {
				Player player = (Player) ((EntityArrow)entity).shootingEntity;
				String name = player.getName().toLowerCase();
				if (player.isSurvival()) {
					if (this.usedskill.get(name)) {
						if (this.level.containsKey(level.getName())) {
							if (this.skill.get(name)) {
								if (this.skilln.get(name) == 6) {
									this.usedskill.put(name, false);
									new skill6(player, block, this);
									this.usedskill.put(name, true);
									getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
										@Override
										public void run() {
											entity.kill();
										}
									}, 20);
									LevelSystem l;
									if (this.limit.containsKey(name)) {
										l = new LevelSystem(this.data.get(name), this.limit.get(name));
									}else{
										l = new LevelSystem(this.data.get(name));
									}
									player.updateBossBar(getBossText(player), l.getProportion(), this.bossbar.get(name));
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onDataPacketReceive(DataPacketReceiveEvent event) {
		DataPacket pk = event.getPacket();
		Player player = event.getPlayer();
		if (pk instanceof ContainerClosePacket) {
			player.getInventory().sendContents(player);
		}
	}

	@SuppressWarnings({ "deprecation" })
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		switch(command.getName()){
		case "skill":
			if (sender instanceof Player) {
				try{
					if(args[0] != null){
						String name = sender.getName().toLowerCase();
						switch (args[0]) {
							case "on":
								this.skill.put(name, true);
								sender.sendMessage("§a>>§bスキルをonにしました");
							break;

							case "off":
								this.skill.put(name, false);
								sender.sendMessage("§a>>§bスキルをoffにしました");
							break;

							case "1":case "2":case "3":case "4":case "5":case "6":case "7":case "8":case "9": case "10":
								int i = Integer.parseInt(args[0]);
								int count = (int) this.data.get(name);
								LevelSystem l;
								if (this.limit.containsKey(name)) {
									l = new LevelSystem(count, this.limit.get(name));
								}else{
									l = new LevelSystem(count);
								}
								if (i * 10 <= l.getLevel()) {
									this.skilln.put(name, i);
									sender.sendMessage("§a>>§bスキルを" + args[0] + "にしました");
								}else{
									sender.sendMessage("§a>>§b貴方はまだそのスキルを使うことができません");
								}
							break;

							default:
								sender.sendMessage("§a>>§b/skill onまたはoff | スキルのon/offを設定するコマンド");
								sender.sendMessage("§a>>§b/skill 1~10        | 使用するスキルのレベルを設定");
							break;
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/skill onまたはoff | スキルのon/offを設定するコマンド");
					sender.sendMessage("§a>>§b/skill 1~10        | 使用するスキルのレベルを設定");
				}
			}else{
				sender.sendMessage("§a>>§bこのコマンドはコンソールから実行できません");
			}
			break;

			case "fly":
				if (sender instanceof Player) {
					try {
						if (args[0] != null) {
							Player player = (Player)sender;
							switch (args[0]) {
							case"on":
								String level = player.getLevel().getName();
								if (this.fly.containsKey(level)) {
									player.setAllowFlight(true);
									player.sendMessage("§a>>§bfly機能を起動しました");
								}else{
									sender.sendMessage("§a>>§bこのワールドではfly機能を使うことができません");
								}
							break;

							case"off":
								player.setAllowFlight(false);
								SetPlayerGameTypePacket pk = new SetPlayerGameTypePacket();
								pk.gamemode = 0;
								player.dataPacket(pk);
								player.sendMessage("§a>>§bfly機能を終了させました");
							break;

							default:
								sender.sendMessage("§a>>§b/fly onまたはoff");
							break;
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e){
						sender.sendMessage("§a>>§b/fly onまたはoff");
					}
				}else{
					sender.sendMessage("§a>>§bこのコマンドはコンソールから実行できません");
				}
			break;

			case "st":
				if (sender instanceof Player) {
					String name = sender.getName().toLowerCase();
					int count = (int) this.data.get(name);
					LevelSystem l;
					if (this.limit.containsKey(name)) {
						l = new LevelSystem(count, this.limit.get(name));
					}else{
						l = new LevelSystem(count);
					}
					sender.sendMessage("§a>>§bレベル" + String.valueOf(l.getLevel()));
					sender.sendMessage("§a>>§b整地量" + String.valueOf(count));
					sender.sendMessage("§a>>§b必要な整地量" + String.valueOf(l.getNextLevel()));
					sender.sendMessage("§a>>§b使えるスキルのレベル" + String.valueOf((int)l.getLevel() / 10));
					sender.sendMessage("§a>>§b使ってるスキル" + this.skilln.get(name));
				}else{
					sender.sendMessage("§a>>§bこのコマンドはコンソールから実行できません");
				}
			break;

			case "seest":
				try{
					if (args[0] != null) {
						String name = args[0].toLowerCase();
						if (this.data.containsKey(name)) {
							int count = (int) this.data.get(name);
							LevelSystem l;
							if (this.limit.containsKey(name)) {
								l = new LevelSystem(count, this.limit.get(name));
							}else{
								l = new LevelSystem(count);
							}
							sender.sendMessage("§a>>§b" + args[0] + "さんは" + String.valueOf(l.getLevel()) + "Lvで" + String.valueOf(count) + "ブロック整地してます");
						}else{
							sender.sendMessage("§a>>§bそのプレイヤーは存在しません");
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/seest [名前]");
				}
			break;

			case "top":
				try{
					if (args[0] != null) {
						int h = this.data.size() / 5 + 1;
						if (isInt(args[0])) {
							int i = Integer.parseInt(args[0]);
							if (i <= h) {
								this.page = i;
							}else{
								this.page = h;
							}
						}else{
							this.page = 1;
						}
						sender.sendMessage("§e====整地量ランキング" + this.page + "/" + h + "====");
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					this.page = 1;
					int h = this.data.size() / 5;
					if (this.data.size() % 5 != 0) {
						h++;
					}
					sender.sendMessage("§e====整地量ランキング" + this.page + "/" + h + "====");
				}
				int p = this.page;
				List<Entry<String, Integer>> top = new ArrayList<Entry<String, Integer>>(this.data.entrySet());
				Collections.sort(top, new Comparator<Entry<String, Integer>>() {
					public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
						return obj2.getValue().compareTo(obj1.getValue());
					}
				});
				int c = 1;
				for (Entry<String, Integer> entry : top) {
					if (c >= p * 5 - 4 && c <= p * 5) {
						sender.sendMessage("§b" + c + "位 | " + entry.getKey() + " : " + entry.getValue() + "ブロック");
					}
					c++;
				}
			break;


			case"way":
				try {
					if (args[0] != null) {
						if (isInt(args[0])) {
							Player player = (Player) sender;
							double x = player.x;
							double y = player.y;
							double z = player.z;
							Level level = player.getLevel();
							double way = Double.parseDouble(args[0]);
							double pitch = player.pitch;
							((Player) sender).teleport(new Location(x, y, z, way, pitch, level));
							sender.sendMessage("§a>>§b向いてる方向を変えました");
						}else{
							sender.sendMessage("§a>>§b/way [角度]");
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/way [角度]");
				}
			break;

			case "uplimit":
				Player player = (Player) sender;
				Item item = player.getInventory().getItemInHand();
				String name = player.getName().toLowerCase();
				if (this.limit.containsKey(name) || new LevelSystem(this.data.get(name)).getLevel() == 100) {
					if (item.hasCompoundTag() && item.getNamedTag().contains("R") && item.getNamedTag().get("R") instanceof CompoundTag) {
						int rank = item.getNamedTag().getCompound("R").getInt("R");
						int limit;
						if (this.limit.containsKey(name)) {
							limit = (int) this.limit.get(name);
						}else{
							limit = 0;
						}
						if (rank == 500) {
							player.getInventory().setItemInHand(Item.get(0));
							limit = limit + 2;
							this.limit.put(name, limit);
							this.config2.set(name, limit);
							this.config2.save();
							sender.sendMessage("§a>>§bレベルの上限を 2 あげました");
							LevelSystem l;
							if (this.limit.containsKey(name)) {
								l = new LevelSystem(this.data.get(name), this.limit.get(name));
							}else{
								l = new LevelSystem(this.data.get(name));
							}
							player.updateBossBar(getBossText(player), l.getProportion(), this.bossbar.get(name));
						}else if (rank == 1000) {
							player.getInventory().setItemInHand(Item.get(0));
							limit = limit + 6;
							this.limit.put(name, limit);
							this.config2.set(name, limit);
							this.config2.save();
							sender.sendMessage("§a>>§bレベルの上限を 6 あげました");
							LevelSystem l;
							if (this.limit.containsKey(name)) {
								l = new LevelSystem(this.data.get(name), this.limit.get(name));
							}else{
								l = new LevelSystem(this.data.get(name));
							}
							player.updateBossBar(getBossText(player), l.getProportion(), this.bossbar.get(name));
						}else{
							sender.sendMessage("§a>>§bそのアイテムではレベルの上限を解除できません");
						}
					}else{
						sender.sendMessage("§a>>§bそのアイテムではレベルの上限を解除できません");
					}
				}else{
					sender.sendMessage("§a>>§b100lv以上ではないと、レベルの上限を解放できません");
				}
			break;

			case "stick":
				((Player)sender).getInventory().addItem(gatya.setNameTag(Item.get(280, 0, 1), "§bいろいろできる便利な木の棒"));
				sender.sendMessage("§a>>§b棒を渡しました");
			break;
		}
		return false;
	}

	private boolean isInt(String number) {
		try {
			Double.parseDouble(number);
			return true;
		}
		catch(NumberFormatException ex){
			return false;
		}
	}

	public Boolean canBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		Block block = event.getBlock();
		Item item = player.getInventory().getItemInHand();
		double x = block.getX();
		double y = block.getY();
		double z = block.getZ();
		Level level = block.getLevel();
		if (this.skill.get(name)) {
			switch (this.skilln.get(name)) {
				case 1:y += 2;if (isAddArea(item)) y++;break;
				case 2:y += 2;if (isAddArea(item)) y++;break;
				case 3:y += 3;if (isAddArea(item)) y++;break;
				case 5:y += 5;if (isAddArea(item)) y++;break;
				case 7:y += 7;if (isAddArea(item)) y++;break;
				case 8:y += 9;if (isAddArea(item)) y++;break;
				case 9:y += 11;if (isAddArea(item)) y++;break;
				default:y += 1;break;
			}
		}else{
			y++;
		}
		int id = level.getBlock(new Vector3(x, y, z)).getId();
		boolean r = false;
		switch (id) {
		case 0: case 6:  case 7: case 17: case 18: case 31: case 32: case 37: case 38: case 39:
		case 40: case 50: case 78: case 81: case 83: case 106: case 111: case 161: case 162: case 175:
			r = true;
			break;
		}
		return r;
	}

	public void useSkill(BlockBreakEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		Item item = player.getInventory().getItemInHand();
		if (item.isTool()) {
			this.usedskill.put(name, false);
			Block block = event.getBlock();
			switch (this.skilln.get(name)) {
				case 0:
					int x = (int) block.x;
					int y = (int) block.y;
					int z = (int) block.z;
					Vector3 pos = new Vector3(x, y, z);
					Level level = block.getLevel();
					skillBlockBreak(player, pos, level);
				break;

				case 1:
					new skill1(player, block, this, isAddArea(item));
				break;

				case 2:
					new skill2(player, block, this, isAddArea(item));
				break;

				case 3:
					new skill3(player, block, this, isAddArea(item));
				break;

				case 5:
					new skill5(player, block, this, isAddArea(item));
				break;

				case 7:
					new skill7(player, block, this, isAddArea(item));
				break;

				case 8:
					new skill8(player, block, this, isAddArea(item));
				break;

				case 9:
					new skill9(player, block, this, isAddArea(item));
				break;
			}
			this.usedskill.put(name, true);
		}
	}

	public boolean skillBlockBreak(Player player, Vector3 pos, Level level) {
		Block block = level.getBlock(pos);
		int id = block.getId();
		if (id != 0) {
			if (id == 17 || id == 162) {
				if (block.getDamage() != 3) {
					Vector3 p1 = new Vector3(pos.x + 1, pos.y, pos.z);
					Vector3 p2 = new Vector3(pos.x - 1, pos.y, pos.z);
					Vector3 p3 = new Vector3(pos.x, pos.y, pos.z + 1);
					Vector3 p4 = new Vector3(pos.x, pos.y, pos.z - 1);
					int b1 = level.getBlock(p1).getId();
					int b2 = level.getBlock(p1).getId();
					int b3 = level.getBlock(p1).getId();
					int b4 = level.getBlock(p1).getId();
					if (b1 == 17 || b1 == 18 || b1 == 161 || b1 == 162) {
						skillBlockBreak(player, p1, level);
					}
					if (b2 == 17 || b2 == 18 || b2 == 161 || b2 == 162) {
						skillBlockBreak(player, p2, level);
					}
					if (b3 == 17 || b3 == 18 || b3 == 161 || b3 == 162) {
						skillBlockBreak(player, p3, level);
					}
					if (b4 == 17 || b4 == 18 || b4 == 161 || b4 == 162) {
						skillBlockBreak(player, p4, level);
					}
				}
			}
			if (id != 7) {
				if (this.level.containsKey(level.getName()) && block.y > 5 && (!(block.y == 6 && block.getId() == 44))) {
					Vector3 p = new Vector3(pos.x, pos.y + 1, pos.z);
					Block b = level.getBlock(p);
					switch (b.getId()) {
						case 6: case 17: case 18: case 31: case 32: case 37: case 38: case 39:
						case 40: case 50: case 78: case 81: case 83: case 106: case 111: case 161: case 162: case 175:
							skillBlockBreak(player, p, level);
						case 0: case 7:
							String name = player.getName().toLowerCase();
							Item item = player.getInventory().getItemInHand();
							BlockBreakEvent ev = new BlockBreakEvent(player, block, item, false, false);
							this.getServer().getPluginManager().callEvent(ev);
							if (!(ev.isCancelled())) {
								if (isAddDamage(item)) {
									item.setDamage(item.getDamage() + 1);
									player.getInventory().setItemInHand(item);
								}
								if (item.isTool()) {
									if (item.getDamage() >= item.getMaxDurability()) {
										player.getInventory().setItemInHand(Item.get(0, 0, 0));
									}
								}
								sendAir(player, (int) pos.x, (int) pos.y, (int) pos.z);
								level.setBlock(pos, Block.get(0));
								addCount(player, 1);
								sendStorage(name, ev.getDrops());
								BlockEntity blockEntity = level.getBlockEntity(pos);
								if (blockEntity != null) {
									if (blockEntity instanceof InventoryHolder) {
										if (blockEntity instanceof BlockEntityChest) {
											((BlockEntityChest) blockEntity).unpair();
										}
										for (Item chestItem : ((InventoryHolder) blockEntity).getInventory().getContents().values()) {
											level.dropItem(pos, chestItem);
										}
									}
									blockEntity.close();
								}
							}
						return true;

						default:
							player.sendTip("§b上から掘って下さい");
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public Boolean isAddDamage(Item item) {
		if (item.isTool() && !(item instanceof ItemBow)) {
			if (!(item.hasCompoundTag() && item.getNamedTag().contains("Unbreakable"))) {
				if ( (int) (Math.random() * 3) == 1) {
					if (item.getEnchantment(17) == null) {
						return true;
					}else{
						Enchantment enchant = item.getEnchantment(17);
						int level = enchant.getLevel();
						if ( (int) (Math.random() * level) == 1) {
							return true;
						}else{
							return false;
						}
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public void addCount(Player player, int count) {
		String name = player.getName().toLowerCase();
		int c = (int) this.data.get(name);
		this.block.put(name, this.block.get(name) + count);
		this.data.put(name, c + count);
		this.gatya.addCount(name);
		this.top30.put(name, this.top30.get(name) + count);
		if (new Random().nextInt(2000000) == 1) {
			Item item = Item.get(377, 0, 1);
			item.setNamedTag(new CompoundTag().putCompound("Data", new CompoundTag("Data").putInt("Data", 1)));
			item.setCustomName("§e覚醒石");
			player.getPlayer().getInventory().addItem(item);
			player.sendMessage("§a>>§bおや、ブロックの中から覚醒石が出てきたようだ");
		}
		LevelSystem l;
		if (this.limit.containsKey(name)) {
			l = new LevelSystem(count + c, this.limit.get(name));
		}else{
			l = new LevelSystem(count + c);
		}
		if (l.isLevelUp()) {
			if (this.limit.containsKey(name)) {
				l = new LevelSystem(this.data.get(name), this.limit.get(name));
			}else{
				l = new LevelSystem(this.data.get(name));
			}
			int lv = l.getLevel();
			String before = "§b[§e整地レベル" + String.valueOf(lv - 1) + "Lv§b]§f";
			String after = "§b[§e整地レベル" + String.valueOf(lv) + "Lv§b]§f";
			player.setDisplayName(player.getDisplayName().replace(before, after));
			player.setNameTag(player.getNameTag().replace(before, after));
			player.sendMessage("§a>>§bおめでとうございます");
			player.sendMessage("§a>>§bレベルが上がりました");
			player.setSubtitle("§bおめでとうございます");
			player.sendTitle("§aレベルが上がりました");
		}
	}

	public void sendStorage(String name, Item[] items) {
		for (int i = 0; i < items.length; i++) {
			Item it = items[i];
			int id = it.getId();
			int damage = it.getDamage();
			int count = it.getCount();
			if (id != 8 && id != 9 && id != 10 && id != 11) {
				this.item.addItem(name, id, damage, count);
			}
		}
	}

	public void sendPlayer(Player player, Item[] items) {
		for (int i = 0; i < items.length; i++) {
			Item it = items[i];
			if (it.getId() != 10) {
				player.getInventory().addItem(it);
			}
		}
	}

	public void handBlockBreak(BlockBreakEvent event, boolean r) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		Block block = event.getBlock();
		Item item = player.getInventory().getItemInHand();
		Level level = block.getLevel();
		this.usedskill.put(name, false);
		BlockBreakEvent ev = new BlockBreakEvent(player, block, item, false, false);
		this.getServer().getPluginManager().callEvent(ev);
		this.usedskill.put(name, true);
		if (this.level.containsKey(level.getName()) && block.y < 6) {
			return;
		}else if (this.level.containsKey(level.getName()) && block.y == 6 && block.getId() == 44) {
			return;
		}
		if (!(ev.isCancelled())) {
			if (isAddDamage(item)) {
				item.setDamage(item.getDamage() + 1);
				player.getInventory().setItemInHand(item);
			}
			if (item.isTool()) {
				if (item.getDamage() >= item.getMaxDurability()) {
					player.getInventory().setItemInHand(Item.get(0, 0, 0));
				}
			}
			sendAir(player, (int) block.x, (int) block.y, (int) block.z);
			Vector3 pos = new Vector3(block.x, block.y, block.z);
			level.setBlock(pos, Block.get(0));
			if (r) {
				addCount(player, 1);
			}
			sendPlayer(player, ev.getDrops());
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity != null) {
				if (blockEntity instanceof InventoryHolder) {
					if (blockEntity instanceof BlockEntityChest) {
						((BlockEntityChest) blockEntity).unpair();
					}
					for (Item chestItem : ((InventoryHolder) blockEntity).getInventory().getContents().values()) {
						level.dropItem(pos, chestItem);
					}
				}
				blockEntity.close();
			}
		}
	}

	public boolean isAddArea(Item item) {
		if (item.hasCompoundTag()) {
			if (item.getNamedTag().contains("R")) {
				if (item.getNamedTag().getCompound("R").getInt("R") == 1000) {
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public boolean placeHalf(Player player, Vector3 pos) {
		Block block = Block.get(44);
		Item item = player.getInventory().getItemInHand();
		Level level = player.getLevel();
		Block before = level.getBlock(pos);
		BlockPlaceEvent event = new BlockPlaceEvent(player, before, before, before, Item.get(44));
		getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			if (before.getId() == 0) {
				level.setBlock(pos, block);
				if (isAddDamage(item)) {
					item.setDamage(item.getDamage() + 1);
					player.getInventory().setItemInHand(item);
				}
				if (item.isTool()) {
					if (item.getDamage() >= item.getMaxDurability()) {
						player.getInventory().setItemInHand(Item.get(0, 0, 0));
					}
				}
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	public int getDirection(Player player) {
		double rotation = (player.yaw - 90) % 360;
		if (rotation < 0) {
			rotation += 360.0;
		}
		if ((0 <= rotation && rotation < 45) || (315 <= rotation && rotation < 360)) {
			return 2; //North
		} else if (45 <= rotation && rotation < 135) {
			return 3; //East
		} else if (135 <= rotation && rotation < 225) {
			return 0; //South
		} else if (225 <= rotation && rotation < 315) {
			return 1; //West
		} else {
			return 0;
        }
	}

	public String getBossText(Player player) {
		String name = player.getName().toLowerCase();
		int count = (int) this.data.get(name);
		LevelSystem l;
		if (this.limit.containsKey(name)) {
			l = new LevelSystem(count, (int) this.limit.get(name));
		}else{
			l = new LevelSystem(count);
		}
		String text;
		if (l.getMaxNextLevel() == 0) {
			text = "§e貴方のステータス : レベル " + String.valueOf(l.getLevel()) + "Lv\n\n§b総整地量 " + NumberFormat.getNumberInstance().format(count) + "ブロック";
		}else{
			text = "§e貴方のステータス : レベル " + String.valueOf(l.getLevel()) + "Lv\n\n§b総整地量 " + NumberFormat.getNumberInstance().format(count) + "ブロック : 次のレベルまで " + NumberFormat.getNumberInstance().format(l.getNextLevel()) + "ブロック";
		}
		return text;
	}

	public void sendAir(Player player, int x, int y, int z) {
		UpdateBlockPacket pk = new UpdateBlockPacket();
		pk.x = x;
		pk.y = y;
		pk.z = z;
		pk.blockId = 0;
		pk.blockData = 0;
		pk.flags = UpdateBlockPacket.FLAG_NONE;
		player.dataPacket(pk);
	}

	public int getRanking(String name) {
		List<Entry<String, Integer>> top = new ArrayList<Entry<String, Integer>>(this.data.entrySet());
		Collections.sort(top, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});
		int c = 1;
		int r = 0;
		for (Entry<String, Integer> entry : top) {
			if (entry.getKey().equals(name)) {
				r = c;
				break;
			}else{
				c++;
			}
		}
		return r;
	}

	public int getNextRankingCount(String name) {
		List<Entry<String, Integer>> top = new ArrayList<Entry<String, Integer>>(this.data.entrySet());
		Collections.sort(top, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});
		int r = 0;
		int next = 0;
		for (Entry<String, Integer> entry : top) {
			if (entry.getKey().equals(name)) {
				r = next - entry.getValue();
				break;
			}else{
				next = entry.getValue();
			}
		}
		return r;
	}

	public void cooltime(Player player, int time) {
		String name = player.getName().toLowerCase();
		this.cooltime.put(name, false);
		getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				cooltask(name);
				player.sendPopup("§bクールタイムが終了しました");
			}
		}, time);
	}

	public void save() {
		this.top30.forEach((name, count) -> {
			this.player.set(name, this.data.get(name));
		});
		this.player.save();
		getServer().getLevels().forEach((key, level) -> level.save());
	}

	public void task1() {
		Map<UUID, Player> players = getServer().getOnlinePlayers();
		players.forEach((UUID, player) -> task2(player));
	}

	public void task2(Player player){
		if (player.hasEffect(3)) {
			player.removeEffect(3);
		}
		String name = player.getName().toLowerCase();
		int d = this.block.get(name) / 100;
		if (d >= 100) {
			d = 100;
		}
		this.block.put(name, 0);
		player.addEffect(Effect.getEffect(3).setDuration(10000 * 20).setAmplifier(d).setVisible(false));
	}

	public void Asault() {
		Map<UUID, Player> players = getServer().getOnlinePlayers();
		players.forEach((UUID, player) -> useAsault(player));
	}

	public void useAsault(Player player) {
		String name = player.getName().toLowerCase();
		if (this.skilln.get(name) == 10) {
			Level level = player.getLevel();
			if (this.level.containsKey(level.getName())) {
				if (this.skill.get(name)) {
					if (player.isSurvival()) {
						Item item = player.getInventory().getItemInHand();
						if (item.isTool()) {
							this.usedskill.put(name, false);
							boolean r = new skill10().use(player, this, isAddArea(item));
							this.usedskill.put(name, true);
							LevelSystem l;
							if (this.limit.containsKey(name)) {
								l = new LevelSystem(this.data.get(name), this.limit.get(name));
							}else{
								l = new LevelSystem(this.data.get(name));
							}
							player.updateBossBar(getBossText(player), l.getProportion(), this.bossbar.get(name));
							if (this.asault.containsKey(name)) {
								if (r) {
									this.asault.put(name, 0);
								}else{
									int c = this.asault.get(name);
									c = c + 3;
									if (c < 200) {
										this.asault.put(name, c);
									}else{
										this.asault.put(name, 0);
										this.skill.put(name, false);
										player.sendMessage("§a>>§b一定の間ブロックを破壊しなかったため、スキルをoffにしました");
									}
								}
							}else{
								this.asault.put(name, 0);
							}
						}
					}
				}
			}
		}
	}

	public void joinTask(Player player) {
		String name = player.getName().toLowerCase();
		Player p = getServer().getPlayer(name);
		String tag = p.getDisplayName();
		LevelSystem l;
		if (this.limit.containsKey(name)) {
			l = new LevelSystem(this.data.get(name), this.limit.get(name));
		}else{
			l = new LevelSystem(this.data.get(name));
		}
		p.setDisplayName("§b[§e整地レベル" + String.valueOf(l.getLevel()) + "Lv§b]§f" + tag);
		p.setNameTag("§b[§e整地レベル" + String.valueOf(l.getLevel()) + "Lv§b]§f" + tag);
	}

	public void top() {
		getServer().broadcastMessage("§e====30分間の整地量のランキング====");
		List<Entry<String, Integer>> top = new ArrayList<Entry<String, Integer>>(this.top30.entrySet());
        Collections.sort(top, new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
                return obj2.getValue().compareTo(obj1.getValue());
            }
        });
        int c = 1;
        for (Entry<String, Integer> entry : top) {
        	if (c < 4) {
	        	getServer().broadcastMessage("§b" + c + "位 | " + entry.getKey() + " : " + entry.getValue() + "ブロック");
        	}
        	c++;
        }
        getServer().getOnlinePlayers().forEach((UUID, player) -> {
        	String name = player.getName().toLowerCase();
        	String count = String.valueOf(this.top30.get(name));
        	player.sendMessage("§a>>§b貴方は30分間で" + count + "ブロック破壊しました");
        });
        this.top30.forEach((name, count) -> this.top30.put(name, 0));
	}

	public void cooltask(String name) {
		this.cooltime.put(name, true);
	}
}


