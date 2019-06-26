package com.xrbpowered.ruins.world.obj;

import org.joml.Vector3f;

import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator;

public abstract class TileObject {

	public final World world;
	public int x, z, y;
	public Direction d;
	public long seed;
	
	public Vector3f position;
	
	public int intractionComponentIndex = -1; 

	public TileObject(World world, WorldGenerator.Token objToken) {
		this.world = world;
		this.x = objToken.x;
		this.z = objToken.z;
		this.y = objToken.y;
		this.d = objToken.d;
		world.map[x][z][y].tileObject = this;
		
		position = new Vector3f(x*2f, y, z*2f);
	}
	
	public abstract Prefab getPrefab();
	
	public void interact() {
	}
	
}
