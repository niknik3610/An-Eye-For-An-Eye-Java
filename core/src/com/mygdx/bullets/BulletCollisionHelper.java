package com.mygdx.bullets;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.enemies.EnemyHelper;
import com.mygdx.enemies.Enemy;
import com.mygdx.objects.Player;

public class BulletCollisionHelper {
    public static BulletCollisionHelper bulletCollisionHelper;
    private Player player;
    private EnemyHelper enemyHelper;

    private float slowMult;

    private BulletCollisionHelper() {
        slowMult = 1;
    }

    public void checkBulletCollisions() {
        BulletHelper bulletHelper = BulletHelper.getBulletHelper();
        ArrayList<Enemy> toDestroy = new ArrayList<>();
        for (Bullet bullet : bulletHelper.getBullets()) {
            for (Enemy enemy : enemyHelper.getSpawnedEnemies()) {
                if (enemy.getHitBox().contains(bullet.getBulletPos()) && bullet.getPlayerOwned()) {
                    bullet.setHasHit();
                    handleEffects(enemy, bullet);
                    if (enemy.damageToEnemy(bullet.getDamage())) {
                        player.killTrigger(enemy.getXP());
                        toDestroy.add(enemy);
                    }
                    break;
                }
                else if (player.getPlayerHitbox().contains(bullet.getBulletPos()) && !bullet.getPlayerOwned()){
                    player.playerDamage(bullet.getDamage());
                    bullet.setHasHit();
                    break;
                }
            }
        }        
        for (Enemy enemy: toDestroy) {
            enemyHelper.despawn(enemy);
        }
    }

    public static BulletCollisionHelper getBulletCollisionHelper() {
        if (bulletCollisionHelper == null) {
            bulletCollisionHelper = new BulletCollisionHelper();
        }
        return bulletCollisionHelper;
    }

    private void AOE(Vector3 explosionPos) {
        for (Enemy enemy : enemyHelper.getSpawnedEnemies()) {
            if (enemy.getEntityPos().dst(explosionPos) < 300) {
                enemy.damageToEnemy(40);
            }
        }
    }

    private void AOE_SLOW(Vector3 explosionPos, float slowRatioInDecimal) {
        for (Enemy enemy : enemyHelper.getSpawnedEnemies()) {
            if (enemy.getEntityPos().dst(explosionPos) < 500) {
                enemy.setSlow(slowRatioInDecimal);
            }
        }
    }

    private void handleEffects(Enemy enemy, Bullet bullet){
        switch (bullet.getWeaponEffect()) {
            case NORMAL:
                break;
            case SLOW:
                enemy.setSlow(0.5f * slowMult);
                break;
            case AOE:
                AOE(new Vector3(bullet.getBulletPos().x, bullet.getBulletPos().y, 0));
                break;
            case SLOW_AND_AOE:
                AOE_SLOW(new Vector3(bullet.getBulletPos().x, bullet.getBulletPos().y, 0), 0.3f * slowMult);
                break;
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setEnemyHelper(EnemyHelper enemyHelper){
        this.enemyHelper = enemyHelper;
    }

    public void incrSlowMult(float percent){
        slowMult += percent/100f;
    }
}
