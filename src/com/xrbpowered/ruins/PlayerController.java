package com.xrbpowered.ruins;

import static java.awt.event.KeyEvent.*;

import org.joml.Vector3f;

import com.xrbpowered.gl.client.ClientInput;
import com.xrbpowered.gl.scene.WalkController;

public class PlayerController extends WalkController {

	public Integer keyJump = VK_SPACE;
	
	public final MapCollider collider = new MapCollider();

	public float jumpVelocity = 0.075f; // 0.09f;
	
	private boolean jumpReset = true;
	private boolean inAir = false;
	
	public PlayerController(ClientInput input, PlayerActor player) {
		super(input);
		setActor(player);
	}
	
	@Override
	protected void updateMove(Vector3f move) {
		super.updateMove(move);
		if(inAir)
			move.mul(0.015f);
	}

	@Override
	protected void updateVelocity(Vector3f move, float dt) {
		if(inAir)
			velocity.add(move.mul(moveSpeed * dt));
		else
			super.updateVelocity(move, dt);
		if(input.isKeyDown(keyJump)) {
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
	
	@Override
	protected void applyVelocity(float dt) {
		if(collider.map==null)
			return;
		if(velocity.length()>0) {
			float nx = collider.clipx(actor.position, velocity.x);
			float nz = collider.clipz(actor.position, velocity.z);
			// FIXME corner collision
			// FIXME upper body collision
			actor.position.x = nx;
			actor.position.z = nz;

			actor.position.y += velocity.y;
			float ny = collider.clipy(actor.position);
			if(inAir && !collider.falling && ny>actor.position.y) {
				inAir = false;
				// TODO fall damage
				int damage = Math.round(Math.max((-velocity.y*velocity.y*velocity.y*1000f-7f)*6.5f, 0));
				if(damage>0)
					System.out.printf("Hit at velocity %.3f (Damage %d%%)\n", velocity.y, damage);
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
