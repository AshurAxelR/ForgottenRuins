package com.xrbpowered.ruins;

import java.util.LinkedList;
import java.util.Random;

public class WorldMap {

	public static final float gridw = 2f;
	public static final float gridh = 1f;

	public static final int passHeight = 3; // change createPass/Ramp if you change this
	
	public static final int size = 64;
	public static final int height = 32;
	
	public enum CellType {
		undefined, empty, solid, ramp
	}

	public enum Direction {
		north(0, -1), east(1, 0), south(0, 1), west(-1, 0);
		public final int dx, dz;
		private Direction(int dx, int dz) {
			this.dx = dx;
			this.dz = dz;
		}
		public Direction opposite() {
			switch(this) {
				case north: return south;
				case south: return north;
				case east: return west;
				case west: return east;
				default: return null;
			}
		}
	}
	
	public static class Cell {
		public CellType type;
		public Direction dir = null;
		public float light;
		
		public boolean preferBlock = false; // FIXME generator only
		
		public Cell(CellType type) {
			this.type = type;
		}
	}
	
	public final Cell[][][] map;
	public int startx, startz;
	
	public WorldMap() {
		map = new Cell[size][size][height];
		for(int x=0; x<size; x++)
			for(int z=0; z<size; z++) {
				CellType type = isEdge(x, z) ? CellType.empty : CellType.undefined; 
				for(int y=0; y<height; y++) {
					map[x][z][y] = new Cell(type);
				}
			}
	}
	
	public static boolean isEdge(int x, int z) {
		return (x==0 || z==0 || x==size-1 || z==size-1);
	}

	public static boolean isInside(int x, int z) {
		return (x>=0 && z>=0 && x<size && z<size);
	}

	private void fillColumn(int x, int z, CellType type) {
		for(int y=0; y<height; y++) {
			map[x][z][y].type = type;
		}
	}

	private class Token {
		int x, z, y;
		public Token(int x, int z, int y) {
			this.x = x;
			this.z = z;
			this.y = y;
			totalPasses++;
		}
	}
	
	private class ColInfo {
		public int height = -1;
		public boolean adjacent = false;
		//public boolean undefined = true;
	}
	
	private ColInfo[][] colInfo;
	private LinkedList<Token> tokens;
	private int totalPasses = 0;

	private void reset(int x, int z, int y, Cell cell) {
		map[x][z][y] = cell;
	}
	
	private boolean matchSolid(CellType type, boolean solid) {
		return type==CellType.undefined || solid ^ type!=CellType.solid;
	}
	
	private boolean checkDiagonal(int x, int z, int y, int dx, int dz, boolean solid) {
		if(matchSolid(map[x+dx][z][y].type, solid) || matchSolid(map[x][z+dz][y].type, solid))
			return true;
		else
			return false; //map[x+dx][z+dz][y].type==CellType.undefined;
	}
	
	private Cell set(int x, int z, int y, CellType type) {
		Cell prev = map[x][z][y];
		if(prev.type==type)
			return prev;
		if(prev.type==CellType.undefined) {
			if(!checkDiagonal(x, z, y, -1, -1, type==CellType.solid) ||
					!checkDiagonal(x, z, y, -1, 1, type==CellType.solid) ||
					!checkDiagonal(x, z, y, 1, -1, type==CellType.solid) ||
					!checkDiagonal(x, z, y, 1, 1, type==CellType.solid))
				return null;
			map[x][z][y] = new Cell(type);
			return prev;
		}
		return null;
	}

	private Cell setRamp(int x, int z, int y, Direction rampd) {
		Cell prev = map[x][z][y];
		if(prev.type==CellType.ramp)
			return (prev.dir==rampd) ? prev : null;
		if(prev.type==CellType.undefined) {
			// TODO check ramp neighbours
			map[x][z][y] = new Cell(CellType.ramp);
			map[x][z][y].dir = rampd;
			return prev;
		}
		return null;
	}

	private boolean createPass(int x, int z, int y) {
		if(y<1 || y>=height-passHeight)
			return false;
		Cell cb = set(x, z, y-1, CellType.solid);
		if(cb!=null) {
			Cell c0 = set(x, z, y+0, CellType.empty);
			if(c0!=null) {
				Cell c1 = set(x, z, y+1, CellType.empty);
				if(c1!=null) {
					Cell c2 = set(x, z, y+2, CellType.empty);
					if(c2!=null) {
						tokens.add(new Token(x, z, y));
						return true;
					}
					reset(x, z, y+1, c1);
				}
				reset(x, z, y+0, c0);
			}
			reset(x, z, y-1, cb);
		}
		return false;
	}

	private boolean createRampUp(Random random, int x, int z, int y, Direction d) {
		if(y<1 || y>=height-passHeight-1)
			return false;
		Cell cb = set(x, z, y-1, CellType.solid);
		if(cb!=null) {
			Cell cr = setRamp(x, z, y+0, d);
			if(cr!=null) {
				Cell c0 = set(x, z, y+1, CellType.empty);
				if(c0!=null) {
					Cell c1 = set(x, z, y+2, CellType.empty);
					if(c1!=null) {
						Cell c2 = set(x, z, y+3, CellType.empty);
						if(c2!=null) {
							if(random.nextInt(6)==0 && createRampUp(random, x+d.dx, z+d.dz, y+1, d))
								return true;
							if(createPass(x+d.dx, z+d.dz, y+1))
								return true;
							reset(x, z, y+3, c2);
						}
						reset(x, z, y+2, c1);
					}
					reset(x, z, y+1, c0);
				}
				reset(x, z, y+0, cr);
			}
			reset(x, z, y-1, cb);
		}
		return false;
	}

	private boolean createRampDown(Random random, int x, int z, int y, Direction d) {
		if(y<2 || y>=height-passHeight)
			return false;
		Cell cb = set(x, z, y-2, CellType.solid);
		if(cb!=null) {
			Cell cr = setRamp(x, z, y-1, d.opposite());
			if(cr!=null) {
				Cell c0 = set(x, z, y+0, CellType.empty);
				if(c0!=null) {
					Cell c1 = set(x, z, y+1, CellType.empty);
					if(c1!=null) {
						Cell c2 = set(x, z, y+2, CellType.empty);
						if(c2!=null) {
							if(random.nextInt(6)==0 && createRampDown(random, x+d.dx, z+d.dz, y-1, d))
								return true;
							if(createPass(x+d.dx, z+d.dz, y-1))
								return true;
							reset(x, z, y+2, c2);
						}
						reset(x, z, y+1, c1);
					}
					reset(x, z, y+0, c0);
				}
				reset(x, z, y-1, cr);
			}
			reset(x, z, y-2, cb);
		}
		return false;
	}

	private boolean createRamp(Random random, int x, int z, int y, Direction d, boolean up) {
		if(up)
			return createRampUp(random, x, z, y, d);
		else
			return createRampDown(random, x, z, y, d);
	}
	
	private void createRandom(Random random, int x, int z, int y, Direction d) {
		if(map[x][z][y].type!=CellType.undefined || map[x][z][y].preferBlock)
			return;
		if(x==0 || z==0 || x==size-1 || z==size-1)
			return;
		int r = random.nextInt(6);
		if(r==0) {
			boolean up = random.nextInt(7)<4;
			if(createRamp(random, x, z, y, d, up))
				return;
			if(createRamp(random, x, z, y, d, !up))
				return;
		}
		if(r<3 || tokens.isEmpty()){
			if(createPass(x, z, y))
				return;
		}
		if(r==3) {
			map[x][z][y].preferBlock = true;
			return;
		}
	}
	
	private void processTokens(Random random) {
		while(!tokens.isEmpty()) {
			Token t = random.nextInt(3)==0 ? tokens.removeLast() : tokens.removeFirst();
			for(Direction d : Direction.values()) {
				createRandom(random, t.x+d.dx, t.z+d.dz, t.y, d);
			}
		}
	}
	
	public void generate(Random random) {
		colInfo = new ColInfo[size][size];
		for(int x=0; x<size; x++)
			for(int z=0; z<size; z++) {
				colInfo[x][z] = new ColInfo();
			}
		
		startx = size/2;
		startz = 1;
		for(int x=-2; x<=2; x++)
			fillColumn(startx+x, startz, CellType.empty);
		map[startx][startz][0].type = CellType.solid;
		
		tokens = new LinkedList<>();
		tokens.add(new Token(startx, startz, 1));
		processTokens(random);
		
		for(int x=0; x<size; x++)
			for(int z=0; z<size; z++) {
				CellType type = CellType.empty;
				float light = 1f;
				for(int y=height-1; y>=0; y--) {
					if(map[x][z][y].type==CellType.undefined)
						map[x][z][y].type = type;
					else if(map[x][z][y].type==CellType.solid) {
						type = CellType.solid;
						light = 0f;
						colInfo[x][z].height = y;
					}
					map[x][z][y].light = light;
				}
				// colInfo[x][z].undefined = false;
			}
		
		int coverage = 0;
		int maxCoverage = size*size;
		for(int x=0; x<size; x++)
			for(int z=0; z<size; z++) {
				boolean adj = false;
				for(Direction d : Direction.values()) {
					if(!isInside(x+d.dx, z+d.dz))
						continue;
					if(colInfo[x+d.dx][z+d.dz].height>=0) {
						adj = true;
						break;
					}
				}
				colInfo[x][z].adjacent = adj;
				if(colInfo[x][z].adjacent)
					coverage++;
			}
		System.out.printf("Total passes: %d, coverage: %d/%d (%.1f%%)\n", totalPasses, coverage, maxCoverage, coverage*100f/(float)maxCoverage);
	}
}
