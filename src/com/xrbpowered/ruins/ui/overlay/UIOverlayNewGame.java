package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.ruins.world.DifficultyMode;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayNewGame extends UIOverlay {

	private final UIButtonPane closeButton;
	private final UIChoicePane[] startButtons;
	
	public final UITextOnly title;

	public UIOverlayNewGame(UIContainer parent) {
		super(parent);
		dismissOnRightClick = true;
		
		closeButton = new UIButtonPane(this, "Back") {
			@Override
			public void onAction() {
				dismiss();
				Ruins.overlayMenu.show();
			}
		};
		
		DifficultyMode[] modes = DifficultyMode.values();
		startButtons = new UIChoicePane[modes.length];
		for(int i=0; i<modes.length; i++) {
			final DifficultyMode mode = modes[i];
			startButtons[i] = new UIChoicePane(this, mode.title, mode.description, "Alpha version: edit config to unlock") {
				@Override
				public void onAction() {
					dismiss();
					Ruins.ruins.restart(mode);
				}
			};
			if(mode==DifficultyMode.hardcore && !Ruins.settings.unlockHardcore)
				startButtons[i].setEnabled(false);
		}

		this.title = new UITextOnly(this, "Start New Game").setFont(UIHud.fontBold).setColor(Color.WHITE);
		this.title.setSize(200, 50);
	}
	
	@Override
	public void layout() {
		closeButton.setPosition(getWidth()/2f-closeButton.getWidth()/2f, getHeight()-100);
		title.setPosition(getWidth()/2f-title.getWidth()/2f, 100);
		float h = startButtons[0].getHeight() + 20;
		float y = getHeight()/2f - (startButtons.length*h - 20)/2f; 
		for(UIButtonPane btn : startButtons) {
			btn.setPosition(getWidth()/2f-btn.getWidth()/2f, y);
			y += h;
		}
		super.layout();
	}
	
	@Override
	public void dismiss() {
		if(!Ruins.preview) {
			super.dismiss();
			Ruins.pause = false;
		}
	}

	public void show() {
		Ruins.ruins.setOverlay(Ruins.overlayNewGame);
		Ruins.pause = true;
	}

}
