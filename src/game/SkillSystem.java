package game;

public class SkillSystem{
    public boolean canDamagePlayer = false;
    public boolean canDamageEnemy = false;
    public float multiplierCD = 1f;

    private int timerCounter = 0;
    
    public void Skill1(int locX, int locY, int targetX, int targetY, Player player){
        timerCounter++;
        if(timerCounter >= 1*multiplierCD*Settings.maxFps){
            timerCounter = 0;
            Bullet bullet = new StandartBullet(locX,locY,targetX,targetY,2f, player);
            bullet.speed = 12f;
            bullet.SetSprite("res\\Bullets\\GojoSatoru(BLUE).png");
            bullet.sizeOfSprite = 1f;
            bullet.canDamagePlayer = canDamagePlayer;
            bullet.canDamageEnemy = canDamageEnemy;
            bullet.damage = 0.5f;
            bullet.AddAngle(0.30f);
            bullet.SetUpCollision();

            Bullet bullet1 = new StandartBullet(locX,locY,targetX,targetY,2f, player);
            bullet1.speed = 12f;
            bullet1.SetSprite("res\\Bullets\\GojoSatoru(BLUE).png");
            bullet1.sizeOfSprite = 1f;
            bullet1.canDamagePlayer = canDamagePlayer;
            bullet1.canDamageEnemy = canDamageEnemy;
            bullet1.damage = 0.5f;
            bullet1.AddAngle(-0.30f);
            bullet1.SetUpCollision();
            
            Bullet bullet2 = new StandartBullet(locX,locY,targetX,targetY,3f, player);
            bullet2.speed = 6f;
            bullet2.SetSprite("res\\Bullets\\GojoSatoru(BLUE).png");
            bullet2.sizeOfSprite = 3f;
            bullet2.canDamagePlayer = canDamagePlayer;
            bullet2.canDamageEnemy = canDamageEnemy;
            bullet2.damage = 2f;
            //bullet2.AddAngle(-0.30f);
            bullet2.delayBeforeStart = 1f;
            bullet2.SetUpCollision();	
        }    
    }

    public void Skill2(int locX, int locY, int targetX, int targetY, Player player){
        timerCounter++;
        if(timerCounter >= 1*multiplierCD*Settings.maxFps){
            Bullet bullet = new StandartBullet(locX,locY,targetX,targetY,3f, player);
            bullet.speed = 6f;
            bullet.SetSprite("res\\Bullets\\GojoSatoru(BLUE).png");
            bullet.sizeOfSprite = 3f;
            bullet.canDamagePlayer = canDamagePlayer;
            bullet.canDamageEnemy = canDamageEnemy;
            bullet.damage = 2f;
            //bullet.AddAngle(-0.30f);
            bullet.delayBeforeStart = 2f;
            bullet.showBeforeStart = false;
            bullet.SetUpCollision();	
        }
        
    }

    public void Skill3(int locX, int locY, int targetX, int targetY, Player player){
        timerCounter++;
        if(timerCounter >= 1*multiplierCD*Settings.maxFps){
            Bullet bullet = new StandartBullet(locX,locY,targetX,targetY,2f,player);
        bullet.speed = 12f;
        bullet.SetSprite("res\\Bullets\\GojoSatoru(BLUE).png");
        bullet.sizeOfSprite = 1f;
        bullet.canDamagePlayer = false;
        bullet.canDamageEnemy = true;
        bullet.damage = 0.5f;
        bullet.SetCustomAngle(-1.56f);
        bullet.SetUpCollision();

        Bullet bullet1 = new StandartBullet(locX,locY,targetX,targetY,2f,player);
        bullet1.speed = 12f;
        bullet1.SetSprite("res\\Bullets\\GojoSatoru(BLUE).png");
        bullet1.sizeOfSprite = 1f;
        bullet1.canDamagePlayer = false;
        bullet1.canDamageEnemy = true;
        bullet1.damage = 0.5f;
        bullet1.SetCustomAngle(1.56f);
        bullet1.SetUpCollision();

        Bullet bullet2 = new StandartBullet(locX,locY,targetX,targetY,2f,player);
        bullet2.speed = 12f;
        bullet2.SetSprite("res\\Bullets\\GojoSatoru(BLUE).png");
        bullet2.sizeOfSprite = 1f;
        bullet2.canDamagePlayer = false;
        bullet2.canDamageEnemy = true;
        bullet2.damage = 0.5f;
        bullet2.SetCustomAngle(0);
        bullet2.SetUpCollision();

        Bullet bullet3 = new StandartBullet(locX,locY,targetX,targetY,2f,player);
        bullet3.speed = 12f;
        bullet3.SetSprite("res\\Bullets\\GojoSatoru(BLUE).png");
        bullet3.sizeOfSprite = 1f;
        bullet3.canDamagePlayer = false;
        bullet3.canDamageEnemy = true;
        bullet3.damage = 0.5f;
        bullet3.SetCustomAngle(3.14f);
        bullet3.SetUpCollision();
        }
        
    }

    public void FireField(int locX, int locY, int targetX, int targetY, Player player){
        timerCounter++;
        if(timerCounter >= 1*multiplierCD*Settings.maxFps){
            for(int i = 0; i < 6; i++){
                Bullet bullet = new StandartBullet(locX, locY, 0, 0, 0, player);
            }
        }
    }
}
