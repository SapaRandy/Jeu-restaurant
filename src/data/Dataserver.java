package data;

import java.awt.Image;

public class Dataserver {
	private int level;
	private int x;
	private int y;
	private int z;
	private int wait;
	private Image sprite;
	private Image food;
	private int client;


	public int getLevel() {
		return this.level;
	}
	
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	public int getWait() {
		return this.wait;
	}
	
	public Image getImage() {
		return this.sprite;
	}

	public Image getFood() {
		return this.food;
	}

	
	public int getClient() {
		return this.client;
	}
}
