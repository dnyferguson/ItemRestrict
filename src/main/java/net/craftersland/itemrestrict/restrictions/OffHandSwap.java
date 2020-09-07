package net.craftersland.itemrestrict.restrictions;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.MaterialData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class OffHandSwap implements Listener {
	
	private final ItemRestrict plugin;
	
	public OffHandSwap(ItemRestrict plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onOffHandSwap(PlayerSwapHandItemsEvent event) {
		MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Ownership, event.getPlayer(), event.getOffHandItem().getType(), event.getOffHandItem().getData().getData(), event.getPlayer().getLocation());
		if (bannedInfo == null) {
			MaterialData bannedInfo2 = plugin.getRestrictedItemsHandler().isBanned(ActionType.Usage, event.getPlayer(), event.getOffHandItem().getType(), event.getOffHandItem().getData().getData(), event.getPlayer().getLocation());
			MaterialData bannedInfo3 = plugin.getRestrictedItemsHandler().isBanned(ActionType.Placement, event.getPlayer(), event.getOffHandItem().getType(), event.getOffHandItem().getData().getData(), event.getPlayer().getLocation());
			if (bannedInfo2 != null) {
				event.setCancelled(true);
				plugin.getSoundHandler().sendPlingSound(event.getPlayer());
				plugin.getConfigHandler().printMessage(event.getPlayer(), "chatMessages.ussageRestricted", bannedInfo2.reason);
			} else if (bannedInfo3 != null) {
				event.setCancelled(true);
			}
		} else {
			event.setCancelled(true);
			event.setOffHandItem(null);
		}
	}
}
