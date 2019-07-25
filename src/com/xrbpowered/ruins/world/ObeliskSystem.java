package com.xrbpowered.ruins.world;

import java.util.ArrayList;

import com.xrbpowered.ruins.world.obj.Obelisk;
import com.xrbpowered.ruins.world.obj.Portal;

public class ObeliskSystem {

	public static final int obeliskCount = 8;
	public static int[] dx = {0, -1, 1, -1, 1, -1, 1, 0};
	public static int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};

	
	public final World world;

	public ArrayList<Obelisk> obelisks = new ArrayList<>();
	public Portal portal = null;
	public int remaining;
	
	public ObeliskSystem(World world) {
		this.world = world;
		this.remaining = 0;
	}
	
	public void add(Obelisk obelisk) {
		obelisks.add(obelisk);
		remaining++;
	}
	
	public void setPortal(Portal portal) {
		this.portal = portal;
	}
	
	public boolean visit(Obelisk obelisk) {
		if(!obelisk.visited) {
			obelisk.visited = true;
			remaining--;
			if(remaining<=0 && portal!=null)
				portal.active = true;
			return true;
		}
		else
			return false;
	}
	
}
