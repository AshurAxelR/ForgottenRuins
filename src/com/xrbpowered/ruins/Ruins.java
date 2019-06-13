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
import com.xrbpowered.gl.ui.common.UIFpsOverlay;
import com.xrbpowered.gl.ui.pane.UIOffscreen;

public class Ruins extends UIClient {

	private MapShader shader;
	private MapTextureAtlas atlas;
	private StaticMesh[] mapMeshes;
	private CameraActor camera = null; 
	private PlayerController controller;
	
	private StaticMesh groundMesh;
	private Texture groundtexture;
	
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
				
				shader = MapShader.getInstance();
				shader.setFog(10, 80, clearColor);
				
				atlas = new MapTextureAtlas();
				//texture = new Texture("map.png", true, false);
				//texture = new Texture("checker.png", true, true);

				//mesh = FastMeshBuilder.cube(1f, MapShader.vertexInfo, null);
				//mesh = ObjMeshLoader.loadObj("test.obj", 0, 1f, MapShader.vertexInfo, null);
				
				player = new PlayerActor();
				controller = new PlayerController(input, player);
				//controller = new Controller(input).setActor(player);
				controller.moveSpeed = 2.5f;
				controller.setMouseLook(true);

				groundtexture = new Texture("ground.png", true, false);
				groundMesh = FastMeshBuilder.plane(256f, 4, 128, MapShader.vertexInfo, null);
				
				createWorldResources();
				
				super.setupResources();
			}
			
			@Override
			public boolean onMouseDown(float x, float y, Button button, int mods) {
				controller.setMouseLook(true);
				return true;
			}
			
			@Override
			public void updateTime(float dt) {
				controller.update(dt);
				super.updateTime(dt);
			}
			
			@Override
			protected void renderBuffer(RenderTarget target) {
				super.renderBuffer(target);
				
				shader.setCamera(camera);
				shader.use();
				GL11.glEnable(GL11.GL_CULL_FACE);
				
				shader.setLightScale(0.1f);
				atlas.getTexture().bind(0);
				for(StaticMesh mesh : mapMeshes) {
					if(mesh!=null)
						mesh.draw();
				}
				
				shader.setLightScale(0f);
				groundtexture.bind(0);
				groundMesh.draw();
				
				shader.unuse();
				GL11.glDisable(GL11.GL_CULL_FACE);
			}
		};
		
		new UIFpsOverlay(this);
	}
	
	private void createWorldResources() {
		WorldMap map = new WorldMap();
		map.generate(new Random());
		mapMeshes = MapBuilder.createChunks(map, atlas);

		controller.collider.map = map;
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
		mapMeshes = null;
	}
	
	@Override
	public void keyPressed(char c, int code) {
		if(code==KeyEvent.VK_ESCAPE)
			requestExit();
		else if(code==KeyEvent.VK_ALT)
			controller.setMouseLook(false);
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
