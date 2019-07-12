package com.xrbpowered.ruins.render.prefab;

import java.util.ArrayList;

import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.texture.Texture;

public class PrefabComponent extends RenderComponent {

	private ArrayList<InstanceInfo> instInfo = null;

	public PrefabComponent(StaticMesh mesh, Texture texture) {
		super(mesh, texture);
	}
	
	@Override
	public PrefabComponent setGlow(Texture glow) {
		return (PrefabComponent) super.setGlow(glow);
	}
	
	@Override
	public PrefabComponent setCulling(boolean culling) {
		return (PrefabComponent) super.setCulling(culling);
	}
	
	@Override
	public void startCreateInstances() {
		instInfo = new ArrayList<>();
		instCount = 0;
	}
	
	@Override
	public int addInstance(InstanceInfo info) {
		instInfo.add(info);
		instCount++;
		return instCount-1;
	}
	
	@Override
	public void finishCreateInstances() {
		instCount = instInfo.size();
		if(createInstanceBuffer(instCount)) {
			float[] instanceData = createInstanceData(instCount);
			int index = 0;
			for(InstanceInfo info : instInfo) {
				setInstanceData(instanceData, info, index);
				index++;
			}
			instBuffer.updateInstanceData(instanceData, instCount);
		}
		instInfo = null;
	}
	
}
