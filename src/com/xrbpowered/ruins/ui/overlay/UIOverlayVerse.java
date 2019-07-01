package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.ruins.world.VerseSystem;
import com.xrbpowered.ruins.world.obj.Tablet;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.std.UIFormattedLabel;
import com.xrbpowered.zoomui.std.UIFormattedLabel.ZoomableCss;

public class UIOverlayVerse extends UIOverlay {

	public final UIPane box;
	public final UIFormattedLabel text;
	public final UIPane info;
	public final UIFormattedLabel infoText;
	
	private static ZoomableCss css = new ZoomableCss("p{text-align:center} p.dim{color:#aaaaaa} span.e{font-style:normal;color:#ffdd55}");
	
	public UIOverlayVerse(UIContainer parent) {
		super(parent);
		
		box = new UIPane(this, true) {
			@Override
			protected void paintSelf(GraphAssist g) {
				g.fill(this, Color.BLACK);
				super.paintSelf(g);
			}
		};
		box.setSize(560, 160);
		text = new UIFormattedLabel(box, "") {
			@Override
			public void setupHtmlKit() {
				htmlKit.defaultFont = UIHud.fontBold;
				htmlKit.defaultColor = Color.WHITE;
				htmlKit.zoomableCss = css;
			}
		};
		text.setSize(box.getWidth()-40, box.getHeight()-60);
		text.setLocation(20, 30);
		
		info = new UIPane(this, false) {
			@Override
			protected void paintSelf(GraphAssist g) {
				g.graph.setBackground(new Color(0, true));
				g.graph.clearRect(0, 0, (int)getWidth(), (int)getHeight());
				super.paintSelf(g);
			}
		};
		info.setSize(text.getWidth()-100, 100);
		infoText = new UIFormattedLabel(info, "") {
			@Override
			public void setupHtmlKit() {
				htmlKit.defaultFont = UIHud.font;
				htmlKit.defaultColor = new Color(0xeeeeee);
				htmlKit.zoomableCss = css;
			}
		};
		infoText.setSize(info.getWidth(), info.getHeight());
	}
	
	@Override
	public void layout() {
		box.setLocation(getWidth()/2f-box.getWidth()/2f, getHeight()/2f-box.getHeight()/2f);
		info.setLocation(getWidth()/2f-info.getWidth()/2f, getHeight()-200);
		super.layout();
	}
	
	public void updateAndShow(VerseSystem verses, Tablet tablet) {
		String html = verses.access(tablet.seed, !tablet.visited);
		text.setHtml(html);
		text.repaint();
		if(tablet.visited)
			infoText.setHtml("<p>You studied this tablet before.</p>");
		else if(verses.learned!=null)
			infoText.setHtml("<p>You thoroughly studied the tablet and learned the word for <span class=\"e\">" + verses.learned + "</span>.</p>");
		else
			infoText.setHtml("<p>You read the tablet but didn't learn any new words.</p>");
		infoText.repaint();
		Ruins.ruins.setOverlay(Ruins.overlayVerse);
	}

}
