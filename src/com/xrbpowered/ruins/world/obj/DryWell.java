package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.entity.WorldEntity;
import com.xrbpowered.ruins.entity.mob.Ghost;
import com.xrbpowered.ruins.entity.mob.MobEntity;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class DryWell extends TileObject implements WorldEntity{

	public static final int count = 100;
	public static final float maxInitialSpawnDelay = 120f;
	public static final float spawnPeriod = 30f;

	private float timeToSpawn;
	private MobEntity ghost = null;
	
	public DryWell(World world, Token objToken, float spawnDelay) {
		super(world, objToken);
		timeToSpawn = spawnDelay*maxInitialSpawnDelay;
	}

	@Override
	public Prefab getPrefab() {
		return PrefabRenderer.dryWell;
	}

	@Override
	public boolean updateTime(float dt) {
		if(ghost==null) {
			timeToSpawn -= dt;
			if(timeToSpawn<0f) {
				ghost = new Ghost(world).spawn(x, z, y, d);
			}
		}
		if(ghost!=null && !ghost.alive) {
			ghost = null;
			timeToSpawn = spawnPeriod;
		}
		return true;
	}
}
