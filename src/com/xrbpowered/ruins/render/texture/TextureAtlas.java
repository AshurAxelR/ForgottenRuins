package com.xrbpowered.ruins.render.texture;

import java.util.ArrayList;

import org.joml.Vector2f;

import com.xrbpowered.gl.res.texture.Texture;

public class TextureAtlas {

	public static final int sizex = 4;
	public static final int sizey = 2;
	public static final float dtilex = 1f/(float)sizex;
	public static final float dtiley = 1f/(float)sizey;
	
	public static class Sprite {
		public int u, v;
		public Sprite(int u, int v) {
			this.u = u;
			this.v = v;
		}
		public Vector2f uv(float du, float dv) {
			return new Vector2f((u+du)*dtilex, (v+dv)*dtiley);
		}
		public Vector2f uvHalf(float du, float dv, int h) {
			return new Vector2f((u+du)*dtilex, (v+dv*0.5f+h*0.5f)*dtiley);
		}
	}
	
	public static class Versions extends ArrayList<Sprite> {
		public void add(int u, int v) {
			add(new Sprite(u, v));
		}
		
		public void addLine(int u1, int u2, int v) {
			for(int u=u1; u<=u2; u++)
				add(u, v);
		}

		public void addRect(int u1, int v1, int u2, int v2) {
			for(int u=u1; u<=u2; u++)
				for(int v=v1; v<=v2; v++)
					add(u, v);
		}

		public Sprite get(long s) {
			return super.get(((int)(s>>7L) & 0x7fffffff) % size());
		}
	}
	
	public Versions top;
	public Versions bottom;
	public Versions side;
	public Versions rampTop;
	public Versions rampSide;

	private Texture texture;
	
	public TextureAtlas() {
		texture = new Texture("wall.png", false, false);
		
		top = new Versions();
		top.add(0, 0);
		
		bottom = new Versions();
		bottom.add(0, 0);
		
		side = new Versions();
		side.addRect(1, 0, 3, 1);

		rampTop = new Versions();
		rampTop.add(0, 1);
		
		rampSide = new Versions();
		rampSide.add(1, 0);
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
