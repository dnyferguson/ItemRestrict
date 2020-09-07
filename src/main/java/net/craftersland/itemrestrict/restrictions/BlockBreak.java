package net.craftersland.itemrestrict.restrictions;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.MaterialData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreak implements Listener {
	
	private final ItemRestrict plugin;
	
	public BlockBreak(ItemRestrict plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onBlockBreak(BlockBreakEvent event) {
		if (plugin.blockBreakBanned.size() != 0) {
			boolean compareDrops = false;
			ItemStack itemToDrop = null;
			if (event.getBlock().getDrops().iterator().hasNext()) {
				itemToDrop = event.getBlock().getDrops().iterator().next();
				if (event.getBlock().getType() == itemToDrop.getType()) {
					compareDrops = true;
				}
			}
			MaterialData bannedInfo;
			if (!compareDrops) {
				bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.BlockBreak, event.getPlayer(), event.getBlock().getType(), event.getBlock().getState().getData().getData(), event.getPlayer().getLocation());
			} else {
				bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.BlockBreak, event.getPlayer(), itemToDrop.getType(), itemToDrop.getData().getData(), event.getPlayer().getLocation());
			}
			
			if (bannedInfo != null) {
				event.setCancelled(true);
				
				plugin.getSoundHandler().sendPlingSound(event.getPlayer());
				plugin.getConfigHandler().printMessage(event.getPlayer(), "chatMessages.blockBreakRestricted", bannedInfo.reason);
			}
		}
	}
}
