package data;

import java.awt.Image;

public class Dataclient {
	private int level;
	private int x;
	private int y;
	private int z;
	private int state;
	private int time;
	private Image sprite;
	private Image wait;

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
	
	public int getState() {
		return this.state;
	}

	public int getWait() {
		return this.time;
	}

	public Image getImage() {
		return this.sprite;
	}
	
	public Image getFImage() {
		return this.wait;
	}
	
	public void setState(int state) {
		if(state >= 0 && state <= 6) this.state = state;
	}

}
