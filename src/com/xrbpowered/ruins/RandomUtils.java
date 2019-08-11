package com.xrbpowered.ruins;

import java.util.Random;

import org.joml.Vector3f;

public class RandomUtils {

	public static Random random = new Random();
	
	public static int random(int min, int max) {
		return random.nextInt(max-min)+min;
	}
	
	public static float random(float min, float max) {
		return random.nextFloat()*(max-min)+min;
	}
	
	public static Vector3f random(Vector3f out, Vector3f in, float r) {
		out.x = (in==null ? 0f : in.x) + random(-r, r);
		out.z = (in==null ? 0f : in.z) + random(-r, r);
		out.y = (in==null ? 0f : in.y) + random(-r, r);
		return out;
	}

	public static Vector3f random(Vector3f out, Vector3f in, float rx, float rz, float ymin, float ymax) {
		out.x = (in==null ? 0f : in.x) + random(-rx, rx);
		out.z = (in==null ? 0f : in.z) + random(-rz, rz);
		out.y = (in==null ? 0f : in.y) + random(ymin, ymax);
		return out;
	}

	public static int random(Random random, int min, int max) {
		return random.nextInt(max-min)+min;
	}
	
	public static float random(Random random, float min, float max) {
		return random.nextFloat()*(max-min)+min;
	}
	
	public static int weighted(Random random, float[] w) {
		float max = 0;
		for(int i = 0; i < w.length; i++)
			max += w[i];
		if(max<=0f)
			return 0;
		float x = random.nextFloat()*max;
		for(int i = 0;; i++) {
			if(x < w[i])
				return i;
			x -= w[i];
		}
	}

}
