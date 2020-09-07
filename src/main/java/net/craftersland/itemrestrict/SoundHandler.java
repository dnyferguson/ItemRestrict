package net.craftersland.itemrestrict;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundHandler {
	
	private final ItemRestrict plugin;
	
	public SoundHandler(ItemRestrict plugin) {
		this.plugin = plugin;
	}
	
	public void sendAnvilLandSound(Player p) {
		if (plugin.getConfigHandler().getBoolean("General.Sounds.onRestrictions")) {
			p.playSound(p.getLocation(), XSound.BLOCK_ANVIL_LAND.parseSound(), 1, 1);
		}
	}
	
	public void sendEndermanTeleportSound(Player p) {
		if (plugin.getConfigHandler().getBoolean("General.Sounds.onRestrictions")) {
			p.playSound(p.getLocation(), XSound.ENTITY_ENDERMAN_TELEPORT.parseSound(), 1, 1);
		}
	}
	
	public void sendItemBreakSound(Player p) {
		if (plugin.getConfigHandler().getBoolean("General.Sounds.onRestrictions")) {
			p.playSound(p.getLocation(), XSound.ENTITY_ITEM_BREAK.parseSound(), 1, 1);
		}
	}
	
	public void sendPlingSound(Player p) {
		if (plugin.getConfigHandler().getBoolean("General.Sounds.onRestrictions")) {
			p.playSound(p.getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 3F, 3F);
		}
	}
	
	public void sendLevelUpSound(Player p) {
		if (plugin.getConfigHandler().getBoolean("General.Sounds.onRestrictions")) {
			p.playSound(p.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1F, 1F);
		}
	}
	
	public void sendArrowHit(Player p) {
		if (plugin.getConfigHandler().getBoolean("General.Sounds.onRestrictions")) {
			p.playSound(p.getLocation(), XSound.ENTITY_ARROW_HIT_PLAYER.parseSound(), 1F, 1F);
		}
	}
}
