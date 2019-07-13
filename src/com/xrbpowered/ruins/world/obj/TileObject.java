package com.xrbpowered.ruins.world.obj;

import org.joml.Vector3f;

import com.xrbpowered.gl.scene.Actor;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator;

public abstract class TileObject extends MapObject {

	public int x, z, y;
	public Direction d;
	public long seed;
	
	public TileObject(World world, WorldGenerator.Token objToken) {
		super(world);
		this.x = objToken.x;
		this.z = objToken.z;
		this.y = objToken.y;
		this.d = objToken.d;
		this.seed = seedXZY(world.seed+58932, x, z, y);
		
		position = new Vector3f(x*2f, y, z*2f);
	}
	
	@Override
	public void place() {
		super.place();
		world.map[x][z][y].tileObject = this;
	}
	
	@Override
	public void copyToActor(Actor actor) {
		actor.rotation.y = -d.rotation();
		super.copyToActor(actor);
	}
	
	@Override
	public void addPrefabInstance() {
		Prefab prefab = getPrefab();
		if(prefab!=null)
			prefab.addInstance(world, this);
	}
	
	private static long nextSeed(long seed, long add) {
		// Multiply by Knuth's Random (Linear congruential generator) and add offset
		seed *= seed * 6364136223846793005L + 1442695040888963407L;
		seed += add;
		return seed;
	}

	private static long seedXZY(long seed, long x, long z, long y) {
		seed = nextSeed(seed, x);
		seed = nextSeed(seed, z);
		seed = nextSeed(seed, y);
		seed = nextSeed(seed, x);
		seed = nextSeed(seed, z);
		seed = nextSeed(seed, y);
		return seed;
	}
}
