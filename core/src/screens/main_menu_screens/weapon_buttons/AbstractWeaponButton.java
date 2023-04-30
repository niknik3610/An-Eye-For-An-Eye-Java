package screens.main_menu_screens.weapon_buttons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.objects.Renderable;
import com.mygdx.weapons.Weapon;

public abstract class AbstractWeaponButton implements Renderable {
    Texture normalTexture;
    public int x, y;
    public Rectangle hitBox;
    public int index;
    protected Weapon weapon;

    public AbstractWeaponButton(Texture normalTexture){
        this.normalTexture = normalTexture;

        x = 0;
        y = 0;
        hitBox = new Rectangle(0, 0, 100, 100);
    }

    public Weapon getWeapon(){
        return weapon;
    }

    public void update(){
        hitBox.setPosition(x, y);
    }

    public void render(SpriteBatch spriteBatch){
        update();
        spriteBatch.draw(normalTexture, x, y);
        return;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getHeight(){
        return normalTexture.getHeight();
    }

    public int getWidth(){
        return normalTexture.getWidth();
    }

}
