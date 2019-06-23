package com.xrbpowered.ruins;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.lwjgl.opengl.GL11;

import com.xrbpowered.gl.client.UIClient;
import com.xrbpowered.gl.res.asset.AssetManager;
import com.xrbpowered.gl.res.asset.FileAssetManager;
import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.scene.CameraActor;
import com.xrbpowered.gl.scene.Controller;
import com.xrbpowered.gl.ui.common.UIFpsOverlay;
import com.xrbpowered.gl.ui.pane.UIOffscreen;
import com.xrbpowered.ruins.entity.PlayerActor;
import com.xrbpowered.ruins.render.WallBuilder;
import com.xrbpowered.ruins.render.effects.FlashPane;
import com.xrbpowered.ruins.render.prefab.Prefabs;
import com.xrbpowered.ruins.render.shader.ShaderEnvironment;
import com.xrbpowered.ruins.render.shader.WallShader;
import com.xrbpowered.ruins.render.texture.TextureAtlas;
import com.xrbpowered.ruins.ui.Hud;
import com.xrbpowered.ruins.world.World;

public class Ruins extends UIClient {

	private WallShader shader;
	private TextureAtlas atlas;
	private StaticMesh[] mapMeshes;
	//private CameraActor camera = null;
	
	private Controller observerController;
	private Controller activeController;
	
	private StaticMesh groundMesh;
	private Texture groundTexture;
	
	//private WallShader tileObjShader;

	public Texture checker;

	private PlayerActor player = new PlayerActor(input);

	public static ShaderEnvironment environment = new ShaderEnvironment();
	public static FlashPane flash;
	
	public Ruins() {
		super("Ruins Generator", 1f);
		
		AssetManager.defaultAssets = new FileAssetManager("assets", AssetManager.defaultAssets);
		
		new UIOffscreen(getContainer()) {
			@Override
			public void setSize(float width, float height) {
				super.setSize(width, height);
				player.camera.setAspectRatio(getWidth(), getHeight());
			}
			
			@Override
			public void setupResources() {
				clearColor = new Color(0xe5efee);
				environment.setFog(10, 80, clearColor);
				environment.lightScale = 0.1f;
				
				player.camera = new CameraActor.Perspective().setRange(0.1f, 80f).setAspectRatio(getWidth(), getHeight());
				
				shader = (WallShader) new WallShader().setEnvironment(environment).setCamera(player.camera);
				
				atlas = new TextureAtlas();
				
				//checker = new Texture("test/sand32_128.png", true, false);
				//checker = new Texture("palm.png", true, false);
				checker = new Texture("test/sand64.png", true, false);

				observerController = new Controller(input).setActor(player.camera);
				observerController.moveSpeed = 10f;
				activeController = player.controller;
				activeController.setMouseLook(true);

				groundTexture = new Texture("ground.png", true, false);
				groundMesh = WallBuilder.createGround(80f); //FastMeshBuilder.plane(256f, 4, 128, WallShader.vertexInfo, null);
				//cellObjTexture = new Texture(new Color(0xeee3c3));
				
				Prefabs.createResources(environment, player.camera);
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
				player.updateTime(dt);
				super.updateTime(dt);
			}
			
			@Override
			protected void renderBuffer(RenderTarget target) {
				super.renderBuffer(target);
				
				GL11.glEnable(GL11.GL_CULL_FACE);
				shader.use();
				
				atlas.getTexture().bind(0);
				for(StaticMesh mesh : mapMeshes) {
					if(mesh!=null)
						mesh.draw();
				}
				
				groundTexture.bind(0);
				groundMesh.draw();
				shader.unuse();
				
				Prefabs.drawInstances();
			}
		};
		
		flash = new FlashPane(getContainer());
		new Hud(getContainer(), player);
		new UIFpsOverlay(this);
	}
	
	private void createWorldResources() {
		World world = World.createWorld(System.currentTimeMillis());
		mapMeshes = WallBuilder.createChunks(world, atlas);

		Prefabs.createInstances(world);

		player.reset(world);
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
			activeController = (activeController==player.controller) ? observerController : player.controller;
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
