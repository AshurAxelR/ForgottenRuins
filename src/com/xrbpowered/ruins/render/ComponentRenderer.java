package com.xrbpowered.ruins.render;

import java.util.ArrayList;

import com.xrbpowered.gl.res.mesh.ObjMeshLoader;
import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.ruins.render.shader.WallShader;

public abstract class ComponentRenderer<C extends RenderComponent<?>> {
	
	protected final String basePath;
	protected ArrayList<C> components = new ArrayList<>(); 
	
	public ComponentRenderer(String basePath) {
		this.basePath = basePath;
	}
	
	public StaticMesh mesh(String path, float scale) {
		return ObjMeshLoader.loadObj(basePath+path, 0, scale, WallShader.vertexInfo);
	}

	public StaticMesh mesh(String path) {
		return mesh(path, 1f);
	}

	public Texture texture(String path) {
		return new Texture(basePath+path, true, false);
	}
	
	protected C add(C comp) {
		components.add(comp);
		return comp;
	}
	
	public void releaseResources() {
		for(C comp : components)
			comp.release();
		components = null;
	}
	
	protected abstract Shader getShader();
	
	public void drawInstances() {
		Shader shader = getShader();
		shader.use();
		for(C comp : components)
			drawComp(shader, comp);
		shader.unuse();
	}
	
	protected void drawComp(Shader shader, C comp) {
		comp.drawInstances(shader);
	}
	
	public void releaseInstances() {
		for(C comp : components)
			comp.releaseInstances();
	}
	
}
