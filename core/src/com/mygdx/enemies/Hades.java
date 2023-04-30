package com.mygdx.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.helpers.CONSTANTS;
import com.mygdx.helpers.CameraHelper;
import com.mygdx.map.CollisionHelper;
import screens.GameScreen;


public class Hades extends Enemy {
    HadesHelper hadesHelper;
    int currentPhaseProgress;
    final int HEALTH_BAR_WIDTH = Gdx.graphics.getWidth()/2, HEALTH_BAR_HEIGHT = Gdx.graphics.getHeight()/20;
    Sprite healthBar, healthBarBackground, healthContents; 
    OrthographicCamera camera;
    
    Vector3 realPos;
    Vector3 hudOrigin;
    boolean draw_healthBar;
    final float BOSS_MAX_HP = 5000;
    private boolean damageable;
    private int currentPhase;


    public Hades(float x, float y, EnemyHelper enemyHelper, Texture debugTexture, CollisionHelper collisionHelper, Texture texture) {
        super(x, y, enemyHelper, debugTexture, collisionHelper, texture, texture);
        currentTexture = new TextureRegion(rightTexture);
        this.attackRange = 0;
        this.modifiedMoveSpeed = 4f;
        this.damage = 10;               
        this.health = BOSS_MAX_HP;
        this.XP = 20;
        damageable = false;

        currentPhaseProgress = 0;
       
        this.width = 256;
        this.height = 256;
        
        this.hitBox = new Rectangle(x, y, width, height);
        this.tempBox = hitBox;
        
        hadesHelper = new HadesHelper(width, height, x, y, enemyHelper);

        currentPhase = 1;
        facing = Facing.SOUTH;
        visionCone.rotate(facing);
        
        camera = CameraHelper.getCamera();
        hudOrigin = new Vector3(0,0,0);
        draw_healthBar = false;

        // Sets up healthbar sprites
        Texture healthTexture = new Texture(Gdx.files.internal("hud/health.png"));
        healthBar = new Sprite(healthTexture);
        healthTexture = new Texture(Gdx.files.internal("hud/healthBackground.png"));
        healthBarBackground = new Sprite(healthTexture);

        //Sets up healthbar contents
        Pixmap pixmap = new Pixmap(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthTexture = new Texture(pixmap);
        healthContents = new Sprite(healthTexture);
        
        pixmap.dispose();
    }

    @Override
    protected void createVisionCone() {
        visionCone = new Triangle(new Vector2(entityPos.x, entityPos.y), new Vector2(entityPos.x - 1000, entityPos.y + 600), new Vector2(entityPos.x + 1000, entityPos.y + 600));
    }

    @Override
    public Boolean damageToEnemy(int dmg) {
        if (damageable) {
            health -= dmg;
        }

        if (health < 1) {
            GameScreen.state = CONSTANTS.STATE_WIN;
            return true;
        }
        return false;
    }

    @Override
    public void spawn() {
        nextState = State.PATROL;
        
    }

    @Override
    public void patrol() {
        if (checkVision()){
            draw_healthBar = true;
            nextState = State.ATTACK;
            damageable = true;
        }
    }

    private void calculateCurrentPhase(){
        if (health / BOSS_MAX_HP > 0.9) {
            currentPhase = 1;
        }
        else if (health / BOSS_MAX_HP > 0.7) {
            currentPhase = 2;
        } else if (health / BOSS_MAX_HP > 0.4) {
            currentPhase = 3;
        }
        else if (health / BOSS_MAX_HP > 0) {
            currentPhase = 4;
        }
    }

    @Override
    public void attack() {
            switch (currentPhase) {
            case 1:
                attack_PhaseOne();
                break;
            case 2:
                attack_PhaseTwo();
                break;
            case 3:
                attack_PhaseThree();
                break;
            case 4:
                attack_PhaseFour();
                break;
        }
    }

    @Override
    protected void animationUpdater() {

    }

    private void attack_PhaseOne(){
        boolean phaseFinished = false;

        switch (currentPhaseProgress) {
            case 0:
                phaseFinished = hadesHelper.bulletsAtPlayer((int) hadesHelper.getBulletsPerSecond() * 3, playerPos, entityPos);
                if (phaseFinished){
                    currentPhaseProgress++;
                    calculateCurrentPhase();
                }
                break;
            case 1:
                hadesHelper.bulletsCircle(entityPos);
                currentPhaseProgress++;
                calculateCurrentPhase();
                break;
            case 2:
                phaseFinished = hadesHelper.fireBolderAttack();
                if (phaseFinished) {
                    currentPhaseProgress++;
                    calculateCurrentPhase();
                }
                break;
            default:
                currentPhaseProgress = 0;

        }
    }

    private void attack_PhaseTwo() {
        boolean phaseFinished = false;

        switch (currentPhaseProgress) {
            case 0:
                phaseFinished = hadesHelper.bulletsAtPlayer((int) hadesHelper.getBulletsPerSecond() * 3, playerPos, entityPos);
                if (phaseFinished){
                    currentPhaseProgress++;
                    calculateCurrentPhase();
                }
                break;
            case 1:
                hadesHelper.bulletsCircle(entityPos);
                currentPhaseProgress++;
                break;
            case 2:
                phaseFinished = hadesHelper.fireBolderAttack();
                if (phaseFinished) {
                    currentPhaseProgress++;
                    calculateCurrentPhase();
                }
                break;
            case 3:
                phaseFinished = hadesHelper.half_roomFireAttack(playerPos);
                if (phaseFinished) {
                    currentPhaseProgress++;
                    calculateCurrentPhase();
                }
                break;
            default:
                currentPhaseProgress = 0;

        }
    }

    private void attack_PhaseThree() {
        boolean phaseFinished = false;

        switch (currentPhaseProgress) {
            case 0:
                hadesHelper.bulletsCircle(entityPos);
                currentPhaseProgress++;
                calculateCurrentPhase();
                break;
            case 1:
                phaseFinished = hadesHelper.fireBolderAttack();
                if (phaseFinished) {
                    hadesHelper.bulletsCircle(entityPos);
                    currentPhaseProgress++;
                    calculateCurrentPhase();
                }
                break;
            case 2:
                phaseFinished = hadesHelper.half_roomFireAttack(playerPos);
                hadesHelper.bulletsAtPlayer((int) hadesHelper.getBulletsPerSecond() * 3, playerPos, entityPos);
                if (phaseFinished) {
                    hadesHelper.bulletsCircle(entityPos);
                    currentPhaseProgress++;
                    calculateCurrentPhase();
                }
                break;
            case 3:
                phaseFinished = hadesHelper.full_roomFireAttack(playerPos);
                hadesHelper.bulletsAtPlayer((int) hadesHelper.getBulletsPerSecond() * 3, playerPos, entityPos);
                if (phaseFinished) {
                    hadesHelper.bulletsCircle(entityPos);
                    currentPhaseProgress++;
                    calculateCurrentPhase();
                }
                break;
            default:
                currentPhaseProgress = 0;
        }
    }

    private void attack_PhaseFour() {
        boolean phaseFinished = false;
        switch (currentPhaseProgress) {
            case 0:

                phaseFinished = hadesHelper.fireBolderAttack();
                hadesHelper.bulletsAtPlayer((int) hadesHelper.getBulletsPerSecond() * 3, playerPos, entityPos);
                if (phaseFinished) {
                    currentPhaseProgress++;
                    hadesHelper.bulletsCircle(entityPos);
                }
                break;
            case 1:

                phaseFinished = hadesHelper.half_roomFireAttack(playerPos);
                hadesHelper.bulletsAtPlayer((int) hadesHelper.getBulletsPerSecond() * 3, playerPos, entityPos);
                if (phaseFinished) {
                    currentPhaseProgress++;
                    hadesHelper.bulletsCircle(entityPos);
                }
                break;
            case 2:

                phaseFinished = hadesHelper.full_roomFireAttack(playerPos);
                hadesHelper.bulletsAtPlayer((int) hadesHelper.getBulletsPerSecond() * 3, playerPos, entityPos);
                if (phaseFinished) {
                    currentPhaseProgress++;
                    hadesHelper.bulletsCircle(entityPos);
                }
                break;
            default:
                currentPhaseProgress = 0;

        }
    }






    private void updateHealthBar(){
        realPos = hudOrigin.cpy();
        camera.unproject(realPos);

        healthBar.setPosition(realPos.x + Gdx.graphics.getWidth()/2 - HEALTH_BAR_WIDTH/2, realPos.y - 70);
        
        float bossHealthDisplay = health / BOSS_MAX_HP * HEALTH_BAR_WIDTH;
        if (bossHealthDisplay < 0) bossHealthDisplay = 0;
        healthContents.setSize(bossHealthDisplay, HEALTH_BAR_HEIGHT);
        healthContents.setPosition(healthBar.getX() + 5, healthBar.getY() + 5);
        healthBarBackground.setPosition(healthBar.getX(), healthBar.getY());
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        update();
        if (hadesHelper.getfireBolderOutlineBool()){
            hadesHelper.drawFireBolders(spriteBatch, hadesHelper.getFireBolderOutline(), false, playerPos);
        }
        else if (hadesHelper.getfireBolderAttackBool()) {
            hadesHelper.drawFireBolders(spriteBatch, hadesHelper.getFireBolder(), true, playerPos);
        }
        else if (hadesHelper.getRoomFireOutlineBool()) {
            hadesHelper.drawHalfRoomFire(spriteBatch, hadesHelper.getRoomFireRec(), false);
        }
        else if (hadesHelper.getRoomFireBool()) {
            hadesHelper.drawHalfRoomFire(spriteBatch, hadesHelper.getRoomFireRec(), true);
        }
        else if (hadesHelper.getFullRoomFireOutlineBoolean()) {
            hadesHelper.drawFullRoomFire(spriteBatch, false);
        }
        else if (hadesHelper.getFullRoomFireBoolean()) {
            hadesHelper.drawFullRoomFire(spriteBatch, true);
        }

        spriteBatch.draw(currentTexture, entityPos.x, entityPos.y);
        if (draw_healthBar) {
            updateHealthBar();
            // healthBarBackground.draw(spriteBatch);
            healthContents.draw(spriteBatch); 
            // healthBar.draw(spriteBatch);
        }

        if (debugMode) {
            spriteBatch.draw(debugTexture, visionCone.v1.x, visionCone.v1.y);
            spriteBatch.draw(debugTexture, visionCone.v2.x, visionCone.v2.y);
            spriteBatch.draw(debugTexture, visionCone.v3.x, visionCone.v3.y);
        }
    }

    @Override
    public void see() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void chase() {
        // TODO Auto-generated method stub
        
    }
}
