package com.xrbpowered.ruins.world;

public class PathFinderThread extends Thread {

	public PathFinder paths = null;
	
	public boolean requestUpdate = false;
	public boolean requestRefresh = false;
	public int mapx, mapy, mapz;

	public PathFinderThread() {
		start();
	}
	
	public void setWorld(World world) {
		this.paths = world.pathfinder;
	}
	
	public void update() {
		if(paths.canUpdate(mapx, mapz, mapy)) {
			paths.clear();
			paths.update(mapx, mapz, mapy, PathFinder.maxPathDist);
			paths.flip();
			requestRefresh = true;
		}
	}

	@Override
	public void run() {
		try {
			for(;;) {
				if(requestUpdate && paths!=null) {
					requestUpdate = false;
					update();
				}
				else {
					Thread.sleep(5);
				}
			}
		}
		catch(InterruptedException e) {
		}
	}

}
