package tedo.OtherPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.network.protocol.ChangeDimensionPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class OtherPlugin extends PluginBase implements Listener{

	public Config config;
	public HashMap<String, Boolean> level = new HashMap<String, Boolean>();
	public HashMap<String, Boolean> ops = new HashMap<String, Boolean>();

	@SuppressWarnings("deprecation")
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getDataFolder().mkdirs();
		this.config = new Config(new File(this.getDataFolder(), "config.yml"),Config.YAML,
		new LinkedHashMap<String, Object>() {
			{
				put("endにするワールド", "end:end2");
			}
		}
		);
		String[] datas = ((String) this.config.get("endにするワールド")).split(":");
		for (int i = 0; i < datas.length; i++) {
			this.level.put(datas[i], true);
		}
		getServer().getOps().getAll().forEach((name, t) -> {
			this.ops.put(name, true);
		});
	}

	public void onDisable() {
		this.ops.forEach((name, t) -> {
			getServer().addOp(name);
		});
	}

	@EventHandler
	public void onDataPacketReceive(DataPacketReceiveEvent event) {
		DataPacket pk = event.getPacket();
		if (pk instanceof LoginPacket) {
			String name = ((LoginPacket) pk).username.toLowerCase();
			if (this.ops.containsKey(name)) {
				getServer().removeOp(name);
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		if (this.ops.containsKey(name)) {
			player.setOp(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Item item = event.getItem();
		if (item.getEnchantment(Enchantment.ID_SILK_TOUCH) != null && item.getEnchantment(Enchantment.ID_SILK_TOUCH).getLevel() > 0) {
			Block block = event.getBlock();
			int id = block.getId();
			Item[] items = new Item[1];
			switch (id) {
				case 1:
					if (item.isPickaxe()) {
						items[0] = Item.get(1, 0, 1);
						event.setDrops(items);
					}
				break;

				case 2:
					items[0] = Item.get(2, 0, 1);
					event.setDrops(items);
				break;

				case 13:
					items[0] = Item.get(13, 0, 1);
					event.setDrops(items);
				break;

				case 16:
					if (item.isPickaxe()) {
						items[0] = Item.get(16, 0, 1);
						event.setDrops(items);
					}
				break;

				case 18:
					items[0] = Item.get(18, block.getDamage(), 1);
					event.setDrops(items);
				break;

				case 20:
					items[0] = Item.get(20, 0, 1);
					event.setDrops(items);
				break;

				case 21:
					if (item.isPickaxe()) {
						items[0] = Item.get(21, 0, 1);
						event.setDrops(items);
					}
				break;

				case 56:
					if (item.isPickaxe()) {
						items[0] = Item.get(56, 0, 1);
						event.setDrops(items);
					}
				break;

				case 73:
					if (item.isPickaxe()) {
						items[0] = Item.get(73, 0, 1);
						event.setDrops(items);
					}
				break;

				case 74:
					if (item.isPickaxe()) {
						items[0] = Item.get(74, 0, 1);
						event.setDrops(items);
					}
				break;

				case 79:
					if (item.isPickaxe()) {
						items[0] = Item.get(79, 0, 1);
						event.setDrops(items);
					}
				break;

				case 89:
					items[0] = Item.get(89, 0, 1);
					event.setDrops(items);
				break;

				case 102:
					items[0] = Item.get(102, 0, 1);
					event.setDrops(items);
				break;

				case 103:
					items[0] = Item.get(103, 0, 1);
					event.setDrops(items);
				break;

				case 129:
					items[0] = Item.get(129, 0, 1);
					event.setDrops(items);
				break;

				case 130:
					if (item.isPickaxe()) {
						items[0] = Item.get(130, 0, 1);
						event.setDrops(items);
					}
				break;

				case 153:
					if (item.isPickaxe()) {
						items[0] = Item.get(153, 0, 1);
						event.setDrops(items);
					}
				break;

				case 161:
					items[0] = Item.get(161, item.getDamage(), 1);
					event.setDrops(items);
				break;
			}
		}else {
			if (item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING) != null && item.isPickaxe()) {
				int level = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING).getLevel();
				level = level > 3 ? 3 : level;
				Block block = event.getBlock();
				Item[] items = new Item[1];
				switch (block.getId()) {
					case 14:
						items[0] = Item.get(266, 0, 1);
						event.setDrops(items);
					break;

					case 15:
						items[0] = Item.get(265, 0, 1);
						event.setDrops(items);
					break;

					case 16:
						items[0] = Item.get(263, 0, fortune(level));
						event.setDrops(items);
					break;

					case 21:
						items[0] = Item.get(351, 4, fortune(level));
						event.setDrops(items);
					break;

					case 56:
						items[0] = Item.get(264, 0, fortune(level));
						event.setDrops(items);
					break;

					case 73:
						items[0] = Item.get(331, 0, fortune(level));
						event.setDrops(items);
					break;

					case 74:
						items[0] = Item.get(331, 0, fortune(level));
						event.setDrops(items);
					break;

					case 129:
						items[0] = Item.get(388, 0, fortune(level));
						event.setDrops(items);
					break;

					case 153:
						items[0] = Item.get(406, 0, fortune(level));
						event.setDrops(items);
					break;
				}
			}
		}
	}

	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		switch(command.getName()){
			case "id":
				Player player = (Player) sender;
				Item item = player.getInventory().getItemInHand();
				sender.sendMessage("§a>>§b貴方が手に持ってるアイテムのIDは" + String.valueOf(item.getId()) + ":" + String.valueOf(item.getDamage()) + "です");
			break;
		}
		return false;
	}

	public void dimensionTask(Player player) {
		ChangeDimensionPacket pk = new ChangeDimensionPacket();
		pk.dimension = 2;
		player.dataPacket(pk);
	}

	public int fortune(int level) {
		switch (new Random().nextInt(level)) {
			case 0:
			case 1:
				return 1;

			case 2:
				return 2;

			case 3:
				return 3;

			default:
				return 4;
		}
	}
}
