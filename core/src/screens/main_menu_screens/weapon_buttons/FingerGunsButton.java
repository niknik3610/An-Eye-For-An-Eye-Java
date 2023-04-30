package screens.main_menu_screens.weapon_buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.weapons.FingerGuns;

public class FingerGunsButton extends AbstractWeaponButton{
    public FingerGunsButton() {
        super(new Texture(Gdx.files.internal("gunIcons/fingerGunIcon.png")));
        weapon = new FingerGuns(20, 6, 20);
    }
}
