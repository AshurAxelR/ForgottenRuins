package com.xrbpowered.ruins.entity;

public enum DamageSource {
	fall("Watch your step..."),
	drown("Beware of quicksands..."),
	dehydrate("Drink more water..."),
	mob("Slain, defeated..."); // FIXME better words for being killed by a mob

	public final String gameOverMessage;
	
	private DamageSource(String gameOverMessage) {
		this.gameOverMessage = gameOverMessage;
	}
}
