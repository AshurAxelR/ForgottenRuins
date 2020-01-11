package com.xrbpowered.ruins.world.gen;

import java.util.ArrayList;
import java.util.Random;

import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.ObeliskSystem;
import com.xrbpowered.ruins.world.TileType;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;
import com.xrbpowered.ruins.world.obj.Chest;
import com.xrbpowered.ruins.world.obj.DryWell;
import com.xrbpowered.ruins.world.obj.Obelisk;
import com.xrbpowered.ruins.world.obj.Palm;
import com.xrbpowered.ruins.world.obj.Portal;
import com.xrbpowered.ruins.world.obj.Shrine;
import com.xrbpowered.ruins.world.obj.StartLocation;
import com.xrbpowered.ruins.world.obj.Tablet;
import com.xrbpowered.ruins.world.obj.Well;

public class TileObjectGenerator {

	public final World world;
	
	public final WorldGenerator gen;
	public final ArrayList<Token> objTokens;
	
	public final SmallObjectGenerator smallObjects;
	private final Random random;
	
	public TileObjectGenerator(WorldGenerator gen, Random random) {
		this.smallObjects = new SmallObjectGenerator(gen, random);
		this.world = gen.world;
		this.random = random;
		this.gen = gen;
		this.objTokens = gen.objTokens;
	}

	private boolean isBottom(Token t) {
		return gen.colInfo[t.x][t.z].bottom==t.y;
	}
	
	private boolean isTop(Token t) {
		return gen.colInfo[t.x][t.z].height<t.y;
	}
	
	private boolean isAdjTop(Token t, int dy) {
		return isTop(t)
			&& gen.colInfo[t.x-1][t.z].height<t.y+dy
			&& gen.colInfo[t.x+1][t.z].height<t.y+dy
			&& gen.colInfo[t.x][t.z-1].height<t.y+dy
			&& gen.colInfo[t.x][t.z+1].height<t.y+dy;
	}
	
	private Direction dWall(Token t, int min, int max) {
		Direction wd = null;
		int wh = 0;
		Direction d = t.d;
		for(int i=0; i<4; i++) {
			int dy = 0;
			for(; dy<max; dy++) {
				if(world.map[t.x+d.dx][t.z+d.dz][t.y+dy].type!=TileType.solid)
					break;
			}
			if(dy>wh) {
				wh = dy;
				wd = d;
			}
			d = d.cw();
		}
		return (wh>=min) ? wd : null;
	}

	private int distToStart(Token t) {
		return (t.x-world.startx)+(t.z-world.startz)+(t.y-1); 
	}
	
	private void generateWell(int cx, int cz, int span) {
		int ix = random.nextInt(span);
		int jz = random.nextInt(span);
		for(int i=0; i<span; i++)
			for(int j=0; j<span; j++) {
				int x = cx+(ix+i) % span;
				int z = cz+(jz+j) % span;
				Token t = gen.info[x][z][gen.colInfo[x][z].bottom].objToken;
				if(t!=null) {
					new Well(world, t).place();
					objTokens.remove(t);
					return;
				}
			}
	}
	
	private void generateWells() {
		int span = Well.genSpan;
		for(int cx=0; cx<world.size; cx+=span)
			for(int cz=0; cz<world.size; cz+=span)
				generateWell(cx, cz, span);
		int extraWells = Well.countExtraWells(world.level);
		for(int count = 0; count<extraWells;) {
			Token t = objTokens.get(random.nextInt(objTokens.size()));
			if(isBottom(t)) {
				new Well(world, t).place();
				objTokens.remove(t);
				count++;
			}
		}
		int dryWellCount = DryWell.getCount(world.level);
		for(int count = 0; count<dryWellCount;) {
			Token t = objTokens.get(random.nextInt(objTokens.size()));
			if(isBottom(t) || t.y>3 && world.map[t.x][t.z][t.y-3].type==TileType.solid) {
				if(distToStart(t)>10) {
					new DryWell(world, t, count/(float)dryWellCount).place();
					objTokens.remove(t);
					count++;
				}
			}
		}
	}
	
	private void generatePortal() {
		int maxh = 0;
		for(int x=0; x<world.size; x++)
			for(int z=0; z<world.size; z++) {
				int h = gen.colInfo[x][z].height;
				if(h>maxh)
					maxh = h;
			}
		Token pt = null;
		for(;;) {
			for(Token t : objTokens) {
				if(t.y>=maxh+1) {
					pt = t;
					break;
				}
			}
			if(pt!=null) {
				new Portal(world.obelisks, pt).place();
				objTokens.remove(pt);
				return;
			}
			maxh--;
		}
	}

	private void generateRoyalChest() {
		Token ct = null;
		int min = world.size;
		for(Token t : objTokens) {
			int dist = Math.abs(t.x-world.size/2)+Math.abs(t.z-world.size/2)+t.y;
			if(dist<min) {
				ct = t;
				min = dist;
			}
		}
		if(ct!=null) {
			new Chest(world, ct, true).place();
			objTokens.remove(ct);
			if(world.level>0) {
				Token t = objTokens.get(random.nextInt(objTokens.size()));
				new Chest(world, t, false).addRoyalKey().place();
				objTokens.remove(t);
			}
		}
	}

	private void generateObelisks() {
		for(int count = 0; count<ObeliskSystem.obeliskCount;) {
			Token t = objTokens.get(random.nextInt(objTokens.size()));
			if(isAdjTop(t, 1)) {
				new Obelisk(world.obelisks, t).place();
				objTokens.remove(t);
				count++;
			}
		}
	}
	
	public boolean generateObjects(Token startToken) {
		try {
			new StartLocation(world, startToken).place();
			generateWells();
			generatePortal();
			generateObelisks();
			generateRoyalChest();
			
			for(Token t : objTokens) {
				if(random.nextInt(10)<4 && isAdjTop(t, 2)) {
					new Palm(world, t).place();
					continue;
				}
				if(random.nextInt(120)<1) {
					new Shrine(world, t).place();
					continue;
				}
				if(random.nextInt(12)<1) {
					new Chest(world, t, false).place();
					continue;
				}
				if(random.nextInt(10)<3) {
					Direction d = dWall(t, 2, 2);
					if(d!=null) {
						t.d = d;
						new Tablet(world, t).place();
						continue;
					}
				}
				if(random.nextInt(10)<6) {
					smallObjects.fillTile(t, 3, 0.5f);
					continue;
				}
				if(gen.getTile(t).light>0)
					smallObjects.fillTile(t, 1, 0.2f);
				else
					smallObjects.fillTile(t, -1, 0.75f);
			}
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
}
