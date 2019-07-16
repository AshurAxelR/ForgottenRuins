package com.xrbpowered.ruins.render.prefab;

import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.ruins.render.ComponentRenderer;

public abstract class InstanceRenderer<C extends InstanceComponent> extends ComponentRenderer<C> {

	public InstanceRenderer(String basePath) {
		super(basePath);
	}

	@Override
	protected Shader getShader() {
		return InstanceShader.getInstance();
	}

}
