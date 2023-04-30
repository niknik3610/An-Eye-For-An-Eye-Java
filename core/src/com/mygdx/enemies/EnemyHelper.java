package com.mygdx.enemies;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.helpers.CONSTANTS;
import com.mygdx.map.CollisionHelper;
import com.mygdx.objects.Player;

public class EnemyHelper {
    private ArrayList<Enemy> spawnedEnemies;
    private Player player;
    private Texture enemyDebugTexture;
    private CollisionHelper collisionHelper;
    private Texture[] textureList;
    
    private final int DOG_LEFT = 0, DOG_RIGHT = 1, DOG_EXCLAMATION = 2, HADES = 3;

    public EnemyHelper(Player player, CollisionHelper collisionHelper) {
        spawnedEnemies = new ArrayList<Enemy>();
        this.player = player;
        this.collisionHelper = collisionHelper;

        //creates the debug texture
        Pixmap pixmap = new Pixmap(15, 15, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillRectangle(0, 0, 15, 15);
        enemyDebugTexture = new Texture(pixmap);
        pixmap.dispose();

        textureList = new Texture[4];
        textureList[DOG_LEFT] = new Texture(Gdx.files.internal("enemies/hell_hound/hell_hound_default_left.png"));
        textureList[DOG_RIGHT] = new Texture(Gdx.files.internal("enemies/hell_hound/hell_hound_default_right.png"));
        textureList[HADES] = new Texture(Gdx.files.internal("enemies/hades/hades.png"));
    }

    public void render(SpriteBatch spritebatch) {
        //checkEnemyCollisions();
        for (Enemy enemy : spawnedEnemies) {
            enemy.setPlayerPos(player.getPlayerX(), player.getPlayerY());
            enemy.render(spritebatch);
        }
    }

    public void spawnEnemy(int ENEMY_ID, float x, float y) {
        switch (ENEMY_ID) {
            case CONSTANTS.HOUND_ID:
                Dog dog = new Dog(x, y, this, enemyDebugTexture, collisionHelper, textureList[DOG_RIGHT], textureList[DOG_LEFT], textureList[DOG_EXCLAMATION]);
                spawnedEnemies.add(dog);
                break;
            case CONSTANTS.HARPIE_ID:
                //TODO: Create Harpie Here
                break;
            case CONSTANTS.HADES_ID:
                Hades hades = new Hades(x, y, this, enemyDebugTexture, collisionHelper, textureList[HADES]);
                spawnedEnemies.add(hades);
                break;
            default:
                System.out.println("This is not a recognized enemy type");
                break;
        }
    }
    

    public void attack(int damage) {
        player.playerDamage(damage);
    }

    public void despawn(Enemy enemy) {
        spawnedEnemies.remove(enemy);
    }

    public void setDebugMode(Boolean debug) {
        for (Enemy enemy : spawnedEnemies) {
            enemy.setDebugMode(debug);
        }
    }

    public void playerSeen() {
        for (Enemy enemy : spawnedEnemies){
            enemy.nextState = Enemy.State.CHASE;
        }
    }

    public ArrayList<Enemy> getSpawnedEnemies() {
        return spawnedEnemies;
    }
}
