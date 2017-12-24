package tedo.SignCommand;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;

public class SignCommand extends PluginBase implements Listener{

	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Block block = event.getBlock();
		if (block.getId() == 63 || block.getId() == 68) {
			Player player = event.getPlayer();
			if (player.getDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_NO_AI)) {
				return;
			}
			double x = block.x;
			double y = block.y;
			double z = block.z;
			Level level = block.getLevel();
			BlockEntity sign = level.getBlockEntity(new Vector3(x, y, z));
			if (sign instanceof BlockEntitySign) {
				String[] text = ((BlockEntitySign) sign).getText();
				if (text[0].startsWith("##")) {
					String cm = text[0].substring(2) + text[1] + text[2] + text[3];
					PlayerCommandPreprocessEvent ev = new PlayerCommandPreprocessEvent(player, cm);
					getServer().getPluginManager().callEvent(ev);
					if (!ev.isCancelled()) {
						getServer().dispatchCommand(player, cm);
						event.setCancelled();
					}
				}
			}
		}
	}
}
