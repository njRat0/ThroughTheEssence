/*** In The Name of Allah ***/
package game;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

/**
 * This class holds the state of game and all of its elements.
 * This class also handles user inputs, which affect the game state.
 * 
 * @author Seyed Mohammad Ghaffarian
 */
public class Player {
	
	public float moveSpeed = 8;
	public int locX, locY, diam;
	public boolean gameOver;
	
	private boolean keyUP, keyDOWN, keyRIGHT, keyLEFT;
	private boolean mousePress;
	private int mouseX, mouseY;	
	private KeyHandler keyHandler;
	private MouseHandler mouseHandler;

	public BufferedImage sprite;
	
	public Player() {
		locX = 100;
		locY = 100;
		diam = 32;
		gameOver = false;
		//
		keyUP = false;
		keyDOWN = false;
		keyRIGHT = false;
		keyLEFT = false;
		//
		mousePress = false;
		mouseX = 0;
		mouseY = 0;
		//
		keyHandler = new KeyHandler();
		mouseHandler = new MouseHandler();
		//
		try {
			sprite = ImageIO.read(new File("res\\Icon1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * The method which updates the game state.
	 */
	public void update() {
		if (mousePress) {
			locY = mouseY - diam / 2;
			locX = mouseX - diam / 2;
		}
		if (keyUP)
			locY -= (keyLEFT || keyRIGHT) ? moveSpeed * Settings.COEFFICIENT_OF_DIAGANOL_MOVING : moveSpeed;
		if (keyDOWN)
			locY += (keyLEFT || keyRIGHT) ? moveSpeed * Settings.COEFFICIENT_OF_DIAGANOL_MOVING : moveSpeed;
		if (keyLEFT)
			locX -= (keyUP || keyDOWN) ? moveSpeed * Settings.COEFFICIENT_OF_DIAGANOL_MOVING : moveSpeed;
		if (keyRIGHT)
			locX += (keyUP || keyDOWN) ? moveSpeed * Settings.COEFFICIENT_OF_DIAGANOL_MOVING : moveSpeed;

		locX = Math.max(locX, 0);
		locX = Math.min(locX, GameFrame.GAME_WIDTH - diam);
		locY = Math.max(locY, 0);
		locY = Math.min(locY, GameFrame.GAME_HEIGHT - diam);
	}
	
	
	public KeyListener getKeyListener() {
		return keyHandler;
	}
	public MouseListener getMouseListener() {
		return mouseHandler;
	}
	public MouseMotionListener getMouseMotionListener() {
		return mouseHandler;
	}



	/**
	 * The keyboard handler.
	 */
	class KeyHandler extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_UP:
					keyUP = true;
					break;
				case KeyEvent.VK_DOWN:
					keyDOWN = true;
					break;
				case KeyEvent.VK_LEFT:
					keyLEFT = true;
					break;
				case KeyEvent.VK_RIGHT:
					keyRIGHT = true;
					break;
				case KeyEvent.VK_ESCAPE:
					gameOver = true;
					break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_UP:
					keyUP = false;
					break;
				case KeyEvent.VK_DOWN:
					keyDOWN = false;
					break;
				case KeyEvent.VK_LEFT:
					keyLEFT = false;
					break;
				case KeyEvent.VK_RIGHT:
					keyRIGHT = false;
					break;
			}
		}

	}

	/**
	 * The mouse handler.
	 */
	class MouseHandler extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			mousePress = true;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mousePress = false;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}
	}
}

