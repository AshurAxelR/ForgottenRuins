package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.UIElement;

public class UIButtonPane extends UIPane {

	public static final int width = 180;
	public static final int height = 40;
	
	public String label;
	public boolean down = false;
	public boolean hover = false;
	
	public UIButtonPane(UIContainer parent, String label) {
		super(parent, true);
		this.label = label;
		setSize(width, height);
	}
	
	@Override
	protected void paintSelf(GraphAssist g) {
		g.setColor(down ? new Color(0xaaaaaa) : hover ?  new Color(0xdddddd) : new Color(0xbbbbbb));
		g.fill(this);
		g.setColor(Color.BLACK);
		g.setFont(UIHud.fontBold);
		g.drawString(label, getWidth()/2, getHeight()/2, GraphAssist.CENTER, GraphAssist.CENTER);
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
			down = true;
			repaint();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onMouseUp(float x, float y, Button button, int mods, UIElement initiator) {
		if(button==Button.left) {
			down = false;
			onAction();
			repaint();
			return true;
		}
		return false;
	}
	
	public void onAction() {
	}

}
