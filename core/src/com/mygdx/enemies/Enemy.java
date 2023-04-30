package com.mygdx.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.map.CollisionHelper;
import com.mygdx.objects.Renderable;

public abstract class Enemy implements Renderable {
    protected Vector3 entityPos;
    protected Vector3 playerPos;

    protected int XP; 
    
    protected float attackTimer;
    protected int attackRange;

    private Boolean currentlySlowed;
    private float slowTimer;
    protected float modifiedMoveSpeed;
    protected float defaultMoveSpeed;


    protected int damage;
    protected float health;
    protected boolean playerSeen;
    
    protected boolean debugMode;

    protected int width;
    protected int height;

    protected TextureRegion currentTexture;
    protected Texture leftTexture;
    protected Texture rightTexture;
    protected Texture debugTexture;
    protected Texture explanationmarkTexture;
    
    protected Rectangle hitBox;
    protected Rectangle tempBox;
    protected Triangle visionCone;

    protected EnemyHelper enemyHelper;
    protected CollisionHelper collisionHelper;

    public State nextState;
    protected Facing facing;

    protected enum State {
        SPAWN,
        PATROL,
        SEE,
        CHASE,
        ATTACK
    }

    protected enum Facing {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }


    public Enemy(float x, float y, EnemyHelper enemyHelper, Texture debugTexture, CollisionHelper collisionHelper, Texture rightTexture, Texture leftTexture) {
        entityPos = new Vector3(x, y, 0);
        playerPos = new Vector3(0, 0, 0);
        this.attackTimer = 0;
       
        this.enemyHelper = enemyHelper;
        debugMode = false;
        this.debugTexture = debugTexture;
        this.collisionHelper = collisionHelper;
        this.leftTexture = leftTexture;
        this.rightTexture = rightTexture;
        nextState = State.SPAWN;
        facing = Facing.NORTH;
        playerSeen = false;

        currentlySlowed = false;

        createVisionCone();
    }

    /**Create Vision code here*/
    protected abstract void createVisionCone();

    /**State Machine Logic here: */
    public abstract void spawn();
    public abstract void patrol();
    public abstract void see();
    public abstract void chase();   
    public abstract void attack();  

    protected abstract void animationUpdater();

    @Override 
    public void update() {
        animationUpdater();
        if (currentlySlowed){
            setSlow(0);
        }
        switch (nextState) {
            case SPAWN:
                spawn();
                break;
            case PATROL:
                patrol();
                break;
            case SEE:
                see();
                break;
            case CHASE:
                chase();
                break;
            case ATTACK:
                attack();
                break;
        } 
    }

  
    @Override
    public void render(SpriteBatch spriteBatch) {
        update();
        spriteBatch.draw(currentTexture, entityPos.x, entityPos.y);

//        if (playerSeen) {
//            spriteBatch.draw(explanationmarkTexture, entityPos.x + width/4, entityPos.y + 100);
//        }
//        if (debugMode) {
//            spriteBatch.draw(debugTexture, visionCone.v1.x, visionCone.v1.y);
//            spriteBatch.draw(debugTexture, visionCone.v2.x, visionCone.v2.y);
//            spriteBatch.draw(debugTexture, visionCone.v3.x, visionCone.v3.y);
//        }
    }

    public Boolean checkVision(){
        return visionCone.contains(new Vector2(playerPos.x, playerPos.y));
    }

    /*
    private boolean checkForPlayerConeOverlap(Rectangle rec) {
        int overlap = visionCone.isContaintedByRectangle(rec);
            if (overlap != -1) {
                Vector2 overlapPos = visionCone.getOverlapVector(rec);
                if (playerPos.y > overlapPos.y){
                    return false;
                } 
            }
        return true;
    }
    */

    protected Facing calculateFacingDirection(Vector3 moveVector) {
        if (Math.abs(moveVector.x) < moveVector.y) {
            return Facing.NORTH;
        }
        else if (moveVector.x > 0 && Math.abs(moveVector.x) >= moveVector.y){
            return Facing.EAST;
        }
        else if (Math.abs(moveVector.y) > moveVector.x && moveVector.y < 0) {
            return Facing.SOUTH;
        }
        else if (Math.abs(moveVector.x) >= moveVector.y && moveVector.x < 0) {
            return Facing.WEST;
        }
        return Facing.NORTH;
    }

    public Rectangle getHitBox() {
        return this.hitBox;
    }

    /**
     * @param dmg amount of damage to be done to the enemy
     * @return true if enemy killed
     */
    public Boolean damageToEnemy(int dmg) {
        health -= dmg;
        if (health < 1) {
            return true;
        }
        return false;
    }

    public void setPlayerPos(float x, float y) {
        playerPos.x = x;
        playerPos.y = y;
    }

    public Vector3 getEntityPos() {
        return entityPos;
    }

    public int getXP() {
        return XP;
    }

    public void setSlow(float slowRatioInDecimal){
        if (!currentlySlowed) {
            modifiedMoveSpeed = defaultMoveSpeed * (slowRatioInDecimal);
            slowTimer = 4;
            currentlySlowed = true;
        }

        slowTimer -= Gdx.graphics.getDeltaTime();

        if (slowTimer < 0){
            modifiedMoveSpeed = defaultMoveSpeed;
            currentlySlowed = false;
        }
    }
   
    public void setDebugMode(Boolean debug){
        debugMode = debug;
    } 

}
