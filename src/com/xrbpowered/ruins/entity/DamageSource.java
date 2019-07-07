package com.xrbpowered.ruins.entity;

public enum DamageSource {
	fall("Watch your step..."),
	drown("Beware of quicksands..."),
	dehydrate("Drink more water...");

	public final String gameOverMessage;
	
	private DamageSource(String gameOverMessage) {
		this.gameOverMessage = gameOverMessage;
	}
}
