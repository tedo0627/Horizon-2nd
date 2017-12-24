package tedo.EconomySystemNameTag;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import tedo.EconomySystemAPI.EconomySystemAPI;

public class EconomySystemNameTag extends PluginBase implements Listener{

	public Config config, data, player;
	public long money;
	public EconomySystemAPI economy;
	public HashMap<String, String> tag = new HashMap<String, String>();
	public HashMap<String, String> pos = new HashMap<String, String>();
	public HashMap<String, HashMap<String, String>> sign = new HashMap<String, HashMap<String, String>>();

	@SuppressWarnings({ "deprecation", "unchecked" })
	public void onEnable(){
		this.economy = (EconomySystemAPI) getServer().getPluginManager().getPlugin("EconomySystemAPI");
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getDataFolder().mkdirs();
		this.config = new Config(new File(this.getDataFolder(), "config.yml"),Config.YAML,
		new LinkedHashMap<String, Object>() {
			{
				put("自由称号の値段", 100000);
			}
		}
		);
		this.data = new Config(new File(this.getDataFolder(), "signdata.yml"),Config.YAML);
		this.player = new Config(new File(this.getDataFolder(), "playerdata.yml"),Config.YAML);
		int m = (int)this.config.get("自由称号の値段");
		this.money = (long) m;
		this.data.getAll().forEach((pos, data) -> this.sign.put(pos, (HashMap<String, String>) data));
		this.player.getAll().forEach((name, tag) -> this.tag.put(name, (String) tag));
	}

	public void onDisable() {
		this.sign.forEach((pos, data) -> this.data.set(pos, data));
		this.data.save();
		this.tag.forEach((name, tag) -> this.player.set(name, tag));
		this.player.save();
	}

	@EventHandler
	public void onPlayerJoib(PlayerJoinEvent event) {
		Player player= event.getPlayer();
		String name = player.getName();
		if (this.tag.containsKey(name.toLowerCase())) {
			String tag = this.tag.get(name.toLowerCase());
			player.setDisplayName("§b[§f" + tag + "§r§b]§f" + name);
			player.setNameTag("§b[§f" + tag + "§r§b]§f" + name);
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		String x = String.valueOf((int) block.x);
		String y = String.valueOf((int) block.y);
		String z = String.valueOf((int) block.z);
		String level = player.getLevel().getName();
		String[] text = event.getLines();
		if (text[0].equals("tag")) {
			if (player.isOp()) {
				if (isInt(text[1]) && !(text[2].equals(""))) {
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("値段", String.valueOf(text[1]));
					data.put("称号", text[2]);
					data.put("level", level);
					String pos = x + ":" + y + ":" + z + ":" + level;
					this.sign.put(pos, data);
					player.sendMessage("§a>>§b称号看板を立てました");
					event.setLine(0, "§b[§a称号SHOP§b]");
					event.setLine(1, "§e値段 : " + text[1] + "円");
					event.setLine(2, "§e称号 : §f" + text[2]);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Block block = event.getBlock();
		if (block.getId() == 63 || block.getId() == 68) {
			String x = String.valueOf((int) block.x);
			String y = String.valueOf((int) block.y);
			String z = String.valueOf((int) block.z);
			String level = block.getLevel().getName();
			String pos = x + ":" + y + ":" + z + ":" + level;
			if (this.sign.containsKey(pos)) {
				HashMap data = this.sign.get(pos);
				Player player = event.getPlayer();
				String name = player.getName().toLowerCase();
				if (this.pos.containsKey(name) && this.pos.get(name).equals(pos)) {
					this.pos.remove(name);
					long money = Long.parseLong((String) data.get("値段"));
					if (this.economy.getMoney(name) >= money) {
						this.economy.delMoney(name, money);
						String tag = (String) data.get("称号");
						if (this.tag.containsKey(name)) {
							String before = "§b[§f" + this.tag.get(name) + "§r§b]§f";
							String after = "§b[§f" + tag + "§r§b]§f";
							player.setDisplayName(player.getDisplayName().replace(before, after));
							player.setNameTag(player.getNameTag().replace(before, after));
						}else{
							String before = "§b]§f";
							String after = "§b]§f§b[§f" + tag + "§r§b]§f";
							player.setDisplayName(player.getDisplayName().replace(before, after));
							player.setNameTag(player.getNameTag().replace(before, after));
						}
						this.tag.put(name, tag);
						player.sendMessage("§a>>§b称号を購入しました");
					}else{
						player.sendMessage("§a>>§bお金が足りません");
					}
				}else{
					this.pos.put(name, pos);
					player.sendMessage("§a>>§b購入する場合はもう一度タップしてください");
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getId() == 63 || block.getId() == 68) {
			String x = String.valueOf((int) block.x);
			String y = String.valueOf((int) block.y);
			String z = String.valueOf((int) block.z);
			String level = block.getLevel().getName();
			String pos = x + ":" + y + ":" + z + ":" + level;
			if (this.sign.containsKey(pos)) {
				Player player = event.getPlayer();
				if (player.isOp()) {
					this.sign.remove(pos);
					if (this.data.exists(pos)) {
						this.data.remove(pos);
						this.data.save();
					}
					player.sendMessage("§a>>§b称号看板を破壊しました");
				}else{
					event.setCancelled();
					player.sendPopup("§b貴方はこの看板を破壊することができません");
				}
			}
		}
	}

	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		switch(command.getName()){
			case "tag":
				try{
					if(args[0] != null){
						String name = sender.getName().toLowerCase();
						if (args[0].length() < 16) {
							if (this.economy.getMoney(name) >= this.money) {
								this.economy.delMoney(name, this.money);
								Player player = (Player) sender;
								String tag = args[0];
								if (this.tag.containsKey(name)) {
									String before = "§b[§f" + this.tag.get(name) + "§r§b]§f";
									String after = "§b[§f" + tag + "§r§b]§f";
									player.setDisplayName(player.getDisplayName().replace(before, after));
									player.setNameTag(player.getNameTag().replace(before, after));
								}else{
									String before = "§b]§f";
									String after = "§b]§f§b[§f" + tag + "§r§b]§f";
									player.setDisplayName(player.getDisplayName().replace(before, after));
									player.setNameTag(player.getNameTag().replace(before, after));
								}
								this.tag.put(name, tag);
								sender.sendMessage("§a>>§b称号を購入しました");
							}else{
								sender.sendMessage("§a>>§bお金が足りません");
							}
						}else{
							sender.sendMessage("§a>>§b文字数は15文字以下にしてください");
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/tag [称号]");
				}
			break;
		}
		return false;
	}

	private boolean isInt(String number) {
		try {
			Long.parseLong(number);
			return true;
		}
		catch(NumberFormatException ex){
			return false;
		}
	}
}
