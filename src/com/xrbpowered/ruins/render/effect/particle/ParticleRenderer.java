package com.xrbpowered.ruins.render.effect.particle;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.ruins.render.ComponentRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.MapObject;
import com.xrbpowered.ruins.world.obj.TileObject;

public class ParticleRenderer extends ComponentRenderer<ParticleComponent> {

	public static ParticleComponent test;
	
	public ParticleRenderer() {
		super("icons/");
		test = add(new ParticleComponent(10000, texture("dot0.png"), 1, 0.16f));
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

	private float t = 0f;
	
	public void updateInstances(World world, float dt) {
		for(ParticleComponent comp : components)
			comp.startCreateInstances();

		// temporary (testing)
		t += dt;
		for(MapObject obj : world.objects) {
			if(obj instanceof TileObject) {
				Particle p = new Particle();
				p.position.set(obj.position);
				p.position.y += 1.5f;
				p.phase = t;
				test.addInstance(p);
			}
		}
		
		for(ParticleComponent comp : components)
			comp.finishCreateInstances();
	}
	
}
