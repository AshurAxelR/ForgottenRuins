package com.xrbpowered.ruins.entity.mob;

import com.xrbpowered.ruins.entity.DamageSource;
import com.xrbpowered.ruins.render.prefab.EntityComponent;
import com.xrbpowered.ruins.render.prefab.MobRenderer;
import com.xrbpowered.ruins.world.World;

public class Ghost extends MobEntity {

	public Ghost(World world) {
		super(world);
	}

	@Override
	public EntityComponent getRenderComponent() {
		return MobRenderer.ghost;
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
