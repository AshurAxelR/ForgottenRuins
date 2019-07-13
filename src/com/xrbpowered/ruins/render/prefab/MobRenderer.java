package com.xrbpowered.ruins.render.prefab;

import com.xrbpowered.ruins.entity.mob.MobEntity;
import com.xrbpowered.ruins.world.World;

public class MobRenderer extends ComponentRenderer<EntityComponent> {

	public static final String prefabPath = "entities/";

	public static EntityComponent ghost;
	
	public MobRenderer() {
		super("entities/");
		ghost = add(new EntityComponent(mesh("ghost.obj"), texture("ghost.png")));
	}
	
	public void allocateInstanceData(World world) {
		for(EntityComponent comp : components)
			comp.allocateInstanceData(World.maxMobs);
	}
	
	public void updateInstances(World world) {
		for(EntityComponent comp : components)
			comp.startCreateInstances();
		for(MobEntity obj : world.mobs)
			obj.addComponentInstance();
		for(EntityComponent comp : components)
			comp.finishCreateInstances();
	}
}
