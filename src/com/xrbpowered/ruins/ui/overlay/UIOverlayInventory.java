package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.PlayerEntity;
import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.ruins.world.item.Item;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayInventory extends UIOverlay {

	public static final String keyHint = "Press TAB to open inventory";
	
	private final UIButtonPane closeButton;
	
	public final UIPane title;
	public final UIItemList items;
	public final UIPane info;
	public final UIText infoText;

	public UIOverlayInventory(UIContainer parent) {
		super(parent);
		
		closeButton = new UIButtonPane(this, "Close") {
			@Override
			public void onAction() {
				dismiss();
			}
		};

		title = new UITextOnly(this, "INVENTORY").setFont(UIHud.fontBold).setColor(Color.WHITE);
		title.setSize(200, 50);
		
		info = new UISolidPane.Clear(this);
		info.setSize(260, 100);
		infoText = new UIText.Small(info);
		info.setVisible(false);

		items = new UIItemList(this, infoText);
	}
	
	@Override
	public void layout() {
		closeButton.setLocation(getWidth()/2f-closeButton.getWidth()/2f, getHeight()-100);
		title.setLocation(getWidth()/2f-title.getWidth()/2f, 100);
		items.setLocation(getWidth()/2f-items.getWidth()/2f, getHeight()/2f-items.getHeight()/2f);
		info.setLocation(getWidth()/2f-info.getWidth()/2f, items.getY()-info.getHeight());
		super.layout();
	}
	
	public void keyPressed(char c, int code) {
		switch(code) {
			case KeyEvent.VK_TAB:
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
	
	public void updateAndShow(PlayerEntity player) {
		if(!player.alive)
			return;
		items.update(player);
		info.setVisible(false);
		Ruins.hud.inventoryHint.setVisible(false);
		Ruins.ruins.setOverlay(Ruins.overlayInventory);
	}

}
