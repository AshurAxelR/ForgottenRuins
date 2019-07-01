package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.Prefabs;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class Tablet extends TileObject {

	public boolean visited = false;
	
	public Tablet(World world, Token objToken) {
		super(world, objToken);
	}

	@Override
	public Prefab getPrefab() {
		return Prefabs.tablet;
	}
	
	@Override
	public String getPickName() {
		return "Tablet";
	}
	
	@Override
	public String getActionString() {
		return visited ? "[Visited]" : "[Right-click to read]";
	}
	
	@Override
	public void interact() {
		Ruins.overlayVerse.updateAndShow(world.verses, this);
		visited = true;
		Ruins.hud.updatePickText(getPickName());
	}
}
