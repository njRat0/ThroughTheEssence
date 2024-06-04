/*** In The Name of Allah ***/
package game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
//import java.util.List;

//import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * The window on which the rendering is performed.
 * This structure uses the modern BufferStrategy approach for 
 * double-buffering; actually, it performs triple-buffering!
 * For more information on BufferStrategy check out:
 *    http://docs.oracle.com/javase/tutorial/extra/fullscreen/bufferstrategy.html
 *    http://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferStrategy.html
 * 
 * @author Seyed Mohammad Ghaffarian
 */
public class GameFrame extends JFrame {
	
	public static int gameHeight;                  // 720p game resolution
	public static int gameWidth;  // wide aspect ratio
	public static int gameCenterY;
	public static int gameCenterX;
	public static float coeficient;
	private int weaponFlipIndex = 1;
	//public static float coeficientY;

	private long lastRender;
	private ArrayList<Float> fpsHistory;

	private BufferStrategy bufferStrategy;
	
	public GameFrame(String title) {
		super(title);
		setResizable(false);
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 
		SetUp_WindowSizeParameters();
		setSize(gameWidth, gameHeight);
		lastRender = -1;
		fpsHistory = new ArrayList<>(100);

		System.out.println(gameWidth);
		System.out.println(gameWidth);
		System.out.println(gameHeight);
		//System.out.println(gameHeight);//1024
		System.out.println(gameCenterX);
		System.out.println(gameCenterY);
		//System.out.println(gameCenterY); // 512
		System.out.println(coeficient);
		//System.out.println(coeficientY);
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

	private void doRendering(Graphics2D g2d, Player player, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets, ArrayList<InteractingObject> interactingObjects, Skill weapon) {
		// Draw background
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, gameWidth + 20, gameHeight + 20);
		//Draw player and enemies
		if(!player.isDead){
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
					
					g2d.rotate(angle, player.locX + weapon.sprite.getWidth()/2,player.locY + weapon.sprite.getHeight()/2);
					if(player.locX > player.mouseX){
						g2d.drawImage(weapon.sprite, player.locX + weapon.sprite.getWidth()/2,player.locY + weapon.sprite.getHeight()/2, weapon.sprite.getWidth(),-weapon.sprite.getHeight(), null);
					}
					else{
						g2d.drawImage(weapon.sprite, player.locX + 16,player.locY+ 8, weapon.sprite.getWidth(),weapon.sprite.getHeight(), null);
					}
					g2d.rotate(-angle,player.locX + weapon.sprite.getWidth()/2,player.locY + weapon.sprite.getHeight()/2);
					
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
				for(MyButton button : GameLoop.chooseSkillButtons){
					button.toDraw(g2d);
					for(Skill skill : GameLoop.choosingSkillsOnButtons){
						String str = skill.name;
						g2d.setColor(Color.WHITE);
						g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(15.0f));
						//int strWidth = g2d.getFontMetrics().stringWidth(str);
						g2d.drawString(str, button.GetLocationX(), button.GetLocationY());
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
			//int strWidth = g2d.getFontMetrics().stringWidth(str);
			g2d.drawString(str1, 18, 75);
				//exp bar
			g2d.setColor(Color.black);
			g2d.fillRect(0, gameHeight - 30, gameWidth, 30);
			g2d.setColor(Color.BLUE);
			g2d.fillRect(0, gameHeight - 27, (int)((float)(player.curEXP) / player.expForLevelUp  * gameWidth), 16);
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
