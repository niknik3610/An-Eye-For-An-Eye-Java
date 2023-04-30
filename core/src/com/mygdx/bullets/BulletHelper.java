package com.mygdx.bullets;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.objects.Renderable;

public class BulletHelper implements Renderable {
    private ArrayList<Bullet> bullets;
    private static BulletHelper bulletHelper;
    private BulletCollisionHelper bulletCollisionHelper;


    public enum SpecialProperty {
        NORMAL, SLOW, AOE, SLOW_AND_AOE
    }


    public static BulletHelper getBulletHelper(){
        if (bulletHelper == null) {
            bulletHelper = new BulletHelper();
        }
        return bulletHelper;
    }

    public BulletHelper(){
        bullets = new ArrayList<>();
        bulletCollisionHelper = BulletCollisionHelper.getBulletCollisionHelper();
    }

   
    public void spawnBullet(Texture bulletTexture, float bulletSpeed, float bulletSpread, int damage, Vector2 spawnLocation, Vector3 targetLocation, SpecialProperty specialProperties, Boolean playerOwned){
        float newSpread = ((float) Math.random()*bulletSpread*2 - bulletSpread)/2;
        Bullet bullet = new Bullet(bulletTexture, bulletSpeed, spawnLocation, new Vector2(targetLocation.x + newSpread, targetLocation.y + newSpread), damage, specialProperties);
        
        if (playerOwned) {
            bullet.setPlayerOwned();
        }

        bullets.add(bullet);
    }
    
    
    private void destroyBullets(ArrayList<Bullet> toDestroy) {
        for (Bullet bullet : toDestroy){
            bullets.remove(bullet);
        }
    }

    private ArrayList<Bullet> moveBullets(){

        ArrayList<Bullet> toDestroy = new ArrayList<>();
        for (Bullet bullet : bullets){
            bullet.move();
            if (bullet.getHasHit() || bullet.destructionTimer()) {
                toDestroy.add(bullet);
            }
        }
        return toDestroy;
    }

    @Override
    public void update() {
        ArrayList<Bullet> toDestroy = moveBullets();
        destroyBullets(toDestroy);
        bulletCollisionHelper.checkBulletCollisions();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        update();
        for (Bullet bullet : bullets) {
            Vector2 bulletPos = bullet.getBulletPos();
            spriteBatch.draw(bullet.getTexture(), bulletPos.x, bulletPos.y);
        }
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public int getBulletAmount() {
        return bullets.size();
    }

    public BulletCollisionHelper getBulletCollisionHelper(){
        return bulletCollisionHelper;
    }
   
}
