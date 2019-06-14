package com.xrbpowered.ruins;

import java.util.LinkedList;
import java.util.Random;

public class WorldMap {

	public static final int passHeight = 3; // change createPass/Ramp if you change this
	
	public static final int size = 64;
	public static final int height = 32;

	public static final int airReserve = 24;

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
			return values()[(ordinal()+2) % 4];
		}
		public Direction cw() {
			return values()[(ordinal()+1) % 4];
		}
		public Direction ccw() {
			return values()[(ordinal()+3) % 4];
		}
	}
	
	public static class Cell {
		public CellType type;
		public Direction dir = null;
		public float light;
		public boolean canHaveObject = false;
		
		public boolean preferBlock = false; // FIXME generator only
		public Token token = null;
		
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
		public int x, z, y;
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
						Token t = new Token(x, z, y);
						tokens.add(t);
						map[x][z][y].token = t;
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
							if(random.nextInt(5)==0 && createRampUp(random, x+d.dx, z+d.dz, y+1, d))
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
							if(random.nextInt(5)==0 && createRampDown(random, x+d.dx, z+d.dz, y-1, d))
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
	
	private boolean createRandom(Random random, int x, int z, int y, Direction d) {
		if(map[x][z][y].type!=CellType.undefined || map[x][z][y].preferBlock)
			return false;
		if(x==0 || z==0 || x==size-1 || z==size-1)
			return false;
		int r = random.nextInt(7);
		if(r==0) {
			boolean up = random.nextInt(7)<4;
			if(createRamp(random, x, z, y, d, up))
				return true;
			if(createRamp(random, x, z, y, d, !up))
				return true;
		}
		if(r<4 || tokens.isEmpty()){
			if(createPass(x, z, y))
				return true;
		}
		if(r==4) {
			map[x][z][y].preferBlock = true;
			return false;
		}
		return false;
	}
	
	private void processTokens(Random random) {
		while(!tokens.isEmpty()) {
			Token t = random.nextInt(3)==0 ? tokens.removeLast() : tokens.removeFirst();
			boolean pass = false;
			for(Direction d : Direction.values()) {
				pass |= createRandom(random, t.x+d.dx, t.z+d.dz, t.y, d);
			}
			if(!pass)
				map[t.x][t.z][t.y].canHaveObject = true;
		}
	}
	
	private void filterCanHaveObjects() {
		for(int x=1; x<size-1; x++)
			for(int z=1; z<size-1; z++)
				for(int y=1; y<height-1; y++) {
					Cell cell = map[x][z][y];
					if(!cell.canHaveObject)
						continue;
					int passes = 0;
					int objects = 0;
					boolean ramp = false;
					Direction passd = null;
					boolean oppositePass = false;
					for(Direction d : Direction.values()) {
						Cell adj = map[x+d.dx][z+d.dz][y];
						boolean pass = false;
						if(adj.canHaveObject)
							objects++;
						if(adj.token!=null)
							pass = true;
						else if(adj.type==CellType.ramp && adj.dir==d) {
							pass = true;
							ramp = true;
						}
						else {
							adj = map[x+d.dx][z+d.dz][y-1];
							if(adj.type==CellType.ramp && adj.dir==d.opposite()) {
								pass = true;
								ramp = true;
							}
						}
						if(pass) {
							passes++;
							if(passd==d.opposite())
								oppositePass = true;
							passd = d;
						}
					}
					if(passes>1) {
						if(!ramp && !(oppositePass && passes-objects==2)) {
							boolean all = true;
							for(Direction d : Direction.values()) {
								Direction cw = d.cw();
								if(map[x+d.dx][z+d.dz][y].token!=null &&
										map[x+cw.dx][z+cw.dz][y].token!=null) {
									Cell diag = map[x+d.dx+cw.dx][z+d.dz+cw.dz][y];
									if(diag.token==null || diag.canHaveObject)
										all = false;
								}
							}
							if(all) {
								cell.canHaveObject = true;
								continue;
							}
						}
						cell.canHaveObject = false;
						continue;
					}
					else {
						cell.canHaveObject = true;
					}
				}
	}
	
	public void generate(Random random) {
		colInfo = new ColInfo[size][size];
		for(int x=0; x<size; x++)
			for(int z=0; z<size; z++) {
				colInfo[x][z] = new ColInfo();
			}
		for(int i=0; i<airReserve; i++) {
			for(int j=i; j<size-i; j++) {
				for(int y=height-1; y>=height-(airReserve-i); y--) {
					map[i][j][y].type = CellType.empty;
					map[size-1-i][j][y].type = CellType.empty;
					map[j][i][y].type = CellType.empty;
					map[j][size-1-i][y].type = CellType.empty;
				}
			}
		}
		
		startx = size/2;
		startz = 1;
		for(int x=-2; x<=2; x++)
			fillColumn(startx+x, startz, CellType.empty);
		map[startx][startz][0].type = CellType.solid;
		map[startx][startz][1].canHaveObject = true;
		
		tokens = new LinkedList<>();
		tokens.add(new Token(startx, startz, 1));
		processTokens(random);

		filterCanHaveObjects();

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
