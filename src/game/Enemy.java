package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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

    public float delayBtwAttacks = 1f;
    private int timerCount = 0;

    private int timerCountForCheckingCollision = 0;

    public Rectangle collision;
    //public boolean isCollision;

    public Enemy(Player player, int locX, int locY){
        this.player = player;
        this.locX = locX;
        this.locY = locY;
    }

    public void toDraw(Graphics2D g2d){
        g2d.drawImage(sprite, locX,locY, (int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite), null);
		g2d.setColor(new Color((int)((1 - curHP / maxHP) * 255),(int)(curHP / maxHP*255),0));
		g2d.fillRect(locX + (int)((32-(int)(28 * curHP / maxHP)) / 2), locY + 32, (int)(28 * curHP / maxHP), 4);	
    }

    public void SetSprite(String source){
        try {
			sprite = ImageIO.read(new File(source));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void SetUpCollision(){
        collision = new Rectangle((int)locX,(int)locY,(int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite));
    }

    abstract void SetUpOfBullet();

    public void update(){
        collision.x = locX;
        collision.y = locY;
        
        if(collision.intersects(player.collision)){
            player.TakeDamage(damage);
        }

        float deltaX = player.locX - locX;
        float deltaY = player.locY - locY;
        double angle = Math.atan2( deltaY, deltaX );
        locX += moveSpeed * Math.cos( angle );
        locY += moveSpeed * Math.sin( angle );
        timerCountForCheckingCollision++;
        curHP -= 0.02f;
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
            timerCount++;
            if(timerCount >= delayBtwAttacks*Settings.maxFps){
                timerCount = 0;
                SetUpOfBullet();
            }
        }
        
    }

    public void Dead(){
        
    }

}

class TestMob extends Enemy{
    public TestMob(Player player, int locX, int locY) {
        super(player, locX, locX);
        SetSprite("res/Characters/Icon2.png");
        super.damage = 2f;

        SetUpCollision();
    }

    void SetUpOfBullet() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'SetUpOfBullet'");
    }
    
}

class FireLizard extends Enemy{
    public FireLizard(Player player, int locX, int locY) {
        super(player, locX, locX);
        SetSprite("res/Characters/Icon28.png");
        super.maxHP = 30f;
        super.curHP = 30f;
        super.isRangeAttack = true;
        delayBtwAttacks = 3f;

        SetUpCollision();
    }

    void SetUpOfBullet() {
        Bullet bullet = new Bullet(locX,locY,player.locX,player.locY,4f, player);
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
        Bullet bullet = new Bullet(locX,locY,player.locX,player.locY,2f, player);
        bullet.speed = 12f;
        bullet.SetSprite("res\\Bullets\\GojoSatoru(BLUE).png");
        bullet.sizeOfSprite = 1f;
        bullet.SetUpCollision();
    }
    
}

