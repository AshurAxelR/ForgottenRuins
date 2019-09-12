package com.xrbpowered.ruins.world;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Save {

	public static final Save autosave = new Save(new File("ruins.save"));

	private static final int formatCode = 47862;
	private static final int saveVersion = 1;

	public final File file;

	public Save(File file) {
		this.file = file;
	}
	
	public boolean exists() {
		return file.exists();
	}

	public World load() {
		World world;
		try(ZipInputStream zip = new ZipInputStream(new FileInputStream(file))) {
			zip.getNextEntry();
			DataInputStream in = new DataInputStream(zip);
			
			if(in.readInt()!=formatCode)
				throw new RuntimeException("Not a save file");
			
			int version = in.readInt();
			if(version!=saveVersion)
				throw new RuntimeException("Save version is different");
			
			DifficultyMode difficulty = DifficultyMode.values()[in.readByte()];
			long seed = in.readLong();
			int level = in.readInt();
			world = World.createWorld(difficulty, seed, level);
			world.loadState(in);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}

		System.out.println("World loaded");
		return world;
	}

	public boolean save(World world) {
		if(world==null)
			return false;
		
		try(ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(file))) {
			zip.putNextEntry(new ZipEntry("savedata"));
			DataOutputStream out = new DataOutputStream(zip);
			
			out.writeInt(formatCode);
			out.writeInt(saveVersion);

			out.writeByte(world.difficulty.ordinal());
			out.writeLong(world.seed);
			out.writeInt(world.level);
			world.saveState(out);
			
			zip.closeEntry();
			
			System.out.println("World saved");
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
