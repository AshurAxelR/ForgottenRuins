package com.xrbpowered.ruins.ui;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.gl.ui.pane.UITexture;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.PlayerBuffs;
import com.xrbpowered.ruins.entity.player.buff.Buff;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class UIBuffPreview extends UINode {

	private final UITexture[] buffIcons;
	
	public UIBuffPreview(UIContainer parent) {
		super(parent);
		buffIcons = new UITexture[Buff.buffList.size()];
		int i = 0;
		for(Buff buff : Buff.buffList) {
			buffIcons[i] = new UITexture(this);
			buffIcons[i].setVisible(false);
			buff.ui = buffIcons[i]; 
			i++;
		}
	}

	@Override
	public void setupResources() {
		float s = UIIcon.pixelSize * getPixelScale();
		float iconSize = Buff.textureSize * s;
		setSize(Buff.buffList.size()*iconSize, iconSize);
		for(Buff buff : Buff.buffList) {
			buff.ui.pane.setTexture(buff.icon);
			buff.ui.setSize(iconSize, iconSize);
		}
	}
	
	@Override
	protected void paintSelf(GraphAssist g) {
		float s = UIIcon.pixelSize * getPixelScale();
		float iconSize = Buff.textureSize * s;
		PlayerBuffs buffs = Ruins.world.player.buffs;
		float x = getWidth()/2f - buffs.count()*iconSize/2f;
		for(Buff buff : Buff.buffList) {
			if(buffs.has(buff)) {
				buff.ui.setVisible(true);
				buff.ui.setLocation(x, 0);
				x += iconSize;
			}
			else {
				buff.ui.setVisible(false);
			}
		}
		super.paintSelf(g);
	}
	
	@Override
	public void render(RenderTarget target) {
		PlayerBuffs buffs = Ruins.world.player.buffs;
		for(Buff buff : Buff.buffList) {
			float t = buffs.getRemaining(buff);
			if(t<=0f)
				buff.ui.pane.alpha = 0f;
			else if(t>=9f)
				buff.ui.pane.alpha = 1f;
			else
				buff.ui.pane.alpha = t/9f*0.25f+0.75f*(0.5f-0.5f*(float)Math.cos(t * Math.PI));
		}
		super.render(target);
	}

}
