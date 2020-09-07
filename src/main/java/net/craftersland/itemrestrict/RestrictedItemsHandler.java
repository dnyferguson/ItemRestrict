package net.craftersland.itemrestrict;

import net.craftersland.itemrestrict.utils.MaterialCollection;
import net.craftersland.itemrestrict.utils.MaterialData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class RestrictedItemsHandler {
	
	private final ItemRestrict plugin;

	public RestrictedItemsHandler(ItemRestrict plugin) {
		this.plugin = plugin;
		
		try {
			File restrictedItemsFile = new File("plugins" + System.getProperty("file.separator") + ItemRestrict.pluginName + System.getProperty("file.separator") + "RestrictedItems.yml");
			
			//Create RestrictedItems.yml and/or load it
			if (!restrictedItemsFile.exists()) {
				ItemRestrict.log.info("No RestrictedItems.yml file found! Creating new one...");
				
				plugin.saveResource("RestrictedItems.yml", false);
			}
			
			FileConfiguration ymlFormat = YamlConfiguration.loadConfiguration(restrictedItemsFile);
			
			//
			//OWNERSHIP BANS - players can't have these at all (crafting is also blocked in this case)
			//
			List<String> OwnershipBanned = ymlFormat.getStringList("OwnershipBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(OwnershipBanned, ItemRestrict.ownershipBanned, "OwnershipBanned");
			
			//
			//CRAFTING BANS 
			//
			List<String> CraftingBanned = ymlFormat.getStringList("CraftingBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(CraftingBanned, plugin.craftingBanned, "CraftingBanned");
			
			//
			//CRAFTING BANS 
			//
			List<String> SmeltingBanned = ymlFormat.getStringList("SmeltingBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(SmeltingBanned, plugin.smeltingBanned, "SmeltingBanned");
			
			//
			//CRAFTING DISABLED
			//
			List<String> CraftingDisabled = ymlFormat.getStringList("CraftingDisabled");
			//parse the strings from the config file
			plugin.craftingDisabled.addAll(CraftingDisabled);
			
			//
			//BREWING BANS 
			//
			List<String> BrewingBanned = ymlFormat.getStringList("BrewingBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(BrewingBanned, plugin.brewingBanned, "BrewingBanned");
			
			//
			//WEARING BANS 
			//
			List<String> WearingBanned = ymlFormat.getStringList("WearingBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(WearingBanned, plugin.wearingBanned, "WearingBanned");
			
			//
			//USAGE BANS 
			//
			List<String> UsageBanned = ymlFormat.getStringList("UsageBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(UsageBanned, plugin.usageBanned, "UsageBanned");
			
			//
			//PLACEMENT BANS 
			//
			List<String> PlacementBanned = ymlFormat.getStringList("PlacementBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(PlacementBanned, plugin.placementBanned, "PlacementBanned");
			
			//
			//BLOCK BREAK BANS 
			//
			if (plugin.getConfigHandler().getBoolean("General.Restrictions.BreakBans")) {
				List<String> BlockBreakBanned = ymlFormat.getStringList("BlockBreakBanned");
				//parse the strings from the config file
				parseMaterialListFromConfig(BlockBreakBanned, plugin.blockBreakBanned, "BlockBreakBanned");
			}
			
			//
			//CREATIVE MENU BANS 
			//
			List<String> CreativeBanned = ymlFormat.getStringList("CreativeBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(CreativeBanned, plugin.creativeBanned, "CreativeBanned");
			
			//
			//PICKUP BANS 
			//
			List<String> PickupBanned = ymlFormat.getStringList("PickupBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(PickupBanned, plugin.pickupBanned, "PickupBanned");
			
			//
			//DROP BANS 
			//
			List<String> DropBanned = ymlFormat.getStringList("DropBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(DropBanned, plugin.dropBanned, "DropBanned");
			
			//
			//SMELTING BANS 
			//
			List<String> SmeltingDisabled = ymlFormat.getStringList("SmeltingDisabled");
			//parse the strings from the config file
			plugin.smeltingDisabled.addAll(SmeltingDisabled);
			
			//
			//WORLD BANS 
			//
			List<String> WorldBanned = ymlFormat.getStringList("WorldBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(WorldBanned, plugin.worldBanned, "WorldBanned");
			
		} catch (Exception e) {
			ItemRestrict.log.severe("Could not create RestrictedItems.yml file! Error: " + e.getMessage());
		    }
		
		}
	
	private void parseMaterialListFromConfig(List<String> stringsToParse, MaterialCollection materialCollection, String configString) {
		materialCollection.clear();
		
		//for each string in the list
		for (String s : stringsToParse) {
			//try to parse the string value into a material info
			MaterialData materialData = MaterialData.fromString(s, plugin);

			//null value returned indicates an error parsing the string from the config file
			if (materialData == null) {
				//show error in log
				ItemRestrict.log.warning("ERROR: Unable to read material entry: " + s + " ,from RestrictedItems.yml file.");
			}

			//otherwise store the valid entry in config data
			else {
				materialCollection.Add(materialData);
			}
		}		
	}
	
	public MaterialData isBanned(ActionType actionType, Player player, Material material, short data, Location location) {
		if (!plugin.getConfigHandler().getString("General.EnableOnAllWorlds").equals("true")) {
			if (location != null) {
				if(!ItemRestrict.enforcementWorlds.contains(location.getWorld())) return null;
			}
		}
		if (player != null && player.hasPermission("ItemRestrict.admin") || player != null && player.hasPermission("ItemRestrict.bypass")) return null;
		MaterialCollection collectionToSearch;
		String permissionNode;
		if(actionType == ActionType.Usage) {
			collectionToSearch = plugin.usageBanned;
			permissionNode = "use";
		} else if(actionType == ActionType.Placement)	{
			collectionToSearch = plugin.placementBanned;
			permissionNode = "place";
		} else if(actionType == ActionType.BlockBreak)	{
			collectionToSearch = plugin.blockBreakBanned;
			permissionNode = "break";
		} else if(actionType == ActionType.Crafting) {
			collectionToSearch = plugin.craftingBanned;
			permissionNode = "craft";
		} else if(actionType == ActionType.Brewing) {
			collectionToSearch = plugin.brewingBanned;
			permissionNode = "brew";
		} else if(actionType == ActionType.Wearing) {
			collectionToSearch = plugin.wearingBanned;
			permissionNode = "wear";
		} else if(actionType == ActionType.Creative) {
			collectionToSearch = plugin.creativeBanned;
			permissionNode = "creative";
		} else if(actionType == ActionType.Pickup) {
			collectionToSearch = plugin.pickupBanned;
			permissionNode = "pickup";
		} else if(actionType == ActionType.Drop) {
			collectionToSearch = plugin.dropBanned;
			permissionNode = "drop";
		} else if(actionType == ActionType.Smelting) {
			collectionToSearch = plugin.smeltingBanned;
			permissionNode = "smelt";
		} else {
			collectionToSearch = ItemRestrict.ownershipBanned;
			permissionNode = "own";
		}

//		System.out.println("DEBUG: trying item " + material.name() + ":" + data);
//		System.out.println("DEBUG 2: Should be " + material.name() + ":" + material);

		MaterialData bannedInfo = collectionToSearch.Contains(new MaterialData(material, data, null, null));
		if(bannedInfo != null) {
			if (player == null) return bannedInfo;
			if(player.hasPermission("ItemRestrict.bypass." + material.getId() + ".*.*")) return null;
			if(player.hasPermission("ItemRestrict.bypass." + material.name().toLowerCase() + ".*.*")) return null;
			if(player.hasPermission("ItemRestrict.bypass." + material.getId() + ".*." + permissionNode)) return null;
			if(player.hasPermission("ItemRestrict.bypass." + material.name().toLowerCase() + ".*." + permissionNode)) return null;
			if(player.hasPermission("ItemRestrict.bypass." + material.getId() + "." + data + "." + permissionNode)) return null;			
			if(player.hasPermission("ItemRestrict.bypass." + material.name().toLowerCase() + "." + data + "." + permissionNode)) return null;
			if(player.hasPermission("ItemRestrict.bypass." + material.getId() + "." + data + ".*")) return null;
			if(player.hasPermission("ItemRestrict.bypass." + material.name().toLowerCase() + "." + data + ".*")) return null;

			return bannedInfo;
		}
		return null;
	}
	
	public enum ActionType {
		Usage, Ownership, Placement, BlockBreak, Crafting, Smelting, Creative, Brewing, Wearing, Pickup, Drop
	}
}
