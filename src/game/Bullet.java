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
    public Bullet bullet;
    public float locX, locY;
    // private double velocityX; private double velocityY;
    public float damage = 1f;
    public float speed;
    public float sizeOfSprite = 1f;
    public float lifeTime = 3f;
    public boolean isEnd = false;
    public double angle;
    
    public float delayBeforeStart = 0;
    public int counterBeforeStart =0;
    public boolean showBeforeStart = true;
    public float delayBtwDealingDamage = 0;
    public int counterBtwDealingDamage =0;
    public boolean isAliveAfterDealingDamage = true;

    public boolean isRotatable = false;
    public boolean isRotatingBullet = false;
    public float addAnglePerTick = 0.04f;

    public boolean hasAdditionalVampirism = false;
    public float amountAdditionalVampirism = 0.05f;

    public Player player;

    public Rectangle collision;

    public boolean stayInTheEndPosition = false;

    public boolean canDamagePlayer = true;
    public boolean canDamageEnemy = false;

    public float additionalAngle = 0f;

    public Timer timer = new Timer();

    public int pointX, pointY;

    public Bullet(int locX,int locY,int pointX, int pointY, float lifeTime , Player player){
        this.lifeTime = lifeTime;
        this.locX = locX;
        this.locY = locY;
        this.player = player;
        this.pointX = pointX;
        this.pointY = pointY;
        
        float deltaX = pointX - locX;
        float deltaY = pointY - locY;
        angle = Math.atan2( deltaY, deltaX );
        //System.out.println(angle);

        GameLoop.listOfBullets.add(this);        

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try{                    
                    isEnd = true;
                    update();
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
            if(isRotatable == true){
                g2d.rotate(angle, locX,locY);
                g2d.drawImage(sprite, (int)locX,(int)locY, (int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite), null);
                g2d.rotate(-angle, locX,locY);
            }
            else{
                g2d.drawImage(sprite, (int)locX,(int)locY, (int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite), null);
            }
        }
    }

    public abstract void update();

    public void SetUpCollision(){
        collision = new Rectangle((int)locX,(int)locY,(int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite));
    }
    
    public void SetSprite(String source){
        try {
			sprite = ImageIO.read(new File(source));
            locX -= sprite.getWidth() * sizeOfSprite / 4;
            locY -= sprite.getHeight() * sizeOfSprite / 4;
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
        if(counterBeforeStart >= (int)(delayBeforeStart * Settings.maxFps) && isEnd == false){
            if(isRotatingBullet){
                angle+= addAnglePerTick;
                if(angle >= 3.14f){
                    angle -= 6.28f;
                }
            }
            locX += speed * Math.cos( angle );
            locY += speed * Math.sin( angle );
            collision.x = (int)locX;
            collision.y = (int)locY;
            
            if(counterBtwDealingDamage >= (int)(delayBtwDealingDamage * Settings.maxFps)){
                counterBtwDealingDamage = 0;
                if(canDamageEnemy){
                    for(Enemy enemy : GameLoop.listOfEnemies){
                        if(collision.intersects(enemy.collision)){
                            enemy.TakeDamage(damage);
                            if(hasAdditionalVampirism == true){
                                player.TakeHeale(damage * amountAdditionalVampirism);
                            }
                            if(isAliveAfterDealingDamage == false){
                                isEnd = true;
                            }
                            //break;
                        }
                    }
                }
                if(canDamagePlayer && collision.intersects(player.collision)){
                    player.TakeDamage(damage);
                    if(isAliveAfterDealingDamage == false){
                        isEnd = true;
                    }
                }
            }
            else{
                counterBtwDealingDamage++;
            }
        }
        else{
            counterBeforeStart++;
        }
    }  
}    


class PushingBullet extends Bullet{
    public float pushingVelocity = 1f;
    public PushingBullet(int locX, int locY, int pointX, int pointY, float lifeTime, Player player) {
        super(locX, locY, pointX, pointY, lifeTime, player);
    }

    @Override
    public void update() {
        if(counterBeforeStart >= (int)(delayBeforeStart * Settings.maxFps) && isEnd == false){
            if(super.isRotatingBullet){
                angle+= addAnglePerTick;
                if(angle >= 3.14f){
                    angle -= 6.28f;
                }
            }

            locX += speed * Math.cos( angle );
            locY += speed * Math.sin( angle );
            collision.x = (int)locX;
            collision.y = (int)locY;
            
            if(counterBtwDealingDamage >= (int)(delayBtwDealingDamage * Settings.maxFps)){
                counterBtwDealingDamage = 0;
                if(canDamageEnemy){
                    for(Enemy enemy : GameLoop.listOfEnemies){
                        if(collision.intersects(enemy.collision)){
                            if(enemy.canBePushed == true){
                                float deltaX = enemy.locX - locX;
                                float deltaY = enemy.locY - locY;
                                double angle = Math.atan2( deltaY, deltaX );
                                enemy.locX += pushingVelocity * Math.cos( angle );
                                enemy.locY += pushingVelocity * Math.sin( angle );
                            }
                            enemy.TakeDamage(damage);
                            if(hasAdditionalVampirism == true){
                                player.TakeHeale(damage * amountAdditionalVampirism);
                            }
                            if(isAliveAfterDealingDamage == false){
                                isEnd = true;
                            }
                            //break;
                        }
                    }
                }
                if(canDamagePlayer && collision.intersects(player.collision)){
                    if(player.canBePushed == true){
                        float deltaX = player.locX - locX;
                        float deltaY = player.locY - locY;
                        double angle = Math.atan2( deltaY, deltaX );
                        player.locX += pushingVelocity* Math.cos( angle );
                        player.locY += pushingVelocity * Math.sin( angle );
                    }   
                    player.TakeDamage(damage);
                    if(isAliveAfterDealingDamage == false){
                        isEnd = true;
                    }
                }
            }
            else{
                counterBtwDealingDamage++;
            }
        }
        else{
            counterBeforeStart++;
        }
    }    
}

class CastingBullet extends Bullet{
    public CastingBullet(int locX, int locY, int pointX, int pointY, float lifeTime, Player player) {
        super(locX, locY, pointX, pointY, lifeTime, player);
    }

    @Override
    public void update() {
        if(isEnd){
            CastBullet();
        }
        if(counterBeforeStart >= (int)(delayBeforeStart * Settings.maxFps) && isEnd == false){
            if(super.isRotatingBullet){
                angle+= addAnglePerTick;
                if(angle >= 3.14f){
                    angle -= 6.28f;
                }
            }

            locX += speed * Math.cos( angle );
            locY += speed * Math.sin( angle );
            collision.x = (int)locX;
            collision.y = (int)locY;
            
            if(counterBtwDealingDamage >= (int)(delayBtwDealingDamage * Settings.maxFps)){
                counterBtwDealingDamage = 0;
                if(canDamageEnemy){
                    for(Enemy enemy : GameLoop.listOfEnemies){
                        if(collision.intersects(enemy.collision)){
                            enemy.TakeDamage(damage);
                            if(hasAdditionalVampirism == true){
                                player.TakeHeale(damage * amountAdditionalVampirism);
                            }
                            if(isAliveAfterDealingDamage == false){
                                isEnd = true;
                                CastBullet();
                            }
                            //break;
                        }
                    }
                }
                if(canDamagePlayer && collision.intersects(player.collision)){
                    player.TakeDamage(damage);
                    if(isAliveAfterDealingDamage == false){
                        isEnd = true;
                        CastBullet();
                    }
                }
            }
            else{
                counterBtwDealingDamage++;
            }
        }
        else{
            counterBeforeStart++;
        }
    } 

    public void CastBullet(){
        bullet.locX = locX - bullet.sprite.getWidth() * bullet.sizeOfSprite / 2;
        bullet.locY = locY - bullet.sprite.getHeight() * bullet.sizeOfSprite / 2;
        float deltaX = bullet.pointX - locX;
        float deltaY = bullet.pointY - locY;
        bullet.angle = Math.atan2( deltaY, deltaX );
        bullet.delayBeforeStart = 0;
    }
}    

class LugartBullet extends Bullet{
    public LugartBullet(int locX, int locY, int pointX, int pointY, float lifeTime, Player player) {
        super(locX, locY, pointX, pointY, lifeTime, player);
    }

    @Override
    public void update() {
        if(isEnd){
            CastBullet();
        }
        if(counterBeforeStart >= (int)(delayBeforeStart * Settings.maxFps) && isEnd == false){
            if(super.isRotatingBullet){
                angle+= addAnglePerTick;
                if(angle >= 3.14f){
                    angle -= 6.28f;
                }
            }
            locX += speed * Math.cos( angle );
            locY += speed * Math.sin( angle );
            collision.x = (int)locX;
            collision.y = (int)locY;
            
            if(counterBtwDealingDamage >= (int)(delayBtwDealingDamage * Settings.maxFps)){
                counterBtwDealingDamage = 0;
                if(canDamageEnemy){
                    for(Enemy enemy : GameLoop.listOfEnemies){
                        if(collision.intersects(enemy.collision)){
                            enemy.TakeDamage(damage);
                            if(hasAdditionalVampirism == true){
                                player.TakeHeale(damage * amountAdditionalVampirism);
                            }
                            if(isAliveAfterDealingDamage == false){
                                isEnd = true;
                                CastBullet();
                            }
                            //break;
                        }
                    }
                }
                if(canDamagePlayer && collision.intersects(player.collision)){
                    player.TakeDamage(damage);
                    if(super.isAliveAfterDealingDamage == false){
                        isEnd = true;
                        CastBullet();
                    }
                }
            }
            else{
                counterBtwDealingDamage++;
            }
        }
        else{
            counterBeforeStart++;
        }
    } 

    public void CastBullet(){
        bullet.locX = locX - bullet.sprite.getWidth() * bullet.sizeOfSprite / 2;
        bullet.locY = locY - bullet.sprite.getHeight() * bullet.sizeOfSprite / 2;
        float deltaX = player.locX - locX;
        float deltaY = player.locY - locY;
        bullet.angle = Math.atan2( deltaY, deltaX );
        bullet.delayBeforeStart = 0;
    }
}

class SpiderWeb extends Bullet{

    public SpiderWeb(int locX, int locY, int pointX, int pointY, float lifeTime, Player player) {
        super(locX, locY, pointX, pointY, lifeTime, player);
    }

    @Override
    public void update() {
        super.collision.x = (int)locX;
        super.collision.y = (int)locY;
        if(counterBeforeStart >= (int)(delayBeforeStart * Settings.maxFps) && isEnd == false){
            if(collision.intersects(player.collision)){
                //System.out.println("works");
                player.TakeDamage(damage);
                if(isAliveAfterDealingDamage == false){
                    isEnd = true;
                }
                player.isSlowed = true;
            }
        }
        else{
            counterBeforeStart++;
        }
    }  
} 

class FollowingBullet extends Bullet{
    public int xOfset = 14;
    public int yOfset = 14;
    public boolean isMomental = false;
    public FollowingBullet(int locX, int locY, int pointX, int pointY, float lifeTime, Player player) {
        super(locX, locY, pointX, pointY, lifeTime, player);
    }

    @Override
    public void update() {
        if(counterBeforeStart >= (int)(delayBeforeStart * Settings.maxFps) && isEnd == false){
            if(isRotatingBullet){
                angle+= addAnglePerTick;
                if(angle >= 3.14f){
                    angle -= 6.28f;
                }
            }
            if(isMomental == true){
                locX = player.locX - xOfset * sizeOfSprite;
                locY = player.locY - yOfset * sizeOfSprite;
            }
            else{
                float deltaX = locX- player.locX;
                float deltaY = locY- player.locY;
                bullet.angle = Math.atan2( deltaY, deltaX );
                locX += speed * Math.cos( angle );
                locY += speed * Math.sin( angle );
            }
            collision.x = (int)locX;
            collision.y = (int)locY;
            if(counterBtwDealingDamage >= (int)(delayBtwDealingDamage * Settings.maxFps)){
                counterBtwDealingDamage = 0;
                if(canDamageEnemy){
                    for(Enemy enemy : GameLoop.listOfEnemies){
                        if(collision.intersects(enemy.collision)){
                            enemy.TakeDamage(damage);
                            if(hasAdditionalVampirism == true){
                                player.TakeHeale(damage * amountAdditionalVampirism);
                            }
                        
                            if(isAliveAfterDealingDamage == false){
                                isEnd = true;
                            }
                            //break;
                        }
                    }
                }
                if(canDamagePlayer && collision.intersects(player.collision)){
                    player.TakeDamage(damage);
                    if(isAliveAfterDealingDamage == false){
                        isEnd = true;
                    }
                }
            }
            else{
                counterBtwDealingDamage++;
            }
        }
        else{
            counterBeforeStart++;
        }
    }  
}    