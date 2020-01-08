package com.xrbpowered.ruins.entity.player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.world.item.Item;
import com.xrbpowered.ruins.world.item.ItemList;

public class PlayerInventory extends ItemList {

	public void load(DataInputStream in) throws IOException {
		clear();
		int num = in.readInt();
		for(int i=0; i<num; i++) {
			Item item = Item.itemById(in.readInt());
			int count = in.readInt();
			if(item!=null && count>0)
				add(item, count);
		}
	}
	
	public void save(DataOutputStream out) throws IOException {
		out.writeInt(stacks.size());
		for(ItemStack s : stacks.values()) {
			out.writeInt(s.item.id);
			out.writeInt(s.count);
		}
	}
	
	@Override
	public void add(Item item, int count) {
		if(item==Item.coins)
			Ruins.world.player.coins += count;
		else
			super.add(item, count);
	}

}
