package com.xrbpowered.ruins.render.effect.particle;

import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.ruins.render.RenderComponent;

public class ParticleComponent extends RenderComponent<Particle> {

	private int maxCount = 0;

	private float[] pointData = null;
	private StaticMesh points = null;
	
	public final Texture texture;
	public final int frames;
	public final float particleSize;

	public ParticleComponent(int maxCount, Texture texture, int frames, float particleSize) {
		this.maxCount = maxCount;
		this.texture = texture;
		this.frames = frames;
		this.particleSize = particleSize;
		allocateInstanceData();
	}
	
	public int getMaxCount() {
		return maxCount;
	}
	
	protected void allocateInstanceData() {
		if(points!=null)
			releaseInstances();
		if(maxCount>0) {
			pointData = new float[maxCount*ParticleShader.vertexInfo.getSkip()];
			points = new StaticMesh(ParticleShader.vertexInfo, pointData, 1, maxCount, true);
		}
		else
			points = null;
	}
	
	@Override
	public void startCreateInstances() {
		instCount = 0;
	}
	
	protected void setPointData(float[] pointData, Particle p, int index) {
		int offs = index * ParticleShader.vertexInfo.getSkip();
		pointData[offs+0] = p.position.x;
		pointData[offs+1] = p.position.y;
		pointData[offs+2] = p.position.z;
		pointData[offs+3] = p.scale;
		pointData[offs+4] = p.phase;
	}
	
	@Override
	public int addInstance(Particle p) {
		if(instCount<maxCount) {
			setPointData(pointData, p, instCount);
			instCount++;
			return instCount-1;
		}
		else
			return -1;
	}
	
	@Override
	public void finishCreateInstances() {
		if(instCount>0) {
			points.updateCountElements(instCount);
			points.updateVertexData(pointData);
		}
	}
	
	@Override
	public void drawInstances(Shader shader) {
		if(instCount==0)
			return;
		((ParticleShader) shader).updateComponent(this);
		texture.bind(0);
		points.draw();
	}
	
	@Override
	public void releaseInstances() {
		if(points!=null)
			points.release();
		points = null;
		instCount = 0;
		maxCount = 0;
	}

	@Override
	public void release() {
		texture.release();
	}

}
