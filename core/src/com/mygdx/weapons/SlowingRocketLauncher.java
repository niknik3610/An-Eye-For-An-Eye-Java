package com.mygdx.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.bullets.BulletHelper;

public class SlowingRocketLauncher extends Weapon{

    public SlowingRocketLauncher(int fireRate, int damage, int bulletSpeed) {
        super(fireRate, damage, bulletSpeed, 0);

        gunWidth = 40;
        gunHeight = 10;

        weaponXOffset = 10;
        weaponProperty = BulletHelper.SpecialProperty.SLOW_AND_AOE;

        Pixmap pixmap = new Pixmap(gunWidth, gunHeight, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillRectangle(0, 0, gunWidth, gunHeight);
        this.weaponTexture = new Texture(pixmap);

        pixmap = new Pixmap(18, 18, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillRectangle(0, 0, 18, 18);
        this.bulletTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void incrSlow (float percent){
        bulletHelper.getBulletCollisionHelper().incrSlowMult(percent);
    }

}
