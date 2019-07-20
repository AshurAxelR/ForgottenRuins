package com.xrbpowered.ruins.ui.overlay;

import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.world.VerseSystem;
import com.xrbpowered.ruins.world.obj.Tablet;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayVerse extends UIOverlay {

	private final UIButtonPane closeButton;
	
	public final UIPane box;
	public final UIText text;
	public final UIPane info;
	public final UIText infoText;
	public final UINode list;
	public final UIButtonPane[] listButtons;
	
	public UIOverlayVerse(UIContainer parent) {
		super(parent);
		dismissOnRightClick = true;
		
		closeButton = new UIButtonPane(this, "Close") {
			@Override
			public void onAction() {
				dismiss();
			}
		};

		box = new UISolidPane.Black(this);
		box.setSize(560, 160);
		text = new UIText(box);
		
		info = new UISolidPane.Clear(this);
		info.setSize(text.getWidth()-100, 100);
		infoText = new UIText.Small(info);
		
		list = new UINode(this);
		listButtons = new UIButtonPane[VerseSystem.verses.length];
		for(int i=0; i<VerseSystem.verses.length; i++) {
			final int verse = i;
			UIButtonPane btn = new UIButtonPane(list, RomanNumerals.toRoman(i+1)) {
				@Override
				public void onAction() {
					show(verse);
				}
			};
			btn.enabled = false;
			btn.setSize(48, btn.getHeight());
			btn.setLocation(i*52, 0);
			listButtons[i] = btn;
		}
		list.setSize(listButtons.length*52-4, listButtons[0].getHeight());
	}
	
	@Override
	public void layout() {
		closeButton.setLocation(getWidth()/2f-closeButton.getWidth()/2f, getHeight()-100);
		box.setLocation(getWidth()/2f-box.getWidth()/2f, getHeight()/2f-box.getHeight()/2f);
		info.setLocation(getWidth()/2f-info.getWidth()/2f, getHeight()-200);
		list.setLocation(getWidth()/2f-list.getWidth()/2f, box.getY()-list.getHeight()-30);
		super.layout();
	}

	public void show(int verse) {
		String html = String.format("<p>- %s -</p><p>%s</p>", RomanNumerals.toRoman(verse+1), VerseSystem.verses[verse].toUpperCase());
		text.setHtml(html);
		infoText.setHtml("");
		Ruins.ruins.setOverlay(Ruins.overlayVerse);
	}

	public void updateAndShow(VerseSystem verses, Tablet tablet) {
		String html = verses.access(tablet.seed, !tablet.visited);
		text.setHtml(html);
		if(tablet.visited)
			infoText.setHtml("<p>You studied this tablet before.</p>");
		else if(verses.learned!=null)
			infoText.setHtml("<p>You thoroughly studied the tablet and learned the word for <span class=\"e\">" + verses.learned + "</span>.</p>");
		else
			infoText.setHtml("<p>You read the tablet but didn't learn any new words.</p>");
		listButtons[verses.verse].setEnabled(verses.complete);
		Ruins.ruins.setOverlay(Ruins.overlayVerse);
	}

}
