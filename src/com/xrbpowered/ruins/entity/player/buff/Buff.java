package com.xrbpowered.ruins.entity.player.buff;

import java.util.TreeMap;

import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.ui.pane.UITexture;

public class Buff {

	public static TreeMap<Integer, Buff> buffs = new TreeMap<>();
	public static int textureSize = 0;

	public static final Buff shield = new Buff(0, "Holy Shield", 1, 15, "icons/shield.png", "Prevents damage from undead");
	public static final Buff feather = new Buff(1, "Feather Fall", 3, 30, "icons/feather.png", "Prevents damage from falling");
	public static final Buff lockpick = new Buff(2, "Lockpicking", 1, 100, "icons/lockpick.png", "Open locked chests without using keys");

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
	
	public static void loadIcons() {
		for(Buff buff : buffs.values()) {
			buff.icon = new Texture(buff.iconPath, false, false);
			textureSize = buff.icon.getWidth();
		}
	}
	
	public static void releaseResources() {
		for(Buff buff : buffs.values())
			buff.icon.release();
	}

	
}
