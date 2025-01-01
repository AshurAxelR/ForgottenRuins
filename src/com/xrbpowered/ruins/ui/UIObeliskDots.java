package com.xrbpowered.ruins.ui;

import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.gl.ui.pane.UITexture;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.world.ObeliskSystem;
import com.xrbpowered.ruins.world.obj.Obelisk;
import com.xrbpowered.zoomui.UIContainer;

public class UIObeliskDots extends UINode {
	
	private static final int num = ObeliskSystem.obeliskCount;
	
	private Texture dotOn, dotOff;
	private final UITexture[] offDots;
	private final UITexture[] onDots;
	private final UITexture onPortal;
	
	public UIObeliskDots(UIContainer parent) {
		super(parent);
		this.offDots = new UITexture[num];
		this.onDots = new UITexture[num];
		for(int i=0; i<num; i++) {
			offDots[i] = new UITexture(this);
			onDots[i] = new UITexture(this);
		}
		this.onPortal = new UITexture(this);
	}

	@Override
	public void setupResources() {
		dotOff = new Texture("icons/dot0.png", false, false);
		dotOn = new Texture("icons/dot1.png", false, false);
		float s = UIIcon.pixelSize * getPixelSize();
		float dotSize = dotOff.getWidth() * s;
		float span = s;
		for(int i=0; i<num; i++) {
			offDots[i].pane.setTexture(dotOff);
			offDots[i].setSize(dotSize, dotSize);
			offDots[i].setPosition((ObeliskSystem.dx[i]+1)*(dotSize+span), (ObeliskSystem.dy[i]+1)*(dotSize+span));
			onDots[i].pane.setTexture(dotOn);
			onDots[i].setSize(dotSize, dotSize);
			onDots[i].setPosition(offDots[i].getX(), offDots[i].getY());
		}
		float size = (dotSize+span)*3-span;
		setSize(size, size);
		onPortal.pane.setTexture(new Texture("icons/portal_open.png", false, false));
		float portalSize = onPortal.pane.getTexture().getWidth() * s;
		onPortal.setSize(portalSize, portalSize);
		onPortal.setPosition(size/2f-portalSize/2f, size/2f-portalSize/2f);
	}
	
	@Override
	public void releaseResources() {
		dotOff.release();
		dotOn.release();
		super.releaseResources();
	}
	
	@Override
	public void updateTime(float dt) {
		ObeliskSystem obelisks = Ruins.world.obelisks;
		for(int i=0; i<num; i++) {
			Obelisk obelisk = obelisks.obelisks.get(i);
			offDots[i].setVisible(!obelisk.visited);
			onDots[i].setVisible(obelisk.visited);
		}
		onPortal.setVisible(obelisks.portal.active);
	}
	
}
