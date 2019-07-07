package com.xrbpowered.ruins.ui.overlay;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayMenu extends UIOverlay {

	private final UIButtonPane resumeButton;
	private final UIButtonPane restartButton;
	private final UIButtonPane exitButton;
	private final UIButtonPane[] buttons;

	public final UISolid box;
	public final UIText text;

	public UIOverlayMenu(UIContainer parent) {
		super(parent);
		resumeButton = new UIButtonPane(this, "Resume") {
			@Override
			public void onAction() {
				closeAction();
			}
		};
		restartButton = new UIButtonPane(this, "Start") {
			@Override
			public void onAction() {
				restart();
			}
		};
		exitButton = new UIButtonPane(this, "Exit") {
			@Override
			public void onAction() {
				Ruins.ruins.requestExit();
			}
		};
		
		buttons = new UIButtonPane[] {resumeButton, restartButton, exitButton};
		
		box = new UISolid.Black(this);
		box.setSize(250, 220);
		text = new UIText.Small(box, 20, 30);
		text.setHtml("<p style=\"text-align:left; color:#ffffff\">Control keys:</p><p>&nbsp;</p>"+
				"<p style=\"text-align:left; color:#aaaaaa\"><span class=\"e\">W, A, S, D</span> - Move<br>"+
				"<span class=\"e\">SPACE</span> - Jump<br>"+
				"<span class=\"e\">RMB</span> - Interact<br>"+
				"<span class=\"e\">TAB</span> - Inventory (W.I.P.)<br>"+
				"<span class=\"e\">ESC</span> - Pause/Menu</p>"
			);
	}
	
	@Override
	public void layout() {
		float gap = 10;
		float bh = restartButton.getHeight();
		float h = resumeButton.isVisible() ? bh*3 + gap*2 : bh*2 + gap;
		float x = 100;
		float y = getHeight()/2f - h/2f;
		for(UIButtonPane btn : buttons) {
			if(btn.isVisible()) {
				btn.setLocation(x, y);
				y += bh + gap;
			}
		}
		box.setLocation(getWidth()-box.getWidth()-x, getHeight()/2f - box.getHeight()/2f);
		super.layout();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		Ruins.pause = false;
	}
	
	@Override
	public void defaultAction() {
		if(Ruins.world==null)
			restart();
		else
			super.defaultAction();
	}
	
	public void restart() {
		Ruins.ruins.restart();
		dismiss();
	}
	
	@Override
	public void closeAction() {
		if(Ruins.world!=null)
			dismiss();
	}
	
	public void show() {
		if(!isActive()) {
			resumeButton.setVisible(Ruins.world!=null);
			restartButton.label = Ruins.world!=null ? "Restart" : "Start";
			invalidateLayout();
			Ruins.ruins.setOverlay(Ruins.overlayMenu);
			Ruins.pause = true;
		}
	}
}
