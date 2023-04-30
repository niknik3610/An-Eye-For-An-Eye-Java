package com.mygdx.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class DynamicRenderObject {
    public Vector2 pos;
    public int id;
    public Texture texture;

    public DynamicRenderObject(int id, Vector2 pos, Texture texture){
        this.id = id;
        this.pos = pos;
        this.texture = texture;
    }
}
