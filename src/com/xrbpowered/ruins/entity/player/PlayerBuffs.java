package com.xrbpowered.ruins.entity.player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.player.buff.Buff;

public class PlayerBuffs {

	private HashMap<Buff, Float> active = new HashMap<>();
	
	public void load(DataInputStream in) throws IOException {
		removeAll();
		int num = in.readInt();
		for(int i=0; i<num; i++) {
			Buff buff = Buff.buffById(in.readInt());
			float t = in.readFloat();
			if(buff!=null && t>=0)
				add(buff, t);
		}
	}
	
	public void save(DataOutputStream out) throws IOException {
		out.writeInt(active.size());
		for(Map.Entry<Buff, Float> e : active.entrySet()) {
			out.writeInt(e.getKey().id);
			out.writeFloat(e.getValue());
		}
	}
	
	public int count() {
		return active.size();
	}
	
	private void add(Buff buff, float duration) {
		active.put(buff, duration);
		buff.activate();
	}

	public void add(Buff buff) {
		if(buff!=null) {
			add(buff, buff.duration*60f);
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
