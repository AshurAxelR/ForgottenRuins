package com.xrbpowered.ruins.entity.mob;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.ruins.entity.EntityActor;
import com.xrbpowered.ruins.render.effect.particle.Particle;
import com.xrbpowered.ruins.render.effect.particle.ParticleEffect;
import com.xrbpowered.ruins.render.effect.particle.ParticleRenderer;
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
	
	protected final MobController controller;
	
	public MobEntity(World world, float walkSpeed) {
		super(world);
		controller = new MobController(this, walkSpeed);
	}
	
	public abstract int getTypeId();

	@Override
	public void loadState(DataInputStream in) throws IOException {
		super.loadState(in);
		time = in.readFloat();
	}
	
	@Override
	public void saveState(DataOutputStream out) throws IOException {
		super.saveState(out);
		out.writeFloat(time);
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
		if(world.isInside(x, z) && mapy>0) {
			Tile tile = world.map[x][z][y];
			Direction d = tile.pathDir[world.pathfinder.activePathLayer];
			if(d!=null) {
				d = d.opposite();
				int dy = (tile.type==TileType.ramp && tile.rampDir==d) ? 1 : 0;
				x += d.dx;
				z += d.dz;
				y += dy;
				controller.target.x = x*2f;
				controller.target.z = z*2f;
				controller.target.y = y;
				controller.targetDist = world.map[mapx][mapz][mapy].pathDist[world.pathfinder.activePathLayer];
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
		if(position.y<=0)
			disappear();
		return alive;
	}
	
	public void disappear() {
		alive = false;
	}
	
	public void radiance() {
		radianceSpark.pivot.set(position);
		radianceSpark.pivot.y += 0.3f;
		radianceSpark.generate();
		disappear();
	}

	public static ParticleEffect radianceSpark = new ParticleEffect() {
		@Override
		public void generateParticle() {
			Particle p = new Particle(2f);
			assign(p);
			ParticleRenderer.radiance.add(p);
		}
		@Override
		public void generate() {
			generateParticle();
		}
	};
	
	public static MobEntity createFromTypeId(World world, int id) {
		switch(id) {
			case Ghost.typeId:
				return new Ghost(world, null);
			default:
				return null;
		}
	}
	
}
