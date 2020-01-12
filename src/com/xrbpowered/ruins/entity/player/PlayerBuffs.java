package com.xrbpowered.ruins.entity.player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.buff.Buff;

public class PlayerBuffs {

	public HashMap<Buff, Float> active = new HashMap<>();
	
	public void add(Buff buff) {
		if(buff!=null) {
			active.put(buff, buff.duration*60f);
			buff.activate();
			Ruins.hud.repaint();
		}
	}
	
	public boolean has(Buff buff) {
		return active.containsKey(buff);
	}
	
	public float getRemaining(Buff buff) {
		Float t = active.get(buff);
		return t==null ? 0f : t;
	}
	
	public void update(float dt) {
		for(Iterator<Map.Entry<Buff, Float>> i = active.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry<Buff, Float> e = i.next();
			float t = e.getValue() - dt;
			if(t>0f)
				e.setValue(t);
			else {
				i.remove();
				e.getKey().deactivate();
				Ruins.hud.repaint();
			}
		}
	}
	
	public void removeAll() {
		for(Buff buff : active.keySet())
			buff.deactivate();
		active.clear();
	}
}
