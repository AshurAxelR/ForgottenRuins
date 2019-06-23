package com.xrbpowered.ruins.render.shader;

import org.lwjgl.opengl.GL20;

import com.xrbpowered.gl.res.shader.CameraShader;
import com.xrbpowered.gl.res.shader.VertexInfo;

public class WallShader extends CameraShader {
	
	public static final VertexInfo vertexInfo = new VertexInfo()
			.addAttrib("in_Position", 3)
			.addAttrib("in_Normal", 3)
			.addAttrib("in_TexCoord", 2)
			.addAttrib("in_Light", 1);
	
	public static final String[] samplerNames = {"texDiffuse"};
	
	public ShaderEnvironment environment = null;
	
	public WallShader() {
		super(vertexInfo, "wall_v.glsl", "wall_f.glsl");
	}
	
	protected WallShader(String pathVS, String pathFS) {
		super(vertexInfo, pathVS, pathFS);
	}
	
	public WallShader setEnvironment(ShaderEnvironment env) {
		this.environment = env;
		updateEnvironment();
		return this;
	}
	
	protected String[] listSamplerNames() {
		return samplerNames;
	}
	
	@Override
	protected void storeUniformLocations() {
		super.storeUniformLocations();
		initSamplers(listSamplerNames());
	}
	
	public void updateEnvironment() {
		if(environment==null)
			return;
		GL20.glUseProgram(pId);
		GL20.glUniform1f(GL20.glGetUniformLocation(pId, "fogNear"), environment.fogNear);
		GL20.glUniform1f(GL20.glGetUniformLocation(pId, "fogFar"), environment.fogFar);
		uniform(GL20.glGetUniformLocation(pId, "fogColor"), environment.fogColor);
		uniform(GL20.glGetUniformLocation(pId, "lightDirection"), environment.lightDirection);
		GL20.glUniform1f(GL20.glGetUniformLocation(pId, "ambient"), environment.ambient);
		GL20.glUniform1f(GL20.glGetUniformLocation(pId, "lightScale"), environment.lightScale);
		GL20.glUseProgram(0);
	}
	
}