package tedo.GatyaSystem.prize;

import java.util.HashMap;
import java.util.Random;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import tedo.GatyaSystem.GatyaSystem;

public class BigLuck extends PluginBase{

	private HashMap<Integer, Item> items = new HashMap<Integer, Item>();

	public BigLuck(String name) {
		String f = "\n";
		GatyaSystem main = new GatyaSystem();

		Item item1 = Item.get(278, 0, 1);
		item1 = main.setNameTag(item1, "§l§eMjolner§r" + f + f + "§fNo.008" + f + "§7Rank:§bSR");
		item1 = main.addName(item1, name);
		item1 = main.addRank(item1, 15);
		item1 = main.addEnchant(item1, 15, 5);
		item1 = main.addEnchant(item1, 17, 7);

		Item item2 = Item.get(277, 0, 1);
		item2 = main.setNameTag(item2, "§l§eGungnir§r" + f + f + "§fNo.009" + f + "§7Rank:§bSR");
		item2 = main.addName(item2, name);
		item2 = main.addRank(item2, 15);
		item2 = main.addEnchant(item2, 15, 5);
		item2 = main.addEnchant(item2, 17, 7);

		Item item3 = Item.get(261, 0, 1);
		item3 = main.setNameTag(item3, "§l§eCandradhnus§r" + f + f + "§fNo.010" + f + "§7Rank:§bSR");
		item3 = main.addName(item3, name);
		item3 = main.addRank(item3, 15);
		item3 = main.addEnchant(item3, 17, 5);
		item3 = main.addEnchant(item3, 21, 1);
		item3 = main.addEnchant(item3, 22, 1);

		Item item4 = Item.get(302, 0, 1);
		item4 = main.setNameTag(item4, "§l§eAegir§r" + f + f + "§fNo.011" + f + "§7攻撃力" + f + "§7Rank:§bSR");
		item4 = main.addName(item4, name);
		item4 = main.addRank(item4, 15);
		item4 = main.addEnchant(item4, 4, 4);
		item4 = main.addEnchant(item4, 17, 6);

		Item item5 = Item.get(305, 0, 1);
		item5 = main.setNameTag(item5, "§l§eMermaid§r" + f + f + "§fNo.012" + f + "§7移動速度" + f + "§7Rank:§bSR");
		item5 = main.addName(item5, name);
		item5 = main.addRank(item5, 15);
		item5 = main.addEnchant(item5, 0, 3);
		item5 = main.addEnchant(item5, 7, 4);
		item5 = main.addEnchant(item5, 17, 6);

		items.put(1, item1);
		items.put(2, item2);
		items.put(3, item3);
		items.put(4, item4);
		items.put(5, item5);
	}

	public void add(Player player) {
		int r = new Random().nextInt(5) + 1;
		Item item = items.get(r);
		if (player.getInventory().canAddItem(item)) {
			player.getInventory().addItem(item);
		}else{
			Vector3 pos = new Vector3(player.x, player.y, player.z);
			player.getLevel().dropItem(pos, item);
		}
	}
}
