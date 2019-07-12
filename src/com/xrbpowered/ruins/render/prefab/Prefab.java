package com.xrbpowered.ruins.render.prefab;

import java.util.Random;

import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.SmallObject;
import com.xrbpowered.ruins.world.obj.TileObject;

public class Prefab {

	protected final Random random = new Random();

	protected boolean interactive = false;
	protected PrefabComponent comp = null;
	
	public Prefab() {
	}
	
	public Prefab(PrefabComponent comp) {
		this(false, comp);
	}

	public Prefab(boolean interactive, PrefabComponent comp) {
		this.interactive = interactive;
		this.comp = comp;
	}
	
	public PrefabComponent getInteractionComp() {
		return interactive ? this.comp : null;
	}

	public void addInstance(World world, TileObject obj) {
		int index = comp.addInstance(new InstanceInfo(world, obj).setRotate(obj.d));
		if(interactive)
			obj.intractionComponentIndex = index;
	}
	
	public void addInstance(World world, SmallObject obj) {
		int index = comp.addInstance(obj.info);
		if(interactive)
			obj.intractionComponentIndex = index;
	}
	
}
