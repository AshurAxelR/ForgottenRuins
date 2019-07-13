package com.xrbpowered.ruins.entity.mob;

import org.joml.Vector3f;

import com.xrbpowered.ruins.entity.EntityActor;
import com.xrbpowered.ruins.entity.EntityController;

public class MobController extends EntityController {

	private Vector3f move = new Vector3f();
	private Vector3f velocity = new Vector3f();
	
	public Vector3f target = new Vector3f();
	public int targetDist = 0;
	public boolean noTarget = true;
	
	public MobController(EntityActor entity, float walkSpeed) {
		super(entity, false, walkSpeed, 0f);
	}

	@Override
	public void update(float dt) {
		if(!noTarget) {
			move.set(target);
			move.sub(entity.position);
			// TODO prevent stacking
		}
		else {
			move.zero();
		}
		if(move.length()>0) {
			move.normalize();
			entity.rotation.y = (float)Math.atan2(-move.z, -move.x);
		}
		update(velocity, move, dt);
	}

}
