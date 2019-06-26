package com.xrbpowered.ruins.world;

import java.util.ArrayList;

import com.xrbpowered.ruins.world.obj.Obelisk;

public class ObeliskSystem {

	public static final int maxObelisks = 8;
	
	public final World world;

	public ArrayList<Obelisk> obelisks = new ArrayList<>(); 
	
	public ObeliskSystem(World world) {
		this.world = world;
	}
	
	public void add(Obelisk obelisk) {
		obelisks.add(obelisk);
	}
	
	public int countVisited() {
		int count = 0;
		for(Obelisk o : obelisks)
			if(o.visited) count++;
		return count;
	}
	
	public int countTotal() {
		return obelisks.size();
	}
	
}
