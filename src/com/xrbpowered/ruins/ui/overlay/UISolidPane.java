package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class UISolidPane extends UIPane {

	public Color fill;
	
	public UISolidPane(UIContainer parent, boolean opaque, Color fill) {
		super(parent, opaque);
		this.fill = fill;
	}

	@Override
	protected void paintSelf(GraphAssist g) {
		g.graph.setBackground(fill);
		g.graph.clearRect(0, 0, (int)getWidth(), (int)getHeight());
	}
	
	public static class Black extends UISolidPane {
		public Black(UIContainer parent) {
			super(parent, true, Color.BLACK);
		}
	}

	public static class Clear extends UISolidPane {
		public Clear(UIContainer parent) {
			super(parent, false, new Color(0, true));
		}
	}

}
