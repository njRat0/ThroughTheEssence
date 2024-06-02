package game;

enum TypeOfSkill{
    passive,
    active,
    weapon
}

public abstract class Skill {
    public Character focusCharacter;
    public Character holderCharacter;
    public TypeOfSkill type;

    public Skill(Character focusCharacter, Character holdeCharacter , TypeOfSkill type){
        this.focusCharacter = focusCharacter;
        this.holderCharacter = holdeCharacter;
        this.type = type;
    }

    public abstract void update();
}

class SplashOfFire extends Skill{
    public int amountBullets = 14;
    private float delayBtwCast = 1.5f;
    private int counter = 0;
    public boolean canDamagePlayer = false;
    public boolean canDamageEnemy = false;
    public float damage = 12f;

    public SplashOfFire(Character focusCharacter, Character holderCharacter) {
        super(focusCharacter, holderCharacter, TypeOfSkill.active);
    }

    @Override
    public void update() {
        if(counter >= (int)(delayBtwCast * Settings.maxFps * holderCharacter.modificator_CoolDownOfSkills)){
            counter =0 ;
            for(int i = 0; i < amountBullets; i++){
                PushingBullet bullet = new PushingBullet((int)(holderCharacter.locX - holderCharacter.sprite.getWidth() / 4 * holderCharacter.sizeOfSprite), (int)(holderCharacter.locY - holderCharacter.sprite.getHeight() / 4 * holderCharacter.sizeOfSprite), 0, 0, 0.1f, null, 10f);
                bullet.canDamagePlayer = false;
                bullet.canDamageEnemy = true;
                bullet.damage = damage * holderCharacter.modificator_Damage;
                bullet.speed = 12f;
                bullet.sizeOfSprite = 1f;
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
    private float delayBtwCast = 3f;
    public float damage = 2f;
    private int counter = 0;
    public boolean canDamagePlayer = false;
    public boolean canDamageEnemy = false;

    public BlueCross(Character focusCharacter, Character holderCharacter) {
        super(focusCharacter, holderCharacter, TypeOfSkill.active);
    }

    @Override
    public void update() {
        if(counter >= (int)(delayBtwCast * Settings.maxFps * holderCharacter.modificator_CoolDownOfSkills)){
            counter =0 ;
            for(int i = 0; i < amountBullets; i++){
                PushingBullet bullet = new PushingBullet((int)(holderCharacter.locX - holderCharacter.sprite.getWidth() / 4 * holderCharacter.sizeOfSprite), (int)(holderCharacter.locY - holderCharacter.sprite.getHeight() / 4 * holderCharacter.sizeOfSprite), 0, 0, 2f, null, -20f);
                bullet.canDamagePlayer = false;
                bullet.canDamageEnemy = true;
                bullet.damage = damage * holderCharacter.modificator_Damage;
                bullet.speed = 12f;
                bullet.sizeOfSprite = 3f;
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

