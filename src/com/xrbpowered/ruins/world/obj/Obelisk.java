package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.ObeliskSystem;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class Obelisk extends TileObject {

	public final ObeliskSystem system;
	public boolean visited = false;
	
	public Obelisk(ObeliskSystem system, Token objToken) {
		super(system.world, objToken);
		this.system = system;
		system.add(this);
	}

	@Override
	public Prefab getPrefab() {
		return visited ? PrefabRenderer.obeliskGlow : PrefabRenderer.obelisk;
	}
	
	@Override
	public String getPickName() {
		return visited ? "Activated Obelisk" : "Inactive Obelisk";
	}
	
	@Override
	public String getActionString() {
		return visited ? "[Visited]" : "[Right-click to activate]";
	}
	
	@Override
	public void interact() {
		if(visited==false) {
			visited = true;
			System.out.printf("New obelisk! Visited %d of %d.\n", system.countVisited(), system.countTotal());
			Ruins.prefabs.updateAllInstances(world);
		}
		else {
			System.out.println("Already visited...");
		}
	}

}
