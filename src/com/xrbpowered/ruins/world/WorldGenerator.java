package com.xrbpowered.ruins.world;

import java.util.LinkedList;
import java.util.Random;

import com.xrbpowered.ruins.world.obj.TileObject;

public class WorldGenerator {

	public static final int size = World.size;
	public static final int height = World.height;
	public static final int airReserve = 24;

	private class TileInfo {
		public boolean canHaveObject = false;
		public boolean preferBlock = false;
		public Token token = null;
	}
	
	private class ColInfo {
		public int height = -1;
		public boolean adjacent = false;
		//public boolean undefined = true;
	}
	
	private class Token {
		public int x, z, y;
		
		public Token(int x, int z, int y) {
			this.x = x;
			this.z = z;
			this.y = y;
			totalPlatforms++;
		}
	}

	public final World world;
	private final Tile[][][] map;
	private final Random random = new Random();

	private final TileInfo[][][] info;
	private final ColInfo[][] colInfo;
	private final LinkedList<Token> tokens;
	
	private int totalPlatforms = 0;

	public WorldGenerator(World world) {
		this.world = world;
		this.map = world.map;
		
		this.info = new TileInfo[size][size][height];
		for(int x=0; x<size; x++)
			for(int z=0; z<size; z++) {
				for(int y=0; y<height; y++) {
					info[x][z][y] = new TileInfo();
				}
			}
		
		this.colInfo = new ColInfo[size][size];
		for(int x=0; x<size; x++)
			for(int z=0; z<size; z++) {
				colInfo[x][z] = new ColInfo();
			}
		
		this.tokens = new LinkedList<>();
	}

	private void fillColumn(int x, int z, TileType type) {
		for(int y=0; y<height; y++) {
			map[x][z][y].type = type;
		}
	}


	private void reset(int x, int z, int y, Tile cell) {
		map[x][z][y] = cell;
	}
	
	private boolean matchSolid(TileType type, boolean solid) {
		return type==TileType.undefined || solid ^ type!=TileType.solid;
	}
	
	private boolean checkDiagonal(int x, int z, int y, int dx, int dz, boolean solid) {
		if(matchSolid(map[x+dx][z][y].type, solid) || matchSolid(map[x][z+dz][y].type, solid))
			return true;
		else
			return false; //map[x+dx][z+dz][y].type==CellType.undefined;
	}
	
	private Tile set(int x, int z, int y, TileType type) {
		Tile prev = map[x][z][y];
		if(prev.type==type)
			return prev;
		if(prev.type==TileType.undefined) {
			if(!checkDiagonal(x, z, y, -1, -1, type==TileType.solid) ||
					!checkDiagonal(x, z, y, -1, 1, type==TileType.solid) ||
					!checkDiagonal(x, z, y, 1, -1, type==TileType.solid) ||
					!checkDiagonal(x, z, y, 1, 1, type==TileType.solid))
				return null;
			map[x][z][y] = new Tile(type);
			return prev;
		}
		return null;
	}

	private Tile setRamp(int x, int z, int y, Direction rampd) {
		Tile prev = map[x][z][y];
		if(prev.type==TileType.ramp)
			return (prev.dir==rampd) ? prev : null;
		if(prev.type==TileType.undefined) {
			// TODO check ramp neighbours
			map[x][z][y] = new Tile(TileType.ramp);
			map[x][z][y].dir = rampd;
			return prev;
		}
		return null;
	}

	private boolean createPass(int x, int z, int y) {
		if(y<1 || y>=height-3)
			return false;
		Tile cb = set(x, z, y-1, TileType.solid);
		if(cb!=null) {
			Tile c0 = set(x, z, y+0, TileType.empty);
			if(c0!=null) {
				Tile c1 = set(x, z, y+1, TileType.empty);
				if(c1!=null) {
					Tile c2 = set(x, z, y+2, TileType.empty);
					if(c2!=null) {
						Token t = new Token(x, z, y);
						tokens.add(t);
						info[x][z][y].token = t;
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
		if(y<1 || y>=height-4)
			return false;
		Tile cb = set(x, z, y-1, TileType.solid);
		if(cb!=null) {
			Tile cr = setRamp(x, z, y+0, d);
			if(cr!=null) {
				Tile c0 = set(x, z, y+1, TileType.empty);
				if(c0!=null) {
					Tile c1 = set(x, z, y+2, TileType.empty);
					if(c1!=null) {
						Tile c2 = set(x, z, y+3, TileType.empty);
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
		if(y<2 || y>=height-3)
			return false;
		Tile cb = set(x, z, y-2, TileType.solid);
		if(cb!=null) {
			Tile cr = setRamp(x, z, y-1, d.opposite());
			if(cr!=null) {
				Tile c0 = set(x, z, y+0, TileType.empty);
				if(c0!=null) {
					Tile c1 = set(x, z, y+1, TileType.empty);
					if(c1!=null) {
						Tile c2 = set(x, z, y+2, TileType.empty);
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
		if(map[x][z][y].type!=TileType.undefined || info[x][z][y].preferBlock)
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
			info[x][z][y].preferBlock = true;
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
				info[t.x][t.z][t.y].canHaveObject = true;
		}
	}
	
	private boolean filterCanHaveObject(int x, int z, int y) {
		TileInfo tilei = info[x][z][y];
		if(!tilei.canHaveObject)
			return false;
		int passes = 0;
		int objects = 0;
		boolean ramp = false;
		Direction passd = null;
		boolean oppositePass = false;
		for(Direction d : Direction.values()) {
			Tile adj = map[x+d.dx][z+d.dz][y];
			TileInfo adji = info[x+d.dx][z+d.dz][y];
			boolean pass = false;
			if(adji.canHaveObject)
				objects++;
			if(adji.token!=null)
				pass = true;
			else if(adj.type==TileType.ramp && adj.dir==d) {
				pass = true;
				ramp = true;
			}
			else {
				adj = map[x+d.dx][z+d.dz][y-1];
				if(adj.type==TileType.ramp && adj.dir==d.opposite()) {
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
			if(!ramp && !(oppositePass && passes-objects<=2)) {
				boolean all = true;
				for(Direction d : Direction.values()) {
					Direction cw = d.cw();
					if(info[x+d.dx][z+d.dz][y].token!=null &&
							info[x+cw.dx][z+cw.dz][y].token!=null) {
						TileInfo diag = info[x+d.dx+cw.dx][z+d.dz+cw.dz][y];
						if(diag.token==null || diag.canHaveObject)
							all = false;
					}
				}
				if(all)
					return true;
			}
			return false;
		}
		return true;
	}
	
	private void filterCanHaveObjects() {
		for(int x=1; x<size-1; x++)
			for(int z=1; z<size-1; z++)
				for(int y=1; y<height-1; y++) {
					info[x][z][y].canHaveObject = filterCanHaveObject(x, z, y);
				}
	}

	private void generateObjects() {
		for(int x=1; x<size-1; x++)
			for(int z=1; z<size-1; z++)
				for(int y=1; y<height-1; y++) {
					if(info[x][z][y].canHaveObject) {
						if(colInfo[x][z].height<y) {
							world.tileObjects.add(new TileObject(world).setLocation(x, z, y));
						}
					}
				}
	}
	
	public boolean generate() {
		for(int i=0; i<airReserve; i++) {
			for(int j=i; j<size-i; j++) {
				for(int y=height-1; y>=height-(airReserve-i); y--) {
					map[i][j][y].type = TileType.empty;
					map[size-1-i][j][y].type = TileType.empty;
					map[j][i][y].type = TileType.empty;
					map[j][size-1-i][y].type = TileType.empty;
				}
			}
		}
		
		world.startx = size/2;
		world.startz = 1;
		for(int x=-2; x<=2; x++)
			fillColumn(world.startx+x, world.startz, TileType.empty);
		map[world.startx][world.startz][0].type = TileType.solid;
		//map[startx][startz][1].canHaveObject = true; // TODO add start object
		
		tokens.add(new Token(world.startx, world.startz, 1));
		processTokens(random);

		filterCanHaveObjects();

		for(int x=0; x<size; x++)
			for(int z=0; z<size; z++) {
				TileType type = TileType.empty;
				float light = 1f;
				for(int y=height-1; y>=0; y--) {
					if(map[x][z][y].type==TileType.undefined)
						map[x][z][y].type = type;
					else if(map[x][z][y].type==TileType.solid) {
						if(type!=TileType.solid) {
							type = TileType.solid;
							light = 0f;
							colInfo[x][z].height = y;
						}
					}
					map[x][z][y].light = light;
				}
				// colInfo[x][z].undefined = false;
			}
		
		generateObjects();
		
		int coverage = 0;
		int maxCoverage = size*size;
		for(int x=0; x<size; x++)
			for(int z=0; z<size; z++) {
				boolean adj = false;
				for(Direction d : Direction.values()) {
					if(!World.isInside(x+d.dx, z+d.dz))
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
		System.out.printf("Total platforms: %d, coverage: %d/%d (%.1f%%)\n",
				totalPlatforms, coverage, maxCoverage, coverage*100f/(float)maxCoverage);
		return coverage > maxCoverage/2;
	}

}
