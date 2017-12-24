package tedo.GatyaSystem;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHumanType;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityArmorChangeEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Config;
import tedo.GatyaSystem.prize.BigLuck;
import tedo.GatyaSystem.prize.HorizonRare;
import tedo.GatyaSystem.prize.HorizonRarePlus;
import tedo.GatyaSystem.prize.Luck;
import tedo.ItemStorage.ItemStorage;

public class GatyaSystem extends PluginBase implements Listener{

	public Config config;
	public ItemStorage item;
	public HashMap<String, Integer> data = new HashMap<String, Integer>();
	public HashMap<String, Boolean> prize = new HashMap<String, Boolean>();

	public void onEnable(){
		this.item = (ItemStorage) getServer().getPluginManager().getPlugin("ItemStorage");
		if (this.item !=  null) {
			this.getServer().getPluginManager().registerEvents(this, this);
			this.getDataFolder().mkdirs();
			this.config = new Config(new File(this.getDataFolder(), "data.yml"),Config.YAML);
			this.config.getAll().forEach((name, data) -> this.data.put(name, (Integer) data));
			new HorizonRarePlus("クリエイティブ");
			new HorizonRare("クリエイティブ");
			new BigLuck("クリエイティブ");
			new Luck("クリエイティブ");
			Item.removeCreativeItem(Item.get(388));
		}else{
			getServer().getLogger().info("§a>>§bItemStorageが見つかりませんでした");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	public void onDisable() {
		this.data.forEach((name, data) -> this.config.set(name, data));
		this.config.save();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		if (!(this.data.containsKey(name))) {
			this.data.put(name, 0);
		}
	}

	@EventHandler
	public void onPlayerIntaract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		PlayerInventory inv = player.getInventory();
		Item item = inv.getItemInHand();
		int count = 0;
		Block block = event.getBlock();
		if (item.getId() == 388) {
			if (player.isSneaking()) {
				for (int i = item.getCount(); i > 0; i--) {
					if (Gatya(player)) {
						count++;
					}
				}
				inv.setItemInHand(Item.get(0));
				player.sendMessage("§a>>§b" + item.getCount() + "回引き、" + String.valueOf(count) + "個はずれました；；");
			}else{
				if (Gatya(player)) {
					player.sendMessage("§a>>§bはずれました；；");
				}
				int c = item.getCount();
				if (c == 1) {
					inv.setItemInHand(Item.get(0));
				}else{
					item.setCount(c - 1);
					inv.setItemInHand(item);
				}
			}
		}
		if (block.getId() == 246) {
			String f = "\n";
			if (hasItem(inv)) {
				if (item.getId() != 0 && item.hasCompoundTag() && item.getNamedTag().contains("R") && item.getNamedTag().getCompound("R").getInt("R") == 500) {
					String s1 = "§l§3K§bR§aO§dN§cO§6S§r" + f + f + "§fNo.001" + f + "§7耐久無限" + f + "§7Rank:§eHR";
					String s2 = "§l§3B§bR§aI§dO§cN§6A§eC§r" + f + f + "§fNo.002" + f + "§7耐久無限" + f + "§7Rank:§eHR";
					String s3 = "§l§3R§bI§aS§dA§cN§6A§eU§fT§r" + f + f + "§fNo.003" + f + "§7耐久無限" + f + "§7Rank:§eHR";
					String name = player.getName().toLowerCase();
					if (item.getCustomName().equals(s1)) {
						if (new Random().nextInt(10) == 0) {
							new HorizonRarePlus(name).add1(player);
							player.sendMessage("§a>>§b覚醒させました");
						}else{
							player.sendMessage("§a>>§b失敗しました");
						}
						HashMap<String, Boolean> r = new HashMap<String, Boolean>();
						r.put("a", false);
						inv.getContents().forEach((i, it) ->{
							if (it.hasCompoundTag() && it.getCustomName().equals("§e覚醒石") && it.getNamedTag().contains("Data")) {
								if (!(r.get("a"))) {
									r.put("a", true);
									if (it.getCount() == 1) {
										it = Item.get(0);
									}else{
										it.setCount(it.getCount() - 1);
									}
									inv.setItem(i, it);
								}
							}
						});
					}else if (item.getCustomName().equals(s2)) {
						if (new Random().nextInt(10) == 0) {
							new HorizonRarePlus(name).add2(player);
							player.sendMessage("§a>>§b覚醒させました");
						}else{
							player.sendMessage("§a>>§b失敗しました");
						}
						HashMap<String, Boolean> r = new HashMap<String, Boolean>();
						r.put("a", false);
						inv.getContents().forEach((i, it) ->{
							if (it.hasCompoundTag() && it.getCustomName().equals("§e覚醒石") && it.getNamedTag().contains("Data")) {
								if (!(r.get("a"))) {
									r.put("a", true);
									if (it.getCount() == 1) {
										it = Item.get(0);
									}else{
										it.setCount(it.getCount() - 1);
									}
									inv.setItem(i, it);
								}
							}
						});
					}else if (item.getCustomName().equals(s3)) {
						if (new Random().nextInt(10) == 0) {
							new HorizonRarePlus(name).add3(player);
							player.sendMessage("§a>>§b覚醒させました");
						}else{
							player.sendMessage("§a>>§b失敗しました");
						}
						HashMap<String, Boolean> r = new HashMap<String, Boolean>();
						r.put("a", false);
						inv.getContents().forEach((i, it) ->{
							if (it.hasCompoundTag() && it.getCustomName().equals("§e覚醒石") && it.getNamedTag().contains("Data")) {
								if (!(r.get("a"))) {
									r.put("a", true);
									if (it.getCount() == 1) {
										it = Item.get(0);
									}else{
										it.setCount(it.getCount() - 1);
									}
									inv.setItem(i, it);
								}
							}
						});
					}else{
						player.sendMessage("§a>>§bそのアイテムは覚醒できません");
					}
				}else{
					player.sendMessage("§a>>§bそのアイテムは覚醒できません");
				}
			}else{
				player.sendMessage("§a>>§b覚醒石がないと覚醒できません");
			}
		}
		if (block.getId() == 247) {
			Item item1 = player.getInventory().getItemInHand();
			if (item1.getId() != 0 && item1.hasCompoundTag() && item1.getNamedTag().contains("R") && item1.getNamedTag().getCompound("R").getInt("R") == 500) {
				if (new Random().nextInt(3) == 0) {
					Item item2 = Item.get(377, 0, 1);
					item2.setNamedTag(new CompoundTag().putCompound("Data", new CompoundTag("Data").putInt("Data", 1)));
					item2.setCustomName("§e覚醒石");
					player.getInventory().setItemInHand(item2);
					player.sendMessage("§a>>§b交換に成功しました");
				}else{
					player.getInventory().setItemInHand(Item.get(0));
					player.sendMessage("§a>>§b交換に失敗しました");
				}
			}else{
				player.sendMessage("§a>>§bこのアイテムは交換できません");
			}
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String[] m = event.getMessage().split(" ");
		if (m[0].equals("/give")) {
			try {
				if (m[2] != null) {
					Item item = Item.fromString(m[2]);
					if (item.getId() == 388) {
						event.setCancelled();
						event.getPlayer().sendMessage("§a>>§bエメラルドをgiveすることができません");
					}
				}
			}
			catch(ArrayIndexOutOfBoundsException e){

			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
				@Override
				public void run() {
					Player player = (Player) entity;
					String f = "\n";
					Item item1 = player.getInventory().getArmorItem(0);
					String name1 = "§l§3A§bT§aH§dE§cN§6A§r" + f + f + "§fNo.004" + f + "§7満腹度回復・暗視・耐久無限" + f + "§7Rank:§eHR";
					if (item1.getId() != 0 && item1.hasCompoundTag() && item1.getNamedTag().contains("R")) {
						if (item1.getCustomName().equals(name1)) {
							item1.setDamage(0);
							player.getInventory().setArmorItem(0, item1);
						}
					}
					item1 = player.getInventory().getArmorItem(1);
					name1 = "§l§3E§bR§aE§dB§cO§6S§r" + f + f + "§fNo.005" + f + "§7体力増強・耐性・耐久無限" + f + "§7Rank:§eHR";
					if (item1.getId() != 0 && item1.hasCompoundTag() && item1.getNamedTag().contains("R")) {
						if (item1.getCustomName().equals(name1)) {
							item1.setDamage(0);
							player.getInventory().setArmorItem(1, item1);
						}
					}
					item1 = player.getInventory().getArmorItem(2);
					name1 = "§l§3H§bE§aR§dM§cE§6S§r" + f + f + "§fNo.006" + f + "§7再生・跳躍力・耐久無限" + f + "§7Rank:§eHR";
					if (item1.getId() != 0 && item1.hasCompoundTag() && item1.getNamedTag().contains("R")) {
						if (item1.getCustomName().equals(name1)) {
							item1.setDamage(0);
							player.getInventory().setArmorItem(2, item1);
						}
					}
					item1 = player.getInventory().getArmorItem(3);
					name1 = "§l§3P§bO§aS§dE§cI§6D§eO§fN§r" + f + f + "§fNo.007" + f + "§7移動速度・耐久無限" + f + "§7Rank:§eHR";
					if (item1.getId() != 0 && item1.hasCompoundTag() && item1.getNamedTag().contains("R")) {
						if (item1.getCustomName().equals(name1)) {
							item1.setDamage(0);
							player.getInventory().setArmorItem(3, item1);
						}
					}
					player.getInventory().sendArmorContents(player);
				}
			}, 1);
		}
	}

	@EventHandler
	public void onEntityArmorChange(EntityArmorChangeEvent event){
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			String f = "\n";
			String item1 = "§l§3A§bT§aH§dE§cN§6A§r" + f + f + "§fNo.004" + f + "§7満腹度回復・暗視・耐久無限" + f + "§7Rank:§eHR";
			String item2 = "§l§3E§bR§aE§dB§cO§6S§r" + f + f + "§fNo.005" + f + "§7体力増強・耐性・耐久無限" + f + "§7Rank:§eHR";
			String item3 = "§l§3H§bE§aR§dM§cE§6S§r" + f + f + "§fNo.006" + f + "§7再生・跳躍力・耐久無限" + f + "§7Rank:§eHR";
			String item4 = "§l§3P§bO§aS§dE§cI§6D§eO§fN§r" + f + f + "§fNo.007" + f + "§7移動速度・耐久無限" + f + "§7Rank:§eHR";
			String item5 = "§l§eAegir§r" + f + f + "§fNo.011" + f + "§7攻撃力" + f + "§7Rank:§bSR";
			String item6 = "§l§eMermaid§r" + f + f + "§fNo.012" + f + "§7移動速度" + f + "§7Rank:§bSR";
			Item item = event.getOldItem();
			if (item.getId() != 0 && item.hasCompoundTag() && item.getNamedTag().contains("R")) {
				String name = item.getCustomName();
				if (name.equals(item1)) {
					if (player.hasEffect(23)) {
						player.removeEffect(23);
					}
					if (player.hasEffect(16)) {
						player.removeEffect(16);
					}
				}else if (name.equals(item2)) {
					if (player.hasEffect(11)) {
						player.removeEffect(11);
					}
					if (player.hasEffect(21)) {
						player.removeEffect(21);
					}
				}else if (name.equals(item3)) {
					if (player.hasEffect(8)) {
						player.removeEffect(8);
					}
					if (player.hasEffect(10)) {
						player.removeEffect(10);
					}
				}else if (name.equals(item4)) {
					if (player.hasEffect(1)) {
						player.removeEffect(1);
					}
				}else if (name.equals(item5)) {
					if (player.hasEffect(5)) {
						player.removeEffect(5);
					}
				}else if (name.equals(item6)) {
					if (player.hasEffect(1)) {
						player.removeEffect(1);
					}
				}
			}
			item = event.getNewItem();
			if (item.getId() != 0 && item.hasCompoundTag() && item.getNamedTag().contains("R")) {
				Effect effect;
				String name = item.getCustomName();
				if (name.equals(item1)) {
					effect = Effect.getEffect(23).setDuration(20 * 10000).setAmplifier(1).setVisible(false);
					player.addEffect(effect);
					effect = Effect.getEffect(16).setDuration(20 * 10000).setAmplifier(1).setVisible(false);
					player.addEffect(effect);
				}else if (name.equals(item2)) {
					effect = Effect.getEffect(11).setDuration(20 * 10000).setAmplifier(1).setVisible(false);
					player.addEffect(effect);
					effect = Effect.getEffect(21).setDuration(20 * 10000).setAmplifier(1).setVisible(false);
					player.addEffect(effect);
				}else if (name.equals(item3)) {
					effect = Effect.getEffect(8).setDuration(20 * 10000).setAmplifier(5).setVisible(false);
					player.addEffect(effect);
					effect = Effect.getEffect(10).setDuration(20 * 10000).setAmplifier(1).setVisible(false);
					player.addEffect(effect);
				}else if (name.equals(item4)) {
					effect = Effect.getEffect(1).setDuration(20 * 10000).setAmplifier(10).setVisible(false);
					player.addEffect(effect);
				}else if (name.equals(item5)) {
					effect = Effect.getEffect(5).setDuration(20 * 10000).setAmplifier(1).setVisible(false);
					player.addEffect(effect);
				}else if (name.equals(item6)) {
					effect = Effect.getEffect(1).setDuration(20 * 10000).setAmplifier(5).setVisible(false);
					player.addEffect(effect);
				}
			}
		}
	}

	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		switch(command.getName()){
			case "change":
				Item item = ((EntityHumanType) sender).getInventory().getItemInHand();
				if (item.hasCompoundTag() && item.getNamedTag().contains("display")) {
					if (item.getNamedTag().getCompound("display").getString("Pl").equals(sender.getName().toLowerCase())) {
						int i = item.getNamedTag().getCompound("R").getInt("R");
						if (i == 5 || i == 15) {
							((EntityHumanType) sender).getInventory().setItemInHand(Item.get(0));
							((EntityHumanType) sender).getInventory().addItem(Item.get(388, 0, item.getNamedTag().getCompound("R").getInt("R")));
							sender.sendMessage("§a>>§b交換しました");
						}else{
							sender.sendMessage("§a>>§bHRよりレア度が高い景品は交換できません");
						}
					}else{
						sender.sendMessage("§a>>§b他人のガチャの景品は交換することができません");
					}
				}else{
					sender.sendMessage("§a>>§bそのアイテムは交換することができません");
				}
			break;

			case "ticket":
				int count = this.data.get(sender.getName().toLowerCase());
				count = 500 - count;
				sender.sendMessage("§a>>§bあと" + String.valueOf(count) + "個で次のガチャ券が手に入ります");
			break;

			case "heal":
				Player player = (Player) sender;
				String f = "\n";
				Item item1 = player.getInventory().getArmorItem(0);
				String name1 = "§l§3A§bT§aH§dE§cN§6A§r" + f + f + "§fNo.004" + f + "§7満腹度回復・暗視・耐久無限" + f + "§7Rank:§eHR";
				if (item1.getId() != 0 && item1.hasCompoundTag() && item1.getNamedTag().contains("R")) {
					if (item1.getCustomName().equals(name1)) {
						item1.setDamage(0);
						player.getInventory().setArmorItem(0, item1);
					}
				}
				item1 = player.getInventory().getArmorItem(1);
				name1 = "§l§3E§bR§aE§dB§cO§6S§r" + f + f + "§fNo.005" + f + "§7体力増強・耐性・耐久無限" + f + "§7Rank:§eHR";
				if (item1.getId() != 0 && item1.hasCompoundTag() && item1.getNamedTag().contains("R")) {
					if (item1.getCustomName().equals(name1)) {
						item1.setDamage(0);
						player.getInventory().setArmorItem(1, item1);
					}
				}
				item1 = player.getInventory().getArmorItem(2);
				name1 = "§l§3H§bE§aR§dM§cE§6S§r" + f + f + "§fNo.006" + f + "§7再生・跳躍力・耐久無限" + f + "§7Rank:§eHR";
				if (item1.getId() != 0 && item1.hasCompoundTag() && item1.getNamedTag().contains("R")) {
					if (item1.getCustomName().equals(name1)) {
						item1.setDamage(0);
						player.getInventory().setArmorItem(2, item1);
					}
				}
				item1 = player.getInventory().getArmorItem(3);
				name1 = "§l§3P§bO§aS§dE§cI§6D§eO§fN§r" + f + f + "§fNo.007" + f + "§7移動速度・耐久無限" + f + "§7Rank:§eHR";
				if (item1.getId() != 0 && item1.hasCompoundTag() && item1.getNamedTag().contains("R")) {
					if (item1.getCustomName().equals(name1)) {
						item1.setDamage(0);
						player.getInventory().setArmorItem(3, item1);
					}
				}
				player.getInventory().sendArmorContents(player);
				sender.sendMessage("§a>>§b修復できる装備を修復しました");
			break;

			case "prize":
				try {
					if (args[0] != null) {
						String name = sender.getName().toLowerCase();
						switch (args[0]) {
							case "on":
								this.prize.put(name, true);
								sender.sendMessage("§a>>§b出たあたりと大当たりの景品を自動でエメラルドに交換するようにしました");
							break;

							case "off":
								if (this.prize.containsKey(name)) {
									this.prize.remove(name);
								}
								sender.sendMessage("§a>>§b出たあたりと大当たりの景品を自動でエメラルドに交換しなくしました");
							break;

							default:
								sender.sendMessage("§a>>§b/prize onまたはoff");
							break;
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/prize onまたはoff");
				}
			break;

		}
		return false;
	}

	public boolean hasItem(Inventory inventory) {
		HashMap<String, Boolean> r = new HashMap<String, Boolean>();
		r.put("a", false);
		inventory.getContents().forEach((i, item) ->{
			if (item.hasCompoundTag() && item.getCustomName().equals("§e覚醒石") && item.getNamedTag().contains("Data")) {
				r.put("a", true);
			}
		});
		return r.get("a");
	}

	public void addCount(Player player) {
		String name = player.getName().toLowerCase();
		addCount(name);
	}

	public void addCount(String name) {
		name = name.toLowerCase();
		this.data.put(name, this.data.get(name) + 1);
		if (this.data.get(name) >= 500) {
			this.data.put(name, this.data.get(name) - 500);
			this.item.addItem(name, 388, 0, 1);
		}
	}

	public boolean Gatya(Player player) {
		String name = player.getName().toLowerCase();
		int r = new Random().nextInt(1000);
		if (r == 0) {
			new HorizonRare(name).add(player);
			player.setSubtitle("§bおめでとうございます");
			player.sendTitle("§aHorizonRareを当てました");
			getServer().broadcastMessage("§a>>§b" + player.getName() + "がHorizonRareを当てました");
			return false;
		}else if(r >= 10 && r <= 19) {
			if (this.prize.containsKey(name)) {
				Item item = Item.get(388, 0, 15);
				Vector3 pos = new Vector3(player.x, player.y, player.z);
				player.getLevel().dropItem(pos, item);
			}else{
				new BigLuck(name).add(player);
			}
			player.sendMessage("§a>>§b大当たり");
			return false;
		}else if(r >= 50 && r <= 99) {
			if (this.prize.containsKey(name)) {
				Item item = Item.get(388, 0, 5);
				Vector3 pos = new Vector3(player.x, player.y, player.z);
				player.getLevel().dropItem(pos, item);
			}else{
				new Luck(name).add(player);
			}
			player.sendMessage("§a>>§b当たり");
			return false;
		}else{
			return true;
		}
	}

	public Item setNameTag(Item item, String name) {
		CompoundTag tag;
		if (item.hasCompoundTag()) {
			tag = item.getNamedTag();
		} else {
			tag = new CompoundTag();
		}
		if (tag.contains("display") && tag.get("display") instanceof CompoundTag) {
			tag.getCompound("display").putString("Name", name);
		} else {
			tag.putCompound("display", new CompoundTag("display").putString("Name", name));
		}
		item.setNamedTag(tag);
		return item;
	}

	public Item addEnchant(Item item, int id, int level) {
		CompoundTag tag;
		if (item.hasCompoundTag()) {
			tag = item.getNamedTag();
		} else {
			tag = new CompoundTag();
		}
		ListTag<CompoundTag> ench;
		if (!tag.contains("ench")) {
			ench = new ListTag<>("ench");
			tag.putList(ench);
		} else {
			ench = tag.getList("ench", CompoundTag.class);
        }
		boolean r = true;
		for (int i = 0; i < ench.size(); i++) {
			if (ench.get(i).getInt("id") == id) {
				ench.add(i, new CompoundTag()
					.putShort("id", id)
					.putShort("lvl", level)
				);
				r = false;
				break;
			}
		}
		if (r) {
			ench.add(new CompoundTag()
				.putShort("id", id)
				.putShort("lvl", level)
			);
		}
		item.setNamedTag(tag);
		return item;
	}

	public Item addUnbreakable(Item item) {
		int count = 1;
		CompoundTag tag;
		if (item.hasCompoundTag()) {
			tag = item.getNamedTag();
		} else {
			tag = new CompoundTag();
		}
		tag.putCompound("Unbreakable", new CompoundTag("Unbreakable").putInt("Unbreakable", count));
		item.setNamedTag(tag);
		return item;
	}

	public Item addRank(Item item, int count) {
		CompoundTag tag;
		if (item.hasCompoundTag()) {
			tag = item.getNamedTag();
		} else {
			tag = new CompoundTag();
		}
		if (tag.contains("R") && tag.get("R") instanceof CompoundTag) {
			tag.getCompound("R").putInt("R", count);
		} else {
			tag.putCompound("R", new CompoundTag("R").putInt("R", count));
		}
		item.setNamedTag(tag);
		return item;
	}

	public Item addName(Item item, String name) {
		CompoundTag tag;
		if (item.hasCompoundTag()) {
			tag = item.getNamedTag();
		} else {
			tag = new CompoundTag();
		}
		if (tag.contains("display") && tag.get("display") instanceof CompoundTag) {
			tag.getCompound("display").putString("Pl", name);
		} else {
			tag.putCompound("display", new CompoundTag("display").putString("Pl", name));
		}
		item.setNamedTag(tag);
		return item;
	}
}
