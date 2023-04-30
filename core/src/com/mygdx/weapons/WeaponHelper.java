package com.mygdx.weapons;

import java.util.ArrayList;

public class WeaponHelper {
    private static WeaponHelper weaponHelper;
    private ArrayList<Weapon> playerWeapons;

    private WeaponHelper(){
        playerWeapons = new ArrayList<>();
    }

    public static WeaponHelper getWeaponHelper() {
        if (weaponHelper == null){
            weaponHelper = new WeaponHelper();
        }

        return weaponHelper;
    }

    public void setPlayerWeapons(ArrayList<Weapon> playerWeapons) {
        this.playerWeapons = playerWeapons;
    }

    public void addPlayerWeapon(Weapon weapon){
        playerWeapons.add(weapon);
    }

    public ArrayList<Weapon> getPlayerWeapons(){
        return playerWeapons;
    }
    public void resetPlayerWeapons() {
        playerWeapons = new ArrayList<>();
    }
}
