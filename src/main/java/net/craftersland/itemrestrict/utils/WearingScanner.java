package net.craftersland.itemrestrict.utils;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class WearingScanner {
	
	private final ItemRestrict plugin;
	
	public WearingScanner(ItemRestrict plugin) {
		this.plugin = plugin;
	}
	
	//WEARING RESTRICTIONS TASK
	public void wearingScanTask() {
		BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				for (final Player p : Bukkit.getOnlinePlayers()) {
					final ItemStack boots = p.getInventory().getBoots();
					final ItemStack leggings = p.getInventory().getLeggings();
					final ItemStack chestplate = p.getInventory().getChestplate();
					final ItemStack helmet = p.getInventory().getHelmet();

					if (boots != null) {
						MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Wearing, p, boots.getType(), boots.getData().getData(), p.getLocation());
						if (bannedInfo != null) {
							plugin.getConfigHandler().printMessage(p, "chatMessages.wearingRestricted", bannedInfo.reason);
							Bukkit.getScheduler().runTask(plugin, new Runnable() {
								@Override
								public void run() {

									p.getInventory().addItem(boots);
									p.getInventory().setBoots(null);
									plugin.getSoundHandler().sendPlingSound(p);
								}
							});
						}
					}
					if (leggings != null) {
						MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Wearing, p, leggings.getType(), leggings.getData().getData(), p.getLocation());
						if (bannedInfo != null) {
							plugin.getConfigHandler().printMessage(p, "chatMessages.wearingRestricted", bannedInfo.reason);
							Bukkit.getScheduler().runTask(plugin, new Runnable() {
								@Override
								public void run() {
									p.getInventory().addItem(leggings);
									p.getInventory().setLeggings(null);
									plugin.getSoundHandler().sendPlingSound(p);
								}
							});
						}
					}
					if (chestplate != null) {
						MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Wearing, p, chestplate.getType(), chestplate.getData().getData(), p.getLocation());
						if (bannedInfo != null) {
							plugin.getConfigHandler().printMessage(p, "chatMessages.wearingRestricted", bannedInfo.reason);
							Bukkit.getScheduler().runTask(plugin, new Runnable() {
								@Override
								public void run() {
									p.getInventory().addItem(chestplate);
									p.getInventory().setChestplate(null);
									plugin.getSoundHandler().sendPlingSound(p);
								}
							});
						}
					}
					if (helmet != null) {
						MaterialData bannedInfo = plugin.getRestrictedItemsHandler().isBanned(ActionType.Wearing, p, helmet.getType(), helmet.getData().getData(), p.getLocation());
						if (bannedInfo != null) {
							plugin.getConfigHandler().printMessage(p, "chatMessages.wearingRestricted", bannedInfo.reason);
							Bukkit.getScheduler().runTask(plugin, new Runnable() {
								@Override
								public void run() {
									p.getInventory().addItem(helmet);
									p.getInventory().setHelmet(null);
									plugin.getSoundHandler().sendPlingSound(p);
								}
							});
						}
					}
				}
			}
		}, 20L, 20L);
		plugin.wearingScanner.clear();
		plugin.wearingScanner.put(true, task.getTaskId());
	}
}
