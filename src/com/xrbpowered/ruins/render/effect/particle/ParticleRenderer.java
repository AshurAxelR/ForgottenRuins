package com.xrbpowered.ruins.render.effect.particle;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.ruins.render.ComponentRenderer;
import com.xrbpowered.ruins.world.PathFinder;

public class ParticleRenderer extends ComponentRenderer<ParticleComponent> {

	public static ParticleComponent dark;
	public static ParticleComponent light;
	public static ParticleComponent radiance;
	public static ParticleComponent smoke;
	public static ParticleComponent trace;
	
	public float globalTime = 0f; 
	
	public ParticleRenderer() {
		super("particles/");
		dark = add(new ParticleComponent(300, texture("dark.png")));
		light = add(new ParticleComponent(1000, texture("light.png")));
		radiance = add(new ParticleComponent(50, texture("radiance.png")));
		smoke = add(new ParticleComponent(100, texture("smoke.png")));
		trace = add(new ParticleComponent(PathFinder.maxPathDist, texture("trace.png")));
	}

	@Override
	protected Shader getShader() {
		return null;
	}
	
	public void drawInstances(RenderTarget target) {
		ParticleShader shader = ParticleShader.getInstance();
		shader.use();
		shader.updateTarget(target);
		for(ParticleComponent comp : components)
			drawComp(shader, comp);
		shader.unuse();
	}

	public void clear() {
		for(ParticleComponent comp : components)
			comp.clear();
	}

	public void update(float dt) {
		globalTime += dt;
		for(ParticleComponent comp : components)
			comp.update(dt);
	}
	
}
