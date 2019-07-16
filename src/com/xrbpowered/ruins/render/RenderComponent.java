package com.xrbpowered.ruins.render;

import com.xrbpowered.gl.res.shader.Shader;

public abstract class RenderComponent<T> {

	protected int instCount;

	public int getInstCount() {
		return instCount;
	}
	
	public abstract void drawInstances(Shader shader);
	public abstract void startCreateInstances();
	public abstract int addInstance(T info);
	public abstract void finishCreateInstances();
	public abstract void releaseInstances();
	
	public void release() {
	}


}
