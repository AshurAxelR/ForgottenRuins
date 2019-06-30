package com.xrbpowered.ruins.render;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.scene.ActorPicker;
import com.xrbpowered.gl.scene.StaticMeshActor;
import com.xrbpowered.ruins.entity.PlayerActor;
import com.xrbpowered.ruins.render.prefab.PrefabComponent;
import com.xrbpowered.ruins.render.prefab.Prefabs;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.MapObject;

public class TileObjectPicker {

	public static final float reach = 2.5f;
	
	private ActorPicker pick;

	private PlayerActor player;
	private World world;
	private WallChunk[] walls;

	public MapObject pickObject = null;
	
	public TileObjectPicker(PlayerActor player) {
		this.player = player;
		this.pick = new ActorPicker(player.camera); 
	}
	
	public void setWorld(World world, WallChunk[] walls) {
		this.world = world;
		this.walls = walls;
	}
	
	public void update(RenderTarget target) {
		update(target, false);
	}
	
	public void update(RenderTarget target, boolean test) {
		if(test)
			pick.startPickingTest(target.getWidth()/2, target.getHeight()/2, target);
		else
			pick.startPicking(target.getWidth()/2, target.getHeight()/2, target);
		
		if(!test) {
			for(WallChunk wall : walls) {
				if(wall.getCameraZ()<WallChunk.radius && wall.getCameraZ()>=-WallChunk.radius-reach)
					pick.drawSceneMesh(wall.getMesh(), 0);
			}
		}
		
		int objId = 1;
		StaticMeshActor objActor = new StaticMeshActor();
		for(MapObject obj : world.objects) {
			PrefabComponent comp = obj.getInteractionComp();
			if(comp!=null) {
				float dist = player.position.distance(obj.position);
				if(dist<=reach) {
					obj.copyToActor(objActor);
					objActor.setMesh(comp.mesh);
					pick.drawActor(objActor, objId);
				}
			}
			objId++;
		}
		int pickId = pick.finishPicking(target);
		if(pickId<=0 || pickId>world.objects.size()) {
			pickObject = null;
			Prefabs.pickedComponent = null;
			Prefabs.pickedComponentIndex = -1;
		}
		else {
			pickObject = world.objects.get(pickId-1);
			Prefabs.pickedComponent = pickObject.getInteractionComp();
			Prefabs.pickedComponentIndex = pickObject.intractionComponentIndex;
		}
	}
	
	public void release() {
		pick.release();
	}
}
