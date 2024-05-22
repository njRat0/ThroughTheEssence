package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class Bullet {
    private BufferedImage sprite;
    private int locX, locY;
    private double velocityX; private double velocityY;
    public float damage;
    public float speed;
    public float sizeOfSprite = 1f;
    public float lifeTime = 3f;
    private int id;

    private Timer timer = new Timer();

    public Bullet(int locX,int locY,int pointX, int pointY, float lifeTime){
        this.lifeTime = lifeTime;
        this.locX = locX;
        this.locY = locY;

        float deltaX = pointX - locX;
        float deltaY = pointY - locY;
        double angle = Math.atan2( deltaY, deltaX );
        velocityX = speed * Math.cos( angle );
        velocityY = speed * Math.sin( angle );

        id = GameLoop.listOfBullets.size();
        GameLoop.listOfBullets.add(this);
        

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try{
                    GameLoop.listOfBullets.remove(id);
                }
                catch(ArrayIndexOutOfBoundsException e){
                    e.getStackTrace();
                }
            }
        }, (long)(lifeTime*1000));
    }

    public void toDraw(Graphics2D g2d){
        g2d.drawImage(sprite, locX,locY, (int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite), null);
    }

    public void update(){
        locX+=velocityX;
        locY+=velocityY;
    }

    
    
    public void SetSprite(String source){
        try {
			sprite = ImageIO.read(new File(source));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
