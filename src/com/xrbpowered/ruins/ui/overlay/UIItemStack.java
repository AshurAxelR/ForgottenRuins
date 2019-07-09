package com.xrbpowered.ruins.ui.overlay;

import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.ruins.entity.PlayerActor;
import com.xrbpowered.ruins.ui.UIIcon;
import com.xrbpowered.ruins.world.item.Item;
import com.xrbpowered.ruins.world.item.ItemList.ItemStack;

public class UIItemStack extends UINode {

	public final UIIcon icon;
	public final UITextOnly count;
	public final UIButtonPane useButton;
	private final UINode hover;
	private final UIText infoText;
	
	private final Item item;
	private final PlayerActor player;
	private String html;
	
	public UIItemStack(UIItemList parent, UIText infoText, ItemStack stack, PlayerActor player) {
		super(parent);
		this.item = stack.item;
		this.player = player;
		this.infoText = infoText;
		this.html = "<p><span class=\"e\">"+item.name+"</span><br>"+item.info+"</p>";
		
		icon = new UIIcon(this, item.icon);
		float w = icon.getWidth();
		float h = icon.getHeight();
		
		count = new UITextOnly(this, Integer.toString(stack.count));
		count.setSize(w, 20);
		count.setLocation(0, h);
		h += count.getHeight()+4;
		
		hover = new UINode(this) {
			@Override
			public void onMouseIn() {
				showInfo(true);
			}
			@Override
			public void onMouseOut() {
				showInfo(false);
			}
		};
		
		if(player!=null) {
			if(stack.item.isConsumable()) {
				useButton = new UIButtonPane(this, "Use") {
					@Override
					public void onAction() {
						useItem();
					}
					@Override
					public void onMouseIn() {
						super.onMouseIn();
						showInfo(true);
					}
					@Override
					public void onMouseOut() {
						super.onMouseOut();
						showInfo(false);
					}
				};
				useButton.setSize(w-8, useButton.getHeight());
				useButton.setLocation(4, h);
			}
			else
				useButton = null;
			h += UIButtonPane.height;
		}
		else
			useButton = null;
		
		hover.setSize(w, h);
		setSize(w, h);
	}

	public void useItem() {
		if(player!=null) {
			player.inventory.use(item, player);
			((UIItemList) getParent()).update(player);
		}
	}
	
	public void showInfo(boolean show) {
		if(infoText.getParent().isVisible()!=show) {
			infoText.getParent().setVisible(show);
			if(show) {
				infoText.setHtml(html);
				getBase().repaint();
			}
		}
	}
	
}
