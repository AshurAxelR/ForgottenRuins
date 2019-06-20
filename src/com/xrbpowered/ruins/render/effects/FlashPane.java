package com.xrbpowered.ruins.render.effects;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.gl.ui.pane.PaneShader;
import com.xrbpowered.zoomui.UIContainer;

public class FlashPane extends UINode {

	private static class FlashShader extends Shader {
		public Vector4f color = new Vector4f();
		public float alpha = 0f;
		
		private int colorLocation;
		private int alphaLocation;
		
		public FlashShader() {
			super(PaneShader.vertexInfo, "scrn_v.glsl", "flash_f.glsl");
		}
		
		@Override
		protected void storeUniformLocations() {
			colorLocation = GL20.glGetUniformLocation(pId, "color");
			alphaLocation = GL20.glGetUniformLocation(pId, "alpha");
		}
		
		@Override
		public void use() {
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_SRC_COLOR);
			super.use();
		}
		
		@Override
		public void updateUniforms() {
			uniform(colorLocation, color);
			GL20.glUniform1f(alphaLocation, alpha);
		}
		
		@Override
		public void unuse() {
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			super.unuse();
		}
	}
	
	private static FlashShader shader;
	private float startAlpha = 0f;
	private float t = 0f;
	
	public FlashPane(UIContainer parent) {
		super(parent);
	}

	@Override
	public void layout() {
		setSize(getParent().getWidth(), getParent().getHeight());
		super.layout();
	}
	
	public void flashPain(float alpha) {
		startAlpha = shader.alpha + alpha;
		t = 0f;
	}
	
	@Override
	public void updateTime(float dt) {
		t += dt;
		float a = (float)Math.pow(0.75f, t*5f) * startAlpha;
		if(a<0.01f) a = 0;
		shader.alpha = a;
		super.updateTime(dt);
	}
	
	public void render(RenderTarget target) {
		if(shader.alpha>0f) {
			shader.use();
			PaneShader.getInstance().quad.draw();
			shader.unuse();
		}
		super.render(target);
	}
	
	@Override
	public void setupResources() {
		shader = new FlashShader();
		shader.color.set(0.75f, 0.125f, 0, 1f);
		super.setupResources();
	}
	
	@Override
	public void releaseResources() {
		shader.release();
		super.releaseResources();
	}
}