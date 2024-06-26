package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

enum TypeOfEnemy{
    GojoSatoru,
    Boss
}

public abstract class Enemy extends Character {
    public float moveSpeed = 4;
    public float damage = 1.5f;
    public float curHP = 20;
    public float maxHP = 20;
    public TypeOfEnemy type;
    
    public Player player;
    public boolean isRangeAttack = false;

    public float delayBtwRangeAttacks = 1f;
    public float delayBtwMeleeAttacks = 0.2f;
    private int timerCountRangeAttack = 0;
    private int timerCountMeleeAttack = 0;

    private int timerCountForCheckingCollision = 0;

    public Rectangle collision;
    public boolean isDead = false;
    public boolean hasOwnTimerSystem = true;

    public boolean isStayAfterAttack = false;

    public int lootType = 1;

    public Enemy(Player player, int locX, int locY){
        this.player = player;
        this.locX = locX;
        this.locY = locY;
    }

    public void toDraw(Graphics2D g2d){
        if(curHP <=0){
            isDead = true;
        }
        if(isDead == false){
            if(this.type == TypeOfEnemy.Boss){
                try {
                    BufferedImage crown = ImageIO.read(new File("res\\Other\\Crown.png"));
                    g2d.drawImage(crown, (int)(locX + 8 *sizeOfSprite),(int)(locY - 8*sizeOfSprite - 4), (int)(sprite.getWidth()*sizeOfSprite / 2),(int)(sprite.getHeight()*sizeOfSprite / 2), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            g2d.drawImage(sprite, locX,locY, (int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite), null);
		    g2d.setColor(new Color((int)((1 - curHP / maxHP) * 255),(int)(curHP / maxHP*255),0));
		    g2d.fillRect(locX + (int)((32 * sizeOfSprite - (int)((32 * sizeOfSprite - 4 * sizeOfSprite) * curHP / maxHP)) / 2), (int)(locY + 32 * sizeOfSprite), (int)((32 * sizeOfSprite - 4 * sizeOfSprite)* curHP / maxHP), 4);
        } 	
    }

    public void SetSprite(String source){
        try {
			sprite = ImageIO.read(new File(source));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void SetUpCollision(){
        collision = new Rectangle((int)locX,(int)locY,(int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite));
    }

    abstract void SetUpOfBullet();

    private float deltaX , deltaY;
    public double angle;
    public void update(){
        if(isDead == false){
            timerCountMeleeAttack++;
            if(isStayAfterAttack == true){
                if(timerCountMeleeAttack >= delayBtwMeleeAttacks*Settings.maxFps){
                    float deltaX = player.locX - locX;
                    float deltaY = player.locY - locY;
                    double angle = Math.atan2( deltaY, deltaX );
                    locX += moveSpeed * Math.cos( angle );
                    locY += moveSpeed * Math.sin( angle );
                }
            }
            else{
                float deltaX = player.locX - locX;
                float deltaY = player.locY - locY;
                double angle = Math.atan2( deltaY, deltaX );
                locX += moveSpeed * Math.cos( angle );
                locY += moveSpeed * Math.sin( angle );
            }

            
            timerCountForCheckingCollision++;
            //curHP -= 0.02f;

            collision.x = locX;
            collision.y = locY;

            
            if(collision.intersects(player.collision) && timerCountMeleeAttack >= delayBtwMeleeAttacks*Settings.maxFps){
                timerCountMeleeAttack=0;
                player.TakeDamage(damage);
                locX -= moveSpeed * Math.cos( angle ) * 4;
                locY -= moveSpeed * Math.sin( angle ) * 4;  
            }
            
            if(timerCountForCheckingCollision >= 1){
                timerCountForCheckingCollision = 0;
                for(Enemy enemy : GameLoop.listOfEnemies){
                    if(collision.intersects(enemy.collision) && collision != enemy.collision){
                        deltaX = enemy.locX - locX;
                        deltaY = enemy.locY - locY;
                        angle = Math.atan2( deltaY, deltaX );
                        locX -= moveSpeed * Math.cos( angle );
                        locY -= moveSpeed * Math.sin( angle );
                        break;
                    }
                }
            }


            if(isRangeAttack){
                if(hasOwnTimerSystem == true){
                    timerCountRangeAttack++;
                    if(timerCountRangeAttack >= delayBtwRangeAttacks*Settings.maxFps){
                        timerCountRangeAttack = 0;
                        SetUpOfBullet();
                    }
                }
                else{
                    SetUpOfBullet();
                }
            }
        }    
    }

    public void TakeDamage(float amount){
        curHP -= amount;
        if(player.vampiric != 0){
            player.TakeHeale(amount * player.vampiric);
        }
        if(curHP <= 0){
            Dead();
        }
	}

    public void Dead(){
        if(isDead==false){
            isDead = true;
            switch (lootType) {
                case 1:
                    new ExpStone_lvl1(locX, locY, player);
                    break;
                case 2:
                    new ExpStone_lvl2(locX, locY, player);
                    break;
                case 3:
                    new ExpStone_lvl3(locX, locY, player);
                    break;
                
                default:
                    break;
            }
        }
    }

}

class TestMob extends Enemy{
    public TestMob(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res/Characters/Icon2.png");
        super.damage = 2f;
        super.moveSpeed = 6f;
        super.maxHP = 800f;
        super.curHP = 800f;
        //super.isRangeAttack = true;
        super.delayBtwRangeAttacks = 2f;
        super.hasOwnTimerSystem = true;
        super.isRangeAttack = true;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
        StandartBullet bullet = new StandartBullet(locX,locY,player.locX,player.locY,4f, player);
        bullet.speed = 14f;
        bullet.canDamagePlayer = true;
        bullet.SetSprite("res\\Bullets\\FireFlash.png");
        bullet.sizeOfSprite = 2f;
        bullet.delayBtwDealingDamage = 0.1f;
        //bullet.pushingVelocity = 2f;
        bullet.isRotatable = true;
        bullet.damage = 8f;
        bullet.isAliveAfterDealingDamage = false;
        bullet.SetUpCollision();
    }
    
}

class FireLizard extends Enemy{
    public FireLizard(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res/Characters/Icon28.png");
        super.maxHP = 30f;
        super.curHP = 30f;
        super.isRangeAttack = true;
        delayBtwRangeAttacks = 3f;
        super.lootType = 1;

        SetUpCollision();
    }

    void SetUpOfBullet() {
        PushingBullet bullet = new PushingBullet(locX,locY,player.locX,player.locY,4f, player);
        bullet.pushingVelocity = 15f;
        bullet.speed = 5f;
        bullet.canDamagePlayer = true;
        bullet.SetSprite("res\\Bullets\\Fireball1.png");
        bullet.sizeOfSprite = 2f;
        bullet.delayBtwDealingDamage = 0.1f;
        //bullet.pushingVelocity = 2f;
        bullet.SetUpCollision();
    }
    
}
class GojoSatoru extends Enemy{
    public GojoSatoru(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res/Characters/524a84a508a5a16.png");
        super.maxHP = 100f;
        super.curHP = 100f;
        super.moveSpeed = 6f;
        super.type = TypeOfEnemy.GojoSatoru;
        super.sizeOfSprite = 0.05f;
        super.isRangeAttack = true;

        SetUpCollision();
    }

    void SetUpOfBullet() {
        Bullet bullet = new StandartBullet(locX,locY,player.locX,player.locY,2f, player);
        bullet.speed = 12f;
        bullet.SetSprite("res\\Bullets\\GojoSatoru(BLUE).png");
        bullet.sizeOfSprite = 1f;
        bullet.SetUpCollision();
    }
    
}

class GoblinWizard extends Enemy{
    public GoblinWizard(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res\\Characters\\GoblinWizard.png");
        super.maxHP = 50f;
        super.curHP = 50f;
        super.moveSpeed = 3f;
        //super.sizeOfSprite = 1f;
        super.isRangeAttack = true;
        super.damage = 2f;
        super.lootType = 2;

        SetUpCollision();
    }

    void SetUpOfBullet() {
        Bullet bullet = new StandartBullet(locX,locY,player.locX,player.locY,1.5f, player);
        bullet.speed = 16f;
        bullet.SetSprite("res\\Effects\\FireEffect.png");
        bullet.sizeOfSprite = 1f;
        bullet.delayBtwDealingDamage = 0.1f;
        bullet.canDamageEnemy = false;
        bullet.damage = 2f;
        bullet.SetUpCollision();

        Bullet bullet1 = new StandartBullet(locX,locY,player.locX,player.locY,2f, player);
        bullet1.speed = 14f;
        bullet1.SetSprite("res\\Effects\\FireEffect.png");
        bullet1.sizeOfSprite = 1f;
        bullet.damage = 2f;
        bullet1.canDamageEnemy = false;
        bullet1.SetUpCollision();
    }
    
}

class Slime_lvl1 extends Enemy{
    public Slime_lvl1(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res/Characters/Icon9.png");
        super.damage = 1f;
        super.moveSpeed = 2f;
        super.maxHP = 10f;
        super.curHP = 10f;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.isRangeAttack = false;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
    }
    
}

class Slime_lvl2 extends Enemy{
    public Slime_lvl2(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res/Characters/Icon20.png");
        super.damage = 3f;
        super.moveSpeed = 2f;
        super.maxHP = 30f;
        super.curHP = 30f;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.isRangeAttack = false;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
    }
    
}

class Slime_lvl3 extends Enemy{
    public Slime_lvl3(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res/Characters/Icon20.png");
        super.sizeOfSprite = 2f;
        super.damage = 10f;
        super.moveSpeed = 2f;
        super.maxHP = 100f;
        super.curHP = 100f;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.isRangeAttack = false;
        super.lootType = 2;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
    }
    
}

class Slime_lvl4 extends Enemy{
    public Slime_lvl4(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res\\Characters\\Icon15.png");
        super.sizeOfSprite = 2f;
        super.damage = 20f;
        super.moveSpeed = 2f;
        super.maxHP = 300f;
        super.curHP = 300f;
        //super.isRangeAttack = true;
        super.hasOwnTimerSystem = true;
        super.isRangeAttack = true;
        super.lootType = 2;
        super.delayBtwRangeAttacks = 5f;
        super.canBePushed = false;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
        StandartBullet bullet = new StandartBullet(locX, locY, player.locX, player.locY, 5, player);
        bullet.speed = 3f;
        bullet.damage = damage;
        bullet.delayBtwDealingDamage = 1f;
        bullet.canDamageEnemy = false;
        bullet.canDamagePlayer = true;
        bullet.SetSprite("res\\Bullets\\BasicGun_bullet.png");
        bullet.sizeOfSprite = 4f;
        bullet.SetUpCollision();
    }
    
}

class Lugart extends Enemy{
    public Lugart(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res\\Characters\\Icon11.png");
        //super.sizeOfSprite = 1f;
        super.damage = 5f;
        super.moveSpeed = 3f;
        super.maxHP = 150f;
        super.curHP = 150f;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.isRangeAttack = true;
        super.lootType = 2;
        super.delayBtwRangeAttacks = 6f;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
        LugartBullet bullet = new LugartBullet(locX, locY, player.locX, player.locY, 3f, player);
        //bullet.AddAngle((float)((r.nextFloat() - 0.5) * dispersion * 2));
        bullet.sizeOfSprite = 3f;
        bullet.SetSprite("res\\Bullets\\LugartBullet.png");
        bullet.speed = 4f;
        bullet.damage = 4;
        bullet.canDamagePlayer = true;
        bullet.canDamageEnemy = false;
        //bullet.delayBeforeStart = 0f;
        bullet.delayBtwDealingDamage = 0.05f;
        bullet.isAliveAfterDealingDamage = true;
        bullet.SetUpCollision();

        bullet.bullet = new LugartBullet(-5000, -5000, player.locX, player.locY,5f, player);
        bullet.bullet.delayBtwDealingDamage = 0.05f;
        bullet.bullet.sizeOfSprite = 2f;
        bullet.bullet.SetSprite("res\\Bullets\\LugartBullet.png");
        bullet.bullet.speed = 8;
        bullet.bullet.damage = 4;
        bullet.bullet.canDamagePlayer = true;
        bullet.bullet.canDamageEnemy = false;
        bullet.bullet.showBeforeStart = false;
        bullet.bullet.isAliveAfterDealingDamage = true;
        bullet.bullet.SetUpCollision(); 

        bullet.bullet.bullet = new LugartBullet(-5000, -5000, player.locX, player.locY,6f, player);
        bullet.bullet.bullet.delayBtwDealingDamage = 0.05f;
        bullet.bullet.bullet.sizeOfSprite = 1f;
        bullet.bullet.bullet.SetSprite("res\\Bullets\\LugartBullet.png");
        bullet.bullet.bullet.speed = 16;
        bullet.bullet.bullet.damage = 4;
        bullet.bullet.bullet.canDamagePlayer = true;
        bullet.bullet.bullet.canDamageEnemy = false;
        bullet.bullet.bullet.showBeforeStart = false;
        bullet.bullet.bullet.isAliveAfterDealingDamage = true;
        bullet.bullet.bullet.SetUpCollision();  
    }
    
}

class Kaban extends Enemy{
    public Kaban(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res\\Characters\\Icon7.png");
        super.sizeOfSprite = 1f;
        super.damage = 12f;
        super.moveSpeed = 8f;
        super.maxHP = 100f;
        super.curHP = 100f;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.delayBtwMeleeAttacks = 3f;
        super.isStayAfterAttack = true;
        super.isRangeAttack = false;
        super.lootType = 2;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
    }
    
}

class FireLeg extends Enemy{
    public FireLeg(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res\\Characters\\Icon16.png");
        super.sizeOfSprite = 2f;
        super.damage = 5f;
        super.moveSpeed = 2f;
        super.maxHP = 500f;
        super.curHP = 500f;
        //super.isRangeAttack = true;
        super.hasOwnTimerSystem = true;
        super.isRangeAttack = true;
        super.lootType = 3;
        super.delayBtwRangeAttacks = 5f;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
        PushingBullet bullet = new PushingBullet(locX, locY, player.locX, player.locY, 3, player);
        bullet.speed = 7f;
        //bullet.sizeOfSprite = 2f;
        bullet.isAliveAfterDealingDamage = false;
        bullet.damage = 35f;
        bullet.pushingVelocity = 65f;
        //bullet.delayBtwDealingDamage = 1f;
        bullet.canDamageEnemy = false;
        bullet.canDamagePlayer = true;
        bullet.SetSprite("res\\Effects\\FireEffect.png");
        bullet.sizeOfSprite = 2f;
        bullet.SetUpCollision();
    }
    
}

class FlyingDemon_lvl1 extends Enemy{
    public SplashOfFire skill;

    public FlyingDemon_lvl1(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res\\Characters\\Icon22.png");
        skill = new SplashOfFire(player, this);
        super.sizeOfSprite = 1f;
        super.damage = 0f;
        super.moveSpeed = 6f;
        super.maxHP = 400f;
        super.curHP = 400f;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.isRangeAttack = true;
        super.lootType = 3;
        //super.delayBtwRangeAttacks = 5f;
        super.hasOwnTimerSystem = false;
        super.modificator_CoolDownOfSkills = 0.5f;
        super.modificator_LifeTimeOfSkills = 1.5f;
        super.modificator_SpeedOfSkills = 1.5f;
        skill.canDamageEnemy = false;
        skill.canDamagePlayer = true;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
        skill.update();
    }
    
}

class Spider_Boss extends Enemy{
    public Spider_Boss(Player player, int locX, int locY) {
        super(player, locX, locY);
        super.sizeOfSprite = 2.5f;
        super.type = TypeOfEnemy.Boss;
        SetSprite("res\\Characters\\Icon25.png");
        super.damage = 15f;
        super.moveSpeed = 3f;
        super.maxHP = 6000f;
        super.curHP = 6000f;
        super.canBePushed = false;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.isRangeAttack = true;
        super.lootType = 3;
        super.delayBtwRangeAttacks = 4f;
        //super.delayBtwRangeAttacks = 5f;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
        CastingBullet bullet = new CastingBullet(locX, locY, player.locX, player.locY, 2f, player);
        //bullet.AddAngle((float)((r.nextFloat() - 0.5) * dispersion * 2));
        bullet.sizeOfSprite = 2f;
        bullet.SetSprite("res\\Bullets\\WebBullet.png");
        bullet.speed = 12f;
        bullet.damage = 12;
        bullet.canDamagePlayer = true;
        bullet.canDamageEnemy = false;
        //bullet.delayBeforeStart = 0f;
        bullet.delayBtwDealingDamage = 0.05f;
        bullet.isAliveAfterDealingDamage = false;
        bullet.SetUpCollision();

        bullet.bullet = new SpiderWeb(-5000, -5000, player.locX, player.locY,12f, player);
        bullet.bullet.delayBtwDealingDamage = 0.05f;
        bullet.bullet.sizeOfSprite = 6f;
        bullet.bullet.SetSprite("res\\Bullets\\Web.png");
        //bullet.bullet.speed = 8;
        bullet.bullet.damage = 0;
        bullet.bullet.canDamagePlayer = true;
        bullet.bullet.canDamageEnemy = false;
        bullet.bullet.showBeforeStart = false;
        bullet.bullet.isAliveAfterDealingDamage = true;
        bullet.bullet.SetUpCollision(); 

        if(curHP / 2 >=1000){
            SpiderEgg egg1 = new SpiderEgg(player, locX, locY);
            egg1.newSpiderHp = curHP / 2;
            GameLoop.listOfEnemies.add(egg1);
        }    
    }
    
}

class SpiderEgg extends Enemy{
    public float newSpiderHp;
    public SpiderEgg(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res\\Characters\\Icon40.png");
        super.sizeOfSprite = 1.5f;
        super.damage = 0;
        super.moveSpeed = 0;
        super.maxHP = 600f;
        super.curHP = 600f;
        super.canBePushed = false;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.isRangeAttack = true;
        super.lootType = 0;
        super.delayBtwRangeAttacks = 7f;
        //super.delayBtwRangeAttacks = 5f;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
        isDead = true;
        Spider_Boss enemy = new Spider_Boss(player, locX, locY);
        //enemy.maxHP = newSpiderHp;
        enemy.curHP = newSpiderHp;
        GameLoop.listOfEnemies.add(enemy);
    }
    
}

class Spider_lvl1 extends Enemy{
    public Spider_lvl1(Player player, int locX, int locY) {
        super(player, locX, locY);
        super.sizeOfSprite = 1.5f;
        SetSprite("res\\Characters\\Icon4.png");
        super.damage = 10f;
        super.moveSpeed = 3f;
        super.maxHP = 1500f;
        super.curHP = 1500f;
        super.canBePushed = false;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.isRangeAttack = true;
        super.lootType = 3;
        super.delayBtwRangeAttacks = 3f;
        //super.delayBtwRangeAttacks = 5f;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
        CastingBullet bullet = new CastingBullet(locX, locY, player.locX, player.locY, 2f, player);
        //bullet.AddAngle((float)((r.nextFloat() - 0.5) * dispersion * 2));
        bullet.sizeOfSprite = 1f;
        bullet.SetSprite("res\\Bullets\\WebBullet.png");
        bullet.speed = 12f;
        bullet.damage = 12;
        bullet.canDamagePlayer = true;
        bullet.canDamageEnemy = false;
        //bullet.delayBeforeStart = 0f;
        bullet.delayBtwDealingDamage = 0.05f;
        bullet.isAliveAfterDealingDamage = false;
        bullet.SetUpCollision();

        bullet.bullet = new SpiderWeb(-5000, -5000, player.locX, player.locY,12f, player);
        bullet.bullet.delayBtwDealingDamage = 0.05f;
        bullet.bullet.sizeOfSprite = 4f;
        bullet.bullet.SetSprite("res\\Bullets\\Web.png");
        //bullet.bullet.speed = 8;
        bullet.bullet.damage = 0;
        bullet.bullet.canDamagePlayer = true;
        bullet.bullet.canDamageEnemy = false;
        bullet.bullet.showBeforeStart = false;
        bullet.bullet.isAliveAfterDealingDamage = true;
        bullet.bullet.SetUpCollision(); 
    }
    
}

class DemonGigant_lvl1 extends Enemy{
    public DemonGigant_lvl1(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res\\Characters\\Icon10.png");
        super.sizeOfSprite = 2f;
        super.damage = 50f;
        super.moveSpeed = 3f;
        super.maxHP = 10000f;
        super.curHP = 10000f;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.delayBtwMeleeAttacks = 1f;
        super.isStayAfterAttack = true;
        super.isRangeAttack = false;
        super.lootType = 3;
        super.canBePushed = false;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
    }
    
}

class DemonGigant_lvl2 extends Enemy{
    public DemonGigant_lvl2(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res\\Characters\\Icon10.png");
        super.sizeOfSprite = 3f;
        super.damage = 80f;
        super.moveSpeed = 3f;
        super.maxHP = 16000;
        super.curHP = 16000;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.delayBtwMeleeAttacks = 1f;
        super.isStayAfterAttack = true;
        super.isRangeAttack = false;
        super.lootType = 3;
        super.canBePushed = false;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
    }
    
}

class Bulurk extends Enemy{
    public Bulurk(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res\\Characters\\Icon33.png");
        super.sizeOfSprite = 1f;
        super.damage = 10f;
        super.moveSpeed = 7f;
        super.maxHP = 8000;
        super.curHP = 8000;
        super.canBePushed = false;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.delayBtwMeleeAttacks = 2f;
        super.isStayAfterAttack = true;
        super.isRangeAttack = false;
        super.lootType = 3;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
    }
    
}

class BlackSpider extends Enemy{
    public BlackSpider(Player player, int locX, int locY) {
        super(player, locX, locY);
        super.sizeOfSprite = 1.5f;
        SetSprite("res\\Characters\\Icon46.png");
        super.damage = 5f;
        super.moveSpeed = 3f;
        super.maxHP = 2200f;
        super.curHP = 2200f;
        super.canBePushed = false;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.isRangeAttack = true;
        super.lootType = 3;
        super.delayBtwRangeAttacks = 1f;
        //super.delayBtwRangeAttacks = 5f;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
        CastingBullet bullet = new CastingBullet(locX, locY, player.locX, player.locY, 2f, player);
        //bullet.AddAngle((float)((r.nextFloat() - 0.5) * dispersion * 2));
        bullet.sizeOfSprite = 1f;
        bullet.SetSprite("res\\Bullets\\GreeenWebBullet.png");
        bullet.speed = 14f;
        bullet.damage = 3;
        bullet.canDamagePlayer = true;
        bullet.canDamageEnemy = false;
        //bullet.delayBeforeStart = 0f;
        //bullet.delayBtwDealingDamage = 0.05f;
        bullet.isAliveAfterDealingDamage = false;
        bullet.SetUpCollision();

        bullet.bullet = new SpiderWeb(-5000, -5000, player.locX, player.locY,6f, player);
        bullet.bullet.delayBtwDealingDamage = 0.1f;
        bullet.bullet.sizeOfSprite = 1.5f;
        bullet.bullet.SetSprite("res\\Bullets\\GreenWeb.png");
        //bullet.bullet.speed = 8;
        bullet.bullet.damage = 2f;
        bullet.bullet.canDamagePlayer = true;
        bullet.bullet.canDamageEnemy = false;
        bullet.bullet.showBeforeStart = false;
        bullet.bullet.isAliveAfterDealingDamage = true;
        bullet.bullet.SetUpCollision(); 
    }
    
}

class FlyingDemon_lvl2 extends Enemy{
    public SplashOfFire skill;

    public FlyingDemon_lvl2(Player player, int locX, int locY) {
        super(player, locX, locY);
        SetSprite("res\\Characters\\Icon22.png");
        skill = new SplashOfFire(player, this);
        super.sizeOfSprite = 2f;
        super.damage = 0f;
        super.moveSpeed = 6f;
        super.maxHP = 9000f;
        super.curHP = 9000f;
        //super.isRangeAttack = true;
        //super.hasOwnTimerSystem = true;
        super.isRangeAttack = true;
        super.lootType = 3;
        //super.delayBtwRangeAttacks = 5f;
        super.hasOwnTimerSystem = false;
        super.modificator_CoolDownOfSkills = 0.3f;
        super.modificator_LifeTimeOfSkills = 2f;
        super.modificator_SpeedOfSkills = 3f;
        super.modificator_Damage = 2f;
        super.modificator_AreaOfSkills = 3f;
        skill.canDamageEnemy = false;
        skill.canDamagePlayer = true;
        
        SetUpCollision();
    }

    void SetUpOfBullet() {
        skill.update();
    }
    
}

