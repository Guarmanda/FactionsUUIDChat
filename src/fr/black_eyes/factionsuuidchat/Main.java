package fr.black_eyes.factionsuuidchat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import fr.black_eyes.factionsuuidchat.command.ChatCommand;
import fr.black_eyes.factionsuuidchat.listeners.ChatListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;







public class Main extends JavaPlugin {
	//public ArrayList<LootChest> lc = new ArrayList<LootChest>();

	private static Main instance;
	private static Config config;
	private static Permission perms = null;
	public static HashMap<Player, String> channels = new HashMap<Player, String>();
	public static Map<String, String> colors;
	public static String noFaction;
	public static Boolean logmessages;
	public static Boolean isFactionOn;
	public static Boolean isEssentials;
    private static Chat chat = null;
	public void onDisable() {

	}
	
    public static void logInfo(String msg) {
    	if(config.getConfig() ==null || !config.getConfig().isSet("logMessages") || config.getConfig().getBoolean("logMessages")) {
    		instance.getLogger().info(msg.replace("&", "§"));
    	}
    }
	
	public void onEnable() {

		instance = this;
		config = new Config();

		logInfo("Loading config files...");
		if(!config.initFiles()) {
        	getLogger().info("§cConfig or data files couldn't be initialized, the plugin will stop.");
        	return;
        }
		config.setConfig("logMessages", true);
		logmessages = config.getConfig().getBoolean("logMessages");
		config.setConfig("updater", true);
		config.setConfig("useHoverInfo", true);
		config.setConfig("hoverInfo","Put double quotes in config for using '\\n' for new line in hover message \n&bkills: %factionsuuid_player_kills% \n&alevel: %player_exp_to_level%"
);
		config.setConfig("itemFormat", "&8[&f{name} &7x{amount} {type}&8]");
		config.setConfig("itemHoverFormat", "&f{name}\nx&7{amount} {type}\n{enchantments}\n{lore}");
        super.onEnable();
        if(config.getConfig().getBoolean("updater")) {
        	logInfo("Checking for update...");
        	new Updater(this);
        }
        isEssentials = false;
        if(Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials")){
        	logInfo("Hooked into essentials for mute");
        	isEssentials = true;
        }
        isFactionOn = (Bukkit.getPluginManager().getPlugin("Factions") != null);
		this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
        this.getCommand("fchat").setExecutor(new ChatCommand());
        this.getCommand("fchat").setTabCompleter(new ChatCommand());
        setupPermissions();
        setupChat();
        noFaction = config.getConfig().getString("noFaction");
         colors = new HashMap<String, String>();
		colors.put("magic","&k");
		colors.put("rgb","&#RRGGBB");
		colors.put("black", "&0");
		colors.put("dark_blue", "&1");
		colors.put("dark_green", "&2");
		colors.put("dark_aqua", "&3");
		colors.put("dark_red", "&4");
		colors.put("dark_purple", "&5");
		colors.put("gold", "&6");
		colors.put("gray", "&7");
		colors.put("dark_gray", "&8");
		colors.put("blue", "&9");
		colors.put("green", "&a");
		colors.put("aqua", "&b");
		colors.put("red", "&c");
		colors.put("light_purple","&d");
		colors.put("yellow","&e");
		colors.put("white","&f");
		colors.put("bold","&l");
		colors.put("strikethrough","&m");
		colors.put("underline","&n");
		colors.put("italic","&o");
		colors.put("reset","&r");
	}
    		

    public static Permission getPermissions() {
        return perms;
    }
    
    public static Chat getChat() {
        return chat;
    }
  
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        try{
        	chat = rsp.getProvider();
        }catch(NullPointerException e){
       
        }
        return chat != null;
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    
	
	public static Main getInstance() {
        return instance;
    }
	public static Config getConfigFiles() {
        return config;
    }

	
	
	
	

}
