package game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public abstract class Bullet {
    public BufferedImage sprite;
    public float locX, locY;
    // private double velocityX; private double velocityY;
    public float damage = 5f;
    public float speed;
    public float sizeOfSprite = 1f;
    public float lifeTime = 3f;
    public boolean isEnd = false;
    public double angle;
    
    public float delayBeforeStart = 0;
    public int counterBeforeStart =0;
    public boolean showBeforeStart = true;

    public Player player;

    public Rectangle collision;

    public boolean stayInTheEndPosition = false;

    public boolean canDamagePlayer = true;
    public boolean canDamageEnemy = false;

    public float additionalAngle = 0f;

    public Timer timer = new Timer();

    public Bullet(int locX,int locY,int pointX, int pointY, float lifeTime , Player player){
        this.lifeTime = lifeTime;
        this.locX = locX;
        this.locY = locY;
        this.player = player;
        
        float deltaX = pointX - locX;
        float deltaY = pointY - locY;
        angle = Math.atan2( deltaY, deltaX );
        System.out.println(angle);

        GameLoop.listOfBullets.add(this);        

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try{
                    isEnd = true;
                }
                catch(ArrayIndexOutOfBoundsException e){
                    e.getStackTrace();
                }
            }
        }, (long)(lifeTime*1000));
    }

    public void SetCustomAngle(float angle){
        this.angle = angle;
    }

    public void AddAngle(float value){
        this.angle += value;
    }

    public void toDraw(Graphics2D g2d){
        if(showBeforeStart == true || counterBeforeStart >= (int)(delayBeforeStart * Settings.maxFps)){
            g2d.drawImage(sprite, (int)locX,(int)locY, (int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite), null);
        }
    }

    public abstract void update();

    public void SetUpCollision(){
        collision = new Rectangle((int)locX,(int)locY,(int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite));
    }
    
    public void SetSprite(String source){
        try {
			sprite = ImageIO.read(new File(source));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}

class StandartBullet extends Bullet{

    public StandartBullet(int locX, int locY, int pointX, int pointY, float lifeTime, Player player) {
        super(locX, locY, pointX, pointY, lifeTime, player);
    }

    @Override
    public void update() {
        if(counterBeforeStart >= (int)(delayBeforeStart * Settings.maxFps)){
            locX += speed * Math.cos( angle );
            locY += speed * Math.sin( angle );
            collision.x = (int)locX;
            collision.y = (int)locY;
    
            if(canDamageEnemy){
                for(Enemy enemy : GameLoop.listOfEnemies){
                    if(collision.intersects(enemy.collision)){
                        enemy.TakeDamage(damage);
                        break;
                    }
                }
            }
            if(canDamagePlayer && collision.intersects(player.collision)){
                player.TakeDamage(damage);
            }
        }
        else{
            counterBeforeStart++;
        }
    }    
}
