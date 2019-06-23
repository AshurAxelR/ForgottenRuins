package com.xrbpowered.ruins.render.prefab;

import java.util.ArrayList;

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

	private static class PrefabShader extends WallShader {
		public PrefabShader(String pathVS, String pathFS, ShaderEnvironment env, CameraActor camera) {
			super(pathVS, pathFS);
			setEnvironment(env);
			setCamera(camera);
		}
		
		@Override
		protected int bindAttribLocations() {
			return PrefabComponent.bindShader(this, super.bindAttribLocations());
		}
	}
	
	private static PrefabShader shader;
	private static PrefabShader glowShader;
	
	private static ArrayList<PrefabComponent> components = null; 
	
	public static Prefab palm;
	public static Prefab obelisk;
	
	public static void createResources(ShaderEnvironment env, CameraActor camera) {
		shader = new PrefabShader("tileobj_v.glsl", "wall_f.glsl", env, camera);
		glowShader = new PrefabShader("tileobj_v.glsl", "tileobj_glow_f.glsl", env, camera) {
			@Override
			protected String[] listSamplerNames() {
				return new String[] {"texDiffuse", "texGlow"};
			}
		};
		
		components = new ArrayList<>();
		
		final PrefabComponent plot = add(new PrefabComponent(mesh("plot.obj"), texture("test/sand64.png")));
		final PrefabComponent palmT = add(new PrefabComponent(mesh("palm_t3.obj"), texture("palm_t.png")));
		final PrefabComponent palm = add(new PrefabComponent(mesh("palm.obj"), texture("palm.png")).setCulling(false));
		
		Prefabs.obelisk = new Prefab(add(new PrefabComponent(mesh("obelisk.obj"), texture("obelisk.png")).setGlow(texture("obelisk_glow.png"))));
		
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
		return ObjMeshLoader.loadObj(path, 0, 1f, WallShader.vertexInfo, null);
	}
	
	public static Texture texture(String path) {
		return new Texture(path, true, false);
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
			if(!comp.hasGlow())
				comp.drawInstances();
		}
		shader.unuse();
		glowShader.use();
		for(PrefabComponent comp : components) {
			if(comp.hasGlow())
				comp.drawInstances();
		}
		glowShader.unuse();
	}
	
	public static void releaseInstances() {
		for(PrefabComponent comp : components)
			comp.releaseInstances();
	}

}
