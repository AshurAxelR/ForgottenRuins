package com.xrbpowered.ruins.entity;

import static java.awt.event.KeyEvent.*;

import org.joml.Vector3f;

import com.xrbpowered.gl.client.ClientInput;
import com.xrbpowered.gl.scene.WalkController;

public class PlayerController extends WalkController {

	public Integer keyJump = VK_SPACE;
	
	public final PlayerCollider collider = new PlayerCollider();

	public final float jumpVelocity = 0.075f;//0.075f; // 0.09f;
	public final float gravity = 0.375f;
	
	private boolean queueJump = false;
	private boolean jumpReset = true;
	private boolean inAir = false;
	private boolean drowning = false;
	
	public boolean enabled = true;
	
	public PlayerController(ClientInput input, PlayerActor player) {
		super(input);
		setActor(player);
		moveSpeed = 2.5f;
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
				move.mul(0.015f);
		}
	}

	public void queueJump() {
		queueJump = true;
	}
	
	@Override
	protected void updateVelocity(Vector3f move, float dt) {
		if(inAir)
			velocity.add(move.mul(moveSpeed * dt));
		else
			super.updateVelocity(move, dt);
		if(isAlive() && enabled && !drowning && queueJump) {
			if(jumpReset && !inAir) {
				//velocity.set(move.mul(moveSpeed * 0.55f));
				velocity.y += jumpVelocity;
				inAir = true;
				jumpReset = false;
			}
		}
		else if(!inAir) {
			jumpReset = true;
		}
		if(queueJump && !input.isKeyDown(keyJump))
			queueJump = false;
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
					velocity.x = 0f;
					velocity.z = 0f;
					velocity.y = 0f;
				}
				if(ny>player.position.y)
					player.position.y = ny;
			}
			
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

			player.position.y += velocity.y;
			float ny = collider.clipy(player.position);
			if(inAir && !collider.falling && ny>player.position.y) {
				inAir = false;
				float damage = Math.max((velocity.y*velocity.y*100f-3.75f)*8f, 0);
				if(damage>0.1f) {
					player.applyDamage(damage);
					System.out.printf("Hit at velocity %.3f (Damage %.1f)\n", velocity.y, damage);
				}
				velocity.y = 0f;
			}
			if(collider.falling) {
				inAir = true;
			}
			if(!inAir) {
				player.position.y = ny;
			}
			else {
				velocity.y -= gravity*dt;
			}
		}
		if(actor.position.y==0)
			drowning = true;
	}

}
