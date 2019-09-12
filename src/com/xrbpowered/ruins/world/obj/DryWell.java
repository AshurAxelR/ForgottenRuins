package com.xrbpowered.ruins.world.obj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import com.xrbpowered.ruins.entity.WorldEntity;
import com.xrbpowered.ruins.entity.mob.Ghost;
import com.xrbpowered.ruins.render.effect.particle.Particle;
import com.xrbpowered.ruins.render.effect.particle.ParticleEffect;
import com.xrbpowered.ruins.render.effect.particle.ParticleGenerator;
import com.xrbpowered.ruins.render.effect.particle.ParticleRenderer;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.DifficultyMode;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class DryWell extends TileObject implements WorldEntity {

	public static final float maxInitialSpawnDelay = 60f;
	public static final float spawnPeriod = 15f;
	public static final float triggerTime = 10f;

	public static final int[] baseCount = {1, 1, 1, 2, 3, 4, 5, 6, 6, 6};

	public static int getCount(int level) {
		return World.baseSize[level]*World.baseSize[level]*baseCount[level];
	}
	
	private float timeToSpawn;
	private float timeToArm;
	private boolean armed = false;
	
	private Ghost ghost = null;
	private int ghostId = -1;
	
	private Random random = new Random();
	
	private ParticleGenerator generator = new ParticleGenerator(effect, 0.02f, 0.04f); 
	
	public DryWell(World world, Token objToken, float spawnDelay) {
		super(world, objToken);
		random.setSeed(this.seed);
		timeToArm = spawnDelay*maxInitialSpawnDelay;
		timeToSpawn = 0f;
	}
	
	@Override
	public void loadState(DataInputStream in) throws IOException {
		timeToSpawn = in.readFloat();
		timeToArm = in.readFloat();
		armed = in.readBoolean();
		ghostId = in.readInt();
	}
	
	@Override
	public void saveState(DataOutputStream out) throws IOException {
		out.writeFloat(timeToSpawn);
		out.writeFloat(timeToArm);
		out.writeBoolean(armed);
		out.writeInt(ghost==null ? -1 : world.mobs.indexOf(ghost));
	}

	@Override
	public Prefab getPrefab() {
		return PrefabRenderer.dryWell;
	}

	public float getTriggerDistance() {
		return 2f*(6+2*world.level);
	}
	
	@Override
	public boolean updateTime(float dt) {
		if(world.difficulty==DifficultyMode.peaceful)
			return false;
		
		if(ghostId>=0) {
			ghost = (Ghost) world.mobs.get(ghostId);
			ghostId = -1;
		}
		
		if(ghost==null) {
			if(timeToSpawn>0f) {
				effect.pivot.set(position);
				effect.pivot.y += 0.5;
				float s = (timeToSpawn / triggerTime);
				generator.setDelay(s*s*0.25f + 0.02f);
				generator.update(dt);
				
				timeToSpawn -= dt;
				if(timeToSpawn<0f) {
					ghost = (Ghost) new Ghost(world, random).spawn(x, z, y, d);
					armed = false;
					timeToSpawn = 0f;
				}
			}
			else if(armed) {
				float dist = position.distance(world.player.position);
				if(dist<getTriggerDistance()) {
					armed = false;
					timeToSpawn = triggerTime; 
				}
			}
			else {
				timeToArm -= dt;
				if(timeToArm<0f) {
					armed = true;
					timeToSpawn = 0f;
				}
			}
		}
		else if(ghost!=null && (!ghost.alive || ghost.time>=ghost.lifespan)) {
			ghost = null;
			armed = false;
			timeToArm = spawnPeriod;
			timeToSpawn = 0f;
		}
		return true;
	}
	
	public static ParticleEffect effect = new ParticleEffect.Up(0.5f, 0.5f, 0f) {
		@Override
		public void generateParticle() {
			Particle p = new Ghost.DotParticle();
			assign(p);
			ParticleRenderer.dark.add(p);
		}
		@Override
		public void generate() {
			generate(1);
		}
	}.setSpeed(1f, 3f);
}
