package com.xrbpowered.ruins.world;

import java.util.ArrayList;

import com.xrbpowered.ruins.world.obj.Obelisk;

public class ObeliskSystem {

	public static final int maxObelisks = 8;
	public static int[] dx = {0, -1, 1, -1, 1, -1, 1, 0};
	public static int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};

	
	public final World world;

	public ArrayList<Obelisk> obelisks = new ArrayList<>();
	public int remaining;
	
	public ObeliskSystem(World world) {
		this.world = world;
		this.remaining = 0;
	}
	
	public void add(Obelisk obelisk) {
		obelisks.add(obelisk);
		remaining++;
	}
	
	public boolean visit(Obelisk obelisk) {
		if(!obelisk.visited) {
			obelisk.visited = true;
			remaining--;
			return true;
		}
		else
			return false;
	}
	
}
