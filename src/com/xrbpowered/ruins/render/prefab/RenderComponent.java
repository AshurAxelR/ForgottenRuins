package com.xrbpowered.ruins.render.prefab;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.shader.InstanceBuffer;
import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.gl.res.texture.Texture;

public abstract class RenderComponent {

	private static int startAttrib = 3;
	private static final String[] attribNames = {"ins_Position", "ins_RotationY" , "ins_Scale" , "ins_Light"};
	private static final int[] attribSizes = {3, 1, 1, 1};
	private static final int itemDataSize = 6; // sum of attribSizes

	public final StaticMesh mesh; 
	public final Texture texture;
	public Texture glowTexture = null;
	
	protected int instCount;
	protected InstanceBuffer instBuffer = null;
	
	public boolean culling = true;

	public RenderComponent(StaticMesh mesh, Texture texture) {
		this.mesh = mesh;
		this.texture = texture;
	}
	
	public RenderComponent setGlow(Texture glow) {
		this.glowTexture = glow;
		return this;
	}
	
	public boolean hasGlow() {
		return glowTexture!=null;
	}
	
	public RenderComponent setCulling(boolean culling) {
		this.culling = culling;
		return this;
	}
	
	public int getInstCount() {
		return instCount;
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
	
	public abstract void startCreateInstances();
	public abstract int addInstance(InstanceInfo info);
	public abstract void finishCreateInstances();
	
	protected boolean createInstanceBuffer(int count) {
		if(instBuffer!=null)
			releaseInstances();
		if(count>0)
			instBuffer = new InstanceBuffer(1, count, startAttrib, attribSizes);
		else
			instBuffer = null;
		return instBuffer!=null;
	}
	
	protected float[] createInstanceData(int count) {
		return new float[count * itemDataSize];
	}
	
	protected void setInstanceData(float[] instanceData, InstanceInfo info, int index) {
		int offs = index * itemDataSize;
		instanceData[offs+0] = info.x;
		instanceData[offs+1] = info.y;
		instanceData[offs+2] = info.z;
		instanceData[offs+3] = info.rotate;
		instanceData[offs+4] = info.scale;
		instanceData[offs+5] = info.light;
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
	
	public static Texture getBlack() {
		if(black==null)
			black = new Texture(Color.BLACK);
		return black;
	}
	
	public static int bindShader(Shader shader, int startAttrib) {
		RenderComponent.startAttrib = startAttrib;
		return InstanceBuffer.bindAttribLocations(shader, startAttrib, attribNames);
	}
}
