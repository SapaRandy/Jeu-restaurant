package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import core.Chef;
import core.Server;

public class Mouse implements MouseListener {
	
	public Mouse() {
	}
	
	public void mouseEntered(MouseEvent mouse) {
		
	}
	
	public void mousePressed(MouseEvent mouse) {
		
	}
	
	public void mouseClicked(MouseEvent mouse, int furnishings, int employees, int width, int height, int scrollX, int scrollY, int coins, ArrayList<Integer> tiles, ArrayList<core.Chef> chefs, ArrayList<core.Server> servers,
			int hours, int minutes, int itemX, int itemY, int foodLimit) {
		if(furnishings == 0 && mouse.getX() >= 64 && mouse.getX() < 320 && mouse.getY() < 64) {
			furnishings = 1;
			employees = 0;
		} else if(furnishings == 1 && employees == 0) {
			if(mouse.getX() >= 64 && mouse.getX() < 320 && mouse.getY() < 64) furnishings = 0;
			else if(mouse.getX() >= 192 && mouse.getX() < 256 && mouse.getY() >= 192 && mouse.getY() < 256) furnishings = 2;
			else if(mouse.getX() >= 576 && mouse.getX() < 640 && mouse.getY() >= 192 && mouse.getY() < 256) furnishings = 3;
			else if(mouse.getX() >= 192 && mouse.getX() < 256 && mouse.getY() >= 320 && mouse.getY() < 384) furnishings = 4;
			else if(mouse.getX() >= 576 && mouse.getX() < 640 && mouse.getY() >= 320 && mouse.getY() < 384) furnishings = 5;
			else if(mouse.getX() >= 192 && mouse.getX() < 256 && mouse.getY() >= 448 && mouse.getY() < 512) furnishings = 6;
			else if(mouse.getX() >= 576 && mouse.getX() < 640 && mouse.getY() >= 448 && mouse.getY() < 512) furnishings = 7;
			else if(mouse.getX() >= 128 && mouse.getX() < 320 && mouse.getY() >= 576 && mouse.getY() < 640 && coins >= 50000) {
				width ++;
				
				for(int i = 0; i < height; i ++) {
					tiles.set((width - 2) * height + i, i == 0 ? 1 : i == height - 1 ? 7 : 4);
					tiles.add(i == 0 ? 2 : i == height - 1 ? 8 : 5);
				}
				
				scrollX += 64;
				furnishings = 0;
				coins -= 50000;
			} else if(mouse.getX() >= 512 && mouse.getX() < 704 && mouse.getY() >= 576 && mouse.getY() < 640 && coins >= 50000) {
				ArrayList<Integer> copy = new ArrayList<Integer>();
				
				for(int i = 0; i < height * width; i ++) {
					copy.add(tiles.get(i));
					
					if(i % height == height - 1) copy.add(4);
				}
				
				height ++;
				tiles = copy;
				
				for(int i = 0; i < width; i ++) {
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
				furnishings = 0;
				coins -= 50000;
			}
		} else if(furnishings > 1) {
			int id = (int)(itemX / 64) * height + (int)(itemY / 64);
			int price = 0;

			if(furnishings == 2) price = 50;
			else if(furnishings == 3) price = 100;
			else if(furnishings == 4) price = 500;
			else if(furnishings == 5) price = 1500;
			else if(furnishings == 6) price = 5000;
			else if(furnishings == 7) price = 15000;

			if(mouse.getX() >= 64 && mouse.getX() < 320 && mouse.getY() < 64) furnishings = 0;
			else if(mouse.getX() >= 704 && mouse.getX() < 960 && mouse.getY() < 64 && tiles.get(id) == 4 && coins >= price) {
				tiles.set(id, furnishings + 12);

				if(tiles.get(id) == 16) tiles.set(id, 9);
				else if(tiles.get(id) == 17) tiles.set(id, 11);
				
				furnishings = 0;
				coins -= price;
			}
		}
		
		if(employees == 0 && mouse.getX() >= 704 && mouse.getX() < 960 && mouse.getY() < 64) {
			employees = 1;
			furnishings = 0;
		} else if(employees == 1 && furnishings == 0) {
			int random = (int)(Math.random() * (width - 1)) * 64;

			if(mouse.getX() >= 704 && mouse.getX() < 960 && mouse.getY() < 64) employees = 0;
			else if(mouse.getX() >= 168 && mouse.getX() < 232 && mouse.getY() >= 192 && mouse.getY() < 256 && coins >= 5000) {
				servers.add(new Server(1, random, height * 64));
				employees = 0;
				coins -= 5000;
			} else if(mouse.getX() >= 432 && mouse.getX() < 496 && mouse.getY() >= 192 && mouse.getY() < 256 && coins >= 10000) {
				servers.add(new Server(2, random, height * 64));
				employees = 0;
				coins -= 10000;
			} else if(mouse.getX() >= 696 && mouse.getX() < 760 && mouse.getY() >= 192 && mouse.getY() < 256 && coins >= 15000) {
				servers.add(new Server(3, random, height * 64));
				employees = 0;
				coins -= 15000;
			} else if(mouse.getX() >= 128 && mouse.getX() < 320 && mouse.getY() >= 320 && mouse.getY() < 384 && coins >= 5000) {
				int id = -1;

				for(int i = 0; i < servers.size(); i ++) if(servers.get(i).getLevel() == 1 && id == -1) id = i;

				if(id != -1) {
					servers.add(new Server(2, (int)(servers.get(id).getX() / 8) * 8, (int)(servers.get(id).getY() / 8) * 8));
					servers.remove(id);
					employees = 0;
					coins -= 5000;
				}
			} else if(mouse.getX() >= 544 && mouse.getX() < 736 && mouse.getY() >= 320 && mouse.getY() < 384 && coins >= 5000) {
				int id = -1;

				for(int i = 0; i < servers.size(); i ++) if(servers.get(i).getLevel() == 2 && id == -1) id = i;

				if(id != -1) {
					servers.add(new Server(3, (int)(servers.get(id).getX() / 8) * 8, (int)(servers.get(id).getY() / 8) * 8));
					servers.remove(id);
					employees = 0;
					coins -= 5000;
				}
			} else if(mouse.getX() >= 168 && mouse.getX() < 232 && mouse.getY() >= 448 && mouse.getY() < 512 && coins >= 5000) {
				chefs.add(new Chef(1, random, height * 64));
				employees = 0;
				coins -= 5000;
			} else if(mouse.getX() >= 432 && mouse.getX() < 496 && mouse.getY() >= 448 && mouse.getY() < 512 && coins >= 10000) {
				chefs.add(new Chef(2, random, height * 64));
				employees = 0;
				coins -= 10000;
			} else if(mouse.getX() >= 696 && mouse.getX() < 760 && mouse.getY() >= 448 && mouse.getY() < 512 && coins >= 15000) {
				chefs.add(new Chef(3, random, height * 64));
				employees = 0;
				coins -= 15000;
			} else if(mouse.getX() >= 128 && mouse.getX() < 320 && mouse.getY() >= 576 && mouse.getY() < 640 && coins >= 5000) {
				int id = -1;

				for(int i = 0; i < chefs.size(); i ++) if(chefs.get(i).getLevel() == 1 && id == -1) id = i;

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
			foodLimit = 200;
			hours = 8;
			minutes = 0;
			employees = 0;
			furnishings = 0;
		}

		itemX = mouse.getX();
		itemY = mouse.getY();
	}
	
	public void mouseReleased(MouseEvent mouse) {
		
	}
	
	public void mouseExited(MouseEvent mouse) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}