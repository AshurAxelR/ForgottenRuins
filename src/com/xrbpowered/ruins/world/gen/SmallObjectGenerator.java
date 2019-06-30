package com.xrbpowered.ruins.world.gen;

import java.util.ArrayList;
import java.util.Random;

import com.xrbpowered.ruins.render.prefab.PrefabComponent.InstanceInfo;
import com.xrbpowered.ruins.world.Tile;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;
import com.xrbpowered.ruins.world.obj.Jar;
import com.xrbpowered.ruins.world.obj.MapObject;
import com.xrbpowered.ruins.world.obj.SmallObject;

public class SmallObjectGenerator {

	private class Circle {
		public float x, z, r;
		
		public Circle(float r) {
			this.r = r;
		}
		
		public Circle setLocation(float x, float z) {
			this.x = x;
			this.z = z;
			return this;
		}
		
		public float sqDist(Circle c) {
			float dx = x-c.x;
			float dz = z-c.z;
			return dx*dx+dz*dz;
		}
		
		public boolean intersects(Circle c) {
			float rsum = r+c.r;
			return sqDist(c)<rsum*rsum;
		}
		
		public boolean intersectsAny() {
			for(Circle c : list) {
				if(intersects(c))
					return true;
			}
			return false;
		}
		
		/*public void intersectMove(float dz) {
			boolean retry = true;
			while(retry) {
				retry = false;
				for(Circle c : list) {
					if(intersects(c)) {
						z = c.z+dz*(c.r+r);
						retry = true;
						break;
					}
				}
			}
		}*/
	}
	
	public final World world;
	public final ArrayList<MapObject> objects; 
	
	public final WorldGenerator gen;

	private final Random random;
	private ArrayList<Circle> list = new ArrayList<>();
	
	public SmallObjectGenerator(WorldGenerator gen, Random random) {
		this.world = gen.world;
		this.random = random;
		this.objects = world.objects;
		this.gen = gen;
	}
	
	private boolean tryAdd(float r, InstanceInfo info) {
		Circle c = new Circle(r);
		float d = 2f - c.r*2f;
		float x = random.nextFloat();
		float z = random.nextFloat();
		c.setLocation(x*d - d/2f, z*d - d/2f);
		if(!c.intersectsAny()) {
			list.add(c);
			info.x = c.x;
			info.z = c.z;
			return true;
		}
		else
			return false;
	}
	
	public void fillTile(Token t) {
		Tile tile = gen.getTile(t);
		list.clear();
		int n = random.nextInt(4)+2;
		for(int i=0; i<n; i++) {
			SmallObject obj = new Jar(world, random);
			InstanceInfo info = new InstanceInfo(tile.light);
			info.scale = randomScale(obj.getScaleRange());
			info.rotate = random.nextFloat() * 2f * (float)Math.PI;
			if(tryAdd(obj.getRadius(info.scale), info)) {
				obj.place(t, info);
				tile.addSmallObject(obj);
				world.objects.add(obj);
			}
		}
	}
	
	private float randomScale(float range) {
		return 1f-range/2f+random.nextFloat()*range;
	}

}
