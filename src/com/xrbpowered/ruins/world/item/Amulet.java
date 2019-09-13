package com.xrbpowered.ruins.world.item;

import java.awt.Color;

public class Amulet extends Item {

	public Amulet(int id, String postfix, String iconPath, Color color, String info) {
		super(id, "Amulet of "+postfix, iconPath, color, info);
		plural = "Amulets of "+postfix;
	}

	@Override
	protected String indefArticle() {
		return "an";
	}
	
}
