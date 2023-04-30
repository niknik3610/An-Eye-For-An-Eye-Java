package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.helpers.AudioHelper;
import com.mygdx.helpers.BatchHelper;
import com.mygdx.helpers.CameraHelper;
import com.mygdx.objects.Renderable;

import screens.main_menu_screens.CreditsMenu;
import screens.main_menu_screens.HelpMenu;
import screens.main_menu_screens.HighScores;
import screens.main_menu_screens.MainMenu;
import screens.main_menu_screens.WeaponSelectScreen;

public class MainMenuScreen extends ScreenAdapter {
    MainMenuState currentMenuState = MainMenuState.MAIN;
    Renderable currentMenu;
    SpriteBatch batch;

    private Sprite mainMenuBackground;
    MainMenu mainMenu;
    HighScores highScores;
    HelpMenu helpMenu;
    WeaponSelectScreen weaponSelectScreen;
    CreditsMenu creditsMenu;
    private Vector3 realPos;
    private final Vector3 ORIGIN = new Vector3(0,0,0);
    AudioHelper audioHelper;
    public MainMenuScreen(){
        realPos = ORIGIN.cpy();
        CameraHelper.getCamera().unproject(realPos);

        mainMenu = new MainMenu(this);
        highScores = new HighScores(this);
        weaponSelectScreen = new WeaponSelectScreen(this, realPos);
        creditsMenu = new CreditsMenu(this);
        helpMenu = new HelpMenu(this);

        Texture mainMenuTexture = new Texture(Gdx.files.internal("backgrounds/mainMenuBackground.png"));
        mainMenuBackground = new Sprite(mainMenuTexture);
        mainMenuBackground.setCenter(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

        currentMenu = mainMenu;
        batch = BatchHelper.getBatch();
        batch.setProjectionMatrix(CameraHelper.getCamera().combined);

        audioHelper = AudioHelper.getInstance();
        audioHelper.setMainMenuTheme(Gdx.files.internal("sound/instinct.mp3"));
        audioHelper.playMainMenuTheme();

        audioHelper.setMainGameTheme(Gdx.files.internal("sound/epic.mp3"));
        audioHelper.setBossTheme(Gdx.files.internal("sound/evolution.mp3"));
    }
    public enum MainMenuState {
        MAIN, HELP, CHOOSING_WEAPON, HIGH_SCORES, CREDITS
    }


    public void update() {
        CameraHelper.getCamera().position.set(0,0,0);
        batch.setProjectionMatrix(CameraHelper.getCamera().combined);
        realPos = ORIGIN.cpy();
        CameraHelper.getCamera().unproject(realPos);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update();
        batch.begin();
        batch.draw(mainMenuBackground, 0, 0, 1920, 900);
        switch (currentMenuState){
            case MAIN:
                mainMenu.setRealPos(realPos);
                mainMenu.update();
                mainMenu.render(batch);
                break;
            case HIGH_SCORES:
                highScores.setRealPos(realPos);
                highScores.update();
                highScores.render(batch);
                break;
            case CHOOSING_WEAPON:
                weaponSelectScreen.setRealPos(realPos);
                weaponSelectScreen.update();
                weaponSelectScreen.render(batch);
                break;
            case HELP:
                helpMenu.setRealPos(realPos);
                helpMenu.update();
                helpMenu.render(batch);
                break;
            case CREDITS:
                creditsMenu.setRealPos(realPos);
                creditsMenu.update();
                creditsMenu.render(batch);
                break;
            default:
                break;
        }
        batch.end();
    }

    public void setCurrentMenuState(MainMenuState currentMenuState) {
        this.currentMenuState = currentMenuState;
    }
}
