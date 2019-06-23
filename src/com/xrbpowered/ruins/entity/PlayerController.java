package com.xrbpowered.ruins.entity;

import static java.awt.event.KeyEvent.*;

import org.joml.Vector3f;

import com.xrbpowered.gl.client.ClientInput;
import com.xrbpowered.gl.scene.WalkController;

public class PlayerController extends WalkController {

	public Integer keyJump = VK_SPACE;
	
	public final PlayerCollider collider = new PlayerCollider();

	public float jumpVelocity = 0.075f; // 0.09f;
	
	private boolean jumpReset = true;
	private boolean inAir = false;
	
	public PlayerController(ClientInput input, PlayerActor player) {
		super(input);
		setActor(player);
	}

	protected boolean isAlive() {
		return ((PlayerActor) actor).alive;
	}
	
	@Override
	protected void updateMove(Vector3f move) {
		if(isAlive()) {
			super.updateMove(move);
			if(inAir)
				move.mul(0.015f);
		}
	}

	@Override
	protected void updateVelocity(Vector3f move, float dt) {
		if(inAir)
			velocity.add(move.mul(moveSpeed * dt));
		else
			super.updateVelocity(move, dt);
		if(isAlive() && input.isKeyDown(keyJump)) {
			if(jumpReset && !inAir) {
				velocity.y += jumpVelocity;
				inAir = true;
				jumpReset = false;
			}
		}
		else if(!inAir) {
			jumpReset = true;
		}
	}
	
	private static final float[] dyPoints = {0f}; //{0f, 0.7f, 1.4f};
	
	@Override
	protected void applyVelocity(float dt) {
		if(collider.world==null)
			return;
		PlayerActor player = (PlayerActor) actor;
		if(velocity.length()>0) {
			// FIXME corner collision
			// FIXME upper body collision: velocity is applied multiple times
			for(float dy : dyPoints) {
				float nx = collider.clipx(actor.position, velocity.x, dy);
				float nz = collider.clipz(actor.position, velocity.z, dy);
				actor.position.x = nx;
				actor.position.z = nz;
			}

			actor.position.y += velocity.y;
			float ny = collider.clipy(actor.position);
			if(inAir && !collider.falling && ny>actor.position.y) {
				inAir = false;
				float damage = Math.max((velocity.y*velocity.y*100f-3.7f)*8f, 0);
				if(damage>0f) {
					player.applyDamage(damage);
					System.out.printf("Hit at velocity %.3f (Damage %.1f)\n", velocity.y, damage);
				}
				velocity.y = 0f;
			}
			if(collider.falling) {
				inAir = true;
			}
			if(!inAir) {
				actor.position.y = ny;
			}
			else {
				velocity.y -= 0.375f*dt;
			}
		}
	}

}
