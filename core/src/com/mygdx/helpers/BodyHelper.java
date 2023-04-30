package com.mygdx.helpers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

//This isn't used for anything atm, might not be, if we use BOX2D physics this will be
//used
//factory pattern, named BodyHelper to match other classes
public class BodyHelper {
    private World world;
    private static BodyHelper thisInstance;

    private BodyHelper(World world) {
        this.world = world;
    }

    //returns singleton
    public static BodyHelper getBodyHelper(World world) {
        if (thisInstance == null) {
            thisInstance = new BodyHelper(world);
        }
        return thisInstance;
    }

    private FixtureDef makeFixture(int type, Shape shape) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        // switch (type) {
        //     case CONSTANTS.FIXTYPE_WALL:
        //         fixtureDef.density = 1f;
        //         fixtureDef.friction = 0.5f;
        //         fixtureDef.restitution = 0f;
        //         break;
        //     case CONSTANTS.FIXTYPE_HITBOX:
        //         fixtureDef.density = 1;
        //         fixtureDef.friction = 1f;
        //         fixtureDef.restitution = 1;
        //         break;
        // }
        return fixtureDef;
    }

    public Body makeRectangleBody(Vector2 pos, Vector2 dimensions, int type, BodyType bodyType, Boolean fixedRotation){
        BodyDef rectangleBodyDef = new BodyDef();
        rectangleBodyDef.type = bodyType;
        rectangleBodyDef.position.x = pos.x;
        rectangleBodyDef.position.y = pos.y;
        rectangleBodyDef.fixedRotation = fixedRotation;

        Body rectangleBody = world.createBody(rectangleBodyDef);
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(dimensions.x/2, dimensions.y/2);
        
        rectangleBody.createFixture(makeFixture(type, bodyShape));
        bodyShape.dispose();
        return rectangleBody;
    }
}
