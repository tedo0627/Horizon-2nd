package tedo.WorldEdit.command;

import java.util.ArrayList;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import tedo.WorldEdit.WorldEdit;

public class PasteCommand {

	public static void execution(Player player, WorldEdit main) {
		String name = player.getName();
		if (!main.isCopy(name)) {
			player.sendMessage("§a>>§bコピーしたものがありません");
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
		ArrayList<Integer> i = new ArrayList<Integer>();
		HashMap<Position, Block> undo = new HashMap<Position, Block>();
		main.getCopy(name).forEach((p, block) -> {
			int xp = (int) p.x + x;
			int yp = (int) p.y + y;
			int zp = (int) p.z + z;
			Position po = new Position(xp, yp, zp, level);
			undo.put(po, level.getBlock(po));
			level.setBlock(po, block);
			i.add(0);
		});
		main.addUndo(undo);
		player.sendMessage("§a>>§b" + i.size() + "ブロック編集が終了しました");
	}
}
