package screens.main_menu_screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.helpers.CONSTANTS;
import screens.MainMenuScreen;

public class MainMenu extends AbstractMenuScreen {
    private CharSequence playText, highScoresText, optionsText, creditsText, exitText;
    private int playYPos, highScoresYPos, optionsYPos, creditsYPos, exitYPos, YPosDist;

    private CharSequence title;

    private int screenWidth, screenHeight;

    public MainMenu(MainMenuScreen mainMenuScreen) {
        super(mainMenuScreen);

        playText = "PLAY";
        highScoresText = "HIGH-SCORES";
        optionsText = "HELP";
        creditsText = "CREDITS";
        exitText = "EXIT";

        YPosDist = 110;

        playYPos = 315;
        highScoresYPos = playYPos + YPosDist;
        optionsYPos = highScoresYPos + YPosDist;
        creditsYPos = optionsYPos + YPosDist;
        exitYPos = creditsYPos + YPosDist;

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        title = "an eye for an eye";
        
        hitBoxes.add(new Rectangle(screenWidth/2 - CONSTANTS.BUTTON_TEXT_WIDTH / 2, screenHeight - playYPos - 50, 300, 100));
        hitBoxes.add(new Rectangle(screenWidth/2 - CONSTANTS.BUTTON_TEXT_WIDTH / 2, screenHeight - highScoresYPos - 50, 300, 100));
        hitBoxes.add(new Rectangle(screenWidth/2 - CONSTANTS.BUTTON_TEXT_WIDTH / 2, screenHeight - optionsYPos - 50, 300, 100));
        hitBoxes.add(new Rectangle(screenWidth/2 - CONSTANTS.BUTTON_TEXT_WIDTH / 2, screenHeight - creditsYPos - 50, 300, 100));
        hitBoxes.add(new Rectangle(screenWidth/2 - CONSTANTS.BUTTON_TEXT_WIDTH / 2, screenHeight - exitYPos - 50, 300, 100));

    }

    @Override
    protected void onExit() {

    }


    @Override
    public void update() {
        int touch = menuHelper.getTouch(hitBoxes);
        switch (touch){
            case 0:
                mainMenuScreen.setCurrentMenuState(MainMenuScreen.MainMenuState.CHOOSING_WEAPON);
                break;
            case 1:
                mainMenuScreen.setCurrentMenuState(MainMenuScreen.MainMenuState.HIGH_SCORES);
                break;
            case 2:
                mainMenuScreen.setCurrentMenuState(MainMenuScreen.MainMenuState.HELP);
                break;
            case 3:
                mainMenuScreen.setCurrentMenuState(MainMenuScreen.MainMenuState.CREDITS);
                break;
            case 4:
                System.exit(0);
                break;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        titleFont.draw(spriteBatch, title, 0, screenHeight - 60, screenWidth, Align.center, false);
        buttonFont.draw(spriteBatch, playText, realpos.x + screenWidth/2 - CONSTANTS.BUTTON_TEXT_WIDTH/2, realpos.y - playYPos, CONSTANTS.BUTTON_TEXT_WIDTH, Align.center, true);
        buttonFont.draw(spriteBatch, highScoresText, realpos.x + screenWidth/2 - CONSTANTS.BUTTON_TEXT_WIDTH/2, realpos.y - highScoresYPos, CONSTANTS.BUTTON_TEXT_WIDTH, Align.center, true);
        buttonFont.draw(spriteBatch, optionsText, realpos.x + screenWidth/2 - CONSTANTS.BUTTON_TEXT_WIDTH/2, realpos.y - optionsYPos, CONSTANTS.BUTTON_TEXT_WIDTH, Align.center, true);
        buttonFont.draw(spriteBatch, creditsText, realpos.x + screenWidth/2 - CONSTANTS.BUTTON_TEXT_WIDTH/2, realpos.y - creditsYPos, CONSTANTS.BUTTON_TEXT_WIDTH, Align.center, true);     
        buttonFont.draw(spriteBatch, exitText, realpos.x + screenWidth/2 - CONSTANTS.BUTTON_TEXT_WIDTH/2, realpos.y - exitYPos, CONSTANTS.BUTTON_TEXT_WIDTH, Align.center, true);
    }
}
