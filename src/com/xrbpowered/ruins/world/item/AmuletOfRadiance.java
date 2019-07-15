package com.xrbpowered.ruins.world.item;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.mob.MobEntity;
import com.xrbpowered.ruins.entity.player.PlayerEntity;

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
			if(player.getDistTo(e)<10f)
				e.alive = false;
		}
		Ruins.ruins.setOverlay(null);
		Ruins.glare.glare(1.2f);
		return true;
	}

}
