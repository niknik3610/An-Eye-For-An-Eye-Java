package screens.main_menu_screens.weapon_buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.weapons.VanillaGun;

public class DefaultGunButton extends AbstractWeaponButton{
    public DefaultGunButton() {
        super(new Texture(Gdx.files.internal("gunIcons/defaultGunIcon.png")));

        weapon = new VanillaGun( 8, 15, 40);
    }
}
