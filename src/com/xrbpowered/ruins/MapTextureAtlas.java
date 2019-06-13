package com.xrbpowered.ruins;

import java.util.ArrayList;

import org.joml.Vector2f;

import com.xrbpowered.gl.res.texture.Texture;

public class MapTextureAtlas {

	public static final int size = 2;
	public static final float dtile = 1f/(float)size;
	
	private static class Tile {
		public int u, v;
		public Tile(int u, int v) {
			this.u = u;
			this.v = v;
		}
		public Vector2f uv(float du, float dv) {
			return new Vector2f((u+du)*dtile, (v+dv)*dtile);
		}
		public Vector2f uvHalf(float du, float dv, int h) {
			return new Vector2f((u+du)*dtile, (v+dv*0.5f+h*0.5f)*dtile);
		}
	}
	
	private static class Versions extends ArrayList<Tile> {
		public void add(int u, int v) {
			add(new Tile(u, v));
		}
		
		/*public void addLine(int u1, int u2, int v) {
			for(int u=u1; u<=u2; u++)
				add(u, v);
		}

		public void addRect(int u1, int v1, int u2, int v2) {
			for(int u=u1; u<=u2; u++)
				for(int v=v1; u<=v2; v++)
					add(u, v);
		}*/

		public Tile get(long s) {
			return super.get((int)s % size());
		}
	}
	
	private static Versions top;
	private static Versions bottom;
	private static Versions side;
	private static Versions rampTop;
	private static Versions rampSide;
	
	static {
		top = new Versions();
		top.add(0, 0);
		
		bottom = new Versions();
		bottom.add(0, 0);
		
		side = new Versions();
		side.add(1, 0);

		rampTop = new Versions();
		rampTop.add(0, 0);
		
		rampSide = new Versions();
		rampSide.add(1, 0);
	}
	
	private Texture texture;
	
	public MapTextureAtlas() {
		texture = new Texture("map.png", false, false);
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public Vector2f top(long s, float du, float dv) {
		return top.get(s).uv(du, dv);
	}

	public Vector2f bottom(long s, float du, float dv) {
		return bottom.get(s).uv(du, dv);
	}

	public Vector2f side(long s, int h, float du, float dv) {
		return side.get(s).uvHalf(du, dv, h);
	}

	public Vector2f rampTop(long s, float du, float dv) {
		return rampTop.get(s).uv(du, dv);
	}

	public Vector2f rampSide(long s, int h, float du, float dv) {
		return side.get(s).uvHalf(du, dv, h);
	}

	public void release() {
		texture.release();
	}

}
