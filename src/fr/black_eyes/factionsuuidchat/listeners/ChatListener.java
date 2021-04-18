package fr.black_eyes.factionsuuidchat.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
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
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void format(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if(Main.isFactionOn ) {
			if(FactionsUtils.isFactionChat(p)) return;
		}
		if(e.isCancelled()) {
			return;
		}
		e.setCancelled(true);
		
		if(Main.isEssentials) {
			net.ess3.api.IEssentials ess = (net.ess3.api.IEssentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
			if(ess.getUser(p).isMuted()) {
				return;
			}
		}
		if(p.hasPermission("chat.color")) e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
		String format = config.getConfig().getString("chatFormat");
		String prefix = Main.getChat().getPlayerPrefix(p);
		String hover = config.getConfig().getString("hoverInfo");
		//set faction tags, are the same for everyone, no need to to this per-player
		if(Main.isFactionOn ) {
			hover = FactionsUtils.setFactionsTags(hover, p);
			format = FactionsUtils.setFactionsTags(format, p);
		}else {
			hover = hover.replace("[faction_role]", "").replace("[faction_name]", "");
			format = format.replace("[faction_role]", "").replace("[faction_name]", "");
		}
		hover = hover.replace("[vault_prefix]", prefix).replace("[player]", p.getName());
		format = format.replace("[vault_prefix]", prefix).replace("[player]", p.getName());
		format = ChatColor.translateAlternateColorCodes('&', format);
		
		
		//if no color in message, last color from format is used, can be &r also
		if(!e.getMessage().contains("&") && !e.getMessage().contains("§")) {		
			String last = ChatColor.getLastColors(format);
			e.setMessage(colorizeMsg(e.getMessage(), last));
		//else, if message contains color, we remove them if user haven't permission tu use them
		}else {
			Main.colors.entrySet().stream().forEach(col -> {if(e.getMessage().contains(col.getValue())) {
				if(!p.hasPermission("chat.color") && !p.hasPermission("chat.color.*") && !p.hasPermission("chat.color." + col.getKey()) && !p.hasPermission("essentials.chat."+col.getKey())) {
					e.setMessage(e.getMessage().replace(col.getValue(), ""));
					e.setMessage(e.getMessage().replace(col.getValue().replace("&", "§"), ""));
				}
			}});
		}
		format = format.replace("[message]", e.getMessage());
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			format = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(p, format);
			hover =  me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(p, hover);
		}
		
		ArrayList<String> item = new ArrayList<String>(Arrays.asList("[Item]", "[item]", "{item}", "{Item}", "[i]", "[I]"));
		//retrieving infos about item in hand
		ItemStack itemstack = p.getItemInHand();
		String amount = Integer.toString(itemstack.getAmount());
		List<String> loreList = (itemstack.getItemMeta() !=null)?itemstack.getItemMeta().getLore():null;
		String lore = "";
		if(loreList !=null) lore = ((loreList.toString().replace("[", "")).replace("]", "")).replace(", ", "\n") ;
		String type = itemstack.getType().name().toLowerCase();
		String name = (itemstack.getItemMeta() !=null)?itemstack.getItemMeta().getDisplayName():"";
		if(name ==null) name = "";
		String enchants = (itemstack.getItemMeta() != null)?itemstack.getItemMeta().getEnchants().toString():(itemstack.getEnchantments() !=null)?itemstack.getEnchantments().toString():"";
		enchants = ((((enchants.replace("{", "")).replace("}", "")).replace("]=", " ")).replaceFirst("Enchantment(.*), ", "")).replace(",", "\n");
		enchants = capitalize(enchants.replace("_", " "));
		
		Main.logInfo("amount: " + amount + " lore: "+lore+" type: "+type+" name: "+name+" enchants: " +enchants);
		
		//building text component with these infos
		String itemFormat = config.getConfig().getString("itemFormat");
		String itemHoverFormat = config.getConfig().getString("itemHoverFormat");
		itemFormat = ((((itemFormat.replace("{amount}", amount)).replace("{type}", type)).replace("{name}", name)).replace("{lore}", lore)).replace("{enchantments}", enchants);
		itemHoverFormat = ((((itemHoverFormat.replace("{amount}", amount)).replace("{type}", type)).replace("{name}", name)).replace("{lore}", lore.toString())).replace("{enchantments}", enchants);
		itemFormat = ChatColor.translateAlternateColorCodes('&', itemFormat);
		itemHoverFormat =  ChatColor.translateAlternateColorCodes('&', itemHoverFormat);
		TextComponent itemtxt = new TextComponent(itemFormat);
		itemtxt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(itemHoverFormat).create()));
		
		for(Player pl : Bukkit.getOnlinePlayers()) {
			String format2 = format;
			String hover2 = hover;
			//set faction relation colors for each player
			if(Main.isFactionOn ) {
				format2 = FactionsUtils.getMessage(format2, p, pl);
				hover2 = FactionsUtils.getMessage(hover2, p, pl);
			}else {
				format2 = format2.replace("[faction_rel_color]", "");
				hover2 = hover2.replace("[faction_rel_color]", "");
			}

			format2 = ChatColor.translateAlternateColorCodes('&', format2);
			hover2 =  ChatColor.translateAlternateColorCodes('&', hover2);
			
			ArrayList<TextComponent> textc = new ArrayList<TextComponent>();
			if(p.hasPermission("chat.item")) {
				//building array of text component with message and item info
				//for each item indicator
				for(String str : item) {
					//maybe a player wants to spam its items? Let's put a while just in case
					while(format2.contains(str)) {
						String substringToItem = format2.substring(0, format2.indexOf(str));
						format2 = replaceFirst2(format2,substringToItem, "");
						TextComponent TxtcSubstringToItem = new TextComponent(substringToItem);
						if(config.getConfig().getBoolean("useHoverInfo")) {
							TxtcSubstringToItem.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover2).create()));
						}
						
						textc.add(TxtcSubstringToItem);
						textc.add(itemtxt);
						format2 = replaceFirst2(format2, str, "");
	
					}
				}
			}
			
			//adding rest of msg to the TextComponent array
			TextComponent msg = new TextComponent(format2);
			if(config.getConfig().getBoolean("useHoverInfo")) {
				msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover2).create()));
			}
			textc.add(msg);
			TextComponent[] arraycmp = new TextComponent[textc.size()];
			for(int i=0; i< textc.size(); i++) {
				arraycmp[i] = textc.get(i);
			}
			pl.spigot().sendMessage(arraycmp);
		}
		if(Main.logmessages)Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.stripColor(format));

	
		
		

		
	}
	
	private static String replaceFirst2(String source, String target, String replacement) {
	    int index = source.indexOf(target);
	    if (index == -1) {
	        return source;
	    }

	    return source.substring(0, index)
	        .concat(replacement)
	        .concat(source.substring(index+target.length()));
	}
	
	

	private static String capitalize(String str){
	        
		 char[] charArray = str.toLowerCase().toCharArray();
		    boolean foundSpace = true;

		    for(int i = 0; i < charArray.length; i++) {

		      // if the array element is a letter
		      if(Character.isLetter(charArray[i])) {

		        // check space is present before the letter
		        if(foundSpace) {

		          // change the letter into uppercase
		          charArray[i] = Character.toUpperCase(charArray[i]);
		          foundSpace = false;
		        }
		      }

		      else {
		        // if the new character is not character
		        foundSpace = true;
		      }
		    }

		    // convert the char array to the string
		    return String.valueOf(charArray);
	    
	 }
	
	
}
