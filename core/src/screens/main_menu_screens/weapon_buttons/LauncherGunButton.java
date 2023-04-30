package screens.main_menu_screens.weapon_buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.weapons.RocketLauncherGun;

public class LauncherGunButton extends AbstractWeaponButton {
    public LauncherGunButton() {
        super(new Texture(Gdx.files.internal("gunIcons/launcherGunIcon.png")));
        weapon = new RocketLauncherGun(2, 0, 20);
    }
}
