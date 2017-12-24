package tedo.EconomySystemCasino;

import java.util.Random;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import tedo.EconomySystemAPI.EconomySystemAPI;

public class EconomySystemCasino extends PluginBase implements Listener{

	public EconomySystemAPI economy;

	public void onEnable(){
		this.economy = (EconomySystemAPI) getServer().getPluginManager().getPlugin("EconomySystemAPI");
	}

	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		switch(label){
			case "casino":
				Player player = (Player) sender;
				String name = player.getName().toLowerCase();
				long money = economy.getMoney(name);
				try{
					if (args[0] != null) {
						if (isInt(args[0])) {
							long amount = Long.parseLong(args[0]);
							if (amount > 0) {
								if (amount <= money) {
									if (new Random().nextInt(2) == 1) {
										economy.addMoney(name, amount);
										sender.sendMessage("§a>>§b" + String.valueOf(amount) + "円勝ち、所持金が" + String.valueOf(economy.getMoney(name)) + "円になりました");
									}else{
										economy.delMoney(name, amount);
										sender.sendMessage("§a>>§b" + String.valueOf(amount) + "円負け、所持金が" + String.valueOf(economy.getMoney(name)) + "円になりました");
									}
								}else{
									sender.sendMessage("§a>>§bお金が足りません");
								}
							}else{
								sender.sendMessage("§a>>§b0円以上かけてください");
							}
						}else{
							sender.sendMessage("§a>>§b/casino [金額]   | 指定した金額でカジノを行います");
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage("§a>>§b/casino [金額]   | 指定した金額でカジノを行います");
				}
			break;
		}
		return true;
	}

	private boolean isInt(String number) {
		try {
			Integer.parseInt(number);
			return true;
		}
		catch(NumberFormatException ex){
			return false;
		}
	}
}
