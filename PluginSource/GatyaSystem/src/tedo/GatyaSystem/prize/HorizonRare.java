package tedo.GatyaSystem.prize;

import java.util.HashMap;
import java.util.Random;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import tedo.GatyaSystem.GatyaSystem;

public class HorizonRare extends PluginBase{

	private HashMap<Integer, Item> items = new HashMap<Integer, Item>();

	public HorizonRare(String name) {
		String f = "\n";
		GatyaSystem main = new GatyaSystem();

		Item item1 = Item.get(278, 0, 1);
		item1 = main.setNameTag(item1, "§l§3K§bR§aO§dN§cO§6S§r" + f + f + "§fNo.001" + f + "§7耐久無限" + f + "§7Rank:§eHR");
		item1 = main.addName(item1, name);
		item1 = main.addRank(item1, 500);
		item1 = main.addUnbreakable(item1);
		item1 = main.addEnchant(item1, 15, 10);
		item1 = main.addEnchant(item1, 16, 1);

		Item item2 = Item.get(277, 0, 1);
		item2 = main.setNameTag(item2, "§l§3B§bR§aI§dO§cN§6A§eC§r" + f + f + "§fNo.002" + f + "§7耐久無限" + f + "§7Rank:§eHR");
		item2 = main.addName(item2, name);
		item2 = main.addRank(item2, 500);
		item2 = main.addUnbreakable(item2);
		item2 = main.addEnchant(item2, 15, 10);
		item2 = main.addEnchant(item2, 16, 1);

		Item item3 = Item.get(279, 0, 1);
		item3 = main.setNameTag(item3, "§l§3R§bI§aS§dA§cN§6A§eU§fT§r" + f + f + "§fNo.003" + f + "§7耐久無限" + f + "§7Rank:§eHR");
		item3 = main.addName(item3, name);
		item3 = main.addRank(item3, 500);
		item3 = main.addUnbreakable(item3);
		item3 = main.addEnchant(item3, 15, 10);
		item3 = main.addEnchant(item3, 16, 1);

		Item item4 = Item.get(310, 0, 1);
		item4 = main.setNameTag(item4, "§l§3A§bT§aH§dE§cN§6A§r" + f + f + "§fNo.004" + f + "§7満腹度回復・暗視・耐久無限" + f + "§7Rank:§eHR");
		item4 = main.addName(item4, name);
		item4 = main.addRank(item4, 500);
		item4 = main.addUnbreakable(item4);
		item4 = main.addEnchant(item4, 0, 4);
		item4 = main.addEnchant(item4, 6, 3);

		Item item5 = Item.get(444, 0, 1);
		item5 = main.setNameTag(item5, "§l§3E§bR§aE§dB§cO§6S§r" + f + f + "§fNo.005" + f + "§7体力増強・耐性・耐久無限" + f + "§7Rank:§eHR");
		item5 = main.addName(item5, name);
		item5 = main.addRank(item5, 500);
		item5 = main.addUnbreakable(item5);
		item5 = main.addEnchant(item5, 0, 4);
		item5 = main.addEnchant(item5, 1, 4);

		Item item6 = Item.get(312, 0, 1);
		item6 = main.setNameTag(item6, "§l§3H§bE§aR§dM§cE§6S§r" + f + f + "§fNo.006" + f + "§7再生・跳躍力・耐久無限" + f + "§7Rank:§eHR");
		item6 = main.addName(item6, name);
		item6 = main.addRank(item6, 500);
		item6 = main.addUnbreakable(item6);
		item6 = main.addEnchant(item6, 0, 4);
		item6 = main.addEnchant(item6, 2, 4);

		Item item7 = Item.get(313, 0, 1);
		item7 = main.setNameTag(item7, "§l§3P§bO§aS§dE§cI§6D§eO§fN§r" + f + f + "§fNo.007" + f + "§7移動速度・耐久無限" + f + "§7Rank:§eHR");
		item7 = main.addName(item7, name);
		item7 = main.addRank(item7, 500);
		item7 = main.addUnbreakable(item7);
		item7 = main.addEnchant(item7, 2, 4);
		item7 = main.addEnchant(item7, 7, 3);;

		items.put(1, item1);
		items.put(2, item2);
		items.put(3, item3);
		items.put(4, item4);
		items.put(5, item5);
		items.put(6, item6);
		items.put(7, item7);
	}

	public void add(Player player) {
		int r = new Random().nextInt(7) + 1;
		Item item = items.get(r);
		if (player.getInventory().canAddItem(item)) {
			player.getInventory().addItem(item);
		}else{
			Vector3 pos = new Vector3(player.x, player.y, player.z);
			player.getLevel().dropItem(pos, item);
		}
	}
}
