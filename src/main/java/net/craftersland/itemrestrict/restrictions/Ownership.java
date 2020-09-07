package net.craftersland.itemrestrict.restrictions;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.MaterialData;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class Ownership implements Listener {
	
	private final ItemRestrict plugin;
	
	public Ownership(ItemRestrict plugin) {
		this.plugin = plugin;
	}
	
	//Inventory Click
	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemClicked(InventoryClickEvent event) {
		if (event.getSlotType() != null) {
			if (event.getCurrentItem() != null) {
				Player p = (Player) event.getWhoClicked();
				ItemStack currentItem = event.getCurrentItem();
				ItemStack cursorItem = event.getCursor();
				
				if (plugin.getConfigHandler().getBoolean("General.Settings.OwnershipPlayerInvOnly")) {
					if (event.getInventory().getType() == InventoryType.PLAYER) {
						inventoryClickRestriction(event, currentItem, p, false);
					} else if (event.getRawSlot() >= event.getInventory().getSize()) {
						inventoryClickRestriction(event, cursorItem, p, true);
					}
				} else {
					inventoryClickRestriction(event, currentItem, p, false);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemDrag(final InventoryDragEvent event) {
		if (plugin.getConfigHandler().getBoolean("General.Settings.OwnershipPlayerInvOnly")) {
			Player p = (Player) event.getWhoClicked();
			ItemStack cursorItem = event.getOldCursor();
			
			if (cursorItem != null) {
				MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Ownership, p, cursorItem.getType(), cursorItem.getData().getData(), p.getLocation());
				
				if (bannedInfo != null) {
					event.setCancelled(true);
					
					plugin.getSoundHandler().sendItemBreakSound(p);
					plugin.getConfigHandler().printMessage(p, "chatMessages.restricted", bannedInfo.reason);
				}
			}
		}
	}
	
	private void inventoryClickRestriction(InventoryClickEvent event, ItemStack currentItem, Player p, Boolean removeCursorItem) {
		MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Ownership, p, currentItem.getType(), currentItem.getData().getData(), p.getLocation());
		
		if (bannedInfo != null) {
			event.setCancelled(true);
			
			if (event.getSlotType() == SlotType.ARMOR) {
				if (p.getInventory().getHelmet() != null) {
					if (p.getInventory().getHelmet().isSimilar(currentItem)) {
						p.getInventory().setHelmet(null);
					}
				}
				if (p.getInventory().getChestplate() != null) {
					if (p.getInventory().getChestplate().isSimilar(currentItem)) {
						p.getInventory().setChestplate(null);
					}
				}
				if (p.getInventory().getLeggings() != null) {
					if (p.getInventory().getLeggings().isSimilar(currentItem)) {
						p.getInventory().setLeggings(null);
					}
				}
				if (p.getInventory().getBoots() != null) {
					if (p.getInventory().getBoots().isSimilar(currentItem)) {
						p.getInventory().setBoots(null);
					}
				}
			} else if (removeCursorItem) {
				p.setItemOnCursor(null);
			} else {
				p.getInventory().remove(currentItem);
				event.getInventory().remove(currentItem);
			}
			
			plugin.getSoundHandler().sendItemBreakSound(p);
			plugin.getConfigHandler().printMessage(p, "chatMessages.restrictedConfiscated", bannedInfo.reason);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemPickup(PlayerPickupItemEvent event) {
		Player p = event.getPlayer();
		ItemStack item = event.getItem().getItemStack();
		
		MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Ownership, p, item.getType(), item.getData().getData(), p.getLocation());
		
		if (bannedInfo != null) {
			event.setCancelled(true);
			
			event.getItem().remove();
			
			plugin.getSoundHandler().sendItemBreakSound(p);
			p.playEffect(event.getItem().getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
			plugin.getConfigHandler().printMessage(p, "chatMessages.pickupRestricted", bannedInfo.reason);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemHeldSwitch(PlayerItemHeldEvent event) {
		int newSlot = event.getNewSlot();
		Player p = event.getPlayer();
		ItemStack item = p.getInventory().getItem(newSlot);
		
		if (item != null) {
			MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Ownership, p, item.getType(), item.getData().getData(), p.getLocation());
			
			if (bannedInfo != null) {
				p.getInventory().setItem(newSlot, null);
				
				plugin.getSoundHandler().sendItemBreakSound(p);
				plugin.getConfigHandler().printMessage(p, "chatMessages.restrictedConfiscated", bannedInfo.reason);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onCreativeEvents(InventoryCreativeEvent event) {
		ItemStack cursorItem = event.getCursor();
		
		if (cursorItem != null) {
			Player p = (Player) event.getWhoClicked();
			
			MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Ownership, p, cursorItem.getType(), cursorItem.getData().getData(), p.getLocation());
			
			if (bannedInfo != null) {
				event.setCancelled(true);
				event.setCursor(null);
				
				plugin.getSoundHandler().sendItemBreakSound(p);
				plugin.getConfigHandler().printMessage(p, "chatMessages.restrictedConfiscated", bannedInfo.reason);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		
		if (item != null) {
			MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Ownership, p, item.getType(), item.getData().getData(), p.getLocation());
			
			if (bannedInfo != null) {
				event.setCancelled(true);
				p.setItemInHand(null);
				
				plugin.getSoundHandler().sendItemBreakSound(p);
				plugin.getConfigHandler().printMessage(p, "chatMessages.restrictedConfiscated", bannedInfo.reason);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemDrop(PlayerDropItemEvent event) {
		Player p = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();
		
		MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Ownership, p, item.getType(), item.getData().getData(), p.getLocation());
		
		if (bannedInfo != null) {
			event.getItemDrop().remove();
			p.setItemInHand(null);
			
			plugin.getSoundHandler().sendItemBreakSound(p);
			plugin.getConfigHandler().printMessage(p, "chatMessages.restrictedConfiscated", bannedInfo.reason);
		}
	}
}
