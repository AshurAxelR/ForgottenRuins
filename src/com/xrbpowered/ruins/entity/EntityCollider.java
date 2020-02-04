package com.xrbpowered.ruins.entity;

import org.joml.Vector3f;

import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.Tile;
import com.xrbpowered.ruins.world.TileType;
import com.xrbpowered.ruins.world.World;

public class EntityCollider {

	private static Tile emptyCell = new Tile(TileType.empty);

	public final EntityActor entity;
	private float r, height;
	private float[] dyPoints;

	public boolean hitx, hitz, hitTop;
	public boolean falling;

	public EntityCollider(EntityActor entity) {
		this.entity = entity;
		entity.setEntityDimensions(this);
	}
	
	public void setEntityDimensions(float r, float h, float[] dyPoints) {
		this.r = r;
		this.height = h;
		this.dyPoints = dyPoints;
	}

	public Tile map(int mx, int mz, int my) {
		if(mx<0 || mx>=entity.world.size || mz<0 || mz>=entity.world.size || my>=World.height)
			return emptyCell;
		return entity.world.map[mx][mz][my];
	}

	public float clipx(Vector3f pos, float vx, float dy) {
		float sv = Math.signum(vx);
		if(sv==0)
			return pos.x;
		float x = pos.x+vx+r*sv;
		int mx = World.mapx(x);
		int mz = World.mapz(pos.z);
		int my = World.mapy(pos.y+dy);
		Tile tile = map(mx, mz, my);
		hitx = tile.type==TileType.solid;
		if(hitx)
			return mx*2f - sv*(1+r);
		if(tile.collisionObject!=null) {
			hitx = tile.collisionObject.testxz(x, pos.z);
			if(hitx)
				return tile.collisionObject.clipx(sv, r);
		}
		return pos.x + vx;
	}

	public float clipz(Vector3f pos, float vz, float dy) {
		float sv = Math.signum(vz);
		if(sv==0)
			return pos.z;
		float z = pos.z+vz+r*sv;
		int mx = World.mapx(pos.x);
		int mz = World.mapz(z);
		int my = World.mapy(pos.y+dy);
		Tile tile = map(mx, mz, my);
		hitz = tile.type==TileType.solid; 
		if(hitz)
			return mz*2f - sv*(1+r);
		if(tile.collisionObject!=null) {
			hitz = tile.collisionObject.testxz(pos.x, z);
			if(hitz)
				return tile.collisionObject.clipz(sv, r);
		}
		return pos.z + vz;
	}
	
	private static final Vector3f v = new Vector3f();

	public void clipxz(Vector3f velocity, Vector3f position) {
		v.set(velocity);
		for(float dy : dyPoints) {
			float nx = clipx(position, v.x, dy);
			float nz = clipz(position, v.z, dy);
			v.x = nx - position.x;
			v.z = nz - position.z;
			if(hitx && hitz) {
				// FIXME corner collision
			}
		}
		position.x += v.x;
		position.z += v.z;
	}
	
	public float rampy(float x, float z, int mx, int mz, Direction rampd) {
		float sx = x - mx*2f;
		float sz = z - mz*2f;
		return (sx*rampd.dx + sz*rampd.dz+1)*0.5f;
	}
	
	public float clipyTop(Vector3f pos, float vy) {
		int mx = World.mapx(pos.x);
		int mz = World.mapz(pos.z);
		int my = World.mapy(pos.y+height+vy);
		Tile cell = map(mx, mz, my);
		hitTop = cell.type==TileType.solid;
		if(hitTop)
			return my-1.01f-height;
		else
			return pos.y + vy;
	}

	public float clipy(Vector3f pos) {
		falling = false;
		int mx = World.mapx(pos.x);
		int mz = World.mapz(pos.z);
		int my = World.mapy(pos.y);
		if(my<=0)
			return 0;
		
		Tile cell = map(mx, mz, my);
		Tile celld = map(mx, mz, my-1);
		if(cell.type==TileType.ramp) {
			return my + rampy(pos.x, pos.z, mx, mz, cell.rampDir);
		}
		else if(celld.type==TileType.ramp) {
			return my-1 + rampy(pos.x, pos.z, mx, mz, celld.rampDir);
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
