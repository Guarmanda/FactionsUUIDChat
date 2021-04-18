package fr.black_eyes.factionsuuidchat.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.black_eyes.factionsuuidchat.Config;
import fr.black_eyes.factionsuuidchat.Main;
import fr.black_eyes.factionsuuidchat.Utils;



public class ChatCommand extends Utils implements CommandExecutor, TabCompleter  {


	Config config = Main.getConfigFiles();

	 FileConfiguration lang = Main.getConfigFiles().getLang();
	 
	 
 
	 
	 
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

			
			if (args.length > 0 && !hasPerm(sender, args[0])) {
				return false;
			}

			if(args.length ==2) {
				
					displayhelp(sender);
					
				
			}
			else if(args.length == 1) {
				
				if(args[0].equalsIgnoreCase("reload")) {
					
					config.reloadConfig();
					Main.noFaction = config.getConfig().getString("noFaction");
					Main.logmessages = config.getConfig().getBoolean("logMessages");
					Main.isFactionOn = (Bukkit.getPluginManager().getPlugin("Factions") != null);
					msg(sender, "reloaded", " ", " ");
				}
				
				else {
					displayhelp(sender);
				}
			
				
				
			}
			else if (args.length ==2) {
				
				if(args[0] == "channel") {
					if(config.getConfig().getConfigurationSection("channels").getKeys(false).contains(args[1].toLowerCase())) {
						Main.channels.put((Player)sender, args[1]);
						msg(sender, "channelChanged", "[Channel]", args[1]);
					}
				}else {
					msg(sender, "channelDoesntExist", "[Channel]", args[1]);
				}
			}
			else if (args.length == 3) {
				if(args[0].equalsIgnoreCase("channel")) {
					if ( !hasPerm(sender, args[1])) {
						return false;
					}
					if(args[1].equalsIgnoreCase("create")) {
						if(config.getConfig().getConfigurationSection("channels").getKeys(false).contains(args[2].toLowerCase())) {
							msg(sender, "channelAlreadyExist",  "[Channel]", args[2]);
						}else {
							msg(sender, "channelCreated",  "[Channel]", args[2]);
							config.setConfig("channels."+args[2]+ ".format", "[" + args[2] + "] " +config.getConfig().getString("chatFormat"));
							config.setConfig("channels."+args[2]+ ".bungee", config.getConfig().getBoolean("bungee"));
							config.setConfig("channels."+args[2]+ ".useHoverInfo", config.getConfig().getBoolean("useHoverInfo"));
							config.setConfig("channels."+args[2]+ ".hoverInfo", config.getConfig().getString("hoverInfo"));
							config.saveConfig();
						}
					}
				}
			}
			else {
				displayhelp(sender);
			}
		
		return false;
	}
	
	public void displayhelp(CommandSender p) {
		List<String> help = config.getLang().getStringList("help");
		for(int i=0; i<help.size();i++) {
			p.sendMessage(help.get(i).replace("&", "§"));
		}
	}

	boolean hasPerm(CommandSender sender, String permission) {
		if (!sender.hasPermission("chat." + permission) && !sender.hasPermission("chat.admin") && !sender.hasPermission("chat.*")) {
			msg(sender, "noPermission", "[Permission]", "chat." + permission);
			return false;
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		final String[] completions0 = { "reload"};

		/*completion improved by alessevan*/
		if(args.length == 1){
		    final List<String> completions = new ArrayList<>();
		    for(final String string : completions0){
		        if(string.toLowerCase().startsWith(args[0].toLowerCase())) completions.add(string);
		    }
		    return completions;
		}
		return null;
	}
	

}
