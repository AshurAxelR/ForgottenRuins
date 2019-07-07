package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.ui.UIText;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayGameOver extends UIOverlay {

	private final UIButtonPane closeButton;
	
	public final UIPane box;
	public final UIText text;

	public UIOverlayGameOver(UIContainer parent) {
		super(parent);
		closeButton = new UIButtonPane(this, "Restart") {
			@Override
			public void onAction() {
				Ruins.ruins.restart();
				dismiss();
			}
		};

		box = new UIPane(this, true) {
			@Override
			protected void paintSelf(GraphAssist g) {
				g.fill(this, Color.BLACK);
				super.paintSelf(g);
			}
		};
		box.setSize(560, 60);
		text = new UIText(box);
		text.setSize(box.getWidth()-40, box.getHeight()-60);
		text.setLocation(20, 30);
	}

	@Override
	public void layout() {
		closeButton.setLocation(getWidth()/2f-closeButton.getWidth()/2f, getHeight()-100);
		box.setLocation(getWidth()/2f-box.getWidth()/2f, getHeight()/2f-box.getHeight()/2f);
		super.layout();
	}
	
	public void show(String message) {
		if(!isActive()) {
			text.setHtml("<p>"+message+"</p>");
			text.repaint();
			Ruins.ruins.setOverlay(Ruins.overlayGameOver);
		}
	}
}
