package tedo.SeichiSystemPlugin.skill;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.math.Vector3;
import tedo.SeichiSystemPlugin.SeichiSystemPlugin;

public class skill4{

	public skill4(Player player, Block block, SeichiSystemPlugin main) {
		int x = (int) block.x;
		int y = 6;
		int z = (int) block.z;
		int x1;
		int z1;
		Vector3 pos;
		boolean r = true;
		int i = 0;
		switch (main.getDirection(player)) {
			case 0:
				for (x1 = x; r; x1++) {
					pos = new Vector3(x1, y, z);
					r = main.placeHalf(player, pos);
					i++;
					if (i >= 1000) {
						r = false;
					}
				}
			break;

			case 1:
				for (z1 = z; r; z1++) {
					pos = new Vector3(x, y, z1);
					r = main.placeHalf(player, pos);
					i++;
					if (i >= 1000) {
						r = false;
					}
				}
			break;

			case 2:
				for (x1 = x; r; x1--) {
					pos = new Vector3(x1, y, z);
					r = main.placeHalf(player, pos);
					i++;
					if (i >= 1000) {
						r = false;
					}
				}
			break;

			case 3:
				for (z1 = z; r; z1--) {
					pos = new Vector3(x, y, z1);
					r = main.placeHalf(player, pos);
					i++;
					if (i >= 1000) {
						r = false;
					}
				}
			break;
		}
	}
}
