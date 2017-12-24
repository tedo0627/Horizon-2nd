package tedo.ElevatorPlugin;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.plugin.PluginBase;

public class ElevatorPlugin extends PluginBase implements Listener{

	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onDataPacketReceive(DataPacketReceiveEvent event) {
		DataPacket pk = event.getPacket();
		if (pk instanceof PlayerActionPacket) {
			if (((PlayerActionPacket) pk).action == PlayerActionPacket.ACTION_JUMP) {
				Player player = event.getPlayer();
				Level level = player.getLevel();
				double x = player.x;
				int y = (int) player.y;
				double z = player.z;
				if (level.getBlock(new Vector3(x, y, z)).getId() == 148) {
					if (level.getBlock(new Vector3(x, y - 1, z)).getId() == 42) {
						for (int i = y + 2; i < 256; i++) {
							if (level.getBlock(new Vector3(x, i, z)).getId() == 42) {
								if (level.getBlock(new Vector3(x, i + 1, z)).getId() == 148) {
									player.teleport(new Vector3(x, i + 1, z));
									break;
								}
							}
						}
					}
				}
			}else if (((PlayerActionPacket) pk).action == PlayerActionPacket.ACTION_START_SNEAK) {
				Player player = event.getPlayer();
				Level level = player.getLevel();
				double x = player.x;
				int y = (int) player.y;
				double z = player.z;
				if (level.getBlock(new Vector3(x, y, z)).getId() == 148) {
					if (level.getBlock(new Vector3(x, y - 1, z)).getId() == 42) {
						for (int i = y - 2; i > 1; i--) {
							if (level.getBlock(new Vector3(x, i, z)).getId() == 148) {
								if (level.getBlock(new Vector3(x, i - 1, z)).getId() == 42) {
									player.teleport(new Vector3(x, i , z));
									player.setSneaking(false);
									break;
								}
							}
						}
					}
				}
			}
		}
	}
}
