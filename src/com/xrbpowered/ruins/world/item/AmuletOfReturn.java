package com.xrbpowered.ruins.world.item;

import java.awt.event.KeyEvent;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.PlayerActor;

public class AmuletOfReturn extends Item {

	public AmuletOfReturn() {
		super("Amulet of Return", "icons/return.png", "Use: instantly returns you to the starting location");
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
	public boolean use(PlayerActor player) {
		player.returnToStart();
		Ruins.ruins.setOverlay(null);
		return true;
	}
	
}
