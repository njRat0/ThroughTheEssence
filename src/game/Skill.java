package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

enum TypeOfSkill{
    passive,
    active,
    weapon
}

public abstract class Skill {
    public Character focusCharacter;
    public Character holderCharacter;
    public TypeOfSkill type;
    public BufferedImage sprite;
    public String name = "null";
    public int chanceOfDrop = 15;

    public Skill(Character focusCharacter, Character holdeCharacter , TypeOfSkill type){
        this.focusCharacter = focusCharacter;
        this.holderCharacter = holdeCharacter;
        this.type = type;
    }

    public abstract void update();
}

class UpgrateSkill_damage extends Skill{
    private Player player;

    public  UpgrateSkill_damage(Player player) {
        super(null, player, TypeOfSkill.passive);
        name = "+damage";
        chanceOfDrop = 15;
        this.player = player;
    }

    @Override
    public void update() {
        player.modificator_Damage += 0.15f;
    }
}
class UpgrateSkill_amountOfCasts extends Skill{
    private Player player;

    public  UpgrateSkill_amountOfCasts(Player player) {
        super(null, player, TypeOfSkill.passive);
        name = "+amount of casts";
        chanceOfDrop = 15;
        this.player = player;
    }

    @Override
    public void update() {
        player.modificator_amountsOfCastSkill += 1;
    }
}

class SplashOfFire extends Skill{
    public int amountBullets = 14;
    public float speed  = 12f;
    private float delayBtwCast = 1.5f;
    public float sizeOfBullets = 1f;
    public float lifeTime = 0.1f;
    private int counter = 0;
    public boolean canDamagePlayer = false;
    public boolean canDamageEnemy = false;
    public float damage = 12f;

    private int amountOfCast = 1;
    private int counterForSkillCast = 0;

    public SplashOfFire(Character focusCharacter, Character holderCharacter) {
        super(focusCharacter, holderCharacter, TypeOfSkill.active);
        name = "SplashOfFire";
        chanceOfDrop = 15;
    }

    @Override
    public void update() {
        if(counter >= (int)(delayBtwCast * Settings.maxFps * holderCharacter.modificator_CoolDownOfSkills)){
            counterForSkillCast++;
            if(counterForSkillCast == amountOfCast + holderCharacter.modificator_amountsOfCastSkill){
                counter =0 ;
                counterForSkillCast = 0;
            }
            
            for(int i = 0; i < amountBullets; i++){
                PushingBullet bullet = new PushingBullet((int)(holderCharacter.locX - 8 * sizeOfBullets * holderCharacter.modificator_AreaOfSkills),(int)(holderCharacter.locY - 8 * sizeOfBullets * holderCharacter.modificator_AreaOfSkills), 0, 0, lifeTime * holderCharacter.modificator_LifeTimeOfSkills, null, 10f);
                bullet.canDamagePlayer = false;
                bullet.canDamageEnemy = true;
                bullet.damage = damage * holderCharacter.modificator_Damage;
                bullet.speed = speed * holderCharacter.modificator_SpeedOfSkills;
                bullet.sizeOfSprite = sizeOfBullets * holderCharacter.modificator_AreaOfSkills;
                bullet.SetCustomAngle((float)(i * 6.28f / amountBullets - 3.14));
                bullet.SetSprite("res\\Effects\\FireEffect.png");
                bullet.SetUpCollision();
            }
        }
        else{
            counter++;
        }
    }
}

class BlueCross extends Skill{
    public int amountBullets = 4;
    public float sizeOfBullets = 2f;
    private float delayBtwCast = 3f;
    public float speed  = 7f;
    public float damage = 2f;
    public float lifeTime = 2f;
    private int counter = 0;
    public boolean canDamagePlayer = false;
    public boolean canDamageEnemy = false;

    private int amountOfCast = 1;
    private int counterForSkillCast = 0;

    public BlueCross(Character focusCharacter, Character holderCharacter) {
        super(focusCharacter, holderCharacter, TypeOfSkill.active);
        name = "BlueCross";
        chanceOfDrop = 5;
    }

    @Override
    public void update() {
        if(counter >= (int)(delayBtwCast * Settings.maxFps * holderCharacter.modificator_CoolDownOfSkills)){
            counterForSkillCast++;
            if(counterForSkillCast == amountOfCast + holderCharacter.modificator_amountsOfCastSkill){
                counter =0 ;
                counterForSkillCast = 0;
            }
            for(int i = 0; i < amountBullets; i++){
                PushingBullet bullet = new PushingBullet((int)(holderCharacter.locX - 8 * sizeOfBullets *2  * holderCharacter.modificator_AreaOfSkills),(int)(holderCharacter.locY - 8 * sizeOfBullets *2 * holderCharacter.modificator_AreaOfSkills), 0, 0, lifeTime * holderCharacter.modificator_LifeTimeOfSkills, null, -20f);
                bullet.canDamagePlayer = false;
                bullet.canDamageEnemy = true;
                bullet.damage = damage * holderCharacter.modificator_Damage;
                bullet.speed = speed * holderCharacter.modificator_SpeedOfSkills;
                bullet.sizeOfSprite = sizeOfBullets * holderCharacter.modificator_AreaOfSkills;
                //bullet.delayBtwDealingDamage = 0.1f;
                bullet.SetCustomAngle((float)(i * 6.28f / amountBullets - 3.14));
                bullet.SetSprite("res\\Bullets\\GojoSatoru(BLUE).png");
                bullet.SetUpCollision();
            }
        }
        else{
            counter++;
        }
    }
}

class FireGun extends Skill{
    public int amountBullets = 3;
    public float sizeOfBullets = 2f;
    private float delayBtwCast = 0.3f;
    public float lifeTime = 2f;
    public float damage = 2f;
    public float speed  = 7f;
    private int counter = 0;
    private float dispersion = 0.14f;
    private Random r = new Random();

    private Player player;

    public int amountOfCast = 1;
    private int counterForSkillCast = 0;

    public FireGun(Player player) {
        super(null, player, TypeOfSkill.weapon);
        this.player = player;
        name = "FireGun";
        chanceOfDrop = 15;

        try {
			sprite = ImageIO.read(new File("res\\GunsAssets\\1px\\1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void update() {
        if(counter >= (int)(delayBtwCast * Settings.maxFps * holderCharacter.modificator_CoolDownOfSkills)){
            if(player.mousePress){
                counterForSkillCast++;
                if(counterForSkillCast == amountOfCast + holderCharacter.modificator_amountsOfCastSkill){
                    counter =0 ;
                    counterForSkillCast = 0;
                }
                for(int i = 0; i < amountBullets; i++){
                    StandartBullet bullet = new StandartBullet(player.locX, player.locY, player.mouseX, player.mouseY, lifeTime * player.modificator_LifeTimeOfSkills, player);
                    bullet.AddAngle((float)((r.nextFloat() - 0.5) * dispersion * 2));
                    bullet.SetSprite("res\\Effects\\FireEffect.png");
                    bullet.speed = speed * player.modificator_SpeedOfSkills * (float)(r.nextFloat() *0.4f + 0.8f);
                    // bullet.AddAngle(r.nextFloat(-dispersion,dispersion));
                    // bullet.SetSprite("res\\Effects\\FireEffect.png");
                    // bullet.speed = speed * player.modificator_SpeedOfSkills * r.nextFloat(0.8f, 1.2f);
                    bullet.damage = damage * player.modificator_Damage;
                    bullet.canDamagePlayer = false;
                    bullet.canDamageEnemy = true;
                    bullet.isAliveAfterDealingDamage = false;
                    bullet.SetUpCollision();
                }
            }         
        }
        else{
            counter++;
        }
    }
}

