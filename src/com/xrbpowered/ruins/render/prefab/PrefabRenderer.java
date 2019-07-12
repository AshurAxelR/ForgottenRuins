package com.xrbpowered.ruins.render.prefab;

import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.MapObject;
import com.xrbpowered.ruins.world.obj.TileObject;

public class PrefabRenderer extends ComponentRenderer<PrefabComponent> {

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
	
	public PrefabRenderer() {
		super("prefabs/");
		
		final PrefabComponent plot = add(new PrefabComponent(mesh("palm/plot.obj"), texture("palm/plot.png")));
		final PrefabComponent palmT = add(new PrefabComponent(mesh("palm/palm_t3.obj"), texture("palm/palm_t.png")));
		final PrefabComponent palm = add(new PrefabComponent(mesh("palm/palm.obj"), texture("palm/palm.png")).setCulling(false));

		PrefabRenderer.well = new Prefab(true, add(new PrefabComponent(mesh("well/well.obj"), texture("well/well.png"))));
		PrefabRenderer.dryWell = new Prefab(false, add(new PrefabComponent(mesh("well/well.obj"), texture("well/well_dry.png"))));
		PrefabRenderer.tablet = new Prefab(true, add(new PrefabComponent(mesh("tablet/tablet.obj"), texture("tablet/tablet.png"))));
		PrefabRenderer.jar1 = new Prefab(true, add(new PrefabComponent(mesh("jar/jar1.obj"), texture("jar/jar.png"))));
		PrefabRenderer.broken = new Prefab(false, add(new PrefabComponent(mesh("jar/broken.obj"), texture("jar/broken.png"))));

		StaticMesh obeliskMesh = mesh("obelisk/obelisk.obj");
		Texture obeliskTex = texture("obelisk/obelisk.png");
		PrefabRenderer.obelisk = new Prefab(true, add(new PrefabComponent(obeliskMesh, obeliskTex)));
		PrefabRenderer.obeliskGlow = new Prefab(true, add(new PrefabComponent(obeliskMesh, obeliskTex).setGlow(texture("obelisk/obelisk_glow.png"))));
		
		PrefabRenderer.palm = new Prefab() {
			@Override
			public void addInstance(World world, TileObject obj) {
				random.setSeed(obj.seed);
				plot.addInstance(new InstanceInfo(world, obj));
				palmT.addInstance(new InstanceInfo(world, obj).setRotate(random.nextFloat()*2f*(float)Math.PI));
				palm.addInstance(new InstanceInfo(world, obj, 2.6f+0.8f*random.nextFloat()).setScale(1f+0.3f*random.nextFloat()));
			}
		};
	}
	
	public void createInstances(World world) {
		for(PrefabComponent comp : components)
			comp.startCreateInstances();
		for(MapObject obj : world.objects)
			obj.addPrefabInstance();
		for(PrefabComponent comp : components)
			comp.finishCreateInstances();
		// System.gc();
	}

	public void updateAllInstances(World world) {
		// FIXME optimise instance update
		releaseInstances();
		createInstances(world);
	}

}
