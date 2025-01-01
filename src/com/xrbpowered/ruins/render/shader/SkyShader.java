package com.xrbpowered.ruins.render.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.xrbpowered.gl.res.shader.CameraShader;
import com.xrbpowered.gl.res.shader.VertexInfo;

public class SkyShader extends CameraShader {

	public static final VertexInfo vertexInfo = new VertexInfo()
			.addAttrib("in_Position", 3);

	public ShaderEnvironment environment = null;

	public SkyShader() {
		super(vertexInfo, "shaders/sky_v.glsl", "shaders/sky_f.glsl");
		followCamera = true;
	}

	public SkyShader(String pathVS, String pathFS) {
		super(vertexInfo, pathVS, pathFS);
		followCamera = true;
	}

	public SkyShader(VertexInfo info, String pathVS, String pathFS) {
		super(info, pathVS, pathFS);
		followCamera = true;
	}
	
	@Override
	public void updateUniforms() {
		super.updateUniforms();
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	@Override
	public void unuse() {
		super.unuse();
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	public SkyShader setEnvironment(ShaderEnvironment env) {
		this.environment = env;
		updateEnvironment();
		return this;
	}
	
	public void updateEnvironment() {
		if(environment==null)
			return;
		GL20.glUseProgram(pId);
		uniform(GL20.glGetUniformLocation(pId, "lightDirection"), environment.lightDirection);
		uniform(GL20.glGetUniformLocation(pId, "upColor"), environment.zenithColor);
		uniform(GL20.glGetUniformLocation(pId, "midColor"), environment.transitionColor);
		uniform(GL20.glGetUniformLocation(pId, "downColor"), environment.horizonColor);
		GL20.glUniform1f(GL20.glGetUniformLocation(pId, "midPoint"), environment.transitionPoint);
		GL20.glUseProgram(0);
	}
}
