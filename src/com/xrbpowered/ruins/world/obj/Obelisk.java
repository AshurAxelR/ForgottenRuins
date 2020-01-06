package com.xrbpowered.ruins.world.obj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.render.effect.TracePathEffect;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.ObeliskSystem;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class Obelisk extends TileObject {

	public final ObeliskSystem system;
	public boolean visited = false;
	
	public Obelisk(ObeliskSystem system, Token objToken) {
		super(system.world, objToken);
		this.system = system;
		system.add(this);
	}
	
	@Override
	public void loadState(DataInputStream in) throws IOException {
		if(in.readBoolean())
			system.visit(this);
	}
	
	@Override
	public void saveState(DataOutputStream out) throws IOException {
		out.writeBoolean(visited);
	}

	@Override
	public Prefab getPrefab() {
		return visited ? PrefabRenderer.obeliskGlow : PrefabRenderer.obelisk;
	}
	
	@Override
	public String getPickName() {
		return visited ? "Activated Obelisk" : "Inactive Obelisk";
	}
	
	@Override
	public String getActionString() {
		return visited ? "[Visited]" : "[Right-click to activate]";
	}
	
	@Override
	public void interact() {
		if(system.visit(this)) {
			if(system.remaining>0)
				Ruins.hud.popup.popup(String.format("%d remaining...", system.remaining));
			else
				Ruins.hud.popup.popup("The exit portal is now open");
			TracePathEffect.update(world);
			Ruins.prefabs.updateAllInstances(world);
		}
	}

}
