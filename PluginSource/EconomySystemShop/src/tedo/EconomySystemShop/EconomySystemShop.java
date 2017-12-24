package tedo.EconomySystemShop;

import java.io.File;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import tedo.EconomySystemAPI.EconomySystemAPI;

public class EconomySystemShop extends PluginBase implements Listener{

	public Config data;
	public HashMap<String, HashMap<String, HashMap<String, String>>> sign = new HashMap<String, HashMap<String, HashMap<String, String>>>();
	public HashMap<String, String> pos = new HashMap<String, String>();

	public EconomySystemAPI economy;

	@SuppressWarnings("unchecked")
	public void onEnable(){
		this.economy = (EconomySystemAPI) getServer().getPluginManager().getPlugin("EconomySystemAPI");
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getDataFolder().mkdirs();
		this.data = new Config(new File(this.getDataFolder(), "data.yml"),Config.YAML);
		this.data.getAll().forEach((level, data) -> this.sign.put(level, (HashMap<String, HashMap<String, String>>) data));

		if (!(this.sign.containsKey(getServer().getDefaultLevel().getName()))) {
			HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
			this.sign.put(getServer().getDefaultLevel().getName(), data);
		}
	}

	public void onDisable() {
		this.sign.forEach((level, data) -> this.data.set(level, data));
		this.data.save();
	}

	@EventHandler
	public void onlevelLoad(LevelLoadEvent event) {
		Level level = event.getLevel();
		String name = level.getName();
		if (!(this.sign.containsKey(name))) {
			HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
			this.sign.put(name, data);
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		String x = String.valueOf((int) block.x);
		String y = String.valueOf((int) block.y);
		String z = String.valueOf((int) block.z);
		String level = player.getLevel().getName();
		String[] text = event.getLines();
		try {
			if (text[0] != null && text[1] != null && text[2] != null && text[3] != null) {
				switch (text[0]) {
					case "shop":
						if (player.isOp()) {
							if (isInt(text[1])) {
								if (isInt(text[3])) {
									String money = text[1];
									Item item = Item.fromString(text[2]);
									String id = String.valueOf(item.getId());
									String damage = String.valueOf(item.getDamage());
									String amount = text[3];
									if (Long.parseLong(amount) > 0) {
										event.setLine(0, "§b[shop]");
										event.setLine(1, "§e値段 : " + money + "円");
										event.setLine(2, "§aアイテム : " + item.getName());
										event.setLine(3, "§a個数 : " + amount + "個");
										HashMap<String, String> data = new HashMap<String, String>();
										String pos = x + ":" + y + ":" + z;
										data.put("data", "shop");
										data.put("money", money);
										data.put("id", id);
										data.put("damage", damage);
										data.put("amount", amount);
										HashMap<String, HashMap<String, String>> d = (HashMap<String, HashMap<String, String>>) this.sign.get(level);
										d.put(pos, data);
										this.sign.put(level, d);
										player.sendMessage("§a>>§bshop看板を作成しました");
									}else{
										player.sendMessage("§a>>§b個数は0個以上にしてください");
									}
								}else{
									player.sendMessage("§a>>§b四行目には数字を入力してください");
								}
							}else{
								player.sendMessage("§a>>§b二行目には数字を入力してください");
							}
						}
					break;

					case "sell":
						if (player.isOp()) {
							if (isInt(text[1])) {
								if (isInt(text[3])) {
									String money = text[1];
									Item item = Item.fromString(text[2]);
									String id = String.valueOf(item.getId());
									String damage = String.valueOf(item.getDamage());
									String amount = text[3];
									if (Integer.parseInt(amount) > 0) {
										event.setLine(0, "§b[sell]");
										event.setLine(1, "§e値段 : " + money + "円");
										event.setLine(2, "§aアイテム : " + item.getName());
										event.setLine(3, "§a個数 : " + amount + "個");
										HashMap<String, String> data = new HashMap<String, String>();
										String pos = x + ":" + y + ":" + z;
										data.put("data", "sell");
										data.put("money", money);
										data.put("id", id);
										data.put("damage", damage);
										data.put("amount", amount);
										HashMap<String, HashMap<String, String>> d = (HashMap<String, HashMap<String, String>>) this.sign.get(level);
										d.put(pos, data);
										this.sign.put(level, d);
										player.sendMessage("§a>>§bsell看板を作成しました");
									}else{
										player.sendMessage("§a>>§b個数は0個以上にしてください");
									}
								}else{
									player.sendMessage("§a>>§b四行目には数字を入力してください");
								}
							}else{
								player.sendMessage("§a>>§b二行目には数字を入力してください");
							}
						}
					break;
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e){

		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Block block = event.getBlock();
		if (block.getId() == 63 || block.getId() == 68) {
			String x = String.valueOf((int) block.x);
			String y = String.valueOf((int) block.y);
			String z = String.valueOf((int) block.z);
			String level = block.getLevel().getName();
			String pos = x + ":" + y + ":" + z;
			if (this.sign.containsKey(level)) {
				HashMap<String, HashMap<String, String>> data = this.sign.get(level);
				if (data.containsKey(pos)) {
					HashMap<String, String> d = (HashMap<String, String>) data.get(pos);
					Player player = event.getPlayer();
					String name = player.getName().toLowerCase();
					event.setCancelled();
					if (this.pos.containsKey(name) && this.pos.get(name).equals(pos)) {
						long money;
						switch ((String) d.get("data")) {
							case "shop":
								money = Long.parseLong((String) d.get("money"));
								if (money <= this.economy.getMoney(name)) {
									int id = Integer.parseInt((String) d.get("id"));
									int damage = Integer.parseInt((String) d.get("damage"));
									int amount = Integer.parseInt((String) d.get("amount"));
									Item item = Item.get(id, damage, amount);
									if (player.getInventory().canAddItem(item)) {
										this.economy.delMoney(name, money);
										this.pos.remove(name);
										player.getInventory().addItem(item);
										player.sendPopup("§b購入しました");
									}else{
										player.sendMessage("§a>>§bインベントリーがいっぱいです");
									}
								}else{
									player.sendMessage("§a>>§bお金が足りません");
								}
							break;

							case "sell":
								int id = Integer.parseInt((String) d.get("id"));
								int damage = Integer.parseInt((String) d.get("damage"));
								int amount = Integer.parseInt((String) d.get("amount"));
								Item item = Item.get(id, damage, amount);
								if (player.getInventory().contains(item)) {
									money = Long.parseLong((String) d.get("money"));
									this.economy.addMoney(name, money);
									this.pos.remove(name);
									player.getInventory().removeItem(item);
									player.sendPopup("§b売却しました");
								}else{
									player.sendMessage("§a>>§bインベントリーにアイテムがありません");
								}
							break;
						}
					}else{
						switch ((String) d.get("data")) {
							case "shop":
								this.pos.put(name, pos);
								player.sendPopup("§b購入する場合はもう一度タップしてください");
							break;

							case "sell":
								this.pos.put(name, pos);
								player.sendPopup("§b売却する場合はもう一度タップしてください");
							break;
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getId() == 63 || block.getId() == 68) {
			String x = String.valueOf((int) block.x);
			String y = String.valueOf((int) block.y);
			String z = String.valueOf((int) block.z);
			String level = block.getLevel().getName();
			String pos = x + ":" + y + ":" + z;
			if (this.sign.containsKey(level)) {
				HashMap<String, HashMap<String, String>> data = this.sign.get(level);
				if (data.containsKey(pos)) {
					Player player = event.getPlayer();
					if (player.isOp()) {
						data.remove(pos);
						this.sign.put(level, data);
						player.sendMessage("§a>>§bshop系の看板を破壊しました");
					}else{
						event.setCancelled();
						player.sendPopup("§bこの看板は破壊できません");
					}
				}
			}
		}
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
}
