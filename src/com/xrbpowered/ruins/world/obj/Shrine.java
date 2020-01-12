package com.xrbpowered.ruins.world.obj;

import java.util.Random;

import com.xrbpowered.ruins.entity.player.buff.Buff;
import com.xrbpowered.ruins.render.prefab.Prefab;
import com.xrbpowered.ruins.render.prefab.PrefabRenderer;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.gen.WorldGenerator.Token;

public class Shrine extends TileObject {

	public final Buff buff;
	
	private static final Random random = new Random();


	public Shrine(World world, Token objToken) {
		super(world, objToken);
		random.setSeed(seed);
		buff = Buff.getRandom(random);
	}

	@Override
	public Prefab getPrefab() {
		return PrefabRenderer.shrine;
	}

	@Override
	public String getPickName() {
		return "Shrine";
	}
	
	@Override
	public String getActionString() {
		return "[Right-click to interact]";
	}
	
	@Override
	public void interact() {
		world.player.buffs.add(buff); // TODO shrine UI
	}
}
