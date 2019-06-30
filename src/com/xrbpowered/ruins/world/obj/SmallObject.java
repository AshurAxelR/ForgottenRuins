package com.xrbpowered.ruins.world.obj;

import org.joml.Vector3f;

import com.xrbpowered.gl.scene.Actor;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabComponent.InstanceInfo;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator;

public abstract class SmallObject extends MapObject {

	public InstanceInfo info = null; 
	
	public SmallObject(World world) {
		super(world);
	}
	
	public abstract float getScaleRange();
	public abstract float getRadius(float scale);
	
	public void place(WorldGenerator.Token t, InstanceInfo info) {
		this.info = info;
		info.x += 2f*t.x;
		info.z += 2f*t.z;
		info.y += t.y;
		position = new Vector3f(info.x, info.y, info.z);
	}

	@Override
	public void copyToActor(Actor actor) {
		actor.rotation.y = -info.rotate;
		actor.scale.set(info.scale, info.scale, info.scale);
		super.copyToActor(actor);
	}
	
	@Override
	public void addPrefabInstance() {
		Prefab prefab = getPrefab();
		if(prefab!=null)
			prefab.addInstance(world, this);
	}

}
