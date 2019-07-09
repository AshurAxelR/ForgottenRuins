package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.gl.ui.pane.UITexture;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.zoomui.UIContainer;

public abstract class UIOverlay extends UINode {

	private final UITexture fill;
	
	protected boolean dismissOnRightClick = false;
	
	public UIOverlay(UIContainer parent) {
		super(parent);
		fill = new UIFill(this, new Color(0x77000000, true));
		setVisible(false);
	}
	
	@Override
	public void layout() {
		fill.setSize(getWidth(), getHeight());
		super.layout();
	}

	public void dismiss() {
		Ruins.ruins.setOverlay(null);
	}
	
	public void defaultAction() {
		closeAction();
	}
	
	public void closeAction() {
		dismiss();
	}
	
	public boolean isActive() {
		return Ruins.ruins.isOverlayActive(this);
	}
	
	@Override
	public boolean onMouseDown(float x, float y, Button button, int mods) {
		if(button==Button.right && dismissOnRightClick) {
			closeAction();
		}
		return true;
	}
	
	public void keyPressed(char c, int code) {
		switch(code) {
			case KeyEvent.VK_ESCAPE:
				closeAction();
				break;
			case KeyEvent.VK_ENTER:
				defaultAction();
				break;
		}
	}
	
}
