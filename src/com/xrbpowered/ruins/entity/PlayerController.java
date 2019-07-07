package com.xrbpowered.ruins.entity;

import static java.awt.event.KeyEvent.*;

import org.joml.Vector3f;

import com.xrbpowered.gl.client.ClientInput;
import com.xrbpowered.gl.scene.WalkController;

public class PlayerController extends WalkController {

	public Integer keyJump = VK_SPACE;
	
	public final PlayerCollider collider = new PlayerCollider();

	public final static float walkSpeed = 2.6f;
	public final static float jumpSpeed = 6.0f;
	public final static float airAccel = 0.8f;
	public final static float gravity = 19.6f;
	
	private boolean queueJump = false;
	private boolean jumpReset = true;
	private boolean inAir = false;
	private boolean drowning = false;
	
	public boolean enabled = true;
	
	public PlayerController(ClientInput input, PlayerActor player) {
		super(input);
		setActor(player);
		moveSpeed = walkSpeed;
	}

	public void reset() {
		drowning = false;
		inAir = false;
		jumpReset = true;
		velocity.zero();
	}
	
	public boolean isDrowning() {
		return drowning;
	}
	
	protected boolean isAlive() {
		return ((PlayerActor) actor).alive;
	}
	
	@Override
	protected void updateMove(Vector3f move) {
		if(isAlive() && enabled) {
			super.updateMove(move);
			if(drowning)
				move.mul(0.25f);
			if(inAir)
				move.mul(airAccel);
		}
	}

	public void queueJump() {
		queueJump = true;
	}
	
	private Vector3f speed = new Vector3f();
	
	@Override
	protected void updateVelocity(Vector3f move, float dt) {
		if(inAir) {
			speed.add(move.mul(moveSpeed * dt));
		}
		else {
			speed.set(move);
			speed.mul(moveSpeed);
		}
		if(isAlive() && enabled && !drowning && queueJump) {
			if(jumpReset && !inAir) {
				speed.y = jumpSpeed;
				
				inAir = true;
				jumpReset = false;
			}
		}
		else if(!inAir) {
			jumpReset = true;
		}
		if(queueJump && !input.isKeyDown(keyJump))
			queueJump = false;
		velocity.set(speed);
		velocity.mul(dt);
	}
	
	private static final float[] dyPoints = {0f, 0.7f, 1.4f};
	private static final Vector3f v = new Vector3f();
	
	@Override
	protected void applyVelocity(float dt) {
		if(collider.world==null)
			return;
		PlayerActor player = (PlayerActor) actor;
		if(velocity.length()>0) {
			if(velocity.y>0f) {
				float ny = collider.clipyTop(player.position, velocity.y, PlayerActor.cameraHeight);
				if(collider.hitTop) {
					if(speed.y>1f) {
						float s = 1f/speed.y;
						speed.x *= s;
						speed.z *= s;
						velocity.x *= s;
						velocity.z *= s;
					}
					speed.y = 0f;
					velocity.y = 0f;
				}
				if(ny>player.position.y)
					player.position.y = ny;
			}
			else
				player.position.y += velocity.y;
			
			v.set(velocity);
			for(float dy : dyPoints) {
				float nx = collider.clipx(player.position, v.x, dy);
				float nz = collider.clipz(player.position, v.z, dy);
				v.x = nx - player.position.x;
				v.z = nz - player.position.z;
				if(collider.hitx && collider.hitz) {
					// FIXME corner collision
				}
			}
			player.position.x += v.x;
			player.position.z += v.z;

			float ny = collider.clipy(player.position);
			if(inAir && !collider.falling && ny>player.position.y) {
				inAir = false;
				float damage = Math.max((speed.y*speed.y-120f)*0.3f, 0);
				if(damage>0.1f) {
					player.applyDamage(damage, DamageSource.fall);
					System.out.printf("Hit at speed %.3f (Damage %.1f)\n", speed.y, damage);
				}
				speed.y = 0f;
			}
			if(collider.falling) {
				inAir = true;
			}
			if(!inAir) {
				player.position.y = ny;
			}
			else {
				speed.y -= gravity*dt;
			}
		}
		if(actor.position.y==0)
			drowning = true;
	}

}
