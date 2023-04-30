package screens.main_menu_screens.weapon_buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.weapons.SlowingRocketLauncher;

public class SlowGunButton extends AbstractWeaponButton{
    public SlowGunButton() {
        super(new Texture(Gdx.files.internal("gunIcons/slowGunIcon.png")));
        weapon = new SlowingRocketLauncher(2, 0, 20);
    }
}
