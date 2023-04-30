package com.mygdx.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class VanillaGun extends Weapon {
    public VanillaGun(int fireRate, int damage, int bulletSpeed) {
        super(fireRate, damage, bulletSpeed, 4);
        gunWidth = 35;
        gunHeight = 10;
        
        weaponXOffset = 10;
        Pixmap pixmap = new Pixmap(gunWidth, gunHeight, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.BROWN);
        pixmap.fillRectangle(0, 0, gunWidth, gunHeight);
        this.weaponTexture = new Texture(pixmap);

        pixmap = new Pixmap(12, 12, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillRectangle(0, 0, 12, 12);
        this.bulletTexture = new Texture(pixmap);

        pixmap.dispose();
    }

}
