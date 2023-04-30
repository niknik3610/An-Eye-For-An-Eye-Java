package com.mygdx.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.bullets.BulletHelper;

public class RocketLauncherGun extends Weapon {
    public RocketLauncherGun(int fireRate, int damage, int bulletSpeed) {
        super(fireRate, damage, bulletSpeed, 5);
        //gun size
        gunWidth = 30;
        gunHeight = 15;

        //this determines where the gun is on the characters body
        weaponXOffset = 24;
        Pixmap pixmap = new Pixmap(gunWidth, gunHeight, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.GREEN);
        pixmap.fillRectangle(0, 0, gunWidth, gunHeight);
        this.weaponTexture = new Texture(pixmap);

        pixmap = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillRectangle(0, 0, 20, 20);
        this.bulletTexture = new Texture(pixmap);
        weaponProperty = BulletHelper.SpecialProperty.AOE;
        pixmap.dispose();
    }
}
