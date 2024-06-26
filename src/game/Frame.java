/*** In The Name of Allah ***/
package game;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
//import java.util.List;

//import javax.swing.JButton;
import javax.swing.JFrame;

public class Frame extends JFrame {
	public static int gameHeight;                  // 720p game resolution
	public static int gameWidth;  // wide aspect ratio
	public static int gameCenterY;
	public static int gameCenterX;
	public static float coeficient;

	private long lastRender;
	private ArrayList<Float> fpsHistory;

	private BufferStrategy bufferStrategy;
	
	public Frame(String title) {
		super(title);
		setResizable(false);
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 
		SetUp_WindowSizeParameters();
		setSize(gameWidth, gameHeight);
		lastRender = -1;
		fpsHistory = new ArrayList<>(100);
	}


	public static void SetUp_WindowSizeParameters(){
		//gameWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		gameWidth = (int) 1280;
		gameHeight = (int)((float)gameWidth / 16 * 9);      
		gameCenterY = gameHeight/2;
		gameCenterX = gameWidth/2;
		coeficient = (float)gameWidth / (float)Settings.STANDART_WINDOW_SIZE_X;
		//coeficientY = (float)gameHeight / (float)Settings.STANDART_WINDOW_SIZE_Y;
		
	}
	
	/**
	 * This must be called once after the JFrame is shown:
	 *    frame.setVisible(true);
	 * and before any rendering is started.
	 */
	public void initBufferStrategy() {
		// Triple-buffering
		createBufferStrategy(3);
		bufferStrategy = getBufferStrategy();
	}

	
	/**
	 * Game rendering with triple-buffering using BufferStrategy.
	 */
	public void render(Player player, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets, ArrayList<InteractingObject> interactingObjects, Skill weapon) {
		// Get a new graphics context to render the current frame
		// Render single frame
		do {
			// The following loop ensures that the contents of the drawing buffer
			// are consistent in case the underlying surface was recreated
			do {
				// Get a new graphics context every time through the loop
				// to make sure the strategy is validated
				Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
				try {
					doRendering(graphics, player, enemies, bullets , interactingObjects, weapon);
				} finally {
					// Dispose the graphics
					graphics.dispose();
				}
				// Repeat the rendering if the drawing buffer contents were restored
			} while (bufferStrategy.contentsRestored());

			// Display the buffer
			bufferStrategy.show();
			// Tell the system to do the drawing NOW;
			// otherwise it can take a few extra ms and will feel jerky!
			Toolkit.getDefaultToolkit().sync();

		// Repeat the rendering if the drawing buffer was lost
		} while (bufferStrategy.contentsLost());
	}
	
	/**
	 * Rendering all game elements based on the game player.
	 */
	public static MyButton[] listOfMenuButtons = new MyButton[3];
	private void doRendering(Graphics2D g2d, Player player, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets, ArrayList<InteractingObject> interactingObjects, Skill weapon) {
		// Draw background
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, gameWidth + 20, gameHeight + 20);
		
		
		if(GameLoop.curLayout == 0){
			for(MyButton button : GameLoop.menuButtons){
				button.toDraw(g2d);
				String str = button.name;
				g2d.setColor(Color.WHITE);
				g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(12.0f));
				int strWidth = g2d.getFontMetrics().stringWidth(str);
				g2d.drawString(str, button.GetLocationX() + (100 - strWidth) / 2,  button.GetLocationY() + 28);
			}
		}
		else if(GameLoop.curLayout == 1){
			for(MyButton button : GameLoop.settingsButtons){
				button.toDraw(g2d);
			}
		}
		else if(GameLoop.curLayout == 2){
			for(MyButton button : GameLoop.rulesButtons){
				button.toDraw(g2d);
			}
		}
		else if(GameLoop.curLayout == 3){
			if(GameLoop.isChoosedClass == false){
				g2d.setColor(Color.white);
				g2d.fillRect(0, 0, gameWidth + 20, gameHeight + 20);
				for(MyButton button : GameLoop.listOfClassButtons){
					button.toDraw(g2d);
					int x = button.GetLocationX();
					int y = button.GetLocationY();
					String nameOfClass = button.name;
					g2d.setColor(Color.WHITE);
					g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(24.0f));
					//int strWidth = g2d.getFontMetrics().stringWidth(str);
					g2d.drawString(nameOfClass, x + 25, y + 65);
	
					g2d.drawImage(button.icon, x+25,y+85,button.icon.getWidth()*12,button.icon.getHeight()*12, null);
				}
			}
			else if(!player.isDead){
				//Draw player and enemies
				player.toDraw(g2d);
				if(interactingObjects != null){
					for(InteractingObject object : interactingObjects){
						object.toDraw(g2d);
					}
				}
				if(enemies != null){
					for(Enemy enemy : enemies){
						enemy.toDraw(g2d);
					}
				}
				if(weapon != null){
					try{
						float deltaX = player.mouseX - player.locX;
						float deltaY = player.mouseY - player.locY;
						double angle = Math.atan2( deltaY, deltaX );
						
						g2d.rotate(angle, player.locX + 16 ,player.locY + 8);
						if(player.locX > player.mouseX){
							g2d.drawImage(weapon.sprite,player.locX + 16 ,player.locY + 8, (int)(weapon.sprite.getWidth() * weapon.sizeOfSprite),(int)(-weapon.sprite.getHeight() * weapon.sizeOfSprite), null);
						}
						else{
							g2d.drawImage(weapon.sprite, player.locX + 16 ,player.locY + 8, (int)(weapon.sprite.getWidth() * weapon.sizeOfSprite),(int)(weapon.sprite.getHeight() * weapon.sizeOfSprite), null);
						}
						g2d.rotate(-angle,player.locX + 16 ,player.locY + 8);
						
					}
					catch(NullPointerException e){
	
					}
				}
				if(bullets != null){
					for(Bullet bullet : bullets){
						bullet.toDraw(g2d);
					}
				}
				if(GameLoop.isChoosingSkills == true){
					for(int i = 0; i < GameLoop.chooseSkillButtons.size(); i++){
						MyButton button = GameLoop.chooseSkillButtons.get(i);
						button.toDraw(g2d);
						String str = GameLoop.choosingSkillsOnButtons.get(i).name;
						g2d.setColor(Color.WHITE);
						g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(15.0f));
						//int strWidth = g2d.getFontMetrics().stringWidth(str);
						g2d.drawString(str, button.GetLocationX(), button.GetLocationY());
						if(GameLoop.choosingSkillsOnButtons.get(i).numberOfChoosedUpgrade.get(i) != 0){
							GameLoop.choosingSkillsOnButtons.get(i).GetPointsOfUpgrateSkill(GameLoop.choosingSkillsOnButtons.get(i).numberOfChoosedUpgrade.get(i));
							str = GameLoop.choosingSkillsOnButtons.get(i).nameOfChoosingParameter;
							g2d.setColor(Color.WHITE);
							g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(12.0f));
							//int strWidth = g2d.getFontMetrics().stringWidth(str);
							g2d.drawString(str, button.GetLocationX(), button.GetLocationY() + 50);
						}
					}
				}
				//>>>>UI rendering
					//hp bar
				g2d.setColor(Color.black);
				g2d.fillRect(8, 31, (int)(player.maxHP + 0.5f)  + 5, 25);
				g2d.setColor(Color.red);
				g2d.fillRect(8, 34, (int)(player.curHP + 0.5f), 19);
					//level indicator
				String str1 = "Level: " + player.level;
				g2d.setColor(Color.WHITE);
				g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(12.0f));
				g2d.drawString(str1, 18, 75);
					//exp bar
				g2d.setColor(Color.black);
				g2d.fillRect(0, gameHeight - 30, gameWidth, 30);
				g2d.setColor(Color.BLUE);
				g2d.fillRect(0, gameHeight - 27, (int)((float)(player.curEXP) / player.expForLevelUp  * gameWidth), 16);
					//timer
				String strTimer = "Seconds: " + (int)(GameLoop.timer / Settings.maxFps);
				g2d.setColor(Color.WHITE);
				g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(15f));
				int strWidthTimer = g2d.getFontMetrics().stringWidth(strTimer);
				g2d.drawString(strTimer, gameWidth - strWidthTimer - 40, 50);
				//<<<
				// Print FPS info
				long currentRender = System.currentTimeMillis();
				if (lastRender > 0) {
					fpsHistory.add(1000.0f / (currentRender - lastRender));
					if (fpsHistory.size() > 100) {
						fpsHistory.remove(0); // remove oldest
					}
					float avg = 0.0f;
					for (float fps : fpsHistory) {
						avg += fps;
					}
					avg /= fpsHistory.size();
					String str = String.format("Average FPS = %.1f , Last Interval = %d ms",
					avg, (currentRender - lastRender));
					g2d.setColor(Color.CYAN);
					g2d.setFont(g2d.getFont().deriveFont(18.0f));
					int strWidth = g2d.getFontMetrics().stringWidth(str);
					int strHeight = g2d.getFontMetrics().getHeight();
					g2d.drawString(str, (gameWidth - strWidth) / 2, strHeight + 25);
				}
				lastRender = currentRender;
			}
			else{
				String str = "GAME OVER";
				g2d.setColor(Color.WHITE);
				g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(64.0f));
				int strWidth = g2d.getFontMetrics().stringWidth(str);
				g2d.drawString(str, (gameWidth - strWidth) / 2, gameHeight / 2);
			}
		}	
	}
}
