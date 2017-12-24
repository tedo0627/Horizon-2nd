package tedo.SeichiSystemPlugin.skill;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import tedo.SeichiSystemPlugin.SeichiSystemPlugin;

public class skill1{

	public skill1(Player player, Block block, SeichiSystemPlugin main, Boolean r) {
		int x = (int) block.x;
		int y = (int) block.y;
		int z = (int) block.z;
		Level level = block.level;
		Vector3 pos;
		if (player.isSneaking() && y < player.y) {
			if (r) {
				pos = new Vector3(x, y , z);
				main.skillBlockBreak(player, pos, level);
				pos = new Vector3(x, y - 1, z);
				main.skillBlockBreak(player, pos, level);
				pos = new Vector3(x, y - 2, z);
				main.skillBlockBreak(player, pos, level);
			}else{
				pos = new Vector3(x, y, z);
				main.skillBlockBreak(player, pos, level);
				pos = new Vector3(x, y - 1, z);
				main.skillBlockBreak(player, pos, level);
			}
		}else{
			if (y <= player.y) {
				y = (int) player.y;
			}
			if (r) {
				pos = new Vector3(x, y + 2, z);
				main.skillBlockBreak(player, pos, level);
			}
			pos = new Vector3(x, y + 1, z);
			main.skillBlockBreak(player, pos, level);
			pos = new Vector3(x, y, z);
			main.skillBlockBreak(player, pos, level);
		}
	}
}
