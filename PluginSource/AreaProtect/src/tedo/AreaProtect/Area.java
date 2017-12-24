package tedo.AreaProtect;

import java.util.HashMap;

public class Area {

	public int x1, y1, z1, x2, y2, z2;
	public String level;

	@SuppressWarnings("rawtypes")
	public Area(HashMap data) {
		level = (String) data.get("level");
		x1 = Integer.parseInt((String) data.get("x1"));
		y1 = Integer.parseInt((String) data.get("y1"));
		z1 = Integer.parseInt((String) data.get("z1"));
		x2 = Integer.parseInt((String) data.get("x2"));
		y2 = Integer.parseInt((String) data.get("y2"));
		z2 = Integer.parseInt((String) data.get("z2"));
	}

	public boolean isPosition(int x, int y, int z, String level) {
		boolean r = false;
		if (level.equals(this.level) && x1 >= x && x2 <= x && y1 >= y && y2 <= y && z1 >= z && z2 <= z) {
			r = true;
		}
		return r;
	}
}
