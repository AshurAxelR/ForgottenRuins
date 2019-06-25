package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.Prefabs;
import com.xrbpowered.ruins.world.ObeliskSystem;
import com.xrbpowered.ruins.world.WorldGenerator.Token;

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
		return Prefabs.obelisk;
	}
	
	@Override
	public void interact() {
		if(visited==false) {
			visited = true;
			System.out.printf("New obelisk! Visited %d of %d.\n", system.countVisited(), system.countTotal());
		}
		else {
			System.out.println("Already visited...");
		}
	}

}
