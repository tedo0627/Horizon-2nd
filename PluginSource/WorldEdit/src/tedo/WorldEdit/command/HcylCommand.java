package tedo.WorldEdit.command;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import tedo.WorldEdit.WorldEdit;

public class HcylCommand{

	public static void execution(Player player, String[] args, WorldEdit main) {
		String name = player.getName();
		if (args.length < 5) {
			player.sendMessage("§a>>§b/ hcyl [ID] [半径] [高さ]");
			return;
		}
		if (!main.isVector1(name)) {
			player.sendMessage("§a>>§b一つ目の位置を決めてください");
			return;
		}
		Level level = player.getLevel();
		Vector3 pos = main.getVector1(name);
		int x = (int) pos.x;
		int y = (int) pos.y;
		int z = (int) pos.z;
		Item item = Item.fromString(args[2]);
		Block block = Block.get(item.getId(), item.getDamage());
		int distance = Integer.parseInt(args[3]);
		int higt = Integer.parseInt(args[4]);
		int r = 0;
		HashMap<Position, Block> undo = new HashMap<Position, Block>();
		for (int hy = y; hy < higt + y; hy++) {
			for (int i = 0; i < 360; i++) {
				double radian = i * Math.PI / 180;
				double hx = Math.sin(radian) * distance;
				double hz = Math.cos(radian) * distance;
				Position p = new Position(hx + x, hy, hz + z, level);
				Block b = level.getBlock(p);
				if (!(block.getId() == b.getId() && block.getDamage() == b.getDamage())) {
					undo.put(p, b);
					level.setBlock(p, block);
					r++;
				}
			}
		}
		main.addUndo(undo);
		player.sendMessage("§a>>§b" + r + "ブロックの編集が終了しました");
	}
}
