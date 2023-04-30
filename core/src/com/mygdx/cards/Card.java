package com.mygdx.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Align;
import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public abstract class Card {
    protected Sprite sprite;
    protected Texture texture;
    protected int type;
    protected float timer = -1;

    protected SpriteBatch spriteBatch;
    protected BitmapFont titleFont;
    protected BitmapFont mainTextFont;
    protected FreeTypeFontGenerator genTitleFont;
    protected FreeTypeFontGenerator genMainFont;
    protected FreeTypeFontParameter paramTitleFont;
    protected FreeTypeFontParameter paramMainFont;
    protected int mainBodyDrawStart; // increased for long text, don't change otherwise

    protected CharSequence title;
    protected CharSequence mainBody;

    public Card(int mainBodyDrawStart) {
        
        genMainFont = new FreeTypeFontGenerator(new FileHandle("assets/fonts/font.ttf"));
        paramMainFont = new FreeTypeFontParameter();
        genTitleFont = new FreeTypeFontGenerator(new FileHandle("assets/fonts/SatyrPassionate-7OVw.ttf"));
        paramTitleFont = new FreeTypeFontParameter();

        paramTitleFont.size = 50;
        paramTitleFont.color = new Color(6/255f, 89/255f, 31/255f, 1f);

        paramMainFont.size = 25;
        paramMainFont.color = paramTitleFont.color;
        paramMainFont.mono = false;
        paramMainFont.borderWidth = 1;
        paramMainFont.borderColor = paramMainFont.color;

        if(mainBodyDrawStart > 0) this.mainBodyDrawStart = mainBodyDrawStart + 20; // looks best for short text at 20
        titleFont = genTitleFont.generateFont(paramTitleFont);
        mainTextFont = genMainFont.generateFont(paramMainFont);

        texture = new Texture(Gdx.files.internal("cards/card_empty_background.png"));
        sprite = new Sprite(texture);
        spriteBatch = new SpriteBatch();

    }

    public abstract void applyCard(Player player);

    public void fontRender(SpriteBatch spriteBatch, int cardX, int cardY) {
        titleFont.draw(spriteBatch, title, cardX - (CONSTANTS.CARD_TEXT_WIDTH + 50)/2 , cardY + 190, CONSTANTS.CARD_TEXT_WIDTH + 50, Align.center, true);
        mainTextFont.draw(spriteBatch, mainBody, cardX - CONSTANTS.CARD_TEXT_WIDTH/2, cardY + mainBodyDrawStart, CONSTANTS.CARD_TEXT_WIDTH, Align.center, true);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getType() {
        return type;
    }

    public float getTimer() {
        return timer;
    } 
}
