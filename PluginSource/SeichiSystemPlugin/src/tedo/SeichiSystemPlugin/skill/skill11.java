package tedo.SeichiSystemPlugin.skill;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import tedo.SeichiSystemPlugin.SeichiSystemPlugin;

public class skill11 {

	public skill11(Player player, SeichiSystemPlugin main, Boolean r) {
		Level level = player.getLevel();
		int x = (int) player.x;
		int y = (int) player.y;
		int z = (int) player.z;
		if (player.isSneaking() && player.pitch == 90) {
			if (r) {
				y-=14;
			}else{
				y-=31;
			}
		}
		if(r) {
			for (int y1 = y + 13; y1 >= y; y1--) {
				for (int x1 = x + 6; x1 >= x - 6; x1--) {
					for (int z1 = z + 6; z1 >= z - 6; z1--) {
						Vector3 pos = new Vector3(x1, y1, z1);
						main.skillBlockBreak(player, pos, level);
					}
				}
			}
		}else{
			for (int y1 = y + 30; y1 >= y; y1--) {
				for (int x1 = x + 15; x1 >= x - 15; x1--) {
					for (int z1 = z + 15; z1 >= z - 15; z1--) {
						Vector3 pos = new Vector3(x1, y1, z1);
						main.skillBlockBreak(player, pos, level);
					}
				}
			}
		}
	}
}

