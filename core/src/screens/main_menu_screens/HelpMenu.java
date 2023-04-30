package screens.main_menu_screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.mygdx.helpers.CONSTANTS;
import screens.MainMenuScreen;

public class HelpMenu extends AbstractMenuScreen{

    CharSequence helpText, helpTitle, backText;
    BitmapFont textfont;
    Sprite textBackground;


    public HelpMenu(MainMenuScreen mainMenuScreen) {
        super(mainMenuScreen);
        backText = "BACK";
        helpTitle = "Help";
        helpText = "Goal: \n"  +
                "The goal of this game is to reach the end of the given level in the\n" +
                "least amount of time possible. Collect XP by 'freeing' enemy souls.\n" +
                "Level up, to collect cards and to increase  aphrodite's power\n" +
                "\nWeapons: \n" +
                "Finger Guns: Two pistols with low damage, but an exceptionally fast fire rate\n" +
                "Vanilla Weapon: An Assault-Rifle, medium fire-rate, strong damage\n" +
                "Love Launcher: High, Area-of-affect damage, with a low fire rate\n" +
                "Bow of Eros: High damage, slow fire rate. Slows enemy that are hit\n" +
                "Charmer: No damage, but Area-of-affect slows, applied in a large radius";

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(new FileHandle("assets/fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 35;
        fontParameter.color = new Color(1,1,1,1);
        textfont = fontGenerator.generateFont(fontParameter);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.7f));
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.fillRectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.textBackground = new Sprite(new Texture(pixmap));
        pixmap.dispose();

        hitBoxes.add(new Rectangle(75, 50, 300, 100));
    }

    @Override
    public void update() {
        textBackground.setPosition(realpos.x, realpos.y - Gdx.graphics.getHeight());

        int touch = menuHelper.getTouch(hitBoxes);
        switch (touch){
            case 0:
                mainMenuScreen.setCurrentMenuState(MainMenuScreen.MainMenuState.MAIN);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        textBackground.draw(spriteBatch);
        buttonFont.draw(spriteBatch, helpTitle, realpos.x + Gdx.graphics.getWidth()/2 - CONSTANTS.BUTTON_TEXT_WIDTH/2, realpos.y - Gdx.graphics.getHeight()/6, CONSTANTS.BUTTON_TEXT_WIDTH, Align.center, true);
        textfont.draw(spriteBatch, helpText, realpos.x+  Gdx.graphics.getWidth()/2 - 850 , realpos.y - Gdx.graphics.getHeight()/6 - 100, 1700, Align.center, true);
        buttonFont.draw(spriteBatch, backText, realpos.x + 75, realpos.y - Gdx.graphics.getHeight() + 100);
    }

    @Override
    protected void onExit() {

    }
}
