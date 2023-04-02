package core;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Server {

	private int level;
	private int x;
	private int y;
	private int z;
	private int four;
	private int client;
	private int wait;  // on initialise les variable
	
	private Image sprite;
	private Image food;
	
	public Server(int level, int x, int y) {
		this.level = level;
		this.x = x;
		this.y = y;
		this.z = 0;
		this.four = -1;
		this.client = -1;
		this.wait = 0;

		try { // On charge les differentes images en mémoire
			this.sprite = ImageIO.read(new File("/Users/Rayan/Desktop/JRestaurant/res/Server_" + String.valueOf(level) + ".png"));
			this.food = ImageIO.read(new File("/Users/Rayan/Desktop/JRestaurant/res/Food.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int play(ArrayList<Integer> tiles, int width, int height, ArrayList<Client> clients) {  // la fonction se lit de bas en haut
		int speed = this.level == 1 ? 2 : this.level == 2 ? 4 : 8;
		
		if(this.wait > 0) {
			this.wait ++;
			
			if(this.wait == (this.level == 1 ? 270 : this.level == 2 ? 90 : 30)) { // le serveur livre le client , selon le niveau du serveur le client mange plus ou moins rapidement et livre plus ou moins rapidement.
				this.wait = 0;
				Client client = clients.get(this.client);
				tiles.set((int)(client.getX() / 64) * height + (int)(client.getY() / 64), 9);
				client.setState(5); // Une fois livrés on place l'etat du client a 5 (cf. Client)
				this.client = -1;
				
				int level = client.getLevel();
				
				return level == 1 ? 100 : level == 2 ? 200 : 600; // selon le niveau du client on recupère l'argent de la commande
			}
		} else if(this.client != -1) {
			Client client = clients.get(this.client);

			if(client.getState() != 3) { // le serveur va vers le client
				this.four = -1;
				this.client = -1;
			} else {
				int clientX = client.getX();
				
				if(this.x < clientX) {
					this.x += speed;
					this.z = 2;
				} else if(this.x > clientX) {
					this.x -= speed;
					this.z = 3;
				}

				int clientY = client.getY() - 32;
				
				if(this.y < clientY) {
					this.y += speed;
					this.z = 0;
				} else if(this.y > clientY) {
					this.y -= speed;
					this.z = 1;
				}
				
				if(this.x == clientX && this.y == clientY) {
					this.wait = 1;
					client.setState(4);
				}
			}
		} else if(this.four != -1) { // le serveur va vers le four
			if(tiles.get(this.four) != 13) this.four = -1;
			else {
				int fourX = (int)(this.four / height) * 64;
				
				if(this.x < fourX) {
					this.x += speed;
					this.z = 2;
				} else if(this.x > fourX) {
					this.x -= speed;
					this.z = 3;
				}

				int fourY = this.four % height * 64;
				
				if(this.y < fourY) {
					this.y += speed;
					this.z = 0;
				} else if(this.y > fourY) {
					this.y -= speed;
					this.z = 1;
				}

				if(this.x == fourX && this.y == fourY) {
					tiles.set(this.four, 11);
					this.four = -1;
					
					for(int i = 0; i < clients.size(); i ++) {
						Client client = clients.get(i);
							
						if(client.getState() == 2 && this.client == -1) {
							this.client = i; 
							client.setState(3);
						}
					}
				}
			}	
		} else {
			int count = 0;  // cette partie permet de nous dire si des clients sont dans le restaurant et que le chef a prépare un plat (tiles 13).
			// On incremente a chaque fois qu'un client attend 
			for(int i = 0; i < clients.size(); i ++) if(clients.get(i).getState() == 2) count ++;
		
			if(count > 0) {
				ArrayList<Integer> fours = new ArrayList<Integer>();
	
				for(int i = 0; i < tiles.size(); i ++) if(tiles.get(i) == 13) fours.add(i);
				
				if(fours.size() > 0) this.four = fours.get((int)(Math.random() * fours.size()));
			}

			if(this.four == -1 && this.y > 0) {
				this.y -= speed;
				this.z = 1;  // si ya pas de four le serveur remonte
				
				if(this.y == 0) this.z = 0;
			}
		}

		return 0;
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
