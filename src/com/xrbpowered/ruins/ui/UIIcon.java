package com.xrbpowered.ruins.ui;

import com.xrbpowered.gl.client.UIClient;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.ui.pane.UITexture;
import com.xrbpowered.zoomui.UIContainer;

public class UIIcon extends UITexture {

	public static int pixelSize = 2;
	
	public final String icon;
	
	public UIIcon(UIContainer parent, String icon) {
		super(parent);
		this.icon = icon;
	}
	
	@Override
	public void setupResources() {
		setTexture(new Texture(icon, false, false));
		float s = pixelSize * getPixelScale();
		setSize(pane.getTexture().getWidth()*s, pane.getTexture().getHeight()*s);
	}

	public static void updatePixelSize(UIClient client) {
		pixelSize = Math.round(client.getContainer().getBaseScale() * 3f);
	}
	
}
