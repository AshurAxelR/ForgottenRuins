package com.xrbpowered.ruins.entity.mob;

import com.xrbpowered.ruins.entity.EntityActor;
import com.xrbpowered.ruins.render.prefab.EntityComponent;
import com.xrbpowered.ruins.render.prefab.InstanceInfo;
import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.Tile;
import com.xrbpowered.ruins.world.TileType;
import com.xrbpowered.ruins.world.World;

public abstract class MobEntity extends EntityActor {

	public static final float spawnTime = 2f;
	
	protected final InstanceInfo instInfo = new InstanceInfo();
	
	public float time = 0f;
	public boolean alive = true;
	
	protected final MobController controller;
	
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
		instInfo.light = world.map[mapx][mapz][mapy].light;
		getRenderComponent().addInstance(instInfo);
	}
	
	protected void setTarget() {
		controller.noTarget = true;
		int x = mapx;
		int z = mapz;
		int y = mapy;
		if(World.isInside(x, z) && mapy>0) {
			Tile tile = world.map[x][z][y];
			Direction d = tile.pathDir;
			if(d!=null) {
				d = d.opposite();
				int dy = (tile.type==TileType.ramp && tile.rampDir==d) ? 1 : 0;
				x += d.dx;
				z += d.dz;
				y += dy;
				controller.target.x = x*2f;
				controller.target.z = z*2f;
				controller.target.y = y;
				controller.targetDist = world.map[mapx][mapz][mapy].pathDist;
				controller.noTarget = false;
			}
		}
	}
	
	@Override
	protected void updateController(float dt) {
		if(time>spawnTime) {
			setTarget();
			controller.update(dt);
		}
	}
	
	@Override
	public boolean updateTime(float dt) {
		time += dt;
		super.updateTime(dt);
		if(mapy<=0)
			alive = false;
		return alive;
	}
	
}
