package gui;

import java.awt.Graphics;

public class DrawClient {

	private core.Client logicClient;
	
	public DrawClient(core.Client c) {
		this.logicClient = c;
	}
	//dessine les chefs et les fait disparaite
	public void draw(Graphics g, int scrollX, int scrollY) {
		int frame = this.logicClient.getZ() * 128;
		
		if(this.logicClient.getZ() < 2) frame += this.logicClient.getY() % 32 < 16 ? 0 : 64;
		else frame += this.logicClient.getX() % 32 < 16 ? 0 : 64;
		
		if(this.logicClient.getState() != 6) g.drawImage(this.logicClient.getImage(), this.logicClient.getX() - scrollX, this.logicClient.getY() - scrollY, this.logicClient.getX() - scrollX + 64, this.logicClient.getY() - scrollY + 64, frame, 0, frame + 64, 64, null);
		
		if(this.logicClient.getState() == 2 || this.logicClient.getState() == 3) g.drawImage(this.logicClient.getFImage(), this.logicClient.getX() - scrollX + 16, this.logicClient.getY() - scrollY - 32, null);
	}
}
