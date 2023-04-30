package com.mygdx.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.bullets.BulletHelper;
import com.mygdx.bullets.BulletHelper.SpecialProperty;
import com.mygdx.helpers.AudioHelper;
import com.mygdx.helpers.CONSTANTS;
import com.mygdx.helpers.CameraHelper;
import com.mygdx.objects.Renderable;

//contains all weapon logic, extend this class to create a new weapon very easily
public abstract class Weapon implements Renderable {
    //basic stats of the weapon, Firetime is interval between shots (similar to fire-rate)
    protected int damage;
    protected float x, y;
    protected int gunWidth, gunHeight, weaponXOffset;
    protected float fireRate, fireTime;
    
    //textures for weapons and bullets
    protected Texture weaponTexture;
    protected Texture bulletTexture;
    
    //used for shooting and fire rate
    protected Boolean canShoot;
    protected float currentShootTime;
    
    //used for bullet calculations
    protected final int bulletSpeed;
    protected final int bulletSpread;

    //bullet size
    protected final int bulletHeight = 10;
    protected final int bulletWidth = 10;
    private final OrthographicCamera camera;

    protected final BulletHelper bulletHelper;

    protected SpecialProperty weaponProperty;
    protected AudioHelper audioHelper;

    private boolean facingLeft;

    public Weapon(float fireRate, int damage, int bulletSpeed, int bulletSpread) {
        weaponProperty = SpecialProperty.NORMAL;
        this.damage = damage;
        this.fireRate = fireRate;
        this.fireTime = 1 / fireRate;
        this.currentShootTime = 0;
        this.bulletSpeed = bulletSpeed;
        this.bulletSpread = bulletSpread;
        this.camera = CameraHelper.getCamera();
        bulletHelper = BulletHelper.getBulletHelper();
        this.audioHelper = AudioHelper.getInstance();
        facingLeft = true;
    }
    
    //depending on implementation allows the player to hold the shoot button, or just press it once
    public Boolean getInput() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            return true;
        }
        return false;
    }

    public void update() {
        currentShootTime += Gdx.graphics.getDeltaTime();
        
        //checks if weapon is shot, and if weapon can be fired at current time, creates a new bullet
        if (getInput() && currentShootTime > fireTime) {
            Vector3 touchpos = new Vector3();
            touchpos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchpos);
            currentShootTime = 0;
            
            playSoundFX();

            Vector2 playerPosVector = new Vector2(x, y);
            bulletHelper.spawnBullet(bulletTexture, bulletSpeed, bulletSpread, damage, playerPosVector, touchpos, weaponProperty, true);
        }
    }

    public void setPlayerPos(float x, float y){
        if (facingLeft) {
            this.x = x + 10;
            this.y = y + 40;
            return;
        }
        this.x = x + CONSTANTS.PLAYER_WIDTH/2;
        this.y = y + 40;
    }

    //called by the Gamedriver class
    public void render(SpriteBatch spriteBatch) {
        update();

        //draws all bullets currently being rendered
        spriteBatch.draw(weaponTexture, x, y, gunWidth, gunHeight);
    }

    // increases attack speed by the given percentage
    public void incrAS(double newASMult) {
        fireRate *= newASMult;
        this.fireTime = 1 / fireRate;

    }

    public void incrDmg(double newDmgMult) {
        damage *= newDmgMult;
    }
    
    public SpecialProperty getSpecialProperty(){
        return weaponProperty;
    }

    // to be used by slowing weapons
    public void incrSlow(double percent){
        return;
    }

    public void setOrientation(boolean facingLeft) {
       this.facingLeft = facingLeft;
    }

    public void playSoundFX(){
        if(this instanceof VanillaGun){
            audioHelper.setSound(Gdx.files.internal("sound/basicShoot.wav"));
            audioHelper.playSound();
        } else if(this instanceof FingerGuns){
            audioHelper.setSound(Gdx.files.internal("sound/pistol.mp3"));
            audioHelper.playSound();
        } else if(this instanceof SniperGun){
            audioHelper.setSound(Gdx.files.internal("sound/sniperShot.mp3"));
            audioHelper.playSound();
        } else if(this instanceof RocketLauncherGun){
            audioHelper.setSound(Gdx.files.internal("sound/rocketLauncher.mp3"));
            audioHelper.playSound();
        } else if(this instanceof SlowingRocketLauncher){
            audioHelper.setSound(Gdx.files.internal("sound/laserGun.mp3"));
            audioHelper.playSound();
        }
    }
}

