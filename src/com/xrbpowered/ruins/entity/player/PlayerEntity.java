package com.xrbpowered.ruins.entity.player;

import com.xrbpowered.gl.client.ClientInput;
import com.xrbpowered.gl.scene.CameraActor;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.DamageSource;
import com.xrbpowered.ruins.entity.EntityActor;
import com.xrbpowered.ruins.entity.EntityCollider;
import com.xrbpowered.ruins.render.DebugPaths;
import com.xrbpowered.ruins.world.PathFinder;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.item.Item;
import com.xrbpowered.ruins.world.item.ItemList;

public class PlayerEntity extends EntityActor {

	public static final float radius = 0.3f;
	
	public static final int baseHealth = 100;
	public static final int baseHydration = 100;
	public static final float healthRegen = 0.2f;
	public static final float hydrationLoss = 0.2f;
	
	public static final float cameraHeight = 1.5f;
	public static final float cameraDeathHeight = 0.3f;
	
	public final PlayerController controller;
	public CameraActor camera;
	
	private float cameraLevel = cameraHeight;
	
	public DamageSource lastDamageSource = null;
	public float deathTimer = 0f;
	
	public boolean invulnerable = false;
	public float health = baseHealth;
	public float hydration = baseHydration;
	public int coins = 0;
	
	public ItemList inventory = new ItemList();
	
	public PlayerEntity(World world, PlayerEntity prev, ClientInput input, CameraActor camera) {
		super(world);
		this.camera = camera;
		controller = new PlayerController(input, this);
		
		world.pathfinder.clear();
		returnToStart();

		alive = true;
		health = baseHealth;
		hydration = baseHydration;
		coins = 0;
		inventory.clear();
		if(prev==null) {
			inventory.add(Item.emptyFlask, 2);
			inventory.add(Item.amuletOfEscape, 1);
		}
		else {
			// TODO copy verse system to next level
			prev.inventory.moveTo(inventory);
		}
		if(Ruins.flash!=null)
			Ruins.flash.reset();
	}
	
	public void returnToStart() {
		controller.reset();
		
		position.x = world.startx * 2f;
		position.z = world.startz * 2f;
		position.y = 1f;
		rotation.x = 0f;
		rotation.z = 0f;
		rotation.y = (float) Math.toRadians(-180f);
		updateMapPosition();
		updateTransform();
		
		cameraLevel = cameraHeight;
	}
	
	@Override
	public void setEntityDimensions(EntityCollider collider) {
		collider.setEntityDimensions(radius, cameraHeight, PlayerController.dyPoints);
	}
	
	public void updateHealth(float dh, DamageSource souce) {
		if(!alive)
			return;
		lastDamageSource = souce;
		health += dh;
		if(health>baseHealth)
			health = baseHealth;
		if(health<1f) {
			die();
		}
		Ruins.flash.setBaseAlpha(health);
	}
	
	public void die() {
		health = 0f;
		hydration = 0f;
		alive = false;
		deathTimer = 0f;
		Ruins.flash.blackOut();
		Ruins.ruins.setOverlay(null);
	}
	
	public void applyDamage(float damage, boolean flash, DamageSource source) {
		if(health<=0 || damage<0.1f || invulnerable)
			return;
		if(flash)
			Ruins.flash.flashPain(damage, health);
		updateHealth(-damage, source);
	}
	
	public void applyDamage(float damage, DamageSource source) {
		applyDamage(damage, true, source);
	}
	
	@Override
	public void updateTransform() {
		super.updateTransform();
		if(camera!=null) {
			camera.position.set(this.position.x, this.position.y+cameraLevel, this.position.z);
			camera.rotation.set(this.rotation);
			camera.updateTransform();
		}
	}
	
	@Override
	protected boolean updateMapPosition() {
		if(super.updateMapPosition()) {
			PathFinder paths = world.pathfinder;
			if(paths.canUpdate(mapx, mapz, mapy)) {
				paths.clear();
				paths.update(mapx, mapz, mapy, PathFinder.maxPathDist);
			}
			DebugPaths.update(world);
			return true;
		}
		else
			return false;
	}
	
	@Override
	protected void updateController(float dt) {
		controller.update(dt);
	}
	
	@Override
	public boolean updateTime(float dt) {
		super.updateTime(dt);
		if(alive && controller.isDrowning()) {
			cameraLevel -= dt*0.25f;
			if(cameraLevel<=cameraDeathHeight) {
				cameraLevel = cameraDeathHeight;
				lastDamageSource = DamageSource.drown;
				die();
			}
		}
		if(alive) {
			if(!invulnerable) {
				hydration -= hydrationLoss*dt;
				if(hydration<=0f) {
					hydration = 0f;
					Ruins.flash.daze(true);
				}
				else
					Ruins.flash.daze(false);
			}
		}
		else {
			cameraLevel = cameraDeathHeight;
			deathTimer += dt;
			if(deathTimer>2f && !Ruins.overlayGameOver.isActive()) {
				Ruins.overlayGameOver.show(lastDamageSource.gameOverMessage);
			}
		}
		updateHealth(hydration>0f ? healthRegen*dt : -healthRegen*dt, DamageSource.dehydrate);
		return true;
	}

}
