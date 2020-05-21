package fr.black_eyes.factionsuuidchat;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import fr.black_eyes.factionsuuidchat.Utils;
import fr.black_eyes.factionsuuidchat.command.ChatCommand;
import fr.black_eyes.factionsuuidchat.listeners.ChatListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;







public class Main extends JavaPlugin {
	//public ArrayList<LootChest> lc = new ArrayList<LootChest>();

	private static Main instance;
	private static Config config;
	private static Permission perms = null;
    private static Chat chat = null;
	public void onDisable() {

	}
	
    public static void logInfo(String msg) {
    	if(config.getConfig() ==null || !config.getConfig().isSet("ConsoleMessages") || config.getConfig().getBoolean("ConsoleMessages")) {
    		instance.getLogger().info(msg.replace("&", "§"));
    	}
    }
	
	public void onEnable() {

		instance = this;
		config = new Config();
		new Utils();
		logInfo("Loading config files...");
		if(!config.initFiles()) {
        	getLogger().info("§cConfig or data files couldn't be initialized, the plugin will stop.");
        	return;
        }

        super.onEnable();
		this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
        this.getCommand("chat").setExecutor(new ChatCommand());
        this.getCommand("chat").setTabCompleter(new ChatCommand());
        setupPermissions();
        setupChat();
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
