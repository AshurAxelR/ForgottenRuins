package com.xrbpowered.ruins.render;

import org.joml.Vector3f;

import com.xrbpowered.gl.res.mesh.AdvancedMeshBuilder;
import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.ruins.render.shader.WallShader;
import com.xrbpowered.ruins.render.texture.TextureAtlas;
import com.xrbpowered.ruins.world.Direction;
import com.xrbpowered.ruins.world.Tile;
import com.xrbpowered.ruins.world.TileType;
import com.xrbpowered.ruins.world.World;

public class WallBuilder extends AdvancedMeshBuilder {
	
	public final World world;
	public final TextureAtlas atlas;
	
	public final int cx, cz;
	
	private WallBuilder(World map, TextureAtlas atlas, int cx, int cz) {
		super(WallShader.vertexInfo, null);
		this.world = map;
		this.atlas = atlas;
		this.cx = cx;
		this.cz = cz;
		setCustomAttrib("in_Light", 0);
	}

	private Face newTriangle(Vector3f norm, float light) {
		Face face = new Triangle(addVertex(), addVertex(), addVertex());
		face.vertices[0].setNormal(norm).setCustom(light);
		face.vertices[1].setNormal(norm).setCustom(light);
		face.vertices[2].setNormal(norm).setCustom(light);
		add(face);
		return face;
	}
	
	private Face newQuad(Vector3f norm, float light) {
		Face face = new Quad(addVertex(), addVertex(), addVertex(), addVertex());
		face.vertices[0].setNormal(norm).setCustom(light);
		face.vertices[1].setNormal(norm).setCustom(light);
		face.vertices[2].setNormal(norm).setCustom(light);
		face.vertices[3].setNormal(norm).setCustom(light);
		add(face);
		return face;
	}
	
	protected void addTop(int x, int z, int y, TextureAtlas.Sprite t, float light) {
		x *= 2;
		z *= 2;
		Vector3f norm = new Vector3f(0, 1, 0);
		Face face = newQuad(norm, light);
		face.vertices[0].setPosition(x-1, y+1, z-1).setTexCoord(t.uv(0, 0));
		face.vertices[1].setPosition(x-1, y+1, z+1).setTexCoord(t.uv(0, 1));
		face.vertices[2].setPosition(x+1, y+1, z+1).setTexCoord(t.uv(1, 1));
		face.vertices[3].setPosition(x+1, y+1, z-1).setTexCoord(t.uv(1, 0));
	}

	protected void addTop(int x, int z, int y, float light) {
		long s = 0;
		addTop(x, z, y, atlas.top.get(s), light);
	}

	protected void addBottom(int x, int z, int y) {
		x *= 2;
		z *= 2;
		long s = 0;
		Vector3f norm = new Vector3f(0, -1, 0);
		Face face = newQuad(norm, 0f);
		face.vertices[0].setPosition(x-1, y, z+1).setTexCoord(atlas.bottom(s, 0, 0));
		face.vertices[1].setPosition(x-1, y, z-1).setTexCoord(atlas.bottom(s, 0, 1));
		face.vertices[2].setPosition(x+1, y, z-1).setTexCoord(atlas.bottom(s, 1, 1));
		face.vertices[3].setPosition(x+1, y, z+1).setTexCoord(atlas.bottom(s, 1, 0));
	}

	protected void addSide(int x, int z, int y, Direction d, float light) {
		x *= 2;
		z *= 2;
		long s = 0;
		int h = y%2;
		Vector3f norm = new Vector3f(d.dx, 0, d.dz);
		Face face = newQuad(norm, light);
		switch(d) {
			case east: {
				face.vertices[0].setPosition(x+1, y, z-1).setTexCoord(atlas.side(s, h, 1, 1));
				face.vertices[1].setPosition(x+1, y+1, z-1).setTexCoord(atlas.side(s, h, 1, 0));
				face.vertices[2].setPosition(x+1, y+1, z+1).setTexCoord(atlas.side(s, h, 0, 0));
				face.vertices[3].setPosition(x+1, y, z+1).setTexCoord(atlas.side(s, h, 0, 1));
				break;
			}
			case west: {
				face.vertices[0].setPosition(x-1, y, z+1).setTexCoord(atlas.side(s, h, 1, 1));
				face.vertices[1].setPosition(x-1, y+1, z+1).setTexCoord(atlas.side(s, h, 1, 0));
				face.vertices[2].setPosition(x-1, y+1, z-1).setTexCoord(atlas.side(s, h, 0, 0));
				face.vertices[3].setPosition(x-1, y, z-1).setTexCoord(atlas.side(s, h, 0, 1));
				break;
			}
			case south: {
				face.vertices[0].setPosition(x+1, y, z+1).setTexCoord(atlas.side(s, h, 1, 1));
				face.vertices[1].setPosition(x+1, y+1, z+1).setTexCoord(atlas.side(s, h, 1, 0));
				face.vertices[2].setPosition(x-1, y+1, z+1).setTexCoord(atlas.side(s, h, 0, 0));
				face.vertices[3].setPosition(x-1, y, z+1).setTexCoord(atlas.side(s, h, 0, 1));
				break;
			}
			case north: {
				face.vertices[0].setPosition(x-1, y, z-1).setTexCoord(atlas.side(s, h, 1, 1));
				face.vertices[1].setPosition(x-1, y+1, z-1).setTexCoord(atlas.side(s, h, 1, 0));
				face.vertices[2].setPosition(x+1, y+1, z-1).setTexCoord(atlas.side(s, h, 0, 0));
				face.vertices[3].setPosition(x+1, y, z-1).setTexCoord(atlas.side(s, h, 0, 1));
				break;
			}
		}
	}
	
	protected void addRampTop(int x, int z, int y, Direction rampd, float light) {
		x *= 2;
		z *= 2;
		long s = 0;
		Vector3f norm = new Vector3f(-rampd.dx, 2, -rampd.dz).normalize();
		Face face = newQuad(norm, light);
		switch(rampd) {
			case west: {
				face.vertices[0].setPosition(x+1, y, z-1).setTexCoord(atlas.rampTop(s, 1, 1));
				face.vertices[1].setPosition(x-1, y+1, z-1).setTexCoord(atlas.rampTop(s, 1, 0));
				face.vertices[2].setPosition(x-1, y+1, z+1).setTexCoord(atlas.rampTop(s, 0, 0));
				face.vertices[3].setPosition(x+1, y, z+1).setTexCoord(atlas.rampTop(s, 0, 1));
				break;
			}
			case east: {
				face.vertices[0].setPosition(x-1, y, z+1).setTexCoord(atlas.rampTop(s, 1, 1));
				face.vertices[1].setPosition(x+1, y+1, z+1).setTexCoord(atlas.rampTop(s, 1, 0));
				face.vertices[2].setPosition(x+1, y+1, z-1).setTexCoord(atlas.rampTop(s, 0, 0));
				face.vertices[3].setPosition(x-1, y, z-1).setTexCoord(atlas.rampTop(s, 0, 1));
				break;
			}
			case north: {
				face.vertices[0].setPosition(x+1, y, z+1).setTexCoord(atlas.rampTop(s, 1, 1));
				face.vertices[1].setPosition(x+1, y+1, z-1).setTexCoord(atlas.rampTop(s, 1, 0));
				face.vertices[2].setPosition(x-1, y+1, z-1).setTexCoord(atlas.rampTop(s, 0, 0));
				face.vertices[3].setPosition(x-1, y, z+1).setTexCoord(atlas.rampTop(s, 0, 1));
				break;
			}
			case south: {
				face.vertices[0].setPosition(x-1, y, z-1).setTexCoord(atlas.rampTop(s, 1, 1));
				face.vertices[1].setPosition(x-1, y+1, z+1).setTexCoord(atlas.rampTop(s, 1, 0));
				face.vertices[2].setPosition(x+1, y+1, z+1).setTexCoord(atlas.rampTop(s, 0, 0));
				face.vertices[3].setPosition(x+1, y, z-1).setTexCoord(atlas.rampTop(s, 0, 1));
				break;
			}
		}
	}
	
	protected void addRampSide(int x, int z, int y, Direction d, Direction rampd, float light) {
		if(d==rampd || d.opposite()==rampd)
			return;
		x *= 2;
		z *= 2;
		long s = 0;
		int h = y%2;
		Vector3f norm = new Vector3f(d.dx, 0, d.dz);
		Face face = newTriangle(norm, light);
		switch(d) {
			case east: {
				if(rampd==Direction.north) {
					face.vertices[0].setPosition(x+1, y, z-1).setTexCoord(atlas.side(s, h, 1, 1));
					face.vertices[1].setPosition(x+1, y+1, z-1).setTexCoord(atlas.side(s, h, 1, 0));
					face.vertices[2].setPosition(x+1, y, z+1).setTexCoord(atlas.side(s, h, 0, 1));
				}
				else {
					face.vertices[0].setPosition(x+1, y, z-1).setTexCoord(atlas.side(s, h, 1, 1));
					face.vertices[1].setPosition(x+1, y+1, z+1).setTexCoord(atlas.side(s, h, 0, 0));
					face.vertices[2].setPosition(x+1, y, z+1).setTexCoord(atlas.side(s, h, 0, 1));
				}
				break;
			}
			case west: {
				if(rampd==Direction.north) {
					face.vertices[0].setPosition(x-1, y, z+1).setTexCoord(atlas.side(s, h, 1, 1));
					face.vertices[1].setPosition(x-1, y+1, z-1).setTexCoord(atlas.side(s, h, 0, 0));
					face.vertices[2].setPosition(x-1, y, z-1).setTexCoord(atlas.side(s, h, 0, 1));
				}
				else {
					face.vertices[0].setPosition(x-1, y, z+1).setTexCoord(atlas.side(s, h, 1, 1));
					face.vertices[1].setPosition(x-1, y+1, z+1).setTexCoord(atlas.side(s, h, 1, 0));
					face.vertices[2].setPosition(x-1, y, z-1).setTexCoord(atlas.side(s, h, 0, 1));
				}
				break;
			}
			case south: {
				if(rampd==Direction.east) {
					face.vertices[0].setPosition(x+1, y, z+1).setTexCoord(atlas.side(s, h, 1, 1));
					face.vertices[1].setPosition(x+1, y+1, z+1).setTexCoord(atlas.side(s, h, 1, 0));
					face.vertices[2].setPosition(x-1, y, z+1).setTexCoord(atlas.side(s, h, 0, 1));
				}
				else {
					face.vertices[0].setPosition(x+1, y, z+1).setTexCoord(atlas.side(s, h, 1, 1));
					face.vertices[1].setPosition(x-1, y+1, z+1).setTexCoord(atlas.side(s, h, 0, 0));
					face.vertices[2].setPosition(x-1, y, z+1).setTexCoord(atlas.side(s, h, 0, 1));
				}
				break;
			}
			case north: {
				if(rampd==Direction.east) {
					face.vertices[0].setPosition(x-1, y, z-1).setTexCoord(atlas.side(s, h, 1, 1));
					face.vertices[1].setPosition(x+1, y+1, z-1).setTexCoord(atlas.side(s, h, 0, 0));
					face.vertices[2].setPosition(x+1, y, z-1).setTexCoord(atlas.side(s, h, 0, 1));
				}
				else {
					face.vertices[0].setPosition(x-1, y, z-1).setTexCoord(atlas.side(s, h, 1, 1));
					face.vertices[1].setPosition(x-1, y+1, z-1).setTexCoord(atlas.side(s, h, 1, 0));
					face.vertices[2].setPosition(x+1, y, z-1).setTexCoord(atlas.side(s, h, 0, 1));
				}
				break;
			}
		}
	}
	
	@Override
	public StaticMesh create() {
		int xmin = cx*chunkSize;
		int zmin = cz*chunkSize;
		int xmax = xmin + chunkSize;
		int zmax = zmin + chunkSize;
		for(int y=0; y<World.height-1; y++)
			for(int x=xmin; x<xmax; x++)
				for(int z=zmin; z<zmax; z++) {
					switch(world.map[x][z][y].type) {
						case solid: {
							if(world.map[x][z][y+1].type==TileType.empty) {
								if(world.map[x][z][y+1].tileObject!=null)
									addTop(x, z, y, atlas.start, world.map[x][z][y+1].light);
								else
									addTop(x, z, y, world.map[x][z][y+1].light);
							}
							if(y>0 && world.map[x][z][y-1].type==TileType.empty)
								addBottom(x, z, y);
							for(Direction d : Direction.values()) {
								Tile cell = world.map[x+d.dx][z+d.dz][y]; 
								if(cell.type!=TileType.solid && !(cell.type==TileType.ramp && cell.dir==d.opposite()))
									addSide(x, z, y, d, cell.light);
							}
							break;
						}
						case ramp: {
							Direction rampd = world.map[x][z][y].dir;
							addRampTop(x, z, y, rampd, world.map[x][z][y].light);
							for(Direction d : Direction.values()) {
								Tile cell = world.map[x+d.dx][z+d.dz][y]; 
								if(cell.type==TileType.empty || (cell.type==TileType.ramp && cell.dir!=d))
									addRampSide(x, z, y, d, rampd, cell.light);
							}
							break;
						}
						default:
					}
				}
		return super.create();
	}
	
	public static final int chunkSize = 16;
	
	public static StaticMesh[] createChunks(World world, TextureAtlas atlas) {
		int s = World.size / chunkSize;
		StaticMesh[] meshes = new StaticMesh[s*s];
		for(int cx=0; cx<s; cx++)
			for(int cz=0; cz<s; cz++) {
				meshes[cx*s+cz] = new WallBuilder(world, atlas, cx, cz).create();
			}
		return meshes;
	}
	
	public static StaticMesh createGround(float viewDist) {
		viewDist *= 0.5f;
		int s = World.size / chunkSize;

		float[] vdata = new float[(s+3)*(s+3)*9];
		int offs = 0;
		for(int cx=-1; cx<=s+1; cx++)
			for(int cz=-1; cz<=s+1; cz++) {
				float x = (cx<0) ? -viewDist : (cx>s) ? (cx-1) * chunkSize + viewDist : cx * chunkSize;
				float z = (cz<0) ? -viewDist : (cz>s) ? (cz-1) * chunkSize + viewDist : cz * chunkSize;
				vdata[offs+0] = x * 2f;
				vdata[offs+1] = 0;
				vdata[offs+2] = z * 2f;
				vdata[offs+3] = 0;
				vdata[offs+4] = 1;
				vdata[offs+5] = 0;
				vdata[offs+6] = x;
				vdata[offs+7] = z;
				vdata[offs+8] = 1;
				offs += 9;
			}

		short[] idata = new short[(s+2)*(s+2)*6];
		offs = 0;
		for(int cx=0; cx<s+2; cx++)
			for(int cz=0; cz<s+2; cz++) {
				idata[offs+0] = (short)((cx+0) * (s+3) + (cz+0));
				idata[offs+1] = (short)((cx+0) * (s+3) + (cz+1));
				idata[offs+2] = (short)((cx+1) * (s+3) + (cz+1));
				idata[offs+3] = (short)((cx+0) * (s+3) + (cz+0));
				idata[offs+4] = (short)((cx+1) * (s+3) + (cz+1));
				idata[offs+5] = (short)((cx+1) * (s+3) + (cz+0));
				offs += 6;
			}
		
		return new StaticMesh(WallShader.vertexInfo, vdata, idata);
	}
	
}
