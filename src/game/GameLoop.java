/*** In The Name of Allah ***/
package game;

import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
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
	
	public Random r = new Random();

	private GameFrame canvas;
	private Player player;
	public static ArrayList<Enemy> listOfEnemies = new ArrayList<Enemy>();
	public static ArrayList<Bullet> listOfBullets = new ArrayList<Bullet>();

	public static boolean isPause = false;

	//ChoosingSkill system
	public static ArrayList<Button> chooseSkillButtons = new ArrayList<Button>();
	public static int maxNumberOfActiveSkills = 2;
	public static int maxNumberOfPassiveSkills = 6;
	public static int countChoosingSlotsForAS = 4; //-->For active skills
	public static int countChoosingSlotsForPS = 3; //-->for passive skills
	//private int maxNumberOfUpgratingParameters = 2;
	public static boolean isChoosingSkills = true;
	public static void ChooseSkills(){
		//isChoosingSkills = true;
		SetUp_ChooseSkillButtons();
	}
	public static void SetUp_ChooseSkillButtons(){
		for(int i = 0; i < GameLoop.countChoosingSlotsForAS; i++){
			chooseSkillButtons.add(new Button());
			chooseSkillButtons.get(i).setSizeOfButton(98, 100);
			chooseSkillButtons.get(i).setPositionOfButton((GameFrame.GAME_CENTER_X - GameLoop.countChoosingSlotsForAS*100 / 2) + i*100, GameFrame.GAME_CENTER_Y);
			chooseSkillButtons.get(i).addMouseListener(null);
			chooseSkillButtons.get(i).addActionListener(null);
			//chooseSkillButtons.get(i).set
		}
	}

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
		
		for(int i = 0; i<20;i++){
			listOfEnemies.add(new TestMob(player, 10*i, 500));
		}
		
		
		//listOfEnemies.add(new FireLizard(player, 700, -100));
		//listOfEnemies.add(new GojoSatoru(player, 700, 200));
		// listOfEnemies.add(new GojoSatoru(player, 200, 200));
		// listOfEnemies.add(new GojoSatoru(player, 100, 400));
		// try{
		// 	listOfEnemies.get(0).curHP = 10f;
		// }
		// catch(ArrayIndexOutOfBoundsException e){
		// 	e.getStackTrace();
		// }
		canvas.addKeyListener(player.getKeyListener());
		canvas.addMouseListener(player.getMouseListener());
		canvas.addMouseMotionListener(player.getMouseMotionListener());
	}

	public static void DeleteEnemy(int id){
		listOfEnemies.remove(id);
	}

	@Override
	public void run() {
		boolean gameOver = false;
		while (!gameOver) {
			try {
				long start = System.currentTimeMillis();
				//
				if(isPause == false){
					player.update();
					for(int i = 0; i < listOfEnemies.size();i++){
						Enemy enemy = listOfEnemies.get(i);
						if(enemy.isDead){
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
