package tedo.SeichiSystemPlugin.skill;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import tedo.SeichiSystemPlugin.SeichiSystemPlugin;

public class skill9{

	public skill9(Player player, Block block, SeichiSystemPlugin main, Boolean r) {
		int x = (int) block.x;
		int y = (int) block.y;
		int z = (int) block.z;
		Level level = block.level;
		int x1;
		int y1;
		int z1;
		Vector3 pos;
		int i = 0;
		if (player.isSneaking() && y < player.y) {
			if (r) {
				for (y1 = y; y1 != y - 12; y1--) {
					for (x1 = x + 5; x1 != x - 6; x1--) {
						for (z1 = z + 5; z1 != z - 6; z1--) {
							pos = new Vector3(x1, y1, z1);
							if (main.skillBlockBreak(player, pos, level)) {
								i++;
							}
						}
					}
				}
				main.cooltime(player, i / 50);
			}else{
				for (y1 = y; y1 != y - 11; y1--) {
					for (x1 = x + 5; x1 != x - 6; x1--) {
						for (z1 = z + 5; z1 != z - 6; z1--) {
							pos = new Vector3(x1, y1, z1);
							if (main.skillBlockBreak(player, pos, level)) {
								i++;
							}
						}
					}
				}
				main.cooltime(player, i / 50);
			}
		}else{
			if (y <= player.y) {
				y = (int) (player.y + 1);
			}
			if (r) {
				switch (main.getDirection(player)) {
					case 0:
						for (z1 = z + 5; z1 != z - 6; z1--) {
							for (x1 = x; x1 != x + 11; x1++) {
								pos = new Vector3(x1, y + 10, z1);
								main.skillBlockBreak(player, pos, level);
							}
						}
					break;

					case 1:
						for (x1 = x + 5; x1 != x - 6; x1--) {
							for (z1 = z; z1 != z + 11; z1++) {
								pos = new Vector3(x1, y + 10, z1);
								main.skillBlockBreak(player, pos, level);
							}
						}
					break;

					case 2:
						for (z1 = z + 5; z1 != z - 6; z1--) {
							for (x1 = x; x1 != x - 11; x1--) {
								pos = new Vector3(x1, y + 10, z1);
								main.skillBlockBreak(player, pos, level);
							}
						}
					break;

					case 3:
						for (x1 = x + 5; x1 != x - 6; x1--) {
							for (z1 = z; z1 != z - 11; z1--) {
								pos = new Vector3(x1, y + 10, z1);
								main.skillBlockBreak(player, pos, level);
							}
						}
					break;
				}
			}
			switch (main.getDirection(player)) {
				case 0:
					for (y1 = y + 9; y1 != y -2; y1--) {
						for (z1 = z + 5; z1 != z - 6; z1--) {
							for (x1 = x; x1 != x + 11; x1++) {
								pos = new Vector3(x1, y1, z1);
								if (main.skillBlockBreak(player, pos, level)) {
									i++;
								}
							}
						}
					}
					main.cooltime(player, i / 50);
				break;

				case 1:
					for (y1 = y + 9; y1 != y -2; y1--) {
						for (x1 = x + 5; x1 != x - 6; x1--) {
							for (z1 = z; z1 != z + 11; z1++) {
								pos = new Vector3(x1, y1, z1);
								if (main.skillBlockBreak(player, pos, level)) {
									i++;
								}
							}
						}
					}
					main.cooltime(player, i / 50);
				break;

				case 2:
					for (y1 = y + 9; y1 != y -2; y1--) {
						for (z1 = z + 5; z1 != z - 6; z1--) {
							for (x1 = x; x1 != x - 11; x1--) {
								pos = new Vector3(x1, y1, z1);
								if (main.skillBlockBreak(player, pos, level)) {
									i++;
								}
							}
						}
					}
					main.cooltime(player, i / 50);
				break;

				case 3:
					for (y1 = y + 9; y1 != y -2; y1--) {
						for (x1 = x + 5; x1 != x - 6; x1--) {
							for (z1 = z; z1 != z - 11; z1--) {
								pos = new Vector3(x1, y1, z1);
								if (main.skillBlockBreak(player, pos, level)) {
									i++;
								}
							}
						}
					}
					main.cooltime(player, i / 50);
				break;
			}
		}
	}
}
