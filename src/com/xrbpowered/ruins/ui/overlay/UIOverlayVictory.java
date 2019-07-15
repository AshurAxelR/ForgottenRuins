package com.xrbpowered.ruins.ui.overlay;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayVictory extends UIOverlayGameOver {

	public UIOverlayVictory(UIContainer parent) {
		super(parent, 640, 220, "Continue");
		text.setHtml("<p>Congratulations!</p>"
				+ "<p>You have completed the pre-alpha preview version<br>of the game.<br>&nbsp;</p>"
				+ "<p style=\"color:#aaaaaa\">Check back for the latest updates at<br>https://github.com/ashurrafiev/ForgottenRuins</p>");
	}

	@Override
	public void closeAction() {
		dismiss();
		Ruins.ruins.restart(true);
		Ruins.overlayMenu.show();
	}
	
	public void show() {
		if(!isActive())
			Ruins.ruins.setOverlay(Ruins.overlayVictory);
	}
}
