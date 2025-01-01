package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.gl.ui.pane.UITexture;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.PlayerEntity;
import com.xrbpowered.ruins.entity.player.buff.Buff;
import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.ruins.ui.UIIcon;
import com.xrbpowered.ruins.world.obj.Shrine;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayShrine extends UIOverlay {

	private final UIChoicePane acceptButton;
	private final UIChoicePane closeButton;
	
	public final UITextOnly title;
	public final UIFill box;
	public final UIPane name;
	public final UIText nameText;
	public final UIPane info;
	public final UIText infoText;
	
	public final UIPane active;
	public final UIText activeText;
	
	public UITexture icon = null;

	public Buff buff;
	public PlayerEntity player;
	
	public UIOverlayShrine(UIContainer parent) {
		super(parent);
		dismissOnRightClick = true;
		
		acceptButton = new UIChoicePane(this, "Donate coins", "Give away coins to receive the blessing", "You don't have enough") {
			@Override
			public void onAction() {
				defaultAction();
			}
		};
		closeButton = new UIChoicePane(this, "Leave", "You can come back later", "") {
			@Override
			public void onAction() {
				closeAction();
			}
		};

		title = new UITextOnly(this, "Shrine").setFont(UIHud.fontBold).setColor(Color.WHITE);
		title.setSize(200, 50);

		box = new UIFill(this, Color.BLACK);
		box.setSize(300, 240);

		name = new UISolidPane.Black(box);
		name.setSize(260, 60);
		name.setPosition(box.getWidth()/2f-name.getWidth()/2f, 10f);
		nameText = new UIText(name, 0, 0);
		
		info = new UISolidPane.Black(box);
		info.setSize(260, 80);
		info.setPosition(box.getWidth()/2f-info.getWidth()/2f, box.getHeight()-info.getHeight());
		infoText = new UIText.Small(info, 0, 0);
		
		active = new UISolidPane.Clear(this);
		active.setSize(260, 50);
		activeText = new UIText.Small(active, 0, 0);
	}

	@Override
	public void layout() {
		closeButton.setPosition(getWidth()/2f-closeButton.getWidth()/2f, getHeight()-140);
		acceptButton.setPosition(getWidth()/2f-closeButton.getWidth()/2f, closeButton.getY()-20-acceptButton.getHeight());
		title.setPosition(getWidth()/2f-title.getWidth()/2f, 100);
		box.setPosition(getWidth()/2f-box.getWidth()/2f, getHeight()/2f-box.getHeight()/2f-80);
		active.setPosition(getWidth()/2f-active.getWidth()/2f, acceptButton.getY()-active.getHeight()-10);
		super.layout();
	}

	@Override
	public void defaultAction() {
		dismiss();
		if(player.coins>=buff.cost) {
			player.coins -= buff.cost;
			player.buffs.add(buff);
		}
	}
	
	@Override
	public void updateTime(float dt) {
		if(isVisible() && player!=null) {
			int t = (int)Math.ceil(player.buffs.getRemaining(buff));
			if(t>0) {
				active.setVisible(true);
				activeText.setHtml(String.format("<p>You already have this blessing<br>%d sec remaining</p>", t));
				active.repaint();
			}
			else if(active.isVisible()) {
				active.setVisible(false);
			}
		}
		super.updateTime(dt);
	}

	public void show(Shrine shrine) {
		this.player = shrine.world.player;
		this.buff = shrine.buff;
		
		acceptButton.setEnabled(player.coins>=buff.cost);
		acceptButton.label = String.format("Donate %d coins", buff.cost);
		
		nameText.setHtml(String.format("<p>Blessing of<br><span class=\"e\">%s</span></p>", buff.name));
		infoText.setHtml(String.format("<p>%s</p><p>Duration: %d min</p>", buff.info, buff.duration));
		
		if(icon!=null)
			box.removeChild(icon);
		icon = new UITexture(box);
		float s = UIIcon.pixelSize * getPixelSize();
		float iconSize = Buff.textureSize * s;
		icon.setSize(iconSize, iconSize);
		icon.setPosition(box.getWidth()/2f-iconSize/2f+s/2f, 20+name.getHeight());
		icon.pane.setTexture(buff.icon);
		
		Ruins.ruins.setOverlay(Ruins.overlayShrine);
	}
	
}
