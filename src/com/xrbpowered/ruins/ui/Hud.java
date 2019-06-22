package com.xrbpowered.ruins.ui;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;

import com.xrbpowered.gl.res.asset.IOUtils;
import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.entity.PlayerActor;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class Hud extends UINode {

	public static Font font;
	static {
		try {
			font = IOUtils.loadFont(new FileInputStream("assets/font/Cinzel-Bold.ttf")).deriveFont(30f);
		}
		catch(IOException e) {
			e.printStackTrace();
			font = null;
		}
	}
	public static final Color clearColor = new Color(0x77000000, true);
	
	public final PlayerActor player;
	
	private final UIPane statusPane;
	
	private int shownHealth = -1;
	
	public Hud(UIContainer parent, PlayerActor player) {
		super(parent);
		this.player = player;
		
		statusPane = new UIPane(this, false) {
			@Override
			protected void paintSelf(GraphAssist g) {
				g.graph.setBackground(clearColor);
				g.graph.clearRect(0, 0, (int)getWidth(), (int)getHeight());
				String s = Integer.toString(shownHealth);
				g.setFont(font);
				g.setColor(shownHealth>25 ? Color.WHITE : new Color(0xff4400));
				g.drawString(s, 50, getHeight()/2f+2, GraphAssist.LEFT, GraphAssist.CENTER);
			}
		};
		statusPane.setSize(150, 40);
	}
	
	@Override
	public void layout() {
		statusPane.setLocation(0, getHeight()-statusPane.getHeight()-20);
		super.layout();
	}
	
	@Override
	public void updateTime(float dt) {
		int health = Math.round(player.health);
		if(health!=shownHealth) {
			shownHealth = health;
			statusPane.repaint();
		}
		super.updateTime(dt);
	}
	
}
