package com.xrbpowered.ruins.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.FileInputStream;
import java.io.IOException;

import com.xrbpowered.gl.res.asset.IOUtils;
import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.PlayerActor;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class UIHud extends UINode {

	public static Font font;
	public static Font fontBold;
	static {
		try {
			font = IOUtils.loadFont(new FileInputStream("assets/font/Cinzel-Bold.ttf")).deriveFont(14f);
			fontBold = font.deriveFont(18f);
		}
		catch(IOException e) {
			e.printStackTrace();
			fontBold = null;
		}
	}
	public static final Color clearColor = new Color(0x77000000, true);
	
	public final PlayerActor player;
	
	private final UIIcon heartIcon;
	private final UIIcon waterIcon;
	private final UIIcon coinIcon;
	private final UIBar healthBar;
	private final UIBar waterBar;
	private final UIObeliskDots obeliskDots;
	
	public final UIPopup popup;
	
	private String shownPick = null;
	private String shownAction = "";
	private final UIPane pickPane;
	
	private int shownCoins = 0;
	private final UIPane coinsPane;
	
	public UIHud(UIContainer parent, final PlayerActor player) {
		super(parent);
		this.player = player;
		
		heartIcon = new UIIcon(this, "icons/heart.png");
		healthBar = new UIBar(this) {
			@Override
			public int getValue() {
				return Math.round(player.health);
			}
			@Override
			public int getMaxValue() {
				return PlayerActor.baseHealth;
			}
			@Override
			public boolean isRedText() {
				return showValue<=25;
			}
		}.setColors(new Color(0xee0000), new Color(0xaa0000), new Color(0x990000));
		waterIcon = new UIIcon(this, "icons/water.png");
		waterBar = new UIBar(this) {
			@Override
			public int getValue() {
				return Math.round(player.hydration);
			}
			@Override
			public int getMaxValue() {
				return PlayerActor.baseHydration;
			}
			@Override
			public boolean isRedText() {
				return showValue<=0;
			}
		}.setColors(new Color(0x0055aaee), new Color(0x3377aa), new Color(0x226699));
		coinIcon = new UIIcon(this, "icons/coin.png");
		coinsPane = new UIPane(this, false) {
			@Override
			protected void paintSelf(GraphAssist g) {
				g.graph.setBackground(new Color(0x77000000, true));
				g.graph.clearRect(0, 0, (int)getWidth(), (int)getHeight());
				g.setFont(UIHud.font);
				g.setColor(shownCoins>0 ? Color.WHITE : new Color(0xdddddd));
				g.drawString(Integer.toString(shownCoins), getWidth()/2, getHeight()/2+1, GraphAssist.CENTER, GraphAssist.CENTER);
			}
		};
		
		obeliskDots = new UIObeliskDots(this);
		
		pickPane = new UIPane(this, false) {
			@Override
			protected void paintSelf(GraphAssist g) {
				g.graph.setBackground(new Color(0, true));
				g.graph.clearRect(0, 0, (int)getWidth(), (int)getHeight());
				if(shownPick!=null) {
					FontMetrics fm = g.graph.getFontMetrics(fontBold);
					float w1 = fm.stringWidth(shownPick)+30f;
					fm = g.graph.getFontMetrics(font);
					float w2 = fm.stringWidth(shownAction)+30f;
					float w = Math.max(w1, w2);
					g.setColor(new Color(0x77000000, true));
					g.fillRect(getWidth()/2f-w/2f, 0, w, getHeight());
					g.setColor(Color.WHITE);
					g.setFont(fontBold);
					g.drawString(shownPick, getWidth()/2f, getHeight()/2f-2, GraphAssist.CENTER, GraphAssist.BOTTOM);
					g.setColor(new Color(0xe5e5e5));
					g.setFont(font);
					g.drawString(shownAction, getWidth()/2f, getHeight()/2f+5, GraphAssist.CENTER, GraphAssist.TOP);
				}
			}
			@Override
			public void render(RenderTarget target) {
				if(!Ruins.ruins.isOverlayActive())
					super.render(target);
			}
		};
		pickPane.setSize(250, 50);
		pickPane.setVisible(false);
		
		popup = new UIPopup(this);
	}
	
	@Override
	public void layout() {
		float s = UIIcon.pixelSize * getPixelScale();
		heartIcon.setLocation(10, getHeight()-heartIcon.getHeight()/2f-s*25);
		waterIcon.setLocation(10, heartIcon.getY()+heartIcon.getHeight());
		coinIcon.setLocation(10, heartIcon.getY()-heartIcon.getHeight());
		healthBar.setSize(s*40, heartIcon.getHeight()-6*s);
		healthBar.setLocation(10+3*s+heartIcon.getWidth(), heartIcon.getY()+3*s);
		waterBar.setSize(healthBar.getWidth(), healthBar.getHeight());
		waterBar.setLocation(healthBar.getX(), waterIcon.getY()+3*s);
		coinsPane.setSize(healthBar.getWidth(), healthBar.getHeight());
		coinsPane.setLocation(healthBar.getX(), coinIcon.getY()+3*s);

		obeliskDots.setLocation(getWidth()-obeliskDots.getWidth()-s*5, getHeight()-obeliskDots.getHeight()-s*5);
		pickPane.setLocation(getWidth()/2f-pickPane.getWidth()/2f, getHeight()/2f-pickPane.getHeight()/2f);
		popup.setLocation(getWidth()/2f-popup.getWidth()/2f, getHeight()*0.75f-popup.getHeight()/2f);
		super.layout();
	}

	public void updatePickText(String pick) {
		shownPick = pick;
		shownAction = "";
		if(shownPick==null)
			pickPane.setVisible(false);
		else {
			shownAction = Ruins.pick.pickObject.getActionString();
			pickPane.setVisible(true);
			repaint();
		}
	}
	
	@Override
	public void updateTime(float dt) {
		if(Ruins.world==null || Ruins.preview)
			return;
		String pick = Ruins.pick.pickObject==null ? null : Ruins.pick.pickObject.getPickName();
		if(pick!=shownPick)
			updatePickText(pick);
		if(player.coins!=shownCoins) {
			shownCoins = player.coins;
			coinsPane.repaint();
		}
		super.updateTime(dt);
	}
	
	@Override
	public void render(RenderTarget target) {
		if(Ruins.world==null || Ruins.preview)
			return;
		super.render(target);
	}
	
}
