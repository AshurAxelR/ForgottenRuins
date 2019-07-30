package com.xrbpowered.ruins.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.joml.Vector3f;

import com.xrbpowered.gl.scene.Actor;
import com.xrbpowered.ruins.world.TileType;
import com.xrbpowered.ruins.world.World;

public abstract class EntityActor extends Actor implements WorldEntity {

	public final World world;
	public boolean alive = true;
	
	public final Vector3f speed = new Vector3f();

	public int mapx, mapz, mapy;
	
	public EntityActor(World world) {
		this.world = world;
	}
	
	public abstract void setEntityDimensions(EntityCollider collider);
	
	public void loadState(DataInputStream in) throws IOException {
		position.x = in.readFloat();
		position.y = in.readFloat();
		position.z = in.readFloat();
		rotation.x = in.readFloat();
		rotation.y = in.readFloat();
		updateTransform();
		updateMapPosition();
	}
	
	public void saveState(DataOutputStream out) throws IOException {
		out.writeFloat(position.x);
		out.writeFloat(position.y);
		out.writeFloat(position.z);
		out.writeFloat(rotation.x);
		out.writeFloat(rotation.y);
	}

	protected boolean updateMapPosition() {
		int x = World.mapx(position.x);
		int z = World.mapz(position.z);
		int y = World.mapy(position.y);
		while(y>0 && (!world.isInside(x, z) || world.map[x][z][y-1].type!=TileType.solid))
			y--;
		if(mapx!=x || mapz!=z || mapy!=y) {
			mapx = x;
			mapz = z;
			mapy = y;
			return true;
		}
		else
			return false;
	}
	
	protected void updateController(float dt) {
	}

	@Override
	public boolean updateTime(float dt) {
		updateController(dt);
		updateMapPosition();
		return true;
	}
	
}
