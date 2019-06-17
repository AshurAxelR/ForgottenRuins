package com.xrbpowered.ruins.entity;

import org.joml.Vector3f;

import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.Tile;
import com.xrbpowered.ruins.world.TileType;
import com.xrbpowered.ruins.world.World;

public class PlayerCollider {

	public float r = 0.3f;
	
	public World world = null;

	public int mapx(float x) {
		return Math.round(x/2f);
	}

	public int mapz(float z) {
		return Math.round(z/2f);
	}

	public int mapy(float y) {
		int my = Math.round(y);
		if(my<0)
			my = 0;
		return my;
	}
	
	private static Tile emptyCell = new Tile(TileType.empty);

	public Tile map(int mx, int mz, int my) {
		if(mx<0 || mx>=World.size || mz<0 || mz>=World.size || my>=World.height)
			return emptyCell;
		return world.map[mx][mz][my];
	}

	public float clipx(Vector3f pos, float vx, float dy) {
		float sv = Math.signum(vx);
		if(sv==0)
			return pos.x;
		int mx = mapx(pos.x+vx+r*sv);
		int mz = mapz(pos.z);
		int my = mapy(pos.y+dy);
		if(map(mx, mz, my).type==TileType.solid) {
			return mx*2f - sv*(1+r);
			//return Math.signum(pos.x+vx-2f*mx)*(1+r)+2f*mx;
		}
		else
			return pos.x + vx;
	}

	public float clipz(Vector3f pos, float vz, float dy) {
		float sv = Math.signum(vz);
		if(sv==0)
			return pos.z;
		int mx = mapx(pos.x);
		int mz = mapz(pos.z+vz+r*sv);
		int my = mapy(pos.y+dy);
		if(map(mx, mz, my).type==TileType.solid) {
			return mz*2f - sv*(1+r);
			//return Math.signum(pos.z+vz-2f*mz)*(1+r)+2f*mz;
		}
		else
			return pos.z + vz;
	}
	
	public float rampy(float x, float z, int mx, int mz, Direction rampd) {
		float sx = x - mx*2f;
		float sz = z - mz*2f;
		//System.out.printf("Ramp (%s): %.3f %.3f -> %.3f\n", rampd.name(), sx, sz, (sx*rampd.dx + sz*rampd.dz+1)*0.5f);
		return (sx*rampd.dx + sz*rampd.dz+1)*0.5f;
	}
	
	public boolean falling;
	
	public float clipy(Vector3f pos) {
		falling = false;
		int mx = mapx(pos.x);
		int mz = mapz(pos.z);
		int my = mapy(pos.y);
		if(my<=0)
			return 0; // TODO sink
		
		Tile cell = map(mx, mz, my);
		Tile celld = map(mx, mz, my-1);
		if(cell.type==TileType.ramp) {
			return my + rampy(pos.x, pos.z, mx, mz, cell.dir);
		}
		else if(celld.type==TileType.ramp) {
			return my-1 + rampy(pos.x, pos.z, mx, mz, celld.dir);
		}
		else if(celld.type==TileType.solid) {
			return my; 
		}
		else {
			falling = true;
			return pos.y;
		}
	}

}
