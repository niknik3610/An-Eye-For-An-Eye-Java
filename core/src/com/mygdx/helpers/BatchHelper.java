package com.mygdx.helpers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BatchHelper {
    private static SpriteBatch batch;

    public static SpriteBatch getBatch() {
        if (batch == null){
            batch = new SpriteBatch();
        }
        return batch;
    }
    public static SpriteBatch refreshBatch() {
        batch = new SpriteBatch();
        return batch;
    }
}
