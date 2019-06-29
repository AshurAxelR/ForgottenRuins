package com.xrbpowered.ruins.render;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.scene.ActorPicker;
import com.xrbpowered.gl.scene.StaticMeshActor;
import com.xrbpowered.ruins.entity.PlayerActor;
import com.xrbpowered.ruins.render.prefab.PrefabComponent;
import com.xrbpowered.ruins.render.prefab.Prefabs;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.TileObject;

public class TileObjectPicker {

	public static final float reach = 2.5f;
	
	private ActorPicker pick;

	private PlayerActor player;
	private World world;
	private WallChunk[] walls;

	public TileObject pickObject = null;
	
	public TileObjectPicker(PlayerActor player) {
		this.player = player;
		this.pick = new ActorPicker(player.camera); 
	}
	
	public void setWorld(World world, WallChunk[] walls) {
		this.world = world;
		this.walls = walls;
	}
	
	public void update(RenderTarget target) {
		pick.startPicking(target.getWidth()/2, target.getHeight()/2, target);
		for(WallChunk wall : walls) {
			if(wall.getCameraZ()<WallChunk.radius && wall.getCameraZ()>=-WallChunk.radius-reach)
				pick.drawSceneMesh(wall.getMesh(), 0);
		}
		int objId = 1;
		StaticMeshActor objActor = new StaticMeshActor();
		for(TileObject obj : world.tileObjects) {
			PrefabComponent comp = obj.getPrefab().interactionComponent;
			if(comp!=null) {
				float dist = player.position.distance(obj.position);
				if(dist<=reach) {
					objActor.position = obj.position;
					objActor.rotation.y = -obj.d.rotation();
					objActor.updateTransform();
					objActor.setMesh(comp.mesh);
					pick.drawActor(objActor, objId);
				}
			}
			objId++;
		}
		int pickId = pick.finishPicking(target);
		if(pickId==0) {
			pickObject = null;
			Prefabs.pickedComponent = null;
			Prefabs.pickedComponentIndex = -1;
		}
		else {
			pickObject = world.tileObjects.get(pickId-1);
			Prefabs.pickedComponent = pickObject.getPrefab().interactionComponent;
			Prefabs.pickedComponentIndex = pickObject.intractionComponentIndex;
		}
	}
	
	public void release() {
		pick.release();
	}
}
