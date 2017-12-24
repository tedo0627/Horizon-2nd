package tedo.WorldEdit.command;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import tedo.WorldEdit.WorldEdit;

public class ReplaceCommand {

	public static void execution(Player player, String[] args, WorldEdit main) {
		if (args.length < 4) {
			player.sendMessage("§a>>§b// replace [置き換える前のブロックID] [置き換える後のブロックID]");
			return;
		}
		String name = player.getName();
		if (!main.isVector1(name)) {
			player.sendMessage("§a>>§b一つ目の位置を決めてください");
			return;
		}
		if (!main.isVector2(name)) {
			player.sendMessage("§a>>§b二つ目の位置を決めてください");
			return;
		}
		Level level = player.getLevel();
		Vector3 pos1 = main.getVector1(name);
		Vector3 pos2 = main.getVector2(name);
		Item item1= Item.fromString(args[2]);
		Item item2= Item.fromString(args[3]);
		Block block = Block.get(item2.getId(), item2.getDamage());
		int x1 = (int) pos1.x;
		int y1 = (int) pos1.y;
		int z1 = (int) pos1.z;
		int x2 = (int) pos2.x;
		int y2 = (int) pos2.y;
		int z2 = (int) pos2.z;
		if (x1 < x2) {
			x1 = x1 + x2;
			x2 = x1 - x2;
			x1 = x1 - x2;
		}
		if (y1 < y2) {
			y1 = y1 + y2;
			y2 = y1 - y2;
			y1 = y1 - y2;
		}
		if (z1 < z2) {
			z1 = z1 + z2;
			z2 = z1 - z2;
			z1 = z1 - z2;
		}
		int i = 0;
		int x, y, z;
		HashMap<Position, Block> undo = new HashMap<Position, Block>();
		for (y = y1; y >= y2; y--) {
			for (x = x1; x >= x2; x--) {
				for (z = z1; z >= z2; z--) {
					Position pos = new Position(x, y, z, level);
					Block before = level.getBlock(pos);
					if (before.getId() == item1.getId() && before.getDamage() == item1.getDamage()) {
						undo.put(pos, before);
						level.setBlock(pos, block);
						i++;
					}
				}
			}
		}
		main.addUndo(undo);
		player.sendMessage("§a>>§b" + i + "ブロックの編集が終了しました");
	}
}
