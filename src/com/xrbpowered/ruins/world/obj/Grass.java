package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;

public class Grass extends SmallObject {

	private int variant;
	
	public Grass(World world, int variant) {
		super(world);
		this.variant = variant % PrefabRenderer.grass.length;
	}

	@Override
	public float getScaleRange() {
		return 0.35f;
	}

	@Override
	public float getRadius(float scale) {
		return 0.15f*scale;
	}

	@Override
	public Prefab getPrefab() {
		return PrefabRenderer.grass[variant];
	}

}
