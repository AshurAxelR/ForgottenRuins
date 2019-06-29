package com.xrbpowered.ruins.ui;

import java.awt.Color;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class UIPopup extends UIPane {

	public final float duration = 3f;
	public final float fade = 1f;
	public float t = duration;
	
	protected String message = null;
	
	public UIPopup(UIContainer parent) {
		super(parent, false);
		setSize(300, 30);
		pane.alpha = 0f;
	}
	
	@Override
	protected void paintSelf(GraphAssist g) {
		g.graph.setBackground(new Color(0x77000000, true));
		g.graph.clearRect(0, 0, (int)getWidth(), (int)getHeight());
		if(message!=null) {
			g.setColor(Color.WHITE);
			g.setFont(UIHud.font);
			g.drawString(message, getWidth()/2f, getHeight()/2f, GraphAssist.CENTER, GraphAssist.CENTER);
		}
	}
	
	public void popup(String msg) {
		message = msg;
		if(msg!=null) {
			t = 0f;
			System.out.println(msg);
		}
		repaint();
	}
	
	@Override
	public void updateTime(float dt) {
		t += dt;
		if(t<duration) {
			if(t<duration-fade)
				pane.alpha = 1f;
			else
				pane.alpha = 1f - (t-(duration-fade))/fade;
		}
		else {
			pane.alpha = 0f;
		}
	}

}
