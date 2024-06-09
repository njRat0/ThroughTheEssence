package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    public float sizeOfSprite = 1;
    public int levelOfSkill = 1;

    public Skill(Character focusCharacter, Character holdeCharacter , TypeOfSkill type){
        this.focusCharacter = focusCharacter;
        this.holderCharacter = holdeCharacter;
        this.type = type;
    }

    public abstract void update();

    public int numberOfUpgradePoints;
    public String nameOfChoosingParameter;
    public ArrayList<Integer> numberOfChoosedUpgrade = new ArrayList<Integer>();
    public abstract void UpgrateSkill(int point);
    public abstract boolean GetPointsOfUpgrateSkill(int point);

}

class UpgrateSkill_damage extends Skill{
    private Player player;

    public  UpgrateSkill_damage(Player player) {
        super(null, player, TypeOfSkill.passive);
        name = "+damage";
        chanceOfDrop = 15;
        numberOfUpgradePoints = 0;
        this.player = player;
    }

    @Override
    public void update() {
        player.modificator_Damage += 0.15f;
        chanceOfDrop = 15;
    }

    @Override
    public void UpgrateSkill(int point) {

    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetPointsOfUpgrateSkill'");
    }
}
class UpgrateSkill_amountOfCasts extends Skill{
    private Player player;

    public  UpgrateSkill_amountOfCasts(Player player) {
        super(null, player, TypeOfSkill.passive);
        name = "+amount of casts";
        numberOfUpgradePoints = 0;
        chanceOfDrop = 5;
        //chanceOfDrop = 15;
        this.player = player;
    }

    @Override
    public void update() {
        player.modificator_amountsOfCastSkill += 1;
        chanceOfDrop = 15;
    }

    @Override
    public void UpgrateSkill(int point) {

    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetPointsOfUpgrateSkill'");
    }
}

class UpgrateSkill_HPpoints extends Skill{
    private Player player;

    public  UpgrateSkill_HPpoints(Player player) {
        super(null, player, TypeOfSkill.passive);
        name = "+20% max hp";
        numberOfUpgradePoints = 0;
        //chanceOfDrop = 15;
        this.player = player;
    }

    @Override
    public void update() {
        player.maxHP += 20;
        player.curHP += 10;
        chanceOfDrop = 15;
    }

    @Override
    public void UpgrateSkill(int point) {

    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetPointsOfUpgrateSkill'");
    }
}

class UpgrateSkill_Speed extends Skill{
    private Player player;

    public  UpgrateSkill_Speed(Player player) {
        super(null, player, TypeOfSkill.passive);
        name = "+10% of max movement speed";
        numberOfUpgradePoints = 0;
        //chanceOfDrop = 15;
        this.player = player;
    }

    @Override
    public void update() {
        player.moveSpeed += 0.5f;
        chanceOfDrop = 15;
    }

    @Override
    public void UpgrateSkill(int point) {

    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetPointsOfUpgrateSkill'");
    }
}

class UpgrateSkill_HPregen extends Skill{
    private Player player;

    public UpgrateSkill_HPregen(Player player) {
        super(null, player, TypeOfSkill.passive);
        name = "+20% of hp regen";
        numberOfUpgradePoints = 0;
        //chanceOfDrop = 15;
        this.player = player;
    }

    @Override
    public void update() {
        player.hpRegen += 0.02f;
        chanceOfDrop = 15;
    }

    @Override
    public void UpgrateSkill(int point) {

    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetPointsOfUpgrateSkill'");
    }
}

class UpgrateSkill_ReduceCDofSkills extends Skill{
    private Player player;

    public UpgrateSkill_ReduceCDofSkills(Player player) {
        super(null, player, TypeOfSkill.passive);
        name = "-10% skill cooldown";
        numberOfUpgradePoints = 0;
        chanceOfDrop = 5;
        this.player = player;
    }

    @Override
    public void update() {
        player.modificator_CoolDownOfSkills -= 0.1;
        chanceOfDrop = 15;
    }

    @Override
    public void UpgrateSkill(int point) {

    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetPointsOfUpgrateSkill'");
    }
}
class AtomicPudge extends Skill{
    public int amountBullets = 1;
    public float sizeOfBullets = 0.1f;
    private float delayBtwCast = 20f;
    public float speed  = 2f;
    public float damage = 10f;
    public float lifeTime = 10f;
    private int counter = 0;
    public boolean canDamagePlayer = false;
    public boolean canDamageEnemy = true;
    private Random r = new Random();

    private int amountOfCast = 1;
    private int counterForSkillCast = 0;

    public AtomicPudge(Character focusCharacter, Character holderCharacter) {
        super(focusCharacter, holderCharacter, TypeOfSkill.active);
        name = "AtomicPudge";
        chanceOfDrop = 15;
        numberOfUpgradePoints = 5;
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
                CastingBullet bullet = new CastingBullet((int)(holderCharacter.locX - 8 * sizeOfBullets *2  * holderCharacter.modificator_AreaOfSkills),(int)(holderCharacter.locY - 8 * sizeOfBullets *2 * holderCharacter.modificator_AreaOfSkills), 0, 0, 8f, null);
                bullet.canDamagePlayer = false;
                bullet.canDamageEnemy = true;
                bullet.isAliveAfterDealingDamage = false;
                bullet.bullet = new PushingBullet(-5000, -5000, 0, 0, lifeTime * holderCharacter.modificator_LifeTimeOfSkills, null);
                bullet.bullet.damage = damage * holderCharacter.modificator_Damage;
                bullet.speed = speed * holderCharacter.modificator_SpeedOfSkills;
                bullet.sizeOfSprite = 1f;
                bullet.bullet.sizeOfSprite = sizeOfBullets * holderCharacter.modificator_AreaOfSkills;
                bullet.bullet.delayBtwDealingDamage = 0.1f;
                bullet.SetCustomAngle((float)(r.nextFloat() * 6.28f - 3.14f));
                bullet.SetSprite("res\\Bullets\\AtomicBomb.png");
                bullet.SetUpCollision();
                bullet.bullet.delayBeforeStart = 40f;
                bullet.bullet.isAliveAfterDealingDamage = true;
                bullet.bullet.showBeforeStart = canDamageEnemy;
                bullet.bullet.canDamagePlayer = canDamagePlayer;
                bullet.bullet.canDamageEnemy = true;
                bullet.bullet.SetSprite("res\\Effects\\pudge bombing effect.png");
                bullet.bullet.sizeOfSprite = sizeOfBullets * holderCharacter.modificator_AreaOfSkills;
                bullet.bullet.SetUpCollision();
            }
        }
        else{
            counter++;
        }
    }

    @Override
    public void UpgrateSkill(int point) {
        switch (point){
            case 1:
                amountBullets++;
                break;
            case 2:
                sizeOfBullets += 0.02f;
                break;
            case 3:
                delayBtwCast -= 2f;
                break;
            case 4:
                lifeTime += 0.5f;
                break;
            case 5:
                damage += 2.5f;
                break;
        }
    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        switch (point){
            case 1:
                nameOfChoosingParameter = "+1 amount of bullet";
                if(amountBullets >= 8){
                    return false;
                }
                break;
            case 2:
                nameOfChoosingParameter = "+20% area of skill";
                if(sizeOfBullets >= 5f){
                    return false;
                }
                break;
            case 3:
                nameOfChoosingParameter = "-10% delayBtwCast";
                if(delayBtwCast <= 5f){
                    return false;
                }
                break;
            case 4:
                nameOfChoosingParameter = "+0.5 sec duration of skill";
                if(lifeTime >= 30f){
                    return false;
                }
                break;
            case 5:
                nameOfChoosingParameter = "+25% damage of skill";
                if(damage >= 30f){
                    return false;
                }
                break;
        }
        return true;
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
    public float damage = 6f;

    private int amountOfCast = 1;
    private int counterForSkillCast = 0;

    public SplashOfFire(Character focusCharacter, Character holderCharacter) {
        super(focusCharacter, holderCharacter, TypeOfSkill.active);
        name = "SplashOfFire";
        chanceOfDrop = 15;
        numberOfUpgradePoints = 6;
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
                PushingBullet bullet = new PushingBullet(holderCharacter.locX + 12,holderCharacter.locY +  12, 0, 0, lifeTime * holderCharacter.modificator_LifeTimeOfSkills, null);
                bullet.pushingVelocity = 10f;
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

    @Override
    public void UpgrateSkill(int point) {
        switch (point){
            case 1:
                amountBullets++;
                break;
            case 2:
                sizeOfBullets += 0.2f;
                break;
            case 3:
                delayBtwCast -= 0.15f;
                break;
            case 4:
                speed += 1.2f;
                break;
            case 5:
                damage += 0.6f;
                break;
            case 6:
                amountOfCast += 1f;
                break;
        }
    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        switch (point){
            case 1:
                nameOfChoosingParameter = "+1 amount of bullet";
                if(amountBullets >= 15){
                    return false;
                }
                break;
            case 2:
                nameOfChoosingParameter = "+20% area of skill";
                if(sizeOfBullets >= 5f){
                    return false;
                }
                break;
            case 3:
                nameOfChoosingParameter = "-10% delayBtwCast";
                if(delayBtwCast <= 0.5f){
                    return false;
                }
                break;
            case 4:
                nameOfChoosingParameter = "+10% radius of skill";
                if(speed >= 24f){
                    return false;
                }
                break;
            case 5:
                nameOfChoosingParameter = "+10% damage of skill";
                if(damage >= 12f){
                    return false;
                }
                break;
            case 6:
                nameOfChoosingParameter = "+1 amount of cast of skill";
                if(amountOfCast >= 12){
                    return false;
                }
                break;
        }
        return true;
    }
}

class BlueCross extends Skill{
    public int amountBullets = 4;
    public float sizeOfBullets = 2f;
    private float delayBtwCast = 3f;
    public float speed  = 7f;
    public float damage = 1f;
    public float lifeTime = 2f;
    private int counter = 0;
    public boolean canDamagePlayer = false;
    public boolean canDamageEnemy = false;

    private int amountOfCast = 1;
    private int counterForSkillCast = 0;

    public BlueCross(Character focusCharacter, Character holderCharacter) {
        super(focusCharacter, holderCharacter, TypeOfSkill.active);
        name = "BlueCross";
        chanceOfDrop = 15;
        numberOfUpgradePoints = 7;
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
                PushingBullet bullet = new PushingBullet(holderCharacter.locX ,holderCharacter.locY, 0, 0, lifeTime * holderCharacter.modificator_LifeTimeOfSkills, null);
                bullet.pushingVelocity = -20f;
                bullet.canDamagePlayer = false;
                bullet.canDamageEnemy = true;
                bullet.damage = damage * holderCharacter.modificator_Damage;
                bullet.delayBtwDealingDamage = 0.1f;
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

    @Override
    public void UpgrateSkill(int point) {
        switch (point){
            case 1:
                amountBullets++;
                break;
            case 2:
                sizeOfBullets += 0.4f;
                break;
            case 3:
                delayBtwCast -= 0.3f;
                break;
            case 4:
                lifeTime += 0.5f;
                break;
            case 5:
                damage += 0.25f;
                break;
            case 6:
                speed += 0.7f;
                break;
            case 7:
                amountOfCast += 1f;
                break;
        }
    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        switch (point){
            case 1:
                nameOfChoosingParameter = "+1 amount of bullet";
                if(amountBullets >= 15){
                    return false;
                }
                break;
            case 2:
                nameOfChoosingParameter = "+20% area of skill";
                if(sizeOfBullets >= 8f){
                    return false;
                }
                break;
            case 3:
                nameOfChoosingParameter = "-10% delayBtwCast";
                if(delayBtwCast <= 0.1f){
                    return false;
                }
                break;
            case 4:
                nameOfChoosingParameter = "+0.5 sec duration of skill";
                if(lifeTime >= 6f){
                    return false;
                }
                break;
            case 5:
                nameOfChoosingParameter = "+25% damage of skill";
                if(damage >= 10f){
                    return false;
                }
                break;
            case 6:
                nameOfChoosingParameter = "+10 speed of skill";
                if(speed >= 14f){
                    return false;
                }
                break;
            case 7:
                nameOfChoosingParameter = "+1 amount of cast of skill";
                if(amountOfCast >= 8){
                    return false;
                }
                break;
        }
        return true;
    }
}
class RotatingDiscs extends Skill{
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

    private Player player;

    public RotatingDiscs(Character focusCharacter, Player holderCharacter) {
        super(focusCharacter, holderCharacter, TypeOfSkill.active);
        player = holderCharacter;
        name = "RotatingDiscks";
        chanceOfDrop = 500;
        numberOfUpgradePoints = 7;
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
                LugartBullet bullet = new LugartBullet(holderCharacter.locX, holderCharacter.locY, 0, 0, lifeTime * holderCharacter.modificator_LifeTimeOfSkills, player);
                bullet.isRotatingBullet = true;
                bullet.SetCustomAngle((float)(i * 6.28f / amountBullets - 3.14));
                bullet.addAnglePerTick = 0.2f * speed / 7 ;
                bullet.sizeOfSprite = sizeOfBullets * holderCharacter.modificator_AreaOfSkills;
                bullet.SetSprite("res\\Bullets\\BlueDisk.png");
                bullet.speed = speed * holderCharacter.modificator_SpeedOfSkills;
                bullet.damage = damage * holderCharacter.modificator_Damage;
                bullet.canDamagePlayer = false;
                bullet.canDamageEnemy = true;
                bullet.delayBtwDealingDamage = 0.05f;
                bullet.isAliveAfterDealingDamage = true;
                bullet.SetUpCollision();

                bullet.bullet = new StandartBullet(-5000, -5000, 0,0,lifeTime * holderCharacter.modificator_LifeTimeOfSkills * 2, null);
                bullet.bullet.delayBtwDealingDamage = 0.05f;
                bullet.bullet.sizeOfSprite = sizeOfBullets * holderCharacter.modificator_AreaOfSkills;
                bullet.bullet.SetSprite("res\\Bullets\\BlueDisk.png");
                bullet.bullet.speed = speed * holderCharacter.modificator_SpeedOfSkills;
                bullet.bullet.damage = damage * holderCharacter.modificator_Damage;
                bullet.bullet.canDamagePlayer = false;
                bullet.bullet.canDamageEnemy = true;
                bullet.bullet.showBeforeStart = false;
                bullet.bullet.isAliveAfterDealingDamage = true;
                bullet.bullet.SetUpCollision();  
            }
        }
        else{
            counter++;
        }
    }

    @Override
    public void UpgrateSkill(int point) {
        switch (point){
            case 1:
                amountBullets++;
                break;
            case 2:
                sizeOfBullets += 0.4f;
                break;
            case 3:
                delayBtwCast -= 0.3f;
                break;
            case 4:
                lifeTime += 0.5f;
                break;
            case 5:
                damage += 0.5f;
                break;
            case 6:
                speed += 0.7f;
                break;
            case 7:
                amountOfCast += 1f;
                break;
        }
    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        switch (point){
            case 1:
                nameOfChoosingParameter = "+1 amount of bullet";
                if(amountBullets >= 15){
                    return false;
                }
                break;
            case 2:
                nameOfChoosingParameter = "+20% area of skill";
                if(sizeOfBullets >= 8f){
                    return false;
                }
                break;
            case 3:
                nameOfChoosingParameter = "-10% delayBtwCast";
                if(delayBtwCast <= 0.1f){
                    return false;
                }
                break;
            case 4:
                nameOfChoosingParameter = "+0.5 sec duration of skill";
                if(lifeTime >= 6f){
                    return false;
                }
                break;
            case 5:
                nameOfChoosingParameter = "+25% damage of skill";
                if(damage >= 20f){
                    return false;
                }
                break;
            case 6:
                nameOfChoosingParameter = "+10 speed of skill";
                if(speed >= 14f){
                    return false;
                }
                break;
            case 7:
                nameOfChoosingParameter = "+1 amount of cast of skill";
                if(amountOfCast >= 8){
                    return false;
                }
                break;
        }
        return true;
    }
}

class FireGun extends Skill{
    public int amountBullets = 3;
    public float sizeOfBullets = 1f;
    private float delayBtwCast = 0.3f;
    public float lifeTime = 2f;
    public float damage = 1f;
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
        numberOfUpgradePoints = 7;

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
                    StandartBullet bullet = new StandartBullet(player.locX+12, player.locY+12, player.mouseX, player.mouseY, lifeTime * player.modificator_LifeTimeOfSkills, player);
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
                    bullet.sizeOfSprite = sizeOfBullets * holderCharacter.modificator_AreaOfSkills;
                    bullet.SetUpCollision();
                }
            }         
        }
        else{
            counter++;
        }
    }

    @Override
    public void UpgrateSkill(int point) {
        switch (point){
            case 1:
                amountBullets++;
                break;
            case 2:
                sizeOfBullets += 0.2f;
                break;
            case 3:
                delayBtwCast -= 0.03f;
                break;
            case 4:
                lifeTime += 0.5f;
                break;
            case 5:
                damage += 0.5f;
                break;
            case 6:
                speed += 0.7f;
                break;
            case 7:
                amountOfCast += 1f;
                break;
        }
    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        switch (point){
            case 1:
                nameOfChoosingParameter = "+1 amount of bullet";
                if(amountBullets >= 15){
                    return false;
                }
                break;
            case 2:
                nameOfChoosingParameter = "+20% area of skill";
                if(sizeOfBullets >= 5f){
                    return false;
                }
                break;
            case 3:
                nameOfChoosingParameter = "-10% delayBtwCast";
                if(delayBtwCast <= 0.1f){
                    return false;
                }
                break;
            case 4:
                nameOfChoosingParameter = "+0.5 sec duration of skill";
                if(lifeTime >= 6f){
                    return false;
                }
                break;
            case 5:
                nameOfChoosingParameter = "+50% damage of skill";
                if(damage >= 10f){
                    return false;
                }
                break;
            case 6:
                nameOfChoosingParameter = "+10 speed of skill";
                if(speed >= 10f){
                    return false;
                }
                break;
            case 7:
                nameOfChoosingParameter = "+1 amount of cast of skill";
                if(amountOfCast >= 12){
                    return false;
                }
                break;
        }
        return true;
    }
}

//weapon >>>>
class BasicGun extends Skill{
    public int amountBullets = 1;
    public float sizeOfBullets = 1f;
    private float delayBtwCast = 0.5f;
    public float lifeTime = 1f;
    public float damage = 4f;
    public float speed  = 28f;
    private int counter = 0;
    //private float dispersion = 0.14f;
    private Random r = new Random();

    private Player player;

    public int amountOfCast = 1;
    private int counterForSkillCast = 0;

    public BasicGun(Player player) {
        super(null, player, TypeOfSkill.weapon);
        this.player = player;
        name = "BasicGun";
        chanceOfDrop = 15;
        numberOfUpgradePoints = 7;

        try {
			sprite = ImageIO.read(new File("res\\GunsAssets\\1px\\17b.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        sizeOfSprite = 2f;
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
                    //bullet.AddAngle((float)((r.nextFloat() - 0.5) * dispersion * 2));
                    bullet.sizeOfSprite = sizeOfBullets * holderCharacter.modificator_AreaOfSkills;
                    bullet.SetSprite("res\\Bullets\\BasicGun_bullet.png");
                    bullet.speed = speed * player.modificator_SpeedOfSkills * (float)(r.nextFloat() *0.2f + 0.4f);
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

    @Override
    public void UpgrateSkill(int point) {
        switch (point){
            case 1:
                amountBullets++;
                break;
            case 2:
                sizeOfBullets += 0.2f;
                break;
            case 3:
                delayBtwCast -= 0.05f;
                break;
            case 4:
                lifeTime += 0.5f;
                break;
            case 5:
                damage += 2f;
                break;
            case 6:
                speed += 2.8f;
                break;
            case 7:
                amountOfCast += 1f;
                break;
        }
    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        switch (point){
            case 1:
                nameOfChoosingParameter = "+1 amount of bullet";
                if(amountBullets >= 6){
                    return false;
                }
                break;
            case 2:
                nameOfChoosingParameter = "+20% area of skill";
                if(sizeOfBullets >= 5f){
                    return false;
                }
                break;
            case 3:
                nameOfChoosingParameter = "-10% delayBtwCast";
                if(delayBtwCast <= 0.1f){
                    return false;
                }
                break;
            case 4:
                nameOfChoosingParameter = "+0.5 sec duration of skill";
                if(lifeTime >= 6f){
                    return false;
                }
                break;
            case 5:
                nameOfChoosingParameter = "+50% damage of skill";
                if(damage >= 10f){
                    return false;
                }
                break;
            case 6:
                nameOfChoosingParameter = "+10 speed of skill";
                if(speed >= 10f){
                    return false;
                }
                break;
            case 7:
                nameOfChoosingParameter = "+1 amount of cast of skill";
                if(amountOfCast >= 6){
                    return false;
                }
                break;
        }
        return true;
    }
}

class IonRed_Gun extends Skill{
    public int amountBullets = 1;
    public float sizeOfBullets = 0.7f;
    private float delayBtwCast = 0.1f;
    public float lifeTime = 0.5f;
    public float damage = 0.4f;
    public float speed  = 35f;
    private int counter = 0;
    //private float dispersion = 0.14f;
    private Random r = new Random();

    private Player player;

    public int amountOfCast = 1;
    private int counterForSkillCast = 0;

    public IonRed_Gun(Player player) {
        super(null, player, TypeOfSkill.weapon);
        this.player = player;
        name = "Ion RedGun";
        chanceOfDrop = 15;
        numberOfUpgradePoints = 5;

        try {
			sprite = ImageIO.read(new File("res\\GunsAssets\\1px\\29.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        sizeOfSprite = 2f;
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
                    CastingBullet bullet = new CastingBullet(player.locX, player.locY, player.mouseX, player.mouseY, lifeTime * player.modificator_LifeTimeOfSkills, player);
                    //bullet.AddAngle((float)((r.nextFloat() - 0.5) * dispersion * 2));
                    bullet.sizeOfSprite = sizeOfBullets * holderCharacter.modificator_AreaOfSkills;
                    bullet.SetSprite("res\\Bullets\\RedDisk.png");
                    bullet.speed = speed * player.modificator_SpeedOfSkills * (float)(r.nextFloat() *0.2f + 0.4f);
                    // bullet.AddAngle(r.nextFloat(-dispersion,dispersion));
                    // bullet.SetSprite("res\\Effects\\FireEffect.png");
                    // bullet.speed = speed * player.modificator_SpeedOfSkills * r.nextFloat(0.8f, 1.2f);
                    bullet.damage = damage * player.modificator_Damage;
                    bullet.canDamagePlayer = false;
                    bullet.canDamageEnemy = true;
                    bullet.delayBtwDealingDamage = 0.05f;
                    bullet.isAliveAfterDealingDamage = true;
                    bullet.SetUpCollision();

                    bullet.bullet = new StandartBullet(-5000, -5000, player.locX, player.locY,lifeTime * player.modificator_LifeTimeOfSkills * 2, player);
                    bullet.bullet.delayBtwDealingDamage = 0.05f;
                    bullet.bullet.sizeOfSprite = sizeOfBullets * holderCharacter.modificator_AreaOfSkills;
                    bullet.bullet.SetSprite("res\\Bullets\\RedDisk.png");
                    bullet.bullet.speed = speed * player.modificator_SpeedOfSkills * (float)(r.nextFloat() *0.2f + 0.4f);
                    bullet.bullet.damage = damage * player.modificator_Damage;
                    bullet.bullet.canDamagePlayer = false;
                    bullet.bullet.canDamageEnemy = true;
                    bullet.bullet.showBeforeStart = false;
                    bullet.bullet.isAliveAfterDealingDamage = true;
                    bullet.bullet.SetUpCollision();  
                }
            }         
        }
        else{
            counter++;
        }
    }

    @Override
    public void UpgrateSkill(int point) {
        switch (point){
            case 1:
                amountBullets++;
                break;
            case 2:
                sizeOfBullets += 0.14f;
                break;
            case 3:
                lifeTime += 0.25f;
                break;
            case 4:
                damage += 0.2f;
                break;
            case 5:
                speed += 3.5f;
                break;
        }
    }

    @Override
    public boolean GetPointsOfUpgrateSkill(int point) {
        switch (point){
            case 1:
                nameOfChoosingParameter = "+1 amount of bullet";
                if(amountBullets >= 6){
                    return false;
                }
                break;
            case 2:
                nameOfChoosingParameter = "+20% area of skill";
                if(sizeOfBullets >= 5f){
                    return false;
                }
                break;
            case 3:
                nameOfChoosingParameter = "+50% lifetime";
                if(lifeTime >= 6f){
                    return false;
                }
                break;
            case 4:
                nameOfChoosingParameter = "+50% damage of skill";
                if(damage >= 10f){
                    return false;
                }
                break;
            case 5:
                nameOfChoosingParameter = "+10 speed of skill";
                if(speed >= 50f){
                    return false;
                }
                break;
        }
        return true;
    }
}

