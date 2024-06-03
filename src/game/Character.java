package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Character{
    public float modificator_CoolDownOfSkills = 1f;
    public float modificator_Damage = 1f;
    public float modificator_AreaOfSkills= 1f;
    public float modificator_SpeedOfSkills = 1f;
    public float modificator_LifeTimeOfSkills = 1f;
    public int locX, locY;
	public BufferedImage sprite;
	public float sizeOfSprite = 1f;

    public abstract void toDraw(Graphics2D g2d);
    public abstract void update();
}