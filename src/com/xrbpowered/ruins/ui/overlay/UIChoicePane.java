package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.ruins.ui.UIIcon;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class UIChoicePane extends UIButtonPane {

	public static final Color disabledColor = new Color(0x555555);
	public static final Color hoverColor = Color.WHITE;
	public static final Color buttonColor = new Color(0xaaaaaa);
	public static final Color downColor = new Color(0x222222);
	public static final Color descriptionColor = new Color(0xaaaaaa);
	
	public static final int width = 600;
	public static final int height = 80;
	
	public String description;
	public String unlock;

	public int margin = 5;

	public UIChoicePane(UIContainer parent, String title, String description, String unlock) {
		super(parent, title);
		this.description = description;
		this.unlock = unlock;
		setSize(width, height);
	}

	@Override
	protected void paintBackground(GraphAssist g) {
		g.setColor(down ? downColor : Color.BLACK);
		g.fill(this);
		g.setColor(!enabled ? disabledColor : hover ?  hoverColor : buttonColor);
		g.setStroke(UIIcon.pixelSize*2f);
		g.border(this);
		g.setColor(!enabled ? disabledColor : Color.WHITE);
		g.setFont(UIHud.fontBold);
		g.drawString(label, margin*UIIcon.pixelSize, getHeight()/3, GraphAssist.LEFT, GraphAssist.CENTER);
		g.setColor(descriptionColor);
		g.setFont(UIHud.font);
		g.drawString(enabled ? description : unlock, margin*UIIcon.pixelSize, getHeight()*2/3, GraphAssist.LEFT, GraphAssist.CENTER);
	}
	
}
