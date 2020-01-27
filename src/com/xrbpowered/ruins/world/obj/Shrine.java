package com.xrbpowered.ruins.world.obj;

import java.util.Random;

import com.xrbpowered.ruins.RandomUtils;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.WorldEntity;
import com.xrbpowered.ruins.entity.player.buff.Buff;
import com.xrbpowered.ruins.render.effect.particle.Particle;
import com.xrbpowered.ruins.render.effect.particle.ParticleEffect;
import com.xrbpowered.ruins.render.effect.particle.ParticleGenerator;
import com.xrbpowered.ruins.render.effect.particle.ParticleRenderer;
import com.xrbpowered.ruins.render.effect.particle.Particle.GravityParticle;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class Shrine extends TileObject implements WorldEntity {

	public final Buff buff;
	
	private static final Random random = new Random();


	public Shrine(World world, Token objToken) {
		super(world, objToken);
		random.setSeed(seed);
		buff = Buff.getRandom(random);
	}

	@Override
	public Prefab getPrefab() {
		return isActive() ? PrefabRenderer.shrine : PrefabRenderer.shrineOff;
	}

	@Override
	public String getPickName() {
		return "Shrine";
	}
	
	@Override
	public String getActionString() {
		return "[Right-click to interact]";
	}
	
	@Override
	public void interact() {
		Ruins.overlayShrine.show(this);
	}
	
	public boolean isActive() {
		return !world.player.buffs.has(buff);
	}
	
	@Override
	public boolean updateTime(float dt) {
		if(isActive()) {
			effect.pivot.set(position);
			effect.pivot.x -= 0.35 * d.dx;
			effect.pivot.z -= 0.35 * d.dz;
			effect.pivot.y += 1.9;
			generator.update(dt);
		}
		return true;
	}
	
	public static ParticleEffect effect = new ParticleEffect.Up(0.15f, 0.15f, 0f) {
		@Override
		public void generateParticle() {
			Particle p = new GravityParticle(0.25f, RandomUtils.random(0.5f, 0.75f));
			assign(p);
			ParticleRenderer.light.add(p);
		}
		@Override
		public void generate() {
			generateParticle();
		}
	};
	
	private static ParticleGenerator generator = new ParticleGenerator(effect, 0.02f, 0.04f);
}
