package com.xrbpowered.ruins.entity.player.buff;

import com.xrbpowered.ruins.render.effect.xray.UIXrayPane;

public class XRayBuff extends Buff {

	public UIXrayPane xrayPane = null;
	
	public XRayBuff(int id, String name, int duration, int cost, String iconPath, String info) {
		super(id, name, duration, cost, iconPath, info);
	}

	@Override
	public void activate() {
		xrayPane.setVisible(true);
	}
	
	@Override
	public void deactivate() {
		xrayPane.setVisible(false);
	}
}
