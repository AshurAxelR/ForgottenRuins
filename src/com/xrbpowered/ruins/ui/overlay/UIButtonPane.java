package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.UIElement;

public class UIButtonPane extends UIPane {

	public static final Color disabledColor = new Color(0x555555);
	public static final Color downColor = new Color(0xaaaaaa);
	public static final Color hoverColor = new Color(0xdddddd);
	public static final Color buttonColor = new Color(0xbbbbbb);
	
	public static final int width = 180;
	public static final int height = 40;
	
	public String label;
	public boolean down = false;
	public boolean hover = false;
	
	protected boolean enabled = true;
	
	public UIButtonPane(UIContainer parent, String label) {
		super(parent, true);
		this.label = label;
		setSize(width, height);
	}
	
	@Override
	protected void paintSelf(GraphAssist g) {
		g.setColor(!enabled ? disabledColor : down ? downColor : hover ?  hoverColor : buttonColor);
		g.fill(this);
		g.setColor(Color.BLACK);
		g.setFont(UIHud.fontBold);
		g.drawString(label, getWidth()/2, getHeight()/2, GraphAssist.CENTER, GraphAssist.CENTER);
	}
	
	public void setEnabled(boolean enabled) {
		if(this.enabled!=enabled) {
			this.enabled = enabled;
			repaint();
		}
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void onMouseIn() {
		hover = true;
		repaint();
	}
	
	@Override
	public void onMouseOut() {
		hover = false;
		down = false;
		repaint();
	}
	
	@Override
	public boolean onMouseDown(float x, float y, Button button, int mods) {
		if(button==Button.left) {
			if(enabled) {
				down = true;
				repaint();
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onMouseUp(float x, float y, Button button, int mods, UIElement initiator) {
		if(button==Button.left) {
			if(enabled) {
				down = false;
				onAction();
				repaint();
			}
			return true;
		}
		return false;
	}
	
	public void onAction() {
	}

}
