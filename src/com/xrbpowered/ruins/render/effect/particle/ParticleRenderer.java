package com.xrbpowered.ruins.render.effect.particle;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.ruins.render.ComponentRenderer;

public class ParticleRenderer extends ComponentRenderer<ParticleComponent> {

	public static ParticleComponent smookeDot;
	
	public ParticleRenderer() {
		super("particles/");
		smookeDot = add(new ParticleComponent(10000, texture("smoke_dot.png")));
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

	public void update(float dt) {
		for(ParticleComponent comp : components)
			comp.update(dt);
	}
	
}
