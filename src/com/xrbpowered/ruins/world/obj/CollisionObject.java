package com.xrbpowered.ruins.world.obj;

public class CollisionObject {

	public boolean active = true;
	
	public final float minx, maxx, minz, maxz;
	
	public CollisionObject(float minx, float maxx, float minz, float maxz) {
		this.minx = Math.min(minx, maxx);
		this.maxx = Math.max(minx, maxx);
		this.minz = Math.min(minz, maxz);
		this.maxz = Math.max(minz, maxz);
	}
	
	public boolean testxz(float x, float z) {
		return active && x>=minx && x<=maxx && z>=minz && z<=maxz;
	}

	public float clipx(float sv, float r) {
		return sv>0 ? minx-r : maxx+r;
	}

	public float clipz(float sv, float r) {
		return sv>0 ? minz-r : maxz+r;
	}

	public CollisionObject place(TileObject obj) {
		return new CollisionObject(
				obj.x*2f + minx*obj.d.dx + minz*obj.d.dz,
				obj.x*2f + maxx*obj.d.dx + maxz*obj.d.dz,
				obj.z*2f + minz*obj.d.dx + minx*obj.d.dz,
				obj.z*2f + maxz*obj.d.dx + maxx*obj.d.dz);
	}

}
