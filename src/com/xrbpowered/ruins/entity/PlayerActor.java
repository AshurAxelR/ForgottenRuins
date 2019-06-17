package com.xrbpowered.ruins.entity;

import com.xrbpowered.gl.scene.Actor;
import com.xrbpowered.gl.scene.CameraActor;

public class PlayerActor extends Actor {

	public final float cameraHeight = 1.5f;
	
	public CameraActor camera = null;
	
	public PlayerActor() {
	}
	
	@Override
	public void updateTransform() {
		super.updateTransform();
		if(camera!=null) {
			camera.position.set(this.position.x, this.position.y+cameraHeight, this.position.z);
			camera.rotation.set(this.rotation);
			camera.updateTransform();
		}
	}

}
