/*** In The Name of Allah ***/
package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
public class Player{
	
	public float moveSpeed = 8;
	public int locX, locY, diam;
	public boolean isDead;
	
	private boolean keyUP, keyDOWN, keyRIGHT, keyLEFT;
	private boolean mousePress;
	private int mouseX, mouseY;	
	private KeyHandler keyHandler;
	private MouseHandler mouseHandler;

	public BufferedImage sprite;
	private float sizeOfSprite = 1f;
	
	public Rectangle collision;
    public boolean isCollision;

	public float curHP = 300;
	public float maxHP = 300;

	private SkillSystem skillSystem;
	
	public Player() {
		locX = 1000;
		locY = 1000;
		diam = 32;
		isDead = false;
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
		collision = new Rectangle(locX, locY, 32,32);
		//
		try {
			sprite = ImageIO.read(new File("res\\Characters\\Icon1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		skillSystem = new SkillSystem();
		skillSystem.canDamageEnemy = true;
	}
	
	/**
	 * The method which updates the game state.
	 */
	public void update() {
		if (mousePress) {
			// locY = mouseY - diam / 2;
			// locX = mouseX - diam / 2;
			skillSystem.Skill2(locX,locY,mouseX,mouseY,this);

			
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

		collision.x = locX;
		collision.y = locY;
	}

	public void TakeDamage(float amount){
		curHP -= amount;
		if(curHP <= 0){
			isDead = true;
		}
	}

	public void toDraw(Graphics2D g2d){
		g2d.drawImage(sprite, locX, locY, sprite.getWidth(), sprite.getHeight(), null);
        g2d.drawImage(sprite, locX,locY, (int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite), null);
		g2d.setColor(new Color((int)((1 - curHP / maxHP) * 255),(int)(curHP / maxHP*255),0));
		g2d.fillRect(locX + (int)((32-(int)(28 * curHP / maxHP)) / 2), locY + 32, (int)(28 * curHP / maxHP), 4);	
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
				case KeyEvent.VK_W:
					keyUP = true;
					break;
				case KeyEvent.VK_S:
					keyDOWN = true;
					break;
				case KeyEvent.VK_A:
					keyLEFT = true;
					break;
				case KeyEvent.VK_D:
					keyRIGHT = true;
					break;
				case KeyEvent.VK_ESCAPE:
					isDead = true;
					break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_W:
					keyUP = false;
					break;
				case KeyEvent.VK_S:
					keyDOWN = false;
					break;
				case KeyEvent.VK_A:
					keyLEFT = false;
					break;
				case KeyEvent.VK_D:
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

