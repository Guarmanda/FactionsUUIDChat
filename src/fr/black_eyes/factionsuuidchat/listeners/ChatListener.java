package fr.black_eyes.factionsuuidchat.listeners;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

import fr.black_eyes.factionsuuidchat.Config;
import fr.black_eyes.factionsuuidchat.Main;

public class ChatListener implements Listener {
	

	Config config = Main.getConfigFiles();

	 FileConfiguration lang = Main.getConfigFiles().getLang();
	
	
	@EventHandler
	public void format(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		HashMap<Player, ChatColor> colors = new HashMap<Player, ChatColor>();
		if(e.getPlayer().hasPermission("chat.color")) e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
		FPlayer fplayer = FPlayers.getInstance().getByPlayer(e.getPlayer());
		String format = config.getConfig().getString("chatFormat");

		String prefix = Main.getChat().getPlayerPrefix(e.getPlayer());
		String faction = fplayer.getFaction().getTag();
		String role = fplayer.getRole().getPrefix();
		if(fplayer.getFactionId().equals("0")) {
			faction = config.getConfig().getString("noFaction");
			role = "";
		}
		format = ChatColor.translateAlternateColorCodes('&', format);
		format = format.replace("[faction_role]", role).replace("[faction_name]", faction).replace("[vault_prefix]", prefix).replace("[player]", e.getPlayer().getName()).replace("[message]", e.getMessage());
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			format = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(e.getPlayer(), format);
		}
		for(Player p : Bukkit.getOnlinePlayers()) {
			FPlayer fp = FPlayers.getInstance().getByPlayer(p);
			colors.put(p, fp.getRelationTo(fplayer).getColor());
			p.sendMessage(format.replace("[faction_rel_color]", ""+fp.getRelationTo(fplayer).getColor()));
		}
		
	}
	
	
	
	
	
}
