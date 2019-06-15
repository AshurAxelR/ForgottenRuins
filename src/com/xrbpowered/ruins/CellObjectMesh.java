package com.xrbpowered.ruins;

import com.xrbpowered.gl.res.mesh.ObjMeshLoader;
import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.shader.InstanceBuffer;
import com.xrbpowered.gl.res.shader.Shader;

public class CellObjectMesh {

	public static final int MAX_INSTANCES = 8192;
	private static int startAttrib = 3;
	private static final String[] ATTRIB_NAMES = {"ins_Position", "ins_RotationY" , "ins_Light"};

	public final StaticMesh mesh; 
	private int instCount;
	private InstanceBuffer instBuffer;
	
	public CellObjectMesh(String path) {
		this.mesh = ObjMeshLoader.loadObj(path, 0, 0.5f, MapShader.vertexInfo, null);
		this.instBuffer = new InstanceBuffer(1, MAX_INSTANCES, startAttrib, new int[] {3, 1, 1});
	}
	
	public void setInstanceData(WorldMap map) {
		instCount = map.cellObjects.size();
		float[] instanceData = new float[instCount * 5];
		int offs = 0;
		for(int i=0; i<instCount; i++) {
			CellObject obj = map.cellObjects.get(i);
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
		CellObjectMesh.startAttrib = startAttrib;
		return InstanceBuffer.bindAttribLocations(shader, startAttrib, ATTRIB_NAMES);
	}
}
