package com.xrbpowered.ruins.ui.overlay;

import java.awt.event.KeyEvent;

import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.ruins.entity.player.PlayerEntity;
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
	private final PlayerEntity player;
	private String html;
	
	public UIItemStack(UIItemList parent, UIText infoText, ItemStack stack, PlayerEntity player) {
		super(parent);
		this.item = stack.item;
		this.player = player;
		this.infoText = infoText;
		String hk = "";
		if(item.hotkey!=0)
			hk = "<br>Hotkey: <span class=\"e\">"+KeyEvent.getKeyText(item.hotkey)+"</span>";
		this.html = "<p><span class=\"e\">"+item.name+"</span><br>"+item.info+hk+"</p>";
		
		icon = new UIIcon(this, item.icon);
		float w = icon.getWidth();
		float h = icon.getHeight();
		
		count = new UITextOnly(this, Integer.toString(stack.count));
		count.setSize(w, 20);
		count.setPosition(0, h);
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
			useButton = new UIButtonPane(this, "Use") {
				@Override
				public void onAction() {
					if(item.isConsumable())
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
			useButton.enabled = item.isConsumable();
			useButton.setSize(w-8, useButton.getHeight());
			useButton.setPosition(4, h);
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
				getRoot().repaint();
			}
		}
	}
	
}
