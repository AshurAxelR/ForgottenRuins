package com.xrbpowered.ruins.world.item;

import java.awt.Color;
import java.util.HashMap;

import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.ruins.entity.player.PlayerEntity;

public class Item {

	private static HashMap<Integer, Item> items = new HashMap<>();

	public static final String basePath = "icons/items/";

	public static final Item emptyFlask = new EmptyFlask(0);
	public static final Item waterFlask = new WaterFlask(1);
	public static final Item healingHerbs = new HealingHerbs(2);
	public static final Item amuletOfEscape = new AmuletOfEscape(3);
	public static final Item amuletOfRadiance = new AmuletOfRadiance(4);
	public static final Item key = new Item(5, "Key", "key.png", new Color(0xb1b278), "Unlocks a Chest");
	public static final Item royalKey = new Item(6, "Royal Key", "key_gold.png", new Color(0xffe994), "Unlocks a Royal Chest");
	public static final Item treasure = new Item(7, "Royal Treasure", "treasure.png", new Color(0xfed466), "You are rich!");
	public static final Item coins = new Item(8, "Coins", "coins.png", new Color(0xffe994), "Donate at a shrine to receive a blessing") {
		@Override
		public boolean hasMaxCount() {
			return false;
		}
	};
	
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
		this.iconPath = basePath + iconPath;
		this.color = color;
		this.info = info;
	}
	
	public boolean isConsumable() {
		return false;
	}
	
	public boolean hasMaxCount() {
		return true;
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
