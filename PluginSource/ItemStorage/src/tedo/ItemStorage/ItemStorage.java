package tedo.ItemStorage;

import java.io.File;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class ItemStorage extends PluginBase implements Listener{


		private Config config;
		@SuppressWarnings("rawtypes")
		private HashMap<String, HashMap> data = new HashMap<String, HashMap>();

		@SuppressWarnings("rawtypes")
		public void onEnable(){
			this.getServer().getPluginManager().registerEvents(this, this);
			this.getDataFolder().mkdirs();
			this.config = new Config(new File(this.getDataFolder(), "data.yml"),Config.YAML);
			this.config.getAll().forEach((name, data) -> this.data.put(name, (HashMap) data));
		}

		public void onDisable() {
			this.data.forEach((name, data) -> this.config.set(name, data));
			this.config.save();
		}

		@EventHandler
		public void onPlayerJoin(PlayerJoinEvent event) {
			Player player = event.getPlayer();
			String name = player.getName().toLowerCase();
			if (!(isStorage(name))) {
				HashMap<String, Integer> items = new HashMap<String, Integer>();
				this.data.put(name, items);
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
			switch(command.getName()){
				case "item":
					try{
						if(args[0] != null){
							String name = sender.getName().toLowerCase();
							switch (args[0]) {
								case "up":
									try{
										if (args[1] != null) {
											if (args[2] != null) {
												if (isInt(args[2])) {
													try{
														Item i = Item.fromString(args[1]);
														int id = i.getId();
														int damage = i.getDamage();
														int count = Integer.parseInt(args[2]);
														Item item = Item.get(id, damage, count);
														Player player = (Player) sender;
														if (player.getInventory().contains(item)) {
															player.getInventory().removeItem(item);
															addItem(name, id, damage, count);
															sender.sendMessage("§a>>§bストレージにアイテムを送りました");
														}else{
															sender.sendMessage("§a>>§b貴方はそんなに指定したアイテムを持っていません");
														}
													}
													catch(ArrayIndexOutOfBoundsException e){
														sender.sendMessage("§a>>§b/item up [ID] [個数]");
													}
												}else{
													sender.sendMessage("§a>>§b/item up [ID] [個数]");
												}
											}else{
												sender.sendMessage("§a>>§b/item up [ID] [個数]");
											}
										}
									}
									catch(ArrayIndexOutOfBoundsException e){
										sender.sendMessage("§a>>§b/item up [ID] [個数]");
									}
								break;

								case "dl":
									try{
										if (args[1] != null) {
											if (args[2] != null && isInt(args[2])) {
												try{
													Item i = Item.fromString(args[1]);
													int id = i.getId();
													int damage = i.getDamage();
													int count = Integer.parseInt(args[2]);
													Item item = Item.get(id, damage, count);
													Player player = (Player) sender;
													if (isItem(name, id, damage, count)) {
														if (player.getInventory().canAddItem(item)) {
															player.getInventory().addItem(item);
															delItem(name, id, damage, count);
															sender.sendMessage("§a>>§bストレージからアイテムを回収しました");
														}else{
															sender.sendMessage("§a>>§b貴方のインベントリーに入りきりません");
														}
													}
												}
												catch(ArrayIndexOutOfBoundsException e){
													sender.sendMessage("§a>>§b/item up [ID] [個数]");
												}
											}else{
												sender.sendMessage("§a>>§bストレージにそんなにアイテムはありません");
											}
										}
									}
									catch(ArrayIndexOutOfBoundsException e){
										sender.sendMessage("§a>>§b/item dl [ID] [個数]");
									}
								break;

								case "list":
									try {
										if (isStorage(name)) {
											HashMap storage = getStorage(name);
											sender.sendMessage("§a>>§bアイテムリスト");
											storage.forEach((id, count) ->
											sender.sendMessage("§a>>§b" + Item.fromString((String) id).getName() + " | " + id + " | " + count + "個"));
										}
									}
									catch(ArrayIndexOutOfBoundsException e){
										sender.sendMessage("§a>>§b貴方はストレージにまだなにも保存していません");
									}
								break;

								case "count":
									try {
										if (args[1] != null) {
											Item item = Item.fromString(args[1]);
											if (isItem(name, item.getId(), item.getDamage(), 0)) {
												int i = getItem(name, item.getId(), item.getDamage());
												sender.sendMessage("§a>>§b" + item.getName() + "は" + String.valueOf(i) + "個あります");
											}else{
												sender.sendMessage("§a>>§b" + item.getName() + "はストレージにありません");
											}
										}
									}
									catch(ArrayIndexOutOfBoundsException e){
										sender.sendMessage("§a>>§b/item count  [ID]");
									}
								break;

								default:
									sender.sendMessage("§a>>§b/item up [ID] [個数] | ストレージにアイテムを追加します");
									sender.sendMessage("§a>>§b/item dl [ID] [個数] | ストレージからアイテムを引き出します");
									sender.sendMessage("§a>>§b/item count  [ID]    | 指定したIDのものがいくつ保存されているか表示します");
									sender.sendMessage("§a>>§b/item list           | 保存されているもののリストを表示します");
								break;
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e){
						sender.sendMessage("§a>>§b/item up [ID] [個数] | ストレージにアイテムを追加します");
						sender.sendMessage("§a>>§b/item dl [ID] [個数] | ストレージからアイテムを引き出します");
						sender.sendMessage("§a>>§b/item count  [ID]    | 指定したIDのものがいくつ保存されているか表示します");
						sender.sendMessage("§a>>§b/item list           | 保存されているもののリストを表示します");
					}
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

		//API

		public Boolean isStorage(String name) {
			name = name.toLowerCase();
			if (this.data.containsKey(name)) {
				return true;
			}else{
				return false;
			}
		}

		@SuppressWarnings("rawtypes")
		public HashMap getStorage(String name) {
			name = name.toLowerCase();
			HashMap r = this.data.get(name);
			return r;
		}

		@SuppressWarnings("rawtypes")
		public boolean isItem(String name, int id, int damage, int count) {
			name = name.toLowerCase();
			if (isStorage(name)) {
				HashMap storage = getStorage(name);
				String id1 = String.valueOf(id);
				String damage1 = String.valueOf(damage);
				id1 = id1 + ":" + damage1;
				if (storage.containsKey(id1)) {
					if (getItem(name, id, damage) >= count) {
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

		@SuppressWarnings("rawtypes")
		public int getItem(String name, int id, int damage) {
			name = name.toLowerCase();
			HashMap storage = getStorage(name);
			String id1 = String.valueOf(id);
			String damage1 = String.valueOf(damage);
			id1 = id1 + ":" + damage1;
			int count;
			if (storage.containsKey(id1)) {
				count = (int) storage.get(id1);
			}else{
				count = 0;
			}
			return count;
		}

		public void addItem(String name, int id, int damage, int count) {
			name = name.toLowerCase();
			int c = getItem(name, id, damage);
			c = c + count;
			setItem(name, id, damage, c);
		}

		public void delItem(String name, int id, int damage, int count) {
			name = name.toLowerCase();
			int c = getItem(name, id, damage);
			c = c - count;
			if (c != 0) {
				setItem(name, id, damage, c);
			}else{
				removeItem(name, id, damage);
			}
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void setItem(String name, int id, int damage, int count) {
			name = name.toLowerCase();
			HashMap storage = getStorage(name);
			String id1 = String.valueOf(id);
			String damage1 = String.valueOf(damage);
			id1 = id1 + ":" + damage1;
			storage.put(id1, count);
			this.data.put(name, storage);
		}

		@SuppressWarnings("rawtypes")
		public void removeItem(String name, int id, int damage) {
			name = name.toLowerCase();
			HashMap storage = getStorage(name);
			String id1 = String.valueOf(id);
			String damage1 = String.valueOf(damage);
			id1 = id1 + ":" + damage1;
			storage.remove(id1);
			this.data.put(name, storage);
		}
	}

