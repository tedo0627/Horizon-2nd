package tedo.InventoryAPI;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.ContainerSetContentPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;

public class CreateDoubleChestInventory {

	public HashMap<Integer, Item> items = new HashMap<Integer, Item>();
	public boolean open = false;

	public byte windowid = 66;

	public Player player = null;
	public int x = 0;
	public int y = 0;
	public int z = 0;

	public CreateDoubleChestInventory(Player player, int x, int y, int z) {
		this.player = player;
		this.x = x;
		this.y = y;
		this.z = z;
		for (int i = 0; i < 54; i++) {
			setItem(i, Item.get(0));
		}
	}

	public void sendChest() {
		UpdateBlockPacket pk = new UpdateBlockPacket();
		pk.x = this.x;
		pk.y = this.y;
		pk.z = this.z;
		pk.blockId = 54;
		pk.blockData = 0;
		pk.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
		this.player.dataPacket(pk);

		pk.x++;
		this.player.dataPacket(pk);

		CompoundTag nbt;
		nbt = new CompoundTag()
				.putString("id", BlockEntity.CHEST)
				.putInt("x", x)
				.putInt("y", y)
				.putInt("z", z)
				.putInt("pairx", x + 1)
				.putInt("pairz", z);

		BlockEntityDataPacket pk1 = new BlockEntityDataPacket();
		pk1.x = x;
		pk1.y = y;
		pk1.z = z;
		try {
			pk1.namedTag = NBTIO.write(nbt, ByteOrder.LITTLE_ENDIAN, true);
		} catch (IOException e) {
            throw new RuntimeException(e);
		}
		this.player.dataPacket(pk1);

		nbt.putInt("pairx", x);
		pk1.x++;
		try {
			pk1.namedTag = NBTIO.write(nbt, ByteOrder.LITTLE_ENDIAN, true);
		} catch (IOException e) {
            throw new RuntimeException(e);
		}
		this.player.dataPacket(pk1);
	}

	public void sendBeforeBlock() {
		Level level = this.player.getLevel();
		Block block1 = level.getBlock(new Vector3(this.x, this.y, this.z));
		Block block2 = level.getBlock(new Vector3(this.x + 1, this.y, this.z));

		UpdateBlockPacket pk = new UpdateBlockPacket();
		pk.x = this.x;
		pk.y = this.y;
		pk.z = this.z;
		pk.blockId = block1.getId();
		pk.blockData = block1.getDamage();
		pk.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
		this.player.dataPacket(pk);

		pk.x++;
		pk.blockId = block2.getId();
		pk.blockData = block2.getDamage();
		this.player.dataPacket(pk);
	}

	public void InventoryOpen() {
		ContainerOpenPacket pk = new ContainerOpenPacket();
		pk.windowid = this.windowid;
		pk.type = 0;
		pk.x = this.x;
		pk.y = this.y;
		pk.z = this.z;
		this.player.dataPacket(pk);
	}

	public void InventoryClose() {
		ContainerClosePacket pk = new ContainerClosePacket();
		pk.windowid = this.windowid;
		this.player.dataPacket(pk);
		this.open = false;
	}

	public void setChestName(String name) {
		CompoundTag nbt;
		nbt = getNBT(this.x, this.y, this.z, name, true);

		BlockEntityDataPacket pk = new BlockEntityDataPacket();
		pk.x = this.x;
		pk.y = this.y;
		pk.z = this.z;
		try {
			pk.namedTag = NBTIO.write(nbt, ByteOrder.LITTLE_ENDIAN, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.player.dataPacket(pk);

		nbt = getNBT(this.x, this.y, this.z, name, false);
		pk.x++;
		try {
			pk.namedTag = NBTIO.write(nbt, ByteOrder.LITTLE_ENDIAN, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.player.dataPacket(pk);
	}

	public CompoundTag getNBT(int x, int y, int z, String chest, boolean pair) {
		CompoundTag nbt = new CompoundTag()
				.putString("id", BlockEntity.CHEST)
				.putInt("x", this.x)
				.putInt("y", this.y)
				.putInt("z", this.z)
				.putString("CustomName", chest)
				.putInt("pairz", z);
		if (pair) {
			nbt.putInt("pairx", x + 1);
		}else{
			nbt.putInt("pairx", x - 1);
		}
		return nbt;
	}

	public HashMap<Integer, Item> getContents() {
		return this.items;
	}

	public void setContents(HashMap<Integer, Item> items) {
		this.items = items;
	}

	public void sendContents() {
		Item[] items = new Item[54];
		for (int i = 0; i < 54; i++) {
			items[i] = getItem(i);
		}

		ContainerSetContentPacket pk = new ContainerSetContentPacket();
		pk.windowid = this.windowid;
		pk.eid = player.getId();
		pk.slots = items;
		this.player.dataPacket(pk);
	}

	public Item getItem(int slot) {
		return this.items.get(slot);
	}

	public void setItem(int slot, Item item) {
		this.items.put(slot, item);
	}
}
