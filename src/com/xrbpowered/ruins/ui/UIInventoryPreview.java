package com.xrbpowered.ruins.ui;

import java.awt.Color;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.world.item.Item;
import com.xrbpowered.ruins.world.item.ItemList;
import com.xrbpowered.ruins.world.item.ItemList.ItemStack;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class UIInventoryPreview extends UIPane {

	private static final float itemWidth = 40f;
	
	public UIInventoryPreview(UIContainer parent) {
		super(parent, false);
		setSize((itemWidth+4f)*Item.countItemTypes()-4f, 30f);
	}

	private void drawStack(GraphAssist g, ItemStack s, float x) {
		g.setColor(s.item.color);
		g.fillRect(x, 0, itemWidth, 4);
		g.setColor(UIHud.clearColor);
		g.fillRect(x, 4, itemWidth, getHeight()-4);
		g.setFont(UIHud.font);
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(s.count), x+itemWidth/2, getHeight()/2+3, GraphAssist.CENTER, GraphAssist.CENTER);
	}
	
	@Override
	protected void paintSelf(GraphAssist g) {
		g.graph.setBackground(UIHud.transparent);
		g.graph.clearRect(0, 0, (int)getWidth(), (int)getHeight());
		ItemList inventory = Ruins.world.player.inventory;
		int n = inventory.countStacks();
		float span = itemWidth + 4f;
		float x = getWidth() - span*n + 4f;
		for(ItemStack s : inventory.stacks.values()) {
			drawStack(g, s, x);
			x += span;
		}
	}
	
	@Override
	public void render(RenderTarget target) {
		if(!Ruins.ruins.isOverlayActive())
			super.render(target);
	}
}
