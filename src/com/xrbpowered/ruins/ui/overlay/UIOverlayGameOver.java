package com.xrbpowered.ruins.ui.overlay;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayGameOver extends UIOverlay {

	private final UIButtonPane restartButton;
	
	public final UIPane box;
	public final UIText text;

	public UIOverlayGameOver(UIContainer parent) {
		this(parent, 560, 60, "Restart");
	}

	protected UIOverlayGameOver(UIContainer parent, int boxWidth, int boxHeight, String buttonLabel) {
		super(parent);
		restartButton = new UIButtonPane(this, buttonLabel) {
			@Override
			public void onAction() {
				defaultAction();
			}
		};

		box = new UISolidPane.Black(this);
		box.setSize(boxWidth, boxHeight);
		text = new UIText(box);
	}

	@Override
	public void layout() {
		restartButton.setLocation(getWidth()/2f-restartButton.getWidth()/2f, getHeight()-100);
		box.setLocation(getWidth()/2f-box.getWidth()/2f, getHeight()/2f-box.getHeight()/2f);
		super.layout();
	}
	
	@Override
	public void closeAction() {
		Ruins.ruins.restart();
		dismiss();
	}
	
	public void show(String message) {
		if(!isActive()) {
			text.setHtml("<p>"+message+"</p>");
			text.repaint();
			Ruins.ruins.setOverlay(Ruins.overlayGameOver);
		}
	}
}
