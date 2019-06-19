package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.WorldGenerator;

public abstract class TileObject {

	public final World world;
	public int x, z, y;
	public Direction d;
	public long seed;

	public TileObject(World world, WorldGenerator.Token objToken) {
		this.world = world;
		this.x = objToken.x;
		this.z = objToken.z;
		this.y = objToken.y;
		this.d = objToken.d;
		world.map[x][z][y].tileObject = this;
	}
	
	public abstract Prefab getPrefab();
	
}
