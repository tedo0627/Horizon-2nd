package tedo.GatyaSystem.prize;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import tedo.GatyaSystem.GatyaSystem;

public class HorizonRarePlus extends PluginBase{

	private HashMap<Integer, Item> items = new HashMap<Integer, Item>();

	public HorizonRarePlus(String name) {
		String f = "\n";
		GatyaSystem main = new GatyaSystem();

		Item item1 = Item.get(278, 0, 1);
		item1 = main.setNameTag(item1, "§l§3K§bR§aO§dN§cO§6S§r" + f + f + "§a覚醒§fNo.001" + f + "§7範囲増加・耐久無限" + f + "§7Rank:§eHR§b+");
		item1 = main.addName(item1, name);
		item1 = main.addRank(item1, 1000);
		item1 = main.addUnbreakable(item1);
		item1 = main.addEnchant(item1, 15, 10);
		item1 = main.addEnchant(item1, 16, 1);

		Item item2 = Item.get(277, 0, 1);
		item2 = main.setNameTag(item2, "§l§3B§bR§aI§dO§cN§6A§eC§r" + f + f + "§a覚醒§fNo.002" + f + "§7範囲増加・耐久無限" + f + "§7Rank:§eHR§b+");
		item2 = main.addName(item2, name);
		item2 = main.addRank(item2, 1000);
		item2 = main.addUnbreakable(item2);
		item2 = main.addEnchant(item2, 15, 10);
		item2 = main.addEnchant(item2, 16, 1);

		Item item3 = Item.get(279, 0, 1);
		item3 = main.setNameTag(item3, "§l§3R§bI§aS§dA§cN§6A§eU§fT§r" + f + f + "§a覚醒§fNo.003" + f + "§7範囲増加・耐久無限" + f + "§7Rank:§eHR§b+");
		item3 = main.addName(item3, name);
		item3 = main.addRank(item3, 1000);
		item3 = main.addUnbreakable(item3);
		item3 = main.addEnchant(item3, 15, 10);
		item3 = main.addEnchant(item3, 16, 1);

		items.put(1, item1);
		items.put(2, item2);
		items.put(3, item3);
	}

	public void add1(Player player) {
		player.getInventory().setItemInHand(Item.get(0));
		player.getInventory().setItemInHand(items.get(1));
	}

	public void add2(Player player) {
		player.getInventory().setItemInHand(Item.get(0));
		player.getInventory().setItemInHand(items.get(2));
	}

	public void add3(Player player) {
		player.getInventory().setItemInHand(Item.get(0));
		player.getInventory().setItemInHand(items.get(3));
	}
}
