package com.xrbpowered.ruins;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.lwjgl.glfw.GLFW;
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
import com.xrbpowered.ruins.entity.player.PlayerController;
import com.xrbpowered.ruins.entity.player.PlayerEntity;
import com.xrbpowered.ruins.entity.player.buff.Buff;
import com.xrbpowered.ruins.render.TileObjectPicker;
import com.xrbpowered.ruins.render.WallBuilder;
import com.xrbpowered.ruins.render.WallChunk;
import com.xrbpowered.ruins.render.effect.FlashPane;
import com.xrbpowered.ruins.render.effect.GlarePane;
import com.xrbpowered.ruins.render.effect.TracePathEffect;
import com.xrbpowered.ruins.render.effect.particle.ParticleRenderer;
import com.xrbpowered.ruins.render.effect.particle.ParticleShader;
import com.xrbpowered.ruins.render.effect.xray.UIXRayNode;
import com.xrbpowered.ruins.render.prefab.InstanceShader;
import com.xrbpowered.ruins.render.prefab.MobRenderer;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.render.shader.ShaderEnvironment;
import com.xrbpowered.ruins.render.shader.SkyShader;
import com.xrbpowered.ruins.render.shader.WallShader;
import com.xrbpowered.ruins.render.texture.TextureAtlas;
import com.xrbpowered.ruins.ui.UIHint;
import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.ruins.ui.UIIcon;
import com.xrbpowered.ruins.ui.overlay.UIOverlay;
import com.xrbpowered.ruins.ui.overlay.UIOverlayGameOver;
import com.xrbpowered.ruins.ui.overlay.UIOverlayInventory;
import com.xrbpowered.ruins.ui.overlay.UIOverlayItems;
import com.xrbpowered.ruins.ui.overlay.UIOverlayLevelStart;
import com.xrbpowered.ruins.ui.overlay.UIOverlayMenu;
import com.xrbpowered.ruins.ui.overlay.UIOverlayNewGame;
import com.xrbpowered.ruins.ui.overlay.UIOverlayPortal;
import com.xrbpowered.ruins.ui.overlay.UIOverlayShrine;
import com.xrbpowered.ruins.ui.overlay.UIOverlayVerse;
import com.xrbpowered.ruins.ui.overlay.UIOverlayVictory;
import com.xrbpowered.ruins.world.DifficultyMode;
import com.xrbpowered.ruins.world.Save;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.item.Item;
import com.xrbpowered.ruins.world.obj.Chest;

public class Ruins extends UIClient {

	public static GlobalSettings settings = new GlobalSettings();
	
	public static final float viewDist = 80f;
	public static final float dtLimit = 0.05f;

	private WallChunk[] walls;
	private WallShader shader;
	private TextureAtlas atlas;
	
	private Controller observerController;
	private boolean observerActive = false;
	
	private StaticMesh groundMesh;
	private Texture groundTexture;
	private SkyShader skyShader;
	private StaticMesh skyMesh;

	private PlayerEntity player;
	private CameraActor camera;

	public static World world;
	public static boolean preview = true;
	public static boolean pause = false;

	public static PrefabRenderer prefabs;
	public static MobRenderer mobs;
	public static ParticleRenderer particles;
	
	public static Ruins ruins;
	public static UIHud hud;
	public static FlashPane flash;
	public static GlarePane glare;
	public static TileObjectPicker pick;

	private ShaderEnvironment environment = new ShaderEnvironment();

	public static UIXRayNode xray;
	
	private UIOverlay activeOverlay = null;
	
	public static UIOverlayInventory overlayInventory;
	public static UIOverlayItems overlayItems;
	public static UIOverlayVerse overlayVerse;
	public static UIOverlayShrine overlayShrine;
	public static UIOverlayPortal overlayPortal;
	
	public static UIOverlayMenu overlayMenu;
	public static UIOverlayNewGame overlayNewGame;
	public static UIOverlayLevelStart overlayLevelStart;
	public static UIOverlayGameOver overlayGameOver;
	public static UIOverlayVictory overlayVictory;
	
	public Ruins() {
		super("Forgotten Ruins");
		fullscreen = settings.fullscreen;
		windowedWidth = settings.windowedWidth;
		windowedHeight = settings.windowedHeight;
		vsync = settings.vsync;
		noVsyncSleep = settings.noVsyncSleep;
		updatePixelSize();
		ruins = this;
		
		AssetManager.defaultAssets = new FileAssetManager("assets", AssetManager.defaultAssets);
		UIHud.initFonts();
		
		new UIOffscreen(getContainer(), settings.renderScaling) {
			@Override
			public void setSize(float width, float height) {
				super.setSize(width, height);
				camera.setAspectRatio(getWidth(), getHeight());
			}
			
			@Override
			public void setupResources() {
				Item.loadIcons();
				Buff.loadIcons();
				
				clearColor = new Color(0xf7f7ee);
				environment.setSkyColor(new Color(0xd5e5ff), new Color(0xe5efee), clearColor, 0.4f);
				environment.setFog(10, viewDist-0.1f);
				environment.lightScale = 0.1f;
				
				camera = new CameraActor.Perspective().setFov(settings.fov).setRange(0.1f, viewDist).setAspectRatio(getWidth(), getHeight());
				pick = new TileObjectPicker(camera);
				
				shader = (WallShader) new WallShader().setEnvironment(environment).setCamera(camera);
				atlas = new TextureAtlas();
				
				observerController = new Controller(input).setActor(camera);
				observerController.moveSpeed = 10f;

				groundTexture = new Texture("ground.png", true, false);

				skyShader = (SkyShader) new SkyShader().setEnvironment(environment).setCamera(camera);
				skyMesh = FastMeshBuilder.sphere(viewDist-0.1f, 64, SkyShader.vertexInfo, null);
				
				InstanceShader.createInstance(environment, camera);
				prefabs = new PrefabRenderer();
				mobs = new MobRenderer();
				ParticleShader.createInstance(environment, camera);
				particles = new ParticleRenderer();
				
				if(!settings.debugSkipLoad && Save.autosave.exists()) {
					preview = false;
					world = Save.autosave.load();
					if(world!=null) {
						world.player.setClient(input, camera);
						createWorldResources();
						world.update(0f);
					}
					else {
						restartPreview();
					}
				}
				else {
					restartPreview();
				}
				overlayMenu.show();
				
				super.setupResources();
			}
			
			@Override
			public void updateTime(float dt) {
				if(world!=null && !preview && !pause) {
					if(observerActive)
						observerController.update(dt);
					world.update((dt>dtLimit) ? dtLimit : dt);
					particles.update(dt);
				}
				super.updateTime(dt);
			}
			
			@Override
			protected void renderBuffer(RenderTarget target) {
				if(world==null)
					return;
				
				if(PlayerEntity.pathsThread.requestRefresh) {
					PlayerEntity.pathsThread.requestRefresh = false;
					TracePathEffect.update(world);
				}
				
				WallChunk.zsort(walls, camera);
				
				GL11.glEnable(GL11.GL_CULL_FACE);
				if(!observerActive)
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
				
				skyShader.use();
				skyMesh.draw();
				skyShader.unuse();
				
				prefabs.drawInstances();
				mobs.updateInstances(world);
				mobs.drawInstances();
				
				particles.drawInstances(target);
				
				//GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				//pick.update(target, true);
			}
		};
		
		xray = new UIXRayNode(getContainer());
		
		glare = new GlarePane(getContainer());
		flash = new FlashPane(getContainer());
		
		overlayInventory = new UIOverlayInventory(getContainer());
		hud = new UIHud(getContainer());

		overlayItems = new UIOverlayItems(getContainer());
		overlayVerse = new UIOverlayVerse(getContainer());
		overlayShrine = new UIOverlayShrine(getContainer());
		overlayPortal = new UIOverlayPortal(getContainer());

		overlayMenu = new UIOverlayMenu(getContainer());
		overlayNewGame = new UIOverlayNewGame(getContainer());
		overlayLevelStart = new UIOverlayLevelStart(getContainer());
		overlayGameOver = new UIOverlayGameOver(getContainer());
		overlayVictory = new UIOverlayVictory(getContainer());
		
		if(settings.showFps)
			new UIFpsOverlay(this);
		
		UIHint.show(UIOverlayInventory.keyHint);
	}
	
	public CameraActor getCamera() {
		return camera;
	}
	
	public void updatePixelSize() {
		int s = Math.max(UIIcon.minPixelSize, Math.min(settings.uiScaling,
				Math.min(getFrameWidth() / (GlobalSettings.minWindowWidth/4), getFrameHeight() / (GlobalSettings.minWindowHeight/4)
			)));
		if(s!=UIIcon.pixelSize)
			UIIcon.setPixelSize(s, this);
	}
	
	@Override
	public void resizeResources() {
		updatePixelSize();
		super.resizeResources();
	}
	
	private void createWorldResources() {
		player = world.player;
		walls = WallBuilder.createChunks(world, atlas);
		groundMesh = WallBuilder.createGround(world, viewDist);

		prefabs.createInstances(world);
		mobs.allocateInstanceData(world);
		pick.setWorld(world, walls);

		if(preview) {
			camera.position.z = -8f;
			camera.position.y = World.height/4f;
			camera.updateTransform();
		}
	}
	
	private void releaseWorldResources() {
		if(world!=null) {
			prefabs.releaseInstances();
			mobs.releaseInstances();
			particles.clear();
			for(WallChunk wall : walls)
				wall.release();
			groundMesh.release();
		}
	}
	
	public void save() {
		if(!settings.debugSkipLoad)
			if(world!=null && !preview)
				Save.autosave.save(world);
			else
				Save.autosave.delete();
	}

	public void restartPreview() {
		restart(DifficultyMode.peaceful, settings.previewLevel, null, true);
	}
	
	public void restart(DifficultyMode difficulty) {
		restart(difficulty, settings.debugStartLevel, null, false);

		Chest.addRandomItems(player.inventory, settings.debugStartBoost);
		if(settings.debugLearnVerses>0) {
			player.verses.learnRandom(settings.debugLearnVerses);
			prefabs.updateAllInstances(world);
		}
	}
	
	public void restart(DifficultyMode difficulty, int level, PlayerEntity player, boolean preview) {
		releaseWorldResources();
		Ruins.preview = preview;
		world = World.createWorld(difficulty, System.currentTimeMillis(), level);
		world.setPlayer(new PlayerEntity(world, player, input, camera));
		createWorldResources();
		save();
		glare.glare(1.0f);
		if(!preview)
			overlayLevelStart.show(world);
	}
	
	public void grabMouse(boolean grab) {
		if(world==null)
			return;
		if(observerActive)
			observerController.setMouseLook(grab);
		else
			player.controller.setMouseLook(grab);
		player.controller.enabled = grab && !observerActive;
		/*if(!grab) { // FIXME mouse pos vs consequent setOverlay
			input.setMousePos(getFrameWidth()/2, getFrameHeight()/2);
		}*/
	}
	
	public boolean isOverlayActive() {
		return activeOverlay!=null;
	}
	
	public boolean isOverlayActive(UIOverlay overlay) {
		return activeOverlay==overlay;
	}
	
	public void setOverlay(UIOverlay overlay) {
		if(activeOverlay!=overlay) {
			if(activeOverlay!=null)
				activeOverlay.setVisible(false);
			activeOverlay = overlay;
			if(player!=null)
				player.controller.enabled = (overlay==null);
			if(overlay!=null) {
				hud.popup.dismiss();
				overlay.setVisible(true);
				grabMouse(false);
			}
			else {
				grabMouse(true);
			}
		}
		getContainer().repaint();
	}
	
	public void enableObserver(boolean enable) {
		observerActive = enable;
		player.camera = enable ? null : camera;
		player.controller.enabled = !enable;
		player.intangible = enable;
		hud.setVisible(!enable);
		if(enable) {
			pick.reset();
			player.controller.setMouseLook(false);
			observerController.setMouseLook(true);
		}
		else {
			observerController.setMouseLook(false);
			player.controller.setMouseLook(true);
		}
	}
	
	@Override
	public void keyPressed(char c, int code) {
		if(isOverlayActive()) {
			activeOverlay.keyPressed(c, code);
			return;
		}
		else {
			if(code==PlayerController.keyJump) {
				if(!observerActive) {
					player.controller.queueJump();
					return;
				}
			}
			switch(code) {
				case KeyEvent.VK_ESCAPE:
					overlayMenu.show();
					break;
				case KeyEvent.VK_TAB:
					if(!observerActive)
						overlayInventory.showInventory(player);
					break;
				case KeyEvent.VK_F1:
					if(settings.debugEnableObserver)
						enableObserver(!observerActive);
					break;
				case KeyEvent.VK_F2:
					world.player.buffs.add(settings.debugGrantBuff);
					break;
				default:
					if(!observerActive && player.alive && Item.keyPressed(code, player)) {
						hud.updateInventoryPreview();
						return;
					}
					super.keyPressed(c, code);
			}
		}
	}
	
	@Override
	public void mouseDown(float x, float y, int button) {
		if(!isOverlayActive()) {
			grabMouse(true);
			if(button==GLFW.GLFW_MOUSE_BUTTON_RIGHT && pick.pickObject!=null && player.alive)
				pick.pickObject.interact();
		}
		super.mouseDown(x, y, button);
	}
	
	@Override
	public void destroyWindow() {
		save();
		super.destroyWindow();
	}
	
	public static void main(String[] args) {
		settings = GlobalSettings.load();
		new Ruins().run();
	}

}
