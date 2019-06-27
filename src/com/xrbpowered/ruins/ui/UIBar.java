package com.xrbpowered.ruins.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public abstract class UIBar extends UIPane {

	public static final Font fontBold = UIHud.fontBold.deriveFont(16f);
	public static final Color bgTopColor = new Color(0x222222);
	public static final Color bgBottomColor = new Color(0x555555);
	public static final Color redTextColor = new Color(0xee0000);
	
	public int showValue;
	protected Color topColor = new Color(0xdddddd);
	protected Color bottomColor = new Color(0x999999);
	protected Color borderColor = new Color(0x888888);
	
	protected GradientPaint bgPaint, paint;
	
	public UIBar(UIContainer parent) {
		super(parent, false);
	}
	
	public UIBar setColors(Color top, Color bottom, Color border) {
		this.topColor = top;
		this.bottomColor = bottom;
		this.borderColor = border;
		return this;
	}
	
	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		bgPaint = new GradientPaint(0, 0, bgTopColor, 0, getHeight(), bgBottomColor);
		paint = new GradientPaint(0, 0, topColor, 0, getHeight(), bottomColor);
	}
	
	@Override
	protected void paintSelf(GraphAssist g) {
		float w = getWidth();
		float h = getHeight();
		g.setPaint(bgPaint);
		g.fillRect(0, 0, w, h);
		g.setPaint(paint);
		int max = getMaxValue();
		g.fillRect(0, 0, max==0 ? w : w*showValue/max, h);
		g.setStroke(2f);
		g.drawRect(0, 0, w, h, borderColor);
		
		g.setFont(UIHud.font);
		g.setColor(isRedText() ? redTextColor : Color.WHITE);
		g.drawString(Integer.toString(showValue), w/2, h/2+1, GraphAssist.CENTER, GraphAssist.CENTER);
	}
	
	public abstract int getValue();
	public abstract int getMaxValue();
	
	public boolean isRedText() {
		return false;
	}
	
	@Override
	public void updateTime(float dt) {
		int value = getValue();
		if(value!=showValue) {
			showValue = value;
			repaint();
		}
	}

}
