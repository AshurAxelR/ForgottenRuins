package com.xrbpowered.ruins.entity.mob;

import java.util.Random;

import org.joml.Vector3f;

import com.xrbpowered.ruins.entity.EntityActor;
import com.xrbpowered.ruins.entity.EntityController;

public class MobController extends EntityController {

	public static float turnSpeed = (float)Math.PI;
	
	private Vector3f move = new Vector3f();
	private Vector3f velocity = new Vector3f();
	
	public Vector3f target = new Vector3f();
	public int targetDist = 0;
	public boolean noTarget = true;
	
	public MobController(EntityActor entity, float walkSpeed) {
		super(entity, false, walkSpeed, 0f);
	}
	
	private static Vector3f v = new Vector3f();
	private static Vector3f f = new Vector3f();
	private static Random random = new Random();
	
	@Override
	protected void updateVelocity(Vector3f velocity, Vector3f move, float dt) {
		if(!noTarget) {
			v.set(random.nextFloat(), 0f, random.nextFloat());
			v.mul(0.2f);
			for(MobEntity e : entity.world.mobs) {
				f.set(entity.position);
				f.sub(e.position);
				float dist = f.length();
				if(dist>0f && dist<4f) {
					f.mul(0.5f/dist/dist);
					v.add(f);
				}
					
			}
			v.y = 0f;
			move.add(v);
			move.normalize();
		}
		super.updateVelocity(velocity, move, dt);
	}

	@Override
	public void update(float dt) {
		if(!noTarget) {
			move.set(target);
			move.sub(entity.position);
		}
		else {
			move.zero();
		}
		if(move.length()>0) {
			move.normalize();
			float a = (float)Math.atan2(-move.z, -move.x);
			float s = (float)Math.sin(a-entity.rotation.y);
			if(s>0f)
				entity.rotation.y += turnSpeed*dt;
			else if(s<0f)
				entity.rotation.y -= turnSpeed*dt;
			float s2 = (float)Math.sin(a-entity.rotation.y);
			if(s*s2<0f)
				entity.rotation.y = a;
		}
		update(velocity, move, dt);
	}

}
