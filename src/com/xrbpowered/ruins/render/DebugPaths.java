package com.xrbpowered.ruins.render;

import java.util.Random;

import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.render.effect.particle.Particle;
import com.xrbpowered.ruins.render.effect.particle.ParticleLoop;
import com.xrbpowered.ruins.render.effect.particle.ParticleRenderer;
import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.PathFinder;
import com.xrbpowered.ruins.world.PathFinder.Token;
import com.xrbpowered.ruins.world.Tile;
import com.xrbpowered.ruins.world.TileType;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.Obelisk;
import com.xrbpowered.ruins.world.obj.TileObject;

public class DebugPaths {

	public static boolean show = false;
	
	private static Random random = new Random();
	
	private static void tracePath(World world, Token t) {
		Tile tile = world.map[t.x][t.z][t.y];
		float dy = tile.type==TileType.ramp ? 0.7f : 0.2f;
		random.setSeed(World.seedXZY(world.seed+1458393, t.x, t.z, t.y));
		Particle p = new ParticleLoop(1f, random.nextFloat());
		p.position.set(t.x*2, t.y+dy, t.z*2);
		ParticleRenderer.trace.add(p);
		Direction d = tile.pathDir[world.pathfinder.activePathLayer];
		if(d!=null) {
			Token dt = t.move(d.opposite());
			if(dt!=null)
				tracePath(world, dt);
		}
	}

	private static void tracePath(World world, int x, int z, int y) {
		tracePath(world, world.pathfinder.startTrace(x, z, y));
	}

	public static void update(World world) {
		if(Ruins.preview || !show)
			return;
		
		ParticleRenderer.trace.clear();

		int min = PathFinder.maxPathDist;
		TileObject nearest = null;
		if(world.obelisks.portal.active) {
			nearest = world.obelisks.portal;
		}
		else {
			for(Obelisk obj : world.obelisks.obelisks) {
				int d = world.map[obj.x][obj.z][obj.y].pathDist[world.pathfinder.activePathLayer];
				if(!obj.visited && d<min) {
					nearest = obj;
					min = d;
				}
			}
		}
		if(nearest!=null)
			tracePath(world, nearest.x, nearest.z, nearest.y);
	}
	
}
