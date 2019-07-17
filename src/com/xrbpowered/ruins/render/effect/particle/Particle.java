package com.xrbpowered.ruins.render.effect.particle;

import org.joml.Vector3f;

import com.xrbpowered.ruins.entity.WorldEntity;

public class Particle implements WorldEntity {

	public Vector3f position = new Vector3f();
	public Vector3f speed = new Vector3f();
	public float scale = 1f;
	public float phase = 0f;

	public float t = 0f;
	public float duration;
	public float cycleTime = 1f;
	
	public Particle(float duration) {
		this.duration = duration;
	}
	
	private static final Vector3f v = new Vector3f();
	
	@Override
	public boolean updateTime(float dt) {
		t += dt;
		phase = t * cycleTime;
		v.set(speed);
		v.mul(dt);
		position.add(v);
		return t<duration;
	}
}
