package com.xrbpowered.ruins.render.prefab;

import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.texture.Texture;

public class EntityComponent extends InstanceComponent {

	private int maxCount = 0;
	private float[] instanceData = null;
	
	public EntityComponent(StaticMesh mesh, Texture texture) {
		super(mesh, texture);
	}
	
	@Override
	public EntityComponent setGlow(Texture glow) {
		return (EntityComponent) super.setGlow(glow);
	}
	
	@Override
	public EntityComponent setCulling(boolean culling) {
		return (EntityComponent) super.setCulling(culling);
	}
	
	public int getMaxCount() {
		return maxCount;
	}
	
	public void allocateInstanceData(int count) {
		maxCount = count;
		if(createInstanceBuffer(maxCount)) {
			instanceData = createInstanceData(maxCount);
		}
	}
	
	@Override
	public void startCreateInstances() {
		instCount = 0;
	}
	
	@Override
	public int addInstance(InstanceInfo info) {
		setInstanceData(instanceData, info, instCount);
		instCount++;
		return instCount-1;
	}
	
	@Override
	public void finishCreateInstances() {
		if(instCount>0)
			instBuffer.updateInstanceData(instanceData, instCount);
	}
	
	@Override
	public void releaseInstances() {
		super.releaseInstances();
		maxCount = 0;
	}

}
