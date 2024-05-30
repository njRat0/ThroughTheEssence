/*** In The Name of Allah ***/
package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
//import java.util.List;

//import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
	
	public static final int GAME_HEIGHT = 720;                  // 720p game resolution
	public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;  // wide aspect ratio
	public static final int GAME_CENTER_Y = GAME_HEIGHT/2;
	public static final int GAME_CENTER_X = GAME_WIDTH/2;

	private long lastRender;
	private ArrayList<Float> fpsHistory;

	public static JPanel panelOfButtons = new JPanel();

	//private JButton restartButton = new JButton("restart"); 

	private BufferStrategy bufferStrategy;
	
	public GameFrame(String title) {
		super(title);
		setResizable(false);
		setSize(GAME_WIDTH, GAME_HEIGHT);
		lastRender = -1;
		fpsHistory = new ArrayList<>(100);

		add(panelOfButtons);

		//restartButton.addActionListener(null);
		//restartButton.setLayout(null);
		//restartButton.setBounds(0,0,100,50);
		//this.add(restartButton);
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
		g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
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
				g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, strHeight);
			}
			lastRender = currentRender;
		}
		else{
			String str = "GAME OVER";
			g2d.setColor(Color.WHITE);
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(64.0f));
			int strWidth = g2d.getFontMetrics().stringWidth(str);
			g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, GAME_HEIGHT / 2);
		}
	}
}
