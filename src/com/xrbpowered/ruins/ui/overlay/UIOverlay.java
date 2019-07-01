package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.gl.ui.pane.UITexture;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.zoomui.UIContainer;

public abstract class UIOverlay extends UINode {

	private static Texture fillTexture = null;
	
	private final UITexture fill;
	private final UIButtonPane closeButton;
	
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
		closeButton = new UIButtonPane(this, "Close") {
			@Override
			public void onAction() {
				dismiss();
			}
		};
		
		setVisible(false);
	}
	
	@Override
	public void layout() {
		fill.setSize(getWidth(), getHeight());
		closeButton.setLocation(getWidth()/2f-closeButton.getWidth()/2f, getHeight()-100);
		super.layout();
	}
	
	@Override
	public void releaseResources() {
		fillTexture.release();
		fillTexture = null;
		super.releaseResources();
	}

	public void dismiss() {
		Ruins.ruins.setOverlay(null);
	}
	
	@Override
	public boolean onMouseDown(float x, float y, Button button, int mods) {
		if(button==Button.right) {
			dismiss();
		}
		return true;
	}
	
}
