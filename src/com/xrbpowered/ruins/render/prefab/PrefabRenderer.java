package com.xrbpowered.ruins.render.prefab;

import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.MapObject;
import com.xrbpowered.ruins.world.obj.TileObject;

public class PrefabRenderer extends InstanceRenderer<PrefabComponent> {

	public static Prefab palm;
	public static Prefab well;
	public static Prefab dryWell;
	public static Prefab tablet;
	public static Prefab obelisk;
	public static Prefab obeliskGlow;
	public static Prefab portalBroken;
	public static Prefab portalFrame;
	public static Prefab portal;

	public static Prefab[] jars;
	public static Prefab broken;
	public static Prefab chest;
	public static Prefab chestOpen;
	public static Prefab chestGold;
	public static Prefab chestGoldOpen;
	public static Prefab[] grass;

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
		PrefabRenderer.broken = new Prefab(false, add(new PrefabComponent(mesh("jar/broken.obj"), texture("jar/broken.png"))));

		Texture jarTex = texture("jar/jar.png");
		PrefabRenderer.jars = new Prefab[] {
			new Prefab(true, add(new PrefabComponent(mesh("jar/jar1.obj"), jarTex))),
			new Prefab(true, add(new PrefabComponent(mesh("jar/jar2.obj"), jarTex))),
		};
		
		Texture chestTex = texture("chest/chest.png");
		chest = new Prefab(true, add(new PrefabComponent(mesh("chest/chest.obj", 0.75f), chestTex)));
		chestOpen = new Prefab(true, add(new PrefabComponent(mesh("chest/chest_open.obj", 0.75f), chestTex)));
		Texture chestGoldTex = texture("chest/chest_gold.png");
		chestGold = new Prefab(true, add(new PrefabComponent(mesh("chest/chest.obj", 0.9f), chestGoldTex)));
		chestGoldOpen = new Prefab(true, add(new PrefabComponent(mesh("chest/chest_open.obj", 0.9f), chestGoldTex)));
		
		StaticMesh grassMesh = mesh("grass/grass.obj");
		PrefabRenderer.grass = new Prefab[] {
			new Prefab(false, add(new PrefabComponent(grassMesh, texture("grass/grass1.png")).setCulling(false))),
			new Prefab(false, add(new PrefabComponent(grassMesh, texture("grass/grass2.png")).setCulling(false))),
			new Prefab(false, add(new PrefabComponent(grassMesh, texture("grass/grass3.png")).setCulling(false))),
			new Prefab(false, add(new PrefabComponent(mesh("grass/grass.obj", 0.5f), texture("grass/grass4.png")).setCulling(false))),
		};

		StaticMesh obeliskMesh = mesh("obelisk/obelisk.obj");
		Texture obeliskTex = texture("obelisk/obelisk.png");
		PrefabRenderer.obelisk = new Prefab(true, add(new PrefabComponent(obeliskMesh, obeliskTex)));
		PrefabRenderer.obeliskGlow = new Prefab(true, add(new PrefabComponent(obeliskMesh, obeliskTex).setGlow(texture("obelisk/obelisk_glow.png"))));

		Texture portalTex = texture("portal/portal.png");
		StaticMesh portalFrameMesh =mesh("portal/portal.obj");
		PrefabRenderer.portalBroken = new Prefab(false, add(new PrefabComponent(mesh("portal/portal_broken.obj"), portalTex)));
		
		final PrefabComponent portalFrameOn = add(new PrefabComponent(portalFrameMesh, portalTex).setGlow(texture("portal/portal_glow.png")));
		final PrefabComponent portalPane = add(new PrefabComponent(mesh("portal/portal_pane.obj"), InstanceComponent.getBlack()).setCulling(false).setGlow(texture("portal/astral.png")));
		final PrefabComponent portalInteract = add(new PrefabComponent(mesh("portal/portal_inter.obj"), portalTex));
		PrefabRenderer.portalFrame = new Prefab(true, add(new PrefabComponent(portalFrameMesh, portalTex)));
		PrefabRenderer.portal = new Prefab() {
			@Override
			public void addInstance(World world, TileObject obj) {
				portalFrameOn.addInstance(new InstanceInfo(world, obj).setRotate(obj.d));
				portalPane.addInstance(new InstanceInfo(world, obj).setRotate(obj.d));
			}
			@Override
			public PrefabComponent getInteractionComp() {
				return portalInteract;
			}
		};

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

	@Override
	protected void drawComp(Shader shader, PrefabComponent comp) {
		((InstanceShader) shader).updateHighlight(comp==pickedComponent ? pickedComponentIndex : -1);
		super.drawComp(shader, comp);
	}
	
	public void updateAllInstances(World world) {
		// FIXME optimise instance update
		releaseInstances();
		createInstances(world);
	}

}
