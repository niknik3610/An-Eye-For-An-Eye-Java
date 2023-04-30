package screens.main_menu_screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.helpers.HighscoreHelper;
import screens.MainMenuScreen;

public class HighScores extends AbstractMenuScreen{
    CharSequence backText, highScoresTitle, highScores;

    public HighScores(MainMenuScreen mainMenuScreen) {
        super(mainMenuScreen);

        backText = "BACK";
        highScoresTitle = "high scores";
        highScores = HighscoreHelper.getHighscoreHelper().getHighscores();

        hitBoxes.add(new Rectangle(75, 50, 300, 100));
    }

    @Override
    protected void onExit() {
    }

    @Override
    public void update() {
        int touch = menuHelper.getTouch(hitBoxes);
        switch (touch){
            case 0:
                mainMenuScreen.setCurrentMenuState(MainMenuScreen.MainMenuState.MAIN);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        ScreenUtils.clear(Color.GRAY);

        titleFont.draw(spriteBatch, highScoresTitle, 0, Gdx.graphics.getHeight() - 60, Gdx.graphics.getWidth(), Align.center, false);
        buttonFont.draw(spriteBatch, highScores, realpos.x + Gdx.graphics.getWidth()/3 - 10, realpos.y - Gdx.graphics.getHeight() * 1/3);
        buttonFont.draw(spriteBatch, backText, realpos.x + 75, realpos.y - Gdx.graphics.getHeight() + 100);
    }
}
