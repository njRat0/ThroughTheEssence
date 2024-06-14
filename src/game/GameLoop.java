/*** In The Name of Allah ***/
package game;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;


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

	private Frame canvas;
	private static Player player;
	public static ArrayList<Enemy> listOfEnemies = new ArrayList<Enemy>();
	public static ArrayList<Bullet> listOfBullets = new ArrayList<Bullet>();
	public static ArrayList<InteractingObject> listOfInteractingObjects = new ArrayList<InteractingObject>();

	public static boolean isPause = false;

	public static boolean isChoosedClass;

	

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
			MyButton button = new MyButton(player, TypeOfButton.ChooseSkill);
			button.SetSize(98, 100);
			button.SetLocation((Frame.gameCenterX - GameLoop.countChoosingSlotsForAS*100 / 2) + i*150, Frame.gameCenterY - 50);
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

	public GameLoop(Frame frame) {
		canvas = frame;
	}
	
	/**
	 * This must be called before the game loop starts.
	 */
	public void init() {
		// Perform all initializations ...
		player = new Player();
		canvas.addKeyListener(player.getKeyListener());
		canvas.addMouseListener(player.getMouseListener());
		canvas.addMouseMotionListener(player.getMouseMotionListener());
		SetUp_MenuButtons();
		SetUp_listOfSkills();

		listOfAllSkills.get(0).chanceOfDrop = 50;
		player.curWeapon = listOfAllSkills.get(0);
		isPause = true;
		SetUp_ChooseClassButtons();
		
	}

	public void SetUp_listOfSkills(){
		listOfAllSkills.add(new BasicGun(player)); //0
		listOfAllSkills.add(new FireGun(player)); //1
		listOfAllSkills.add(new IonRed_Gun(player)); //2
		listOfAllSkills.add(new FiveX_Gun(player)); //3
		listOfAllSkills.add(new Crest_weapon(player)); //4
		listOfAllSkills.add(new UpgrateSkill_amountOfCasts(player)); //5
		listOfAllSkills.add(new UpgrateSkill_damage(player)); //6
		listOfAllSkills.add(new UpgrateSkill_IncreaseVampiric(player)); //7
		listOfAllSkills.add(new UpgrateSkill_Speed(player)); //8
		listOfAllSkills.add(new UpgrateSkill_HPregen(player)); //9
		listOfAllSkills.add(new UpgrateSkill_ReduceCDofSkills(player)); //10
		listOfAllSkills.add(new UpgrateSkill_HPpoints(player)); //11
		listOfAllSkills.add(new VampiricDisc(null, player)); //12
		listOfAllSkills.add(new AtomicPudge(null, player)); //13
		listOfAllSkills.add(new BlueCross(null , player)); //14
		listOfAllSkills.add(new SplashOfFire(null, player)); //15
		listOfAllSkills.add(new RotatingDiscs(null, player)); //16
		listOfAllSkills.add(new SniperShoot(null, player)); //17
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
					posY = Frame.gameHeight;
				}
				else{
					posY = 0;
				}
				posX = r.nextInt(Frame.gameWidth + 1);
			}
			else{
				if(r.nextInt(2) == 1){
					posX = Frame.gameWidth;
				}
				else{
					posX = 0;
				}
				posY = r.nextInt(Frame.gameHeight + 1);
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
						posY = Frame.gameHeight;
					}
					else{
						posY = 0;
					}
					posX = r.nextInt(Frame.gameWidth + 1);
				}
				else{
					if(r.nextInt(2) == 1){
						posX = Frame.gameWidth;
					}
					else{
						posX = 0;
					}
					posY = r.nextInt(Frame.gameHeight + 1);
				}
				switch (choosedEnemy) {
					case 1:
						//listOfEnemies.add(new FlyingDemon_lvl2(player, posX, posY));
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
					case 16:
						listOfEnemies.add(new FlyingDemon_lvl2(player, posX, posY));
						break;
					default:
						break;
				}
			}
		}
	}

	public static int curLayout = 3; // 0 -> menu; 1 -> settings; 2 -> rules; 3 -> game
	public static MyButton[] menuButtons = new MyButton[4];
	public static MyButton[] settingsButtons = new MyButton[4];
	public static MyButton[] rulesButtons = new MyButton[1];
	public static void SetUp_MenuButtons(){
		for(int i = 0; i < menuButtons.length; i++){
			menuButtons[i] = new MyButton(player, TypeOfButton.Menu);
			menuButtons[i].id=i;
			menuButtons[i].borderSize = 3;
			menuButtons[i].colorBackground = new Color(125, 125, 125);
			menuButtons[i].colorBorders = new Color(0, 0, 0);
			menuButtons[i].colorOver = new Color(94, 94, 94);
			menuButtons[i].colorClick = new Color(0, 0, 0);
			menuButtons[i].SetSize(100, 50);
			menuButtons[i].SetLocation(Frame.gameCenterX -50 , Frame.gameCenterY -240 + 60 * i );
		}
		menuButtons[0].name = "Start";
		menuButtons[1].name = "Settings";
		menuButtons[2].name = "Rules";
		menuButtons[3].name = "Exit";

		menuButtons[0].goTo = 3;
		menuButtons[1].goTo = 1;
		menuButtons[2].goTo = 2;
		menuButtons[3].goTo = -1;
	}

	public static MyButton[] listOfClassButtons = new MyButton[3];
	public static void SetUp_ChooseClassButtons(){
		int width = Frame.gameWidth / 3;
		int height = Frame.gameHeight;
		for(int i = 0; i < 3; i++){
			listOfClassButtons[i] = new MyButton(player, TypeOfButton.ChooseClass);
			listOfClassButtons[i].id=i;
			listOfClassButtons[i].borderSize = 3;
			listOfClassButtons[i].colorBackground = new Color(125, 125, 125);
			listOfClassButtons[i].colorBorders = new Color(0, 0, 0);
			listOfClassButtons[i].colorOver = new Color(94, 94, 94);
			listOfClassButtons[i].colorClick = new Color(0, 0, 0);
			listOfClassButtons[i].SetSize(width - 10, height);
			listOfClassButtons[i].SetLocation(width * i - 10 * i + 15, 0);
		}

		listOfClassButtons[0].name = "Warrior";
		listOfClassButtons[1].name = "Assasin";
		listOfClassButtons[2].name = "Mage";

		listOfClassButtons[0].description = "Warrior";
		listOfClassButtons[1].description = "Assasin";
		listOfClassButtons[2].description = "Mage";

		try {
			listOfClassButtons[0].icon = ImageIO.read(new File("res\\Characters\\Warrior.png"));
			listOfClassButtons[1].icon = ImageIO.read(new File("res\\Characters\\Assasin.png"));
			listOfClassButtons[2].icon = ImageIO.read(new File("res\\Characters\\Mage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void ChooseClass(int index){
		isPause = false;
		switch (index) {
			case 0:
				try {
					player.sprite = ImageIO.read(new File("res\\Characters\\Warrior.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.classOfPlayer = ClassOfPlayer.Warrior;
				player.canBePushed = false;
				player.maxHP = 260;
				player.curHP = 250;
				player.hpRegen = 1f;
				player.canBeSlowed = false;
				player.moveSpeed = 4f;
				player.modificator_Damage = 0.8f;
				break;
			case 1:
				try {
					player.sprite= ImageIO.read(new File("res\\Characters\\Assasin.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.classOfPlayer = ClassOfPlayer.Assasin;
				player.canBePushed = true;
				player.maxHP = 150;
				player.curHP = 150;
				player.hpRegen = 0.45f;
				player.canBeSlowed = true;
				player.moveSpeed = 6f;
				player.modificator_CoolDownOfSkills = 1.2f;
				player.modificator_Damage = 1.2f;
				break;
			case 2:
				try {
					player.sprite = ImageIO.read(new File("res\\Characters\\Mage.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.classOfPlayer = ClassOfPlayer.Mage;
				player.canBePushed = true;
				player.maxHP = 80;
				player.curHP = 80;
				player.hpRegen = 0.25f;
				player.canBeSlowed = true;
				player.moveSpeed = 5;
				player.modificator_CoolDownOfSkills = 0.8f;
				player.modificator_Damage = 1f;
				player.modificator_AreaOfSkills = 1.2f;
				player.modificator_amountsOfCastSkill = 1;
				player.curWeapon = null;
				player.skills.add(listOfAllSkills.get(14));
				listOfAllSkills.get(14).chanceOfDrop = 50;
				for(Skill skill : listOfAllSkills){
					if(skill.type == TypeOfSkill.weapon){
						skill.chanceOfDrop = 0;
					}
				}
				break;
		}
		isChoosedClass = true;
	}

	@Override
	public void run() {
		boolean gameOver = false;
		isPause = true;
		while (!gameOver) {
			try {
				long start = System.currentTimeMillis();
				System.out.println(player.mouseX + ", " + player.mouseY);
				if(curLayout == 0){
					for(MyButton button : GameLoop.menuButtons){
						button.update();
					}
					//continue;
				}
				if(isChoosedClass == false){
					for(MyButton classButton : listOfClassButtons){
						classButton.update();
					}
					canvas.render(player, listOfEnemies, listOfBullets, listOfInteractingObjects, player.curWeapon);
					continue;
				}
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
