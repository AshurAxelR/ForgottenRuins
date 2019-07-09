package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;
import java.util.HashMap;

import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.ui.pane.UITexture;
import com.xrbpowered.zoomui.UIContainer;

public class UIFill extends UITexture {

	private static HashMap<Integer, Texture> cache = new HashMap<>();
	
	public final Color color;
	
	public UIFill(UIContainer parent, Color color) {
		super(parent);
		this.color = color;
	}
	
	@Override
	public void setupResources() {
		setTexture(create(color));
		super.setupResources();
	}
	
	@Override
	public void releaseResources() {
		release(color);
		super.releaseResources();
	}
	
	protected static Texture create(Color color) {
		int rgb = color.getRGB();
		Texture texture = cache.get(rgb);
		if(texture==null) {
			texture = new Texture(color);
			cache.put(rgb, texture);
		}
		return texture;
	}
	
	protected static void release(Color color) {
		Texture texture = cache.get(color.getRGB());
		if(texture!=null)
			texture.release();
	}

}
