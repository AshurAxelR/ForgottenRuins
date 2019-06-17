package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.world.World;

public class TileObject {

	public final World world;
	public int x, z, y;

	public TileObject(World world) {
		this.world = world;
	}
	
	public TileObject setLocation(int x, int z, int y) {
		this.x = x;
		this.z = z;
		this.y = y;
		world.map[x][z][y].tileObject = this;
		return this;
	}
	
}
