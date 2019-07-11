package com.xrbpowered.ruins.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.xrbpowered.gl.res.mesh.FastMeshBuilder;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.render.prefab.PrefabComponent;
import com.xrbpowered.ruins.render.prefab.PrefabComponent.InstanceInfo;
import com.xrbpowered.ruins.render.prefab.Prefabs;
import com.xrbpowered.ruins.render.shader.WallShader;
import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.PathFinder.Token;
import com.xrbpowered.ruins.world.Tile;
import com.xrbpowered.ruins.world.TileType;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.obj.Obelisk;

public class DebugPaths {

	public static boolean show = false;
	
	private static PrefabComponent dot;

	private static void tracePath(World world, Token t) {
		Tile tile = world.map[t.x][t.z][t.y];
		float dy = tile.type==TileType.ramp ? 0.7f : 0.2f;
		dot.addInstance(new InstanceInfo(t.x*2, t.z*2, t.y+dy, 1f));
		Direction d = tile.pathDir;
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
		
		if(dot==null)
			dot = new PrefabComponent(FastMeshBuilder.cube(0.2f, WallShader.vertexInfo, null), new Texture(Color.RED));
		dot.releaseInstances();
		dot.startCreateInstances();
		
		for(Obelisk obj : world.obelisks.obelisks)
			if(!obj.visited)
				tracePath(world, obj.x, obj.z, obj.y);
		
		dot.finishCreateInstances();
	}
	
	public static void draw() {
		if(dot!=null && show) {
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			Prefabs.drawInstances(dot);
		}
	}

}
