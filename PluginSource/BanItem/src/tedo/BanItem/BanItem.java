package tedo.BanItem;

import java.io.File;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class BanItem extends PluginBase implements Listener{

	private Config config;
	private HashMap<String, Boolean> data = new HashMap<String, Boolean>();

	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getDataFolder().mkdirs();
		this.config = new Config(new File(this.getDataFolder(), "data.yml"),Config.YAML);
		this.config.getAll().forEach((id, r) -> this.data.put(id, (Boolean) r));
	}

	public void onDisable() {
		this.data.forEach((id, r) -> this.config.set(String.valueOf(id), r));
		this.config.save();
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Item item = player.getInventory().getItemInHand();
		String id = String.valueOf(item.getId());
		if (this.data.containsKey(id)) {
			event.setCancelled();
			player.sendPopup("§bこのアイテムは使用することができません");
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Item item = player.getInventory().getItemInHand();
		String id = String.valueOf(item.getId());
		if (this.data.containsKey(id)) {
			event.setCancelled();
			player.sendPopup("§bこのアイテムは使用することができません");
		}
	}

	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		switch(command.getName()){
			case"banitem":
				try {
					if (args[0] != null) {
						switch (args[0]) {
							case"add":
								try {
									if (args[1] != null) {
										if (isInt(args[1])) {
											this.data.put(args[1], true);
											sender.sendMessage("§a>>§b使えないアイテムを追加しました");
										}else{
											sender.sendMessage("§a>>§b/banitem add [ID]");
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/banitem add [ID]");
								}
							break;

							case"del":
								try {
									if (args[1] != null) {
										if (isInt(args[1])) {
											this.data.remove(args[1]);
											if (this.config.exists(args[1])) {
												this.config.remove(args[1]);
												this.config.save();
											}
											sender.sendMessage("§a>>§b使えないアイテムから削除しました");
										}else{
											sender.sendMessage("§a>>§b/banitem del [ID]");
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/banitem del [ID]");
								}
							break;

							case"list":
								sender.sendMessage("§a>>§b使えないアイテムリスト");
								this.data.forEach((id, r) -> sender.sendMessage("§a>>§b" + Item.fromString(id).getName() + " : " + id));
							break;

							default:
								sender.sendMessage("§a>>§b/banitem add [ID] | 使えないアイテムを追加します");
								sender.sendMessage("§a>>§b/banitem del [ID] | 使えないアイテムを削除します");
								sender.sendMessage("§a>>§b/banitem list     | 使えないアイテムのリストを表示します");
							break;
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/banitem add [ID] | 使えないアイテムを追加します");
					sender.sendMessage("§a>>§b/banitem del [ID] | 使えないアイテムを削除します");
					sender.sendMessage("§a>>§b/banitem list     | 使えないアイテムのリストを表示します");
				}
			break;
		}
		return false;
	}

	private boolean isInt(String number) {
		try {
			Double.parseDouble(number);
			return true;
		}
		catch(NumberFormatException ex){
			return false;
		}
	}
}
