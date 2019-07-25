package com.xrbpowered.ruins.world.obj;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.EntityController;
import com.xrbpowered.ruins.entity.player.PlayerEntity;
import com.xrbpowered.ruins.render.effect.particle.Particle;
import com.xrbpowered.ruins.render.effect.particle.ParticleEffect;
import com.xrbpowered.ruins.render.effect.particle.ParticleRenderer;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;
import com.xrbpowered.ruins.world.item.EmptyFlask;

public class Well extends TileObject {

	public static final int genSpan = 16;
	
	public static int countExtraWells(int level) {
		return World.getSize(level)/8;
	}
	
	public Well(World world, Token objToken) {
		super(world, objToken);
	}

	@Override
	public Prefab getPrefab() {
		return PrefabRenderer.well;
	}
	
	@Override
	public String getPickName() {
		return "Water Well";
	}
	
	@Override
	public String getActionString() {
		return "[Right-click to drink]";
	}
	
	@Override
	public void interact() {
		world.player.hydration = PlayerEntity.baseHydration;
		EmptyFlask.fill(world.player.inventory);
		effect.pivot.set(position);
		effect.pivot.y += 0.5;
		effect.generate();
		Ruins.glare.smallGlare();
		Ruins.hud.popup.popup("Refreshing...");
		Ruins.hud.updateInventoryPreview();
	}

	public static ParticleEffect effect = new ParticleEffect.Up(0.5f, 0.5f, 0f) {
		@Override
		public void generateParticle() {
			Particle p = new Particle(random(0.75f, 1.25f)) {
				@Override
				public boolean updateTime(float dt) {
					speed.y -= EntityController.gravity*dt;
					return super.updateTime(dt);
				}
			};
			assign(p);
			ParticleRenderer.light.add(p);
		}
		@Override
		public void generate() {
			generate(20);
		}
	}.setSpeed(2.5f, 5f);
}
