package net.craftersland.itemrestrict.utils;

import java.util.ArrayList;
import java.util.List;

public class MaterialCollection {
	
	List<MaterialData> materials = new ArrayList<>();
	
	public void Add(MaterialData material) {
		this.materials.add(material);
	}
	
	//returns a MaterialInfo complete with the friendly material name from the config file
	public MaterialData Contains(MaterialData material) {
		for (MaterialData mat : this.materials) {
			if (material.material != mat.material) {
				continue;
			}

			if (material.data != mat.data && !mat.allDataValues) {
				continue;
			}

			return mat;
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
