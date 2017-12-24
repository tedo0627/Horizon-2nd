package tedo.EconomySystemLand;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import cn.nukkit.utils.Config;

public class LandSystem {

	public Config config;
	public EconomySystemLand main;
	public HashMap<String, HashMap<Integer, Land>> level = new HashMap<String, HashMap<Integer, Land>>();
	public HashMap<Integer, Land> levels = new HashMap<Integer, Land>();

	@SuppressWarnings("unchecked")
	public LandSystem(Config config, EconomySystemLand main) {

		this.config = config;
		this.main = main;

		config.getAll().forEach((id, data) -> {
			setData(Integer.parseInt(id), (HashMap<String, Object>) data);
		});
	}

	public void setData(int id, HashMap<String, Object> data) {
		Land land = new Land(data, main);
		String level = land.level;
		HashMap<Integer, Land> datas;

		if (this.level.containsKey(level)) {
			datas = this.level.get(level);
		}else{
			datas = new HashMap<Integer, Land>();
		}
		datas.put(id, land);
		this.level.put(level, datas);

		this.levels.put(id, land);
	}

	public void save(Land land) {
		this.config.set(String.valueOf(land.id), land.getData());
		this.config.save();
	}

	public void save(HashMap<String, Object> data) {
		this.config.set(String.valueOf(data.get("id")), data);
		this.config.save();
	}

	public boolean isLand(int id) {
		if (levels.containsKey(id)) {
			return true;
		}else{
			return false;
		}
	}

	public boolean isLand(int x, int z, String level) {
		HashMap<String, Boolean> data = new HashMap<String, Boolean>();
		data.put("return", false);
		if (this.level.containsKey(level)) {
			this.level.get(level).forEach((id, land) -> {
				if (land.isLand(x, z)) {
					data.put("return", true);
				}
			});
		}
		return data.get("return");
	}

	public Land getLand(int id) {
		return levels.get(id);
	}

	public Land getLand(int x, int z, String level) {
		HashMap<String, Land> data = new HashMap<String, Land>();
		data.put("return", null);
		if (this.level.containsKey(level)) {
			this.level.get(level).forEach((id, land) -> {
				if (land.isLand(x, z)) {
					data.put("return", land);
				}
			});
		}
		return data.get("return");
	}

	public void addLand(HashMap<String, Object> data) {
		int id = (int) data.get("id");
		setData(id, data);
		save(new Land(data, main));
	}

	public void delLand(int id) {
		if (levels.containsKey(id)) {
			Land land = levels.get(id);
			levels.remove(id);
			String level = land.level;
			if (this.level.containsKey(level)) {
				if (this.level.get(level).containsKey(id)) {
					HashMap<Integer, Land> data = this.level.get(level);
					data.remove(id);
					this.level.put(level, data);
				}
			}
			if (config.exists(String.valueOf(id))) {
				config.remove(String.valueOf(id));
				config.save();
			}
		}
	}

	public void updateLand(Land land) {
		int id = land.id;
		String level = land.level;
		HashMap<Integer, Land> data = this.level.get(level);
		data.put(id, land);
		this.level.put(level, data);
		this.levels.put(id, land);
		save(land);
	}

	public ArrayList<Land> getMyLands(String name) {
		ArrayList<Land> lands = new ArrayList<Land>();
		this.levels.forEach((id, land) -> {
			if (land.isOwner(name)) {
				lands.add(land);
			}
		});
		return lands;
	}

	public ArrayList<Land> getMyLands(String name, String level) {
		ArrayList<Land> lands = new ArrayList<Land>();
		if (this.level.containsKey(level)) {
			this.level.get(level).forEach((id, land) -> {
				if (land.isOwner(name)) {
					lands.add(land);
				}
			});
		}
		return lands;
	}

	public int getMyLandCount(String name) {
		HashMap<String, Integer> count = new HashMap<String, Integer>();
		count.put("return", 0);
		this.levels.forEach((id, land) -> {
			if (land.isOwner(name)) {
				int c = count.get("return");
				count.put("return", c + 1);
			}
		});
		return count.get("return");
	}

	public int getMyLandCount(String name, String level) {
		HashMap<String, Integer> count = new HashMap<String, Integer>();
		count.put("return", 0);
		if (this.level.containsKey(level)) {
			this.level.get(level).forEach((id, land) -> {
				if (land.isOwner(name)) {
					int c = count.get("return");
					count.put("return", c + 1);
				}
			});
		}
		return count.get("return");
	}

	public int getMyLandBlockCount(String name) {
		HashMap<String, Integer> count = new HashMap<String, Integer>();
		count.put("return", 0);
		levels.forEach((id, land) -> {
			if (land.isOwner(name)) {
				int c = count.get("return");
				count.put("return", c + land.getBlockCount());
			}
		});
		return count.get("return");
	}

	public void delAutoLand(String level, int time) {
		long t = 1000 * 60 * 60 * 24 * time;
		long now = System.currentTimeMillis();
		if (this.level.containsKey(level)) {
			try {
				this.level.get(level).forEach((id, land) -> {
					if (now - land.time >= t) {
						delLand(id);
					}
				});
			}
			catch (ConcurrentModificationException e) {

			}
		}
	}

	public void delBanLand(String name) {
		try{
			this.levels.forEach((id, land) -> {
				if (land.isOwner(name)) {
					delLand(id);
				}
			});
		}
		catch (ConcurrentModificationException e) {

		}
	}

	public Boolean canEditLand(String name, int x, int z, String level) {
		HashMap<String, Boolean> data = new HashMap<String, Boolean>();
		data.put("return", null);
		if (this.level.containsKey(level)) {
			this.level.get(level).forEach((id, land) -> {
				if (land.isLand(x, z)) {
					if (land.isEdit(name)) {
						data.put("return", true);
					}else{
						data.put("return", false);
					}
				}
			});
		}
		return data.get("return");
	}

	public void updateTime(String name) {
		this.levels.forEach((id, land) -> {
			if (land.isOwner(name)) {
				land.setTime();
				updateLand(land);
			}
		});
	}
}