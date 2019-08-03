package com.xrbpowered.ruins.render.shader;

import java.awt.Color;

import org.joml.Vector3f;

public class ShaderEnvironment {

	public float fogNear = 10f;
	public float fogFar = 50f;

	public Vector3f lightDirection = new Vector3f(0.15f, -0.5f, -0.3f).normalize();
	public float ambient = 0.7f;
	public float lightScale = 0.1f;

	public Color zenithColor = new Color(0xe5efee);
	public Color transitionColor = new Color(0xe5efee);
	public Color horizonColor = new Color(0xe5efee);
	public float transitionPoint = 0.5f;

	public void setSkyColor(Color zenith, Color transition, Color horizon, float transitionPoint) {
		this.zenithColor = zenith;
		this.transitionColor = transition;
		this.horizonColor = horizon;
		this.transitionPoint = transitionPoint;
	}
	
	public void setFog(float near, float far) {
		this.fogNear = near;
		this.fogFar = far;
	}
}
