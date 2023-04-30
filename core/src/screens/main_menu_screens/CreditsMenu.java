package screens.main_menu_screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.mygdx.helpers.CONSTANTS;

import screens.MainMenuScreen;

public class CreditsMenu extends AbstractMenuScreen {

    private CharSequence titleText, mainCreditsText, backButtonText;
    private BitmapFont buttonFontLocal;
    
    private int screenHeight, screenWidth;
    private int backYPos;
    private Sprite textBackground;
    private int marginFromEdge;

    public CreditsMenu(MainMenuScreen mainMenuScreen){
        super(mainMenuScreen);

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        mainCreditsText = "An Eye for An Eye was made as a 2nd year group project for BSc Computer Science at Lancaster Univeristy Leipzig\n\n" +
                        "Code by: Niklas Harnish, Nelli Mugattarova\n\n" +
                        "Original art by: Nelli Mugattarova, Niklas Harnish, Maksim Mardziasau\n\n" +
                        "All external assets used under free for personal use license\n";
        
        titleText = "credits";
        backButtonText = "BACK";
        backYPos = 315 + 110*4;
        hitBoxes.add(new Rectangle(screenWidth/2 - CONSTANTS.BUTTON_TEXT_WIDTH / 2, screenHeight - backYPos - 50, 300, 100));


        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(new FileHandle("assets/fonts/font.ttf"));
        FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 33;
        fontParameter.color = new Color(1,1,1,1);
        fontParameter.shadowColor = Color.BLACK;
        fontParameter.shadowOffsetY = 3;
        fontParameter.shadowOffsetX = 2;

        buttonFontLocal = fontGenerator.generateFont(fontParameter);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.7f));
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.fillRectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.textBackground = new Sprite(new Texture(pixmap));
        pixmap.dispose();

        marginFromEdge = 200;

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
        titleFont.draw(spriteBatch, titleText, 0, screenHeight - 60, screenWidth, Align.center, false);
        buttonFontLocal.draw(spriteBatch, mainCreditsText, marginFromEdge, screenHeight - 300, screenWidth - marginFromEdge*2, backYPos, true);
        buttonFont.draw(spriteBatch, backButtonText, realpos.x + screenWidth/2 - CONSTANTS.BUTTON_TEXT_WIDTH/2, realpos.y - backYPos, CONSTANTS.BUTTON_TEXT_WIDTH, Align.center, true);
    }

    @Override
    protected void onExit() {
        
    }
    
}
