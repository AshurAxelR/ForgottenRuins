package com.xrbpowered.ruins.render;

import java.util.Arrays;
import java.util.Comparator;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.scene.CameraActor;
import com.xrbpowered.ruins.world.World;

public class WallChunk {

	public static final int size = World.chunkSize;
	public static final float radius = size * (float)Math.sqrt(2); 

	private static final Comparator<WallChunk> zorder = new Comparator<WallChunk>() {
		@Override
		public int compare(WallChunk o1, WallChunk o2) {
			return -Float.compare(o1.getCameraZ(), o2.getCameraZ());
		}
	};
	
	public static void zsort(WallChunk[] walls, CameraActor camera) {
		for(WallChunk wall : walls)
			wall.updateCameraZ(camera);
		Arrays.sort(walls, zorder);
	}
	
	private StaticMesh mesh;
	public final Vector4f pivot = new Vector4f();
	private float cameraZ = 0f;
	
	public WallChunk(StaticMesh mesh, int cx, int cz) {
		this.mesh = mesh;
		float pivotx = cx * size *2f + size;
		float pivotz = cz * size *2f + size;
		this.pivot.set(pivotx, 0, pivotz, 1);
	}
	
	public StaticMesh getMesh() {
		return mesh;
	}
	
	public void drawVisible() {
		if(mesh!=null && cameraZ<radius)
			mesh.draw();
	}

	public void draw() {
		if(mesh!=null)
			mesh.draw();
	}

	public void release() {
		if(mesh!=null) {
			mesh.release();
			mesh = null;
		}
	}
	
	private static final Matrix4f m = new Matrix4f();
	public static final Vector4f v = new Vector4f();
	
	public float updateCameraZ(CameraActor camera) {
		m.identity();
		m.translate(camera.position.x, 0f, camera.position.z);
		m.rotateY(camera.rotation.y);
		m.invert();
		m.transform(pivot, v);
		cameraZ = v.z;
		return cameraZ;
	}

	public float getCameraZ() {
		return cameraZ;
	}

}
