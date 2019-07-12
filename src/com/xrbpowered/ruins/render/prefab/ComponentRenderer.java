package com.xrbpowered.ruins.render.prefab;

import java.util.ArrayList;

import com.xrbpowered.gl.res.mesh.ObjMeshLoader;
import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.ruins.render.shader.WallShader;

public class ComponentRenderer<C extends RenderComponent> {
	
	public static Prefab palm;
	public static Prefab well;
	public static Prefab dryWell;
	public static Prefab tablet;
	public static Prefab obelisk;
	public static Prefab obeliskGlow;

	public static Prefab jar1;
	public static Prefab broken;

	public static PrefabComponent pickedComponent = null; 
	public static int pickedComponentIndex = -1; 
	

	protected final String basePath;
	protected ArrayList<C> components = new ArrayList<>(); 
	
	public ComponentRenderer(String basePath) {
		this.basePath = basePath;
	}
	
	public StaticMesh mesh(String path) {
		return ObjMeshLoader.loadObj(basePath+path, 0, 1f, WallShader.vertexInfo, null);
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
	
	public void drawInstances() {
		ComponentShader shader = ComponentShader.getInstance();
		shader.use();
		for(C comp : components)
			drawComp(comp);
		shader.unuse();
	}
	
	protected void drawComp(C comp) {
		comp.drawInstances();
	}
	
	public void releaseInstances() {
		for(C comp : components)
			comp.releaseInstances();
	}
	
}
