package com.xrbpowered.ruins.render.prefab;

import java.util.ArrayList;

import org.lwjgl.opengl.GL20;

import com.xrbpowered.gl.res.mesh.ObjMeshLoader;
import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.scene.CameraActor;
import com.xrbpowered.ruins.render.prefab.PrefabComponent.InstanceInfo;
import com.xrbpowered.ruins.render.shader.ShaderEnvironment;
import com.xrbpowered.ruins.render.shader.WallShader;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.TileObject;

public class Prefabs {

	public static final String prefabPath = "prefabs/";
	
	private static class PrefabShader extends WallShader {
		private int highlightInstanceLocation;
		
		public PrefabShader(String pathVS, String pathFS, ShaderEnvironment env, CameraActor camera) {
			super(pathVS, pathFS);
			setEnvironment(env);
			setCamera(camera);
		}
		
		@Override
		protected void storeUniformLocations() {
			super.storeUniformLocations();
			highlightInstanceLocation = GL20.glGetUniformLocation(pId, "highlightInstance");
		}
		
		@Override
		protected String[] listSamplerNames() {
			return new String[] {"texDiffuse", "texGlow"};
		}
		
		@Override
		protected int bindAttribLocations() {
			return PrefabComponent.bindShader(this, super.bindAttribLocations());
		}
		
		public void updateHighlight(int index) {
			GL20.glUniform1i(highlightInstanceLocation, index);
		}
	}
	
	private static PrefabShader shader;
	
	private static ArrayList<PrefabComponent> components = null; 
	
	public static Prefab palm;
	public static Prefab well;
	public static Prefab obelisk;
	public static Prefab obeliskGlow;
	
	public static PrefabComponent pickedComponent = null; 
	public static int pickedComponentIndex = -1; 
	
	public static void createResources(ShaderEnvironment env, CameraActor camera) {
		shader = new PrefabShader("tileobj_v.glsl", "tileobj_f.glsl", env, camera);
		components = new ArrayList<>();
		
		final PrefabComponent plot = add(new PrefabComponent(mesh("palm/plot.obj"), texture("palm/plot.png")));
		final PrefabComponent palmT = add(new PrefabComponent(mesh("palm/palm_t3.obj"), texture("palm/palm_t.png")));
		final PrefabComponent palm = add(new PrefabComponent(mesh("palm/palm.obj"), texture("palm/palm.png")).setCulling(false));

		Prefabs.well = new Prefab(true, add(new PrefabComponent(mesh("well/well.obj"), texture("well/well.png"))));

		StaticMesh obeliskMesh = mesh("obelisk/obelisk.obj");
		Texture obeliskTex = texture("obelisk/obelisk.png");
		Prefabs.obelisk = new Prefab(true, add(new PrefabComponent(obeliskMesh, obeliskTex)));
		Prefabs.obeliskGlow = new Prefab(true, add(new PrefabComponent(obeliskMesh, obeliskTex).setGlow(texture("obelisk/obelisk_glow.png"))));
		
		Prefabs.palm = new Prefab() {
			@Override
			public void addInstance(World world, TileObject obj) {
				random.setSeed(obj.seed);
				plot.addInstance(new InstanceInfo(world, obj));
				palmT.addInstance(new InstanceInfo(world, obj).setRotate(random.nextFloat()*2f*(float)Math.PI));
				palm.addInstance(new InstanceInfo(world, obj, 2.6f+0.8f*random.nextFloat()).setScale(1f+0.3f*random.nextFloat()));
			}
		};
	}
	
	public static StaticMesh mesh(String path) {
		return ObjMeshLoader.loadObj(prefabPath+path, 0, 1f, WallShader.vertexInfo, null);
	}
	
	public static Texture texture(String path) {
		return new Texture(prefabPath+path, true, false);
	}
	
	protected static PrefabComponent add(PrefabComponent comp) {
		components.add(comp);
		return comp;
	}
	
	public static void releaseResources() {
		for(PrefabComponent comp : components)
			comp.release();
		components = null;
		shader.release();
	}
	
	public static void createInstances(World world) {
		for(PrefabComponent comp : components)
			comp.startCreateInstances();
		for(TileObject obj : world.tileObjects) {
			Prefab prefab = obj.getPrefab();
			if(prefab!=null)
				prefab.addInstance(world, obj);
		}
		for(PrefabComponent comp : components)
			comp.finishCreateInstances();
		System.gc();
	}
	
	public static void drawInstances() {
		shader.use();
		for(PrefabComponent comp : components) {
			shader.updateHighlight(comp==pickedComponent ? pickedComponentIndex : -1);
			comp.drawInstances();
		}
		shader.unuse();
	}
	
	public static void releaseInstances() {
		for(PrefabComponent comp : components)
			comp.releaseInstances();
	}
	
	public static void updateAllInstances(World world) {
		// FIXME optimise instance update
		releaseInstances();
		createInstances(world);
	}

}
