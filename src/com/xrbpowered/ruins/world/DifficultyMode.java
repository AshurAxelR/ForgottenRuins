package com.xrbpowered.ruins.world;

public enum DifficultyMode {

	normal("Normal Mode", "If you die, restart the level."),
	peaceful("Peaceful Mode", "No enemies. Exit portal is Free."),
	hardcore("Hardcore Mode", "If you die, restart from Level I.");
	
	public final String title;
	public final String description;
	
	private DifficultyMode(String title, String description) {
		this.title = title;
		this.description = description;
	}
}
