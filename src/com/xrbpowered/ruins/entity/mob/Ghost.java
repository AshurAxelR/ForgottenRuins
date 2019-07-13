package com.xrbpowered.ruins.entity.mob;

import com.xrbpowered.ruins.entity.DamageSource;
import com.xrbpowered.ruins.entity.EntityCollider;
import com.xrbpowered.ruins.entity.player.PlayerController;
import com.xrbpowered.ruins.render.prefab.EntityComponent;
import com.xrbpowered.ruins.render.prefab.MobRenderer;
import com.xrbpowered.ruins.world.World;

public class Ghost extends MobEntity {

	public static final float radius = 0.3f;
	public static final float height = 1.7f;
	public static final float speed = 1.5f;

	public Ghost(World world) {
		super(world, speed);
	}

	@Override
	public EntityComponent getRenderComponent() {
		return MobRenderer.ghost;
	}
	
	@Override
	public void setEntityDimensions(EntityCollider collider) {
		collider.setEntityDimensions(radius, height, PlayerController.dyPoints);
	}


	@Override
	public boolean updateTime(float dt) {
		super.updateTime(dt);
		if(this.time>spawnTime && this.getDistTo(world.player)<2.8f) {
			world.player.applyDamage(30f, DamageSource.mob);
			alive = false;
		}
		return alive;
	}
	
}
