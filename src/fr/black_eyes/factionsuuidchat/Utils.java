package fr.black_eyes.factionsuuidchat;

import org.bukkit.command.CommandSender;

public class Utils  {
	Main instance = Main.getInstance();
	Config config = Main.getConfigFiles();

	
	//message functions that automatically get a message from config.getLang()uage file
	public void msg(CommandSender p, String path, String replacer, String replacement) {
		p.sendMessage(getMsg(path, replacer, replacement));
	}
	
	public String getMsg(String path, String replacer, String replacement) {
		return config.getLang().getString(path).replace(replacer, replacement).replace("&", "§");
	}
	
	
	
	

	

	
	
}
