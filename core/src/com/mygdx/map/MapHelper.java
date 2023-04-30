package com.mygdx.map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.helpers.CONSTANTS;
import com.mygdx.enemies.EnemyHelper;
import com.mygdx.objects.Player;
import com.mygdx.objects.Renderable;

public class MapHelper implements Renderable{
    TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;
    EnemyHelper enemyHelper;
    Player player;
    public static ArrayList<DynamicRenderObject> renderableObjects;
    
    public MapHelper(OrthographicCamera camera, Player player, EnemyHelper enemyHelper) {
        this.camera = camera;
        this.player = player;
        this.enemyHelper = enemyHelper;
        
        map = new TmxMapLoader().load("assets/maps/mainMap/main.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, CONSTANTS.MAP_UNITSCALE);
        renderableObjects = new ArrayList<>();
    }
    
    @Override
    public void update() {
        mapRenderer.setView(camera);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        update();
        mapRenderer.render();

        for (DynamicRenderObject objects : renderableObjects) {
            spriteBatch.draw(objects.texture, objects.pos.x, objects.pos.y);
        }
    }

    public TiledMap getMap(){
        return map;
    }

    public static void addRenderableObject(DynamicRenderObject object){
        renderableObjects.add(object);
    }

}
