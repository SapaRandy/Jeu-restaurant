package core;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Chef {

	private int level;
	private int x;
	private int y;
	private int z;
	private int state;
	private int four;
	private int wait; // on initialise les différente variable
	
	private Image sprite;
	
	public Chef(int level, int x, int y) {
		this.level = level;
		this.x = x;
		this.y = y;
		this.z = 0;
		this.state = 0;
		this.four = -1;
		this.wait = 0;

		try { // on charge l'image en mémoire
			this.sprite = ImageIO.read(new File("/Users/Rayan/Desktop/JRestaurant/res/Chef_" + String.valueOf(level) + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void play(ArrayList<Integer> tiles, int width, int height, int foodLimit) {  // commande qui se lis de bas en haut
		int speed = this.level == 1 ? 2 : this.level == 2 ? 4 : 8;

		if(this.state == 2) { // 3eme et derniere etape le chef "cuisine", une fois arrive au four, selon le niveau du chef la cuisine sera plus au moins longue ensuite , l'etat repasse a 0 ( reprise de la fonction en partant de bas)et le chef laisse le four ou il etait avec une image de burger. 
			if(this.wait == 180) {
				this.state = 0;
				this.four = -1;
				this.wait = -1;

				if(foodLimit > 0) {
					tiles.set((int)(this.x / 64) * height + (int)(this.y / 64), 13);
					foodLimit --;
				}
			}

			this.wait ++;
		} else if(this.state == 1) {  // 2eme etape: le chef va vers le four qu'il a trouvé.
			if(tiles.get(this.four) != 11) {
				this.state = 0;
				this.four = -1;
			} else {
			
				int fourX = this.four / height;
			
				if(this.x < fourX * 64) {
					this.x += speed;
					this.z = 2;
				} else if(this.x > fourX * 64) {
					this.x -= speed;
					this.z = 3;
				}
	
				int fourY = this.four % height;
	
				if(this.y < fourY * 64) {
					this.y += speed;
					this.z = 0;
				} else if(this.y > fourY * 64) {
					this.y -= speed;
					this.z = 1;
				}
	
				if(this.x == fourX * 64 && this.y == fourY * 64) {
					tiles.set(this.four, 12);
	
					this.z = 1;
					this.state = 2;
				}
			}
		} else if(this.state == 0) {
			ArrayList<Integer> fours = new ArrayList<Integer>(); // 1ere étape : ici le chef cherche un four c'est la tile numéro 11 . Le chef le cherche de maniere aléatoire.
	
			for(int i = 0; i < tiles.size(); i ++) if(tiles.get(i) == 11) fours.add(i);
			
			if(fours.size() > 0) {
				this.four = fours.get((int)(Math.random() * fours.size()));
				this.state = 1;
			}
			
			if(this.state == 0 && this.y > 0) {
				this.y -= speed;
				this.z = 1;
			}
		}
	}
	
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
	
	public Image getSprite() {
		return this.sprite;
	}
}
