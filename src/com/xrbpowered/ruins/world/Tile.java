package com.xrbpowered.ruins.world;

import java.util.ArrayList;

import com.xrbpowered.ruins.world.obj.SmallObject;
import com.xrbpowered.ruins.world.obj.TileObject;

public class Tile {
	public TileType type;
	public Direction dir = null;
	public float light;
	
	public boolean canHaveObject = false;
	public TileObject tileObject = null;
	public ArrayList<SmallObject> smallObjects = null;
	
	public Tile(TileType type) {
		this.type = type;
	}
	
	public void addSmallObject(SmallObject obj) {
		if(smallObjects==null)
			smallObjects = new ArrayList<>();
		smallObjects.add(obj);
	}
}