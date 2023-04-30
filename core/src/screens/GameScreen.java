package screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.bullets.BulletCollisionHelper;
import com.mygdx.bullets.BulletHelper;
import com.mygdx.cards.CardHelper;
import com.mygdx.helpers.AudioHelper;
import com.mygdx.helpers.BatchHelper;
import com.mygdx.helpers.CONSTANTS;
import com.mygdx.helpers.CameraHelper;
import com.mygdx.enemies.EnemyHelper;
import com.mygdx.map.TriggerHelper;
import com.mygdx.helpers.HudHelper;
import com.mygdx.map.MapHelper;
import com.mygdx.map.CollisionHelper;
import com.mygdx.objects.Player;


//incharge of updating the screen 60 times a second
public class GameScreen extends ScreenAdapter{
    //player is an object representing the player, contains weapons
    public static float currentInGameTime;
    private Player player;
    private BulletHelper bulletHelper;
    //the sprite batch contains the textures to be drawn each frame
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private CollisionHelper collisionHelper;
    private MapHelper mapHelper;
    private TriggerHelper triggerHelper;

    private HudHelper hudHelper;
    private CardHelper cardHelper;
    private EnemyHelper enemyHelper;
    private AudioHelper audioHelper;

    WinScreen winscreen;
    DeathScreen deathScreen;
    Sprite tutScreen;

    //is used by almost every aspect of the game, therefore set to public
    static public int state;

    public GameScreen() {
        currentInGameTime = 0;
        this.audioHelper = AudioHelper.getInstance();

        audioHelper.dispose();
        state = CONSTANTS.STATE_TUTORIAL;

        //sets-up the camera
        this.camera = CameraHelper.getCamera();
        camera.position.set(CONSTANTS.WINDOW_WIDTH/2, CONSTANTS.WINDOW_HEIGHT/2, 0);
        //this.viewport = CameraHelper.getViewport();
        cardHelper = new CardHelper();

        //loads and sets-up the map;
        mapHelper = new MapHelper(camera, player, enemyHelper);
        collisionHelper = new CollisionHelper(mapHelper.getMap());


        //instantiates the new player
        this.player = new Player((float) 2483, (float) 1100, camera, collisionHelper, cardHelper, audioHelper);

        //sets-up the hud
        enemyHelper = new EnemyHelper(player, collisionHelper);
        triggerHelper = new TriggerHelper(enemyHelper, player, mapHelper.getMap(), collisionHelper);
        hudHelper = new HudHelper(camera, player, enemyHelper, audioHelper);
        winscreen = new WinScreen();
        deathScreen = new DeathScreen();
        tutScreen = new Sprite(new Texture(Gdx.files.internal("backgrounds/tutorial_screen.png")));
        //sets-up the batch
        batch = BatchHelper.getBatch();
        this.cardHelper.setHudHelper(hudHelper);
        this.hudHelper.setCardHelper(cardHelper);
        this.cardHelper.setPlayer(player);
        bulletHelper = BulletHelper.getBulletHelper();

        BulletCollisionHelper.getBulletCollisionHelper().setEnemyHelper(enemyHelper);
        BulletCollisionHelper.getBulletCollisionHelper().setPlayer(player);

    }

   
    public void update() {   
//        viewport.getCamera().position.set(player.getPlayerX(), player.getPlayerY(), 0);
        switch (state) {
            case CONSTANTS.STATE_MAIN_MENU:
                break;
            case CONSTANTS.STATE_RUNNING:
                updateRunning();
                break;
            case CONSTANTS.STATE_PAUSED:
                updatePaused();
                break;
            case CONSTANTS.STATE_WIN:
                updateWinScreen();
                break;
            case CONSTANTS.STATE_DEATH:
                updateDeathScreen();
                break;
            case CONSTANTS.STATE_TUTORIAL:
                updateRunning();
                updateTutorialScreen();
                break;
        }
        triggerHelper.checkTriggers();
    }

    int tempTimer = 0;
    private void updateRunning() {
        //updates camera every frame
        currentInGameTime += Gdx.graphics.getDeltaTime();

        camera.position.set(player.getPlayerX(), player.getPlayerY(),0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    private void updatePaused() {
    }

    private void updateTutorialScreen() {
        updateRunning();
        Vector3 origin = new Vector3(0, Gdx.graphics.getHeight() - 200, 0);
        camera.unproject(origin);
        tutScreen.setPosition(origin.x + 75, origin.y - 225);

        if (currentInGameTime > 10) {
            audioHelper.dispose();
            state = CONSTANTS.STATE_RUNNING;
        }
    }
    private void updateDeathScreen() {
        deathScreen.setTime(currentInGameTime);
        deathScreen.update();
    }
    private void updateWinScreen() {
        winscreen.setTime(currentInGameTime);
        winscreen.update();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(Color.BLACK);
//        viewport.apply(true);
        //updates camera position
        update();
        //draws everything within the batch, then flushes the batch
        batch.begin();
        
        switch (state) {
            case CONSTANTS.STATE_RUNNING:
                mapHelper.render(batch);
                enemyHelper.render(batch);
                player.render(batch);
                bulletHelper.render(batch);
                break;
            case CONSTANTS.STATE_WIN:
                winscreen.render(batch);
                break;
            case CONSTANTS.STATE_DEATH:
                deathScreen.render(batch);
                break;
            case CONSTANTS.STATE_TUTORIAL:
                mapHelper.render(batch);
                enemyHelper.render(batch);
                player.render(batch);
                bulletHelper.render(batch);
                tutScreen.draw(batch);
                break;
        }

        hudHelper.render(batch);
        batch.end();
    }
}
