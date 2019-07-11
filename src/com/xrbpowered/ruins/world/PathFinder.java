package com.xrbpowered.ruins.world;

import java.util.LinkedList;

public class PathFinder {

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
	
	public void clear() {
		for(int x=0; x<World.size; x++)
			for(int z=0; z<World.size; z++)
				for(int y=0; y<World.height; y++) {
					world.map[x][z][y].pathDir = null;
				}
	}
	
	private Token move(int x, int z, int y, Direction d, int dist) {
		if(x<0 || x>=World.size || z<0 || z>=World.size)
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
	
	private void processTokens() {
		while(!tokens.isEmpty()) {
			Token t = tokens.removeFirst();
			Tile tile = world.map[t.x][t.z][t.y];
			if(tile.pathDir==null) {
				tile.pathDir = t.d;
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
	
	public void update(int x0, int z0, int y0, int maxDist) {
		if(y0<=0)
			return;
		tokens.add(new Token(x0, z0, y0, Direction.north, maxDist));
		processTokens();
		tokens.clear();
		world.map[x0][z0][y0].pathDir = null;
	}

}
