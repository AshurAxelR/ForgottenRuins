package com.xrbpowered.ruins.render.shader;

import java.awt.Color;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class ShaderEnvironment {

	public float fogNear = 10f;
	public float fogFar = 50f;
	public Vector4f fogColor = new Vector4f(0.4f, 0.6f, 0.9f, 1);

	public Vector3f lightDirection = new Vector3f(0.15f, -0.5f, 0.3f).normalize();
	public float ambient = 0.7f;
	public float lightScale = 0.1f;

	public void setFog(float near, float far, Vector4f color) {
		this.fogNear = near;
		this.fogFar = far;
		this.fogColor.set(color);
	}

	public void setFog(float near, float far, Color color) {
		this.fogNear = near;
		this.fogFar = far;
		this.fogColor.set(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1);
	}
}
