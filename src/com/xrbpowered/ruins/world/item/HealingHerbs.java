package com.xrbpowered.ruins.world.item;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.PlayerActor;

public class HealingHerbs extends Item {

	public static final int restoreHealth = 20;
	public static final int waterCost = -10;

	public HealingHerbs() {
		super("Healing Herbs", "icons/herbs.png", String.format("Use: %+d Health, %+d Water", restoreHealth, waterCost));
	}

	public String countString(int count) {
		return count==1 ? name : String.format("%d %s", count, name);
	}
	
	@Override
	public boolean isConsumable() {
		return true;
	}
	
	@Override
	public boolean use(PlayerActor player) {
		if(Math.round(player.health)<PlayerActor.baseHealth) {
			player.health += restoreHealth;
			if(player.health>PlayerActor.baseHealth)
				player.health = PlayerActor.baseHealth;
			player.hydration += waterCost;
			if(player.hydration<0)
				player.hydration = 0;
			Ruins.hud.popup.popup("A touch of healing...");
			return true;
		}
		else {
			Ruins.hud.popup.popup("Not injured");
			return false;
		}
	}

}
