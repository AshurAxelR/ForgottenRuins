package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.ObeliskSystem;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class Portal extends TileObject {

	public final ObeliskSystem system;

	public Portal(ObeliskSystem system, Token objToken) {
		super(system.world, objToken);
		this.system = system;
	}

	@Override
	public Prefab getPrefab() {
		return system.remaining==0 ? PrefabRenderer.portal : PrefabRenderer.portalFrame;
	}

	@Override
	public String getPickName() {
		return "Exit Portal";
	}
	
	@Override
	public String getActionString() {
		return "[Right-click to interact]";
	}

	@Override
	public void interact() {
		if(system.remaining==0)
			Ruins.hud.popup.popup("VICTORY! (W.I.P.)");
		else
			Ruins.hud.popup.popup("Activate all obelisks to open");
	}
}
