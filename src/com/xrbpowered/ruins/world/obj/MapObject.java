package com.xrbpowered.ruins.world.obj;

import org.joml.Vector3f;

import com.xrbpowered.gl.scene.Actor;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabComponent;
import com.xrbpowered.ruins.world.World;

public abstract class MapObject {

	public final World world;
	
	public Vector3f position;
	
	public int intractionComponentIndex = -1;

	public MapObject(World world) {
		this.world = world;
	}
	
	public abstract Prefab getPrefab();
	public abstract void addPrefabInstance();
	
	public PrefabComponent getInteractionComp() {
		Prefab prefab = getPrefab();
		return prefab==null ? null : prefab.getInteractionComp();
	}
	
	public void copyToActor(Actor actor) {
		actor.position = position;
		actor.updateTransform();
	}
	
	public void interact() {
	}
	
	public String getPickName() {
		return null;
	}
	
	public String getActionString() {
		return "";
	}

}
