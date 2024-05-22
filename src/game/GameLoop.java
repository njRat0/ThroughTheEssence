/*** In The Name of Allah ***/
package game;

import java.util.ArrayList;
//mport java.util.List;

/**
 * A very simple structure for the main game loop.
 * THIS IS NOT PERFECT, but works for most situations.
 * Note that to make this work, none of the 2 methods 
 * in the while loop (update() and render()) should be 
 * long running! Both must execute very quickly, without 
 * any waiting and blocking!
 * 
 * Detailed discussion on different game loop design
 * patterns is available in the following link:
 *    http://gameprogrammingpatterns.com/game-loop.html
 */
public class GameLoop implements Runnable {
	
	/**
	 * Frame Per Second.
	 * Higher is better, but any value above 24 is fine.
	 */
	public static final int FPS = 30;
	
	private GameFrame canvas;
	private Player player;
	private ArrayList<Enemy> listOfEnemies = new ArrayList<Enemy>();
	public static ArrayList<Bullet> listOfBullets = new ArrayList<Bullet>();

	public GameLoop(GameFrame frame) {
		canvas = frame;
	}
	
	/**
	 * This must be called before the game loop starts.
	 */
	public void init() {
		// Perform all initializations ...
		player = new Player();
		listOfEnemies.add(new TestMob(player, 500, 200));
		listOfEnemies.add(new TestMob(player, -100, -100));
		listOfEnemies.add(new FireLizard(player, 700, -100));
		listOfEnemies.add(new GojoSatoru(player, 700, 200));
		try{
			listOfEnemies.get(0).curHP = 10f;
		}
		catch(ArrayIndexOutOfBoundsException e){
			e.getStackTrace();
		}
		canvas.addKeyListener(player.getKeyListener());
		canvas.addMouseListener(player.getMouseListener());
		canvas.addMouseMotionListener(player.getMouseMotionListener());
	}

	@Override
	public void run() {
		boolean gameOver = false;
		while (!gameOver) {
			try {
				long start = System.currentTimeMillis();
				//
				player.update();
				for(int i = 0; i < listOfEnemies.size();i++){
					Enemy enemy = listOfEnemies.get(i);
					if(enemy.curHP<=0){
						enemy.Dead();
						listOfEnemies.remove(i);
						continue;
					}
					else{
						enemy.update();
					}
				}
				for(int i = 0; i < listOfBullets.size();i++){
					//if()
					Bullet bullet = listOfBullets.get(i);
					if(bullet.isEnd == true){
						try{
							listOfBullets.remove(i);
							//continue;
						}
						catch(IndexOutOfBoundsException e){
							e.getStackTrace();
						}
					}
					else{
						bullet.update();
					}	
				}
				canvas.render(player, listOfEnemies, listOfBullets);
				//gameOver = player.gameOver;
				//
				long delay = (1000 / FPS) - (System.currentTimeMillis() - start);
				if (delay > 0)
					Thread.sleep(delay);
			} catch (InterruptedException ex) {
			}
		}
		canvas.render(player, listOfEnemies, listOfBullets);
	}
}
