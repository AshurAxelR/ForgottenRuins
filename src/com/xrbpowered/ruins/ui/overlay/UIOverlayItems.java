package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.ruins.world.item.ItemList;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayItems extends UIOverlay {

	private final UIButtonPane closeButton;
	
	public final UITextOnly title;
	public final UIItemList items;
	public final UIPane info;
	public final UIText infoText;

	public UIOverlayItems(UIContainer parent) {
		this(parent, "", "Take All");
	}

	public UIOverlayItems(UIContainer parent, String title, String closeLabel) {
		super(parent);
		dismissOnRightClick = true;
		
		closeButton = new UIButtonPane(this, closeLabel) {
			@Override
			public void onAction() {
				dismiss();
			}
		};

		this.title = new UITextOnly(this, title).setFont(UIHud.fontBold).setColor(Color.WHITE);
		this.title.setSize(200, 50);
		
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
	
	public void showItems(String title, ItemList items) {
		this.title.label = title;
		this.items.update(items, null);
		info.setVisible(false);
		Ruins.ruins.setOverlay(Ruins.overlayItems);
	}

}
