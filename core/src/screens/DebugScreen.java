package screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.bullets.BulletHelper;
import com.mygdx.cards.Card;
import com.mygdx.objects.Player;
import com.mygdx.objects.Renderable;

public class DebugScreen implements Renderable {
    BitmapFont font;
    Player player;
    BulletHelper bulletHelper;
    ArrayList<Card> cards;
    Vector3 realPos;
    
    public DebugScreen(Player player, ArrayList<Card> cards) {
        this.player = player;
        this.cards = cards;
        bulletHelper = BulletHelper.getBulletHelper();
        font = new BitmapFont();
    }

    public void updateRealPos(Vector3 realpos) {
        this.realPos = realpos;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), realPos.x + 10, realPos.y - 10);
        font.draw(spriteBatch, "Player Pos: " + player.getPlayerX() + ", " + player.getPlayerY(), realPos.x + 10, realPos.y - 30);
        font.draw(spriteBatch, "Current Bullet Count: " + bulletHelper.getBulletAmount(), realPos.x + 10, realPos.y - 50);
        
    }

    
    
}
