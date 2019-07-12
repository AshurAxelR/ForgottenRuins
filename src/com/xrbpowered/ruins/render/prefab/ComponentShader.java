package com.xrbpowered.ruins.render.prefab;

import org.lwjgl.opengl.GL20;

import com.xrbpowered.gl.scene.CameraActor;
import com.xrbpowered.ruins.render.shader.ShaderEnvironment;
import com.xrbpowered.ruins.render.shader.WallShader;

public class ComponentShader extends WallShader {
	private int highlightInstanceLocation;
	
	public ComponentShader(ShaderEnvironment env, CameraActor camera) {
		super("tileobj_v.glsl", "tileobj_f.glsl");
		setEnvironment(env);
		setCamera(camera);
	}
	
	@Override
	protected void storeUniformLocations() {
		super.storeUniformLocations();
		highlightInstanceLocation = GL20.glGetUniformLocation(pId, "highlightInstance");
	}
	
	@Override
	protected String[] listSamplerNames() {
		return new String[] {"texDiffuse", "texGlow"};
	}
	
	@Override
	protected int bindAttribLocations() {
		return PrefabComponent.bindShader(this, super.bindAttribLocations());
	}
	
	public void updateHighlight(int index) {
		GL20.glUniform1i(highlightInstanceLocation, index);
	}
	
	private static ComponentShader instance = null;
	
	public static void createInstance(ShaderEnvironment env, CameraActor camera) {
		instance = new ComponentShader(env, camera);
	}
	
	public static ComponentShader getInstance() {
		return instance;
	}
	
	public static void releaseInstance() {
		instance.release();
	}

}