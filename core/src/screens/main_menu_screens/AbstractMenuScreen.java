package screens.main_menu_screens;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.menu.MenuHelper;
import com.mygdx.objects.Renderable;
import screens.MainMenuScreen;

import java.util.ArrayList;

public abstract class AbstractMenuScreen implements Renderable {
    private final int FONT_SIZE_BUTTON = 40;
    MainMenuScreen mainMenuScreen;
    MenuHelper menuHelper;
    BitmapFont buttonFont, titleFont;
    ArrayList<Rectangle> hitBoxes;
    protected Vector3 realpos;

    public AbstractMenuScreen(MainMenuScreen mainMenuScreen){
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(new FileHandle("assets/fonts/font.ttf"));
        FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = FONT_SIZE_BUTTON;
        fontParameter.color = new Color(1,1,1,1);
        fontParameter.shadowColor = Color.BLACK;
        fontParameter.shadowOffsetY = 3;
        fontParameter.shadowOffsetX = 2;

        buttonFont = fontGenerator.generateFont(fontParameter);

        FreeTypeFontGenerator genTitleFont = new FreeTypeFontGenerator(new FileHandle("assets/fonts/SatyrPassionate-7OVw.ttf"));
        FreeTypeFontParameter paramTitleFont = new FreeTypeFontParameter();
        paramTitleFont.size = 190;
        paramTitleFont.color = new Color(255/255f, 255/255f, 99/255f, 1f);
        paramTitleFont.borderWidth = 3.3f;
        paramTitleFont.borderColor = new Color(221/255f, 185/255f, 65/255f, 1f);
        
        titleFont = genTitleFont.generateFont(paramTitleFont);

        hitBoxes = new ArrayList<com.badlogic.gdx.math.Rectangle>();

        menuHelper = MenuHelper.getMenuHelper();

        this.mainMenuScreen = mainMenuScreen;
    }
    public void setRealPos (Vector3 realPos) {
        this.realpos = realPos;
    }
    protected abstract void onExit();
}

