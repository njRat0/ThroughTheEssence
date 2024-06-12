/*** In The Name of Allah ***/
package game;

import java.util.ArrayList;
import java.util.Random;


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
	
	public static Random r = new Random();

	private GameFrame canvas;
	private static Player player;
	public static ArrayList<Enemy> listOfEnemies = new ArrayList<Enemy>();
	public static ArrayList<Bullet> listOfBullets = new ArrayList<Bullet>();
	public static ArrayList<InteractingObject> listOfInteractingObjects = new ArrayList<InteractingObject>();

	public static boolean isPause = false;

	//ChoosingSkill system
	public static ArrayList<Skill> listOfAllSkills = new ArrayList<Skill>();
	public static int probabilityOfAllSkills;
	public static ArrayList<MyButton> chooseSkillButtons = new ArrayList<MyButton>();
	public static ArrayList<Skill> choosingSkillsOnButtons = new ArrayList<Skill>();
	public static int maxNumberOfActiveSkills = 2;
	public static int maxNumberOfPassiveSkills = 6;
	public static int countChoosingSlotsForAS = 3; //-->For active skills
	public static int countChoosingSlotsForPS = 3; //-->for passive skills
	//private int maxNumberOfUpgratingParameters = 2;
	public static boolean isChoosingSkills = true;
	
	public static void ChoosingSkills(){
		isPause = true;
		probabilityOfAllSkills = 0;
		for(Skill skill : listOfAllSkills){
			probabilityOfAllSkills += skill.chanceOfDrop;
		}
		//System.out.println("probability: "+probabilityOfAllSkills);
		SetUp_ChooseSkillButtons();
	}
	public static void SetUp_ChooseSkillButtons(){
		
		chooseSkillButtons.clear();
		choosingSkillsOnButtons.clear();
		for(int i = 0; i < GameLoop.countChoosingSlotsForAS; i++){
			MyButton button = new MyButton(player);
			button.SetSize(98, 100);
			button.SetLocation((GameFrame.gameCenterX - GameLoop.countChoosingSlotsForAS*100 / 2) + i*150, GameFrame.gameCenterY - 50);
			button.id = i;
			//int num = 0;
			int num = r.nextInt(probabilityOfAllSkills);
			for(Skill skill : listOfAllSkills){
				num -= skill.chanceOfDrop;
				//System.out.println(num);
				if(num <= 0){
					choosingSkillsOnButtons.add(skill);
					skill.numberOfChoosedUpgrade.add(0);
					skill.numberOfChoosedUpgrade.add(0);
					skill.numberOfChoosedUpgrade.add(0);
					if(skill.chanceOfDrop == 50){
						boolean isChoosed = false;
						for(int j = 0; j< 50; j++){
							int chooseUpgrate = r.nextInt(skill.numberOfUpgradePoints) + 1;
							//int chooseUpgrate = r.nextInt(1, skill.numberOfUpgradePoints + 1);
							//System.out.println(chooseUpgrate);
							if(skill.GetPointsOfUpgrateSkill(chooseUpgrate)){
								isChoosed = true;
								skill.numberOfChoosedUpgrade.set(i,chooseUpgrate);
								break;
							}
						}
						if(isChoosed == false){
							continue;
						}
					}
					break;
				}	
			}

			chooseSkillButtons.add(button);
		}
		isChoosingSkills = true;
	}

	public static void EndChoosingSkills(int id){
		isChoosingSkills = false;
		isPause = false;
		Skill skill = choosingSkillsOnButtons.get(id);
		if(skill.chanceOfDrop == 50){
			skill.UpgrateSkill(skill.numberOfChoosedUpgrade.get(id));
			skill.levelOfSkill += 1;
		}
		else{
			if(skill.type == TypeOfSkill.weapon){
				skill.chanceOfDrop = 50;
				if(player.curWeapon  != null){
					player.curWeapon.chanceOfDrop = 15;
					for(int i = 0; i < player.curWeapon.levelOfSkill; i++){
						skill.UpgrateSkill(r.nextInt(skill.levelOfSkill)  + 1);
					}
				}
				player.curWeapon = skill;
			}
			else{
				skill.chanceOfDrop = 50;
				player.skills.add(skill);		
			}
		}
		for(Skill skill1 : choosingSkillsOnButtons){
			skill1.numberOfChoosedUpgrade.clear();
		}
		if(choosingSkillsOnButtons.get(id).type == TypeOfSkill.weapon){
			player.curWeapon = choosingSkillsOnButtons.get(id);
		}
		//System.out.println(skill.chanceOfDrop);
		player.isLevelingUpping = false;
		player.mousePress = false;
		//System.out.println("player skills: " + player.skills.size());
		//choosingSkillsOnButtons.clear();
		//chooseSkillButtons.clear();
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

		//>>Creating test enemies
		// listOfEnemies.add(new TestMob(player, 500, 200));
		
		// for(int i = 0; i<20;i++){
		// 	listOfEnemies.add(new FireLizard(player, 10*i, 500));
		// }
		// try{
		// 	listOfEnemies.get(0).curHP = 10f;
		// }
		// catch(ArrayIndexOutOfBoundsException e){
		// 	e.getStackTrace();
		// }
		canvas.addKeyListener(player.getKeyListener());
		canvas.addMouseListener(player.getMouseListener());
		canvas.addMouseMotionListener(player.getMouseMotionListener());
		
		SetUp_listOfSkills();

		listOfAllSkills.get(0).chanceOfDrop = 50;
		player.curWeapon = listOfAllSkills.get(0);
		
		//ChoosingSkills(); 
	}

	public void SetUp_listOfSkills(){
		listOfAllSkills.add(new BasicGun(player));
		listOfAllSkills.add(new FireGun(player));
		listOfAllSkills.add(new IonRed_Gun(player));
		listOfAllSkills.add(new FiveX_Gun(player));
		listOfAllSkills.add(new Crest_weapon(player));
		listOfAllSkills.add(new UpgrateSkill_amountOfCasts(player));
		listOfAllSkills.add(new UpgrateSkill_damage(player));
		listOfAllSkills.add(new UpgrateSkill_IncreaseVampiric(player));
		listOfAllSkills.add(new UpgrateSkill_Speed(player));
		listOfAllSkills.add(new UpgrateSkill_HPregen(player));
		listOfAllSkills.add(new UpgrateSkill_ReduceCDofSkills(player));
		listOfAllSkills.add(new UpgrateSkill_HPpoints(player));
		listOfAllSkills.add(new VampiricDisc(null, player));
		listOfAllSkills.add(new AtomicPudge(null, player));
		listOfAllSkills.add(new BlueCross(null , player));
		listOfAllSkills.add(new SplashOfFire(null, player));
		listOfAllSkills.add(new RotatingDiscs(null, player));
	}

	public static int timer = 0;
	public int levelOfDificulty = 0;
	public void SpawnEnemies(){
		if(timer % (300*Settings.maxFps) == 0){
			levelOfDificulty ++;
			int choosedEnemy = r.nextInt(levelOfDificulty)+1;
			int posX = 0;
			int posY = 0;
			if(r.nextInt(2) == 1){
				if(r.nextInt(2) == 1){
					posY = GameFrame.gameHeight;
				}
				else{
					posY = 0;
				}
				posX = r.nextInt(GameFrame.gameWidth + 1);
			}
			else{
				if(r.nextInt(2) == 1){
					posX = GameFrame.gameWidth;
				}
				else{
					posX = 0;
				}
				posY = r.nextInt(GameFrame.gameHeight + 1);
			}
			switch (choosedEnemy) {
				case 1:
					int rBoss = r.nextInt(1) + 1;
					switch (rBoss) {
						case 1:
							listOfEnemies.add(new Spider_Boss(player, posX, posY));
							break;
					
						default:
							break;
					}
					break;
				default:
					break;
				}
			}
		if(timer % (5*Settings.maxFps) == 0){
			int levelOfDificulty = timer / (30*Settings.maxFps) + 1;

			for(int i = 0; i < levelOfDificulty * 5 - levelOfDificulty;){
				int choosedEnemy = r.nextInt(levelOfDificulty)+1;
				i+= choosedEnemy;
				int posX = 0;
				int posY = 0;
				if(r.nextInt(2) == 1){
					if(r.nextInt(2) == 1){
						posY = GameFrame.gameHeight;
					}
					else{
						posY = 0;
					}
					posX = r.nextInt(GameFrame.gameWidth + 1);
				}
				else{
					if(r.nextInt(2) == 1){
						posX = GameFrame.gameWidth;
					}
					else{
						posX = 0;
					}
					posY = r.nextInt(GameFrame.gameHeight + 1);
				}
				switch (choosedEnemy) {
					case 1:
						//listOfEnemies.add(new BlackSpider(player, posX, posY));
						listOfEnemies.add(new Slime_lvl1(player, posX, posY));
						break;
					case 2:
						listOfEnemies.add(new Slime_lvl2(player, posX, posY));
						break;
					case 3:
						listOfEnemies.add(new FireLizard(player, posX, posY));
						break;
					case 4:
						listOfEnemies.add(new Slime_lvl3(player, posX, posY));
						break;
					case 5:
						listOfEnemies.add(new GoblinWizard(player, posX, posY));
						break;
					case 6:
						listOfEnemies.add(new Kaban(player, posX, posY));
						break;
					case 7:
						listOfEnemies.add(new Lugart(player, posX, posY));
						break;
					case 8:
						listOfEnemies.add(new Slime_lvl4(player, posX, posY));
						break;
					case 9:
						listOfEnemies.add(new FireLeg(player, posX, posY));
						break;
					case 10:
						listOfEnemies.add(new FlyingDemon_lvl1(player, posX, posY));
						break;
					case 11:
						listOfEnemies.add(new Spider_lvl1(player, posX, posY));
						break;
					case 12:
						listOfEnemies.add(new BlackSpider(player, posX, posY));
						break;
					case 13:
						listOfEnemies.add(new DemonGigant_lvl1(player, posX, posY));
						break;
					case 14:
						listOfEnemies.add(new Bulurk(player, posX, posY));
						break;
					case 15:
						listOfEnemies.add(new DemonGigant_lvl2(player, posX, posY));
						break;
					default:
						break;
				}
			}
		}
	}

	@Override
	public void run() {
		boolean gameOver = false;
		while (!gameOver) {
			try {
				long start = System.currentTimeMillis();
				//System.out.println(player.mouseX + ", " + player.mouseY);
				//
				if(isChoosingSkills == true){
					for(MyButton button : chooseSkillButtons){
						button.update();
					}
				}
				if(isPause == false){
					timer++;
					SpawnEnemies();
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
					for(int i = 0; i < listOfInteractingObjects.size();i++){
						InteractingObject object = listOfInteractingObjects.get(i);
						if(object.isActivated){
							listOfInteractingObjects.remove(i);
							continue;
						}
						else{
							object.update();
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
				canvas.render(player, listOfEnemies, listOfBullets, listOfInteractingObjects, player.curWeapon);
				//gameOver = player.gameOver;
				//
				long delay = (1000 / FPS) - (System.currentTimeMillis() - start);
				if (delay > 0)
					Thread.sleep(delay);
			} catch (InterruptedException ex) {
			}
		}
		canvas.render(player, listOfEnemies, listOfBullets, listOfInteractingObjects, player.curWeapon);
	}

}
