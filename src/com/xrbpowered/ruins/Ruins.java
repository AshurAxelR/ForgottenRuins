package com.xrbpowered.ruins;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.lwjgl.glfw.GLFW;
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
import com.xrbpowered.ruins.render.TileObjectPicker;
import com.xrbpowered.ruins.render.WallBuilder;
import com.xrbpowered.ruins.render.WallChunk;
import com.xrbpowered.ruins.render.effects.FlashPane;
import com.xrbpowered.ruins.render.prefab.Prefabs;
import com.xrbpowered.ruins.render.shader.ShaderEnvironment;
import com.xrbpowered.ruins.render.shader.WallShader;
import com.xrbpowered.ruins.render.texture.TextureAtlas;
import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.ruins.ui.UIIcon;
import com.xrbpowered.ruins.ui.overlay.UIOverlay;
import com.xrbpowered.ruins.ui.overlay.UIOverlayVerse;
import com.xrbpowered.ruins.world.World;

public class Ruins extends UIClient {

	// settings
	public static float renderScale = 1f; 
	public static boolean enableObserver = true;
	
	private WallChunk[] walls;
	private WallShader shader;
	private TextureAtlas atlas;
	
	private Controller observerController;
	private Controller activeController;
	
	private StaticMesh groundMesh;
	private Texture groundTexture;

	private PlayerActor player = new PlayerActor(input);

	public static World world;

	public static Ruins ruins;
	public static UIHud hud;
	public static FlashPane flash;
	public static TileObjectPicker pick;

	private ShaderEnvironment environment = new ShaderEnvironment();

	private UIOverlay activeOverlay = null;
	
	public static UIOverlayVerse overlayVerse;
	
	public Ruins() {
		super("Ruins Generator");
		ruins = this;
		UIIcon.updatePixelSize(this);
		
		AssetManager.defaultAssets = new FileAssetManager("assets", AssetManager.defaultAssets);
		
		new UIOffscreen(getContainer(), renderScale) {
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
				pick = new TileObjectPicker(player);
				
				shader = (WallShader) new WallShader().setEnvironment(environment).setCamera(player.camera);
				atlas = new TextureAtlas();
				
				observerController = new Controller(input).setActor(player.camera);
				observerController.moveSpeed = 10f;
				activeController = player.controller;
				activeController.setMouseLook(true);

				groundTexture = new Texture("ground.png", true, false);
				groundMesh = WallBuilder.createGround(80f);
				
				Prefabs.createResources(environment, player.camera);
				createWorldResources();
				
				super.setupResources();
			}
			
			@Override
			public void updateTime(float dt) {
				if(activeController==observerController)
					observerController.update(dt);
				else
					player.updateTime(dt);
				super.updateTime(dt);
			}
			
			@Override
			protected void renderBuffer(RenderTarget target) {
				WallChunk.zsort(walls, player.camera);
				
				GL11.glEnable(GL11.GL_CULL_FACE);
				pick.update(target);

				super.renderBuffer(target);
				shader.use();
				
				atlas.getTexture().bind(0);
				//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				for(WallChunk wall : walls)
					wall.drawVisible();

				groundTexture.bind(0);
				groundMesh.draw();
				shader.unuse();
				
				Prefabs.drawInstances();
				//pick.update(target, true);
			}
		};
		
		flash = new FlashPane(getContainer());
		hud = new UIHud(getContainer(), player);
		
		overlayVerse = new UIOverlayVerse(getContainer());
		
		new UIFpsOverlay(this);
	}
	
	private void createWorldResources() {
		world = World.createWorld(System.currentTimeMillis(), player);
		walls = WallBuilder.createChunks(world, atlas);

		Prefabs.createInstances(world);

		player.reset(world);
		pick.setWorld(world, walls);
	}
	
	private void releaseWorldResources() {
		Prefabs.releaseInstances();
		for(WallChunk wall : walls)
			wall.release();
	}
	
	public void grabMouse(boolean grab) {
		activeController.setMouseLook(grab);
		player.controller.enabled = grab;
		if(!grab) {
			input.setMousePos(getWidth()/2, getHeight()/2);
		}
	}
	
	public boolean isOverlayActive() {
		return activeOverlay!=null;
	}
	
	public void setOverlay(UIOverlay overlay) {
		if(activeOverlay!=null)
			activeOverlay.setVisible(false);
		activeOverlay = overlay;
		if(overlay!=null) {
			overlay.setVisible(true);
			grabMouse(false);
		}
		else {
			grabMouse(true);
		}
		getContainer().repaint();
	}
	
	@Override
	public void keyPressed(char c, int code) {
		if(isOverlayActive())
			return; // TODO overlay keys

		if(code==player.controller.keyJump) {
			if(activeController==player.controller) {
				player.controller.queueJump();
				return;
			}
		}
		switch(code) {
			case KeyEvent.VK_ESCAPE:
				requestExit();
				break;
			case KeyEvent.VK_F1:
				if(code==KeyEvent.VK_F1 && enableObserver) {
					activeController.setMouseLook(false);
					activeController = (activeController==player.controller) ? observerController : player.controller;
					hud.setVisible(activeController==player.controller);
					activeController.setMouseLook(true);
				}
				break;
			case KeyEvent.VK_ALT:
				grabMouse(false);
				break;
			case KeyEvent.VK_BACK_SPACE:
				releaseWorldResources();
				createWorldResources();
				break;
			default:
				super.keyPressed(c, code);
		}
	}
	
	@Override
	public void mouseDown(float x, float y, int button) {
		if(!isOverlayActive()) {
			grabMouse(true);
			if(button==GLFW.GLFW_MOUSE_BUTTON_RIGHT && pick.pickObject!=null)
				pick.pickObject.interact();
		}
		super.mouseDown(x, y, button);
	}
	
	public static void main(String[] args) {
		new Ruins().run();
	}

}
