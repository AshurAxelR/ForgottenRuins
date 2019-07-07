package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.std.UIFormattedLabel;

public class UIText extends UIFormattedLabel {

	public static ZoomableCss css = new ZoomableCss("p{text-align:center} p.dim{color:#aaaaaa} span.e{font-style:normal;color:#ffdd55}");

	public UIText(UIContainer parent, float marginx, float marginy) {
		super(parent, "");
		setSize(parent.getWidth()-marginx*2f, parent.getHeight()-marginy*2f);
		setLocation(marginx, marginy);
	}

	public UIText(UIContainer parent) {
		this(parent, 20, 30);
	}

	@Override
	public void setupHtmlKit() {
		htmlKit.defaultFont = UIHud.fontBold;
		htmlKit.defaultColor = Color.WHITE;
		htmlKit.zoomableCss = css;
	}
	
	public static class Small extends UIText {
		public Small(UIContainer parent, float marginx, float marginy) {
			super(parent, marginx, marginy);
		}
		public Small(UIContainer parent) {
			super(parent, 0, 0);
		}
		@Override
		public void setupHtmlKit() {
			htmlKit.defaultFont = UIHud.font;
			htmlKit.defaultColor = new Color(0xeeeeee);
			htmlKit.zoomableCss = css;
		}
	}
}
