package com.xrbpowered.ruins.render.effect.particle;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.res.shader.VertexInfo;
import com.xrbpowered.gl.scene.CameraActor;
import com.xrbpowered.ruins.render.shader.ShaderEnvironment;
import com.xrbpowered.ruins.render.shader.WallShader;

public class ParticleShader extends WallShader {
	
	public static final VertexInfo vertexInfo = new VertexInfo()
			.addAttrib("in_Position", 3)
			.addAttrib("in_Scale", 1)
			.addAttrib("in_Phase", 1);
	
	private int framesLocation;
	private int sizeLocation;
	private int screenHeightLocation;
	
	public ParticleShader(ShaderEnvironment env, CameraActor camera) {
		super(vertexInfo, "shaders/point_v.glsl", "shaders/point_f.glsl");
		setEnvironment(env);
		setCamera(camera);
	}
	
	@Override
	public void use() {
		GL11.glEnable(GL20.GL_POINT_SPRITE);
		GL11.glEnable(GL32.GL_PROGRAM_POINT_SIZE);
		super.use();
	}
	
	@Override
	protected void storeUniformLocations() {
		super.storeUniformLocations();
		framesLocation = GL20.glGetUniformLocation(pId, "frames");
		sizeLocation = GL20.glGetUniformLocation(pId, "size");
		screenHeightLocation = GL20.glGetUniformLocation(pId, "screenHeight");
	}

	public void updateTarget(RenderTarget target) {
		GL20.glUniform1f(screenHeightLocation, target.getHeight());
	}
	
	public void updateComponent(ParticleComponent comp) {
		GL20.glUniform1i(framesLocation, comp.frames);
		GL20.glUniform1f(sizeLocation, comp.particleSize);
	}

	public void updateEnvironment() {
		if(environment==null)
			return;
		GL20.glUseProgram(pId);
		GL20.glUniform1f(GL20.glGetUniformLocation(pId, "fogNear"), environment.fogNear);
		GL20.glUniform1f(GL20.glGetUniformLocation(pId, "fogFar"), environment.fogFar);
		uniform(GL20.glGetUniformLocation(pId, "fogColor"), environment.fogColor);
		GL20.glUseProgram(0);
	}

	private static ParticleShader instance = null;
	
	public static void createInstance(ShaderEnvironment env, CameraActor camera) {
		instance = new ParticleShader(env, camera);
	}
	
	public static ParticleShader getInstance() {
		return instance;
	}
	
	public static void releaseInstance() {
		instance.release();
	}

}
