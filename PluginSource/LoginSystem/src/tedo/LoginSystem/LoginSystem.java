package tedo.LoginSystem;

import java.io.File;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.ContainerSetSlotPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.DropItemPacket;
import cn.nukkit.network.protocol.RemoveBlockPacket;
import cn.nukkit.network.protocol.UseItemPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class LoginSystem extends PluginBase implements Listener{

	public Config config;
	public HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
	public HashMap<String, Boolean> register = new HashMap<String, Boolean>();
	public HashMap<String, Boolean> login = new HashMap<String, Boolean>();

	@SuppressWarnings("unchecked")
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getDataFolder().mkdirs();
		this.config = new Config(new File(this.getDataFolder(), "data.yml"),Config.YAML);
		this.config.getAll().forEach((name, data) -> this.data.put(name, (HashMap<String, String>) data));
	}

	public void onDisable() {
		this.data.forEach((name, data) -> this.config.set(name, data));
		this.config.save();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		if (this.data.containsKey(name)) {
			String ip = player.getAddress();
			String cid = String.valueOf(player.getClientId());
			HashMap<String, String> data = this.data.get(name);
			String i = (String)data.get("ip");
			String c = (String)data.get("cid");
			if (i.equals(ip)) {
				if (c.equals(cid)) {
					player.sendMessage("§a>>§b貴方はログインに成功しました");
				}else{
					player.sendMessage("§a>>§b貴方の端末の情報が変わっています");
					player.sendMessage("§a>>§b/login [パスワード] でログインしてください");
					player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_NO_AI, true);
					this.login.put(name, true);
				}
			}else{
				player.sendMessage("§a>>§b貴方の端末の情報が変わっています");
				player.sendMessage("§a>>§b/login [パスワード] でログインしてください");
				player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_NO_AI, true);
				this.login.put(name, true);
			}
		}else{
			player.sendMessage("§a>>§bこの鯖で遊ぶにはアカウントを登録する必要があります");
			player.sendMessage("§a>>§b/register [パスワード] [パスワード] で登録してください");
			player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_NO_AI, true);
			this.register.put(name, true);
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		if (this.register.containsKey(name)) {
			String[] m = event.getMessage().split(" ");
			if (!(m[0].equals("/register"))) {
				event.setCancelled();
				player.sendMessage("§a>>§b/register [パスワード] [パスワード] でアカウントを登録してください");
			}
		}else if (this.login.containsKey(name)) {
			String[] m = event.getMessage().split(" ");
			if (!(m[0].equals("/login"))) {
				event.setCancelled();
				player.sendMessage("§a>>§b/login [パスワード] でログインしてください");
			}
		}
	}

	@EventHandler
	public void DataPacketReceive(DataPacketReceiveEvent event) {
		DataPacket pk = event.getPacket();
		if (pk instanceof UseItemPacket || pk instanceof RemoveBlockPacket || pk instanceof DropItemPacket || pk instanceof ContainerSetSlotPacket) {
			Player player = event.getPlayer();
			String name = player.getName().toLowerCase();
			if (this.register.containsKey(name) || this.login.containsKey(name)) {
				event.setCancelled();
			}
		}
	}

	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();
		if (this.register.containsKey(name)) {
			event.setCancelled();
			player.sendMessage("§a>>§b/register [パスワード] [パスワード] でアカウントを登録してください");
		}else if (this.login.containsKey(name)) {
			event.setCancelled();
			player.sendMessage("§a>>§b/login [パスワード] でログインしてください");
		}
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		String name = sender.getName().toLowerCase();
		switch(command.getName()){
			case "register":
				if (this.register.containsKey(name)) {
					try{
						if(args[0] != null){
							try {
								if (args[1] != null) {
									if (args[0].equals(args[1])){
										if (!(args[0].toLowerCase().equals(sender.getName().toLowerCase()))) {
											Player player = (Player) sender;
											String ip = player.getAddress();
											String cid = String.valueOf(player.getClientId());
											HashMap<String, String> data = new HashMap<String, String>();
											data.put("ip", ip);
											data.put("cid", cid);
											data.put("pass", args[0]);
											this.data.put(name, data);
											player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_NO_AI, false);
											this.register.remove(name);
											sender.sendMessage("§a>>§b貴方のパスワードは" + args[0] + "で登録されました");
											sender.sendMessage("§a>>§bパスワードは忘れないようにしてください");
										}else{
											sender.sendMessage("§a>>§b安易なパスワードは使えません");
										}
									}else{
										sender.sendMessage("§a>>§b入力した二つのパスワードが違います");
									}
								}
							}
							catch(ArrayIndexOutOfBoundsException e){
								sender.sendMessage("§a>>§b/register [パスワード] [パスワード]");
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e){
						sender.sendMessage("§a>>§b/register [パスワード] [パスワード]");
					}
				}else{
					sender.sendMessage("§a>>§b貴方は既にアカウントを登録済みです");
				}
			break;

			case "login":
				if (this.login.containsKey(name)) {
					try {
						if (args[0] != null) {
							String p = args[0];
							String pass = (String) this.data.get(name).get("pass");
							if (p.equals(pass)){
								Player player = (Player) sender;
								String ip = player.getAddress();
								String cid = String.valueOf(player.getClientId());
								HashMap<String, String> data = new HashMap<String, String>();
								data.put("ip", ip);
								data.put("cid", cid);
								data.put("pass", args[0]);
								this.data.put(name, data);
								player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_NO_AI, false);
								this.login.remove(name);
								sender.sendMessage("§a>>§bログインが完了しました");
							}else{
								sender.sendMessage("§a>>§bパスワードが違います");
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e){
						sender.sendMessage("§a>>§b/login [パスワード]");
					}
				}else{
					sender.sendMessage("§a>>§b貴方は既にログイン済みです");
				}
			break;

			case "unregister":
				try {
					if (args[0] != null) {
						String n = args[0].toLowerCase();
						if (this.data.containsKey(n)) {
							this.data.remove(n);
							if (this.config.exists(n)) {
								this.config.remove(n);
								this.config.save();
							}
							sender.sendMessage("§a>>§b" + args[0] + "のログインデーターを削除しました");
						}else{
							sender.sendMessage("§a>>§b" + args[0] + "は存在しません");
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/unregister [名前]");
				}
			break;
		}
		return false;
	}
}
