package com.xrbpowered.ruins.world.item;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.PlayerEntity;

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
		Ruins.glare.glare(0.75f);
		return true;
	}
	
}
