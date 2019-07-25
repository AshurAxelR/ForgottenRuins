package com.xrbpowered.ruins;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import com.xrbpowered.ruins.world.World;

public class GlobalSettings {

	public static final int minWindowWidth = 1600;
	public static final int minWindowHeight = 900;
	
	public static final String path = "./ruins.cfg";
	
	public boolean fullscreen = false;
	public int windowedWidth = minWindowWidth;
	public int windowedHeight = minWindowHeight;
	public int uiScaling = 0;
	public int renderScaling = 1;
	public boolean vsync = false;
	public int noVsyncSleep = 4;
	public int fov = 75;
	public float mouseSensitivity = 0.002f;
	
	public int startLevel = 6;
	public boolean enableObserver = false;
	public boolean enableDebugPaths = false;
	
	public static HashMap<String, String> loadValues() {
		try {
			HashMap<String, String> values = new HashMap<>();
			Scanner in = new Scanner(new File(path));
			while(in.hasNextLine()) {
				String[] s = in.nextLine().trim().split("\\s*:\\s*", 2);
				if(s.length==2)
					values.put(s[0], s[1]);
			}
			in.close();
			return values;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	private static int getInt(String value, int min, int max, int fallback) {
		if(value==null)
			return fallback;
		try {
			int n = Integer.parseInt(value);
			if(n<min || n>max)
				return fallback;
			return n;
		}
		catch(NumberFormatException e) {
			return fallback;
		}
	}
	
	private static boolean getBoolean(String value, boolean fallback) {
		if(value==null)
			return fallback;
		if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"))
			return true;
		else if(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no"))
			return false;
		else
			return fallback;
	}
	
	public static GlobalSettings load() {
		HashMap<String, String> values = loadValues();
		GlobalSettings s = new GlobalSettings();
		if(values==null)
			return s;
		
		s.fullscreen = getBoolean(values.get("fullscreen"), s.fullscreen);
		s.windowedWidth = getInt(values.get("windowedWidth"), minWindowWidth, Integer.MAX_VALUE, s.windowedWidth);
		s.windowedHeight = getInt(values.get("windowedHeight"), minWindowHeight, Integer.MAX_VALUE, s.windowedHeight);
		s.uiScaling = getInt(values.get("uiScaling"), 0, 200, s.uiScaling);
		if(s.uiScaling<100)
			s.uiScaling = 0;
		s.renderScaling = getInt(values.get("renderScaling"), 1, 8, s.renderScaling);
		s.vsync = getBoolean(values.get("vsync"), s.vsync);
		s.noVsyncSleep = getInt(values.get("noVsyncSleep"), 0, 1000, s.noVsyncSleep);
		s.fov = getInt(values.get("fov"), 40, 80, s.fov);
		s.mouseSensitivity = getInt(values.get("mouseSensitivity"), 10, 1000, 100) / 100f * 0.002f;

		s.startLevel = getInt(values.get("startLevel"), 0, World.maxLevel, s.startLevel);
		s.enableObserver = getBoolean(values.get("enableObserver"), s.enableObserver);
		s.enableDebugPaths = getBoolean(values.get("enableDebugPaths"), s.enableDebugPaths);

		return s;
	}
	
}
