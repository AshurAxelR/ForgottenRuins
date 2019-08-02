package com.xrbpowered.ruins.world.obj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import com.xrbpowered.ruins.entity.WorldEntity;
import com.xrbpowered.ruins.entity.mob.Ghost;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class DryWell extends TileObject implements WorldEntity {

	public static final float maxInitialSpawnDelay = 180f;
	public static final float spawnPeriod = 60f;

	public static int getCount(int level) {
		return (level+1)*(level+1);
	}
	
	private float timeToSpawn;
	private Ghost ghost = null;
	private int ghostId = -1;
	
	private Random random = new Random();
	
	public DryWell(World world, Token objToken, float spawnDelay) {
		super(world, objToken);
		random.setSeed(this.seed);
		timeToSpawn = spawnDelay*maxInitialSpawnDelay;
	}
	
	@Override
	public void loadState(DataInputStream in) throws IOException {
		timeToSpawn = in.readFloat();
		ghostId = in.readInt();
	}
	
	@Override
	public void saveState(DataOutputStream out) throws IOException {
		out.writeFloat(timeToSpawn);
		out.writeInt(ghost==null ? -1 : world.mobs.indexOf(ghost));
	}

	@Override
	public Prefab getPrefab() {
		return PrefabRenderer.dryWell;
	}

	@Override
	public boolean updateTime(float dt) {
		if(ghostId>=0) {
			ghost = (Ghost) world.mobs.get(ghostId);
			ghostId = -1;
		}
		if(ghost==null) {
			timeToSpawn -= dt;
			if(timeToSpawn<0f) {
				ghost = (Ghost) new Ghost(world, random).spawn(x, z, y, d);
			}
		}
		if(ghost!=null && (!ghost.alive || ghost.time>=ghost.lifespan)) {
			ghost = null;
			timeToSpawn = spawnPeriod;
		}
		return true;
	}
}
