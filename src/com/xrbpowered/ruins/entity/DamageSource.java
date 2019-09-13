package com.xrbpowered.ruins.entity;

public enum DamageSource {
	fall("Watch your step..."),
	drown("Beware of quicksands..."),
	dehydrate("Drink more water..."),
	mob("Slain, defeated..."),
	portal("Your sacrificed too much...");

	public final String gameOverMessage;
	
	private DamageSource(String gameOverMessage) {
		this.gameOverMessage = gameOverMessage;
	}
	
	public static DamageSource fromInt(int i) {
		return (i<0) ? null : values()[i];
	}
	
	public static int toInt(DamageSource d) {
		return (d==null) ? -1 : d.ordinal();
	}

}
