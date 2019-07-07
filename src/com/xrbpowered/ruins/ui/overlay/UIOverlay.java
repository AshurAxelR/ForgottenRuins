package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.gl.ui.pane.UITexture;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.zoomui.UIContainer;

public abstract class UIOverlay extends UINode {

	private static Texture fillTexture = null;
	
	private final UITexture fill;
	
	public UIOverlay(UIContainer parent) {
		super(parent);
		
		fill = new UITexture(this) {
			@Override
			public void setupResources() {
				if(fillTexture==null)
					fillTexture = new Texture(new Color(0x77000000, true));
				setTexture(fillTexture);
			}
		};
		
		setVisible(false);
	}
	
	@Override
	public void layout() {
		fill.setSize(getWidth(), getHeight());
		super.layout();
	}
	
	@Override
	public void releaseResources() {
		if(fillTexture!=null)
			fillTexture.release();
		fillTexture = null;
		super.releaseResources();
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
		if(button==Button.right) {
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
