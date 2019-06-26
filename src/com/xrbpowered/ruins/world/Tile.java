package com.xrbpowered.ruins.world;

import com.xrbpowered.ruins.world.obj.TileObject;

public class Tile {
	public TileType type;
	public Direction dir = null;
	public float light;
	
	public boolean canHaveObject = false;
	public TileObject tileObject = null;
	
	public Tile(TileType type) {
		this.type = type;
	}
}