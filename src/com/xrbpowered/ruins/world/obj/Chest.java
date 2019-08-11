package com.xrbpowered.ruins.world.obj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import com.xrbpowered.ruins.RandomUtils;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;
import com.xrbpowered.ruins.world.item.Item;
import com.xrbpowered.ruins.world.item.ItemList;

public class Chest extends TileObject {

	public final ItemList loot = new ItemList();
	public boolean locked = false;
	public boolean visited = false;
	
	private static final int minItems = 3;
	private static final int maxItems = 7;
	
	private static float[] witems = {1f, 2.5f, 1f, 2f};
	private static Item[] items = {Item.amuletOfEscape, Item.amuletOfRadiance, Item.emptyFlask, Item.healingHerbs};
	
	private static final Random random = new Random();
	
	public Chest(World world, Token objToken) {
		super(world, objToken);
		random.setSeed(seed);
		locked = random.nextInt(3) < 2;
		int n = RandomUtils.random(random, minItems, maxItems);
		for(int i=0; i<n; i++) {
			Item item = items[RandomUtils.weighted(random, witems)];
			loot.add(item, 1);
		}
	}
	
	@Override
	public void loadState(DataInputStream in) throws IOException {
		visited = in.readBoolean();
	}
	
	@Override
	public void saveState(DataOutputStream out) throws IOException {
		out.writeBoolean(visited);
	}

	@Override
	public Prefab getPrefab() {
		return visited ? PrefabRenderer.chestOpen : PrefabRenderer.chest;
	}
	
	@Override
	public String getPickName() {
		return visited ? "Looted Chest" : locked ? "Locked Chest" : "Chest";
	}
	
	@Override
	public String getActionString() {
		if(visited)
			return "[Empty]";
		//else if(locked && noKey) {
		//}
		else
			return "[Right-click to open]";
	}
	
	@Override
	public void interact() {
		if(!visited) {
			visited = true;
			loot.addTo(world.player.inventory);
			Ruins.prefabs.updateAllInstances(world);
			Ruins.overlayItems.showItems("CHEST", loot);
		}
	}

}
