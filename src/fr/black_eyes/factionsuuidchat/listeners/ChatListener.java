package fr.black_eyes.factionsuuidchat.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import fr.black_eyes.factionsuuidchat.Config;
import fr.black_eyes.factionsuuidchat.FactionsUtils;
import fr.black_eyes.factionsuuidchat.Main;
import fr.black_eyes.factionsuuidchat.Utils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatListener extends Utils implements Listener {
	

	Config config = Main.getConfigFiles();

	 FileConfiguration lang = Main.getConfigFiles().getLang();
	
	
	@EventHandler
	public void format(AsyncPlayerChatEvent e) {
		if(Main.isFactionOn ) {
			if(FactionsUtils.isFactionChat(e.getPlayer())) return;
		}
		e.setCancelled(true);
		if(e.getPlayer().hasPermission("chat.color")) e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
		String format = config.getConfig().getString("chatFormat");
		String prefix = Main.getChat().getPlayerPrefix(e.getPlayer());
		String hover = config.getConfig().getString("hoverInfo");
		if(Main.isFactionOn ) {
			hover = FactionsUtils.setFactionsTags(hover, e.getPlayer());
			format = FactionsUtils.setFactionsTags(format, e.getPlayer());
		}else {
			hover = hover.replace("[faction_role]", "").replace("[faction_name]", "");
			format = format.replace("[faction_role]", "").replace("[faction_name]", "");
		}
		hover = hover.replace("[vault_prefix]", prefix).replace("[player]", e.getPlayer().getName());
		format = format.replace("[vault_prefix]", prefix).replace("[player]", e.getPlayer().getName());
		format = ChatColor.translateAlternateColorCodes('&', format);
		String last = ChatColor.getLastColors(format);
		e.setMessage(colorizeMsg(e.getMessage(), last));
		format = format.replace("[message]", e.getMessage());
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			format = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(e.getPlayer(), format);
			hover =  me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(e.getPlayer(), hover);
		}

		for(Player pl : Bukkit.getOnlinePlayers()) {
			String format2 = format;
			String hover2 = hover;
			if(Main.isFactionOn ) {
				format2 = FactionsUtils.getMessage(format2, e.getPlayer(), pl);
				hover2 = FactionsUtils.getMessage(hover2, e.getPlayer(), pl);
			}else {
				format2 = format2.replace("[faction_rel_color]", "");
				hover2 = hover2.replace("[faction_rel_color]", "");
			}

			format2 = ChatColor.translateAlternateColorCodes('&', format2);
			hover2 =  ChatColor.translateAlternateColorCodes('&', hover2);
			
			TextComponent msg = new TextComponent(format2);
			if(config.getConfig().getBoolean("useHoverInfo")) {
				msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover2).create()));
			}
			pl.spigot().sendMessage(msg);
		}
		if(Main.logmessages)Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.stripColor(format));

	
		
		

		
	}
	
	
	
	
	
}
