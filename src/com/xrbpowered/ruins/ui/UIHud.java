package com.xrbpowered.ruins.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.IOException;

import com.xrbpowered.gl.res.asset.AssetManager;
import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.PlayerEntity;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.UIElement;

public class UIHud extends UINode {

	public static Font font;
	public static Font fontBold;

	public static final Color clearColor = new Color(0x77000000, true);
	public static final Color transparent = new Color(0, true);
	
	private final UIIcon heartIcon;
	private final UIIcon waterIcon;
	private final UIIcon coinIcon;
	private final UIBar healthBar;
	private final UIBar waterBar;
	private final UIObeliskDots obeliskDots;
	private final UIInventoryPreview invPreview;
	private final UIBuffPreview buffPreview;
	
	public final UIPopup popup;
	
	private String shownPick = null;
	private String shownAction = "";
	private final UIPane pickPane;
	private final UIIcon crosshair;
	
	private int shownCoins = 0;
	private final UIPane coinsPane;
	
	public static void initFonts() {
		try {
			font = AssetManager.defaultAssets.loadFont("font/Cinzel-Bold.ttf").deriveFont(14f);
			fontBold = font.deriveFont(18f);
		}
		catch(IOException e) {
			e.printStackTrace();
			font = null;
			fontBold = null;
		}
	}
	
	public UIHud(UIContainer parent) {
		super(parent);
		
		heartIcon = new UIIcon(this, "icons/heart.png");
		healthBar = new UIBar(this) {
			@Override
			public int getValue() {
				return Math.round(Ruins.world.player.health);
			}
			@Override
			public int getMaxValue() {
				return PlayerEntity.baseHealth;
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
				return Math.round(Ruins.world.player.hydration);
			}
			@Override
			public int getMaxValue() {
				return PlayerEntity.baseHydration;
			}
			@Override
			public boolean isRedText() {
				return showValue<=0;
			}
		}.setColors(new Color(0x0055aaee), new Color(0x3377aa), new Color(0x226699));
		coinIcon = new UIIcon(this, "icons/coin.png");
		coinsPane = new UIPane(this, false) {
			@Override
			protected void paintBackground(GraphAssist g) {
				g.graph.setBackground(clearColor);
				g.graph.clearRect(0, 0, (int)getWidth(), (int)getHeight());
				g.setFont(font);
				g.setColor(shownCoins>0 ? Color.WHITE : new Color(0xdddddd));
				g.drawString(Integer.toString(shownCoins), getWidth()/2, getHeight()/2+1, GraphAssist.CENTER, GraphAssist.CENTER);
			}
		};
		
		obeliskDots = new UIObeliskDots(this);
		invPreview = new UIInventoryPreview(this);
		buffPreview = new UIBuffPreview(this);
		
		pickPane = new UIPane(this, false) {
			@Override
			protected void paintBackground(GraphAssist g) {
				g.graph.setBackground(transparent);
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
		
		crosshair = new UIIcon(this, "icons/crosshair.png") {
			@Override
			public void render(RenderTarget target) {
				if(!Ruins.ruins.isOverlayActive())
					super.render(target);
			}
		};
		
		popup = new UIPopup(this);
	}
	
	@Override
	public UIElement getElementAt(float x, float y) {
		return null;
	}
	
	@Override
	public void layout() {
		float s = UIIcon.pixelSize * getPixelSize();
		heartIcon.setPosition(10, getHeight()-heartIcon.getHeight()/2f-s*25);
		waterIcon.setPosition(10, heartIcon.getY()+heartIcon.getHeight());
		coinIcon.setPosition(10, heartIcon.getY()-heartIcon.getHeight());
		healthBar.setSize(s*40, heartIcon.getHeight()-6*s);
		healthBar.setPosition(10+3*s+heartIcon.getWidth(), heartIcon.getY()+3*s);
		waterBar.setSize(healthBar.getWidth(), healthBar.getHeight());
		waterBar.setPosition(healthBar.getX(), waterIcon.getY()+3*s);
		coinsPane.setSize(healthBar.getWidth(), healthBar.getHeight());
		coinsPane.setPosition(healthBar.getX(), coinIcon.getY()+3*s);

		obeliskDots.setPosition(getWidth()-obeliskDots.getWidth()-s*5, getHeight()-obeliskDots.getHeight()-s*5);
		invPreview.setPosition(obeliskDots.getX()-invPreview.getWidth() - s*5, getHeight()-invPreview.getHeight()-s*5);
		buffPreview.setPosition(getWidth()/2f-buffPreview.getWidth()/2f, s);
		
		crosshair.setPosition(getWidth()/2f-crosshair.getWidth()/2f, getHeight()/2f-crosshair.getHeight()/2f);
		pickPane.setPosition(getWidth()/2f-pickPane.getWidth()/2f, crosshair.getY()+crosshair.getHeight()+s*3);
		popup.setPosition(getWidth()/2f-popup.getWidth()/2f, getHeight()-120-popup.getHeight());
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
	
	public void updateInventoryPreview() {
		invPreview.repaint();
	}
	
	@Override
	public void updateTime(float dt) {
		if(Ruins.world==null || Ruins.preview)
			return;
		String pick = Ruins.pick.pickObject==null ? null : Ruins.pick.pickObject.getPickName();
		if(pick!=shownPick)
			updatePickText(pick);
		if(Ruins.world.player.coins!=shownCoins) {
			shownCoins = Ruins.world.player.coins;
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
