package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.helpers.AudioHelper;

public class DeathScreen extends WinScreen{
    private Texture lossScreenTexture;
    
    public DeathScreen() {
        super();
        flavorText = "You died!!!";

        lossScreenTexture = new Texture(Gdx.files.internal("backgrounds/lossScreenBackground.png"));
    }

    @Override
    protected void onQuit() {
        AudioHelper.getInstance().stopMainGameTheme();
    }

    @Override
    public void setTime(float time) {
        super.setTime(time);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(lossScreenTexture, realPos.x, realPos.y - Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        buttonFont.draw(batch, menuButtonText, realPos.x, realPos.y - Gdx.graphics.getHeight() + Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth(), Align.center, false);
        titleFont.draw(batch, flavorText, realPos.x , realPos.y - 100, Gdx.graphics.getWidth(), Align.center, false);
        buttonFont.draw(batch, timeText, realPos.x + Gdx.graphics.getWidth()/2 - 250, realPos.y - 350);
    }
}
