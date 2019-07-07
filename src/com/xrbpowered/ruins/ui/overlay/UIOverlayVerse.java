package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.ui.UIText;
import com.xrbpowered.ruins.world.VerseSystem;
import com.xrbpowered.ruins.world.obj.Tablet;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayVerse extends UIOverlay {

	private final UIButtonPane closeButton;
	
	public final UIPane box;
	public final UIText text;
	public final UIPane info;
	public final UIText infoText;
	
	public UIOverlayVerse(UIContainer parent) {
		super(parent);
		
		closeButton = new UIButtonPane(this, "Close") {
			@Override
			public void onAction() {
				dismiss();
			}
		};

		box = new UIPane(this, true) {
			@Override
			protected void paintSelf(GraphAssist g) {
				g.fill(this, Color.BLACK);
				super.paintSelf(g);
			}
		};
		box.setSize(560, 160);
		text = new UIText(box);
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
		infoText = new UIText.Small(info);
		infoText.setSize(info.getWidth(), info.getHeight());
	}
	
	@Override
	public void layout() {
		closeButton.setLocation(getWidth()/2f-closeButton.getWidth()/2f, getHeight()-100);
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
