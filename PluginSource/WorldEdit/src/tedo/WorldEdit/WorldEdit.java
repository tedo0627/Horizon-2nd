package tedo.WorldEdit;

import java.util.ArrayList;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.ItemHoeIron;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;

public class WorldEdit extends PluginBase implements Listener{

	public HashMap<String, Vector3> pos1 = new HashMap<String, Vector3>();
	public HashMap<String, Vector3> pos2 = new HashMap<String, Vector3>();

	public HashMap<String, HashMap<Position, Block>> copy = new HashMap<String, HashMap<Position, Block>>();

	public ArrayList<HashMap<Position, Block>> undo = new ArrayList<HashMap<Position, Block>>();

	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);

		this.getServer().getCommandMap().register("/", new MainCommand(this));
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.isOp() && player.isCreative()) {
			if (player.getInventory().getItemInHand() instanceof ItemHoeIron) {
				Block block = event.getBlock();
				if (!(block instanceof BlockAir)) {
					int x = (int) block.x;
					int y = (int) block.y;
					int z = (int) block.z;
					Vector3 pos = new Vector3(x, y, z);
					this.pos1.put(player.getName(), pos);
					player.sendMessage("§a>>§b一つ目の位置 | x " + x + " : y " + y + " : z " + z + " | 範囲 : " + getArea(player.getName()));
					player.sendMessage("§a>>§bブロック名 : " + block.getName() + " | ID " + block.getId() + ":" + block.getDamage());
					event.setCancelled();
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.isOp() && player.isCreative()) {
			if (player.getInventory().getItemInHand() instanceof ItemHoeIron) {
				Block block = event.getBlock();
				if (!(block instanceof BlockAir)) {
					int x = (int) block.x;
					int y = (int) block.y;
					int z = (int) block.z;
					Vector3 pos = new Vector3(x, y, z);
					this.pos2.put(player.getName(), pos);
					player.sendMessage("§a>>§b二つ目の位置 | x " + x + " : y " + y + " : z " + z + " | 範囲 : " + getArea(player.getName()));
					player.sendMessage("§a>>§bブロック名 : " + block.getName() + " | ID " + block.getId() + ":" + block.getDamage());
					event.setCancelled();
				}
			}
		}
	}

	public boolean isVector1(String name) {
		if (this.pos1.containsKey(name)) {
			return true;
		}else{
			return false;
		}
	}

	public boolean isVector2(String name) {
		if (this.pos2.containsKey(name)) {
			return true;
		}else{
			return false;
		}
	}

	public Vector3 getVector1(String name) {
		if (this.pos1.containsKey(name)) {
			return this.pos1.get(name);
		}else{
			return null;
		}
	}

	public Vector3 getVector2(String name) {
		if (this.pos2.containsKey(name)) {
			return this.pos2.get(name);
		}else{
			return null;
		}
	}

	public int getArea(String name) {
		if (!this.pos1.containsKey(name)) {
			return 0;
		}
		if (!this.pos2.containsKey(name)) {
			return 0;
		}
		Vector3 pos1 = this.pos1.get(name);
		Vector3 pos2 = this.pos2.get(name);
		int x1 = (int) pos1.x;
		int y1 = (int) pos1.y;
		int z1 = (int) pos1.z;
		int x2 = (int) pos2.x;
		int y2 = (int) pos2.y;
		int z2 = (int) pos2.z;
		if (x1 < x2) {
			x1 = x1 + x2;
			x2 = x1 - x2;
			x1 = x1 - x2;
		}
		if (y1 < y2) {
			y1 = y1 + y2;
			y2 = y1 - y2;
			y1 = y1 - y2;
		}
		if (z1 < z2) {
			z1 = z1 + z2;
			z2 = z1 - z2;
			z1 = z1 - z2;
		}
		int x = x1 - x2 + 1;
		int y = y1 - y2 + 1;
		int z = z1 - z2 + 1;
		return x * y * z;
	}

	public void addUndo(HashMap<Position, Block> undo) {
		this.undo.add(undo);
	}

	public HashMap<Position, Block> getUndo() {
		if (this.undo.size() < 1) {
			return null;
		}
		return this.undo.remove(this.undo.size() - 1);
	}

	public boolean isCopy(String name) {
		return this.copy.containsKey(name);
	}

	public void addCopy(String name, HashMap<Position, Block> copy) {
		this.copy.put(name, copy);
	}

	public HashMap<Position, Block> getCopy(String name) {
		return this.copy.get(name);
	}
}
