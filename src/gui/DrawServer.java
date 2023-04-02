package gui;

import java.awt.Graphics;
import java.util.ArrayList;

public class DrawServer {
	private core.Server thisServer;
	
	public DrawServer(core.Server t) {
		this.thisServer = t;
	}
	
	public void draw(Graphics g, int scrollX, int scrollY) {
		int frame = this.thisServer.getZ() * 128;
		
		if(this.thisServer.getWait() > 0) frame = this.thisServer.getWait() % 15 < 8 ? 512 : 576;
		else if(this.thisServer.getZ() < 2) frame += this.thisServer.getY() % 32 < 16 ? 0 : 64;
		else frame += this.thisServer.getX() % 32 < 16 ? 0 : 64;
	
		g.drawImage(this.thisServer.getImage(), this.thisServer.getX() - scrollX, this.thisServer.getY() - scrollY, this.thisServer.getX() - scrollX + 64, this.thisServer.getY() - scrollY + 64, frame, 0, frame + 64, 64, null);

		if(this.thisServer.getClient() != -1 && this.thisServer.getWait() == 0) g.drawImage(this.thisServer.getFood(), this.thisServer.getX() - scrollX + 16, this.thisServer.getY() - scrollY - 32, null);
	}
}
// Si le client n'est pas entrain de manger lorsque le serveur va vers le client on affiche une petite image de burger.