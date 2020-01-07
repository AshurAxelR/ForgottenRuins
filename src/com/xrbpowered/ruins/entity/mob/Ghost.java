package com.xrbpowered.ruins.entity.mob;

import static com.xrbpowered.ruins.RandomUtils.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import com.xrbpowered.ruins.entity.DamageSource;
import com.xrbpowered.ruins.entity.EntityCollider;
import com.xrbpowered.ruins.entity.player.PlayerController;
import com.xrbpowered.ruins.render.effect.particle.Particle;
import com.xrbpowered.ruins.render.effect.particle.ParticleEffect;
import com.xrbpowered.ruins.render.effect.particle.ParticleGenerator;
import com.xrbpowered.ruins.render.effect.particle.ParticleRenderer;
import com.xrbpowered.ruins.render.prefab.EntityComponent;
import com.xrbpowered.ruins.render.prefab.MobRenderer;
import com.xrbpowered.ruins.world.World;

public class Ghost extends MobEntity {

	public static final int typeId = 0;
	
	public static final float radius = 0.3f;
	public static final float height = 1.7f;
	
	public static final float speedMin = 0.75f;
	public static final float speedMax = 1.25f;
	public static final float agitatedSpeedMin = 1.5f;
	public static final float agitatedSpeedMax = 1.7f;
	public static final int agitationDist = 20;
	public static final int chargeDist = 5;
	
	public static final float minLifespan = 120f; // 150f; 
	public static final float maxLifespan = 180f; // 600f; 

	public static final float damageMin = 25f;
	public static final float damageMax = 40f;
	
	public float speed;
	public float agitatedSpeed;
	public float lifespan;
	public float damage;
	
	public boolean agitated = false;
	public boolean charging = false;
	private ParticleGenerator chargeParticles = new ParticleGenerator(explosion, 0.02f, 0.1f);

	public Ghost(World world, Random random) {
		super(world, speedMin);
		if(random!=null) {
			speed = random(random, speedMin, speedMax);
			agitatedSpeed = random(random, agitatedSpeedMin, agitatedSpeedMax);
			lifespan = random(random, minLifespan, maxLifespan);
			damage = random(random, damageMin, damageMax);
		}
	}
	
	@Override
	public int getTypeId() {
		return typeId;
	}
	
	@Override
	public void loadState(DataInputStream in) throws IOException {
		super.loadState(in);
		speed = in.readFloat();
		agitatedSpeed = in.readFloat();
		lifespan = in.readFloat();
		damage = in.readFloat();
	}
	
	@Override
	public void saveState(DataOutputStream out) throws IOException {
		super.saveState(out);
		out.writeFloat(speed);
		out.writeFloat(agitatedSpeed);
		out.writeFloat(lifespan);
		out.writeFloat(damage);
	}

	@Override
	public EntityComponent getRenderComponent() {
		return MobRenderer.ghost;
	}
	
	@Override
	public void setEntityDimensions(EntityCollider collider) {
		collider.setEntityDimensions(radius, height, PlayerController.dyPoints);
	}

	@Override
	protected void setTarget() {
		super.setTarget();
		agitated = !controller.noTarget && (controller.targetDist>0 && controller.targetDist<agitationDist);
		charging = agitated && controller.targetDist<chargeDist;
		controller.walkSpeed = agitated ? agitatedSpeed : speed;
		if(charging)
			controller.walkSpeed *= 1.5f;
	}

	@Override
	public void disappear() {
		setExplosionPivot(this);
		explosion.generate(20);
		super.disappear();
	}
	
	@Override
	public boolean updateTime(float dt) {
		super.updateTime(dt);
		if(charging) {
			setExplosionPivot(this);
			chargeParticles.update(dt);
		}
		else
			chargeParticles.reset();
		if(time>spawnTime && this.getDistTo(world.player)<2.8f) {
			world.player.applyDamage(damage, DamageSource.mob);
			setExplosionPivot(this);
			explosion.generate();
			smoke.pivot.set(position);
			smoke.generate();
			alive = false;
		}
		if(time>lifespan && !agitated)
			disappear();
		return alive;
	}
	
	private static void setExplosionPivot(Ghost ghost) {
		explosion.pivot.set(ghost.position);
		explosion.pivot.y += height/2f;
	}
	
	public static class DotParticle extends Particle {
		public DotParticle() {
			super(random(0.5f, 1.5f));
		}
		@Override
		public boolean updateTime(float dt) {
			random(speed, speed, 25f*dt);
			speed.mul((float)Math.pow(0.15f, dt));
			return super.updateTime(dt);
		}
	}
	
	public static ParticleEffect explosion = new ParticleEffect.Radial(radius/2f) {
		@Override
		public void generateParticle() {
			Particle p = new DotParticle();
			assign(p);
			ParticleRenderer.dark.add(p);
		}
		@Override
		public void generate() {
			generate(80);
		}
	}.setSpeed(2f, 5f);
	
	public static ParticleEffect smoke = new ParticleEffect.Rand(radius, radius, 0f, height) {
		@Override
		public void generateParticle() {
			Particle p = new Particle(random(0.5f, 1f));
			assign(p);
			ParticleRenderer.smoke.add(p);
		}
		@Override
		public void generate() {
			generate(12);
		}
	}.setSpeed(0.2f, 1f);
	
}
