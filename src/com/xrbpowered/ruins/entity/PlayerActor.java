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
	
	public final PlayerController controller;
	public CameraActor camera = null;
	
	public boolean alive = true;
	public float health = baseHealth;
	public float hydration = baseHydration;
	
	public PlayerActor(ClientInput input) {
		controller = new PlayerController(input, this);
		controller.moveSpeed = 2.5f;
	}
	public void reset(World world) {
		controller.collider.world = world;

		position.x = world.startx * 2f;
		position.z = world.startz * 2f;
		position.y = 1f;
		rotation.x = 0f;
		rotation.z = 0f;
		rotation.y = (float) Math.toRadians(-180f);
		updateTransform();

		alive = true;
		health = baseHealth;
		hydration = baseHydration;
		if(Ruins.flash!=null)
			Ruins.flash.reset();
	}
	
	public void updateHealth(float dh) {
		if(!alive)
			return;
		health += dh;
		if(health>baseHealth)
			health = baseHealth;
		if(health<1f) {
			health = 0f;
			alive = false;
			Ruins.flash.blackOut();
		}
		Ruins.flash.setBaseAlpha(health);
	}
	
	public void applyDamage(float damage, boolean flash) {
		if(health<=0 || damage<=0)
			return;
		if(flash)
			Ruins.flash.flashPain(damage, health);
		updateHealth(-damage);
	}
	
	public void applyDamage(float damage) {
		applyDamage(damage, true);
	}
	
	@Override
	public void updateTransform() {
		super.updateTransform();
		if(camera!=null) {
			camera.position.set(this.position.x, this.position.y+(alive ? cameraHeight : cameraDeathHeight), this.position.z);
			camera.rotation.set(this.rotation);
			camera.updateTransform();
		}
	}
	
	public void updateTime(float dt) {
		if(alive) {
			hydration -= hydrationLoss*dt;
			if(hydration<0f)
				hydration = 0f;
		}
		updateHealth(hydration>0f ? healthRegen*dt : -healthRegen*dt);
	}

}
