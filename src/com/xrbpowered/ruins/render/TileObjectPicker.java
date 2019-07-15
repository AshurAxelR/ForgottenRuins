package com.xrbpowered.ruins.render;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.scene.ActorPicker;
import com.xrbpowered.gl.scene.CameraActor;
import com.xrbpowered.gl.scene.StaticMeshActor;
import com.xrbpowered.ruins.render.prefab.PrefabComponent;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.MapObject;

public class TileObjectPicker {

	public static final float reach = 3.0f;
	
	private ActorPicker pick;

	private World world;
	private WallChunk[] walls;

	public MapObject pickObject = null;
	
	public TileObjectPicker(CameraActor camera) {
		this.pick = new ActorPicker(camera); 
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
				float dist = world.player.position.distance(obj.position);
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
			PrefabRenderer.pickedComponent = null;
			PrefabRenderer.pickedComponentIndex = -1;
		}
		else {
			pickObject = world.objects.get(pickId-1);
			PrefabRenderer.pickedComponent = pickObject.getInteractionComp();
			PrefabRenderer.pickedComponentIndex = pickObject.intractionComponentIndex;
		}
	}
	
	public void release() {
		pick.release();
	}
}
