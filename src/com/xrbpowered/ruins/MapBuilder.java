package com.xrbpowered.ruins;

import org.joml.Vector3f;

import com.xrbpowered.gl.res.mesh.AdvancedMeshBuilder;
import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.ruins.WorldMap.Cell;
import com.xrbpowered.ruins.WorldMap.CellType;
import com.xrbpowered.ruins.WorldMap.Direction;

public class MapBuilder extends AdvancedMeshBuilder {
	
	public final WorldMap map;
	public final MapTextureAtlas atlas;
	
	public final int cx, cz;
	
	private MapBuilder(WorldMap map, MapTextureAtlas atlas, int cx, int cz) {
		super(MapShader.vertexInfo, null);
		this.map = map;
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
	
	protected void addTop(int x, int z, int y, float light) {
		x *= 2;
		z *= 2;
		long s = 0;
		Vector3f norm = new Vector3f(0, 1, 0);
		Face face = newQuad(norm, light);
		face.vertices[0].setPosition(x-1, y+1, z-1).setTexCoord(atlas.top(s, 0, 0));
		face.vertices[1].setPosition(x-1, y+1, z+1).setTexCoord(atlas.top(s, 0, 1));
		face.vertices[2].setPosition(x+1, y+1, z+1).setTexCoord(atlas.top(s, 1, 1));
		face.vertices[3].setPosition(x+1, y+1, z-1).setTexCoord(atlas.top(s, 1, 0));
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
		for(int y=0; y<WorldMap.height-1; y++)
			for(int x=xmin; x<xmax; x++)
				for(int z=zmin; z<zmax; z++) {
					switch(map.map[x][z][y].type) {
						case solid: {
							if(map.map[x][z][y+1].type==CellType.empty)
								addTop(x, z, y, map.map[x][z][y+1].light);
							if(y>0 && map.map[x][z][y-1].type==CellType.empty)
								addBottom(x, z, y);
							for(Direction d : Direction.values()) {
								Cell cell = map.map[x+d.dx][z+d.dz][y]; 
								if(cell.type!=CellType.solid && !(cell.type==CellType.ramp && cell.dir==d.opposite()))
									addSide(x, z, y, d, cell.light);
							}
							break;
						}
						case ramp: {
							Direction rampd = map.map[x][z][y].dir;
							addRampTop(x, z, y, rampd, map.map[x][z][y].light);
							for(Direction d : Direction.values()) {
								Cell cell = map.map[x+d.dx][z+d.dz][y]; 
								if(cell.type==CellType.empty || (cell.type==CellType.ramp && cell.dir!=d))
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
	
	public static StaticMesh[] createChunks(WorldMap map, MapTextureAtlas atlas) {
		int s = WorldMap.size / chunkSize;
		StaticMesh[] meshes = new StaticMesh[s*s];
		for(int cx=0; cx<s; cx++)
			for(int cz=0; cz<s; cz++) {
				meshes[cx*s+cz] = new MapBuilder(map, atlas, cx, cz).create();
			}
		return meshes;
	}
	
}
