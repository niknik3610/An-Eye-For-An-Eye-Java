package com.mygdx.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Texture bulletTexture;
    private Rectangle bulletHitBox;

    private float time;
    private int damage;
    private Vector2 currPos, moveVector;
   
    private Boolean playerOwned;
    private Boolean hasHit;
    private BulletHelper.SpecialProperty weaponEffect;


    public Bullet(Texture texture, float speed, Vector2 initialPos, Vector2 targetPos, int damage, BulletHelper.SpecialProperty effect) {
        this.bulletTexture = texture;
        weaponEffect = effect;

        this.damage = damage;
        playerOwned = false;
        hasHit = false;
        time = 0;
        speed = speed * 50;
        currPos = new Vector2(initialPos.x, initialPos.y + 2);
        bulletHitBox = new Rectangle(currPos.x, currPos.y, texture.getWidth(), texture.getHeight());
        moveVector = targetPos.sub(currPos).nor().scl(speed);
    }

    public void move() {
        Vector2 tempVector = moveVector.cpy(); 
        tempVector.scl(Gdx.graphics.getDeltaTime());
        currPos.add(tempVector);
        bulletHitBox.x = currPos.x;
        bulletHitBox.y = currPos.y;
    }

    public Boolean destructionTimer() {
        time += Gdx.graphics.getDeltaTime();
        
        if (time > 5) {
            return true;
        }
        return false;
    }

    public Vector2 getBulletPos() {
        return currPos;
    }

    public void setPlayerOwned(){
        playerOwned = true;
    }
    
    public Boolean getHasHit() {
        return hasHit;
    }

    public void setHasHit() {
        hasHit = true;
    }

    public int getDamage() {
        return damage;
    }

    public Rectangle getBulletHitbox() {
        return bulletHitBox;
    }

    public Boolean getPlayerOwned() {
        return playerOwned;
    }

    public Texture getTexture(){
        return bulletTexture;
    }

    public BulletHelper.SpecialProperty getWeaponEffect() {
        return weaponEffect;
    }
    
}
