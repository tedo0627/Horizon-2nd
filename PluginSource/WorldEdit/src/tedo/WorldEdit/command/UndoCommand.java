package tedo.WorldEdit.command;

import java.util.ArrayList;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import tedo.WorldEdit.WorldEdit;

public class UndoCommand {

	public static void execution(Player player, WorldEdit main) {
		HashMap<Position, Block> undo = main.getUndo();
		if (undo == null) {
			player.sendMessage("§a>>§b戻すブロックのデータがありません");
			return;
		}
		ArrayList<Integer> i = new ArrayList<Integer>();
		undo.forEach((pos, block) -> {
			Level level = pos.getLevel();
			level.setBlock(pos, block);
			i.add(0);
		});
		player.sendMessage("§a>>§b" + i.size() + "ブロックを戻しました");
	}
}
