package com.xrbpowered.ruins;

public class CellObject {

	public int x, z, y;

	public CellObject setLocation(int x, int z, int y) {
		this.x = x;
		this.z = z;
		this.y = y;
		return this;
	}
	
}
