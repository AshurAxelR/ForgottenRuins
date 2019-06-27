package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.Prefabs;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class Well extends TileObject {

	public static final int genSpan = 16;
	public static final int extraWells = 10;
	
	public Well(World world, Token objToken) {
		super(world, objToken);
	}

	@Override
	public Prefab getPrefab() {
		return Prefabs.well;
	}
	
	@Override
	public void interact() {
		System.out.println("Refreshing...");
	}

}
