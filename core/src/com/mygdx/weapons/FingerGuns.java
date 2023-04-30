package com.mygdx.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class FingerGuns extends Weapon{
    public FingerGuns(int fireRate, int damage, int bulletSpeed) {
        super(fireRate, damage, bulletSpeed, 60);

        //gun size
        gunWidth = 20;
        gunHeight = 7;

        //this determines where the gun is on the characters body 
        weaponXOffset = 24;
        Pixmap pixmap = new Pixmap(gunWidth, gunHeight, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.PINK);
        pixmap.fillRectangle(0, 0, gunWidth, gunHeight);
        this.weaponTexture = new Texture(pixmap);

        pixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillRectangle(0, 0, 8, 8);
        this.bulletTexture = new Texture(pixmap);
        pixmap.dispose();
    }
    
}
