package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

enum TypeOfEnemy{
    GojoSatoru
}

public abstract class Enemy {
    public float moveSpeed = 4;
    public float damage = 1.5f;
    public float curHP = 20;
    public float maxHP = 20;
    public int locX, locY;
    public BufferedImage sprite;
    public TypeOfEnemy type;

    public float sizeOfSprite = 1f;
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
            g2d.drawImage(sprite, locX,locY, (int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite), null);
		    g2d.setColor(new Color((int)((1 - curHP / maxHP) * 255),(int)(curHP / maxHP*255),0));
		    g2d.fillRect(locX + (int)((32-(int)(28 * curHP / maxHP)) / 2), locY + 32, (int)(28 * curHP / maxHP), 4);
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

    public void update(){
        if(isDead == false){
            
            float deltaX = player.locX - locX;
            float deltaY = player.locY - locY;
            double angle = Math.atan2( deltaY, deltaX );
            locX += moveSpeed * Math.cos( angle );
            locY += moveSpeed * Math.sin( angle );
            timerCountForCheckingCollision++;
            curHP -= 0.02f;

            collision.x = locX;
            collision.y = locY;

            timerCountMeleeAttack++;
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
        if(curHP <= 0){
            Dead();
        }
	}

    public void Dead(){
        isDead = true;
    }

}

class TestMob extends Enemy{
    private SkillSystem skillSystem;

    public TestMob(Player player, int locX, int locY) {
        super(player, locX, locX);
        SetSprite("res/Characters/Icon2.png");
        super.damage = 2f;
        super.moveSpeed = 6f;
        super.maxHP = 800f;
        super.curHP = 800f;
        //super.isRangeAttack = true;
        super.delayBtwRangeAttacks = 2f;
        super.hasOwnTimerSystem = false;

        skillSystem = new SkillSystem();
        skillSystem.canDamagePlayer = true;
        SetUpCollision();
    }

    void SetUpOfBullet() {
        skillSystem.Skill1(locX,locY,player.locX,player.locY,player);
    }
    
}

class FireLizard extends Enemy{
    public FireLizard(Player player, int locX, int locY) {
        super(player, locX, locX);
        SetSprite("res/Characters/Icon28.png");
        super.maxHP = 30f;
        super.curHP = 30f;
        super.isRangeAttack = true;
        delayBtwRangeAttacks = 3f;

        SetUpCollision();
    }

    void SetUpOfBullet() {
        Bullet bullet = new StandartBullet(locX,locY,player.locX,player.locY,4f, player);
        bullet.speed = 5f;
        bullet.SetSprite("res\\Bullets\\Fireball1.png");
        bullet.sizeOfSprite = 2f;
        bullet.SetUpCollision();
    }
    
}
class GojoSatoru extends Enemy{
    public GojoSatoru(Player player, int locX, int locY) {
        super(player, locX, locX);
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
        super(player, locX, locX);
        SetSprite("res\\Characters\\GoblinWizard.png");
        super.maxHP = 50f;
        super.curHP = 50f;
        super.moveSpeed = 3f;
        //super.sizeOfSprite = 1f;
        super.isRangeAttack = true;
        super.damage = 1f;

        SetUpCollision();
    }

    void SetUpOfBullet() {
        Bullet bullet = new StandartBullet(locX,locY,player.locX,player.locY,1.5f, player);
        bullet.speed = 16f;
        bullet.SetSprite("res\\Effects\\FireEffect.png");
        bullet.sizeOfSprite = 1f;
        bullet.canDamageEnemy = false;
        bullet.SetUpCollision();

        Bullet bullet1 = new StandartBullet(locX,locY,player.locX,player.locY,2f, player);
        bullet1.speed = 14f;
        bullet1.SetSprite("res\\Effects\\FireEffect.png");
        bullet1.sizeOfSprite = 1f;
        bullet1.canDamageEnemy = false;
        bullet1.SetUpCollision();
    }
    
}

