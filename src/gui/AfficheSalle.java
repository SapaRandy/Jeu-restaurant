package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class AfficheSalle {

	private ArrayList<Image> sprites;
	
	private ArrayList<Image> background;
	private ArrayList<Image> items;
	// 
	public AfficheSalle() {
		this.sprites = new ArrayList<Image>();
		this.background = new ArrayList<Image>();
		this.items = new ArrayList<Image>();
		
		try { // charge la partie graphique
			for(int i = 0; i < 6; i ++) this.background.add(ImageIO.read(new File("/Users/Rayan/Desktop/JRestaurant/res/Background_" + String.valueOf(i) + ".png")));

			for(int i = 0; i < 6; i ++) this.items.add(ImageIO.read(new File("/Users/Rayan/Desktop/JRestaurant/res/Item_" + String.valueOf(i) + ".png")));
			
			for(int i = 0; i < 20; i ++) this.sprites.add(ImageIO.read(new File("/Users/Rayan/Desktop/JRestaurant/res/Tile_" + String.valueOf(i) + ".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void drawTiles(Graphics g, int w, int h, int sX, int sY, ArrayList<Integer> tiles) {
		for(int x = 0; x < w; x ++) {
			for(int y = 0; y < h; y ++) { //dessine les tiles
				g.drawImage(this.sprites.get(tiles.get(x * h + y)), x * 64 - sX, y * 64 - sY, null);
			}
		}
	}
	
	public void drawMenu(Graphics g, int f, int e, int t, int iX, int iY, int h, ArrayList<core.Client> clients) {
		int a = 0;
		
		for(int i = 0; i < clients.size(); i ++) {
			if(clients.get(i).getState() < 6) a ++; 
		}
		
		if((h < 20 || a > 0) && h != 0) { //affiche le menu de la fin lorsque la journee es fini et qu'il reste 0 client
			if(f > 0) g.drawImage(this.background.get(Math.min(f, 2)), 0, 0, null);
			else if(e > 0) g.drawImage(this.background.get(Math.min(e + 2, 4)), 0, 0, null);
			else g.drawImage(this.background.get(0), 0, 0, null);
	
			if(f > 1 && t % 4 < 2) g.drawImage(this.items.get(f - 2), (int)(iX / 64) * 64, (int)(iY / 64) * 64, null);
		} else g.drawImage(this.background.get(5), 0, 0, null); 

	}
}
