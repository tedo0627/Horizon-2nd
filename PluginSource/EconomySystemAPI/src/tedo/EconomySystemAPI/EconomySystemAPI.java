package tedo.EconomySystemAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class EconomySystemAPI extends PluginBase implements Listener{

	public Config config, data;
	public long configdata;
	public HashMap<String, Long> money = new HashMap<String, Long>();
	public ArrayList<String> login = new ArrayList<String>();

	@SuppressWarnings("deprecation")
	public void onEnable(){
//		Command command = getServer().getCommandMap().getCommand("money");
//			command.addCommandParameters("default", new CommandParameter[]{});
//			command.addCommandParameters("see", new CommandParameter[]{
//					new CommandParameter("see", new String[]{"see"}),
//					new CommandParameter("player", CommandParameter.ARG_TYPE_PLAYER, false),
//			});
//			command.addCommandParameters("pay", new CommandParameter[]{
//					new CommandParameter("pay", new String[]{"pay"}),
//					new CommandParameter("player", CommandParameter.ARG_TYPE_PLAYER, false),
//					new CommandParameter("money", CommandParameter.ARG_TYPE_INT, false),
//			});
//			command.addCommandParameters("top", new CommandParameter[]{
//					new CommandParameter("top", new String[]{"top"}),
//					new CommandParameter("page", CommandParameter.ARG_TYPE_INT, false),
//			});
//			command.addCommandParameters("add", new CommandParameter[]{
//					new CommandParameter("add", new String[]{"add"}),
//					new CommandParameter("player", CommandParameter.ARG_TYPE_PLAYER, false),
//					new CommandParameter("money", CommandParameter.ARG_TYPE_INT, false),
//			});
//			command.addCommandParameters("del", new CommandParameter[]{
//					new CommandParameter("del", new String[]{"del"}),
//					new CommandParameter("player", CommandParameter.ARG_TYPE_PLAYER, false),
//					new CommandParameter("money", CommandParameter.ARG_TYPE_INT, false),
//			});
//			command.addCommandParameters("set", new CommandParameter[]{
//					new CommandParameter("set", new String[]{"set"}),
//					new CommandParameter("player", CommandParameter.ARG_TYPE_PLAYER, false),
//					new CommandParameter("money", CommandParameter.ARG_TYPE_INT, false),
//			});
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getDataFolder().mkdirs();
		this.data = new Config(new File(this.getDataFolder(), "data.yml"),Config.YAML);
		this.config = new Config(new File(this.getDataFolder(), "config.yml"),Config.YAML,
		new LinkedHashMap<String, Object>() {
			{
				put("初期金額", (long) 1000L);
			}
		}
		);
		this.data.getAll().forEach((name, money) -> {
			try {
				if (money == null) {
					this.money.put(name, this.configdata);
				}else{
					int m1 = (int) money;
					long m2 = (long) m1;
					this.money.put(name, m2);
				}
			}
			catch (ClassCastException e) {
				this.money.put(name, this.configdata);
			}
		});
		this.configdata = Long.parseLong(String.valueOf(this.config.get("初期金額")));
		getServer().getScheduler().scheduleRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				save();
			}
		}, 20 * 60 * 10);
	}

	public void onDisable() {
		save();
	}

	public void save() {
		this.login.forEach((name) -> this.data.set(name, this.money.get(name)));
		this.data.save();
	}

	@EventHandler
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		if (!isMoney(name)) {
			this.money.put(name, this.configdata);
			getMoney(name);
		}
		this.login.add(name);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		this.data.set(name, this.money.get(name));
		this.data.save();
	}

	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		switch(command.getName()){
			case "money":
				try{
					if(args[0] != null){
						switch (args[0]) {
							case "see":
								try{
									if(args[1] != null){
										String name = getName(args[1]);
										if (isMoney(name)) {
											long money = getMoney(name);
											sender.sendMessage("§a>>§b" + name + "は" + money + "円所持してます");
										}else{
											sender.sendMessage("§a>>§b" + args[1] + "は存在しません");
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/money see [名前]");
								}
							break;

							case "top":
								HashMap<String, Integer> page = new HashMap<String, Integer>();
								page.put("page", 1);
								try{
									if (args[0] != null) {
										int h = this.money.size() / 5 + 1;
										if (isLong(args[0])) {
											int i = Integer.parseInt(args[0]);
											if (i <= h) {
												page.put("page", i);
											}else{
												page.put("page", h);
											}
										}
										sender.sendMessage("§e====所持金ランキング" + page.get("page") + "/" + h + "====");
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									int h = this.money.size() / 5;
									if (this.money.size() % 5 != 0) {
										h++;
									}
									sender.sendMessage("§e====所持金ランキング" + page.get("page") + "/" + h + "====");
								}
								int p = page.get("page");
								List<Entry<String, Long>> top = new ArrayList<Entry<String, Long>>(this.money.entrySet());
								Collections.sort(top, new Comparator<Entry<String, Long>>() {
									public int compare(Entry<String, Long> obj1, Entry<String, Long> obj2) {
										return obj2.getValue().compareTo(obj1.getValue());
									}
								});
								int c = 1;
								for (Entry<String, Long> entry : top) {
									if (c >= p * 5 - 4 && c <= p * 5) {
										sender.sendMessage("§b" + c + "位 | " + entry.getKey() + " : " + entry.getValue() + "円");
									}
									c++;
								}
							break;

							case "pay":
								if (sender instanceof Player) {
									try {
										if(args[1] != null && args[2] != null){
											String name = getName(args[1]);
											if (isLong(args[2])) {
												if (isMoney(name)) {
													long money = getMoney(name);
													long data = Long.parseLong(args[2]);
													if (data > 0) {
														if (money >= data) {
															delMoney(sender.getName().toLowerCase(), data);
															addMoney(name, data);
															sender.sendMessage("§a>>§b" + name + "に" + data + "円渡しました");
														}else{
															sender.sendMessage("§a>>§b貴方はそんなにお金を持っていません");
														}
													}else{
														sender.sendMessage("§a>>§b0円より多くしてください");
													}
												}else{
													sender.sendMessage("§a>>§b" + args[1] + "は存在しません");
												}
											}else{
												sender.sendMessage("§a>>§b/money pay [名前] [金額]");
											}
										}
									}
									catch(ArrayIndexOutOfBoundsException e){
										sender.sendMessage("§a>>§b/money pay [名前] [金額]");
									}
								}else{
									sender.sendMessage("§a>>§bこのコマンドはコンソールから実行できません");
								}
							break;

							case "add":
								if (!(sender instanceof Player) || sender.isOp()) {
									try{
										if(args[1] != null && args[2] != null){
											String name = getName(args[1]);
											if (isLong(args[2])) {
												if (isMoney(name)) {
													long money = Long.parseLong(args[2]);
													addMoney(name, money);
													sender.sendMessage("§a>>§b" + name + "に" + money + "円渡しました");
												}else{
													sender.sendMessage("§a>>§b" + args[1] + "は存在しません");
												}
											}else{
												sender.sendMessage("§a>>§b/money add [名前] [金額]");
											}
										}
									}
									catch(ArrayIndexOutOfBoundsException e){
										sender.sendMessage("§a>>§b/money add [名前] [金額]");
									}
								}else{
									sender.sendMessage("§a>>§b貴方はこのコマンドを使用する権限がありません");
								}
							break;

							case "del":
								if (!(sender instanceof Player) || sender.isOp()) {
									try{
										if(args[1] != null && args[2] != null){
											String name = getName(args[1]);
											if (isLong(args[2])) {
												if (isMoney(name)) {
													long money = Long.parseLong(args[2]);
													if (money <= getMoney(name)) {
														delMoney(name, money);
														sender.sendMessage("§a>>§b" + name + "から" + money + "円没収しました");
													}else{
														sender.sendMessage("§a>>§b" + name + "はそんなにお金を所持していません");
													}
												}else{
													sender.sendMessage("§a>>§b" + args[1] + "は存在しません");
												}
											}else{
												sender.sendMessage("§a>>§b/money del [名前] [金額]");
											}
										}
									}
									catch(ArrayIndexOutOfBoundsException e){
										sender.sendMessage("§a>>§b/money del [名前] [金額]");
									}
								}else{
									sender.sendMessage("§a>>§b貴方はこのコマンドを使用する権限がありません");
								}
							break;

							case "set":
								if (!(sender instanceof Player) || sender.isOp()) {
									try{
										if(args[1] != null && args[2] != null){
											String name = getName(args[1]);
											if (isLong(args[2])) {
												if (isMoney(name)) {
													long money = Long.parseLong(args[2]);
													setMoney(name, money);
													sender.sendMessage("§a>>§b" + name + "の所持金を" + money + "円にしました");
												}else{
													sender.sendMessage("§a>>§b" + args[1] + "は存在しません");
												}
											}else{
												sender.sendMessage("§a>>§b/money set [名前] [金額]");
											}
										}
									}
									catch(ArrayIndexOutOfBoundsException e){
										sender.sendMessage("§a>>§b/money set [名前] [金額]");
									}
								}else{
									sender.sendMessage("§a>>§b貴方はこのコマンドを使用する権限がありません");
								}
							break;

							default:
								sender.sendMessage("§a>>§b/money                   | 自分の所持金を表示します");
								sender.sendMessage("§a>>§b/money see [名前]        | ほかの人の所持金を表示します");
								sender.sendMessage("§a>>§b/money pay [名前] [金額] | お金を相手に渡します");
								sender.sendMessage("§a>>§b/money top [ページ数]    | 所持金ランキングを表示します");
								if (!(sender instanceof Player) || sender.isOp()) {
									sender.sendMessage("§a>>§b/money add [名前] [金額] | 指定したプレイヤーの所持金を増やします");
									sender.sendMessage("§a>>§b/money del [名前] [金額] | 指定したプレイヤーの所持金を減らします");
									sender.sendMessage("§a>>§b/money set [名前] [金額] | 指定したプレイヤーの所持金をセットします");
								}
							break;
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					if (sender instanceof Player) {
						String name = sender.getName();
						long money = getMoney(name);
						sender.sendMessage("§a>>§b貴方は" + money + "円所持してます");
					}else{
						sender.sendMessage("§a>>§b/money                   | 自分の所持金を表示します");
						sender.sendMessage("§a>>§b/money see [名前]        | ほかの人の所持金を表示します");
						sender.sendMessage("§a>>§b/money pay [名前] [金額] | お金を相手に渡します");
						sender.sendMessage("§a>>§b/money top [ページ数]    | 所持金ランキングを表示します");
						if (!(sender instanceof Player) || sender.isOp()) {
							sender.sendMessage("§a>>§b/money add [名前] [金額] | 指定したプレイヤーの所持金を増やします");
							sender.sendMessage("§a>>§b/money del [名前] [金額] | 指定したプレイヤーの所持金を減らします");
							sender.sendMessage("§a>>§b/money set [名前] [金額] | 指定したプレイヤーの所持金をセットします");
						}
					}
				}
			break;
		}
		return false;
	}

	private boolean isLong(String number) {
		try {
			Long.parseLong(number);
			return true;
		}
		catch(NumberFormatException ex){
			return false;
		}
	}

	private String getName(String name) {
		Player player = getServer().getPlayer(name);
		if (player instanceof Player) {
			name = player.getName().toLowerCase();
		}else{
			name = name.toLowerCase();
		}
		return name;
	}

	//API

	public Boolean isMoney(String name) {
		name = name.toLowerCase();
		if (this.money.containsKey(name)) {
			return true;
		}else{
			return false;
		}
	}

	public long getMoney(String name) {
		name = name.toLowerCase();
		long money = (long) this.money.get(name);
		return money;
	}

	public void addMoney(String name, long money) {
		name = name.toLowerCase();
		long data = getMoney(name);
		data = data + money;
		this.money.put(name, data);
	}

	public void delMoney(String name, long money) {
		name = name.toLowerCase();
		long data = getMoney(name);
		data = data - money;
		this.money.put(name, data);
	}

	public void setMoney(String name, long money) {
		name = name.toLowerCase();
		this.money.put(name, money);
	}
}