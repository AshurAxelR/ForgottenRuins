package com.xrbpowered.ruins.entity;

import static java.awt.event.KeyEvent.*;

import org.joml.Vector3f;

import com.xrbpowered.gl.client.ClientInput;
import com.xrbpowered.gl.scene.WalkController;
import com.xrbpowered.ruins.Ruins;

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
	
	private static final float[] dyPoints = {0f}; //{0f, 0.7f, 1.4f};
	
	@Override
	protected void applyVelocity(float dt) {
		if(collider.world==null)
			return;
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
				// TODO fall damage
				int damage = Math.round(Math.max((-velocity.y*velocity.y*velocity.y*1000f-7f)*6.5f, 0));
				if(damage>0) {
					System.out.printf("Hit at velocity %.3f (Damage %d%%)\n", velocity.y, damage);
					Ruins.flash.flashPain(damage/100f+0.02f);
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
