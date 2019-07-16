package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;

public class Grass extends SmallObject {

	public Grass(World world) {
		super(world);
	}

	@Override
	public float getScaleRange() {
		return 0.3f;
	}

	@Override
	public float getRadius(float scale) {
		return 0.15f*scale;
	}

	@Override
	public Prefab getPrefab() {
		return PrefabRenderer.grass1;
	}

}
