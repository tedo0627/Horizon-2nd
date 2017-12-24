package tedo.BanSystem;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class BanSystem extends PluginBase implements Listener{

	private Config config;
	private Config player;
	private HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, HashMap<String, String>> name = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, String> ip = new HashMap<String, String>();
	private HashMap<String, String> cid = new HashMap<String, String>();
	private HashMap<String, String> host = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getDataFolder().mkdirs();
		this.player = new Config(new File(this.getDataFolder(), "data.yml"),Config.YAML);
		this.config = new Config(new File(this.getDataFolder(), "ban.yml"),Config.YAML);
		this.player.getAll().forEach((name, data) -> this.data.put(name, (HashMap<String, String>) data));
		this.config.getAll().forEach((name, data) -> this.name.put(name, (HashMap<String, String>) data));
		this.name.forEach((name, data) -> this.ip.put(data.get("ip"), data.get("ip")));
		this.name.forEach((name, data) -> this.cid.put(data.get("cid"), data.get("cid")));
		this.name.forEach((name, data) -> this.host.put(data.get("host"), data.get("host")));
	}

	public void onDisable() {
		this.name.forEach((name, data) -> this.config.set(name, data));
		this.config.save();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		String ip = player.getAddress();
		String cid = String.valueOf(player.getClientId());
		String host;
		try {
			InetAddress ia = InetAddress.getByName(ip);
			host = ia.getHostName();
		} catch (UnknownHostException e) {
			host = ip;
		}
		if (this.name.containsKey(name)) {
			event.setKickMessage("§c貴方はvbanされています");
			event.setCancelled();
		}
		if (this.ip.containsKey(ip)) {
			event.setKickMessage("§c貴方はvbanされています");
			event.setCancelled();
		}
		if (this.cid.containsKey(cid)) {
			event.setKickMessage("§c貴方はvbanされています");
			event.setCancelled();
		}
		if (this.host.containsKey(host)) {
			event.setKickMessage("§c貴方はvbanされています");
			event.setCancelled();
		}
		if (!(event.isCancelled())) {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("ip", ip);
			data.put("cid", cid);
			data.put("host", host);
			this.data.put(name, data);
			this.player.set(name, data);
			this.player.save();
		}
	}

	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		switch(command.getName()){
			case "vban":
				try{
					if(args[0] != null){
						switch (args[0]) {
							case "add":
								try {
									if (args[1] != null) {
										if (this.data.containsKey(args[1])) {
											if (!(isVBan(args[1]))) {
												addVBan(args[1]);
												Player player = getServer().getPlayer(args[1]);
												if (player != null) {
													player.kick("§c貴方はvbanされました", false);
												}
												sender.sendMessage("§a>>§b" + args[1] + "をvbanしました");
											}else{
												sender.sendMessage("§a>>§b" + args[1] +"は既にvbanされています");
											}
										}else{
											sender.sendMessage("§a>>§bそのプレイヤーは存在しません");
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/vban add [名前]");
								}
							break;

							case "del":
								try {
									if (args[1] != null) {
										if (this.data.containsKey(args[1])) {
											if (isVBan(args[1])) {
												delVBan(args[1]);
												sender.sendMessage("§a>>§b" + args[1] + "のvbanを解除しました");
											}else{
												sender.sendMessage("§a>>§b" + args[1] + "はvbanされていません");
											}
										}else{
											sender.sendMessage("§a>>§bそのプレイヤーは存在しません");
										}
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									sender.sendMessage("§a>>§b/vban del [名前]");
								}
							break;

							case "list":
								sender.sendMessage("§a>>§bvbanリスト");
								this.name.forEach((name, data) -> sender.sendMessage("§a>>§b" + name + " | ip : " + data.get("ip") + " | cid : " + data.get("cid") + " | host : " + data.get("host")));
							break;

							default:
								sender.sendMessage("§a>>§b/vban add [名前] | 指定したプレイヤーをvbanします");
								sender.sendMessage("§a>>§b/vban del [名前] | 指定したプレイヤーのvbanを解除します");
								sender.sendMessage("§a>>§b/vban list       | vbanされているプレイヤーのリストを表示します");
							break;
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/vban add [名前] | 指定したプレイヤーをvbanします");
					sender.sendMessage("§a>>§b/vban del [名前] | 指定したプレイヤーのvbanを解除します");
					sender.sendMessage("§a>>§b/vban list       | vbanされているプレイヤーのリストを表示します");
				}
			break;
		}
		return false;
	}

	public boolean isVBan(String name) {
		name = name.toLowerCase();
		if (this.name.containsKey(name)) {
			return true;
		}else{
			return false;
		}
	}

	public void addVBan(String name) {
		name = name.toLowerCase();
		HashMap<String, String> data = this.data.get(name);
		this.name.put(name, data);
		this.ip.put(data.get("ip"), data.get("ip"));
		this.cid.put(data.get("cid"), data.get("cid"));
		this.host.put(data.get("host"), data.get("host"));
		this.config.set(name, data);
		this.config.save();
	}

	public void delVBan(String name) {
		name = name.toLowerCase();
		HashMap<String, String> data = this.data.get(name);
		this.name.remove(name, data);
		this.ip.remove( (String) data.get("ip"));
		this.cid.remove( (String) data.get("cid"));
		this.host.remove( (String) data.get("host"));
		this.config.remove(name);
		this.config.save();
	}
}
