package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class Shrine extends TileObject {

	public Shrine(World world, Token objToken) {
		super(world, objToken);
	}

	@Override
	public Prefab getPrefab() {
		return PrefabRenderer.shrine;
	}

	@Override
	public String getPickName() {
		return "Shrine";
	}
	
	@Override
	public String getActionString() {
		return "[Right-click to interact]";
	}
}
