package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class DryWell extends TileObject {

	public static final int count = 100;

	public DryWell(World world, Token objToken) {
		super(world, objToken);
	}

	@Override
	public Prefab getPrefab() {
		return PrefabRenderer.dryWell;
	}

}
