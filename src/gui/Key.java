package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Key implements KeyListener {
	
	public void keyTyped(KeyEvent key) {
		
	}
	
	public void keyPressed(KeyEvent key, int scrollX, int scrollY, int width, int height) {
		if(key.getKeyCode() == KeyEvent.VK_RIGHT && scrollX <= width * 64 - 1088) scrollX += 64;
		else if(key.getKeyCode() == KeyEvent.VK_LEFT && scrollX >= 64) scrollX -= 64;
		else if(key.getKeyCode() == KeyEvent.VK_DOWN && scrollY <= height * 64 - 768) scrollY += 64;
		else if(key.getKeyCode() == KeyEvent.VK_UP && scrollY >= 64) scrollY -= 64;
	}
	
	public void keyReleased(KeyEvent key) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
