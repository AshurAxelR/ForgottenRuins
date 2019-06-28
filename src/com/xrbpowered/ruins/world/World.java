package com.xrbpowered.ruins.world;

import java.util.ArrayList;

import com.xrbpowered.ruins.entity.PlayerActor;
import com.xrbpowered.ruins.world.gen.WorldGenerator;
import com.xrbpowered.ruins.world.obj.TileObject;

public class World {

	public static final int size = 64;
	public static final int height = 32;

	public final long seed;
	public final Tile[][][] map;
	public int startx, startz;
	
	public ObeliskSystem obelisks;
	
	public final ArrayList<TileObject> tileObjects = new ArrayList<>();
	
	public final PlayerActor player;
	
	public World(long seed, PlayerActor player) {
		this.seed = seed;
		this.player = player;
		map = new Tile[size][size][height];
		for(int x=0; x<size; x++)
			for(int z=0; z<size; z++) {
				TileType type = isEdge(x, z) ? TileType.empty : TileType.undefined; 
				for(int y=0; y<height; y++) {
					map[x][z][y] = new Tile(type);
				}
			}
	}
	
	public static boolean isEdge(int x, int z) {
		return (x==0 || z==0 || x==size-1 || z==size-1);
	}

	public static boolean isInside(int x, int z) {
		return (x>=0 && z>=0 && x<size && z<size);
	}

	public static World createWorld(long seed, PlayerActor player) {
		for(;;) {
			World world = new World(seed, player);
			WorldGenerator gen = new WorldGenerator(world);
			if(gen.generate())
				return world;
			else
				seed = gen.nextAttempt();
		}
	}
	
}
