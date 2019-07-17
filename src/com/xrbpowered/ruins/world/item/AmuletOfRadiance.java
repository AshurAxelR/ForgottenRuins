package com.xrbpowered.ruins.world.item;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.mob.MobEntity;
import com.xrbpowered.ruins.entity.player.PlayerEntity;
import com.xrbpowered.ruins.render.effect.particle.Particle;
import com.xrbpowered.ruins.render.effect.particle.ParticleEffect;
import com.xrbpowered.ruins.render.effect.particle.ParticleRenderer;

public class AmuletOfRadiance extends Item {

	public AmuletOfRadiance() {
		super("Amulet of Radiance", "icons/radiance.png", new Color(0xd0ecff),
				"Use: destroys all undead up to 4 tiles away");
		hotkey = KeyEvent.VK_R;
	}

	@Override
	protected String indefArticle() {
		return "an";
	}
	
	@Override
	public boolean isConsumable() {
		return true;
	}
	
	@Override
	public boolean use(PlayerEntity player) {
		for(MobEntity e : player.world.mobs) {
			if(e.alive && player.getDistTo(e)<10f)
				e.radiance();
		}
		explosion.pivot.set(player.position);
		explosion.generate();
		Ruins.ruins.setOverlay(null);
		Ruins.glare.glare(1.2f);
		return true;
	}
	
	public static ParticleEffect explosion = new ParticleEffect.Rand(8f) {
		@Override
		public void generateParticle() {
			Particle p = new Particle(random(1.5f, 3f));
			assign(p);
			ParticleRenderer.light.add(p);
		}
		@Override
		public void generate() {
			generate(500);
		}
	}.setSpeed(0.25f, 0.5f);

}
