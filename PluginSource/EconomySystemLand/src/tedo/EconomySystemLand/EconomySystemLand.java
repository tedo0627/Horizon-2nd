package tedo.EconomySystemLand;

import java.io.File;
import java.util.ArrayList;
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
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import tedo.EconomySystemAPI.EconomySystemAPI;

public class EconomySystemLand extends PluginBase implements Listener{

	public Config config, data, blocks;
	public EconomySystemAPI economy;
	public LandSystem land;
	public int id;
	public HashMap<String, Object> co = new HashMap<String, Object>();

	public HashMap<String, Integer> blockcount = new HashMap<String, Integer>();
	public HashMap<String, Boolean> login = new HashMap<String, Boolean>();

	public HashMap<String, Boolean> pos = new HashMap<String, Boolean>();
	public HashMap<String, Integer[]> pos1 = new HashMap<String, Integer[]>();
	public HashMap<String, Integer[]> pos2 = new HashMap<String, Integer[]>();

	@SuppressWarnings({ "deprecation", "unchecked" })
	public void onEnable(){
//		Command command = getServer().getCommandMap().getCommand("land");
//		command.addCommandParameters("default", new CommandParameter[]{});
//		command.addCommandParameters("pos1", new CommandParameter[]{
//				new CommandParameter("pos1", new String[]{"pos1"}),
//		});
//		command.addCommandParameters("pos2", new CommandParameter[]{
//				new CommandParameter("pos2", new String[]{"pos2"}),
//		});
//		command.addCommandParameters("buy", new CommandParameter[]{
//				new CommandParameter("buy", new String[]{"buy"}),
//		});
//		command.addCommandParameters("sell", new CommandParameter[]{
//				new CommandParameter("sell", new String[]{"sell"}),
//				new CommandParameter("land", CommandParameter.ARG_TYPE_INT, false),
//		});
//		command.addCommandParameters("give", new CommandParameter[]{
//				new CommandParameter("give", new String[]{"give"}),
//				new CommandParameter("land", CommandParameter.ARG_TYPE_INT, false),
//				new CommandParameter("player", CommandParameter.ARG_TYPE_PLAYER, false),
//		});
//		command.addCommandParameters("move", new CommandParameter[]{
//				new CommandParameter("move", new String[]{"move"}),
//				new CommandParameter("land", CommandParameter.ARG_TYPE_INT, false),
//		});
//		command.addCommandParameters("setmove", new CommandParameter[]{
//				new CommandParameter("setmove", new String[]{"setmove"}),
//		});
//		command.addCommandParameters("status", new CommandParameter[]{
//				new CommandParameter("status", new String[]{"status"}),
//		});
//		command.addCommandParameters("here", new CommandParameter[]{
//				new CommandParameter("here", new String[]{"here"}),
//		});
//		command.addCommandParameters("show", new CommandParameter[]{
//				new CommandParameter("show", new String[]{"show"}),
//		});
//		command.addCommandParameters("invi1", new CommandParameter[]{
//				new CommandParameter("invi", new String[]{"invi"}),
//				new CommandParameter("add", new String[]{"add"}),
//				new CommandParameter("land", CommandParameter.ARG_TYPE_INT, false),
//				new CommandParameter("player", CommandParameter.ARG_TYPE_PLAYER, false),
//		});
//		command.addCommandParameters("invi2", new CommandParameter[]{
//				new CommandParameter("invi", new String[]{"invi"}),
//				new CommandParameter("del", new String[]{"del"}),
//				new CommandParameter("land", CommandParameter.ARG_TYPE_INT, false),
//				new CommandParameter("player", CommandParameter.ARG_TYPE_PLAYER, false),
//		});
//		command.addCommandParameters("invi3", new CommandParameter[]{
//				new CommandParameter("invi", new String[]{"invi"}),
//				new CommandParameter("list", new String[]{"list"}),
//				new CommandParameter("land", CommandParameter.ARG_TYPE_INT, false),
//		});
		this.economy = (EconomySystemAPI) getServer().getPluginManager().getPlugin("EconomySystemAPI");
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getDataFolder().mkdirs();
		this.config = new Config(new File(this.getDataFolder(), "config.yml"),Config.YAML,
			new LinkedHashMap<String, Object>() {
				{
					put("ID", 0);
					put("土地一マスの値段", 100);
					put("保護しないと建築できない用にするか", false);
					put("保護できるワールドを指定するか", new LinkedHashMap<String, Object>() {
						{
							put("有効させるか", false);
							put("保護できるワールド", "seikatu:life");
						}
					});
					put("ログイン時間によって保護できるブロックの量を決めるか", new LinkedHashMap<String, Object>() {
						{
							put("有効させるか", false);
							put("デフォルトの保護できるブロックの量", 400);
							put("何秒ごとに1ブロック保護できるようにするか", 20);
						}
					});
					put("保護できる土地の1辺の大きさを決めるか", new LinkedHashMap<String, Object>() {
						{
							put("有効させるか", false);
							put("保護できる最小の1辺のブロックの数", 10);
							put("保護できる最大の1辺のブロックの数", 20);
						}
					});
					put("保護できる土地の最大数を決めるか", new LinkedHashMap<String, Object>() {
						{
							put("有効させるか", false);
							put("有効するワールド", "seikatu:life");
							put("最大数", 10);
						}
					});
					put("一定の間サーバーにログインしなかったら土地を削除するか", new LinkedHashMap<String, Object>() {
						{
							put("有効させるか", false);
							put("有効するワールド", "sigen:seichi");
							put("削除するまでの間隔(日)", 10);
						}
					});
				}
			}
		);
		this.data =  new Config(new File(this.getDataFolder(), "data.yml"),Config.YAML);
		this.co = (HashMap<String, Object>) this.config.getAll();
		this.id = this.config.getInt("ID");
		this.land = new LandSystem(this.data, this);

		if ((boolean) ((HashMap<String, Object>) this.co.get("ログイン時間によって保護できるブロックの量を決めるか")).get("有効させるか")) {
			this.blocks =  new Config(new File(this.getDataFolder(), "count.yml"),Config.YAML);
			this.blocks.getAll().forEach((name, count) -> {
				blockcount.put(name, (Integer) count);
			});
			getServer().getScheduler().scheduleRepeatingTask(this, new Runnable() {
			    @Override
			    public void run() {
			    	CountTask();
			    }
			}, 20 * (int) ((HashMap<String, Object>) this.co.get("ログイン時間によって保護できるブロックの量を決めるか")).get("何秒ごとに1ブロック保護できるようにするか"));
		}

		if ((boolean) ((HashMap<String, Object>) this.co.get("一定の間サーバーにログインしなかったら土地を削除するか")).get("有効させるか")) {
			HashMap<String, Object> data = (HashMap<String, Object>) this.co.get("一定の間サーバーにログインしなかったら土地を削除するか");
			String[] ls = ((String) data.get("有効するワールド")).split(":");
			for (int i = 0; i < ls.length; i++) {
				this.land.delAutoLand(ls[i], (int) data.get("削除するまでの間隔(日)"));
			}
		}

		getServer().getNameBans().getEntires().forEach((name, d) -> {
			this.land.delBanLand(name);
		});
	}

	@SuppressWarnings("unchecked")
	public void onDisable() {
		this.config.set("ID", this.id);
		this.config.save();
		if ((boolean) ((HashMap<String, Object>) this.co.get("ログイン時間によって保護できるブロックの量を決めるか")).get("有効させるか")) {
			getServer().getOnlinePlayers().forEach((UUID, player) -> {
				String name = player.getName().toLowerCase();
				this.blocks.set(name, blockcount.get(name));
				this.blocks.save();
			});
		}
	}

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		this.land.updateTime(name);
		if (!blockcount.containsKey(name)) {
			blockcount.put(name, (Integer) ((HashMap<String, Object>)this.co.get("ログイン時間によって保護できるブロックの量を決めるか")).get("デフォルトの保護できるブロックの量"));
		}
	}

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if ((boolean) ((HashMap<String, Object>) this.co.get("ログイン時間によって保護できるブロックの量を決めるか")).get("有効させるか")) {
			Player player = event.getPlayer();
			String name = player.getName().toLowerCase();
			this.blocks.set(name, blockcount.get(name));
			this.blocks.save();
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		Block block = event.getBlock();
		int x = (int) block.x;
		int z = (int) block.z;
		String level = player.getLevel().getName();
		if (block.getId() != 0) {
			if (this.pos.containsKey(name)) {
				Integer[] i =  new Integer[2];
				i[0] = x;
				i[1] = z;
				if (this.pos.get(name)) {
					this.pos1.put(name, i);
					this.pos.remove(name);
					event.setCancelled();
					player.sendMessage("§a>>§b一つ目の位置を決めました");
				}else{
					this.pos2.put(name, i);
					this.pos.remove(name);
					event.setCancelled();
					player.sendMessage("§a>>§b二つ目の位置を決めました");
				}
				getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						sendBlock(player, x, (int) block.y, z, 57);
					}
				}, 1);
			}else{
				if (!(player.isOp() && player.isCreative())) {
					Boolean r = land.canEditLand(name, x, z, level);
					if (r == null) {
						if (canBleakLevel(player)) {
							if ((boolean) this.co.get("保護しないと建築できない用にするか")) {
								event.setCancelled();
								player.sendPopup("§b保護してない場所は編集できません");
							}
						}
					}else if (r) {
					}else{
						event.setCancelled();
						String owner = (String) land.getLand(x, z, level).owner;
						player.sendPopup("§bこの土地は" + owner + "の土地です");
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		Block block = event.getBlock();
		int x = (int) block.x;
		int z = (int) block.z;
		String level = player.getLevel().getName();
		if (!(player.isOp() && player.isCreative())) {
			Boolean r = land.canEditLand(name, x, z, level);
			if (r == null) {
				if (canBleakLevel(player)) {
					if ((boolean) this.co.get("保護しないと建築できない用にするか")) {
						event.setCancelled();
						player.sendPopup("§b保護してない場所は編集できません");
					}
				}
			}else if (r) {
			}else{
				event.setCancelled();
				String owner = (String) land.getLand(x, z, level).owner;
				player.sendPopup("§bこの土地は" + owner + "の土地です");
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		Block block = event.getBlock();
		int x = (int) block.x;
		int z = (int) block.z;
		String level = player.getLevel().getName();
		if (!(player.isOp() && player.isCreative())) {
			Boolean r = land.canEditLand(name, x, z, level);
			if (r == null) {
				if (canBleakLevel(player)) {
					if ((boolean) this.co.get("保護しないと建築できない用にするか")) {
						event.setCancelled();
						player.sendPopup("§b保護してない場所は編集できません");
					}
				}
			}else if (r) {
			}else{
				event.setCancelled();
				String owner = (String) land.getLand(x, z, level).owner;
				player.sendPopup("§bこの土地は" + owner + "の土地です");
			}
		}
	}

	@SuppressWarnings("unchecked")
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		switch(label){
			case "land":
				String name = sender.getName().toLowerCase();
				try{
					if(args[0] != null){
						switch (args[0]) {
							case "pos1":
								this.pos.put(name, true);
								sender.sendMessage("§a>>§b一つ目の位置のブロックをタッチして下さい");
							break;

							case "pos2":
								this.pos.put(name, false);
								sender.sendMessage("§a>>§b二つ目の位置のブロックをタッチして下さい");
							break;

							case "buy":
								if (this.pos1.containsKey(name)) {
									if (this.pos2.containsKey(name)) {
										if (canProtectLevel((Player) sender)) {
											if (canBuy((Player) sender)) {
												if ((boolean) ((HashMap<String, Object>) this.co.get("ログイン時間によって保護できるブロックの量を決めるか")).get("有効させるか")) {
													if (blockcount.get(name) - this.land.getMyLandBlockCount(name) < getBlockCount((Player) sender)) {
														sender.sendMessage("§a>>§b保護できるブロック数が足りません");
														sender.sendMessage("§a>>§b鯖にログインしていると保護できるブロックの数が増えます");
														break;
													}
												}
												if ((boolean) ((HashMap<String, Object>) this.co.get("保護できる土地の最大数を決めるか")).get("有効させるか")) {
													HashMap<String, Object> d = (HashMap<String, Object>) this.co.get("保護できる土地の最大数を決めるか");
													HashMap<String, String> level = new HashMap<String, String>();
													String[] l = ((String) d.get("有効するワールド")).split(":");
													for (int i = 0; i < l.length; i++) {
														level.put(l[i], l[i]);
													}
													if (level.containsKey(((Player) sender).getLevel().getName())) {
														int co = this.land.getMyLandCount(name, ((Player) sender).getLevel().getName());
														if (co >= ((int) d.get("最大数"))) {
															sender.sendMessage("§a>>§b貴方はこのワールドにこれ以上保護している場所を増やすことはできません");
															sender.sendMessage("§a>>§b保護する場合は必要のない土地を売却してください");
															break;
														}
													}
												}
												if (isSide()) {
													if (!hasSide((Player) sender)) {
														HashMap<String, Object> data = (HashMap<String, Object>) co.get("保護できる土地の1辺の大きさを決めるか");
														int i1 = (int) data.get("保護できる最小の1辺のブロックの数");
														int i2 = (int) data.get("保護できる最大の1辺のブロックの数");
														sender.sendMessage("§a>>§b土地の1辺の長さは" + String.valueOf(i1) + "から" + String.valueOf(i2) + "にしてください");
														break;
													}
												}
												long money = getCount((Player) sender);
												if (this.economy.getMoney(name) >= money) {
													this.economy.delMoney(name, money);
													showBlock((Player) sender);
													addLand((Player) sender);
													sender.sendMessage("§a>>§b土地を購入しました");
												}else{
													sender.sendMessage("§a>>§bお金が足りません");
												}
											}else{
												sender.sendMessage("§a>>§b範囲内に保護されている場所があります");
											}
										}else{
											sender.sendMessage("§a>>§bこのワールドは保護できません");
										}
									}else{
										sender.sendMessage("§a>>§b二つ目の位置を決めてください");
									}
								}else{
									sender.sendMessage("§a>>§b一つ目の位置を決めてください");
								}
							break;

							case "sell":
								try {
									if (args[1] != null && isInt(args[1])) {
										int id = Integer.parseInt(args[1]);
										if (this.land.isLand(id)) {
											Land land = this.land.getLand(id);
											if (land.isOwner(name) || sender.isOp()) {
												int count = land.getBlockCount();
												int money = count / 2;
												this.economy.addMoney(name, money);
												delLand(id);
												sender.sendMessage("§a>>§b土地を売却しました");
											}else{
												sender.sendMessage("§a>>§b貴方はその土地の所有者ではありません");
											}
										}else{
											sender.sendMessage("§a>>§bその土地は存在しません");
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/land sell [土地番号]");
								}
							break;

							case "give":
								try {
									if (args[1] != null && isInt(args[1]) && args[2] != null) {
										int id = Integer.parseInt(args[1]);
										if (this.land.isLand(id)) {
											Land land = this.land.getLand(id);
											if (land.isOwner(name)) {
												if (this.economy.isMoney(args[2].toLowerCase())) {
													land.setOwner(args[2].toLowerCase());
													land.clearInvite();
													this.land.updateLand(land);
													sender.sendMessage("§a>>§b" + args[2] + "に土地を渡しました");
												}else{
													sender.sendMessage("§a>>§bそのプレイヤーは存在しません");
												}
											}else{
												sender.sendMessage("§a>>§b貴方はその土地の所有者ではありません");
											}
										}else{
											sender.sendMessage("§a>>§bその土地は存在しません");
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/land give [土地番号] [プレイヤー名]");
								}
							break;

							case "here":
								int x = (int) ((Player) sender).x;
								int z = (int) ((Player) sender).z;
								String level = ((Player) sender).getLevel().getName();
								if (this.land.isLand(x, z, level)) {
									Land land = this.land.getLand(x, z, level);
									String owner = land.owner;
									sender.sendMessage("§a>>§bこの土地は" + owner + "が所有しており、土地番号は" + String.valueOf(land.id) + "番です");
								}else{
									sender.sendMessage("§a>>§bこの土地は誰も所有していません");
								}
							break;

							case "status":
								if ((boolean) ((HashMap<String, Object>) this.co.get("ログイン時間によって保護できるブロックの量を決めるか")).get("有効させるか")) {
									sender.sendMessage("§a>>§b貴方が保護できる残りのブロック数は" + String.valueOf(blockcount.get(name) - this.land.getMyLandBlockCount(name)) + "ブロック");
								}
								if ((boolean) ((HashMap<String, Object>) this.co.get("保護できる土地の最大数を決めるか")).get("有効させるか")) {
									HashMap<String, Object> d = (HashMap<String, Object>) this.co.get("保護できる土地の最大数を決めるか");
									HashMap<String, String> levels = new HashMap<String, String>();
									String[] l = ((String) d.get("有効するワールド")).split(":");
									for (int i = 0; i < l.length; i++) {
										levels.put(l[i], l[i]);
									}
									if (levels.containsKey(((Player) sender).getLevel().getName())) {
										int co = this.land.getMyLandCount(name, ((Player) sender).getLevel().getName());
										sender.sendMessage("§a>>§b貴方はこのワールドで残り" + ((int) d.get("最大数") - co) + "個の土地を保護できます");
									}
								}
								sender.sendMessage("§a>>§b貴方が所有している土地");
								ArrayList<Land> lands = this.land.getMyLands(name);
								lands.forEach((land) -> {
									String x1 = String.valueOf(land.x1);
									String z1 = String.valueOf(land.z1);
									String x2 = String.valueOf(land.x2);
									String z2 = String.valueOf(land.z2);
									String id = String.valueOf(land.id);
									String le = String.valueOf(land.level);
									sender.sendMessage("§a>>§b土地番号" + id + " : x " + x1 + " ^ " + x2 + " : z " + z1 + " ^ " + z2 + " : world " + le);
								});
							break;

							case "move":
								try{
									if (args[1] != null) {
										if (isInt(args[1])) {
											int id = Integer.parseInt(args[1]);
											if (this.land.isLand(id)) {
												Land land = this.land.getLand(id);
												if (land.isOwner(name) || land.isInvite(name) || sender.isOp()) {
													((Player) sender).teleport(land.getPosition());
													((Player) sender).sendTip("§bテレポートしました");
												}else{
													sender.sendMessage("§a>>§bその土地にテレポートできません");
												}
											}else{
												sender.sendMessage("§a>>§bその土地は存在しません");
											}
										}else{
											sender.sendMessage("§a>>§b/land move [土地番号]");
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/land move [土地番号]");
								}
							break;

							case "setmove":
								Player player = (Player) sender;
								int x1 = (int) player.x;
								int y1 = (int) player.y;
								int z1 = (int) player.z;
								String level1 = player.getLevel().getName();
								if (this.land.isLand(x1, z1, level1)) {
									Land land = this.land.getLand(x1, z1, level1);
									if (land.isOwner(name)) {
										land.setPosition(new Position(x1, y1, z1, player.getLevel()));
										this.land.updateLand(land);
										sender.sendMessage("§a>>§bテレポートの場所を決めました");
									}else{
										sender.sendMessage("§a>>§b貴方はこの土地のテレポートをセットできません");
									}
								}else{
									sender.sendMessage("§a>>§bここは誰の土地でもありません");
								}
							break;

							case "show":
								ArrayList<Land> lands1 = this.land.getMyLands(name, ((Player)sender).getLevel().getName());
								lands1.forEach((land) -> {
									showBlock((Player) sender, land);
								});
								sender.sendMessage("§a>>§b自分が持ってる土地の範囲を見やすくしました");
							break;

							case "invi":
								try {
									if (args[1] != null) {
										switch (args[1]) {
											case "add":
												try {
													if (args[2] != null && args[3] != null) {
														if (isInt(args[2])) {
															int id = Integer.parseInt(args[2]);
															if (this.land.isLand(id)) {
																Land land = this.land.getLand(id);
																if (land.isOwner(name)) {
																	if (!land.isInvite(name)) {
																		land.addInvite(args[3].toLowerCase());
																		this.land.updateLand(land);
																		sender.sendMessage("§a>>§b" + args[3] + "を共有に追加しました");
																	}else{
																		sender.sendMessage("§a>>§b" + args[3] + "は既に追加されています");
																	}
																}else{
																	sender.sendMessage("§a>>§b貴方はその土地の所有者ではありません");
																}
															}else{
																sender.sendMessage("§a>>§bその土地は存在しません");
															}
														}else{
															sender.sendMessage("§a>>§b/land invi add [土地番号] [名前]");
														}
													}
												}
												catch(ArrayIndexOutOfBoundsException e){
													sender.sendMessage("§a>>§b/land invi add [土地番号] [名前]");
												}
											break;

											case "del":
												try {
													if (args[2] != null && args[3] != null) {
														if (isInt(args[2])) {
															int id = Integer.parseInt(args[2]);
															if (this.land.isLand(id)) {
																Land land = this.land.getLand(id);
																if (land.isOwner(name)) {
																	if (land.isInvite(args[3].toLowerCase())) {
																		land.delInvite(args[3].toLowerCase());
																		this.land.updateLand(land);
																		sender.sendMessage("§a>>§b" + args[3] + "共有から外しました");
																	}else{
																		sender.sendMessage("§a>>§b" + args[3] + "は共有されていません");
																	}
																}else{
																	sender.sendMessage("§a>>§b貴方はその土地の所有者ではありません");
																}
															}else{
																sender.sendMessage("§a>>§bその土地は存在しません");
															}
														}else{
															sender.sendMessage("§a>>§b/land invi del [土地番号] [名前]");
														}
													}
												}
												catch(ArrayIndexOutOfBoundsException e){
													sender.sendMessage("§a>>§b/land invi del [土地番号] [名前]");
												}
											break;

											case "list":
												try {
													if (args[2] != null && isInt(args[2])) {
														int id = Integer.parseInt(args[2]);
														if (this.land.isLand(id)) {
															Land land = this.land.getLand(id);
															String[] inv = land.invite.split(":");
															if (inv.length != 0) {
																sender.sendMessage("§a>>§b共有されている人");
																for (int i = 0; i < inv.length; i++) {
																	sender.sendMessage("§a>>§b" + inv[i]);
																}
															}else{
																sender.sendMessage("§a>>§b共有してる人がいません");
															}
														}else{
															sender.sendMessage("§a>>§bその土地は存在しません");
														}
													}
												}
												catch(ArrayIndexOutOfBoundsException e){
													sender.sendMessage("§a>>§b/land invi list [土地番号]");
												}
											break;

											default:
												sender.sendMessage("§a>>§b/land invi add [土地番号] [名前]");
												sender.sendMessage("§a>>§b/land invi del [土地番号] [名前]");
												sender.sendMessage("§a>>§b/land invi list [土地番号]");
											break;
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/land invi add [土地番号] [名前]");
									sender.sendMessage("§a>>§b/land invi del [土地番号] [名前]");
									sender.sendMessage("§a>>§b/land invi list [土地番号]");
								}
							break;

							default:
								sender.sendMessage("§a>>§b/land pos1                       | 購入する範囲の一つ目を決めます");
								sender.sendMessage("§a>>§b/land pos2                       | 購入する範囲の二つ目の決めます");
								sender.sendMessage("§a>>§b/land buy                        | 土地を購入します");
								sender.sendMessage("§a>>§b/land sell [土地番号]            | 土地を売却します");
								sender.sendMessage("§a>>§b/land give [土地番号] [名前]     | 土地を譲渡します");
								sender.sendMessage("§a>>§b/land move [土地番号]            | 自分の土地にテレポートします");
								sender.sendMessage("§a>>§b/land setmove                    | 立っている土地のテレポートの場所を決めます");
								sender.sendMessage("§a>>§b/land status                     | 自分が保護している土地等を見れます");
								sender.sendMessage("§a>>§b/land here                       | 自分が立っている土地の土地番号を確認します");
								sender.sendMessage("§a>>§b/land show                       | 今いるワールドの自分の土地の範囲を表示します");
								sender.sendMessage("§a>>§b/land invi add [土地番号] [名前] | 指定した土地を共有します");
								sender.sendMessage("§a>>§b/land invi del [土地番号] [名前] | 指定した土地の共有を外します");
								sender.sendMessage("§a>>§b/land invi list [土地番号]       | 指定した土地の共有リストを表示します");
							break;
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/land pos1                       | 購入する範囲の一つ目を決めます");
					sender.sendMessage("§a>>§b/land pos2                       | 購入する範囲の二つ目の決めます");
					sender.sendMessage("§a>>§b/land buy                        | 土地を購入します");
					sender.sendMessage("§a>>§b/land sell [土地番号]            | 土地を売却します");
					sender.sendMessage("§a>>§b/land give [土地番号] [名前]     | 土地を譲渡します");
					sender.sendMessage("§a>>§b/land move [土地番号]            | 自分の土地にテレポートします");
					sender.sendMessage("§a>>§b/land setmove                    | 立っている土地のテレポートの場所を決めます");
					sender.sendMessage("§a>>§b/land status                     | 自分が保護している土地等を見れます");
					sender.sendMessage("§a>>§b/land here                       | 自分が立っている土地の土地番号を確認します");
					sender.sendMessage("§a>>§b/land show                       | 今いるワールドの自分の土地の範囲を表示します");
					sender.sendMessage("§a>>§b/land invi add [土地番号] [名前] | 指定した土地を共有します");
					sender.sendMessage("§a>>§b/land invi del [土地番号] [名前] | 指定した土地の共有を外します");
					sender.sendMessage("§a>>§b/land invi list [土地番号]       | 指定した土地の共有リストを表示します");
				}
			break;
		}
		return false;
	}

	private boolean isInt(String number) {
		try {
			Integer.valueOf(number);
			return true;
		}
		catch(NumberFormatException ex){
			return false;
		}
	}

	public void addLand(Player player) {
		String name = player.getName().toLowerCase();
		String level = player.getLevel().getName();
		HashMap<String, Object> data = new HashMap<String, Object>();
		HashMap<String, Integer> d = preparePosition(player);
		data.put("id", this.id);
		data.put("level", level);
		data.put("x1", d.get("x1"));
		data.put("z1", d.get("z1"));
		data.put("x2", d.get("x2"));
		data.put("z2", d.get("z2"));
		data.put("owner", name);
		data.put("invite", "");
		data.put("time", System.currentTimeMillis());
		data.put("position", String.valueOf(d.get("x1")) + ":" + String.valueOf((int) player.y) + ":" + String.valueOf(d.get("z1")));
		this.pos1.remove(name);
		this.pos2.remove(name);
		this.land.addLand(data);
		this.id++;
		this.config.set("ID", this.id);
		this.config.save();
	}

	public void delLand(int id) {
		this.land.delLand(id);
	}

	public boolean canBuy(Player player) {
		HashMap<String, Boolean> r = new HashMap<String, Boolean>();
		String name = player.getName().toLowerCase();
		String level = player.getLevel().getName();
		HashMap<String, Integer> data = preparePosition(player);
		int x1 = data.get("x1");
		int z1 = data.get("z1");
		int x2 = data.get("x2");
		int z2 = data.get("z2");
		r.put(name, true);
		int x, z;
		for (x = x1; x >= x2; x--) {
			for (z = z1; z >=z2; z--) {
				int x3 = x;
				int z3 = z;
				if (this.land.isLand(x3, z3, level)) {
					r.put(name, false);
				}
			}
		}
		return r.get(name);
	}

	public long getCount(Player player) {
		HashMap<String, Integer> data = preparePosition(player);
		int x1 = data.get("x1");
		int z1 = data.get("z1");
		int x2 = data.get("x2");
		int z2 = data.get("z2");
		int x = x1 - x2 + 1;
		int z = z1 - z2 + 1;
		return  (long) (x * z * (int)this.co.get("土地一マスの値段"));
	}

	public HashMap<String, Integer> preparePosition(Player player) {
		String name = player.getName().toLowerCase();
		Integer[] pos1 = this.pos1.get(name);
		Integer[] pos2 = this.pos2.get(name);
		int x1 = pos1[0];
		int z1 = pos1[1];
		int x2 = pos2[0];
		int z2 = pos2[1];
		int ts;
		if (x1 < x2) {
			ts = x1;
			x1 = x2;
			x2 = ts;
		}
		if (z1 < z2) {
			ts = z1;
			z1 = z2;
			z2 = ts;
		}
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		data.put("x1", x1);
		data.put("z1", z1);
		data.put("x2", x2);
		data.put("z2", z2);
		return data;
	}

	public int getBlockCount(Player player) {
		HashMap<String, Integer> data = preparePosition(player);
		int x1 = data.get("x1");
		int z1 = data.get("z1");
		int x2 = data.get("x2");
		int z2 = data.get("z2");
		int x = x1 - x2 + 1;
		int z = z1 - z2 + 1;
		return x * z;
	}

	@SuppressWarnings("unchecked")
	public boolean isSide() {
		HashMap<String, Object> data = (HashMap<String, Object>) co.get("保護できる土地の1辺の大きさを決めるか");
		if ((boolean) data.get("有効させるか")) {
			return true;
		}else{
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean hasSide(Player player) {
		HashMap<String, Integer> pos = preparePosition(player);
		HashMap<String, Object> data = (HashMap<String, Object>) co.get("保護できる土地の1辺の大きさを決めるか");
		if ((boolean) data.get("有効させるか")) {
			if (pos.get("x1") - pos.get("x2") + 1 >= (int) data.get("保護できる最小の1辺のブロックの数") && pos.get("x1") - pos.get("x2") + 1 <= (int) data.get("保護できる最大の1辺のブロックの数")) {
				if (pos.get("z1") - pos.get("z2") + 1 >= (int) data.get("保護できる最小の1辺のブロックの数") && pos.get("z1") - pos.get("z2") + 1 <= (int) data.get("保護できる最大の1辺のブロックの数")) {
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

	@SuppressWarnings("unchecked")
	public boolean canProtectLevel(Player player) {
		HashMap<String, Object> data = (HashMap<String, Object>) this.co.get("保護できるワールドを指定するか");
		if ((boolean) data.get("有効させるか")) {
			String level = player.getLevel().getName();
			String[] levels = ((String) data.get("保護できるワールド")).split(":");
			for (int i = 0; i < levels.length; i++) {
				if (levels[i].equals(level)) {
					return true;
				}
			}
			return false;
		}else{
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean canBleakLevel(Player player) {
		HashMap<String, Object> data = (HashMap<String, Object>) this.co.get("保護できるワールドを指定するか");
		if ((boolean) data.get("有効させるか")) {
			String level = player.getLevel().getName();
			String[] levels = ((String) data.get("保護できるワールド")).split(":");
			for (int i = 0; i < levels.length; i++) {
				if (levels[i].equals(level)) {
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}

	public void showBlock(Player player) {
		HashMap<String, Integer> data = preparePosition(player);
		int x1 = data.get("x1");
		int z1 = data.get("z1");
		int x2 = data.get("x2");
		int z2 = data.get("z2");
		int y = (int) (player.y - 1);
		Level level = player.getLevel();
		Block block;
		block = getShowBlock(x1, y, z1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 41);
		block = getShowBlock(x1, y, z2, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 41);
		block = getShowBlock(x2, y, z1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 41);
		block = getShowBlock(x2, y, z2, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 41);
		block = getShowBlock(x1 - 1, y, z1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x1, y, z1 - 1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x1 - 1, y, z2, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x1, y, z2 + 1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x2 + 1, y, z1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x2, y, z1 - 1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x2 + 1, y, z2, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x2, y, z2 + 1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
	}

	public void showBlock(Player player, Land land) {
		int x1 = land.x1;
		int z1 = land.z1;
		int x2 = land.x2;
		int z2 = land.z2;
		int y = (int) (player.y - 1);
		Level level = player.getLevel();
		Block block;
		block = getShowBlock(x1, y, z1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 41);
		block = getShowBlock(x1, y, z2, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 41);
		block = getShowBlock(x2, y, z1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 41);
		block = getShowBlock(x2, y, z2, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 41);
		block = getShowBlock(x1 - 1, y, z1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x1, y, z1 - 1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x1 - 1, y, z2, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x1, y, z2 + 1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x2 + 1, y, z1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x2, y, z1 - 1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x2 + 1, y, z2, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
		block = getShowBlock(x2, y, z2 + 1, level);
		sendBlock(player, (int) block.x, (int) block.y, (int) block.z, 89);
	}

	public Block getShowBlock(int x, int y, int z, Level level) {
		Block retu = level.getBlock(new Vector3(x, y, z));
		if (retu.getId() != 0) {
			for (int i = y; i < y + 20; i ++) {
				Block block = level.getBlock(new Vector3(x, i, z));
				if (block.getId() == 0) {
					return level.getBlock(new Vector3(x, i - 1, z));
				}
			}
		}else{
			for (int i = y - 1; i > y - 20; i --) {
				Block block = level.getBlock(new Vector3(x, i, z));
				if (block.getId() != 0) {
					return block;
				}
			}
		}
		return retu;
	}

	public void sendBlock(Player player, int x, int y, int z, int id) {
		UpdateBlockPacket pk = new UpdateBlockPacket();
		pk.x = x;
		pk.y = y;
		pk.z = z;
		pk.blockId = id;
		pk.blockData = 0;
		pk.flags = UpdateBlockPacket.FLAG_NONE;
		player.dataPacket(pk);
	}

	public void CountTask() {
    	getServer().getOnlinePlayers().forEach((UUID, player) -> {
    		String name = player.getName().toLowerCase();
    		int count = this.blockcount.get(name);
    		this.blockcount.put(name, count + 1);
    	});
	}
}
