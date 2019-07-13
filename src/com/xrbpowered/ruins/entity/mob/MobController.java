package com.xrbpowered.ruins.entity.mob;

import org.joml.Vector3f;

import com.xrbpowered.ruins.entity.EntityActor;
import com.xrbpowered.ruins.entity.EntityController;

public class MobController extends EntityController {

	private Vector3f move = new Vector3f();
	private Vector3f velocity = new Vector3f();
	
	public MobController(EntityActor entity, float walkSpeed) {
		super(entity, false, walkSpeed, 0f);
	}

	@Override
	public void update(float dt) {
		move.zero();
		// TODO move to target
		update(velocity, move, dt);
	}

}
