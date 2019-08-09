package com.xrbpowered.ruins.world.item;

import java.awt.Color;
import java.util.HashMap;

import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.ruins.entity.player.PlayerEntity;

public abstract class Item {

	private static HashMap<Integer, Item> items = new HashMap<>();

	public static final Item emptyFlask = new EmptyFlask(0);
	public static final Item waterFlask = new WaterFlask(1);
	public static final Item healingHerbs = new HealingHerbs(2);
	public static final Item amuletOfEscape = new AmuletOfEscape(3);
	public static final Item amuletOfRadiance = new AmuletOfRadiance(4);
	
	public final int id;
	public final String name;
	public String plural;
	public final String iconPath;
	public final Color color;
	public final String info;
	public int hotkey = 0;
	
	public Texture icon = null;
	
	public Item(int id, String name, String iconPath, Color color, String info) {
		this.id = id;
		items.put(id, this);
		this.name = name;
		this.plural = name+"s";
		this.iconPath = iconPath;
		this.color = color;
		this.info = info;
	}
	
	public boolean isConsumable() {
		return false;
	}
	
	public boolean use(PlayerEntity player) {
		return false;
	}
	
	protected String indefArticle() {
		return "a";
	}
	
	public String countString(int count) {
		return count==1 ? String.format("%s %s", indefArticle(), name) : String.format("%d %s", count, plural);
	}
	
	public static Item itemById(int id) {
		return items.get(id);
	}
	
	public static void loadIcons() {
		for(Item item : items.values())
			item.icon = new Texture(item.iconPath, false, false);
	}
	
	public static void releaseResources() {
		for(Item item : items.values())
			item.icon.release();
	}
	
	public static boolean keyPressed(int code, PlayerEntity player) {
		for(Item item : items.values()) {
			if(item.hotkey==code) {
				player.inventory.use(item, player);
				return true;
			}
		}
		return false;
	}

	public static int countItemTypes() {
		return items.size();
	}
}
