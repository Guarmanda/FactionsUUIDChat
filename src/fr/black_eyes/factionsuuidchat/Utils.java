package fr.black_eyes.factionsuuidchat;

import java.util.ArrayList;
import java.util.List;

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
	
	

	public String colorizeMsg(String message, String color) {
		List<String> words = new ArrayList<String>();
		for(String word : message.split(" ")) {
			words.add(color + word);
		}
		return words.toString().replace(", ", " ").replace("[", "").replace("]", "");
	}


	
	
}
