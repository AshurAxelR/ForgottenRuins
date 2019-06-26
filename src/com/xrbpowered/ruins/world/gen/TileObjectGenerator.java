package com.xrbpowered.ruins.world.gen;

import java.util.ArrayList;
import java.util.Random;

import com.xrbpowered.ruins.world.ObeliskSystem;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;
import com.xrbpowered.ruins.world.obj.Obelisk;
import com.xrbpowered.ruins.world.obj.Palm;
import com.xrbpowered.ruins.world.obj.TileObject;

public class TileObjectGenerator {

	public final World world;
	public final ArrayList<TileObject> tileObjects; 
	
	public final WorldGenerator gen;
	public final ArrayList<Token> objTokens;
	
	public TileObjectGenerator(WorldGenerator gen) {
		this.world = gen.world;
		this.tileObjects = world.tileObjects;
		this.gen = gen;
		this.objTokens = gen.objTokens;
	}

	private boolean isTop(Token t) {
		return gen.colInfo[t.x][t.z].height<t.y;
	}
	
	private boolean isAdjTop(Token t) {
		return isTop(t)
			&& gen.colInfo[t.x-1][t.z].height<t.y+2
			&& gen.colInfo[t.x+1][t.z].height<t.y+2
			&& gen.colInfo[t.x][t.z-1].height<t.y+2
			&& gen.colInfo[t.x][t.z+1].height<t.y+2;
	}
	
	public void generateObjects(Random random) {
		ObeliskSystem obelisks = new ObeliskSystem(gen.world);
		for(int obeliskCount = 0; obeliskCount<ObeliskSystem.maxObelisks;) {
			Token t = objTokens.get(random.nextInt(objTokens.size()));
			if(isAdjTop(t)) {
				gen.world.tileObjects.add(new Obelisk(obelisks, t));
				objTokens.remove(t);
				obeliskCount++;
			}
		}
		for(Token t : objTokens) {
			if(random.nextInt(10)<4 && isAdjTop(t))
				gen.world.tileObjects.add(new Palm(world, t));
			else
				world.map[t.x][t.z][t.y].canHaveObject = true;
		}
	}
	
}
