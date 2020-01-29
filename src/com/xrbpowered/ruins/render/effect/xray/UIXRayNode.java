package com.xrbpowered.ruins.render.effect.xray;

import static com.xrbpowered.ruins.render.prefab.PrefabRenderer.*;

import java.awt.Color;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.scene.CameraActor;
import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.buff.Buff;
import com.xrbpowered.ruins.render.prefab.MobRenderer;
import com.xrbpowered.ruins.world.item.Item;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.UIElement;

public class UIXRayNode extends UINode {

	public static XRayObjectShader objectShader = null;
	public static XRayPaneShader paneShader = null;

	public static UIXrayPane xrayWells;
	public static UIXrayPane xrayGhosts;
	public static UIXrayPane xrayShrines;
	public static UIXrayPane xrayObelisks;
	public static UIXrayPane xrayRoyalChest;
	
	private float time = 0f;
	
	public UIXRayNode(UIContainer parent) {
		super(parent);
		
		xrayGhosts = new UIXrayPane(this, Buff.underworld, Color.BLACK, 0.2f);
		xrayWells = new UIXrayPane(this, Buff.oasis, Item.waterFlask.color, 0.4f);
		xrayShrines = new UIXrayPane(this, Buff.angels, Item.amuletOfRadiance.color, 0.6f);
		xrayObelisks = new UIXrayPane(this, Buff.visions, Color.WHITE, 0.8f);
		xrayRoyalChest = new UIXrayPane(this, Buff.kings, Item.treasure.color, 1f);
	}
	
	@Override
	public void layout() {
		for(UIElement c : children) {
			c.setLocation(0, 0);
			c.setSize(getWidth(), getHeight());
			c.layout();
		}
	}

	@Override
	public void updateTime(float dt) {
		if(paneShader!=null) {
			time += dt;
			paneShader.updateTime(time*0.5f);
		}
		super.updateTime(dt);
	}
	
	@Override
	public void render(RenderTarget target) {
		if(Ruins.world!=null) {
			CameraActor.Perspective camera = (CameraActor.Perspective) objectShader.getCamera();
			camera.setRange(0.1f, Ruins.world.size*2f);
			super.render(target);
			camera.setRange(0.1f, Ruins.viewDist);
		}
	}
	
	@Override
	public void setupResources() {
		objectShader = new XRayObjectShader(Ruins.ruins.getCamera());
		paneShader = new XRayPaneShader();
		
		xrayWells.setPrefabs(well);
		xrayGhosts.setComponents(MobRenderer.ghost);
		xrayShrines.setPrefabs(shrine);
		xrayObelisks.setPrefabs(obelisk, obeliskGlow);
		xrayRoyalChest.setPrefabs(chestGold, chestGoldOpen);
		
		super.setupResources();
	}
	
	@Override
	public void releaseResources() {
		objectShader.release();
		paneShader.release();
		super.releaseResources();
	}

}
