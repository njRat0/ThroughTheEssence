package game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class InteractingObject {
    public int locX, locY;
    public BufferedImage sprite;
    public float sizeOfSprite = 1f;
    public Player player;
    public Rectangle collision;

    public boolean isActivated = false;

    public InteractingObject(int locX, int locY, Player player){
        this.locX = locX;
        this.locY = locY;
        this.player = player;

        GameLoop.listOfInteractingObjects.add(this);
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

    public void toDraw(Graphics2D g2d){
        g2d.drawImage(sprite, (int)locX,(int)locY, (int)(sprite.getWidth()*sizeOfSprite),(int)(sprite.getHeight()*sizeOfSprite), null);
    }

    public abstract void update();
}

class ExpStone_lvl1 extends InteractingObject{
    private int amountExp = 1;

    public ExpStone_lvl1(int locX, int locY, Player player) {
        super(locX, locY, player);
        super.sizeOfSprite = 0.8f;
        super.SetSprite("res\\InteractingObjects\\ExpStone_lvl1.png");
        super.SetUpCollision();
    }

    public void update() {
        if(isActivated == false && collision.intersects(player.collision)){
            player.curEXP += amountExp;
            isActivated = true;
        }
    }
    
}

class ExpStone_lvl2 extends InteractingObject{
    private int amountExp = 5;

    public ExpStone_lvl2(int locX, int locY, Player player) {
        super(locX, locY, player);
        super.sizeOfSprite = 0.8f;
        super.SetSprite("res\\InteractingObjects\\ExpStone_lvl2.png");
        super.SetUpCollision();
    }

    public void update() {
        if(isActivated == false && collision.intersects(player.collision)){
            player.curEXP += amountExp;
            isActivated = true;
        }
    }
    
}

class ExpStone_lvl3 extends InteractingObject{
    private int amountExp = 20;

    public ExpStone_lvl3(int locX, int locY, Player player) {
        super(locX, locY, player);
        super.sizeOfSprite = 0.8f;
        super.SetSprite("res\\InteractingObjects\\ExpStone_lvl3.png");
        super.SetUpCollision();
    }

    public void update() {
        if(isActivated == false && collision.intersects(player.collision)){
            player.curEXP += amountExp;
            isActivated = true;
        }
    }
    
}
