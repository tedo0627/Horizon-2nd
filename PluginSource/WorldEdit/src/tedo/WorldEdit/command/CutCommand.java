package tedo.WorldEdit.command;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import tedo.WorldEdit.WorldEdit;

public class CutCommand {

	public static void execution(Player player, WorldEdit main) {
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
		Block block = Block.get(0);
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
					undo.put(pos, level.getBlock(pos));
					level.setBlock(pos, block);
					i++;
				}
			}
		}
		main.addUndo(undo);
		player.sendMessage("§a>>§b" + i + "ブロックの編集が終了しました");
	}
}
