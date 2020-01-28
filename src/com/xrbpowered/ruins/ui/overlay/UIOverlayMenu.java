package com.xrbpowered.ruins.ui.overlay;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayMenu extends UIOverlay {

	private final UIButtonPane resumeButton;
	private final UIButtonPane restartButton;
	private final UIButtonPane exitButton;
	private final UIButtonPane[] buttons;

	public final UIPane help;
	public final UIText helpText;
	public final UIPane level;
	public final UIText levelText;

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
		
		help = new UISolidPane.Black(this);
		help.setSize(250, 220);
		helpText = new UIText.Small(help, 20, 30);
		helpText.setHtml("<p style=\"text-align:left; color:#ffffff\">Control keys:</p><p>&nbsp;</p>"+
				"<p style=\"text-align:left; color:#aaaaaa\"><span class=\"e\">W, A, S, D</span> - Move<br>"+
				"<span class=\"e\">SPACE</span> - Jump<br>"+
				"<span class=\"e\">RMB</span> - Interact<br>"+
				"<span class=\"e\">TAB</span> - Inventory<br>"+
				"<span class=\"e\">ESC</span> - Pause/Menu</p>"
			);
		level = new UISolidPane.Clear(this);
		level.setSize(180, 80);
		levelText = new UIText(level, 0, 0);
	}
	
	@Override
	public void layout() {
		float gap = 10;
		float bh = restartButton.getHeight();
		float h = resumeButton.isVisible() ? bh*3 + gap*2 : bh*2 + gap;
		float x = 100;
		float y = getHeight()/2f - h/2f;
		float top = y;
		for(UIButtonPane btn : buttons) {
			if(btn.isVisible()) {
				btn.setLocation(x, y);
				y += bh + gap;
			}
		}
		level.setLocation(x, top-level.getHeight());
		help.setLocation(getWidth()-help.getWidth()-x, getHeight()/2f - help.getHeight()/2f);
		super.layout();
	}

	@Override
	public void dismiss() {
		if(!Ruins.preview) {
			super.dismiss();
			Ruins.pause = false;
		}
	}
	
	@Override
	public void defaultAction() {
		if(Ruins.preview)
			restart();
		else
			super.defaultAction();
	}
	
	public void restart() {
		dismiss();
		//Ruins.ruins.restart(false);
		Ruins.overlayNewGame.show();
	}
	
	public void show() {
		if(!isActive()) {
			resumeButton.setVisible(!Ruins.preview);
			restartButton.label = !Ruins.preview ? "Restart" : "Start";
			levelText.setHtml(Ruins.preview ? "" : String.format("<p>Level %s<br>%s</p>",
					RomanNumerals.toRoman(Ruins.world.level+1), Ruins.world.difficulty.title));
			invalidateLayout();
			Ruins.ruins.setOverlay(Ruins.overlayMenu);
			Ruins.pause = true;
		}
	}
}
