package com.xrbpowered.ruins.ui;

import java.awt.Color;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.ui.overlay.UIFill;
import com.xrbpowered.ruins.ui.overlay.UISolidPane;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class UIHint extends UIFill {

	private float t = 0f;
	public final UIPane textPane;
	
	public UIHint(UIContainer parent, final String text) {
		super(parent, UIHud.clearColor);
		textPane = new UISolidPane.Clear(this) {
			@Override
			protected void paintSelf(GraphAssist g) {
				super.paintSelf(g);
				g.setColor(Color.WHITE);
				g.setFont(UIHud.font);
				g.drawString(text, getWidth()/2f, getHeight()/2f, GraphAssist.CENTER, GraphAssist.CENTER);
			}
			@Override
			public void updateTime(float dt) {
				t += dt;
				pane.alpha = (float)Math.sin(t*3f)*0.5f+0.5f; 
				super.updateTime(dt);
			}
		};
		setSize(300, 30);
		textPane.setSize(getWidth(), getHeight());
	}
	
	@Override
	public void layout() {
		setLocation(getParent().getWidth()/2f-getWidth()/2f, getParent().getHeight()-getHeight()-30);
	}
	
	@Override
	public void render(RenderTarget target) {
		if(!Ruins.ruins.isOverlayActive())
			super.render(target);
	}
	
	private static UIHint hintPane = null; 
	
	public static UIHint show(String hint) {
		if(hintPane!=null)
			dismiss();
		hintPane = new UIHint(Ruins.hud, hint);
		return hintPane;
	}
	
	public static void dismiss() {
		if(hintPane!=null)
			hintPane.getParent().removeChild(hintPane);
	}

}
