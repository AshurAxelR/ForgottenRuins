package com.xrbpowered.ruins.render.effect.xray;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.ui.pane.PaneShader;
import com.xrbpowered.ruins.ui.UIIcon;

public class XRayPaneShader extends Shader {

	private int colorLocation;
	private int alphaLocation;
	private int timeLocation;
	private int texSizeLocation;
	
	private Texture noiseTexture;
	
	public XRayPaneShader() {
		super(PaneShader.vertexInfo, "shaders/scrn_v.glsl", "shaders/xray_f.glsl");
		noiseTexture = new Texture("cloud_noise.png", true, false);
	}
	
	@Override
	protected void storeUniformLocations() {
		colorLocation = GL20.glGetUniformLocation(pId, "color");
		alphaLocation = GL20.glGetUniformLocation(pId, "alpha");
		timeLocation = GL20.glGetUniformLocation(pId, "time");
		texSizeLocation = GL20.glGetUniformLocation(pId, "texSize");
		GL20.glUseProgram(pId);
		GL20.glUniform1i(GL20.glGetUniformLocation(pId, "texMask"), 0);
		GL20.glUniform1i(GL20.glGetUniformLocation(pId, "texNoise"), 1);
		GL20.glUseProgram(0);
	}
	
	@Override
	public void use() {
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		super.use();
		noiseTexture.bind(1);
	}

	public void setColor(Color color, float alpha) {
		uniform(colorLocation, color);
		GL20.glUniform1f(alphaLocation, alpha);
	}
	
	public void updateTime(float t) {
		GL20.glUseProgram(pId);
		GL20.glUniform1f(timeLocation, t);
		GL20.glUseProgram(0);
	}
	
	public void updatePaneSize(float width, float height) {
		float w = width/noiseTexture.getWidth()/UIIcon.pixelSize;
		float h = height/noiseTexture.getHeight()/UIIcon.pixelSize;
		GL20.glUniform2f(texSizeLocation, w, h);
	}

	@Override
	public void updateUniforms() {
	}

	@Override
	public void unuse() {
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		super.unuse();
	}

}
