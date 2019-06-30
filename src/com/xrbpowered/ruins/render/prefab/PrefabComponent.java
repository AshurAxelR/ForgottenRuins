package com.xrbpowered.ruins.render.prefab;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.shader.InstanceBuffer;
import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.TileObject;

public class PrefabComponent {

	private static int startAttrib = 3;
	private static final String[] ATTRIB_NAMES = {"ins_Position", "ins_RotationY" , "ins_Scale" , "ins_Light"};

	public static class InstanceInfo {
		public float x, z, y;
		public float rotate = 0f;
		public float scale = 1f;
		public float light = 1f;

		public InstanceInfo(float light) {
			this.x = 0;
			this.z = 0;
			this.y = 0;
			this.light = light;
		}

		public InstanceInfo(float x, float z, float y, float light) {
			this.x = x;
			this.z = z;
			this.y = y;
			this.light = light;
		}

		public InstanceInfo(float x, float z, float y) {
			this(x, z, y, 1f);
		}

		public InstanceInfo(World world, TileObject obj, float yoffs) {
			this.x = obj.x * 2f;
			this.y = obj.y + yoffs;
			this.z = obj.z * 2f;
			this.light = world.map[obj.x][obj.z][obj.y].light;
		}

		public InstanceInfo(World world, TileObject obj) {
			this(world, obj, 0f);
		}

		public InstanceInfo setRotate(float a) {
			this.rotate = a;
			return this;
		}

		public InstanceInfo setRotate(Direction d) {
			this.rotate = d.rotation();
			return this;
		}

		public InstanceInfo setScale(float s) {
			this.scale = s;
			return this;
		}
	}
	
	public final StaticMesh mesh; 
	public final Texture texture;
	public Texture glowTexture = null;
	
	private ArrayList<InstanceInfo> instInfo = null;
	private int instCount;
	private InstanceBuffer instBuffer = null;
	
	public boolean culling = true;

	public PrefabComponent(StaticMesh mesh, Texture texture) {
		this.mesh = mesh;
		this.texture = texture;
	}
	
	public PrefabComponent setGlow(Texture glow) {
		this.glowTexture = glow;
		return this;
	}
	
	public boolean hasGlow() {
		return glowTexture!=null;
	}
	
	public PrefabComponent setCulling(boolean culling) {
		this.culling = culling;
		return this;
	}

	public void startCreateInstances() {
		instInfo = new ArrayList<>();
		instCount = 0;
	}
	
	public int addInstance(InstanceInfo info) {
		instInfo.add(info);
		instCount++;
		return instCount-1;
	}
	
	public void finishCreateInstances() {
		if(instBuffer!=null)
			releaseInstances();
		instCount = instInfo.size();
		if(instCount>0) {
			instBuffer = new InstanceBuffer(1, instCount, startAttrib, new int[] {3, 1, 1, 1});
			float[] instanceData = new float[instCount * 6];
			int offs = 0;
			for(InstanceInfo info : instInfo) {
				instanceData[offs+0] = info.x;
				instanceData[offs+1] = info.y;
				instanceData[offs+2] = info.z;
				instanceData[offs+3] = info.rotate;
				instanceData[offs+4] = info.scale;
				instanceData[offs+5] = info.light;
				offs += 6;
			}
			instBuffer.updateInstanceData(instanceData, instCount);
		}
		instInfo = null;
	}
	
	public void drawInstances() {
		if(instCount==0)
			return;
		if(culling)
			GL11.glEnable(GL11.GL_CULL_FACE);
		else
			GL11.glDisable(GL11.GL_CULL_FACE);
		mesh.enableDraw(null);
		texture.bind(0);
		(hasGlow() ? glowTexture : getBlack()).bind(1);
		instBuffer.enable();
		mesh.drawCallInstanced(instCount);
		instBuffer.disable();
		mesh.disableDraw();
	}
	
	public void releaseInstances() {
		if(instBuffer!=null)
			instBuffer.release();
		instBuffer = null;
		instCount = 0;
	}
	
	public void release() {
		mesh.release();
		texture.release();
		if(black!=null) {
			black.release();
			black = null;
		}
	}

	private static Texture black = null;
	
	private static Texture getBlack() {
		if(black==null)
			black = new Texture(Color.BLACK);
		return black;
	}
	
	public static int bindShader(Shader shader, int startAttrib) {
		PrefabComponent.startAttrib = startAttrib;
		return InstanceBuffer.bindAttribLocations(shader, startAttrib, ATTRIB_NAMES);
	}
}
