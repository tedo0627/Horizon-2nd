package tedo.WorldEdit;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import tedo.WorldEdit.command.CopyCommand;
import tedo.WorldEdit.command.CutCommand;
import tedo.WorldEdit.command.HcylCommand;
import tedo.WorldEdit.command.PasteCommand;
import tedo.WorldEdit.command.RandomCommand;
import tedo.WorldEdit.command.ReplaceCommand;
import tedo.WorldEdit.command.SetCommand;
import tedo.WorldEdit.command.UndoCommand;

public class MainCommand extends Command{

	public WorldEdit main;

	public MainCommand(WorldEdit main) {
		super("/", "WorldEdit command", "// ...");

		this.main = main;

//		this.commandParameters.clear();
//		this.commandParameters.put("default", new CommandParameter[]{});
//
//		this.commandParameters.put("set1", new CommandParameter[]{
//				new CommandParameter("set", new String[]{"set"}),
//				new CommandParameter("ID", CommandParameter.ENUM_TYPE_ITEM_LIST, false)
//		});
//		this.commandParameters.put("set2", new CommandParameter[]{
//				new CommandParameter("set", new String[]{"set"}),
//				new CommandParameter("ID", CommandParameter.ARG_TYPE_INT, false)
//		});
//		this.commandParameters.put("set3", new CommandParameter[]{
//				new CommandParameter("set", new String[]{"set"}),
//				new CommandParameter("ID", CommandParameter.ARG_TYPE_STRING, false)
//		});
//
//		this.commandParameters.put("cut", new CommandParameter[]{
//				new CommandParameter("cut", new String[]{"cut"}),
//		});
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§a>>§bコンソールから実行できません");
			return false;
		}
		if (args.length < 2) {
			sender.sendMessage("§a>>§b// set [ID]");
			sender.sendMessage("§a>>§b// cut");
			sender.sendMessage("§a>>§b// undo");
			return false;
		}
		Player player = (Player) sender;
		switch (args[1]) {
			case "set":
				SetCommand.execution(player, args, this.main);
			break;

			case "cut":
				CutCommand.execution(player, this.main);
			break;

			case "random":
				RandomCommand.execution(player, args, this.main);
			break;

			case "undo":
				UndoCommand.execution(player, this.main);
			break;

			case "copy":
				CopyCommand.execution(player, this.main);
			break;

			case "paste":
				PasteCommand.execution(player, this.main);
			break;

			case "replace":
				ReplaceCommand.execution(player, args, this.main);
			break;

			case "hcyl":
				HcylCommand.execution(player, args, this.main);
			break;

			default:
			break;
		}
		return false;
	}

}
