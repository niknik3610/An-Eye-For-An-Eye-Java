package screens.main_menu_screens.weapon_buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.weapons.SniperGun;

public class SniperGunButton extends AbstractWeaponButton{
    public SniperGunButton() {
        super(new Texture(Gdx.files.internal("gunIcons/sniperGunIcon.png")));

        weapon = new SniperGun(1, 50, 80);
    }
}
