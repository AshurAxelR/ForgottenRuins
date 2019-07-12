package com.xrbpowered.ruins.entity.mob;

import com.xrbpowered.gl.scene.Actor;
import com.xrbpowered.ruins.render.prefab.EntityComponent;
import com.xrbpowered.ruins.render.prefab.InstanceInfo;
import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.World;

public abstract class MobEntity extends Actor {

	public static final float spawnTime = 2f;
	
	public final World world;
	
	protected final InstanceInfo instInfo = new InstanceInfo();
	
	protected float time = 0f;
	
	public MobEntity(World world) {
		this.world = world;
	}
	
	public MobEntity spawn(int tx, int tz, int ty, Direction d, float delay) {
		position.x = tx*2f;
		position.z = tz*2f;
		position.y = ty;
		rotation.y = d.rotation();
		time = -delay*30f;
		instInfo.light = world.map[tx][tz][ty].light;
		return this;
	}
	
	public abstract EntityComponent getRenderComponent();

	public void addComponentInstance() {
		if(time<0f)
			return;
		instInfo.x = position.x;
		instInfo.z = position.z;
		instInfo.y = position.y;
		instInfo.rotate = rotation.y;
		if(time<spawnTime)
			instInfo.y += -2f + 2f*(float)Math.sin(Math.PI*time/spawnTime/2f);
		else
			instInfo.rotate += (time-spawnTime) * 0.3f;
		getRenderComponent().addInstance(instInfo);
	}
	
	public boolean updateTime(float dt) {
		time += dt;
		return true;
	}
	
}
