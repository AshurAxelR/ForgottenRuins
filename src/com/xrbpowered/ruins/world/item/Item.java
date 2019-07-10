package com.xrbpowered.ruins.world.item;

import java.util.ArrayList;

import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.ruins.entity.PlayerActor;

public abstract class Item {

	private static ArrayList<Item> items = new ArrayList<>();

	public static final Item emptyFlask = new EmptyFlask();
	public static final Item waterFlask = new WaterFlask();
	public static final Item healingHerbs = new HealingHerbs();
	public static final Item amuletOfReturn = new AmuletOfReturn();
	
	public final int id;
	public final String name;
	public final String iconPath;
	public final String info;
	public int hotkey = 0;
	
	public Texture icon = null;
	
	public Item(String name, String iconPath, String info) {
		this.id = items.size();
		items.add(this);
		this.name = name;
		this.iconPath = iconPath;
		this.info = info;
	}
	
	public boolean isConsumable() {
		return false;
	}
	
	public boolean use(PlayerActor player) {
		return false;
	}
	
	protected String indefArticle() {
		return "a";
	}
	
	public String countString(int count) {
		return count==1 ? String.format("%s %s", indefArticle(), name) : String.format("%d %ss", count, name);
	}
	
	public static Item itemById(int id) {
		return items.get(id);
	}
	
	public static void loadIcons() {
		for(Item item : items)
			item.icon = new Texture(item.iconPath, false, false);
	}
	
	public static void releaseResources() {
		for(Item item : items)
			item.icon.release();
	}
	
	public static boolean keyPressed(int code, PlayerActor player) {
		for(Item item : items) {
			if(item.hotkey==code) {
				player.inventory.use(item, player);
				return true;
			}
		}
		return false;
	}
	
}
