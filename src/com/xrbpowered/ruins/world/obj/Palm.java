package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.Prefabs;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.WorldGenerator.Token;

public class Palm extends TileObject {

	public Palm(World world, Token objToken) {
		super(world, objToken);
	}

	@Override
	public Prefab getPrefab() {
		return Prefabs.palm;
	}

}
