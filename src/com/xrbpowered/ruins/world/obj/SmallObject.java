package com.xrbpowered.ruins.world.obj;

import org.joml.Vector3f;

import com.xrbpowered.gl.scene.Actor;
import com.xrbpowered.ruins.render.prefab.InstanceInfo;
import com.xrbpowered.ruins.world.Tile;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator;

public abstract class SmallObject extends MapObject {

	public SmallObject(World world) {
		super(world);
	}
	
	public abstract float getScaleRange();
	public abstract float getRadius(float scale);
	
	public void place(Tile tile, WorldGenerator.Token t, InstanceInfo info) {
		this.instInfo = info;
		info.x += 2f*t.x;
		info.z += 2f*t.z;
		info.y += t.y;
		position = new Vector3f(info.x, info.y, info.z);
		tile.addSmallObject(this);
		super.place();
	}

	@Override
	public void copyToActor(Actor actor) {
		actor.rotation.y = -instInfo.rotate;
		actor.scale.set(instInfo.scale, instInfo.scale, instInfo.scale);
		super.copyToActor(actor);
	}

}
