package tedo.SeichiSystemPlugin.skill;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import tedo.SeichiSystemPlugin.SeichiSystemPlugin;

public class skill2{

	public skill2(Player player, Block block, SeichiSystemPlugin main, Boolean r) {
		int x = (int) block.x;
		int y = (int) block.y;
		int z = (int) block.z;
		Level level = block.level;
		int x1;
		int y1;
		int z1;
		Vector3 pos;
		if (player.isSneaking() && y < player.y) {
			if (r) {
				switch (main.getDirection(player)) {
					case 0:
						for (y1 = y; y1 != y - 3; y1--) {
							for (z1 = z + 1; z1 != z - 2; z1--) {
								x1 = x;
								pos = new Vector3(x1, y1, z1);
								main.skillBlockBreak(player, pos, level);
							}
						}
					break;

					case 1:
						for (y1 = y; y1 != y - 3; y1--) {
							for (x1 = x + 1; x1 != x - 2; x1--) {
								z1 = z;
								pos = new Vector3(x1, y1, z1);
								main.skillBlockBreak(player, pos, level);
							}
						}
					break;

					case 2:
						for (y1 = y; y1 != y - 3; y1--) {
							for (z1 = z + 1; z1 != z - 2; z1--) {
								x1 = x;
								pos = new Vector3(x1, y1, z1);
								main.skillBlockBreak(player, pos, level);
							}
						}
					break;

					case 3:
						for (y1 = y; y1 != y - 3; y1--) {
							for (x1 = x + 1; x1 != x - 2; x1--) {
								z1 = z;
								pos = new Vector3(x1, y1, z1);
								main.skillBlockBreak(player, pos, level);
							}
						}
					break;
				}
			}else{
				switch (main.getDirection(player)) {
					case 0:
						for (y1 = y; y1 != y - 2; y1--) {
							for (z1 = z + 1; z1 != z - 2; z1--) {
								x1 = x;
								pos = new Vector3(x1, y1, z1);
								main.skillBlockBreak(player, pos, level);
							}
						}
					break;

					case 1:
						for (y1 = y; y1 != y - 2; y1--) {
							for (x1 = x + 1; x1 != x - 2; x1--) {
								z1 = z;
								pos = new Vector3(x1, y1, z1);
								main.skillBlockBreak(player, pos, level);
							}
						}
					break;

					case 2:
						for (y1 = y; y1 != y - 2; y1--) {
							for (z1 = z + 1; z1 != z - 2; z1--) {
								x1 = x;
								pos = new Vector3(x1, y1, z1);
								main.skillBlockBreak(player, pos, level);
							}
						}
					break;

					case 3:
						for (y1 = y; y1 != y - 3; y1--) {
							for (x1 = x + 1; x1 != x - 2; x1--) {
								z1 = z;
								pos = new Vector3(x1, y1, z1);
								main.skillBlockBreak(player, pos, level);
							}
						}
					break;
				}
			}
		}else{
			if (y <= player.y) {
				y = (int) player.y;
			}
			if (r) {
				switch (main.getDirection(player)) {
					case 0:
						for (z1 = z + 1; z1 != z - 2; z1--) {
							pos = new Vector3(x, y + 2, z1);
							main.skillBlockBreak(player, pos, level);
						}
					break;

					case 1:
						for (x1 = x + 1; x1 != x - 2; x1--) {
							pos = new Vector3(x1, y + 2, z);
							main.skillBlockBreak(player, pos, level);
						}
					break;

					case 2:
						for (z1 = z + 1; z1 != z - 2; z1--) {
							pos = new Vector3(x, y + 2, z1);
							main.skillBlockBreak(player, pos, level);
						}
					break;

					case 3:
						for (x1 = x + 1; x1 != x - 2; x1--) {
							pos = new Vector3(x1, y + 2, z);
							main.skillBlockBreak(player, pos, level);
						}
					break;
				}
			}
			switch (main.getDirection(player)) {
				case 0:
					for (y1 = y + 1; y1 != y -1; y1--) {
						for (z1 = z + 1; z1 != z - 2; z1--) {
							x1 = x;
							pos = new Vector3(x1, y1, z1);
							main.skillBlockBreak(player, pos, level);
						}
					}
				break;

				case 1:
					for (y1 = y + 1; y1 != y -1; y1--) {
						for (x1 = x + 1; x1 != x - 2; x1--) {
							z1 = z;
							pos = new Vector3(x1, y1, z1);
							main.skillBlockBreak(player, pos, level);
						}
					}
				break;

				case 2:
					for (y1 = y + 1; y1 != y -1; y1--) {
						for (z1 = z + 1; z1 != z - 2; z1--) {
							x1 = x;
							pos = new Vector3(x1, y1, z1);
							main.skillBlockBreak(player, pos, level);
						}
					}
				break;

				case 3:
					for (y1 = y + 1; y1 != y -1; y1--) {
						for (x1 = x + 1; x1 != x - 2; x1--) {
							z1 = z;
							pos = new Vector3(x1, y1, z1);
							main.skillBlockBreak(player, pos, level);
						}
					}
				break;
			}
		}
	}
}
