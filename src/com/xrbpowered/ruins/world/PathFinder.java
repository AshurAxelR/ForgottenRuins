package com.xrbpowered.ruins.world;

import java.util.LinkedList;

public class PathFinder {

	public static final int maxPathDist = 5000;
	
	public int activePathLayer = 0;
	private int editLayer = 1;
	
	public class Token {
		public int x, z, y;
		public Direction d = null;
		public int dist = 0;

		public Token(int x, int z, int y) {
			this.x = x;
			this.z = z;
			this.y = y;
		}

		public Token(int x, int z, int y, Direction d, int dist) {
			this.x = x;
			this.z = z;
			this.y = y;
			this.d = d;
			this.dist = dist;
		}
		
		public Token move(Direction d) {
			Tile tile = world.map[x][z][y];
			int dy = (tile.type==TileType.ramp && tile.rampDir==d) ? 1 : 0;
			return PathFinder.this.move(x+d.dx, z+d.dz, y+dy, d, dist-1);
		}
	}
	
	public final World world;
	
	private final LinkedList<Token> tokens = new LinkedList<>();
	
	public PathFinder(World world) {
		this.world = world;
	}
	
	public Token startTrace(int x, int z, int y) {
		return new Token(x, z, y);
	}
	
	public void clear(int layer) {
		for(int x=0; x<world.size; x++)
			for(int z=0; z<world.size; z++)
				for(int y=0; y<World.height; y++) {
					world.map[x][z][y].pathDir[layer] = null;
					world.map[x][z][y].pathDist[layer] = 0;
				}
	}
	
	public void clear() {
		clear(editLayer);
	}
	
	public void clearAll() {
		clear(0);
		clear(1);
	}
	
	private Token move(int x, int z, int y, Direction d, int dist) {
		if(x<0 || x>=world.size || z<0 || z>=world.size)
			return null;
		Token t = new Token(x, z, y, d, dist);
		Tile tile = world.map[t.x][t.z][t.y];
		if(tile.type==TileType.solid)
			return null;
		else if(tile.type==TileType.ramp)
			return t;
		else {
			Tile lower = world.map[t.x][t.z][t.y-1];
			if(lower.type==TileType.solid)
				return t;
			else if(lower.type==TileType.ramp && lower.rampDir==d.opposite()) {
				t.y--;
				return t;
			}
			else
				return null;
		}
	}
	
	private void processTokens(int maxDist) {
		while(!tokens.isEmpty()) {
			Token t = tokens.removeFirst();
			Tile tile = world.map[t.x][t.z][t.y];
			if(tile.pathDir[editLayer]==null) {
				tile.pathDir[editLayer] = t.d;
				tile.pathDist[editLayer] = maxDist-t.dist;
				if(t.dist>0) {
					for(Direction d : Direction.values()) {
						Token dt = t.move(d);
						if(dt!=null)
							tokens.add(dt);
					}
				}
			}
		}
	}
	
	public boolean canUpdate(int x0, int z0, int y0) {
		return y0>0 && world.isInside(x0, z0);
	}
	
	public void update(int x0, int z0, int y0, int maxDist) {
		if(!canUpdate(x0, z0, y0))
			return;
		tokens.add(new Token(x0, z0, y0, Direction.north, maxDist));
		processTokens(maxDist);
		tokens.clear();
		world.map[x0][z0][y0].pathDir[editLayer] = null;
	}

	public void flip() {
		activePathLayer = 1 - activePathLayer;
		editLayer = 1 - activePathLayer; 
	}
	
}
