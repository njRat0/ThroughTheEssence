/*** In The Name of Allah ***/
package game;

import java.awt.*;
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
		gameWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		gameHeight = (int)((float)gameWidth / 16 * 9);      
		gameCenterY = gameHeight/2;
		gameCenterX = gameWidth/2;
		coeficient = (float)gameWidth / (float)Settings.STANDART_WINDOW_SIZE_X;
		//coeficientY = (float)gameHeight / (float)Settings.STANDART_WINDOW_SIZE_Y;
		
	}

	public static void addButton(MyButton button){
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
	public void render(Player player, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets) {
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
					doRendering(graphics, player, enemies, bullets);
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
	private void doRendering(Graphics2D g2d, Player player, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets) {
		// Draw background
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, gameWidth + 20, gameHeight + 20);
		//Draw player and enemies
		if(!player.isDead){
			player.toDraw(g2d);
			if(enemies != null){
				for(Enemy enemy : enemies){
					enemy.toDraw(g2d);
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
				}
			}

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
				g2d.drawString(str, (gameWidth - strWidth) / 2, strHeight);
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
