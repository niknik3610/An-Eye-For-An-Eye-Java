package com.mygdx.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.bullets.BulletHelper;
import com.mygdx.bullets.BulletHelper.SpecialProperty;
import com.mygdx.helpers.AudioHelper;

public class HadesHelper {
    int width, height;
    
    Vector2 bossRoomDimensionStart, bossRoomDimensionsEnd;
    BulletHelper bulletHelper;
    EnemyHelper enemyHelper;
    AudioHelper audioHelper;

    private Texture bulletTexture, fireBolder, fireBolderOutline, half_roomFire, half_roomFireOutline, full_roomFire, full_roomFireOutline, full_safeZone;
   
    float currentAttackTimer;
    
    boolean draw_fireBolderAttack, draw_fireBolderOutline;
    boolean draw_halfRoomFire, draw_halfRoomFireOutline;
    boolean draw_fullRoomFire, draw_fullRoomFireOutline;

    boolean playerTakenCurrentAttackDamage;
    float phaseGraceTimer = 2;

    int[] fireBolderLocations;
    Vector2 safeZoneLocation;

    Rectangle half_roomFireLocation;
    Rectangle full_roomFireLocation;
    

    private final int BOLDER_SIZE = 364, BOLDER_DAMAGE = 10;
    private final int BULLET_WIDTH = 20, BULLET_HEIGHT = 20;
    private final int ROOMFIRE_DAMAGE = 80;
    private final int SAFE_ZONE_RADIUS = 512;

    Vector2 fireDimensions;

    float timeSinceLastBullet, bulletsPerSecond;
    int bulletsToShoot;

    public HadesHelper(int width, int height, float x, float y, EnemyHelper enemyHelper){
        audioHelper = AudioHelper.getInstance();
        bossRoomDimensionStart = new Vector2(1786, 9884);
        bossRoomDimensionsEnd = new Vector2(3262, 11520);
        
        bulletsPerSecond = 2;
        this.timeSinceLastBullet = 0;
        currentAttackTimer = 2;
        
        draw_fireBolderAttack = false;
        draw_fireBolderOutline = false;
        this.bulletHelper = BulletHelper.getBulletHelper();
        this.enemyHelper = enemyHelper;

        fireDimensions = new Vector2(bossRoomDimensionsEnd.x - bossRoomDimensionStart.x, (bossRoomDimensionsEnd.y - bossRoomDimensionStart.y)/2);
        full_roomFireLocation = new Rectangle(bossRoomDimensionStart.x, bossRoomDimensionStart.y, bossRoomDimensionsEnd.x - bossRoomDimensionStart.x, bossRoomDimensionsEnd.y - bossRoomDimensionStart.y);

        fireBolderOutline = new Texture(Gdx.files.internal("enemies/hades/boss-fight/preppingBolder.png"));
        fireBolder = new Texture(Gdx.files.internal("enemies/hades/boss-fight/bolder.png"));
        half_roomFireOutline = new Texture(Gdx.files.internal("enemies/hades/boss-fight/prepping-halfRoom.png"));
        half_roomFire = new Texture(Gdx.files.internal("enemies/hades/boss-fight/halfRoom.png"));
        full_roomFire = new Texture(Gdx.files.internal("enemies/hades/boss-fight/fullRoom.png"));
        full_roomFireOutline = new Texture(Gdx.files.internal("enemies/hades/boss-fight/prepare_fullRoom.png"));
        generatePixmaps();
    }

    public boolean bulletsAtPlayer(int amount, Vector3 playerPos, Vector3 entityPos){
        if (bulletsToShoot <= 0) {
            bulletsToShoot = amount;
        }
        if (timeSinceLastBullet > 1/bulletsPerSecond) {
            shootOneBullet(playerPos, new Vector2(entityPos.x+width/2 - 5, entityPos.y));
            timeSinceLastBullet = 0;
            bulletsToShoot--;
        }
        timeSinceLastBullet += Gdx.graphics.getDeltaTime();
        
        if (bulletsToShoot == 0) {
            return true;
        }

        return false;
    }
    
    public void bulletsCircle(Vector3 entityPos){
        for (float i = (float) (-3*Math.PI); i < Math.PI; i+= Math.PI/8){
            int scaleFactor = 500;
            Vector2 circle = new Vector2((float) Math.cos(i) * scaleFactor, (float) Math.sin(i) * scaleFactor);
            Vector3 targetVector = new Vector3(entityPos.x + circle.x, entityPos.y + circle.y, 0);
            shootOneBullet(targetVector, new Vector2(entityPos.x+width/2 - 5, entityPos.y));
        }
    }

    private void shootOneBullet(Vector3 target, Vector2 origin){
        bulletHelper.spawnBullet(bulletTexture, 10f, 0f, 10, origin , target, SpecialProperty.NORMAL, false);
    }

    public boolean fireBolderAttack(){
        if (currentAttackTimer >= 2) {
            audioHelper.setSound(Gdx.files.internal("sound/roar.mp3"));
            audioHelper.playSound();
            fireBolderLocations = randomizeFireBolders();
            playerTakenCurrentAttackDamage = false;
        }
        currentAttackTimer -= Gdx.graphics.getDeltaTime();
        if (currentAttackTimer > 0) {
            draw_fireBolderOutline = true;
        }
        else if (currentAttackTimer <= 0 && !draw_fireBolderAttack) {
            draw_fireBolderOutline = false;
            draw_fireBolderAttack = true;
        }
        else if (currentAttackTimer <= -2 - phaseGraceTimer){
            currentAttackTimer = 2;
            draw_fireBolderAttack = false;
            return true;
        }
        
        return false;
    }
    
    public void drawFireBolders(SpriteBatch batch, Texture bolderTexture, boolean dealsDamage, Vector3 playerPos) {
        int i = 0;
        for (float x = bossRoomDimensionStart.x + BOLDER_SIZE; x < bossRoomDimensionsEnd.x; x+= BOLDER_SIZE*2){
            for (float y = bossRoomDimensionStart.y + BOLDER_SIZE; y < bossRoomDimensionsEnd.y; y+= BOLDER_SIZE*2){
                if (fireBolderLocations[i] == 1){
                    batch.draw(bolderTexture, x, y);
                    if (dealsDamage) {
                        playerBolderDamage(x, y, playerPos);
                    }
                }
                i++;
            }
        }
    }

    public void playerBolderDamage(float x, float y, Vector3 playerPos){
        if (playerPos.dst(x + BOLDER_SIZE/2+10, y + BOLDER_SIZE/2 - 10, 0) < BOLDER_SIZE/2 && !playerTakenCurrentAttackDamage) {
            enemyHelper.attack(BOLDER_DAMAGE);
            playerTakenCurrentAttackDamage = true;
        }
        
    }

    public int[] randomizeFireBolders(){
        int[] toReturn = new int[15];
        for (int i = 0; i < 15; i++) {
            toReturn[i] = (int) Math.round(Math.random());
        }
        return toReturn;
    }

    public boolean half_roomFireAttack(Vector3 playerPos){
        if (currentAttackTimer == 2) {
            audioHelper.setSound(Gdx.files.internal("sound/roar.mp3"));
            audioHelper.playSound();
            half_roomFireLocation = halfRoomFireRectangle();
            playerTakenCurrentAttackDamage = false;
        }
        currentAttackTimer -= Gdx.graphics.getDeltaTime();
        if (currentAttackTimer > 0) {
            draw_halfRoomFireOutline = true;
        }
        else if (currentAttackTimer <= 0 && !draw_halfRoomFire) {
            draw_halfRoomFireOutline = false;
            draw_halfRoomFire = true;
        }
        else if (currentAttackTimer <= -2 - phaseGraceTimer){
            currentAttackTimer = 2;
            draw_halfRoomFire = false;
            return true;
        }
        else if (currentAttackTimer < 0 && !playerTakenCurrentAttackDamage) {
            halfRoomPlayerDamage(playerPos);
        }
        
        return false;
    }

    public Rectangle halfRoomFireRectangle(){
        float roomHalf = Math.round(Math.random());
        Rectangle rec;
       
        if (roomHalf == 0) {
            rec = new Rectangle(bossRoomDimensionStart.x, bossRoomDimensionStart.y, fireDimensions.x, fireDimensions.y);
        }
        else {
            rec = new Rectangle(bossRoomDimensionStart.x, bossRoomDimensionStart.y + fireDimensions.y, fireDimensions.x, fireDimensions.y);
        }
        
        return rec;
    }

    public void drawHalfRoomFire(SpriteBatch batch, Rectangle rec, Boolean doesDmg) {
        Texture toDraw;
        if (doesDmg){
            toDraw = half_roomFire;
        }
        else {
            toDraw = half_roomFireOutline;
        }
        batch.draw(toDraw, rec.x, rec.y);
    }

    private void halfRoomPlayerDamage(Vector3 playerPos){
        if (half_roomFireLocation.contains(playerPos.x, playerPos.y) && !playerTakenCurrentAttackDamage) {
            enemyHelper.attack(ROOMFIRE_DAMAGE);
            playerTakenCurrentAttackDamage = true;
        }
    }

    public boolean full_roomFireAttack(Vector3 playerPos){
        if (currentAttackTimer == 2) {
            audioHelper.setSound(Gdx.files.internal("sound/roar.mp3"));
            audioHelper.playSound();
            safeZoneLocation = full_generateSafeZone();
            playerTakenCurrentAttackDamage = false;
        }
       
        currentAttackTimer -= Gdx.graphics.getDeltaTime();
        if (currentAttackTimer > 0) {
            draw_fullRoomFireOutline = true;
        }
        else if (currentAttackTimer <= -2 && !draw_fullRoomFire) {
            draw_fullRoomFireOutline = false;
            draw_fullRoomFire = true;
        }
        else if (currentAttackTimer <= -2 - phaseGraceTimer){
            currentAttackTimer = 2;
            draw_fullRoomFire = false;
            return true;
        }
        else if (currentAttackTimer < -2 && !playerTakenCurrentAttackDamage) {
            full_roomFireDmg(safeZoneLocation.x, safeZoneLocation.y, playerPos);
        }
        
        return false;
    }

    public void full_roomFireDmg(float x, float y, Vector3 playerPos){
        if (playerPos.dst(safeZoneLocation.x + SAFE_ZONE_RADIUS/2, safeZoneLocation.y + SAFE_ZONE_RADIUS/2, 0) > SAFE_ZONE_RADIUS/2 && !playerTakenCurrentAttackDamage) {
            enemyHelper.attack(ROOMFIRE_DAMAGE);
            playerTakenCurrentAttackDamage = true;
        }
    }

    public void drawFullRoomFire(SpriteBatch batch, Boolean doesDmg){
        Texture toDraw;
        if (doesDmg) {
            toDraw = full_roomFire;
        }
        else {
            toDraw = full_roomFireOutline;
        }
        batch.draw(toDraw, bossRoomDimensionStart.x, bossRoomDimensionStart.y);
        batch.draw(full_safeZone, safeZoneLocation.x, safeZoneLocation.y);
    }

    private Vector2 full_generateSafeZone(){
        int x = (int) (Math.random() * (bossRoomDimensionsEnd.x - SAFE_ZONE_RADIUS - bossRoomDimensionStart.x) + bossRoomDimensionStart.x);
        int y = (int) (Math.random() * (bossRoomDimensionsEnd.y - SAFE_ZONE_RADIUS - bossRoomDimensionStart.y) + bossRoomDimensionStart.y);
        return new Vector2(x, y);
    }

    public float getBulletsPerSecond() {
        return bulletsPerSecond;
    }

    public boolean getfireBolderAttackBool(){
        return draw_fireBolderAttack;
    }
    
    public boolean getfireBolderOutlineBool(){
        return draw_fireBolderOutline;
    }

    public Texture getFireBolderOutline() {
        return fireBolderOutline;
    }

    public Texture getFireBolder() {
        return fireBolder;
    }

    public boolean getRoomFireOutlineBool(){
        return draw_halfRoomFireOutline;
    }

    public boolean getRoomFireBool() {
        return draw_halfRoomFire;
    }

    public Rectangle getRoomFireRec(){
        return half_roomFireLocation;
    }

    public boolean getFullRoomFireOutlineBoolean() {
        return draw_fullRoomFireOutline;
    }

    public boolean getFullRoomFireBoolean() {
        return draw_fullRoomFire;
    }

    public float getPhaseGraceTimer() {
        return phaseGraceTimer;
    }

    public void setPhaseGraceTimer(float newTimer) {
        phaseGraceTimer = newTimer;
    }

    private void generatePixmaps() {
        //TODO: Textures
        
        //Bullets
        Pixmap pixmap = new Pixmap(BULLET_WIDTH, BULLET_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, BULLET_WIDTH, BULLET_HEIGHT);
        this.bulletTexture = new Texture(pixmap);
        
        //Bolders
//        pixmap = new Pixmap(BOLDER_SIZE, BOLDER_SIZE, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.RED);
//        pixmap.setBlending(Pixmap.Blending.None);
//        pixmap.fillCircle(BOLDER_SIZE/2, BOLDER_SIZE/2, BOLDER_SIZE/2);
//        this.fireBolder = new Texture(pixmap);
//
//        pixmap = new Pixmap(BOLDER_SIZE, BOLDER_SIZE, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.GRAY);
//        pixmap.setBlending(Pixmap.Blending.None);
//        pixmap.fillCircle(BOLDER_SIZE/2, BOLDER_SIZE/2, BOLDER_SIZE/2);
//        this.fireBolderOutline = new Texture(pixmap);

        //Half_room fire
//        pixmap = new Pixmap((int) fireDimensions.x, (int) fireDimensions.y, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.GRAY);
//        pixmap.setBlending(Pixmap.Blending.None);
//        pixmap.fillRectangle(0, 0, (int) fireDimensions.x, (int) fireDimensions.y);
//        this.half_roomFireOutline = new Texture(pixmap);
//
//        pixmap = new Pixmap((int) fireDimensions.x, (int) fireDimensions.y, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.RED);
//        pixmap.setBlending(Pixmap.Blending.None);
//        pixmap.fillRectangle(0, 0, (int) fireDimensions.x, (int) fireDimensions.y);
//        this.half_roomFire = new Texture(pixmap);

        //Full_room fire
//        pixmap = new Pixmap((int) full_roomFireLocation.width, (int) full_roomFireLocation.height, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.GRAY);
//        pixmap.setBlending(Pixmap.Blending.None);
//        pixmap.fillRectangle(0, 0, (int) full_roomFireLocation.width, (int) full_roomFireLocation.height);
//        this.full_roomFireOutline = new Texture(pixmap);
//
//        pixmap = new Pixmap((int) full_roomFireLocation.width, (int) full_roomFireLocation.height, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.RED);
//        pixmap.setBlending(Pixmap.Blending.None);
//        pixmap.fillRectangle(0, 0, (int) full_roomFireLocation.width, (int) full_roomFireLocation.height);
//        this.full_roomFire = new Texture(pixmap);

        pixmap = new Pixmap(SAFE_ZONE_RADIUS, SAFE_ZONE_RADIUS, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.GREEN);
        pixmap.fillCircle(SAFE_ZONE_RADIUS/2, SAFE_ZONE_RADIUS/2, SAFE_ZONE_RADIUS/2);
        this.full_safeZone = new Texture(pixmap);
    }
}
