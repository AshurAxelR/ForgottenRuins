package com.xrbpowered.ruins.world.obj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.joml.Vector3f;

import com.xrbpowered.gl.scene.Actor;
import com.xrbpowered.ruins.entity.WorldEntity;
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
	
	protected void place() {
		world.objects.add(this);
		if(this instanceof WorldEntity)
			world.objectEntities.add((WorldEntity) this);
	}
	
	public void loadState(DataInputStream in) throws IOException {
		// TODO load map object state
	}
	
	public void saveState(DataOutputStream out) throws IOException {
		// TODO load map object state
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
