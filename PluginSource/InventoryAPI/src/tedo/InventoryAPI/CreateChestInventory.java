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

public class CreateChestInventory {

	public HashMap<Integer, Item> items = new HashMap<Integer, Item>();
	public boolean open = false;

	public byte windowid = 65;

	public Player player = null;
	public int x = 0;
	public int y = 0;
	public int z = 0;

	public CreateChestInventory(Player player, int x, int y, int z) {
		this.player = player;
		this.x = x;
		this.y = y;
		this.z = z;
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
	}

	public void sendBeforeBlock() {
		Level level = this.player.getLevel();
		Block block = level.getBlock(new Vector3(this.x, this.y, this.z));
		UpdateBlockPacket pk = new UpdateBlockPacket();
		pk.x = this.x;
		pk.y = this.y;
		pk.z = this.z;
		pk.blockId = block.getId();
		pk.blockData = block.getDamage();
		pk.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
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
		CompoundTag nbt = new CompoundTag()
				.putString("id", BlockEntity.CHEST)
				.putInt("x", this.x)
				.putInt("y", this.y)
				.putInt("z", this.z)
				.putString("CustomName", name);

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
	}

	public HashMap<Integer, Item> getContents() {
		return this.items;
	}

	public void setContents(HashMap<Integer, Item> items) {
		this.items = items;
	}

	public void sendContents() {
		Item[] items = new Item[27];
		for (int i = 0; i < 27; i++) {
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
