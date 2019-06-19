package com.xrbpowered.ruins;

import java.awt.Color;
import java.awt.event.KeyEvent;

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
import com.xrbpowered.ruins.entity.PlayerActor;
import com.xrbpowered.ruins.entity.PlayerController;
import com.xrbpowered.ruins.render.WallBuilder;
import com.xrbpowered.ruins.render.prefab.PrefabComponent;
import com.xrbpowered.ruins.render.prefab.Prefabs;
import com.xrbpowered.ruins.render.shader.WallShader;
import com.xrbpowered.ruins.render.texture.TextureAtlas;
import com.xrbpowered.ruins.world.World;

public class Ruins extends UIClient {

	private WallShader shader;
	private TextureAtlas atlas;
	private StaticMesh[] mapMeshes;
	private CameraActor camera = null;
	
	private PlayerController playerController;
	private Controller observerController;
	private Controller activeController;
	
	private StaticMesh groundMesh;
	private Texture groundTexture;
	
	private WallShader cellObjShader;

	public Texture checker;

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
				
				shader = new WallShader();
				shader.setFog(10, 80, clearColor);
				shader.setCamera(camera);
				
				cellObjShader = new WallShader("tileobj_v.glsl", "wall_f.glsl") {
					@Override
					protected int bindAttribLocations() {
						return PrefabComponent.bindShader(this, super.bindAttribLocations());
					}
				};
				cellObjShader.setFog(10, 80, clearColor);
				cellObjShader.setCamera(camera);
				
				atlas = new TextureAtlas();
				
				//checker = new Texture("test/sand32_128.png", true, false);
				//checker = new Texture("palm.png", true, false);
				checker = new Texture("test/sand64.png", true, false);

				player = new PlayerActor();
				
				playerController = new PlayerController(input, player);
				playerController.moveSpeed = 2.5f;
				observerController = new Controller(input).setActor(camera);
				observerController.moveSpeed = 10f;
				activeController = playerController;
				activeController.setMouseLook(true);

				groundTexture = new Texture("ground.png", true, false);
				groundMesh = FastMeshBuilder.plane(256f, 4, 128, WallShader.vertexInfo, null);
				//cellObjTexture = new Texture(new Color(0xeee3c3));
				
				Prefabs.createResources();
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
				Prefabs.drawInstances();
				cellObjShader.unuse();
			}
		};
		
		new UIFpsOverlay(this);
	}
	
	private void createWorldResources() {
		World world = World.createWorld(System.currentTimeMillis());
		mapMeshes = WallBuilder.createChunks(world, atlas);

		Prefabs.createInstances(world);
		/*plot = new PrefabComponent("plot.obj", new Texture("test/sand64.png", true, false));
		plot.setInstanceData(world);
		palmT = new PrefabComponent("palm_t3.obj", new Texture("palm_t.png", true, false));
		palmT.setInstanceData(world);
		palm = new PrefabComponent("palm.obj", new Texture("palm.png", true, false));
		palm.culling = false;
		palm.yoffs = 3f;
		palm.scale = 1.25f;
		palm.setInstanceData(world);*/

		playerController.collider.world = world;
		player.position.x = world.startx * 2f;
		player.position.z = world.startz * 2f;
		player.position.y = 1f; 
		player.rotation.y = (float) Math.toRadians(-180f);
		player.camera = camera;
		player.updateTransform();
	}
	
	private void releaseWorldResources() {
		Prefabs.releaseInstances();
		for(StaticMesh mesh : mapMeshes) {
			if(mesh!=null)
				mesh.release();
		}
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
