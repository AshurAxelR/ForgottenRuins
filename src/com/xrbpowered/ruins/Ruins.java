package com.xrbpowered.ruins;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.xrbpowered.gl.client.UIClient;
import com.xrbpowered.gl.res.asset.AssetManager;
import com.xrbpowered.gl.res.asset.FileAssetManager;
import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.res.mesh.FastMeshBuilder;
import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.scene.CameraActor;
import com.xrbpowered.gl.scene.Controller;
import com.xrbpowered.gl.ui.common.UIFpsOverlay;
import com.xrbpowered.gl.ui.pane.UIOffscreen;

public class Ruins extends UIClient {

	private MapShader shader;
	private MapTextureAtlas atlas;
	private StaticMesh[] mapMeshes;
	private CameraActor camera = null;
	
	private PlayerController playerController;
	private Controller observerController;
	private Controller activeController;
	
	private StaticMesh groundMesh;
	private Texture groundTexture;
	
	private MapShader cellObjShader;
	private CellObjectMesh cellObjMesh;
	private Texture cellObjTexture;
	
	private PlayerActor player;
	
	public Ruins() {
		super("Ruins Generator");
		
		AssetManager.defaultAssets = new FileAssetManager("assets", AssetManager.defaultAssets);
		
		new UIOffscreen(getContainer()) {
			@Override
			public void setSize(float width, float height) {
				super.setSize(width, height);
				camera.setAspectRatio(getWidth(), getHeight());
			}
			
			@Override
			public void setupResources() {
				clearColor = new Color(0xe5efee);
				
				camera = new CameraActor.Perspective().setRange(0.1f, 80f).setAspectRatio(getWidth(), getHeight());
				
				shader = new MapShader();
				shader.setFog(10, 80, clearColor);
				shader.setCamera(camera);
				
				cellObjShader = new MapShader("cellobj_v.glsl", "map_f.glsl") {
					@Override
					protected int bindAttribLocations() {
						//return super.bindAttribLocations();
						return CellObjectMesh.bindShader(this, super.bindAttribLocations());
					}
				};
				cellObjShader.setFog(10, 80, clearColor);
				cellObjShader.setCamera(camera);
				
				atlas = new MapTextureAtlas();
				//texture = new Texture("map.png", true, false);
				//texture = new Texture("checker.png", true, true);

				//mesh = FastMeshBuilder.cube(1f, MapShader.vertexInfo, null);
				//mesh = ObjMeshLoader.loadObj("test.obj", 0, 1f, MapShader.vertexInfo, null);
				
				player = new PlayerActor();
				
				playerController = new PlayerController(input, player);
				playerController.moveSpeed = 2.5f;
				observerController = new Controller(input).setActor(camera);
				observerController.moveSpeed = 10f;
				activeController = playerController;
				activeController.setMouseLook(true);

				groundTexture = new Texture("ground.png", true, false);
				groundMesh = FastMeshBuilder.plane(256f, 4, 128, MapShader.vertexInfo, null);
				cellObjTexture = new Texture(new Color(0xeee3c3));
				
				createWorldResources();
				
				super.setupResources();
			}
			
			@Override
			public boolean onMouseDown(float x, float y, Button button, int mods) {
				activeController.setMouseLook(true);
				return true;
			}
			
			@Override
			public void updateTime(float dt) {
				activeController.update(dt);
				super.updateTime(dt);
			}
			
			@Override
			protected void renderBuffer(RenderTarget target) {
				super.renderBuffer(target);
				
				GL11.glEnable(GL11.GL_CULL_FACE);
				shader.use();
				
				shader.setLightScale(0.1f);
				atlas.getTexture().bind(0);
				for(StaticMesh mesh : mapMeshes) {
					if(mesh!=null)
						mesh.draw();
				}
				
				shader.setLightScale(0f);
				groundTexture.bind(0);
				groundMesh.draw();
				shader.unuse();
				
				cellObjShader.use();
				cellObjShader.setLightScale(0.1f);
				cellObjTexture.bind(0);
				cellObjMesh.drawInstances();
				cellObjMesh.mesh.draw();
				cellObjShader.unuse();
				
				GL11.glDisable(GL11.GL_CULL_FACE);
			}
		};
		
		new UIFpsOverlay(this);
	}
	
	private void createWorldResources() {
		WorldMap map = new WorldMap();
		map.generate(new Random());
		mapMeshes = MapBuilder.createChunks(map, atlas);

		cellObjMesh = new CellObjectMesh("test.obj");
		cellObjMesh.setInstanceData(map);

		playerController.collider.map = map;
		player.position.x = map.startx * 2f;
		player.position.z = map.startz * 2f;
		player.position.y = 1f; 
		player.rotation.y = (float) Math.toRadians(-180f);
		player.camera = camera;
		player.updateTransform();
	}
	
	private void releaseWorldResources() {
		for(StaticMesh mesh : mapMeshes) {
			if(mesh!=null)
				mesh.release();
		}
		cellObjMesh.release();
	}
	
	@Override
	public void keyPressed(char c, int code) {
		if(code==KeyEvent.VK_ESCAPE)
			requestExit();
		else if(code==KeyEvent.VK_F1) {
			activeController.setMouseLook(false);
			activeController = (activeController==playerController) ? observerController : playerController;
			activeController.setMouseLook(true);
		}
		else if(code==KeyEvent.VK_ALT)
			activeController.setMouseLook(false);
		else if(code==KeyEvent.VK_BACK_SPACE) {
			releaseWorldResources();
			createWorldResources();
		}
		else
			super.keyPressed(c, code);
	}
	
	public static void main(String[] args) {
		new Ruins().run();
	}

}
