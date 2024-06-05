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
import java.util.ArrayList;

/**
 * This class holds the state of game and all of its elements.
 * This class also handles user inputs, which affect the game state.
 * 
 * @author Seyed Mohammad Ghaffarian
 */
public class Player extends Character{
	
	//parameters
	public float moveSpeed = 5f;

	public boolean isDead;
	
	public boolean keyUP, keyDOWN, keyRIGHT, keyLEFT;
	public boolean mousePress;
	public int mouseX, mouseY;	
	private KeyHandler keyHandler;
	private MouseHandler mouseHandler;

	public Skill curWeapon;
	public Rectangle collision;
    public boolean isCollision;

	public float curHP = 300;
	public float maxHP = 300;
	private float hpRegen = 0.05f;

	public int level = 1;
	public int curEXP = 0;
	public int expForLevelUp = 5;
	public boolean isLevelingUping = false;
	//private int remainingEXP = 0;

	public ArrayList<Skill> skills = new ArrayList<Skill>();
	
	public Player() {
		locX = 0;
		locY = 0;
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

		//SplashOfFire skill = new SplashOfFire(null, this);
		//skill.canDamageEnemy = true;
		//skill.canDamagePlayer = false;
		// BlueCross skill1 = new BlueCross(null, this);
		// skill1.canDamageEnemy = true;
		// skill1.canDamagePlayer = false;
		// FireGun testWeapon = new FireGun(this);
		//skills.add(skill);
		// skills.add(skill1);
		// skills.add(testWeapon);
		// curWeapon = testWeapon;
	}
	
	/**
	 * The method which updates the game state.
	 */
	public void update() {
		if( isLevelingUping == false&& curEXP >= expForLevelUp){
			LevelUp();
		}
		if(curHP <= maxHP){
			curHP+=hpRegen;
		}
		for(Skill skill : skills){
			skill.update();
		}
		// if (mousePress) {
		// 	// locY = mouseY - diam / 2;
		// 	// locX = mouseX - diam / 2;
		// 	StandartBullet bullet = new StandartBullet(locX,locY,mouseX,mouseY,2f,this);
		// 	bullet.speed = 12f;
        //     bullet.SetSprite("res\\Bullets\\GojoSatoru(BLUE).png");
        //     bullet.sizeOfSprite = 1f;
        //     bullet.canDamagePlayer = false;
        //     bullet.canDamageEnemy = true;
        //     bullet.damage = 4f;

		// 	bullet.SetUpCollision();
		// }
		if (keyUP)
			locY -= (keyLEFT || keyRIGHT) ? moveSpeed * Settings.COEFFICIENT_OF_DIAGANOL_MOVING : moveSpeed;
		if (keyDOWN)
			locY += (keyLEFT || keyRIGHT) ? moveSpeed * Settings.COEFFICIENT_OF_DIAGANOL_MOVING : moveSpeed;
		if (keyLEFT)
			locX -= (keyUP || keyDOWN) ? moveSpeed * Settings.COEFFICIENT_OF_DIAGANOL_MOVING : moveSpeed;
		if (keyRIGHT)
			locX += (keyUP || keyDOWN) ? moveSpeed * Settings.COEFFICIENT_OF_DIAGANOL_MOVING : moveSpeed;

		locX = Math.max(locX, 0);
		locX = Math.min(locX, GameFrame.gameWidth);
		locY = Math.max(locY, 0);
		locY = Math.min(locY, GameFrame.gameHeight);

		collision.x = locX;
		collision.y = locY;

	}

	public void LevelUp(){
		isLevelingUping = true;
		curEXP = curEXP - expForLevelUp;
		System.out.println(curEXP);
		expForLevelUp = (5+level*5) * level;
		level++;
		GameLoop.ChoosingSkills();
	}

	public void TakeDamage(float amount){
		curHP -= amount;
		if(curHP <= 0){
			isDead = true;
		}
	}

	public void toDraw(Graphics2D g2d){
		//g2d.drawImage(sprite, locX, locY, sprite.getWidth(), sprite.getHeight(), null);
        g2d.drawImage(sprite, locX,locY, (int)(sprite.getWidth()*sizeOfSprite * GameFrame.coeficient),(int)(sprite.getHeight()*sizeOfSprite * GameFrame.coeficient), null);
		g2d.setColor(new Color((int)((1 - curHP / maxHP) * 255),(int)(curHP / maxHP*255),0));
		g2d.fillRect(locX+ (int)((32-(int)(28 * curHP / maxHP)) / 2 *sizeOfSprite * GameFrame.coeficient),(int)(locY + 32 *sizeOfSprite * GameFrame.coeficient), (int)(28 * curHP / maxHP*sizeOfSprite * GameFrame.coeficient), (int)(4 *sizeOfSprite * GameFrame.coeficient));	
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
					GameLoop.isPause = (GameLoop.isPause == true)? false:true;
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
		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}
		@Override
		public void mouseDragged(MouseEvent e){
			mouseX = e.getX();
			mouseY = e.getY();
		}
	}
}

