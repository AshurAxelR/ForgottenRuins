package com.xrbpowered.ruins.ui.overlay;

import java.awt.event.KeyEvent;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.PlayerEntity;
import com.xrbpowered.ruins.ui.UIHint;
import com.xrbpowered.ruins.world.item.Item;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayInventory extends UIOverlayItems {

	public static final String keyHint = "Press TAB to open inventory";
	
	public UIOverlayInventory(UIContainer parent) {
		super(parent, "INVENTORY", "Close");
		dismissOnRightClick = false;
	}
	
	public void keyPressed(char c, int code) {
		switch(code) {
			case KeyEvent.VK_TAB:
				if(keyReleased)
					closeAction();
				break;
			default:
				if(Item.keyPressed(code, Ruins.world.player)) {
					items.update(Ruins.world.player);
					return;
				}
				super.keyPressed(c, code);
		}
	}
	
	public void showInventory(PlayerEntity player) {
		if(!player.alive)
			return;
		items.update(player);
		info.setVisible(false);
		UIHint.dismiss();
		Ruins.ruins.setOverlay(Ruins.overlayInventory);
	}

}
