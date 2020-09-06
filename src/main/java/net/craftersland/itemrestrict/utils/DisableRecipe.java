package net.craftersland.itemrestrict.utils;

import net.craftersland.itemrestrict.ItemRestrict;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Iterator;

public class DisableRecipe {
	
	private final ItemRestrict plugin;
	
	public DisableRecipe(ItemRestrict plugin) {
		this.plugin = plugin;
		disableRecipesTask(5);
	}
	
	private void removeRecipe(ItemStack is) {
        Iterator<Recipe> it = Bukkit.getServer().recipeIterator();
        Recipe recipe;
        while (it.hasNext()) {
            recipe = it.next();
            if (recipe != null && recipe.getResult().isSimilar(is)) {
            	plugin.disabledRecipes.add(recipe);
                it.remove();
            }
        }
    }
	
	public void disableRecipesTask(int delay) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				if (!plugin.craftingDisabled.isEmpty()) {
					for (String s : plugin.craftingDisabled) {
						String[] s1 = s.split(":");
						try {
							int id = Integer.parseInt(s1[0]);
							Material m = Material.getMaterial(id);
							ItemStack is;
							if (s1[1].contains("*")) {
								is = new ItemStack(m);
							} else {
								short b = Short.parseShort(s1[1]);
								is = new ItemStack(m, 1, b);
							}
							
							removeRecipe(is);
						} catch (Exception e) {
							ItemRestrict.log.warning("Failed to disable crafting for item: " + s1[0] + ":" + s1[1] + " . Error: " + e.getMessage());
						}
					}
				}
			}
			
		}, delay * 20L);
	}
	
	public void restoreRecipes() {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				if (!plugin.disabledRecipes.isEmpty()) {
					for (Recipe r : plugin.disabledRecipes) {
						try {
							Bukkit.addRecipe(r);
						} catch (Exception e) {
							ItemRestrict.log.warning("Failed to restore disabled recipe for: " + r.getResult().getType().toString() + " .Error: " + e.getMessage());
						}
					}
					plugin.disabledRecipes.clear();
				}
			}
		});
	}
}
