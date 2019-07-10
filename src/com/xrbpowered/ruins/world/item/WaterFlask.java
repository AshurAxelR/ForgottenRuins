package com.xrbpowered.ruins.world.item;

import java.awt.event.KeyEvent;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.PlayerActor;

public class WaterFlask extends Item {

	public static final int restore = 20;
	
	public WaterFlask() {
		super("Flask of Water", "icons/water_flask.png", String.format("Use: %+d Water", restore));
		hotkey = KeyEvent.VK_F;
	}

	@Override
	public boolean isConsumable() {
		return true;
	}
	
	@Override
	public boolean use(PlayerActor player) {
		if(Math.round(player.hydration)<PlayerActor.baseHydration-5f) {
			player.hydration += restore;
			if(player.hydration>PlayerActor.baseHydration)
				player.hydration = PlayerActor.baseHydration;
			player.inventory.add(Item.emptyFlask, 1);
			Ruins.hud.popup.popup("Refreshing...");
			return true;
		}
		else {
			Ruins.hud.popup.popup("Not thirsty");
			return false;
		}
	}
	
}
