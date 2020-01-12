package com.xrbpowered.ruins.entity.player.buff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.ui.pane.UITexture;

public class Buff {

	private static HashMap<Integer, Buff> buffs = new HashMap<>();
	public static ArrayList<Buff> buffList = new ArrayList<>();
	public static int textureSize = 0;

	public static final Buff shield = new Buff(0, "Holy Shield", 1, 20, "icons/shield.png", "Prevents damage from undead");
	public static final Buff feather = new Buff(1, "Feather Fall", 3, 15, "icons/feather.png", "Prevents damage from falling");
	public static final Buff lockpick = new Buff(2, "Lockpicking", 1, 100, "icons/lockpick.png", "Open locked chests without using keys");
	public static final Buff pathfinder = new PathfinderBuff(3);
	public static final Buff fox = new Buff(4, "Desert Fox", 3, 15, "icons/fox.png", "Cannot die from dehydration");

	public final int id;
	public final String name;
	public final int duration;
	public final int cost;
	public final String iconPath;
	public final String info;
	
	public Texture icon = null;
	public UITexture ui = null;
	
	public Buff(int id, String name, int duration, int cost, String iconPath, String info) {
		this.id = id;
		buffs.put(id, this);
		buffList.add(this);
		this.name = name;
		this.duration = duration;
		this.cost = cost;
		this.iconPath = iconPath;
		this.info = info;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	public void activate() {
	}
	
	public void deactivate() {
	}
	
	public static Buff buffById(int id) {
		return buffs.get(id);
	}
	
	public static Buff getRandom(Random random) {
		return buffList.get(random.nextInt(buffList.size()));
	}
	
	public static void loadIcons() {
		for(Buff buff : buffList) {
			buff.icon = new Texture(buff.iconPath, false, false);
			textureSize = buff.icon.getWidth();
		}
	}
	
	public static void releaseResources() {
		for(Buff buff : buffList)
			buff.icon.release();
	}
	
}
