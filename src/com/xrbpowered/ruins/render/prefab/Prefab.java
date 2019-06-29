package com.xrbpowered.ruins.render.prefab;

import java.util.Random;

import com.xrbpowered.ruins.render.prefab.PrefabComponent.InstanceInfo;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.TileObject;

public class Prefab {

	protected final Random random = new Random();
	public final PrefabComponent[] components;

	public PrefabComponent interactionComponent = null;
	
	public Prefab() {
		this.components = null;
	}
	
	public Prefab(PrefabComponent... comp) {
		this.components = comp;
	}

	public Prefab(boolean interactive, PrefabComponent... comp) {
		this.components = comp;
		if(interactive)
			interactionComponent = comp[0];
	}
	
	public void addInstance(World world, TileObject obj) {
		for(PrefabComponent comp : components) {
			int index = comp.addInstance(new InstanceInfo(world, obj).setRotate(obj.d));
			if(comp==interactionComponent)
				obj.intractionComponentIndex = index;
		}
	}
	
}
