package com.mygdx.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.map.CollisionHelper;

public class Dog extends Enemy{
    private Vector3 movementVector;
    private Vector3 normalizedMovementVector;
    private float visionCheckCounter;
    private float seenCounter;
    private float jumpCounter;

    //animation stuff
    private static final int FRAME_COLS = 5, FRAME_ROWS = 1;
    private Animation<TextureRegion> attackAnimationLeft;
    private Animation<TextureRegion> attackAnimationRight;
    private Boolean isLeft;
    Texture walkSheet;
    float animationStateTime;

    float attackAnimationTime;
    final TextureRegion PREPPING_ATTACK;
    private boolean doneDamage;

    public Dog(float x, float y, EnemyHelper enemyHelper, Texture debugTexture, CollisionHelper collisionHelper, Texture rightTexture, Texture leftTexture, Texture explanationMark) {
        super(x, y, enemyHelper, debugTexture, collisionHelper, rightTexture, leftTexture);
        this.attackRange = 100;
        this.modifiedMoveSpeed = 550f;
        this.defaultMoveSpeed = modifiedMoveSpeed;
        this.damage = 25;
        this.health = 100;
        this.XP = 20;
        this.explanationmarkTexture = explanationMark;
        this.width = 140;
        this.height = 107;
        
        this.visionCheckCounter = 0f;
        this.jumpCounter = 0;

        this.hitBox = new Rectangle(x, y, width, height);
        this.tempBox = hitBox;
        init_animations();

        Texture temp = new Texture(Gdx.files.internal("enemies/hell_hound/hell_hound_prepping_left.png"));
        PREPPING_ATTACK = new TextureRegion(temp);
        currentTexture = attackAnimationLeft.getKeyFrame(0f, true);
        doneDamage = false;
    }

    @Override
    protected void createVisionCone() {
        visionCone = new Triangle(new Vector2(entityPos.x, entityPos.y), new Vector2(entityPos.x - 1000, entityPos.y + 1000), new Vector2(entityPos.x + 1000, entityPos.y + 1000));
    }

    @Override 
    public void setPlayerPos(float x, float y) {
        playerPos.x = x;
        playerPos.y = y;
    }

    //TODO: Animate Enemy Spawn
    @Override
    public void spawn() {
        generateNewMovementVector();
        nextState = State.PATROL;
    }

    @Override
    public void patrol() {
        if (visionCheckCounter > 0.5){
            this.facing = calculateFacingDirection(movementVector);
            visionCone.rotate(facing);
            visionCheckCounter = 0;
            if (checkVision()){
                nextState = State.SEE;
                seenCounter = 0;
            }
        }

        visionCheckCounter+=Gdx.graphics.getDeltaTime();
        
        if (moveOneUnit()) 
            return;
        generateNewMovementVector();
    }

    @Override
    public void see() {
        if (seenCounter > 1) {
            nextState = State.CHASE;
            enemyHelper.playerSeen();
        }
        else if (seenCounter < 1 && checkVision()) {
            playerSeen = true;
            seenCounter+=Gdx.graphics.getDeltaTime();
        }
        else if (seenCounter < 0){
            playerSeen = false;
            nextState = State.PATROL;
        }   
        else if (seenCounter < 1 && !checkVision()){
            playerSeen = true;
        }
        
    }
    
    @Override
    public void chase() {
        currentTexture = attackAnimationLeft.getKeyFrame(0f, true);
        generatePlayerMovementVector();        
        if (playerPos.dst(entityPos) < attackRange) {
            doneDamage = false;
            generatePlayerMovementVector();
            normalizedMovementVector.scl(1.4f);
            if (normalizedMovementVector.x > 0) { isLeft = false; }
            else { isLeft = true; }
            attackAnimationTime = 0;
            nextState = State.ATTACK;
        }
        if (moveOneUnit()) {
            jumpCounter = 0;
            return;
        }
        if (jumpCounter > 1) {
            jump(playerPos);
            return;
        }

        jumpCounter+=Gdx.graphics.getDeltaTime();
    }

    @Override
    public void attack() {
        //animate
        if (isLeft) {
            currentTexture = attackAnimationLeft.getKeyFrame(attackAnimationTime, true);
        }
        else {
            currentTexture = attackAnimationRight.getKeyFrame(attackAnimationTime, true);
        }

        if (attackTimer > 0.5) {
            attackAnimationTime += Gdx.graphics.getDeltaTime();
            moveOneUnit();

            if (!doneDamage){ checkEnemyDamage(); }

            if (attackAnimationTime > 0.5) {
                attackTimer = 0;
                nextState = State.CHASE;
            }
        }
        else {
            attackTimer+=Gdx.graphics.getDeltaTime();
        }

    }

    private void checkEnemyDamage(){
        if (hitBox.contains(playerPos.x + 25, playerPos.y + 25)){
            enemyHelper.attack(damage);
            doneDamage = true;
        }
    }

    @Override
    protected void animationUpdater() {
        animationStateTime+=Gdx.graphics.getDeltaTime();
    }

    private void jump(Vector3 jumpTarget) {
        hitBox.setPosition(entityPos.x, entityPos.y);
        Vector3 movementVector = jumpTarget.sub(entityPos).nor().scl(modifiedMoveSpeed * Gdx.graphics.getDeltaTime() * 5);
        entityPos.add(movementVector);
    }

    private Boolean moveOneUnit() {
        hitBox.setPosition(entityPos.x, entityPos.y);
        movementVector = normalizedMovementVector.cpy();
        movementVector.scl(modifiedMoveSpeed);

        //checks for possible collision using a temporary hitbox
        Vector3 deltaScaleMove = movementVector.cpy();
        deltaScaleMove.scl(Gdx.graphics.getDeltaTime());
        tempBox.x = hitBox.x + deltaScaleMove.x;
        tempBox.y = hitBox.y + deltaScaleMove.y;
        
        //sets enemy texture to right direction
        if (movementVector.x > 0) {
            currentTexture = attackAnimationRight.getKeyFrame(0,true);
        }
        else if (movementVector.x < 0) {
            currentTexture = attackAnimationLeft.getKeyFrame(0, true);
        }
        
        if (!collisionHelper.checkCollisions(tempBox)) {
            entityPos.add(deltaScaleMove);
            visionCone.moveTriangle(deltaScaleMove);
            return true;
        }
        return false;
    }

    private void generatePlayerMovementVector() {
        this.normalizedMovementVector = playerPos.cpy().sub(entityPos).nor();
    }

    private void generateNewMovementVector() {
        float directionRadians = (float) (Math.random() * (Math.PI * 2) - Math.PI);
        this.normalizedMovementVector = new Vector3((float) Math.cos(directionRadians), (float) Math.sin(directionRadians), 0);
        normalizedMovementVector.nor();
    }

    private void init_animations(){
        walkSheet = new Texture(Gdx.files.internal("enemies/hell_hound/hell_hound_spritesheet_left.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth()/FRAME_COLS,
                walkSheet.getHeight()/FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_ROWS * FRAME_COLS];

        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++){
            for (int j = 0; j < FRAME_COLS; j++){
               walkFrames[index++] = tmp[i][j];
            }
        }

        attackAnimationLeft = new Animation<TextureRegion>(0.125f, walkFrames);
        walkSheet = new Texture(Gdx.files.internal("enemies/hell_hound/hell_hound_spritesheet_right.png"));
        tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth()/FRAME_COLS,
                walkSheet.getHeight()/FRAME_ROWS);

        walkFrames = new TextureRegion[FRAME_ROWS * FRAME_COLS];
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++){
            for (int j = 0; j < FRAME_COLS; j++){
                walkFrames[index++] = tmp[i][j];
            }
        }
        attackAnimationRight = new Animation<TextureRegion>(0.125f, walkFrames);
        animationStateTime = 0;
    }

}
