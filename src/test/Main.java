package test;

import javax.swing.JFrame;

import core.Restaurant;

public class Main { // voici la classe permettant de lancer le jeu

	public static void main(String[] args) {
		JFrame jRestaurant = new JFrame();
		jRestaurant.setContentPane(new Restaurant());
		jRestaurant.setTitle("Restaurant");
		jRestaurant.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jRestaurant.setSize(1024, 768);
		jRestaurant.setResizable(false);
		jRestaurant.setVisible(true);
	}	
}
