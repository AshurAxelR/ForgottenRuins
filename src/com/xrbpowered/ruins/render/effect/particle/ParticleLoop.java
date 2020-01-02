package com.xrbpowered.ruins.render.effect.particle;

import com.xrbpowered.ruins.Ruins;

public class ParticleLoop extends Particle {

	public float phaseShift;
	
	public ParticleLoop(float period, float phaseShift) {
		super(period);
		this.phaseShift = phaseShift;
	}
	
	@Override
	public boolean updateTime(float dt) {
		super.updateTime(dt);
		phase = Ruins.particles.globalTime / cycleTime + phaseShift;
		phase = phase - (float)Math.floor(phase);
		return true;
	}

}
