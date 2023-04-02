package core;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Client {

	private int level; // on initialise les variable
	private int x;
	private int y;
	private int z;
	private int state;
	private int table;
	private int time;
	
	private Image sprite;
	private Image wait;
	
	public Client(int level, int x, int y) {
		this.level = level;
		this.x = x;
		this.y = y;
		this.z = 0;
		this.state = 0;
		this.table = -1;
		this.time = 0;

		try { // on charge l'image en mémoire
			this.sprite = ImageIO.read(new File("/Users/Rayan/Desktop/JRestaurant/res/Client_" + String.valueOf(level) + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Paragraphe sur comment speed est gére: pour toute les classes
	//Speed c'est le coefficient de vitesse selon le niveau
	// niveau 1 = 2 pixel par frame 2= 4,  3=8
	// Le programme s'actualise a 30 frame par seconde donc pour le niveau 3 exemple : 30x8=240 pixel par seconde 
	// 1 tile = 64 pixel donc 240 pixel = environ 3 a 4 tile par seconde. Pour le niveau 3.
	
	
	public void play(ArrayList<Integer> tiles, int width, int height) { // Comme pour client la fonction se lis de bas en haut
		int speed = this.level == 1 ? 2 : this.level == 2 ? 4 : 8;
		// les etats  2,3,4,5 sont gerer par le serveur
		if(this.state == 5 && this.y < height * 64) { // une fois qu'il est sur la table
			this.y += speed;
			this.z = 0;

			if(this.y == height * 64) this.state = 6;
		} else if(this.state == 2 || this.state == 3) {
			if(this.time == 800) {
				this.state = 5; // l'etat numéro 5 correspond a une fois qu'il a mangé ou qu'il n'est pas servi a temps il repart de la ou il est venu càd vers le bas.

				tiles.set((int)(this.x / 64) * height + (int)(this.y / 64), 9); // On libere la table
			} else if(this.time % 60 == 0) {
				try {
					if(this.time == 0) this.wait = ImageIO.read(new File("/Users/Rayan/Desktop/JRestaurant/res/Wait_1.png"));  // selon le temps qu'il attend on montre son temps d'attente selon une image de differente couleur
					else if(this.time == 400) this.wait = ImageIO.read(new File("/Users/Rayan/Desktop/JRestaurant/res/Wait_2.png"));
					else if(this.time == 800) this.wait = ImageIO.read(new File("/Users/Rayan/Desktop/JRestaurant/res/Wait_3.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			this.time ++;
		} else if(this.state == 1) {
			if(tiles.get(this.table) != 9) {
				this.state = 0;
				this.table = -1;
			} else { // une fois cette table trouvé le client se dirige vers elle avec plus ou moins de vitesse selon le niveau du client.
				int tableX = this.table / height;
					
				if(this.x < tableX * 64) {
					this.x += speed;
					this.z = 2;
				} else if(this.x > tableX * 64) {
					this.x -= speed;
					this.z = 3;
				}
	
				int tableY = this.table % height;
	
				if(this.y < tableY * 64) {
					this.y += speed;
					this.z = 0;
				} else if(this.y > tableY * 64) {
					this.y -= speed;
					this.z = 1;
				}
	
				if(this.x == tableX * 64 && this.y == tableY * 64) {
					tiles.set(this.table, 10);

					this.z = 1;
					this.state = 2;
				}
			}
		} else if(this.state == 0) { // lorsque le client arrive il cherche une table qui est disponible qui est la tile numéro 9 de maniere aléatoire.
			ArrayList<Integer> tables = new ArrayList<Integer>();

			for(int i = 0; i < tiles.size(); i ++) if(tiles.get(i) == 9) tables.add(i);
			
			if(tables.size() > 0) {
				this.table = tables.get((int)(Math.random() * tables.size()));
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
