package com.xrbpowered.ruins.entity.mob;

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

}
