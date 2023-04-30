package com.mygdx.objects;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.helpers.AudioHelper;
import com.mygdx.helpers.CONSTANTS;
import com.mygdx.map.CollisionHelper;
import com.mygdx.weapons.WeaponHelper;
import com.mygdx.bullets.BulletHelper.SpecialProperty;
import com.mygdx.cards.CardHelper;
import screens.GameScreen;
import com.mygdx.weapons.Weapon;

//TODO: Clean up this class idk if its possible
public class Player implements Renderable{
    //main stats for player, x and y are current position 
    private int playerHealth;
    private int playerMaxHealth;
    private int playerXP;
    public int xpForLvlUp;
    private int weaponASMult;
    private int weaponDmgMult;
    //private OrthographicCamera plCamera; // for creating weapons dynamically
    private double xPGainMult;
    private double dmgTakenMult;
    private boolean isNextCardBoosted;
    private boolean drawCardsAgain;
    private int numDrawCardsAgain;

    private boolean isDashEnabled;
    private float dashCooldown;
    private Vector3 dashVector;
    private float dashSubCount;
    private boolean isLeft;

    //player speed modifiers
    private float playerModifiedSpeed;
    private float playerDefaultSpeed;
    private float playerSpeedResetTimer;
    
    //player position information
    protected float x, y;
    protected Rectangle playerHitBox; 
    
    //related to player textures
    protected Texture currentTexture;
    protected Texture playerTextureRight;
    protected Texture playerTextureLeft;
    protected Boolean isPTextureLeft;

    //weapons
    private ArrayList<Weapon> weapons;
    private int currentWeapon;
    private int numWeapons;
    
    //helpers
    private CollisionHelper collisionHelper;
    private CardHelper cardHelper;
    private AudioHelper audioHelper;

    //used for debug screen
    private Boolean debugScreenEnabled;

    public Player(float x, float y, OrthographicCamera camera, CollisionHelper mapHelper, CardHelper cardHelper, AudioHelper audioHelper) {
        this.x = x;
        this.y = y;
        
        //player health from 0-100
        playerMaxHealth = 100;
        playerHealth = playerMaxHealth;

        //player xp from 0-100
        playerXP = 0;

        playerDefaultSpeed = 1f;
        playerModifiedSpeed = playerDefaultSpeed;
        playerSpeedResetTimer = -1;
        debugScreenEnabled = false;
        weaponASMult = 1;
        weaponDmgMult = 1;
        xPGainMult = 1;
        xpForLvlUp = 100;
        dmgTakenMult = 1;

        isDashEnabled = false;
        isNextCardBoosted = false;
        drawCardsAgain = false;
        dashCooldown = 0;
        dashVector = new Vector3();
        dashSubCount = 0;

        //creates the temp texture for the player, to be removed 
        this.playerTextureRight = new Texture(Gdx.files.internal("player/right_PlayerModel.png"));
        this.playerTextureLeft = new Texture(Gdx.files.internal("player/left_PlayerModel.png"));
        this.currentTexture = playerTextureRight;
        this.isPTextureLeft = false;

        //creates player hitbox
        this.playerHitBox = new Rectangle(x, y, CONSTANTS.PLAYER_WIDTH, CONSTANTS.PLAYER_HEIGHT);
        this.collisionHelper = mapHelper;
        this.cardHelper = cardHelper;

        //weapons array, contains all weapons
        currentWeapon = 0;
        numWeapons = CONSTANTS.INIT_NUM_WEAPONS;
        weapons = new ArrayList<>(numWeapons);
        
        weapons = WeaponHelper.getWeaponHelper().getPlayerWeapons();

        this.audioHelper = audioHelper;
    }
    
    //checks for keypresses, for player movement and weapon swapping
    public void update() {
        if (playerHealth <= 0) {
            audioHelper.dispose();
            GameScreen.state = CONSTANTS.STATE_DEATH;
        }

        if (playerXP >= xpForLvlUp) {
            playerXP %= xpForLvlUp;
            xpForLvlUp = (int) (xpForLvlUp * 1.15);
            cardHelper.drawCards(3);
        }

        if(drawCardsAgain == true){
            if( ! isNextCardBoosted){
                drawCardsAgain = false;
            } else {
                isNextCardBoosted = false;
            }
            cardHelper.drawCards(numDrawCardsAgain);
        }

        if (playerSpeedResetTimer > 0) {
            playerSpeedResetTimer -= Gdx.graphics.getDeltaTime();
        } else {
            playerModifiedSpeed = playerDefaultSpeed;
        }

        //potentially could add controller support, by just creating new method
        getPlayerInputKeyboard();
        weapons.get(currentWeapon).setOrientation(isLeft);
        weapons.get(currentWeapon).setPlayerPos(x,y);
    }

    //draws player texture, to be called by the GameScreen class
    public void render(SpriteBatch spriteBatch) {
		this.update(); 
        spriteBatch.draw(currentTexture, x, y, CONSTANTS.PLAYER_WIDTH, CONSTANTS.PLAYER_HEIGHT);
        getWeapon().render(spriteBatch);
	}

    public void killTrigger(int xp) {
        this.playerXP += xp * xPGainMult;
        cardHelper.checkActives(CONSTANTS.ON_KILL);
    }

    public void getPlayerInputKeyboard() {
        //tempBox is in charge of player collision detection, prevents player from getting stuck in walls
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            audioHelper.dispose();
            GameScreen.state = CONSTANTS.STATE_PAUSED;
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            //GameDriver.getINSTANCE().setMainMenuScreen();
            audioHelper.dispose();
//            GameScreen.state = CONSTANTS.STATE_DEATH;
            cardHelper.drawCards(3);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(CONSTANTS.WINDOW_WIDTH, CONSTANTS.WINDOW_HEIGHT);
            }
            else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            if (debugScreenEnabled)
                this.debugScreenEnabled = false;
            else 
                this.debugScreenEnabled = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            currentWeapon++; 
            if (currentWeapon > 1) {
                currentWeapon = 0;
            }   
        }
        checkMovementInputs();
    }
    
    private void checkMovementInputs() {
        Rectangle tempBox = this.playerHitBox;
        Vector3 movementVector = new Vector3();

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            movementVector.x = movementVector.x - 400*Gdx.graphics.getDeltaTime() * playerModifiedSpeed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            movementVector.x = movementVector.x + 400*Gdx.graphics.getDeltaTime() * playerModifiedSpeed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            movementVector.y = movementVector.y - 400*Gdx.graphics.getDeltaTime() * playerModifiedSpeed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            movementVector.y = movementVector.y + 400*Gdx.graphics.getDeltaTime() * playerModifiedSpeed;
        }        

        if(isDashEnabled){
            if(dashCooldown <= 0) {
                if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                    dashVector = movementVector.cpy();
                    dashVector.nor().scl(10);
                    dashSubCount = 0.2f;
                    dashCooldown = 1f;
                }
            } else {
                dashCooldown -= Gdx.graphics.getDeltaTime();
            }
        }

        if (dashSubCount > 0 && ((Math.abs(dashVector.x) + Math.abs(dashVector.y)) > 0)) {
            dashSubCount-= Gdx.graphics.getDeltaTime();
            if (dashSubCount <= 0) {
                dashVector = new Vector3(0, 0, 0);
            }
        }

        movementVector.add(dashVector.cpy().scl(80 * Gdx.graphics.getDeltaTime()));
        tempBox.x += movementVector.x;
        tempBox.y += movementVector.y;

        if (tempBox.x - x > 0) {
            isLeft = false;
            currentTexture = playerTextureRight;
        }
        else if (tempBox.x - x < 0) {
            isLeft = true;
            currentTexture = playerTextureLeft;
        }


        if (!collisionHelper.checkCollisions(tempBox)) {
            x = tempBox.x;
            y = tempBox.y;
        }

        updatePlayerHitbox();
    }

    public void addWeapon() {
        numWeapons++;
    }

    public void addWeapon(Weapon weapon) {
        numWeapons++;
        weapon.getClass();
        weapons.add(weapon);
    }
    
    public void incrWeaponAS(double percent) {
        weaponASMult += percent/100;
        for (Weapon w : weapons) {
            w.incrAS(weaponASMult);
        }
    }

    public void incrDmg(double percent) {
        weaponDmgMult += percent/100;
        for (Weapon w : weapons) {
            w.incrDmg(weaponDmgMult);
        }
    }

    public void enableDash() {
        isDashEnabled = true;
    }

    public void incrXPMult(double percent) {
        xPGainMult += percent/100f;
    }

    /*
     * @param percentUpd can be negative - to reduce dmg
     * or positive, to increase dmg
     */
    public void updDmgTaken(double percentUpd){
        dmgTakenMult += percentUpd/100f;
    }

    public void updSlowPercent(float percent){
        for (Weapon w : weapons) {
            SpecialProperty sp = w.getSpecialProperty();
            if(sp == SpecialProperty.SLOW || sp == SpecialProperty.SLOW_AND_AOE){
                w.incrSlow(percent);
            }
        }
    }

    public float getPlayerX() {
        return x;
    }
	
    public float getPlayerY() {
        return y;
    }

    //returns current weapon being held by player
    public Weapon getWeapon() {
        return weapons.get(currentWeapon);
    }

    public Rectangle getPlayerHitbox() {
        return playerHitBox;
    }

    private void updatePlayerHitbox() {
        playerHitBox.x = x;
        playerHitBox.y = y;
    }

    public int getPlayerHP() {
        return playerHealth;
    }

    public int getPlayerXP() {
        return playerXP;
    }

    public int getPlayerMaxHp() {
        return playerMaxHealth;
    }

    public void playerDamage(int dmg) {
        if (playerHealth > 0)
            playerHealth -= dmg * dmgTakenMult;
    }

    public void playerHeal(int heal) {
        playerHealth += heal;
        if (playerHealth > playerMaxHealth) {
            playerHealth = playerMaxHealth;
        }
    }

    public void playerSpeedUp(float multiplier) {
        if (playerModifiedSpeed < 1.5f)
            playerModifiedSpeed *= multiplier;
    }

    public void playerMaxHealthUp(int increase) {
        playerMaxHealth += increase;
        playerHealth += increase;
    }

    public void setPlayerSpeedTimer(float timer) {
        playerSpeedResetTimer = timer;
    }

    public Boolean getDebugBoolean() {
        return debugScreenEnabled;
    }

    public CardHelper getCardHelper() {
        return cardHelper;
    }

    public void fullyHeal(){
        playerHealth = playerMaxHealth;
    }

    public void drawCardsAgain(int numDrawCardsAgain) {
        drawCardsAgain = true;
        this.numDrawCardsAgain = numDrawCardsAgain;
    }

    public void boostNextCard(){
        isNextCardBoosted = true;
        cardHelper.boostNextCard();
    }
}
 