package com.xrbpowered.ruins.entity.mob;

import com.xrbpowered.ruins.entity.EntityActor;
import com.xrbpowered.ruins.render.prefab.EntityComponent;
import com.xrbpowered.ruins.render.prefab.InstanceInfo;
import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.World;

public abstract class MobEntity extends EntityActor {

	public static final float spawnTime = 2f;
	
	protected final InstanceInfo instInfo = new InstanceInfo();
	
	protected float time = 0f;
	public boolean alive = true;
	
	private final MobController controller;
	
	public MobEntity(World world, float walkSpeed) {
		super(world);
		controller = new MobController(this, walkSpeed);
	}
	
	public MobEntity spawn(int tx, int tz, int ty, Direction d) {
		if(world.mobs.size()>=World.maxMobs) {
			alive = false;
			return null;
		}
		else {
			alive = true;
			time = 0f;
			position.x = tx*2f;
			position.z = tz*2f;
			position.y = ty;
			rotation.y = d.rotation();
			world.mobs.add(this);
			updateMapPosition();
			return this;
		}
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
		instInfo.light = world.map[mapx][mapz][mapy].light;
		getRenderComponent().addInstance(instInfo);
	}
	
	@Override
	protected void updateController(float dt) {
		// TODO set target
		controller.update(dt);
	}
	
	@Override
	public boolean updateTime(float dt) {
		time += dt;
		super.updateTime(dt);
		return alive;
	}
	
}
