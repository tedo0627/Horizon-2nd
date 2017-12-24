package tedo.SeichiSystemPlugin;

import cn.nukkit.plugin.PluginBase;

public class LevelSystem extends PluginBase{

	public int count;
	public int up;

	public LevelSystem(int count) {
		this.count = count;
		this.up = 100;
	}

	public LevelSystem(int count, int up) {
		this.count = count;
		this.up = up + 100;
	}


	public int getLevel() {
		int count = this.count;
		if (count >= 0 && count <= 2999) {//0~9
			return count / 300;

		}else if (count >= 3000 && count <= 9999) {//10~19
			count = count - 3000;
			return count / 700 + 10;

		}else if (count >= 10000 && count <= 39999) {//20~29
			count = count - 10000;
			return count / 3000 + 20;

		}else if (count >= 40000 && count <= 159999) {//30~39
			count = count - 40000;
			return count / 12000 + 30;

		}else if (count >= 160000 && count <= 299999) {//40~49
			count = count - 160000;
			return count / 14000 + 40;

		}else if (count >= 300000 && count <= 1099999) {//50~59
			count = count - 300000;
			return count / 80000 + 50;

		}else if (count >= 1100000 && count <= 1999999) {//60~69
			count = count - 1100000;
			return count / 90000 + 60;

		}else if (count >= 2000000 && count <= 4999999) {//70~79
			count = count - 2000000;
			return count / 300000 + 70;

		}else if (count >= 5000000 && count <= 14999999) {//80~89
			count = count - 5000000;
			return count / 1000000 + 80;

		}else if (count >= 15000000 && count <= 39999999){//90~99
			count = count - 15000000;
			return count / 2500000 + 90;

		}else{
			if (this.up > 100) {
				count = count - 40000000;
				int l = count / 2000000 + 100;
				if (l < this.up) {
					return l;
				}else{
					return this.up;
				}
			}else{
				return 100;
			}
		}
	}

	public int getMaxNextLevel() {
		int count = this.count;
		if (count >= 0 && count <= 2999) {//0~9
			return 300;

		}else if (count >= 3000 && count <= 9999) {//10~19
			return 700;

		}else if (count >= 10000 && count <= 39999) {//20~29
			return 3000;

		}else if (count >= 40000 && count <= 159999) {//30~39
			return 12000;

		}else if (count >= 160000 && count <= 299999) {//40~49
			return 14000;

		}else if (count >= 300000 && count <= 1099999) {//50~59
			return 80000;

		}else if (count >= 1100000 && count <= 1999999) {//60~69
			return 90000;

		}else if (count >= 2000000 && count <= 4999999) {//70~79
			return 300000;

		}else if (count >= 5000000 && count <= 14999999) {//80~89
			return 1000000;

		}else if (count >= 15000000 && count <= 39999999){//90~99
			return 2500000;

		}else{
			if (getLevel() < this.up) {
				return 2000000;
			}else{
				return 0;
			}
		}
	}

	public int getNextLevel() {
		int level = getLevel();
		int count = this.count;
		if (level >= 0 && level <= 9) {//0~9
			return (count / 300 + 1) * 300 - count;

		}else if (level >= 10 && level <= 19) {//10~19
			count = count - 3000;
			return (count / 700 + 1) * 700 - count;

		}else if (level >= 20 && level <= 29) {//20~29
			count = count - 10000;
			return (count / 3000 + 1) * 3000 - count;

		}else if (level >= 30 && level <= 39) {//30~39
			count = count - 40000;
			return (count / 12000 + 1) * 12000 - count;

		}else if (level >= 40 && level <= 49) {//40~49
			count = count - 160000;
			return (count / 14000 + 1) * 14000 - count;

		}else if (level >= 50 && level <= 59) {//50~59
			count = count - 300000;
			return (count / 80000 + 1) * 80000 - count;

		}else if (level >= 60 && level <= 69) {//60~69
			count = count - 1100000;
			return (count / 90000 + 1) * 90000 - count;

		}else if (level >= 70 && level <= 79) {//70~79
			count = count - 2000000;
			return (count / 300000 + 1) * 300000 - count;

		}else if (level >= 80 && level <= 89) {//80~89
			count = count - 5000000;
			return (count / 1000000 + 1) * 1000000 - count;

		}else if (level >= 90 && level <= 99) {//90~99
			count = count - 15000000;
			return (count / 2500000 + 1) * 2500000 - count;

		}else{
			if (getLevel() < this.up) {
				count = count - 40000000;
				return (count / 2000000 + 1) * 2000000 - count;
			}else{
				return 0;
			}
		}
	}

	public int getProportion() {
		int level = getLevel();
		if (level >= 100) {
			if (level < this.up) {
				double i = getNextLevel() * 100 / getMaxNextLevel();
				int r = (int) i;
				if (r == 100) {
					r = 99;
				}
				return 100 - r;
			}else{
				return 100;
			}
		}else{
			double i = getNextLevel() * 100 / getMaxNextLevel();
			int r = (int) i;
			if (r == 100) {
				r = 99;
			}
			return 100 - r;
		}
	}

	public boolean isLevelUp() {
		int count = this.count;
		if (count >= 0 && count <= 2999) {//0~9
			if (count % 300 == 0) {
				return true;
			}else{
				return false;
			}
		}else if (count >= 3000 && count <= 9999) {//10~19
			count = count - 3000;
			if (count % 700 == 0) {
				return true;
			}else{
				return false;
			}
		}else if (count >= 10000 && count <= 39999) {//20~29
			count = count - 10000;
			if (count % 3000 == 0) {
				return true;
			}else{
				return false;
			}
		}else if (count >= 40000 && count <= 159999) {//30~39
			count = count - 40000;
			if (count % 12000 == 0) {
				return true;
			}else{
				return false;
			}
		}else if (count >= 160000 && count <= 299999) {//40~49
			count = count - 160000;
			if (count % 14000 == 0) {
				return true;
			}else{
				return false;
			}
		}else if (count >= 300000 && count <= 1099999) {//50~59
			count = count - 300000;
			if (count % 80000 == 0) {
				return true;
			}else{
				return false;
			}
		}else if (count >= 1100000 && count <= 1999999) {//60~69
			count = count - 1100000;
			if (count % 90000 == 0) {
				return true;
			}else{
				return false;
			}
		}else if (count >= 2000000 && count <= 4999999) {//70~79
			count = count - 2000000;
			if (count % 300000 == 0) {
				return true;
			}else{
				return false;
			}
		}else if (count >= 5000000 && count <= 14999999) {//80~89
			count = count - 5000000;
			if (count % 1000000 == 0) {
				return true;
			}else{
				return false;
			}
		}else if (count >= 15000000 && count <= 40000000){//90~99
			count = count - 15000000;
			if (count % 2500000 == 0) {
				return true;
			}else{
				return false;
			}
		}else{
			if (getLevel() <= this.up) {
				count = count - 40000000;
				if (count % 2000000 == 0) {
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
	}
}
