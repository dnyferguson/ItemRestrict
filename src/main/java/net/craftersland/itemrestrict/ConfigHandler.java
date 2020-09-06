package net.craftersland.itemrestrict;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
	
	private final ItemRestrict plugin;
	
	public ConfigHandler(final ItemRestrict plugin) {
		this.plugin = plugin;
		loadConfig();
	}
	
	public void loadConfig() {
		File pluginFolder = new File("plugins" + System.getProperty("file.separator") + ItemRestrict.pluginName);
		if (!pluginFolder.exists()) {
    		pluginFolder.mkdir();
    	}
		File configFile = new File("plugins" + System.getProperty("file.separator") + ItemRestrict.pluginName + System.getProperty("file.separator") + "config.yml");
		if (!configFile.exists()) {
			ItemRestrict.log.info("No config file found! Creating new one...");
			plugin.saveDefaultConfig();
		}
    	try {
    		ItemRestrict.log.info("Loading the config file...");
    		plugin.getConfig().load(configFile);
    	} catch (Exception e) {
    		ItemRestrict.log.severe("Could not load the config file! You need to regenerate the config! Error: " + e.getMessage());
			e.printStackTrace();
    	}
    	if (getBoolean("General.EnableOnAllWorlds")) {
    		ItemRestrict.log.info("Restrictions enabled on all worlds.");
    	} else {
    		getWorldsTask();
    	}
	}
	
	public String getString(String key) {
		if (!plugin.getConfig().contains(key)) {
			plugin.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + ItemRestrict.pluginName + " folder! (Try generating a new one by deleting the current)");
			return "errorCouldNotLocateInConfigYml:" + key;
		} else {
			return plugin.getConfig().getString(key);
		}
	}
	
	public Integer getInteger(String key) {
		if (!plugin.getConfig().contains(key)) {
			plugin.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + ItemRestrict.pluginName + " folder! (Try generating a new one by deleting the current)");
			return null;
		} else {
			return plugin.getConfig().getInt(key);
		}
	}
	
	public Boolean getBoolean(String key) {
		if (!plugin.getConfig().contains(key)) {
			plugin.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + ItemRestrict.pluginName + " folder! (Try generating a new one by deleting the current)");
			return null;
		} else {
			return plugin.getConfig().getBoolean(key);
		}
	}
	
	//Send chat messages from config
	public void printMessage(Player p, String messageKey, String reason) {
		if (plugin.getConfig().contains(messageKey)){
			List<String> message = new ArrayList<String>();
			message.add(plugin.getConfig().getString(messageKey));
			
			if (reason != null) {
				message.set(0, message.get(0).replaceAll("%reason", "" + reason));
			}
			
			if (p != null) {				
				//Message format
				p.sendMessage(getString("chatMessages.prefix").replaceAll("&", "§") + message.get(0).replaceAll("&", "§"));
			}		
		} else {
			plugin.getLogger().severe("Could not locate '"+messageKey+"' in the config.yml inside of the ItemRestrict folder!");
			p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Could not locate '"+messageKey+"' in the config.yml inside of the ItemRestrict folder!");
		}
			
	}
	
	//Get worlds
	private void getWorldsTask() {
		//Get worlds to enable restrictions from config
		final List<String> enabledWorlds = plugin.getConfig().getStringList("General.Worlds");
		ItemRestrict.log.info("Scanning for loaded worlds in 10 seconds...");
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				//validate that list
				ItemRestrict.enforcementWorlds = new ArrayList<World>();
				ItemRestrict.log.info("Scanning for loaded worlds...");
				for (String worldName : enabledWorlds) {
					World world = plugin.getServer().getWorld(worldName);
					if (world == null) {
						ItemRestrict.log.warning("Error: There's no world named " + worldName + ".  Please update your config.yml.");
					} else {
						ItemRestrict.enforcementWorlds.add(world);
					}
				}
				if(enabledWorlds.size() == 0) {
					ItemRestrict.log.warning("No worlds found listed in config! Restrictions will not take place!");
				}
				//List the world names found.
				ArrayList<String> worldNames = new ArrayList<String>();
				for (World x : ItemRestrict.enforcementWorlds) {
					worldNames.add(x.getName());
				}
				ItemRestrict.log.info("Plugin enabled on worlds: " + worldNames.toString());
			}
		}, 200L);
	}
}
