package com.xrbpowered.ruins.entity;

import com.xrbpowered.gl.client.ClientInput;
import com.xrbpowered.gl.scene.Actor;
import com.xrbpowered.gl.scene.CameraActor;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.world.World;

public class PlayerActor extends Actor {

	public static final int baseHealth = 100;
	public static final int baseHydration = 100;
	public static final float healthRegen = 0.2f;
	public static final float hydrationLoss = 0.2f;
	
	public static final float cameraHeight = 1.5f;
	public static final float cameraDeathHeight = 0.3f;
	
	public static final float dtLimit = 0.05f;
	
	public final PlayerController controller;
	public CameraActor camera = null;
	
	private float cameraLevel = cameraHeight;
	
	public boolean alive = true;
	public DamageSource lastDamageSource = null;
	public float deathTimer = 0f;
	
	public float health = baseHealth;
	public float hydration = baseHydration;
	public int coins = 0;
	
	public PlayerActor(ClientInput input) {
		controller = new PlayerController(input, this);
	}
	public void reset(World world) {
		controller.collider.world = world;
		controller.reset();

		position.x = world.startx * 2f;
		position.z = world.startz * 2f;
		position.y = 1f;
		rotation.x = 0f;
		rotation.z = 0f;
		rotation.y = (float) Math.toRadians(-180f);
		updateTransform();
		
		cameraLevel = cameraHeight;

		alive = true;
		health = baseHealth;
		hydration = baseHydration;
		coins = 0;
		if(Ruins.flash!=null)
			Ruins.flash.reset();
	}
	
	public void updateHealth(float dh, DamageSource souce) {
		if(!alive)
			return;
		lastDamageSource = souce;
		health += dh;
		if(health>baseHealth)
			health = baseHealth;
		if(health<1f) {
			die();
		}
		Ruins.flash.setBaseAlpha(health);
	}
	
	public void die() {
		health = 0f;
		hydration = 0f;
		alive = false;
		deathTimer = 0f;
		Ruins.flash.blackOut();
	}
	
	public void applyDamage(float damage, boolean flash, DamageSource source) {
		if(health<=0 || damage<0.1f)
			return;
		if(flash)
			Ruins.flash.flashPain(damage, health);
		updateHealth(-damage, source);
	}
	
	public void applyDamage(float damage, DamageSource source) {
		applyDamage(damage, true, source);
	}
	
	@Override
	public void updateTransform() {
		super.updateTransform();
		if(camera!=null) {
			camera.position.set(this.position.x, this.position.y+cameraLevel, this.position.z);
			camera.rotation.set(this.rotation);
			camera.updateTransform();
		}
	}
	
	public void updateTime(float dt) {
		if(dt>dtLimit)
			dt = dtLimit;
		controller.update(dt);
		if(alive && controller.isDrowning()) {
			cameraLevel -= dt*0.25f;
			if(cameraLevel<=cameraDeathHeight) {
				cameraLevel = cameraDeathHeight;
				lastDamageSource = DamageSource.drown;
				die();
			}
		}
		if(alive) {
			hydration -= hydrationLoss*dt;
			if(hydration<=0f) {
				hydration = 0f;
				Ruins.flash.daze(true);
			}
			else
				Ruins.flash.daze(false);
		}
		else {
			cameraLevel = cameraDeathHeight;
			deathTimer += dt;
			if(deathTimer>2f && !Ruins.overlayGameOver.isActive()) {
				Ruins.overlayGameOver.show(lastDamageSource.gameOverMessage);
			}
		}
		updateHealth(hydration>0f ? healthRegen*dt : -healthRegen*dt, DamageSource.dehydrate);
	}

}
