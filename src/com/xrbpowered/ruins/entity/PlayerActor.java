package com.xrbpowered.ruins.entity;

import com.xrbpowered.gl.scene.Actor;
import com.xrbpowered.gl.scene.CameraActor;
import com.xrbpowered.ruins.Ruins;

public class PlayerActor extends Actor {

	public static final int baseHealth = 100;
	
	public static final float cameraHeight = 1.5f;
	public static final float cameraDeathHeight = 0.3f;
	
	public CameraActor camera = null;
	
	public boolean alive = true;
	public float health = baseHealth;
	
	public PlayerActor() {
		reset();
	}
	
	public void reset() {
		alive = true;
		health = baseHealth;
		if(Ruins.flash!=null)
			Ruins.flash.reset();
	}
	
	public void applyDamage(float damage) {
		if(health<=0 || damage<=0)
			return;
		Ruins.flash.flashPain(damage, health);
		health -= damage;
		if(health<1f) {
			health = 0f;
			alive = false;
			Ruins.flash.blackOut();
		}
		Ruins.flash.setBaseAlpha(health);
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

}
