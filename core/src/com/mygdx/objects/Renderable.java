package com.mygdx.objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

//anything that needs to be rendered within the GameScreen needs to implement this interface!
public interface Renderable {
    public void update();
    public void render(SpriteBatch spriteBatch);
}
