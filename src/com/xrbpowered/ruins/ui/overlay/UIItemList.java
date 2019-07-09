package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.ruins.entity.PlayerActor;
import com.xrbpowered.ruins.world.item.ItemList;
import com.xrbpowered.ruins.world.item.ItemList.ItemStack;
import com.xrbpowered.zoomui.UIContainer;

public class UIItemList extends UIFill {

	private final UIText infoText;
	
	public UIItemList(UIContainer parent, UIText infoText) {
		super(parent, Color.BLACK);
		this.infoText = infoText;
	}

	public void update(ItemList items, PlayerActor player) {
		removeAllChildren();
		int i = 0;
		UIItemStack item = null;
		for(ItemStack s : items.stacks.values()) {
			item = new UIItemStack(this, infoText, s, player);
			item.setLocation(i*item.getWidth()+20, 20);
			i++;
		}
		if(item==null)
			setVisible(false);
		else {
			setVisible(true);
			setSize(i*item.getWidth()+40, item.getHeight()+40);
		}
		repaint();
	}
	
	public void update(PlayerActor player) {
		update(player.inventory, player);
	}

}
