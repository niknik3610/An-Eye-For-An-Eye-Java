package com.mygdx.helpers;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.cards.Card;
import com.mygdx.cards.CardHelper;
import com.mygdx.enemies.EnemyHelper;
import com.mygdx.menu.MenuHelper;
import com.mygdx.objects.Player;
import com.mygdx.objects.Renderable;
import screens.DebugScreen;
import screens.GameScreen;
import screens.PausedScreen;

public class HudHelper implements Renderable {
    //used for positioning HUD on screen
    final int FONTSIZE = 40;
    private Vector3 realPos;
    private Vector3 hudOrigin;

    private OrthographicCamera camera;
    
    private BitmapFont font;
    private Texture pauseTexture;
    private Sprite pauseBackground;

    private Player player;
    
    public Boolean cardHUD;
    private ArrayList<Card> cards;

    private CardHelper cardHelper;
    private PausedScreen pausedScreen;
    private MenuHelper menuHelper;
    private EnemyHelper enemyHelper;
    private AudioHelper audioHelper;

    private Sprite healthBar;
    private Sprite healthBarBackground;
    private Sprite healthContents;

    private DebugScreen debugScreen;
    private Float cardTimer;

    public HudHelper(OrthographicCamera camera, Player player, EnemyHelper enemyHelper, AudioHelper audioHelper) {
        //default hud values, gets unprojected to match camera position
        hudOrigin = new Vector3(0, 0, 0);
        this.camera = camera;

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(new FileHandle("assets/fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = FONTSIZE;
        fontParameter.color = new Color(1,1,1,1);
        font = fontGenerator.generateFont(fontParameter);

        this.player = player;
        this.enemyHelper = enemyHelper;
        this.audioHelper = audioHelper;

        cardHUD = false;
        cardTimer = 0f;

        //black screen used for background of card and pause menu
        Pixmap pixmap = new Pixmap(CONSTANTS.WINDOW_WIDTH + 100, CONSTANTS.WINDOW_HEIGHT + 100, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(0.0f, 0.0f, 0.0f, 0.8f);
        pixmap.fillRectangle(0, 0, CONSTANTS.WINDOW_WIDTH + 100, CONSTANTS.WINDOW_HEIGHT + 100);
        pauseTexture = new Texture(pixmap);
        pauseBackground = new Sprite(pauseTexture);
        pixmap.dispose();
        
        //Sets up healthbar sprites
        Texture texture = new Texture(Gdx.files.internal("hud/health.png"));
        healthBar = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("hud/healthBackground.png"));
        healthBarBackground = new Sprite(texture);

        //Sets up healthbar contents
        pixmap = new Pixmap(CONSTANTS.MAXIMUM_BAR_WIDTH, 21, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.GREEN);
        pixmap.fillRectangle(0, 0, CONSTANTS.MAXIMUM_BAR_WIDTH, 21);
        texture = new Texture(pixmap);
        pixmap.dispose();
        healthContents = new Sprite(texture);

        menuHelper = MenuHelper.getMenuHelper();
        pausedScreen = new PausedScreen(font, camera);
        font.getData().setScale(1f);

        debugScreen = new DebugScreen(player, cards);
    }
    
    @Override
    public void update() {
        realPos = hudOrigin.cpy();
        camera.unproject(realPos);

        debugScreen.updateRealPos(realPos);
        //draws health bar
        healthBar.setPosition(realPos.x + 20, realPos.y - CONSTANTS.WINDOW_HEIGHT + 20);
        float playerHealthDisplay = ((float) player.getPlayerHP()) / player.getPlayerMaxHp() * CONSTANTS.MAXIMUM_BAR_WIDTH;
        if (playerHealthDisplay < 0) playerHealthDisplay = 0;
        healthContents.setSize(playerHealthDisplay, 21);
        healthContents.setPosition(healthBar.getX() + 5, healthBar.getY() + 5);
        healthBarBackground.setPosition(healthBar.getX(), healthBar.getY());
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        update();

        switch (GameScreen.state) {
            case CONSTANTS.STATE_RUNNING:
                playHud(spriteBatch);
                break;
            case CONSTANTS.STATE_LEVELUP:
                cardHud(spriteBatch);
                break;
            case CONSTANTS.STATE_PAUSED:
                pausedHud(spriteBatch);
                break;
        }
    }

    public void drawCardHud(ArrayList<Card> cards) {
        this.cards = cards;
        audioHelper.dispose();
        GameScreen.state = CONSTANTS.STATE_LEVELUP;
    }

    public void setCardHelper(CardHelper cardHelper) {
        this.cardHelper = cardHelper;
    }

    private void playHud(SpriteBatch spriteBatch) {
        //timer display TODO: Turn time to minutes + seconds and maybe optomize

        //TODO: Update with textures health and XP, turn XP into percentage value
        // temp health display 
        // font.draw(spriteBatch, "Health: " + player.getPlayerHP() + " / " + player.getPlayerMaxHp(), realPos.x + 10 , realPos.y - CONSTANTS.WINDOW_HEIGHT + 20);
        healthBarBackground.draw(spriteBatch);
        healthContents.draw(spriteBatch);
        healthBar.draw(spriteBatch);
        
        //temp xp display
        font.draw(spriteBatch, "XP: " + player.getPlayerXP() + " / " + player.xpForLvlUp, realPos.x + Gdx.graphics.getWidth() - 250, realPos.y - Gdx.graphics.getHeight() + 60);
        font.draw(spriteBatch, "Time: " + Math.round(GameScreen.currentInGameTime), realPos.x + Gdx.graphics.getWidth() - 200, realPos.y - 40);

        if (player.getDebugBoolean()) {
            debugScreen.render(spriteBatch);
            enemyHelper.setDebugMode(true);
        }
        else {
            enemyHelper.setDebugMode(false);
        }
    }

    private void cardHud(SpriteBatch spriteBatch) {
        pauseBackground.setCenter(player.getPlayerX() - 20, player.getPlayerY() - 20);
        pauseBackground.draw(spriteBatch);

        //draws cards on the screen
        Vector2 playerCoords = new Vector2(player.getPlayerX(), player.getPlayerY());
        ArrayList<Sprite> tSprites = new ArrayList<>();
        int i = 0;
        if (cards.size() == 2) {
            i = -CONSTANTS.CARD_OFFSET/2;
        }
        if (cards.size() == 3) {
            i = -CONSTANTS.CARD_OFFSET;
        }
        float posX, posY;
        for (Card card : cards) {
            posX = playerCoords.x + i;
            posY = playerCoords.y;

            Sprite cardSprite = card.getSprite();
            tSprites.add(cardSprite);
            
            cardSprite.setCenter(posX, posY);
            
            i += CONSTANTS.CARD_OFFSET;
            cardSprite.draw(spriteBatch);
            card.fontRender(spriteBatch, (int) posX, (int) (posY));
        }
        
        //checks for clicks 
        ArrayList<Rectangle> cardHitBoxes = new ArrayList<>();
        for (Sprite sprite : tSprites) {
            cardHitBoxes.add(new Rectangle(sprite.getX(), sprite.getY(), CONSTANTS.CARD_WIDTH, CONSTANTS.CARD_HEIGHT));
        }

        if (cardTimer > 0.7f) {
            int touchPos = menuHelper.getTouch(cardHitBoxes);
            if (touchPos < 0) {
                return;
            }

            //applies selected card, adds other cards back to pile, switches back to running game state
            cardHelper.applyCard(cards.remove(touchPos));
            cardHelper.addCards(cards);
            cardHUD = false;
            audioHelper.dispose();
            GameScreen.state = CONSTANTS.STATE_RUNNING;
            cardTimer = 0f;
        }
        cardTimer+= Gdx.graphics.getDeltaTime();
    }

    private void pausedHud(SpriteBatch spriteBatch) {
        pausedScreen.setRealPos(realPos);
        pauseBackground.setCenter(player.getPlayerX() - 20, player.getPlayerY() - 20);
        pauseBackground.draw(spriteBatch);
        pausedScreen.render(spriteBatch);
    }


}
