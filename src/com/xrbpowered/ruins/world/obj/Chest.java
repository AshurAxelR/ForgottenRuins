package com.xrbpowered.ruins.world.obj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import com.xrbpowered.ruins.RandomUtils;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.PlayerInventory;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;
import com.xrbpowered.ruins.world.item.Item;
import com.xrbpowered.ruins.world.item.ItemList;

public class Chest extends TileObject {

	public final ItemList loot = new ItemList();
	public final boolean royal;
	public boolean locked = false;
	public boolean visited = false;
	
	private static final int minItems = 2;
	private static final int maxItems = 4;
	private static final int royalMultiplier = 3;
	
	private static float[] witems = {0.7f, 0.1f, 0.5f, 2f, 0.5f, 0.2f};
	private static Item[] items = {Item.key, Item.royalKey, Item.amuletOfEscape, Item.amuletOfRadiance, Item.emptyFlask, Item.healingHerbs};
	
	private static final Random random = new Random();
	
	public Chest(World world, Token objToken, boolean royal) {
		super(world, objToken);
		this.royal = royal;
		random.setSeed(seed);
		locked = royal || random.nextInt(3) < 2;
		int n = RandomUtils.random(random, minItems, maxItems);
		if(royal) {
			n *= royalMultiplier;
			loot.add(Item.treasure, 1);
		}
		for(int i=0; i<n; i++) {
			Item item = items[RandomUtils.weighted(random, witems)];
			loot.add(item, 1);
		}
	}
	
	public Chest addRoyalKey() {
		loot.add(Item.royalKey, 1);
		return this;
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
		return royal ? (visited ? PrefabRenderer.chestGoldOpen : PrefabRenderer.chestGold) : (visited ? PrefabRenderer.chestOpen : PrefabRenderer.chest);
	}
	
	@Override
	public String getPickName() {
		return visited ? "Looted Chest" : royal ? "Royal Chest" : locked ? "Locked Chest" : "Chest";
	}
	
	@Override
	public String getActionString() {
		if(visited)
			return "[Empty]";
		else {
			Item key = key();
			if(locked && !world.player.inventory.has(key))
				return String.format("[Requires %s]", key.name);
			else
				return "[Right-click to open]";
		}
	}
	
	public Item key() {
		return royal ? Item.royalKey : Item.key;
	}
	
	public boolean unlock() {
		Item key = key();
		PlayerInventory inv = world.player.inventory;
		if(locked) {
			if(inv.has(key)) {
				inv.add(key, -1);
				return true;
			}
			else
				return false;
		}
		else
			return true;
	}
	
	@Override
	public void interact() {
		if(!visited && unlock()) {
			visited = true;
			loot.addTo(world.player.inventory);
			Ruins.prefabs.updateAllInstances(world);
			Ruins.overlayItems.showItems(royal ? "ROYAL CHEST" : "CHEST", loot);
		}
	}

}
