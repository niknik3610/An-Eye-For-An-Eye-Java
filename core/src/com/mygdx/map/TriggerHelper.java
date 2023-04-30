package com.mygdx.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.helpers.AudioHelper;
import com.mygdx.helpers.CONSTANTS;
import com.mygdx.enemies.EnemyHelper;
import com.mygdx.objects.Player;

public class TriggerHelper {
    EnemyHelper enemyHelper;
    MapLayer triggerLayer;
    MapObjects triggerObjects;
    CollisionHelper collisionHelper;
    Player player;
    int counter;
    Texture doorTexture;

    public TriggerHelper(EnemyHelper enemyHelper, Player player, TiledMap map, CollisionHelper collisionHelper){
        this.enemyHelper = enemyHelper;
        this.player = player;
        counter = 0;
        triggerLayer = map.getLayers().get(2);
        triggerObjects = triggerLayer.getObjects();
        for (RectangleMapObject rec : triggerObjects.getByType(RectangleMapObject.class)) {
            scaleRectangle(rec.getRectangle());
        }
        this.collisionHelper = collisionHelper;


        //TODO: Temp texture 288 64

        Pixmap pixmap = new Pixmap((int) (288* CONSTANTS.MAP_UNITSCALE),(int) (64*CONSTANTS.MAP_UNITSCALE), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.fillRectangle(0, 0, (int) (288*CONSTANTS.MAP_UNITSCALE),(int) (64*CONSTANTS.MAP_UNITSCALE));
        this.doorTexture = new Texture(pixmap);


    }

    public void roomTrigger(int roomId){
        switch (roomId){
            case 0:
                trigger_0();
                break;
            case 1:
                trigger_1();
                break;
            case 2:
                trigger_2();
                break;
            case 3:
                trigger_3();
                break;
            case 4:
                trigger_4();
                break;
            case 5:
                trigger_5();
                break;
        }
    }

    private void trigger_0(){
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 1973, 2116);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 3126, 2303);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 2987, 3076);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 3200, 3589);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 1580, 3589);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 1586, 2609);
    }

    private void trigger_1(){
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 583, 5271);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 583, 5923);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 583, 6390);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 1622, 5271);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 1622, 5923);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 1622, 6390);
    }

    private void trigger_2(){
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 3129, 5271);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 3129, 5923);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 3129, 6390);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 4439, 5271);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 4439, 5923);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 4439, 6390);
    }

    private void trigger_3() {
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 3344, 8283);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 3344, 8648);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 3344, 8810);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 3344, 9125);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 1469, 8283);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 1469, 8648);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 1469, 8810);
        enemyHelper.spawnEnemy(CONSTANTS.HOUND_ID, 1469, 9125);

    }

    private void trigger_4(){
        for (RectangleMapObject obj : triggerObjects.getByType(RectangleMapObject.class)) {
                if ((int)obj.getProperties().get("ID") == 4) {
                    obj.getRectangle().y -= 128;
                    collisionHelper.newCollisionObject(obj);
                    MapHelper.addRenderableObject(new DynamicRenderObject(4, new Vector2(obj.getRectangle().x + 24, obj.getRectangle().y), doorTexture));
                }
            }


    }

    private void trigger_5(){
        enemyHelper.spawnEnemy(CONSTANTS.HADES_ID, 2480, 10_500);
        AudioHelper.getInstance().stopMainGameTheme();
        AudioHelper.getInstance().playBossTheme();
    }


    public void checkTriggers() {
        if (counter % CONSTANTS.COLLISION_CHECK_INTERVAL == 0){
        for (RectangleMapObject obj : triggerObjects.getByType(RectangleMapObject.class))
            if (obj.getRectangle().contains(new Vector2(player.getPlayerX(), player.getPlayerY()))) {
                roomTrigger((int)obj.getProperties().get("ID"));
                triggerObjects.remove(obj);
                break;
            }
        }
        counter++;
    }


    private void scaleRectangle(Rectangle rec) {
        rec.set(rec.getX() * CONSTANTS.MAP_UNITSCALE, rec.getY() * CONSTANTS.MAP_UNITSCALE, rec.getWidth() * CONSTANTS.MAP_UNITSCALE, rec.getHeight() * CONSTANTS.MAP_UNITSCALE);
    }

    
}
