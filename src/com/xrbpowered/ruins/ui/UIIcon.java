package com.xrbpowered.ruins.ui;

import com.xrbpowered.gl.client.UIClient;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.ui.pane.UITexture;
import com.xrbpowered.zoomui.UIContainer;

public class UIIcon extends UITexture {

	public static int pixelSize = 0;
	public static int minPixelSize = 4;
	
	public final String icon;
	
	public UIIcon(UIContainer parent, String icon) {
		super(parent);
		this.icon = icon;
	}
	
	public UIIcon(UIContainer parent, Texture icon) {
		super(parent);
		this.icon = null;
		setTexture(icon);
	}
	
	@Override
	protected UITexture setTexture(Texture texture) {
		super.setTexture(texture);
		float s = pixelSize * getPixelScale();
		setSize(pane.getTexture().getWidth()*s, pane.getTexture().getHeight()*s);
		return this;
	}
	
	@Override
	public void setupResources() {
		setTexture(new Texture(icon, false, false));
	}

	public static void setPixelSize(int size, UIClient client) {
		pixelSize = size;
		client.getContainer().setBaseScale(size * 0.3f);
		System.out.printf("Pixel size: %d, scale: %.0f%%\n", pixelSize, client.getContainer().getBaseScale()*100f);
	}
	
}
