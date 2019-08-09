package com.xrbpowered.ruins.world.item;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.PlayerEntity;

public class HealingHerbs extends Item {

	public static final int restoreHealth = 20;
	public static final int waterCost = -10;

	public HealingHerbs(int id) {
		super(id, "Healing Herbs", "icons/herbs.png", new Color(0xfab7cc),
				String.format("Use: %+d Health, %+d Water", restoreHealth, waterCost));
		plural = name;
		hotkey = KeyEvent.VK_Q;
	}

	public String countString(int count) {
		return count==1 ? name : String.format("%d %s", count, name);
	}
	
	@Override
	public boolean isConsumable() {
		return true;
	}
	
	@Override
	public boolean use(PlayerEntity player) {
		if(Math.round(player.health)<PlayerEntity.baseHealth) {
			player.health += restoreHealth;
			if(player.health>PlayerEntity.baseHealth)
				player.health = PlayerEntity.baseHealth;
			player.hydration += waterCost;
			if(player.hydration<0)
				player.hydration = 0;
			Ruins.glare.smallGlare();
			Ruins.hud.popup.popup("A touch of healing...");
			return true;
		}
		else {
			Ruins.hud.popup.popup("Not injured");
			return false;
		}
	}

}
