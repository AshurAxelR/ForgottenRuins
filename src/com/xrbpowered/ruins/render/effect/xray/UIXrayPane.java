package com.xrbpowered.ruins.render.effect.xray;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.xrbpowered.gl.res.buffer.OffscreenBuffer;
import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.ui.pane.PaneShader;
import com.xrbpowered.gl.ui.pane.UIOffscreen;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.buff.XRayBuff;
import com.xrbpowered.ruins.render.prefab.InstanceComponent;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.zoomui.UIContainer;

public class UIXrayPane extends UIOffscreen {

	public Color color = Color.WHITE;
	public float alpha = 1f;
	private InstanceComponent[] components = null;
	
	public UIXrayPane(UIContainer parent, XRayBuff buff, Color color, float alpha) {
		super(parent, Ruins.settings.renderScaling);
		this.color = color;
		this.alpha = alpha;
		if(buff!=null)
			buff.xrayPane = this;
		setVisible(false);
	}
	
	public void setComponents(InstanceComponent... components) {
		this.components = components;
	}

	public void setPrefabs(Prefab... prefabs) {
		components = new InstanceComponent[prefabs.length];
		for(int i=0; i<prefabs.length; i++)
			components[i] = prefabs[i].getInteractionComp();
	}

	@Override
	public void setupResources() {
		clearColor = Color.BLACK;
		pane.alpha = 0.75f;
		super.setupResources();
	}
	
	@Override
	public void renderBuffer(RenderTarget target) {
		if(Ruins.world==null || components==null)
			return;
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		super.renderBuffer(target);
		UIXRayNode.objectShader.use();
		for(InstanceComponent comp : components)
			comp.meshDrawCallInstanced();
		UIXRayNode.objectShader.unuse();
	}

	public void render(RenderTarget target) {
		if(isVisible()) {
			OffscreenBuffer buffer = pane.getBuffer();
			buffer.use();
			renderBuffer(buffer);
			buffer.resolve();
			
			target.use();
			
			XRayPaneShader shader = UIXRayNode.paneShader;
			shader.use();
			shader.setColor(color, alpha);
			shader.updatePaneSize(pane.width, pane.height);
			pane.bindTexture(0);
			PaneShader.getInstance().quad.draw();
			shader.unuse();
		}
	}
	
}
