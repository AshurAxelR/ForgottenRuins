package com.xrbpowered.ruins.world;

public enum Direction {
	north(0, -1), east(1, 0), south(0, 1), west(-1, 0);
	
	public final int dx, dz;
	
	private Direction(int dx, int dz) {
		this.dx = dx;
		this.dz = dz;
	}
	
	public Direction opposite() {
		return values()[(ordinal()+2) % 4];
	}
	
	public Direction cw() {
		return values()[(ordinal()+1) % 4];
	}
	
	public Direction ccw() {
		return values()[(ordinal()+3) % 4];
	}
	
	public float rotation() {
		return (ordinal()-1) * (float)Math.PI / 2f;
	}
}