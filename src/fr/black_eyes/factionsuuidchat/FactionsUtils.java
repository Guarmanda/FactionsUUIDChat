package fr.black_eyes.factionsuuidchat;

import org.bukkit.entity.Player;

import com.massivecraft.factions.struct.ChatMode;

public class FactionsUtils {
	
	
	public static String setFactionsTags(String format, Player p) {
		String faction;
		String role;
		com.massivecraft.factions.FPlayer fplayer = com.massivecraft.factions.FPlayers.getInstance().getByPlayer(p);
		faction =fplayer.getFaction().getTag();
		role = fplayer.getRole().getPrefix();
		if(fplayer.getFactionId().equals("0")) {
			faction = Main.noFaction;
			role = "";
		}
		return format.replace("[faction_role]", role).replace("[faction_name]", faction);
	}
	
	public static boolean isFactionChat(Player p) {
		com.massivecraft.factions.FPlayer fplayer = com.massivecraft.factions.FPlayers.getInstance().getByPlayer(p);
		return !fplayer.getChatMode().equals(ChatMode.PUBLIC);

	}
	
	
	public static String getMessage(String format, Player p, Player pl) {
			com.massivecraft.factions.FPlayer fplayer = com.massivecraft.factions.FPlayers.getInstance().getByPlayer(p);
			com.massivecraft.factions.FPlayer fp = com.massivecraft.factions.FPlayers.getInstance().getByPlayer(pl);
			return format.replace("[faction_rel_color]", fp.getRelationTo(fplayer).getColor().toString());
	}
	
	
	
	
}
