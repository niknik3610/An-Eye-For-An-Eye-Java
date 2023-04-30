package com.mygdx.map;

import java.util.ArrayList;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.helpers.CONSTANTS;

//class is in charge of handling map collision detection
public class CollisionHelper{
    
    MapLayer collisionLayer;
    MapObjects collisionObjects;
    Vector2 temp_RectanglePositionVector;
    int counter;
    ArrayList<Rectangle> closeObj;


    public CollisionHelper(TiledMap map) {
        collisionLayer = map.getLayers().get(1);
        collisionObjects = collisionLayer.getObjects();
        temp_RectanglePositionVector = new Vector2(0, 0);
        for (RectangleMapObject rec : collisionObjects.getByType(RectangleMapObject.class)) {
            scaleRectangle(rec.getRectangle());
        }
        counter = 0;
    }

    private void scaleRectangle(Rectangle rec) {
        rec.set(rec.getX() * CONSTANTS.MAP_UNITSCALE, rec.getY() * CONSTANTS.MAP_UNITSCALE, rec.getWidth() * CONSTANTS.MAP_UNITSCALE, rec.getHeight() * CONSTANTS.MAP_UNITSCALE);
    }
    
    //TODO: Add logic for enemy collision
    //TODO: Create method for getting objects close to the player
    /**
     * 
     * @param hitBox
     * @return true if collision is found
     */
    public boolean checkCollisions(Rectangle hitBox) {
        // if (counter % CONSTANTS.COLLISION_CHECK_INTERVAL == 0)
        //     closeObj = getCloseMapObjects(hitBox, collisionObjects.getByType(RectangleMapObject.class));
        
        // counter++;
        for (RectangleMapObject obj : collisionObjects.getByType(RectangleMapObject.class)) {
            if (Intersector.overlaps(obj.getRectangle(), hitBox)) {
                return true;
            }
        }
        return false;
    }
    //adds all objects within a certain range of the given hitbox to a list, to be checked for collisions
    //occurs every CONSTANTS.COLLISION_CHECK_INTERVAL frames
    /*
    private ArrayList<Rectangle> getCloseMapObjects(Rectangle hitBox, Array<RectangleMapObject> array) {
        ArrayList<Rectangle> closeMapObjects = new ArrayList<>();
        for (RectangleMapObject object : array) {
            Rectangle objectRec = object.getRectangle();
            temp_RectanglePositionVector.set(objectRec.x, objectRec.y);
            float dst = temp_RectanglePositionVector.dst(hitBox.x, hitBox.y);
            if (dst < 2000)
                closeMapObjects.add(objectRec);
        }
        return closeMapObjects;
    }
    */

    public ArrayList<Rectangle> getCloseObjs() {
        return closeObj;
    }

    public void newCollisionObject(RectangleMapObject newRectangle) {
        collisionObjects.add(newRectangle);
    }
}
