package net.craftersland.itemrestrict.utils;

import com.cryptomorin.xseries.XMaterial;
import net.craftersland.itemrestrict.ItemRestrict;
import org.bukkit.Material;

import java.util.logging.Level;

public class MaterialData {

	public Material material;
	public short data;
	public boolean allDataValues;
	public String description;
	public String reason;
	
	public MaterialData(Material material, short data, String description, String reason) {
		this.material = material;
		this.data = data;
		this.allDataValues = false;
		this.description = description;
		this.reason = reason;
	}
	
	public MaterialData(Material material, String description, String reason) {
		this.material = material;
		this.data = 0;
		this.allDataValues = true;
		this.description = description;
		this.reason = reason;
	}
	
	private MaterialData(Material material, short data, boolean allDataValues, String description, String reason) {
		this.material = material;
		this.data = data;
		this.allDataValues = allDataValues;
		this.description = description;
		this.reason = reason;
	}
	
	@Override
	public String toString() {
		String returnValue = material.name() + ":" + (this.allDataValues?"*":String.valueOf(this.data));
		if(this.description != null) returnValue += ":" + this.description + ":" + this.reason;
		
		return returnValue;
	}
	
	public static MaterialData fromString(String string, ItemRestrict plugin) {
		if(string == null || string.isEmpty()) return null;
		
		String [] parts = string.split(":");
		if(parts.length < 2) return null;

		Material mat = null;
		short data;
		boolean allDataValues;
		if(parts[1].equals("*")) {
			allDataValues = true;
			data = 0;
		} else {
			allDataValues = false;
			data = Short.parseShort(parts[1]);
		}

		try {
			String value = parts[0].toUpperCase().replace(" ", "_");
			mat = XMaterial.matchXMaterial(value).get().parseMaterial();
			if (data != 0) {
				plugin.getLogger().log(Level.INFO, "Loaded modern item by name: " + mat.name() + ":" + data + ".");
			} else {
				plugin.getLogger().log(Level.INFO, "Loaded modern item by name: " + mat.name() + ".");
			}
		} catch (Exception ignore) {
			// material name not found, try finding it using old ID system
			try {
				int typeID = Integer.parseInt(parts[0]);
				mat = XMaterial.matchXMaterial(typeID, (byte) data).get().parseMaterial();
				plugin.getLogger().log(Level.INFO, "Loaded legacy item by id: " + typeID + ".");
			} catch(Exception ignore2) {
				return null;
			}
		}

		if (mat == null) {
			return null;
		}
			
		return new MaterialData(mat, data, allDataValues, parts.length >= 3 ? parts[2] : "", parts.length >= 4 ? parts[3] : "(No reason provided.)");
	}
}
