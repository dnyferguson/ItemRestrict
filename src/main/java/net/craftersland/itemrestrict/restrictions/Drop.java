package net.craftersland.itemrestrict.restrictions;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.MaterialData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class Drop implements Listener {
	
	private final ItemRestrict plugin;
	
	public Drop(ItemRestrict plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemDrop(PlayerDropItemEvent event) {
		if (plugin.getConfigHandler().getBoolean("General.Restrictions.DropBans")) {
			Player p = event.getPlayer();
			ItemStack item = event.getItemDrop().getItemStack();
			
			MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Ownership, p, item.getTypeId(), item.getDurability(), p.getLocation());
			
			if (bannedInfo == null) {
				MaterialData bannedInfo2 = plugin.getRestrictedItemsHandler().isBanned(ActionType.Drop, p, item.getTypeId(), item.getDurability(), p.getLocation());
				
				if (bannedInfo2 != null) {
					event.setCancelled(true);
					
					plugin.getSoundHandler().sendPlingSound(p);
					plugin.getConfigHandler().printMessage(p, "chatMessages.dropingRestricted", bannedInfo2.reason);
				}
			}
		}
	}
}
