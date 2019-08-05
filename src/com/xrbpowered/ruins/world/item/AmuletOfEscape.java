package com.xrbpowered.ruins.world.item;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.PlayerEntity;
import com.xrbpowered.ruins.render.effect.particle.Particle;
import com.xrbpowered.ruins.render.effect.particle.ParticleEffect;
import com.xrbpowered.ruins.render.effect.particle.ParticleRenderer;
import com.xrbpowered.ruins.render.effect.particle.RandomUtils;

public class AmuletOfEscape extends Item {

	public AmuletOfEscape() {
		super("Amulet of Escape", "icons/return.png", new Color(0xbaa670),
				"Use: instantly returns you to the starting location");
		hotkey = KeyEvent.VK_E;
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
		player.returnToStart();
		Ruins.ruins.setOverlay(null);
		effect.pivot.set(player.position);
		effect.generate();
		Ruins.glare.glare(0.75f);
		return true;
	}
	
	public static ParticleEffect effect = new ParticleEffect.Up(1f, 1f, 0f, 2f) {
		@Override
		public void generateParticle() {
			Particle p = new Particle(RandomUtils.random(1f, 1.5f));
			assign(p);
			ParticleRenderer.light.add(p);
		}
		@Override
		public void generate() {
			generate(50);
		}
	}.setSpeed(0.5f, 1f);

}
