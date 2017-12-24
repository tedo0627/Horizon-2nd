package tedo.EconomySystemLand;

import java.util.HashMap;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

public class Land {

	public HashMap<String, Object> data;
	public EconomySystemLand main;

	public int x1, z1, x2, z2;
	public int id;
	public String level;
	public String owner;
	public String invite;
	public long time;
	public int[] position;

	public Land(HashMap<String, Object> data, EconomySystemLand main) {
		this.data = data;
		this.main = main;
		dataPut();
	}

	public void dataPut() {
		String[] pos = new String[3];
		pos = ((String) data.get("position")).split(":");

		x1 = (int) data.get("x1");
		z1 = (int) data.get("z1");
		x2 = (int) data.get("x2");
		z2 = (int) data.get("z2");
		id = (int) data.get("id");
		owner = (String) data.get("owner");
		invite = (String) data.get("invite");
		level = (String) data.get("level");
		time = (long) data.get("time");
		position = new int[3];
		position[0] = Integer.parseInt(pos[0]);
		position[1] = Integer.parseInt(pos[1]);
		position[2] = Integer.parseInt(pos[2]);
	}

	public void dataUpdate() {
		data.put("x1", x1);
		data.put("z1", z1);
		data.put("x2", x2);
		data.put("z2", z2);
		data.put("id", id);
		data.put("owner", owner);
		data.put("invite", invite);
		data.put("level", level);
		data.put("time", time);
		data.put("position", getPositionString());
	}

	public HashMap<String, Object> getData() {
		dataUpdate();
		return data;
	}

	//オーナー系

	public boolean isOwner(String name) {
		if (owner.equals(name)) {
			return true;
		}else{
			return false;
		}
	}

	public void setOwner(String name) {
		owner = name;
	}

	//共有系

	public boolean isInvite(String name) {
		boolean r = false;
		String[] data = invite.split(":");
		for (int i = 0; i < data.length; i++) {
			if (data[i].equals(name)) {
				r = true;
				break;
			}
		}
		return r;
	}

	public void addInvite(String name) {
		invite = invite + name + ":";
	}

	public void delInvite(String name) {
		String[] data = invite.split(":");
		String inv = "";
		for (int i = 0; i < data.length; i++) {
			if (!data[i].equals(name)) {
				inv = inv + name + ":";
			}
		}
		invite = inv;
	}

	public void clearInvite() {
		invite = "";
	}

	//時間系

	public boolean isTime(long time) {
		if (System.currentTimeMillis() - this.time >= time) {
			return true;
		}else{
			return false;
		}
	}

	public void setTime() {
		time = System.currentTimeMillis();
	}

	//範囲内のブロックの数

	public int getBlockCount() {
		int x = this.x1 - this.x2 + 1;
		int z = this.z1 - this.z2 + 1;
		return x * z;
	}

	//テレポート
	public Position getPosition() {
		Level level = main.getServer().getLevelByName(this.level);
		int x = position[0];
		int y = position[1];
		int z = position[2];
		return new Position(x, y, z, level);
	}

	public void setPosition(Position position) {
		int x = (int) position.x;
		int y = (int) position.y;
		int z = (int) position.z;
		this.position[0] = x;
		this.position[1] = y;
		this.position[2] = z;
	}

	//確認

	public boolean isLand(int x, int z) {
		if (x <= x1 && x2 <= x) {
			if (z <= z1 && z2 <= z) {
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public boolean isEdit(String name) {
		if (isOwner(name) || isInvite(name)) {
			return true;
		}else{
			return false;
		}
	}

	public String getPositionString() {
		String x = String.valueOf((int) position[0]);
		String y = String.valueOf((int) position[1]);
		String z = String.valueOf((int) position[2]);
		return x + ":" + y + ":" + z;
	}
}