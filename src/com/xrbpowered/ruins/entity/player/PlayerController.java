package com.xrbpowered.ruins.entity.player;

import static java.awt.event.KeyEvent.*;

import org.joml.Vector3f;

import com.xrbpowered.gl.client.ClientInput;
import com.xrbpowered.gl.scene.WalkController;
import com.xrbpowered.ruins.entity.DamageSource;
import com.xrbpowered.ruins.entity.EntityController;

public class PlayerController extends EntityController {

	public static Integer keyJump = VK_SPACE;
	
	public static final float[] dyPoints = {0f, 0.7f, 1.4f};

	public final static float walkSpeed = 2.6f;
	public final static float jumpSpeed = 6.0f;
	public final static float airAccel = 0.8f;
	
	private boolean drowning = false;
	
	private final WalkController inputController;
	
	public PlayerController(ClientInput input, final PlayerEntity player) {
		super(player, true, walkSpeed, jumpSpeed);
		setAirAccel(airAccel);
		inputController = new WalkController(input) {
			@Override
			protected void updateMove(Vector3f move) {
				if(player.alive && enabled) {
					super.updateMove(move);
				}
			}
			@Override
			protected void updateVelocity(Vector3f move, float dt) {
				PlayerController.this.updateVelocity(velocity, move, dt);
				if(!input.isKeyDown(keyJump))
					resetQueueJump();
			}
			@Override
			protected void applyVelocity(float dt) {
				PlayerController.this.applyVelocity(velocity, dt);
			}
		};
		inputController.setActor(player);
	}
	
	public void setMouseLook(boolean enable) {
		inputController.setMouseLook(enable);
	}

	@Override
	public void reset() {
		super.reset();
		drowning = false;
	}
	
	public boolean isDrowning() {
		return drowning;
	}
	
	@Override
	protected void updateVelocity(Vector3f velocity, Vector3f move, float dt) {
		if(drowning) {
			move.mul(0.25f);
			resetQueueJump();
		}
		super.updateVelocity(velocity, move, dt);
	}
	
	@Override
	protected void applyFallDamage(float speedy) {
		float damage = Math.max((speedy*speedy-120f)*0.3f, 0);
		if(damage>0.1f)
			((PlayerEntity) entity).applyDamage(damage, DamageSource.fall);
	}
	
	@Override
	protected void applyVelocity(Vector3f velocity, float dt) {
		super.applyVelocity(velocity, dt);
		if(entity.position.y==0)
			drowning = true;
	}
	
	@Override
	public void update(float dt) {
		inputController.update(dt);
	}

}
