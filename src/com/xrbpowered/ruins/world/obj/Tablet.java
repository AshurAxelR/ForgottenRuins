package com.xrbpowered.ruins.world.obj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class Tablet extends TileObject {

	public boolean visited = false;
	
	public Tablet(World world, Token objToken) {
		super(world, objToken);
	}

	@Override
	public void loadState(DataInputStream in) throws IOException {
		visited = in.readBoolean();
	}
	
	@Override
	public void saveState(DataOutputStream out) throws IOException {
		out.writeBoolean(visited);
	}
	
	@Override
	public Prefab getPrefab() {
		return PrefabRenderer.tablet;
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
		Ruins.overlayVerse.updateAndShow(world.player.verses, this);
		visited = true;
		Ruins.hud.updatePickText(getPickName());
	}
}
