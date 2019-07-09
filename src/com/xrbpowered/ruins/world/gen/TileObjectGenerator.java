package com.xrbpowered.ruins.world.gen;

import java.util.ArrayList;
import java.util.Random;

import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.ObeliskSystem;
import com.xrbpowered.ruins.world.TileType;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;
import com.xrbpowered.ruins.world.obj.MapObject;
import com.xrbpowered.ruins.world.obj.Obelisk;
import com.xrbpowered.ruins.world.obj.Palm;
import com.xrbpowered.ruins.world.obj.Tablet;
import com.xrbpowered.ruins.world.obj.Well;

public class TileObjectGenerator {

	public final World world;
	public final ArrayList<MapObject> objects; 
	
	public final WorldGenerator gen;
	public final ArrayList<Token> objTokens;
	
	public final SmallObjectGenerator smallObjects;
	private final Random random;
	
	public TileObjectGenerator(WorldGenerator gen, Random random) {
		this.smallObjects = new SmallObjectGenerator(gen, random);
		this.world = gen.world;
		this.random = random;
		this.objects = world.objects;
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

	private void generateWell(int cx, int cz, int span) {
		int ix = random.nextInt(span);
		int jz = random.nextInt(span);
		for(int i=0; i<span; i++)
			for(int j=0; j<span; j++) {
				int x = cx+(ix+i) % span;
				int z = cz+(jz+j) % span;
				Token t = gen.info[x][z][gen.colInfo[x][z].bottom].objToken;
				if(t!=null) {
					objects.add(new Well(world, t));
					objTokens.remove(t);
					return;
				}
			}
	}
	
	private void generateWells() {
		int span = Well.genSpan;
		for(int cx=0; cx<World.size; cx+=span)
			for(int cz=0; cz<World.size; cz+=span)
				generateWell(cx, cz, span);
		for(int count = 0; count<Well.extraWells;) {
			Token t = objTokens.get(random.nextInt(objTokens.size()));
			if(isBottom(t)) {
				objects.add(new Well(world, t));
				objTokens.remove(t);
				count++;
			}
		}
	}
	
	private void generateObelisks() {
		for(int count = 0; count<ObeliskSystem.maxObelisks;) {
			Token t = objTokens.get(random.nextInt(objTokens.size()));
			if(isAdjTop(t, 1)) {
				objects.add(new Obelisk(world.obelisks, t));
				objTokens.remove(t);
				count++;
			}
		}
	}
	
	public void generateObjects() {
		generateWells();
		generateObelisks();
		
		for(Token t : objTokens) {
			if(random.nextInt(10)<4 && isAdjTop(t, 2)) {
				objects.add(new Palm(world, t));
				continue;
			}
			if(random.nextInt(10)<3) {
				Direction d = dWall(t, 2, 2);
				if(d!=null) {
					t.d = d;
					objects.add(new Tablet(world, t));
					continue;
				}
			}
			if(random.nextInt(10)<6) {
				smallObjects.fillTile(t);
				continue;
			}
			world.map[t.x][t.z][t.y].canHaveObject = true;
		}
	}
	
}
