package com.xrbpowered.ruins.render.prefab;

import java.awt.Color;

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
	public static Prefab tabletGlow;
	public static Prefab obelisk;
	public static Prefab obeliskGlow;
	public static Prefab portalBroken;
	public static Prefab portalOff;
	public static Prefab portal;
	public static Prefab shrineOff;
	public static Prefab shrine;

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
		
		PrefabRenderer.well = new Prefab(true, add(new PrefabComponent(mesh("well/well.obj"), texture("well/well.png"))));
		PrefabRenderer.dryWell = new Prefab(false, add(new PrefabComponent(mesh("well/well.obj"), texture("well/well_dry.png"))));
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

		StaticMesh tabletMesh = mesh("tablet/tablet.obj");
		Texture tabletTex = texture("tablet/tablet.png");
		PrefabRenderer.tablet = new Prefab(true, add(new PrefabComponent(tabletMesh, tabletTex)));
		PrefabRenderer.tabletGlow = new Prefab(true, add(new PrefabComponent(tabletMesh, tabletTex).setGlow(texture("tablet/tablet_glow.png"))));

		Texture portalTex = texture("portal/portal.png");
		StaticMesh portalFrameMesh =mesh("portal/portal.obj");
		PrefabRenderer.portalBroken = new Prefab(false, add(new PrefabComponent(mesh("portal/portal_broken.obj"), portalTex)));
		
		final PrefabComponent portalFrame = add(new PrefabComponent(portalFrameMesh, portalTex));
		final PrefabComponent portalPane = add(new PrefabComponent(mesh("portal/portal_pane.obj"), InstanceComponent.getBlack()).setCulling(false).setGlow(texture("portal/astral.png")));
		final PrefabComponent portalInteract = add(new PrefabComponent(mesh("portal/portal_inter.obj"), portalTex));
		PrefabRenderer.portalOff = new Prefab(true, portalFrame);
		PrefabRenderer.portal = new Prefab() {
			@Override
			public void addInstance(World world, MapObject obj) {
				portalFrame.addInstance(obj.instInfo);
				portalPane.addInstance(obj.instInfo);
			}
			@Override
			public PrefabComponent getInteractionComp() {
				return portalInteract;
			}
		};
		
		final PrefabComponent angel = add(new PrefabComponent(mesh("shrine/angel.obj"), texture("shrine/angel.png")));
		final PrefabComponent shrineBase = add(new PrefabComponent(mesh("shrine/base.obj"), texture("shrine/base.png")));
		final PrefabComponent prism = add(new PrefabComponent(mesh("shrine/prism.obj"), new Texture(new Color(0xd0ecff))).setGlow(new Texture(new Color(0x35354d))));
		PrefabRenderer.shrineOff = new Prefab(true, angel) {
			@Override
			public void addInstance(World world, MapObject obj) {
				super.addInstance(world, obj);
				shrineBase.addInstance(obj.instInfo);
			}
		};
		PrefabRenderer.shrine = new Prefab(true, angel) {
			@Override
			public void addInstance(World world, MapObject obj) {
				super.addInstance(world, obj);
				shrineBase.addInstance(obj.instInfo);
				prism.addInstance(obj.instInfo);
			}
		};

		final PrefabComponent plot = add(new PrefabComponent(mesh("palm/plot.obj"), texture("palm/plot.png")));
		final PrefabComponent palmT = add(new PrefabComponent(mesh("palm/palm_t3.obj"), texture("palm/palm_t.png")));
		final PrefabComponent palm = add(new PrefabComponent(mesh("palm/palm.obj"), texture("palm/palm.png")).setCulling(false));
		PrefabRenderer.palm = new Prefab() {
			@Override
			public void addInstance(World world, MapObject mapObj) {
				TileObject obj = (TileObject) mapObj;
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
		for(MapObject obj : world.objects) {
			Prefab prefab = obj.getPrefab();
			if(prefab!=null)
				prefab.addInstance(world, obj);
		}
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
		//System.out.println("--- updateAllInstances");
		// FIXME optimise instance update
		releaseInstances();
		createInstances(world);
	}

}
