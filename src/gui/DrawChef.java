package gui;

import java.awt.Graphics;

public class DrawChef {

	private core.Chef chef;
	
	public DrawChef(core.Chef c) {
		this.chef = c;
	}
	// dessine les chef 
	public void draw(Graphics g, int scrollX, int scrollY) {
		int frame = this.chef.getZ() * 128;
		
		if(this.chef.getWait() > 0) frame = this.chef.getWait() % 15 < 8 ? 512 : 576;
		else if(this.chef.getZ() < 2) frame += this.chef.getY() % 32 < 16 ? 0 : 64;
		else frame += this.chef.getX() % 32 < 16 ? 0 : 64;
	
		g.drawImage(this.chef.getSprite(), this.chef.getX() - scrollX, this.chef.getY() - scrollY, this.chef.getX() - scrollX + 64, this.chef.getY() - scrollY + 64, frame, 0, frame + 64, 64, null);
	}
	
}
