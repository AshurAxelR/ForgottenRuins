package com.xrbpowered.ruins.entity;

import org.joml.Vector3f;

public abstract class EntityController {

	public final static float gravity = 19.6f;

	public final EntityActor entity;
	private final EntityCollider collider;
	
	public boolean canJump;
	public float walkSpeed;
	public float jumpSpeed;
	public float airAccel = 0f;
	
	private boolean queueJump = false;
	private boolean jumpReset = true;
	
	public boolean enabled = true;

	public EntityController(EntityActor entity, boolean canJump, float walkSpeed, float jumpSpeed) {
		this.entity = entity;
		this.collider = new EntityCollider(entity);
		
		this.canJump = canJump;
		this.walkSpeed = walkSpeed;
		this.jumpSpeed = jumpSpeed;
	}

	public EntityController setAirAccel(float airAccel) {
		this.airAccel = airAccel;
		return this;
	}
	
	public void reset() {
		entity.inAir = false;
		jumpReset = true;
		entity.speed.zero();
	}
	
	public void queueJump() {
		if(canJump)
			queueJump = true;
	}

	protected void resetQueueJump() {
		queueJump = false;
	}
	
	protected void updateVelocity(Vector3f velocity, Vector3f move, float dt) {
		if(entity.inAir) {
			entity.speed.add(move.mul(airAccel * walkSpeed * dt));
		}
		else {
			entity.speed.set(move);
			entity.speed.mul(walkSpeed);
		}
		if(entity.alive && enabled && queueJump) {
			if(jumpReset && !entity.inAir) {
				entity.speed.y = jumpSpeed;
				
				entity.inAir = true;
				jumpReset = false;
			}
		}
		else if(!entity.inAir) {
			jumpReset = true;
		}
		velocity.set(entity.speed);
		velocity.mul(dt);
	}
	
	protected void applyFallDamage(float speedy) {
	}
	
	protected void applyVelocity(Vector3f velocity, float dt) {
		if(velocity.length()>0) {
			if(velocity.y>0f) {
				float ny = collider.clipyTop(entity.position, velocity.y);
				if(collider.hitTop) {
					if(entity.speed.y>1f) {
						float s = 1f/entity.speed.y;
						entity.speed.x *= s;
						entity.speed.z *= s;
						velocity.x *= s;
						velocity.z *= s;
					}
					entity.speed.y = 0f;
					velocity.y = 0f;
				}
				if(ny>entity.position.y)
					entity.position.y = ny;
			}
			else
				entity.position.y += velocity.y;
			
			collider.clipxz(velocity, entity.position);

			float ny = collider.clipy(entity.position);
			if(entity.inAir && !collider.falling && ny>entity.position.y) {
				entity.inAir = false;
				applyFallDamage(entity.speed.y);
				entity.speed.y = 0f;
			}
			if(collider.falling) {
				entity.inAir = true;
			}
			if(!entity.inAir) {
				entity.position.y = ny;
			}
			else {
				entity.speed.y -= gravity*dt;
			}
		}
	}
	
	protected void update(Vector3f velocity, Vector3f move, float dt) {
		updateVelocity(velocity, move, dt);
		applyVelocity(velocity, dt);
		entity.updateTransform();
	}
	
	public abstract void update(float dt);

}
