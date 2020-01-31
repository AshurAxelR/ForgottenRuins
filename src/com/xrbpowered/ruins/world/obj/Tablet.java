package com.xrbpowered.ruins.world.obj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.ui.overlay.RomanNumerals;
import com.xrbpowered.ruins.world.VerseSystem;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class Tablet extends TileObject {

	public final int verse;
	
	public boolean visited = false;
	
	public Tablet(World world, Token objToken) {
		super(world, objToken);
		verse = VerseSystem.getVerseNumber(seed);
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
	
	public boolean isComplete() {
		return world.player.verses.completeVerses[verse];
	}
	
	@Override
	public String getPickName() {
		return visited || isComplete() ? String.format("Tablet %s", RomanNumerals.toRoman(verse+1)) : "Tablet";
	}
	
	@Override
	public String getActionString() {
		return isComplete() ? "[Complete]" :  visited ? "[Visited]" : "[Right-click to read]";
	}
	
	@Override
	public void interact() {
		Ruins.overlayVerse.updateAndShow(world.player.verses, this);
		if(!visited) {
			visited = true;
			Ruins.prefabs.updateAllInstances(world);
		}
		Ruins.hud.updatePickText(getPickName());
	}
}
