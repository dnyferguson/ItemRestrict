package net.craftersland.itemrestrict.restrictions;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.MaterialData;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public class Smelting implements Listener {
	
	private final ItemRestrict plugin;
	
	public Smelting(ItemRestrict plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onItemCrafted(FurnaceSmeltEvent event) {
		ItemStack item = event.getSource();
		Furnace f = (Furnace) event.getBlock().getState();
		if (!f.getInventory().getViewers().isEmpty()) {
			Player p = (Player) f.getInventory().getViewers().get(0);
			MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Smelting, p, item.getTypeId(), item.getDurability(), p.getLocation());
			
			if (bannedInfo != null) {
				event.setCancelled(true);
				plugin.getSoundHandler().sendPlingSound(p);
				plugin.getConfigHandler().printMessage(p, "chatMessages.smeltingRestricted", bannedInfo.reason);
			}
		} else {
            MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Smelting, null, item.getTypeId(), item.getDurability(), null);
			if (bannedInfo != null) {
				event.setCancelled(true);
			}
		}
	}
}
