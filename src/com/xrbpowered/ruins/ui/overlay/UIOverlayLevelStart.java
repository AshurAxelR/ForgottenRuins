package com.xrbpowered.ruins.ui.overlay;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayLevelStart extends UIOverlayGameOver {

	public UIOverlayLevelStart(UIContainer parent) {
		super(parent, 560, 60, "Start");
	}

	@Override
	public void closeAction() {
		dismiss();
		Ruins.pause = false;
		Ruins.glare.paused = false;
		Ruins.flash.blackScreen(false);
		Ruins.ruins.enableObserver(false);
	}
	
	public void show(World world) {
		text.setHtml(String.format("<p>Level %s of %s</p>", RomanNumerals.toRoman(world.level+1), RomanNumerals.toRoman(World.maxLevel+1)));
		Ruins.pause = true;
		Ruins.glare.paused = true;
		Ruins.flash.blackScreen(true);
		Ruins.ruins.enableObserver(true);
		Ruins.ruins.setOverlay(Ruins.overlayLevelStart);
	}

}
