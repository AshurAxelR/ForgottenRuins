package com.xrbpowered.ruins.entity.mob;

import java.util.Random;

import com.xrbpowered.ruins.entity.DamageSource;
import com.xrbpowered.ruins.entity.EntityCollider;
import com.xrbpowered.ruins.entity.player.PlayerController;
import com.xrbpowered.ruins.render.effect.particle.Particle;
import com.xrbpowered.ruins.render.effect.particle.ParticleEffect;
import com.xrbpowered.ruins.render.effect.particle.ParticleRenderer;
import com.xrbpowered.ruins.render.prefab.EntityComponent;
import com.xrbpowered.ruins.render.prefab.MobRenderer;
import com.xrbpowered.ruins.world.World;

public class Ghost extends MobEntity {

	public static final float radius = 0.3f;
	public static final float height = 1.7f;
	
	public static final float speedMin = 0.5f;
	public static final float speedMax = 1.0f;
	public static final float agitatedSpeedMin = 1.5f;
	public static final float agitatedSpeedMax = 1.7f;
	public static final int agitationDist = 25;
	public static final int chargeDist = 5;
	
	public static final float minLifespan = 150f; 
	public static final float maxLifespan = 600f; 

	public static final float damage = 30f;
	
	public float speed;
	public float agitatedSpeed;
	public float lifespan;
	
	public boolean agitated = false;
	
	public Ghost(World world, Random random) {
		super(world, speedMin);
		speed = random.nextFloat()*(speedMax-speedMin)+speedMin;
		agitatedSpeed = random.nextFloat()*(agitatedSpeedMax-agitatedSpeedMin)+agitatedSpeedMin;
		lifespan = random.nextFloat()*(maxLifespan-minLifespan)+minLifespan;
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
		controller.walkSpeed = agitated ? agitatedSpeed : speed;
		if(agitated && controller.targetDist<chargeDist)
			controller.walkSpeed *= 1.5f;
	}

	@Override
	public boolean updateTime(float dt) {
		super.updateTime(dt);
		if(time>spawnTime && this.getDistTo(world.player)<2.8f) {
			world.player.applyDamage(damage, DamageSource.mob);
			explosion.pivot.set(position);
			explosion.pivot.y += height/2f;
			explosion.generate();
			alive = false;
		}
		if(time>lifespan && !agitated)
			alive = false;
		return alive;
	}
	
	public static ParticleEffect explosion = new ParticleEffect.Radial(radius/2f, radius/2f, radius/2f) {
		@Override
		public void generateParticle() {
			Particle p = new Particle(random(0.5f, 1.5f)) {
				@Override
				public boolean updateTime(float dt) {
					float jitter = 25f*dt;
					random(speed, speed, jitter, jitter, jitter);
					speed.mul((float)Math.pow(0.2f, dt));
					return super.updateTime(dt);
				}
			};
			assign(p);
			ParticleRenderer.smookeDot.add(p);
		}
		@Override
		public void generate() {
			generate(80);
		}
	}.setSpeed(1.5f, 4f);
	
}
