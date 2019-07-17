package com.xrbpowered.ruins.render.effect.particle;

import java.util.Random;

import org.joml.Vector3f;

public abstract class ParticleEffect {

	protected static final Random random = new Random();
	
	public Vector3f pivot = new Vector3f();
	public float rx, rz, ymin, ymax;
	public float speedMin, speedMax;

	public ParticleEffect(float rx, float rz, float ymin, float ymax) {
		this.rx = rx;
		this.rz = rz;
		this.ymin = ymin;
		this.ymax = ymax;
	}

	public ParticleEffect(float rx, float rz, float ry) {
		this(rx, rz, -ry, ry);
	}
	
	public ParticleEffect(float r) {
		this(r, r, r);
	}
	
	public ParticleEffect() {
		this(0f, 0f, 0f);
	}
	
	public ParticleEffect setSpeed(float min, float max) {
		this.speedMin = min;
		this.speedMax = max;
		return this;
	}
	
	public abstract void generateParticle();
	public abstract void generate();
	
	public void generate(int count) {
		for(int i=0; i<count; i++)
			generateParticle();
	}
	
	protected void assign(Particle p) {
		assignPosition(p);
		assignSpeed(p);
	}
	
	protected void assignPosition(Particle p) {
		random(p.position, pivot, rx, rz, ymin, ymax);
	}

	protected void assignSpeed(Particle p, float speed) {
	}
	
	protected void assignSpeed(Particle p) {
		assignSpeed(p, random(speedMin, speedMax));
	}

	protected static int random(int min, int max) {
		return random.nextInt(max-min)+min;
	}
	
	protected static float random(float min, float max) {
		return random.nextFloat()*(max-min)+min;
	}
	
	protected static Vector3f random(Vector3f out, Vector3f in, float r) {
		out.x = (in==null ? 0f : in.x) + random(-r, r);
		out.z = (in==null ? 0f : in.z) + random(-r, r);
		out.y = (in==null ? 0f : in.y) + random(-r, r);
		return out;
	}

	protected static Vector3f random(Vector3f out, Vector3f in, float rx, float rz, float ymin, float ymax) {
		out.x = (in==null ? 0f : in.x) + random(-rx, rx);
		out.z = (in==null ? 0f : in.z) + random(-rz, rz);
		out.y = (in==null ? 0f : in.y) + random(ymin, ymax);
		return out;
	}

	public static abstract class Up extends ParticleEffect {
		public Up(float rx, float rz, float ymin, float ymax) {
			super(rx, rz, ymin, ymax);
		}
		public Up(float rx, float rz, float ry) {
			super(rx, rz, ry);
		}
		public Up(float r) {
			super(r);
		}
		public Up() {
		}
		@Override
		protected void assignSpeed(Particle p, float speed) {
			p.speed.y = speed;
		}
	}

	public static abstract class Radial extends ParticleEffect {
		public Radial(float rx, float rz, float ymin, float ymax) {
			super(rx, rz, ymin, ymax);
		}
		public Radial(float rx, float rz, float ry) {
			super(rx, rz, ry);
		}
		public Radial(float r) {
			super(r);
		}
		public Radial() {
		}

		@Override
		protected void assignSpeed(Particle p, float speed) {
			p.speed.set(p.position);
			p.speed.sub(pivot);
			p.speed.normalize();
			p.speed.mul(speed);
		}
	}

	public static abstract class Rand extends ParticleEffect {
		public Rand(float rx, float rz, float ymin, float ymax) {
			super(rx, rz, ymin, ymax);
		}
		public Rand(float rx, float rz, float ry) {
			super(rx, rz, ry);
		}
		public Rand(float r) {
			super(r);
		}
		public Rand() {
		}

		@Override
		protected void assignSpeed(Particle p, float speed) {
			random(p.speed, null, 1f);
			p.speed.normalize();
			p.speed.mul(speed);
		}
	}
}
