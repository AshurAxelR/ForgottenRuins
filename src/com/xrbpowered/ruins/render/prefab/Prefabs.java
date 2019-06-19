package com.xrbpowered.ruins.render.prefab;

import java.util.ArrayList;

import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.ruins.render.prefab.PrefabComponent.InstanceInfo;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.TileObject;

public class Prefabs {

	private static ArrayList<PrefabComponent> components = null; 
	
	public static Prefab palm;
	public static Prefab obelisk;
	
	public static void createResources() {
		components = new ArrayList<>();
		
		final PrefabComponent plot = add(new PrefabComponent("plot.obj", new Texture("test/sand64.png", true, false)));
		final PrefabComponent palmT = add(new PrefabComponent("palm_t3.obj", new Texture("palm_t.png", true, false)));
		final PrefabComponent palm = add(new PrefabComponent("palm.obj", new Texture("palm.png", true, false)).setCulling(false));
		
		Prefabs.obelisk = new Prefab(add(new PrefabComponent("obelisk.obj", new Texture("test/sand32_128.png", true, false))));
		
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
	
	protected static PrefabComponent add(PrefabComponent comp) {
		components.add(comp);
		return comp;
	}
	
	public static void releaseResources() {
		for(PrefabComponent comp : components)
			comp.release();
		components = null;
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
		for(PrefabComponent comp : components)
			comp.drawInstances();
	}
	
	public static void releaseInstances() {
		for(PrefabComponent comp : components)
			comp.releaseInstances();
	}

}
