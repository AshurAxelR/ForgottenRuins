package com.xrbpowered.ruins.world.item;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.PlayerEntity;

public class WaterFlask extends Item {

	public static final int restore = 20;
	
	public WaterFlask(int id) {
		super(id, "Flask of Water", "icons/water_flask.png", new Color(0x0055aaee),
				String.format("Use: %+d Water", restore));
		hotkey = KeyEvent.VK_F;
	}

	@Override
	public boolean isConsumable() {
		return true;
	}
	
	@Override
	public boolean use(PlayerEntity player) {
		if(Math.round(player.hydration)<PlayerEntity.baseHydration-5f) {
			player.hydration += restore;
			if(player.hydration>PlayerEntity.baseHydration)
				player.hydration = PlayerEntity.baseHydration;
			player.inventory.add(Item.emptyFlask, 1);
			Ruins.glare.smallGlare();
			Ruins.hud.popup.popup("Refreshing...");
			return true;
		}
		else {
			Ruins.hud.popup.popup("Not thirsty");
			return false;
		}
	}
	
}
