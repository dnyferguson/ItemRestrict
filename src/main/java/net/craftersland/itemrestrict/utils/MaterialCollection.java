package net.craftersland.itemrestrict.utils;

import java.util.ArrayList;
import java.util.List;

public class MaterialCollection {
	
	List<MaterialData> materials = new ArrayList<>();
	
	public void Add(MaterialData material) {
		int i;
		for(i = 0; i < this.materials.size() && this.materials.get(i).typeID <= material.typeID; i++) {
			this.materials.add(i, material);
		}
	}
	
	//returns a MaterialInfo complete with the friendly material name from the config file
	public MaterialData Contains(MaterialData material) {
		for (MaterialData thisMaterial : this.materials) {
			if (material.typeID == thisMaterial.typeID && (thisMaterial.allDataValues || material.data == thisMaterial.data)) {
				return thisMaterial;
			} else if (thisMaterial.typeID > material.typeID) {
				return null;
			}
		}
			
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (MaterialData material : this.materials) {
			stringBuilder.append(material.toString()).append(" ");
		}
		
		return stringBuilder.toString();
	}
	
	public int size()
	{
		return this.materials.size();
	}

	public void clear() 
	{
		this.materials.clear();
	}
}
