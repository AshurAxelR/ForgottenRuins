package com.xrbpowered.ruins.world.obj;

import java.util.Random;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.Prefabs;
import com.xrbpowered.ruins.world.World;

public class Jar extends SmallObject {

	public int coins;
	public boolean broken = false;
	
	public Jar(World world, Random random) {
		super(world);
		coins = random.nextInt(8)-4;
		if(coins<0) {
			coins = 0;
			if(random.nextInt(4)<2)
				broken = true;
		}
	}
	
	@Override
	public float getScaleRange() {
		return 0.25f;
	}
	
	@Override
	public float getRadius(float scale) {
		return 0.25f*scale;
	}

	@Override
	public Prefab getPrefab() {
		return broken ? Prefabs.broken : Prefabs.jar1;
	}
	
	@Override
	public String getPickName() {
		return "Jar";
	}
	
	@Override
	public String getActionString() {
		return coins>0 ? "[Right-click to search]" : "[Empty]";
	}
	
	@Override
	public void interact() {
		if(!broken) {
			if(coins>1)
				Ruins.hud.popup.popup(String.format("Found %d coins", coins));
			else if(coins==1)
				Ruins.hud.popup.popup("Found a coin");
			else
				Ruins.hud.popup.popup("Nothing here");
			world.player.coins += coins;
			coins = 0;
			broken = true;
			Prefabs.updateAllInstances(world);
		}
	}
}
