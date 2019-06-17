package com.xrbpowered.ruins.render.prefab;

import com.xrbpowered.gl.res.mesh.ObjMeshLoader;
import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.shader.InstanceBuffer;
import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.ruins.render.shader.WallShader;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.TileObject;

public class PrefabComponent {

	public static final int MAX_INSTANCES = 8192;
	private static int startAttrib = 3;
	private static final String[] ATTRIB_NAMES = {"ins_Position", "ins_RotationY" , "ins_Light"};

	public final StaticMesh mesh; 
	private int instCount;
	private InstanceBuffer instBuffer;
	
	public PrefabComponent(String path) {
		this.mesh = ObjMeshLoader.loadObj(path, 0, 1f, WallShader.vertexInfo, null);
		this.instBuffer = new InstanceBuffer(1, MAX_INSTANCES, startAttrib, new int[] {3, 1, 1});
	}
	
	public void setInstanceData(World map) {
		instCount = map.tileObjects.size();
		float[] instanceData = new float[instCount * 5];
		int offs = 0;
		for(int i=0; i<instCount; i++) {
			TileObject obj = map.tileObjects.get(i);
			instanceData[offs+0] = obj.x * 2f;
			instanceData[offs+1] = obj.y;
			instanceData[offs+2] = obj.z * 2f;
			instanceData[offs+3] = 0;
			instanceData[offs+4] = map.map[obj.x][obj.z][obj.y].light;
			offs += 5;
		}
		instBuffer.updateInstanceData(instanceData, instCount);
	}
	
	public void drawInstances() {
		mesh.enableDraw(null);
		instBuffer.enable();
		mesh.drawCallInstanced(instCount);
		instBuffer.disable();
		mesh.disableDraw();
	}
	
	public void release() {
		mesh.release();
	}

	public static int bindShader(Shader shader, int startAttrib) {
		PrefabComponent.startAttrib = startAttrib;
		return InstanceBuffer.bindAttribLocations(shader, startAttrib, ATTRIB_NAMES);
	}
}
