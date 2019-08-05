package com.xrbpowered.ruins.render.effect.particle;

public class ParticleGenerator {

	public final ParticleEffect effect;
	public float minDelay, maxDelay;
	
	private float t = 0f;
	
	public ParticleGenerator(ParticleEffect effect, float minDelay, float maxDelay) {
		this.effect = effect;
		this.minDelay = minDelay;
		this.maxDelay = maxDelay;
	}
	
	public void setDelay(float minDelay, float maxDelay) {
		this.minDelay = minDelay;
		this.maxDelay = maxDelay;
	}

	public void setDelay(float delay) {
		this.minDelay = delay;
		this.maxDelay = delay;
	}

	public void reset() {
		t = 0f;
	}
	
	public void update(float dt) {
		t += dt;
		while(t>0f) {
			effect.generateParticle();
			t -= RandomUtils.random(minDelay, maxDelay);
		}
	}

}
