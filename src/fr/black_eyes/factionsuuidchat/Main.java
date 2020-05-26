package fr.black_eyes.factionsuuidchat;

import org.bukkit.Bukkit;
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
	public static String noFaction;
	public static Boolean logmessages;
	public static Boolean isFactionOn;
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
        super.onEnable();
        if(config.getConfig().getBoolean("updater")) {
        	logInfo("Checking for update...");
        	Updater.checkversion();
        }
        isFactionOn = (Bukkit.getPluginManager().getPlugin("Factions") != null);
		this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
        this.getCommand("fchat").setExecutor(new ChatCommand());
        this.getCommand("fchat").setTabCompleter(new ChatCommand());
        setupPermissions();
        setupChat();
        noFaction = config.getConfig().getString("noFaction");
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
