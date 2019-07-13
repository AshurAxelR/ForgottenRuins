package com.xrbpowered.ruins.entity;

import com.xrbpowered.gl.scene.Actor;
import com.xrbpowered.ruins.world.World;

public abstract class EntityActor extends Actor implements WorldEntity {

	public final World world;
	public boolean alive = true;

	public int mapx, mapz, mapy;
	
	public EntityActor(World world) {
		this.world = world;
	}

}
