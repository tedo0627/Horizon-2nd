package tedo.InventoryAPI;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;

public class InventoryAPI extends PluginBase {

	public CreateChestInventory getChestInventory(Player player, int x, int y, int z) {
		return new CreateChestInventory(player, x, y, z);
	}

	public CreateDoubleChestInventory getDoubleChestInventory(Player player, int x, int y, int z) {
		return new CreateDoubleChestInventory(player, x, y, z);
	}
}
