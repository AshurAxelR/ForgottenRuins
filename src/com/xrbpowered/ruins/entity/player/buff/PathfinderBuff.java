package com.xrbpowered.ruins.entity.player.buff;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.render.effect.TracePathEffect;

public class PathfinderBuff extends Buff {

	public PathfinderBuff(int id) {
		super(id, "Pathfinder", 1, 100, "icons/pathfinder.png", "Traces path to the next obelisk");
	}

	@Override
	public void activate() {
		if(!TracePathEffect.show) {
			TracePathEffect.show = true;
			TracePathEffect.update(Ruins.world);
		}
	}
	
	@Override
	public void deactivate() {
		if(TracePathEffect.show) {
			TracePathEffect.show = false;
			TracePathEffect.update(Ruins.world);
		}
	}
	
}
