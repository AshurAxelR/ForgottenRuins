package com.xrbpowered.ruins.world.obj;

import java.util.Random;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.item.Item;

public class Jar extends SmallObject {

	public Item item = null;
	public int coins = 0;
	public boolean broken = false;
	
	public Jar(World world, Random random) {
		super(world);
		int n = random.nextInt(25); 
		if(n==0)
			item = Item.amuletOfEscape;
		else if(n==1)
			item = Item.amuletOfRadiance;
		else if(n<4)
			item = Item.emptyFlask;
		else if(n<6)
			item = Item.healingHerbs;
		else {
			coins = random.nextInt(8)-4;
			if(coins<0) {
				coins = 0;
				if(random.nextInt(4)<2)
					broken = true;
			}
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
		return broken ? PrefabRenderer.broken : PrefabRenderer.jar1;
	}
	
	@Override
	public String getPickName() {
		return "Jar";
	}
	
	@Override
	public String getActionString() {
		return coins>0 || item!=null ? "[Right-click to search]" : "[Empty]";
	}
	
	@Override
	public void interact() {
		if(!broken) {
			if(item!=null) {
				Ruins.hud.popup.popup("Found "+item.countString(1));
				world.player.inventory.add(item, 1);
				item = null;
			}
			else if(coins>1)
				Ruins.hud.popup.popup(String.format("Found %d coins", coins));
			else if(coins==1)
				Ruins.hud.popup.popup("Found a coin");
			else
				Ruins.hud.popup.popup("Nothing here");
			world.player.coins += coins;
			coins = 0;
			broken = true;
			Ruins.prefabs.updateAllInstances(world);
		}
	}
}
