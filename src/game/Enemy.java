package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    private boolean isAttacking = false;

    private Timer timer = new Timer();

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

    abstract void SetUpOfBullet();

    public void update(){
        float deltaX = player.locX - locX;
        float deltaY = player.locY - locY;
        double angle = Math.atan2( deltaY, deltaX );
        locX += moveSpeed * Math.cos( angle );
        locY += moveSpeed * Math.sin( angle );
        curHP -= 0.1f;

        if(isRangeAttack && isAttacking == false){
            isAttacking = true;
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    try{

                        SetUpOfBullet();
                        isAttacking = false;
                    }
                    catch(IndexOutOfBoundsException e){
                        e.getStackTrace();
                    }
                }
            }, (long)(delayBtwAttacks*1000));
        }
    }

    public void Dead(){
        
    }

    private void RangeAttack(){

    }
}

class TestMob extends Enemy{
    public TestMob(Player player, int locX, int locY) {
        super(player, locX, locX);
        SetSprite("res\\Icon2.png");
    }

    void SetUpOfBullet() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'SetUpOfBullet'");
    }
    
}

class FireLizard extends Enemy{
    public FireLizard(Player player, int locX, int locY) {
        super(player, locX, locX);
        SetSprite("res\\Icon28.png");
        super.maxHP = 30f;
        super.curHP = 30f;
    }

    @Override
    void SetUpOfBullet() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'SetUpOfBullet'");
    }
    
}
class GojoSatoru extends Enemy{
    public GojoSatoru(Player player, int locX, int locY) {
        super(player, locX, locX);
        SetSprite("res\\524a84a508a5a16.png");
        super.maxHP = 100f;
        super.curHP = 100f;
        super.moveSpeed = 6f;
        super.type = TypeOfEnemy.GojoSatoru;
        super.sizeOfSprite = 0.05f;
        super.isRangeAttack = false;
    }

    void SetUpOfBullet() {
        Bullet bullet = new Bullet(locX,locY,player.locX,player.locY,1f);
        bullet.speed = 12f;
        bullet.SetSprite("res\\dcuz40l-3dfd983d-4019-482d-ac00-ba651038ef3e.png");
        bullet.sizeOfSprite = 0.04f;

        //GameLoop.listOfBullets.add(bullet);
    }
    
}

