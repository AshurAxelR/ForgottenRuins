package com.xrbpowered.ruins.render.effect.particle;

import java.util.Random;

import org.joml.Vector3f;

public abstract class ParticleEffect {

	protected static final Random random = new Random();
	
	public Vector3f pivot = new Vector3f();
	public float rx, rz, ry;
	public float speedMin, speedMax;

	public ParticleEffect(float rx, float rz, float ry) {
		this.rx = rx;
		this.rz = rz;
		this.ry = ry;
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
		random(p.position, pivot, rx, rz, ry);
	}

	protected abstract void assignSpeed(Particle p, float speed);
	
	protected void assignSpeed(Particle p) {
		assignSpeed(p, random(speedMin, speedMax));
	}

	protected static int random(int min, int max) {
		return random.nextInt(max-min)+min;
	}
	
	protected static float random(float min, float max) {
		return random.nextFloat()*(max-min)+min;
	}
	
	protected static Vector3f random(Vector3f out, Vector3f in, float rx, float rz, float ry) {
		out.x = (in==null ? 0f : in.x) + random(-rx, rx);
		out.z = (in==null ? 0f : in.z) + random(-rz, rz);
		out.y = (in==null ? 0f : in.y) + random(-ry, ry);
		return out;
	}
	
	public static abstract class Up extends ParticleEffect {
		public Up(float rx, float rz, float ry) {
			this.rx = rx;
			this.rz = rz;
			this.ry = ry;
		}
		public Up(float r) {
			this(r, r, r);
		}
		public Up() {
			this(0f, 0f, 0f);
		}
		@Override
		protected void assignSpeed(Particle p, float speed) {
			p.speed.y = speed;
		}
	}

	public static abstract class Radial extends ParticleEffect {
		public Radial(float rx, float rz, float ry) {
			this.rx = rx;
			this.rz = rz;
			this.ry = ry;
		}
		public Radial(float r) {
			this(r, r, r);
		}
		public Radial() {
			this(0f, 0f, 0f);
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
		public Rand(float rx, float rz, float ry) {
			this.rx = rx;
			this.rz = rz;
			this.ry = ry;
		}
		public Rand(float r) {
			this(r, r, r);
		}
		public Rand() {
			this(0f, 0f, 0f);
		}
		@Override
		protected void assignSpeed(Particle p, float speed) {
			random(p.speed, null, 1f, 1f, 1f);
			p.speed.normalize();
			p.speed.mul(speed);
		}
	}
}
