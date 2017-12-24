package tedo.ShowPlayerInventory;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerSetSlotPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.plugin.PluginBase;
import tedo.InventoryAPI.CreateDoubleChestInventory;
import tedo.InventoryAPI.InventoryAPI;

public class ShowPlayerInventory extends PluginBase implements Listener{

	public InventoryAPI api;
	public HashMap<String, CreateDoubleChestInventory> chest = new HashMap<String, CreateDoubleChestInventory>();
	public HashMap<String, Player> player = new HashMap<String, Player>();

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		this.api = (InventoryAPI) getServer().getPluginManager().getPlugin("InventoryAPI");
		if (this.api == null) {
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	@EventHandler
	public void onDataPacketReceive(DataPacketReceiveEvent event) {
		DataPacket pk = event.getPacket();
		if (pk instanceof ContainerSetSlotPacket) {
			Player player = event.getPlayer();
			String name = player.getName();
			if (this.chest.containsKey(name)) {
				ContainerSetSlotPacket pk1 = (ContainerSetSlotPacket) pk;
				int slot = pk1.slot;
				Item item = pk1.item;
				if (pk1.windowid == 66) {
					CreateDoubleChestInventory inventory = this.chest.get(name);
					if (item.getId() == 102 && item.getCustomName().equals("§a仕切り")) {
						Item item1 = inventory.getItem(slot);
						if (item1.getId() != 0) {
							if (27 <= slot && slot <= 35) {
								inventory.sendContents();
							}else if (49 <= slot && slot <= 53) {
								inventory.sendContents();
							}else{
								inventory.setItem(slot, Item.get(0));
							}
						}
						inventory.sendContents();
						return;
					}
					event.setCancelled();
					Player target = this.player.get(name);
					if (target == null) {
						this.chest.get(name).InventoryClose();
						this.chest.remove(name);
						this.player.remove(name);
					}
					if (27 <= slot && slot <= 35) {
						inventory.sendContents();
						player.getInventory().addItem(item);
						return;
					}else if (49 <= slot && slot <= 53) {
						inventory.sendContents();
						player.getInventory().addItem(item);
						return;
					}else if (45 == slot) {
						if (item.isHelmet() || item.getId() == 0) {
							target.getInventory().setArmorItem(slot - 45, item);
							target.getInventory().sendArmorContents(target);
							inventory.setItem(slot, item);
						}else{
							inventory.sendContents();
							player.getInventory().addItem(item);
						}
					}else if (46 == slot) {
						if (item.isChestplate() || item.getId() == 0) {
							target.getInventory().setArmorItem(slot - 45, item);
							target.getInventory().sendArmorContents(target);
							inventory.setItem(slot, item);
						}else{
							inventory.sendContents();
							player.getInventory().addItem(item);
						}
					}else if (47 == slot) {
						if (item.isLeggings() || item.getId() == 0) {
							target.getInventory().setArmorItem(slot - 45, item);
							target.getInventory().sendArmorContents(target);
							inventory.setItem(slot, item);
						}else{
							inventory.sendContents();
							player.getInventory().addItem(item);
						}
					}else if (48 == slot) {
						if (item.isBoots() || item.getId() == 0) {
							target.getInventory().setArmorItem(slot - 45, item);
							target.getInventory().sendArmorContents(target);
							inventory.setItem(slot, item);
						}else{
							inventory.sendContents();
							player.getInventory().addItem(item);
						}
					}else if (36 <= slot && slot <= 44) {
						target.getInventory().setItem(slot - 36, item);
						target.getInventory().sendContents(target);
						inventory.setItem(slot, item);
					}else if (0 <= slot && slot <= 26) {
						target.getInventory().setItem(slot + 9, item);
						target.getInventory().sendContents(target);
						inventory.setItem(slot, item);
					}
				}else if (pk1.windowid == 0) {
					if (item.getId() == 102 && item.getCustomName().equals("§a仕切り")) {
						if (player.getInventory().getItem(slot).getId() != 0) {
							player.getInventory().setItem(slot, Item.get(0));
						}
						player.getInventory().sendContents(player);
						return;
					}
					player.getInventory().setItem(slot, item);
				}
			}
		}else if (pk instanceof ContainerClosePacket) {
			Player player = event.getPlayer();
			String name = player.getName();
			if (this.chest.containsKey(name)) {
				this.chest.get(name).sendBeforeBlock();
				this.chest.remove(name);
			}
			if (this.player.containsKey(name)) {
				this.player.remove(name);
			}
		}
	}

	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		switch(label){
			case "open":
				try{
					if(args[0] != null){
						Player player = getServer().getPlayer(args[0]);
						if (player == null) {
							sender.sendMessage("§a>>§b" + args[0] + "は現在サーバーにいません");
							return false;
						}
						String name = player.getName();
						Player s = (Player) sender;
						int x = (int) s.x;
						int y = (int) s.y;
						int z = (int) s.z;
						PlayerInventory inventory = player.getInventory();
						CreateDoubleChestInventory chest = this.api.getDoubleChestInventory(s, x, y - 2, z);
						chest.sendChest();
						for (int slot = 0; slot < 54; slot++) {
							Item item = inventory.getItem(slot);
							if (0 <= slot && slot <= 8) {
								chest.setItem(slot + 36, item);
							}else if (9 <= slot && slot <= 35) {
								chest.setItem(slot - 9, item);
							}else if (36 <= slot && slot <= 44) {
								chest.setItem(slot - 9, Item.get(102, 0, 1).setCustomName("§a仕切り"));
							}else if (45 <= slot && slot <= 53) {
								chest.setItem(slot, Item.get(102, 0, 1).setCustomName("§a仕切り"));
							}
						};
						Item[] armor = inventory.getArmorContents();
						for (int i = 0; i < armor.length; i++) {
							chest.setItem(i + 45, armor[i]);
						}
						chest.setChestName("§2" + name + "のインベントリー  §c※クラシックで見てください");
						this.player.put(sender.getName(), player);
						this.chest.put(sender.getName(), chest);
						getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
							@Override
							public void run() {
								chest.InventoryOpen();
								chest.sendContents();
							}
						}, 3);
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/open [名前]");
				}
			break;
		}
		return false;
	}
}
