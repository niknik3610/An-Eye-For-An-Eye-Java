package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.mygdx.crawl.GameDriver;
import com.mygdx.helpers.AudioHelper;
import com.mygdx.helpers.CameraHelper;
import com.mygdx.helpers.HighscoreHelper;
import com.mygdx.menu.MenuHelper;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.objects.Renderable;

import java.util.ArrayList;

public class WinScreen implements Renderable {
    private final int FONT_SIZE_BUTTON = 40;
    final Vector3 ORIGIN = new Vector3(0,0,0);
    private ArrayList<Rectangle> hitBoxes;
    private MenuHelper menuHelper;
    private Texture winScreenTexture;
    
    protected CharSequence timeText, menuButtonText, flavorText;
    protected BitmapFont titleFont;
    protected FreeTypeFontGenerator genTitleFont;
    protected FreeTypeFontParameter paramTitleFont;

    BitmapFont buttonFont;
    Camera camera;
    Sprite optionsTempSprite;
    Vector3 realPos;
    int time;

    public WinScreen(){
        menuHelper = MenuHelper.getMenuHelper();
        font_generator();
        flavorText = "You Won!!!";
        hitBoxes = new ArrayList<>();

        winScreenTexture = new Texture(Gdx.files.internal("backgrounds/victoryScreenBackground.png"));

        //main menu button

        camera = CameraHelper.getCamera();
        hitBoxes.add(new Rectangle( Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 +100,
                Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/4 +100, 300, 100));
    }

    public void setTime(float time) {
        timeText = "Your Time: " + Math.round(time)+ " seconds";
        menuButtonText = "Quit";
        this.time = (int) time;
    }

    public void update() {
        camera.position.set(0,0,0);

        realPos = ORIGIN.cpy();
        camera.unproject(realPos);
        for (Rectangle rec : hitBoxes) {
           rec.setPosition(realPos.x + Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - 50,
                   realPos.y - Gdx.graphics.getHeight() + Gdx.graphics.getHeight()/4 - 50);
        }
        int touch = menuHelper.getTouch(hitBoxes);
        switch(touch){
            case 0:
                CameraHelper.refreshCamera();
                onQuit();
                GameDriver.getINSTANCE().setMainMenuScreen();
                break;
        }
    }

    protected void onQuit() {
        HighscoreHelper.getHighscoreHelper().saveNewHighscore(time);
        AudioHelper.getInstance().stopBossTheme();
    }

    public void render(SpriteBatch batch) {
        batch.draw(winScreenTexture, realPos.x, realPos.y - Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        buttonFont.draw(batch, menuButtonText, realPos.x, realPos.y - Gdx.graphics.getHeight() + Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth(), Align.center, false);
        titleFont.draw(batch, flavorText, realPos.x , realPos.y - 100, Gdx.graphics.getWidth(), Align.center, false);
        buttonFont.draw(batch, timeText, realPos.x + Gdx.graphics.getWidth()/2 - 250, realPos.y - 350);
    }

    private void font_generator(){
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(new FileHandle("assets/fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = FONT_SIZE_BUTTON;
        fontParameter.color = new com.badlogic.gdx.graphics.Color(1,1,1,1);
        fontParameter.shadowColor = Color.BLACK;
        fontParameter.shadowOffsetY = 3;
        fontParameter.shadowOffsetX = 2;
        
        buttonFont = fontGenerator.generateFont(fontParameter);

        genTitleFont = new FreeTypeFontGenerator(new FileHandle("assets/fonts/SatyrPassionate-7OVw.ttf"));
        paramTitleFont = new FreeTypeFontParameter();
        paramTitleFont.size = 190;
        paramTitleFont.color = new Color(255/255f, 255/255f, 99/255f, 1f);
        paramTitleFont.borderWidth = 3.3f;
        paramTitleFont.borderColor = new Color(221/255f, 185/255f, 65/255f, 1f);

        titleFont = genTitleFont.generateFont(paramTitleFont);

    }
}
