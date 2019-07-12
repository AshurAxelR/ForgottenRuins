package com.xrbpowered.ruins.render.prefab;

import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.TileObject;

public class InstanceInfo {
	public float x, z, y;
	public float rotate = 0f;
	public float scale = 1f;
	public float light = 1f;

	public InstanceInfo(float light) {
		this.x = 0;
		this.z = 0;
		this.y = 0;
		this.light = light;
	}

	public InstanceInfo(float x, float z, float y, float light) {
		this.x = x;
		this.z = z;
		this.y = y;
		this.light = light;
	}

	public InstanceInfo(float x, float z, float y) {
		this(x, z, y, 1f);
	}

	public InstanceInfo(World world, TileObject obj, float yoffs) {
		this.x = obj.x * 2f;
		this.y = obj.y + yoffs;
		this.z = obj.z * 2f;
		this.light = world.map[obj.x][obj.z][obj.y].light;
	}

	public InstanceInfo(World world, TileObject obj) {
		this(world, obj, 0f);
	}

	public InstanceInfo setRotate(float a) {
		this.rotate = a;
		return this;
	}

	public InstanceInfo setRotate(Direction d) {
		this.rotate = d.rotation();
		return this;
	}

	public InstanceInfo setScale(float s) {
		this.scale = s;
		return this;
	}
}