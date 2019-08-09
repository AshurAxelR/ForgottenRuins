package com.xrbpowered.ruins.world.item;

import java.util.TreeMap;

import com.xrbpowered.ruins.entity.player.PlayerEntity;

public class ItemList {

	public class ItemStack implements Comparable<ItemStack> {
		public final Item item;
		public int count;
		
		public ItemStack(Item item) {
			this.item = item;
			this.count = 0;
		}
		
		public void add(int count) {
			this.count += count;
			if(this.count>99)
				this.count = 99;
			else if(this.count<=0)
				stacks.remove(item.id);
		}
		
		@Override
		public int compareTo(ItemStack o) {
			return Integer.compare(this.item.id, o.item.id);
		}
	}

	public TreeMap<Integer, ItemStack> stacks = new TreeMap<>();

	public void clear() {
		stacks.clear();
	}
	
	public ItemStack getStack(Item item) {
		return stacks.get(item.id);
	}

	public int count(Item item) {
		ItemStack s = getStack(item);
		return s==null ? 0 : s.count;
	}
	
	public boolean has(Item item) {
		return stacks.containsKey(item.id);
	}
	
	public int countStacks() {
		return stacks.size();
	}
	
	public void add(Item item, int count) {
		ItemStack s = getStack(item);
		if(s==null) {
			if(count<=0)
				return;
			s = new ItemStack(item);
			stacks.put(item.id, s);
		}
		s.add(count);
	}
	
	public boolean use(Item item, PlayerEntity player) {
		ItemStack s = getStack(item);
		if(s==null)
			return false;
		if(!item.use(player))
			return false;
		s.add(-1);
		return true;
	}
	
	public void moveTo(ItemList list) {
		for(ItemStack s : stacks.values()) {
			list.add(s.item, s.count);
		}
		stacks.clear();
	}
	
}
