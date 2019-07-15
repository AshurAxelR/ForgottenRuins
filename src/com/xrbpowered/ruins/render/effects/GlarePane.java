package com.xrbpowered.ruins.render.effects;

import java.awt.Color;

import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.ui.pane.UITexture;
import com.xrbpowered.zoomui.UIContainer;

public class GlarePane extends UITexture {

	private static final float falloff = 1f;
	
	private float power = 0f;
	
	public GlarePane(UIContainer parent) {
		super(parent);
		pane.alpha = 0f;
		pane.setVisible(false);
	}
	
	@Override
	public void setupResources() {
		setTexture(new Texture(Color.WHITE));
		super.setupResources();
	}

	public void clear() {
		this.power = 0f;
		pane.setVisible(false);
	}
	
	public void glare(float power) {
		if(power>0f) {
			this.power += power;
			pane.setVisible(true);
		}
	}
	
	public void smallGlare() {
		glare(0.25f);
	}
	
	@Override
	public void updateTime(float dt) {
		if(pane.isVisible()) {
			power -= falloff*dt;
			if(power<0.01f)
				clear();
			else if(power<1f)
				pane.alpha = power;
			else
				pane.alpha = 1f;
		}
		super.updateTime(dt);
	}
	
}
