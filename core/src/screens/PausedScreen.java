package screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.crawl.GameDriver;
import com.mygdx.helpers.AudioHelper;
import com.mygdx.helpers.CONSTANTS;
import com.mygdx.helpers.CameraHelper;
import com.mygdx.menu.MenuHelper;
import com.mygdx.objects.Renderable;

public class PausedScreen implements Renderable {
    //hitbox ids
    final private int PAUSE_MAIN_RESUME_HITBOX = 0; 
    final private int PAUSE_MAIN_OPTIONS_HITBOX = 1; 
    final private int PAUSE_MAIN_EXIT_HITBOX = 2;
    final private int PAUSE_OPTIONS_TEMP_HITBOX = 0;
    final private int PAUSE_BACK_HITBOX = 1;
    
    //pausedState ids
    final private int PAUSE_MAIN = 0;
    final private int PAUSE_OPTIONS = 1;

    private int pausedState;

    private ArrayList<Rectangle> mainHitboxes;
    private ArrayList<Rectangle> optionsHitboxes;
    private Sprite resumeSprite, optionsSprite, exitSprite, optionsTempSprite, backSprite;
    private Vector3 realPos;
    private MenuHelper menuHelper;
  
    public PausedScreen(BitmapFont font, OrthographicCamera camera) {
        mainHitboxes = new ArrayList<>();
        optionsHitboxes = new ArrayList<>();
        this.menuHelper = MenuHelper.getMenuHelper();

        //loads all the textures for the buttons
        Texture texture = new Texture(Gdx.files.internal("menu/resume.png"));
        resumeSprite = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("menu/options.png"));
        optionsSprite = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("menu/exit.png"));
        exitSprite = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("menu/optionsTemp.png"));
        optionsTempSprite = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("menu/back.png"));
        backSprite = new Sprite(texture);

        //creates hitboxes for the buttons
        mainHitboxes.add(new Rectangle(0, 0, 400, 70));
        mainHitboxes.add(new Rectangle(0, 0, 400, 70));
        mainHitboxes.add(new Rectangle(0, 0, 400, 70));

        optionsHitboxes.add(new Rectangle(0, 0, 400, 70));
        optionsHitboxes.add(new Rectangle(0 , 0, 400, 70));
        
        realPos = new Vector3(0,0,0);
        pausedState = PAUSE_MAIN;
    }
    
    @Override
    public void render(SpriteBatch spriteBatch) {
        update();
        switch (pausedState) {
            case PAUSE_MAIN:
                pausedMainScreen(spriteBatch);
                break;
            case PAUSE_OPTIONS:
                pausedOptionsScreen(spriteBatch);
                break;
        }
    }

    @Override
    public void update() {
    }

    //used to position pause menu correctly on screen
    public void setRealPos(Vector3 pos) {
        this.realPos = pos;
    }

    private void pausedMainScreen(SpriteBatch spriteBatch) {
        //do not touch!!!
        resumeSprite.setCenter(realPos.x + CONSTANTS.WINDOW_WIDTH/2, realPos.y - 300);
        mainHitboxes.get(PAUSE_MAIN_RESUME_HITBOX).setPosition(resumeSprite.getX(), resumeSprite.getY() + 90);
    
        optionsSprite.setCenter(realPos.x + CONSTANTS.WINDOW_WIDTH/2, realPos.y-450);
        mainHitboxes.get(PAUSE_MAIN_OPTIONS_HITBOX).setPosition(optionsSprite.getX(), optionsSprite.getY() + 90);
  
        exitSprite.setCenter(realPos.x + CONSTANTS.WINDOW_WIDTH/2, realPos.y - 600);
        mainHitboxes.get(PAUSE_MAIN_EXIT_HITBOX).setPosition(exitSprite.getX(), exitSprite.getY() + 90);
        
        
        int touch = menuHelper.getTouch(mainHitboxes);

        switch (touch) {
            case 0:
                GameScreen.state = CONSTANTS.STATE_RUNNING;
                break;
            case 1:
                pausedState = PAUSE_OPTIONS;
                break;
            case 2:
                CameraHelper.refreshCamera();
                AudioHelper.getInstance().stopMainGameTheme();
                AudioHelper.getInstance().stopBossTheme();
                GameDriver.getINSTANCE().setMainMenuScreen();
                break;
        }
        resumeSprite.draw(spriteBatch);
        optionsSprite.draw(spriteBatch);
        exitSprite.draw(spriteBatch);
    }

    private void pausedOptionsScreen(SpriteBatch spriteBatch) {
        optionsTempSprite.setCenter(realPos.x + CONSTANTS.WINDOW_WIDTH/2, realPos.y - 300);
        optionsHitboxes.get(PAUSE_OPTIONS_TEMP_HITBOX).setPosition(optionsTempSprite.getX(), optionsTempSprite.getY() + 90);
        backSprite.setCenter(realPos.x + 120, realPos.y - CONSTANTS.WINDOW_HEIGHT + 50);
        optionsHitboxes.get(PAUSE_BACK_HITBOX).setPosition(backSprite.getX(), backSprite.getY() + 90);

        int touch = menuHelper.getTouch(optionsHitboxes);
        switch (touch) {
            case 0:
                break;
            case 1:
                pausedState = PAUSE_MAIN;
                break;
        }

        optionsTempSprite.draw(spriteBatch);
        backSprite.draw(spriteBatch);
    }
}
