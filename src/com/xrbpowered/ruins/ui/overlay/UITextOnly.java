package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;
import java.awt.Font;

import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class UITextOnly extends UISolidPane.Clear {

	public String label;
	
	private Font font = UIHud.font;
	private Color color = new Color(0xeeeeee);
	
	public UITextOnly(UIContainer parent, String label) {
		super(parent);
		this.label = label;
	}
	
	public UITextOnly setFont(Font font) {
		this.font = font;
		return this;
	}
	
	public UITextOnly setColor(Color color) {
		this.color = color;
		return this;
	}

	@Override
	protected void paintSelf(GraphAssist g) {
		super.paintSelf(g);
		g.setFont(font);
		g.setColor(color);
		g.drawString(label, getWidth()/2f, getHeight()/2f, GraphAssist.CENTER, GraphAssist.CENTER);
	}
	
}
