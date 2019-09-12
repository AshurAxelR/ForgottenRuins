package com.xrbpowered.ruins.ui.overlay;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.world.DifficultyMode;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayGameOver extends UIOverlay {

	private final UIButtonPane restartButton;
	
	public final UIPane box;
	public final UIText text;

	public UIOverlayGameOver(UIContainer parent) {
		this(parent, 560, 60, "Respawn");
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
		text = new UIText(box, 20, 15);
	}

	@Override
	public void layout() {
		restartButton.setLocation(getWidth()/2f-restartButton.getWidth()/2f, getHeight()-100);
		box.setLocation(getWidth()/2f-box.getWidth()/2f, getHeight()/2f-box.getHeight()/2f);
		super.layout();
	}
	
	@Override
	public void closeAction() {
		if(Ruins.world.difficulty==DifficultyMode.hardcore) {
			Ruins.ruins.restartPreview();
			dismiss();
			Ruins.overlayNewGame.show();
		}
		else {
			Ruins.ruins.restart(Ruins.world.difficulty, Ruins.world.level, null, false);
			dismiss();
		}
	}
	
	public void show(String message) {
		if(!isActive()) {
			text.setHtml("<p>"+message+"</p>");
			text.repaint();
			Ruins.ruins.enableObserver(true);
			Ruins.ruins.setOverlay(Ruins.overlayGameOver);
		}
	}
}
