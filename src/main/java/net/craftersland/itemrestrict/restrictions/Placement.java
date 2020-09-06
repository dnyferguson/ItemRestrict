package net.craftersland.itemrestrict.restrictions;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.MaterialData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class Placement implements Listener {
	
	private final ItemRestrict plugin;
	
	public Placement(ItemRestrict plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onInteract(BlockPlaceEvent event) {
		if (plugin.placementBanned.size() != 0) {
			MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Placement, event.getPlayer(), event.getItemInHand().getTypeId(), event.getItemInHand().getDurability(), event.getPlayer().getLocation());
			if (bannedInfo != null) {
				event.setCancelled(true);
				plugin.getSoundHandler().sendEndermanTeleportSound(event.getPlayer());
				plugin.getConfigHandler().printMessage(event.getPlayer(), "chatMessages.placementRestricted", bannedInfo.reason);
			} else if (plugin.is19Server) {
				MaterialData bannedInfo2 = plugin.getRestrictedItemsHandler().isBanned(ActionType.Placement, event.getPlayer(), event.getPlayer().getInventory().getItemInOffHand().getTypeId(), event.getPlayer().getInventory().getItemInOffHand().getDurability(), event.getPlayer().getLocation());
				if (bannedInfo2 != null) {
					event.setCancelled(true);
					plugin.getSoundHandler().sendEndermanTeleportSound(event.getPlayer());
					plugin.getConfigHandler().printMessage(event.getPlayer(), "chatMessages.placementRestricted", bannedInfo2.reason);
				}
			}
		}
	}
}
