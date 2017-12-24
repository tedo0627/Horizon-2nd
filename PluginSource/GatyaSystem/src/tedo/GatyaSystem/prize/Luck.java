package tedo.GatyaSystem.prize;

import java.util.HashMap;
import java.util.Random;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import tedo.GatyaSystem.GatyaSystem;

public class Luck extends PluginBase{

	private HashMap<Integer, Item> items = new HashMap<Integer, Item>();

	public Luck(String name) {
		String f = "\n";
		GatyaSystem main = new GatyaSystem();

		Item item1 = Item.get(278, 0, 1);
		item1 = main.setNameTag(item1, "§l§bKuronoa PickAxe§r" + f + f + "§fNo.013" + f + "§7Rank:§aR");
		item1 = main.addName(item1, name);
		item1 = main.addRank(item1, 5);
		item1 = main.addEnchant(item1, 15, 5);
		item1 = main.addEnchant(item1, 17, 3);

		Item item2 = Item.get(277, 0, 1);
		item2 = main.setNameTag(item2, "§l§bKuronoa Shovel§r" + f + f + "§fNo.014" + f + "§7Rank:§aR");
		item2 = main.addName(item2, name);
		item2 = main.addRank(item2, 5);
		item2 = main.addEnchant(item2, 15, 5);
		item2 = main.addEnchant(item2, 17, 3);

		Item item3 = Item.get(279, 0, 1);
		item3 = main.setNameTag(item3, "§l§bKuronoa Axe§r" + f + f + "§fNo.015" + f + "§7Rank:§aR");
		item3 = main.addName(item3, name);
		item3 = main.addRank(item3, 5);
		item3 = main.addEnchant(item3, 15, 5);
		item3 = main.addEnchant(item3, 17, 3);

		Item item4 = Item.get(278, 1552, 1);
		item4 = main.setNameTag(item4, "§l§bFortuna PickAxe§r" + f + f + "§fNo.016" + f + "§7Rank:§aR");
		item4 = main.addName(item4, name);
		item4 = main.addRank(item4, 5);
		item4 = main.addEnchant(item4, 18, 10);
		item4 = main.addEnchant(item4, 17, 3);

		Item item5 = Item.get(278, 0, 1);
		item5 = main.setNameTag(item5, "§l§bGanymede PickAxe§r" + f + f + "§fNo.017" + f + "§7Rank:§aR");
		item5 = main.addName(item5, name);
		item5 = main.addRank(item5, 5);
		item5 = main.addEnchant(item5, 15, 3);
		item5 = main.addEnchant(item5, 17, 3);
		item5 = main.addEnchant(item5, 18, 2);

		Item item6 = Item.get(444, 0, 1);
		item6 = main.setNameTag(item6, "§l§bFaery wing§r" + f + f + "§fNo.018" + f + "§7Rank:§aR");
		item6 = main.addName(item6, name);
		item6 = main.addRank(item6, 5);
		item6 = main.addEnchant(item6, 0, 3);
		item6 = main.addEnchant(item6, 5, 3);
		item6 = main.addEnchant(item6, 17, 3);

		items.put(1, item1);
		items.put(2, item2);
		items.put(3, item3);
		items.put(4, item4);
		items.put(5, item5);
		items.put(6, item6);
	}

	public void add(Player player) {
		int r = new Random().nextInt(6) + 1;
		Item item = items.get(r);
		if (player.getInventory().canAddItem(item)) {
			player.getInventory().addItem(item);
		}else{
			Vector3 pos = new Vector3(player.x, player.y, player.z);
			player.getLevel().dropItem(pos, item);
		}
	}
}
