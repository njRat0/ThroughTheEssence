package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Enemy {
    public float moveSpeed = 4;
    public float damage = 1.5f;
    public float curHP = 20;
    public float maxHP = 20;
    public int locX, locY;
    public BufferedImage sprite;

    public Player player;
    private boolean isRangeAttack = false;

    public Enemy(Player player, int locX, int locY){
        this.player = player;
        this.locX = locX;
        this.locY = locY;
    }

    public void SetSprite(String source){
        try {
			sprite = ImageIO.read(new File(source));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void update(){
        float deltaX = player.locX - locX;
        float deltaY = player.locY - locY;
        double angle = Math.atan2( deltaY, deltaX );
        locX += moveSpeed * Math.cos( angle );
        locY += moveSpeed * Math.sin( angle );
        curHP -= 0.1f;
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
    
}
