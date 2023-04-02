package core;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Restaurant extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private int width;
	private int height;
	private int scrollX;
	private int scrollY;
	private int itemX;
	private int itemY;
	private int hours;
	private int minutes;  //on initialise les variable
	
	private int menu;
	private int employees;
	
	private int timer;
	private int coins;

	private ArrayList<Integer> tiles;
	
	private ArrayList<Server> servers;
	private ArrayList<Client> clients;
	private ArrayList<Chef> chefs;
	
	private gui.AfficheSalle guiResto;
	private gui.DrawServer guiServer;
	private gui.DrawClient guiClient;
	private gui.DrawChef guiChef;
	
	private JLabel screen;
	private int approvisionement;
	
	public Restaurant() {
		this.width = 10;
		this.height = 6;
		this.scrollX = 0;
		this.scrollY = 0;
		this.itemX = 0;
		this.itemY = 0;
		this.menu = 0;
		this.employees = 0;
		this.timer = 0;
		this.coins = 5000; // le nombre de crédit de base
		this.hours = 8; //on place l'heure du debut de la journée
		this.minutes = 0 * 30;
		this.approvisionement = 500; // la limite des plats dispo
		
		this.guiResto = new gui.AfficheSalle();
		
		this.tiles = new ArrayList<Integer>(); // 1 tile c'est un element du jeu
		// C'est un systeme d'ID pour recuperer les tiles dans lequel l'action intervient
		
		for(int x = 0; x < this.width; x ++) {  // on initialise la carte
			for(int y = 0; y < this.height; y ++) {
				if(x == 0 && y == 0) tiles.add(0);
				else if(x == this.width - 1 && y == 0) tiles.add(2);
				else if(y == 0) tiles.add(1);
				else if(x == 0 && y == this.height - 1) tiles.add(6);
				else if(x == this.width - 1 && y == this.height - 1) tiles.add(8);
				else if(y == this.height - 1) tiles.add(7);
				else if(x == 0) tiles.add(3);
				else if(x == this.width - 1) tiles.add(5);
				else tiles.add(4);
			}
		}

		for(int i = 0; i < 3; i ++) tiles.set(((int)(Math.random() * (this.width /2 - 1)) + this.width / 2 - 1) * this.height + (int)(Math.random() * (this.height - 2)) + this.height + 1, 9);
		// on place 3 TABLE de maniere aléatoire
		for(int i = 0; i < 1; i ++) tiles.set((int)(Math.random() * (this.width /2 - 1)) * this.height + (int)(Math.random() * (this.height - 2)) + this.height + 1, 11);
		// on place 1 four
		this.servers = new ArrayList<Server>();

		for(int i = 0; i < 1; i ++) this.servers.add(new Server(1 , i * 64, 0));
		// on met 1 serveur
		this.clients = new ArrayList<Client>();

		this.chefs = new ArrayList<Chef>();

		for(int i = 0; i < 1; i ++) this.chefs.add(new Chef(1, (width - i - 1) * 64, 0));
		// on met 1 chef
		this.screen = new JLabel("Coins : 0");
		this.screen.setFont(new Font(this.screen.getName(), Font.PLAIN, 24));
		this.add(this.screen);
		
		Key key = new Key();
		gui.Key gKey = new gui.Key();
		this.addKeyListener(key);
		this.addKeyListener(gKey);
		this.setFocusable(true);
		
		this.addMouseListener(new Mouse());
		this.addMouseListener(new gui.Mouse());
		
		Timer timer = new Timer(); // clignotement de l'item lorsqu'on le place
		timer.schedule(new Repaint(), 0, 1000 / 30); // fonction qui permet de rafraichir le programme 30 fois par seconde 
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		
		this.guiResto.drawTiles(g, this.width, this.height, this.scrollX, this.scrollY, this.tiles);
// si les menu sont ferme, boucle qui affiche draw  et la fonction play
		for(int i = 0; i < this.servers.size(); i ++) {
			if(this.hours != 0 && this.menu == 0 && this.employees == 0) this.coins += this.servers.get(i).play(this.tiles, this.width, this.height, this.clients);
			// dessine les serveur et recupere son argent
			this.guiServer = new gui.DrawServer(this.servers.get(i));
			
			this.guiServer.draw(g, this.scrollX, this.scrollY);
		}

		for(int i = 0; i < this.clients.size(); i ++) {
			if(this.hours != 0 && this.menu == 0 && this.employees == 0) this.clients.get(i).play(this.tiles, this.width, this.height);
			// dessine les clients
			this.guiClient = new gui.DrawClient(this.clients.get(i));
			this.guiClient.draw(g, this.scrollX, this.scrollY);
		}

		for(int i = 0; i < this.chefs.size(); i ++) {
			if(this.hours != 0 && this.menu == 0 && this.employees == 0) this.chefs.get(i).play(this.tiles, this.width, this.height, this.approvisionement);
			// dessine les chefs et fait correspondre 
			this.guiChef = new gui.DrawChef(this.chefs.get(i));
			this.guiChef.draw(g, this.scrollX, this.scrollY);
		}
		
		this.guiResto.drawMenu(g, this.menu, this.employees, this.timer, this.itemX, this.itemY, this.hours, this.clients);
		
		this.timer ++;

		int a = 0; 
	
		for(int i = 0; i < clients.size(); i ++) {
			if(this.clients.get(i).getState() != 6) a ++;  // verifie qu'il n'y a plus aucun client a la fin de journee 
		}
		
		if(this.menu == 0 && this.employees == 0 && this.hours != 0) { // une fois la journée termine on affiche le bilan de la journée 
			if((this.hours < 20 || a > 0) && hours !=0) {
				this.minutes ++;
				this.screen.setText("<html>Coins : $" + this.coins + "<br/><p style='text-align : center'>" + this.hours + "H" + (this.minutes / 30) + "</p></html>");  // on ecris en html pour cette partie la car pour ecrire sur la page de fin on a pensé que c'etait la meilleur facon de faire des retour a la ligne.
			} else {
				int total = 4000;  // on place le total a 4000 des charges 
				this.hours = 0;
				this.screen.setText("<html><br/><br/><br/><br/><br/> <br/>Eau => $1500<br/>Electricité => $1500 <br/> Provision = 1000$ ");
				String l = "<br/>Loyer : " + this.width + "m*" + this.height + "m*$10" + " => $" + (width * height * 10); // le loyer est payé selon la grandeur de la carte
				
				this.screen.setText(this.screen.getText() + l);
				total += (width * height * 10);
				
				int c1 = 0;
				int c2 = 0;
				int c3 = 0;
				
				for(int i = 0; i < chefs.size(); i ++) {
					if(chefs.get(i).getLevel() == 1) c1 ++;
					else if(chefs.get(i).getLevel() == 2) c2 ++;
					else if(chefs.get(i).getLevel() == 3) c3 ++;
				}
	
				this.screen.setText(this.screen.getText() + "<br/>Chef (Level 1) => " + c1 + " * $250 => $" + (c1 * 250));
				this.screen.setText(this.screen.getText() + "<br/>Chef (Level 2) => " + c2 + " * $500 => $" + (c2 * 500));
				this.screen.setText(this.screen.getText() + "<br/>Chef (Level 3) => " + c3 + " * $750 => $" + (c3 * 750));
				total += (c1 * 250);
				total += (c2 * 500);
				total += (c3 * 750);  // on comptabilise le nombre de chef avec les niveau, ensuite on rajoute au total le salaire selon le nombre de chef
				
				int s1 = 0;
				int s2 = 0;
				int s3 = 0;
				
				for(int i = 0; i < servers.size(); i ++) {
					if(servers.get(i).getLevel() == 1) s1 ++;
					else if(servers.get(i).getLevel() == 2) s2 ++;
					else if(servers.get(i).getLevel() == 3) s3 ++;
				}
				
				this.screen.setText(this.screen.getText() + "<br/>Serveur (Level 1) => " + s1 + " * $250 => $" + (s1 * 250));
				this.screen.setText(this.screen.getText() + "<br/>Serveur (Level 2) => " + s2 + " * $500 => $" + (s2 * 500));
				this.screen.setText(this.screen.getText() + "<br/>Serveur (Level 3) => " + s3 + " * $750 => $" + (s3 * 750));
	
				total += (s1 * 250);  // De meme pour les serveur
				total += (s2 * 500);
				total += (s3 * 750);
				this.coins -= total;
				
				this.screen.setText(this.screen.getText() + "<br/>*****<br/>Total : $" + total + "</html>");
				this.screen.setText(this.screen.getText() + "</html>");
			}
		}
		
		if(this.minutes / 30 == 60) { // affichage de l'heure
			this.minutes = 0;
			this.hours ++;
		}
	}
	
	public class Key implements KeyListener {
		
		public void keyTyped(KeyEvent key) {
			
		}
		
		public void keyPressed(KeyEvent key) {  // Permet de reconnaitre des touches au clavier
			if(key.getKeyCode() == KeyEvent.VK_RIGHT && scrollX <= width * 64 - 1088) scrollX += 64;
			else if(key.getKeyCode() == KeyEvent.VK_LEFT && scrollX >= 64) scrollX -= 64; // ScrollX et scrollY permet de deplacer la map avec les fleches directionnel
			else if(key.getKeyCode() == KeyEvent.VK_DOWN && scrollY <= height * 64 - 768) scrollY += 64;
			else if(key.getKeyCode() == KeyEvent.VK_UP && scrollY >= 64) scrollY -= 64;
		}
		
		public void keyReleased(KeyEvent key) {
			
		}
	}
	
	public class Mouse implements MouseListener {
		
		public void mouseEntered(MouseEvent mouse) {
			
		}
		
		public void mousePressed(MouseEvent mouse) {
			
		}
		
		public void mouseClicked(MouseEvent mouse) {  // Permet de gerer les menu
			if(menu == 0 && mouse.getX() >= 64 && mouse.getX() < 320 && mouse.getY() < 64) {
				menu = 1; // lorsque menu=1 cela correspond à quand on clique sur furnishing càd le menu d'amelioration de la salle
				employees = 0; // securite pour que seul le menu furnishing s'ouvre
			} else if(menu == 1 && employees == 0) {
				if(mouse.getX() >= 64 && mouse.getX() < 320 && mouse.getY() < 64) menu = 0;
				else if(mouse.getX() >= 192 && mouse.getX() < 256 && mouse.getY() >= 192 && mouse.getY() < 256) menu = 2;
				else if(mouse.getX() >= 576 && mouse.getX() < 640 && mouse.getY() >= 192 && mouse.getY() < 256) menu = 3;
				else if(mouse.getX() >= 192 && mouse.getX() < 256 && mouse.getY() >= 320 && mouse.getY() < 384) menu = 4;
				else if(mouse.getX() >= 576 && mouse.getX() < 640 && mouse.getY() >= 320 && mouse.getY() < 384) menu = 5;
				else if(mouse.getX() >= 192 && mouse.getX() < 256 && mouse.getY() >= 448 && mouse.getY() < 512) menu = 6;
				else if(mouse.getX() >= 576 && mouse.getX() < 640 && mouse.getY() >= 448 && mouse.getY() < 512) menu = 7;
				else if(mouse.getX() >= 128 && mouse.getX() < 320 && mouse.getY() >= 576 && mouse.getY() < 640 && coins >= 50000) {
					width ++;
					
					for(int i = 0; i < height; i ++) { //ajoute une colonne a la map
						tiles.set((width - 2) * height + i, i == 0 ? 1 : i == height - 1 ? 7 : 4);
						tiles.add(i == 0 ? 2 : i == height - 1 ? 8 : 5);
					}
					
					scrollX += 64;
					menu = 0;
					coins -= 50000; 
				} else if(mouse.getX() >= 512 && mouse.getX() < 704 && mouse.getY() >= 576 && mouse.getY() < 640 && coins >= 50000) {
					ArrayList<Integer> copy = new ArrayList<Integer>();
					// pour ajouter une ligne a la map , on creer une arraylist qui copie la map et a cette arraylist on ajoute une ligne. Par la suite c'est cette Arraylist qui deviendra la map.
					for(int i = 0; i < height * width; i ++) {
						copy.add(tiles.get(i));
						
						if(i % height == height - 1) copy.add(4);
					}
					
					height ++;
					tiles = copy;
					
					for(int i = 0; i < width; i ++) {  // permet de gerer les murs
						if(i == 0) {
							tiles.set(i * height + height - 2, 3);
							tiles.set(i * height + height - 1, 6);
						} else if(i == width - 1) {
							tiles.set(i * height + height - 2, 5);
							tiles.set(i * height + height - 1, 8);
						} else {
							tiles.set(i * height + height - 2, 4);
							tiles.set(i * height + height - 1, 7);
						}
					}
					
					scrollY += 64;
					menu = 0;
					coins -= 50000;
				}
			} else if(menu > 1) { // lorsque menu > 1 on selectionne un élement du menu
				int id = (int)(itemX / 64) * height + (int)(itemY / 64);
				int price = 0;

				if(menu == 2) price = 50;
				else if(menu == 3) price = 100;
				else if(menu == 4) price = 500;
				else if(menu == 5) price = 1500;
				else if(menu == 6) price = 5000;
				else if(menu == 7) price = 15000;

				if(mouse.getX() >= 64 && mouse.getX() < 320 && mouse.getY() < 64) menu = 0;
				else if(mouse.getX() >= 704 && mouse.getX() < 960 && mouse.getY() < 64 && tiles.get(id) == 4 && coins >= price) { // permet de verifier que l'espace est libre
					tiles.set(id, menu + 12);  

					if(tiles.get(id) == 16) tiles.set(id, 9);
					else if(tiles.get(id) == 17) tiles.set(id, 11);
					
					menu = 0;
					coins -= price;
				}
			}
			
			if(employees == 0 && mouse.getX() >= 704 && mouse.getX() < 960 && mouse.getY() < 64) {
				employees = 1;
				menu = 0;  // lorsque le menu de furnishings est ferme et que celui des employées est ouvert.
			} else if(employees == 1 && menu == 0) {
				int random = (int)(Math.random() * (width - 1)) * 64;

				if(mouse.getX() >= 704 && mouse.getX() < 960 && mouse.getY() < 64) employees = 0;
				else if(mouse.getX() >= 168 && mouse.getX() < 232 && mouse.getY() >= 192 && mouse.getY() < 256 && coins >= 5000) {
					servers.add(new Server(1, random, height * 64));  // on ajoute un serveur de niveau 1
					employees = 0;
					coins -= 5000;
				} else if(mouse.getX() >= 432 && mouse.getX() < 496 && mouse.getY() >= 192 && mouse.getY() < 256 && coins >= 10000) {
					servers.add(new Server(2, random, height * 64));  //  on ajoute un serveur de niveau 2
					employees = 0;
					coins -= 10000;
				} else if(mouse.getX() >= 696 && mouse.getX() < 760 && mouse.getY() >= 192 && mouse.getY() < 256 && coins >= 15000) {
					servers.add(new Server(3, random, height * 64)); // on ajoute un serveur de niveau 3
					employees = 0;
					coins -= 15000;
				} else if(mouse.getX() >= 128 && mouse.getX() < 320 && mouse.getY() >= 320 && mouse.getY() < 384 && coins >= 5000) {
					int id = -1;

					for(int i = 0; i < servers.size(); i ++) if(servers.get(i).getLevel() == 1 && id == -1) id = i;

					if(id != -1) {  // ici on gere les amélioration des serveur 
						servers.add(new Server(2, (int)(servers.get(id).getX() / 8) * 8, (int)(servers.get(id).getY() / 8) * 8));
						servers.remove(id); // niveau 1 --> niveau 2
						employees = 0;
						coins -= 5000;
					}
				} else if(mouse.getX() >= 544 && mouse.getX() < 736 && mouse.getY() >= 320 && mouse.getY() < 384 && coins >= 5000) {
					int id = -1;

					for(int i = 0; i < servers.size(); i ++) if(servers.get(i).getLevel() == 2 && id == -1) id = i;

					if(id != -1) {
						servers.add(new Server(3, (int)(servers.get(id).getX() / 8) * 8, (int)(servers.get(id).getY() / 8) * 8));
						servers.remove(id); //niveau 2 --> niveau 3
						employees = 0;
						coins -= 5000;
					}
				} else if(mouse.getX() >= 168 && mouse.getX() < 232 && mouse.getY() >= 448 && mouse.getY() < 512 && coins >= 5000) {
					chefs.add(new Chef(1, random, height * 64));
					employees = 0; // on ajoute un chef de niveau 1
					coins -= 5000;
				} else if(mouse.getX() >= 432 && mouse.getX() < 496 && mouse.getY() >= 448 && mouse.getY() < 512 && coins >= 10000) {
					chefs.add(new Chef(2, random, height * 64));
					employees = 0;
					coins -= 10000; // un chef de niveau 2
				} else if(mouse.getX() >= 696 && mouse.getX() < 760 && mouse.getY() >= 448 && mouse.getY() < 512 && coins >= 15000) {
					chefs.add(new Chef(3, random, height * 64));
					employees = 0;
					coins -= 15000; // un chef de niveau 3
				} else if(mouse.getX() >= 128 && mouse.getX() < 320 && mouse.getY() >= 576 && mouse.getY() < 640 && coins >= 5000) {
					int id = -1;

					for(int i = 0; i < chefs.size(); i ++) if(chefs.get(i).getLevel() == 1 && id == -1) id = i;
					// Pareil que pour les serveurs ici on gere l'amelioration des chefs
					if(id != -1) {
						chefs.add(new Chef(2, (int)(chefs.get(id).getX() / 8) * 8, (int)(chefs.get(id).getY() / 8) * 8));
						chefs.remove(id);
						employees = 0;
						coins -= 5000;
					}
				} else if(mouse.getX() >= 544 && mouse.getX() < 736 && mouse.getY() >= 576 && mouse.getY() < 640 && coins >= 5000) {
					int id = -1;

					for(int i = 0; i < chefs.size(); i ++) if(chefs.get(i).getLevel() == 2 && id == -1) id = i;

					if(id != -1) {
						chefs.add(new Chef(3, (int)(chefs.get(id).getX() / 8) * 8, (int)(chefs.get(id).getY() / 8) * 8));
						chefs.remove(id);
						employees = 0;
						coins -= 5000;
					}
				}
			}
			
			if(mouse.getX() >= 64 && mouse.getX() < 960 && mouse.getY() < 64 && hours == 0) {
				approvisionement = 500;
				hours = 8;
				minutes = 0;
				employees = 0;
				menu = 0;  // on relance la journee avec le nombre de plat remis au max.
			}

			itemX = mouse.getX();
			itemY = mouse.getY();
		}
		
		public void mouseReleased(MouseEvent mouse) {
			
		}
		
		public void mouseExited(MouseEvent mouse) {
			
		}
	}
	
	public class Repaint extends TimerTask {
		
		public void run() {
			if(hours < 20 && Math.random() * 500 < 1 && menu == 0 && employees == 0) clients.add(new Client(Math.random() < .8 ? 1 : Math.random() < .15 ? 2 : 3, (int)(Math.random() * (width - 1)) * 64, height * 64));
			//Permet de faire rentre les clients avec des frequence aléatoire en restant sur la base 1 client toutes les 15 seconde mais ca reste aléatoire
			// On prend un nombre aléatoire entre 0 et 500 et il faut que ce chiffre soit inferieur a 1 pour que le client entre, on a donc 1 chance sur 500 que le client entre
			//on fait donc 500 /30 car le jeu s'actualise 30 fois par seconde, ce qui nous fait un client toutes les 16,6 seconde.
			//Ensuite on a 80% de chance que cela soit un client de niveau 1, 15% que ca soit un niveau 2,  5% que sa soit un niveau 3
			repaint();
		}	
	}
}
