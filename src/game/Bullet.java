package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet {
    public BufferedImage sprite;
    public int locX, locY;
    private int velocityX; private int pointY;
    private float damage;
    private float speed;

    public Bullet(int pointX, int pointY, float damage, float speed){
        this.damage = damage;
        this.speed = speed;

        // float deltaX = player.locX - locX;
        // float deltaY = player.locY - locY;
        // double angle = Math.atan2( deltaY, deltaX );
        // locX += moveSpeed * Math.cos( angle );
        // locY += moveSpeed * Math.sin( angle );
    }

    public void update(){

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
