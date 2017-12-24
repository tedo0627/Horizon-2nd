package tedo.TestPlugin;

import java.text.NumberFormat;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.PlayerEnderChestInventory;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerSetSlotPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.plugin.PluginBase;
import tedo.EconomySystemAPI.EconomySystemAPI;
import tedo.GatyaSystem.GatyaSystem;
import tedo.InventoryAPI.CreateChestInventory;
import tedo.InventoryAPI.CreateDoubleChestInventory;
import tedo.InventoryAPI.InventoryAPI;
import tedo.ItemStorage.ItemStorage;
import tedo.SeichiSystemPlugin.LevelSystem;
import tedo.SeichiSystemPlugin.SeichiSystemPlugin;

public class TestPlugin extends PluginBase implements Listener{

	public HashMap<String, CreateChestInventory> data0 = new HashMap<String, CreateChestInventory>();
	public HashMap<String, CreateChestInventory> data1 = new HashMap<String, CreateChestInventory>();
	public HashMap<String, CreateChestInventory> data2 = new HashMap<String, CreateChestInventory>();
	public HashMap<String, CreateChestInventory> data3 = new HashMap<String, CreateChestInventory>();
	public HashMap<String, CreateChestInventory> data4 = new HashMap<String, CreateChestInventory>();
	public HashMap<String, CreateDoubleChestInventory> data9 = new HashMap<String, CreateDoubleChestInventory>();

	public HashMap<String, Item> hand = new HashMap<String, Item>();

	public SeichiSystemPlugin data5;
	public GatyaSystem data6;
	public ItemStorage data7;
	public EconomySystemAPI data8;
	public InventoryAPI api;

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		this.data5 = (SeichiSystemPlugin) getServer().getPluginManager().getPlugin("SeichiSystemPlugin");
		this.data6 = (GatyaSystem) getServer().getPluginManager().getPlugin("GatyaSystem");
		this.data7 = (ItemStorage) getServer().getPluginManager().getPlugin("ItemStorage");
		this.data8 = (EconomySystemAPI) getServer().getPluginManager().getPlugin("EconomySystemAPI");

		this.api = (InventoryAPI) getServer().getPluginManager().getPlugin("InventoryAPI");
		if (this.api == null) {
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	@EventHandler
	public void onDataPacketReceive(DataPacketReceiveEvent event) {
		DataPacket pk = event.getPacket();
		Player player = event.getPlayer();
		if (pk instanceof ContainerClosePacket) {
			String name = player.getName();
			if (this.hand.containsKey(name)) {
				if (player.getInventory().getItemInHand().getId() != 0) {
					player.getInventory().addItem(this.hand.get(name));
				}else{
					player.getInventory().setItemInHand(this.hand.get(name));
				}
				player.getInventory().sendContents(player);
				this.hand.remove(name);
			}
			if (this.data0.containsKey(name)) {
				this.data0.get(name).sendBeforeBlock();
				this.data0.remove(name);
			}
			if (this.data1.containsKey(name)) {
				this.data1.get(name).sendBeforeBlock();
				this.data1.remove(name);
			}
			if (this.data2.containsKey(name)) {
				this.data2.get(name).sendBeforeBlock();
				this.data2.remove(name);
			}
			if (this.data3.containsKey(name)) {
				this.data3.get(name).sendBeforeBlock();
				this.data3.remove(name);
			}
			if (this.data4.containsKey(name)) {
				this.data4.get(name).sendBeforeBlock();
				this.data4.remove(name);
			}
			if (this.data9.containsKey(name)) {
				CreateDoubleChestInventory chest = this.data9.get(name);
				player.namedTag.putList(new ListTag<CompoundTag>("DoubleChestInventory"));
				for (int slot = 0; slot < 54; slot++) {
					Item item = chest.getItem(slot);
					player.namedTag.getList("DoubleChestInventory", CompoundTag.class).add(NBTIO.putItemHelper(item, slot));
				}
				this.data9.get(name).sendBeforeBlock();
				this.data9.remove(name);
			}
			player.getInventory().sendContents(player);
		}else if (pk instanceof ContainerSetSlotPacket) {
			String name = player.getName();
			ContainerSetSlotPacket pk2 = (ContainerSetSlotPacket) pk;
			if (this.data0.containsKey(name)) {
				event.setCancelled();
				if (pk2.windowid == 65) {
					CreateChestInventory chest = this.data0.get(name);
					chest.sendContents();
					switch (pk2.slot) {
						case 3:
							getServer().dispatchCommand(player, "skill on");
						break;

						case 4:
							getServer().dispatchCommand(player, "skill off");
						break;

						case 5:
							chest.InventoryClose();
							this.data0.remove(name);
							CreateChestInventory chest1 = this.api.getChestInventory(player, chest.x, chest.y, chest.z);
							getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
								@Override
								public void run() {
									putData4(name, chest1);
									chest1.sendChest();
									chest1.setChestName("§1スキル選択欄");
									chest1.InventoryOpen();
									Item[] items1 = getSkillItem();
									for (int slot = 0; slot < items1.length; slot++) {
										chest1.setItem(slot, items1[slot]);
									}
									chest1.sendContents();
									Item item = player.getInventory().getItemInHand();
									player.getInventory().setItemInHand(Item.get(0));
									setHand(name, item);
								}
							}, 8);
						break;

						case 8:
							chest.InventoryClose();
							this.data0.remove(name);
							CreateChestInventory chest2 = this.api.getChestInventory(player, chest.x, chest.y, chest.z);
							getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
								@Override
								public void run() {
									putData1(name, chest2);
									chest2.sendChest();
									chest2.setChestName("§cゴミ箱  §4※ゴミ箱に入れたアイテムは戻ってきません");
									chest2.InventoryOpen();
									for (int slot = 0; slot < 27; slot++) {
										chest2.setItem(slot, Item.get(0));
									}
									chest2.sendContents();
									Item item = player.getInventory().getItemInHand();
									player.getInventory().setItemInHand(Item.get(0));
									setHand(name, item);
								}
							}, 8);
						break;

						case 9:
							getServer().dispatchCommand(player, "fly on");
						break;

						case 10:
							getServer().dispatchCommand(player, "fly off");
						break;

						case 12:
							getServer().dispatchCommand(player, "prize on");
						break;

						case 13:
							getServer().dispatchCommand(player, "prize off");
						break;

						case 16:
							chest.InventoryClose();
							this.data0.remove(name);
							CreateDoubleChestInventory chest5 = this.api.getDoubleChestInventory(player, chest.x, chest.y, chest.z);
							chest5.sendChest();
							chest5.setChestName("§1ラージチェスト");
							getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
								@Override
								public void run() {
									chest5.InventoryOpen();
									ListTag<CompoundTag> inventoryList = player.namedTag.getList("DoubleChestInventory", CompoundTag.class);
									for (CompoundTag item : inventoryList.getAll()) {
										int slot = item.getByte("Slot");
										chest5.setItem(slot, NBTIO.getItemHelper(item));
									}
									chest5.sendContents();
									putData9(name, chest5);
									Item item = player.getInventory().getItemInHand();
									player.getInventory().setItemInHand(Item.get(0, 0, 1));
									setHand(name, item);
								}
							}, 8);
						break;

						case 17:
							chest.InventoryClose();
							this.data0.remove(name);
							CreateChestInventory chest3 = this.api.getChestInventory(player, chest.x, chest.y, chest.z);
							getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
								@Override
								public void run() {
									chest3.sendChest();
									chest3.setChestName("§1エンダーチェスト");
									chest3.InventoryOpen();
									PlayerEnderChestInventory inventory = player.getEnderChestInventory();
									for (int slot = 0; slot < 27; slot++) {
										chest3.setItem(slot, inventory.getItem(slot));
									}
									chest3.sendContents();
									putData2(name, chest3);
									Item item = player.getInventory().getItemInHand();
									player.getInventory().setItemInHand(Item.get(0, 0, 1));
									setHand(name, item);
								}
							}, 8);
						break;

						case 18:
							getServer().dispatchCommand(player, "home set");
						break;

						case 19:
							getServer().dispatchCommand(player, "home warp");
						break;

						case 21:
							getServer().dispatchCommand(player, "item dl 388 1");
						break;

						case 22:
							getServer().dispatchCommand(player, "item dl 388 10");
						break;

						case 23:
							getServer().dispatchCommand(player, "item dl 388 64");
						break;

						case 26:
							chest.InventoryClose();
							this.data0.remove(name);
							CreateChestInventory chest4 = this.api.getChestInventory(player, chest.x, chest.y, chest.z);
							getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
								@Override
								public void run() {
									chest4.sendChest();
									chest4.setChestName("§2メニュー2");
									chest4.InventoryOpen();
									Item[] items3 = getItems2();
									for (int slot = 0; slot < items3.length; slot++) {
										chest4.setItem(slot, items3[slot]);
									}
									chest4.sendContents();
									putData3(name, chest4);
									Item item = player.getInventory().getItemInHand();
									player.getInventory().setItemInHand(Item.get(0));
									setHand(name, item);
								}
							}, 8);
						break;
					}
				}else if (pk2.windowid == 0) {
					player.getInventory().sendContents(player);
				}
			}else if (this.data1.containsKey(name)) {
				event.setCancelled();
				CreateChestInventory chest = this.data1.get(name);
				if (pk2.windowid == 0) {
					player.getInventory().setItem(pk2.slot, pk2.item);
				}else if (pk2.windowid == 65) {
					chest.setItem(pk2.slot, pk2.item);
				}
			}else if (this.data2.containsKey(name)) {
				event.setCancelled();
				if (pk2.windowid == 0) {
					player.getInventory().setItem(pk2.slot, pk2.item);
				}else if (pk2.windowid == 65) {
					player.getEnderChestInventory().setItem(pk2.slot, pk2.item);
				}
			}else if (data3.containsKey(name)) {
				event.setCancelled();
				CreateChestInventory chest = this.data3.get(name);
				chest.sendContents();
				player.getInventory().sendContents(player);
				if (pk2.windowid == 65) {
					switch (pk2.slot) {
						case 0:
							getServer().dispatchCommand(player, "home sub1 set");
						break;

						case 1:
							getServer().dispatchCommand(player, "home sub2 set");
						break;

						case 2:
							getServer().dispatchCommand(player, "home sub3 set");
						break;

						case 4:
							getServer().dispatchCommand(player, "wtp world");
						break;

						case 5:
							getServer().dispatchCommand(player, "wtp seichi");
						break;

						case 6:
							getServer().dispatchCommand(player, "wtp life");
						break;

						case 9:
							getServer().dispatchCommand(player, "home sub1 warp");
						break;

						case 10:
							getServer().dispatchCommand(player, "home sub2 warp");
						break;

						case 11:
							getServer().dispatchCommand(player, "home sub3 warp");
						break;

						case 13:
							getServer().dispatchCommand(player, "spawn");
						break;

						case 14:
							getServer().dispatchCommand(player, "wtp seichi2");
						break;

						case 15:
							getServer().dispatchCommand(player, "wtp life2");
						break;

						case 18:
							chest.InventoryClose();
							this.data3.remove(name);
							CreateChestInventory chest5 = this.api.getChestInventory(player, chest.x, chest.y, chest.z);
							getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
								@Override
								public void run() {
									chest5.sendChest();
									chest5.setChestName("§2メニュー1");
									chest5.InventoryOpen();
									Item[] items = getItems1(player);
									for (int slot = 0; slot < items.length; slot++) {
										chest5.setItem(slot, items[slot]);
									}
									chest5.sendContents();
									putData0(name, chest5);
									Item item = player.getInventory().getItemInHand();
									player.getInventory().setItemInHand(Item.get(0));
									setHand(name, item);
								}
							}, 8);
						break;
					}
				}else if (pk2.windowid == 0) {
					player.getInventory().sendContents(player);
				}
			}else if (this.data4.containsKey(name)) {
				event.setCancelled();
				CreateChestInventory chest = this.data4.get(name);
				chest.sendContents();
				player.getInventory().sendContents(player);
				if (pk2.windowid == 65) {
					switch (pk2.slot) {
						case 2:
							getServer().dispatchCommand(player, "skill 1");
						break;

						case 3:
							getServer().dispatchCommand(player, "skill 2");
						break;

						case 4:
							getServer().dispatchCommand(player, "skill 3");
						break;

						case 5:
							getServer().dispatchCommand(player, "skill 4");
						break;

						case 6:
							getServer().dispatchCommand(player, "skill 5");
						break;

						case 11:
							getServer().dispatchCommand(player, "skill 6");
						break;

						case 12:
							getServer().dispatchCommand(player, "skill 7");
						break;

						case 13:
							getServer().dispatchCommand(player, "skill 8");
						break;

						case 14:
							getServer().dispatchCommand(player, "skill 9");
						break;

						case 15:
							getServer().dispatchCommand(player, "skill 10");
						break;

						case 18:
							chest.InventoryClose();
							this.data4.remove(name);
							CreateChestInventory chest5 = this.api.getChestInventory(player, chest.x, chest.y, chest.z);
							getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
								@Override
								public void run() {
									chest5.sendChest();
									chest5.setChestName("§2メニュー1");
									chest5.InventoryOpen();
									Item[] items = getItems1(player);
									for (int slot = 0; slot < items.length; slot++) {
										chest5.setItem(slot, items[slot]);
									}
									chest5.sendContents();
									putData0(name, chest5);
									Item item = player.getInventory().getItemInHand();
									player.getInventory().setItemInHand(Item.get(0));
									setHand(name, item);
								}
							}, 8);
						break;
					}
				}else if (pk2.windowid == 0) {
					player.getInventory().sendContents(player);
				}
			}else if (this.data9.containsKey(name)) {
				event.setCancelled();
				CreateDoubleChestInventory chest = this.data9.get(name);
				if (pk2.windowid == 0) {
					player.getInventory().setItem(pk2.slot, pk2.item);
				}else if (pk2.windowid == 66) {
					chest.setItem(pk2.slot, pk2.item);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.namedTag.contains("DoubleChestInventory")) {
			ListTag<CompoundTag> nbt = new ListTag<CompoundTag>("DoubleChestInventory");
			for (int slot = 0; slot < 54; slot++) {
				Item item = Item.get(0);
				nbt.add(NBTIO.putItemHelper(item, slot));
			}
			player.namedTag.putList(nbt);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		if (data0.containsKey(name)) {
			data0.remove(name);
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		if (data0.containsKey(name) || data1.containsKey(name) || data2.containsKey(name) || hand.containsKey(name)) {
			event.setCancelled();
			player.getInventory().sendContents(player);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Item item = player.getInventory().getItemInHand();
		if (item.getId() == 280) {
			String name = player.getName();
			int x = (int) player.x;
			int y = (int) player.y + 5;
			int z = (int) player.z;
			CreateChestInventory chest = this.api.getChestInventory(player, x, y, z);
			chest.sendChest();
			chest.setChestName("§2メニュー1");
			chest.InventoryOpen();
			Item[] items = getItems1(player);
			for (int slot = 0; slot < items.length; slot++) {
				chest.setItem(slot, items[slot]);
			}
			chest.sendContents();
			this.data0.put(name, chest);
			event.setCancelled();
			player.getInventory().setItemInHand(Item.get(0));
			this.hand.put(name, item);
		}
	}

	public void putData0(String name, CreateChestInventory chest) {
		this.data0.put(name,  chest);
	}

	public void putData1(String name, CreateChestInventory chest) {
		this.data1.put(name,  chest);
	}

	public void putData2(String name, CreateChestInventory chest) {
		this.data2.put(name,  chest);
	}

	public void putData3(String name, CreateChestInventory chest) {
		this.data3.put(name,  chest);
	}

	public void putData4(String name, CreateChestInventory chest) {
		this.data4.put(name,  chest);
	}

	public void putData9(String name, CreateDoubleChestInventory chest) {
		this.data9.put(name,  chest);
	}

	public void setHand(String name, Item item) {
		this.hand.put(name, item);
	}

	public String getSeichiData(Player player) {
		String name = player.getName().toLowerCase();
		if (this.data5 != null) {
			int i = (int) this.data5.data.get(name);
			LevelSystem l;
			if (this.data5.limit.containsKey(name)) {
				l = new LevelSystem(i, this.data5.limit.get(name));
			}else{
				l = new LevelSystem(i);
			}
			String count = NumberFormat.getNumberInstance().format(i);
			String next = NumberFormat.getNumberInstance().format(l.getNextLevel());
			String txt = "§a貴方のステータス\n"
				+ "§bレベル" +String.valueOf(l.getLevel()) + "Lv\n"
				+ "§b整地量" + count +"ブロック\n"
				+ "§b必要な整地量" + next +"ブロック\n"
				+ "§b使えるスキルのレベル" + String.valueOf((int)l.getLevel() / 10) +"\n"
				+ "§b使ってるスキル" + data5.skilln.get(name);
			return txt;
		}else{
			return "";
		}
	}

	public String getSeichiData2(Player player) {
		String name = player.getName().toLowerCase();
		if (this.data5 != null) {
			String txt = "§a貴方のステータス\n"
					+ "§b現在のランキング" + String.valueOf(data5.getRanking(name)) + "位\n"
					+ "§b次のランキングまで" + NumberFormat.getNumberInstance().format(data5.getNextRankingCount(name)) + "ブロック";
			return txt;
		}else{
			return "";
		}
	}

	public String getTextData(Player player) {
		String name = player.getName().toLowerCase();
		String txt = "§a次のガチャ券までの整地量" + String.valueOf((int) data6.data.get(name)) + "ブロック\n"
			+ "§aガチャ券の総量" + NumberFormat.getNumberInstance().format(data7.getItem(name, 388, 0)) + "個";
		if (this.data8 != null) {
			txt = txt + "\n"
					+ "§a所持金" + NumberFormat.getNumberInstance().format(data8.getMoney(name)) + "円";
		}
		return txt;
	}

	public Item[] getItems1(Player player) {
		Item[] items = new Item[27];
		items[0] = Item.get(403, 0, 1).setCustomName(getSeichiData(player));
		items[1] = Item.get(403, 0, 1).setCustomName(getSeichiData2(player));
		items[2] = Item.get(403, 0, 1).setCustomName(getTextData(player));
		items[3] = Item.get(340, 0, 1).setCustomName("§eスキルをonにする");
		items[4] = Item.get(340, 0, 1).setCustomName("§eスキルをoffにする");
		items[5] = Item.get(278, 0, 1).setNamedTag(new CompoundTag().putList(new ListTag<>("ench"))).setCustomName("§aスキルの設定");
		items[6] = Item.get(0);
		items[7] = Item.get(0);
		items[8] = Item.get(325, 0, 1).setCustomName("§cゴミ箱\n§4注意 入れたアイテムは戻ってきません");
		items[9] = Item.get(288, 0, 1).setCustomName("§bfly機能をonにする");
		items[10] = Item.get(289, 0, 1).setCustomName("§bfly機能をoffにする");
		items[11] = Item.get(0);
		items[12] = Item.get(265, 0, 1).setCustomName("§6ガチャを引いた時の当たりと大当たりを自動でガチャ券に交換する");
		items[13] = Item.get(266, 0, 1).setCustomName("§6ガチャを引いた時の当たりと大当たりを自動でガチャ券に交換しなくする");
		items[14] = Item.get(0);
		items[15] = Item.get(0);
		items[16] = Item.get(54, 0, 1).setCustomName("§6ストレージを開く");
		items[17] = Item.get(130, 0, 1).setCustomName("§6エンダーチェストを開く");
		items[18] = Item.get(355, 6, 1).setCustomName("§dメインのホームポイントをセットする");
		items[19] = Item.get(368, 0, 1).setCustomName("§3メインのホームポイントにワープする");
		items[20] = Item.get(0);
		items[21] = Item.get(388, 0, 1).setCustomName("§aガチャ券を1個取り出す");
		items[22] = Item.get(388, 0, 10).setCustomName("§aガチャ券を10個取り出す");
		items[23] = Item.get(388, 0, 64).setCustomName("§aガチャ券を64個取り出す");
		items[24] = Item.get(0);
		items[25] = Item.get(0);
		items[26] = Item.get(369, 0, 1).setCustomName("§2次のページ");
		return items;
	}

	public Item[] getItems2() {
		Item[] items = new Item[27];
		items[0] = Item.get(355, 6, 1).setCustomName("§dサブ1のホームポイントをセットする");
		items[1] = Item.get(355, 6, 1).setCustomName("§dサブ2のホームポイントをセットする");
		items[2] = Item.get(355, 6, 1).setCustomName("§dサブ3のホームポイントをセットする");
		items[3] = Item.get(0);
		items[4] = Item.get(381, 0, 1).setCustomName("§bメインワールドにテレポートする");
		items[5] = Item.get(381, 0, 1).setCustomName("§b整地ワールドにテレポートする");
		items[6] = Item.get(381, 0, 1).setCustomName("§b生活ワールドにテレポートする");
		items[7] = Item.get(0);
		items[8] = Item.get(0);
		items[9] = Item.get(368, 0, 1).setCustomName("§3サブ1のホームポイントにワープする");
		items[10] = Item.get(368, 0, 1).setCustomName("§3サブ2のホームポイントにワープする");
		items[11] = Item.get(368, 0, 1).setCustomName("§3サブ3のホームポイントにワープする");
		items[12] = Item.get(0);
		items[13] = Item.get(385, 0, 1).setCustomName("§cワールドのリス地にワープする");
		items[14] = Item.get(381, 0, 1).setCustomName("§b第2整地ワールドにテレポートする");
		items[15] = Item.get(381, 0, 1).setCustomName("§b第2生活ワールドにテレポートする");
		items[16] = Item.get(0);
		items[17] = Item.get(0);
		items[18] = Item.get(369, 0, 1).setCustomName("§2前のページ");
		items[19] = Item.get(0);
		items[20] = Item.get(0);
		items[21] = Item.get(0);
		items[22] = Item.get(0);
		items[23] = Item.get(0);
		items[24] = Item.get(0);
		items[25] = Item.get(0);
		items[26] = Item.get(0);
		return items;
	}

	public Item[] getSkillItem() {
		Item[] items = new Item[27];
		items[0] = Item.get(403, 0, 1).setCustomName("§b下向きにスキルを発動させる\n§eスニークしながら自分が立っている\n§e位置より低い場所を破壊すると\n§e下向きにスキルを発動することができます");
		items[1] = Item.get(0);
		items[2] = Item.get(267, 0, 1).setCustomName("§aツヴァイ・ブレイク\n\n"
		+ "§b範囲 1×2×1\n"
		+ "§b必要Lv10");
		items[3] = Item.get(292, 0, 2).setCustomName("§aドライ・ブレイク\n\n"
		+ "§b範囲 3×2×1\n"
		+ "§b必要Lv20");
		items[4] = Item.get(258, 0, 3).setCustomName("§aエクスプロージョン\n\n"
		+ "§b範囲 3×3×3\n"
		+ "§b必要Lv30");
		items[5] = Item.get(256, 0, 4).setCustomName("§aハーフ設置スキル(仮)\n\n"
		+ "§b範囲 1×1×∞\n"
		+ "§b必要Lv40\n"
		+ "§c注意 岩盤の高さのブロックをタッチして発動します\n"
		+ "§c手にツールを持ってないと発動しません");
		items[6] = Item.get(257, 0, 5).setCustomName("§aドラゴン・フレア\n\n"
		+ "§b範囲 5×5×5\n"
		+ "§b必要Lv50");
		items[7] = Item.get(0);
		items[8] = Item.get(0);
		items[9] = Item.get(0);
		items[10] = Item.get(0);
		items[11] = Item.get(276, 0, 6).setCustomName("§a弓スキル(仮)\n\n"
		+ "§b範囲 5×5×5\n"
		+ "§b必要Lv60\n"
		+ "§c注意 弓を打って矢が当たったところに発動します");
		items[12] = Item.get(293, 0, 7).setCustomName("§aメガ・タイタン\n\n"
		+ "§b範囲 7×7×7\n"
		+ "§b必要Lv70\n"
		+ "§bクールタイム 0.5秒");
		items[13] = Item.get(279, 0, 8).setCustomName("§aハムート・フレア\n\n"
		+ "§b範囲 9×9×9\n"
		+ "§b必要Lv80\n"
		+ "§bクールタイム 1秒");
		items[14] = Item.get(277, 0, 9).setCustomName("§aスキル9(仮)\n\n"
		+ "§b範囲 11×11×11\n"
		+ "§b必要Lv90\n"
		+ "§bクールタイム 2秒");
		items[15] = Item.get(278, 0, 10).setCustomName("§a最終スキル(仮)\n\n"
		+ "§b範囲 13×13×13\n"
		+ "§b必要Lv100");
		items[16] = Item.get(0);
		items[17] = Item.get(0);
		items[18] = Item.get(369, 0, 1).setCustomName("§2戻る");
		items[19] = Item.get(0);
		items[20] = Item.get(0);
		items[21] = Item.get(0);
		items[22] = Item.get(0);
		items[23] = Item.get(0);
		items[24] = Item.get(0);
		items[25] = Item.get(0);
		items[26] = Item.get(0);
		return items;
	}
}
