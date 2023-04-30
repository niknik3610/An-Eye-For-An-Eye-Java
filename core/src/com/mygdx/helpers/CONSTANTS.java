package com.mygdx.helpers;

public class CONSTANTS {
    public static final int STATE_MAIN_MENU = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_LEVELUP = 3;
    public static final int STATE_WIN = 4;
    public static final int STATE_DEATH = 5;
    public static final int STATE_TUTORIAL = 6;

    public static final int PPM = 8;
    public static final int PLAYER_HEIGHT = 128;
    public static final int PLAYER_WIDTH = 80;
    public static final int WEAPON_OFFSET_Y = 40;
    public static final int INIT_NUM_WEAPONS = 2;

    public static final int WINDOW_HEIGHT = 900;
    public static final int WINDOW_WIDTH = 1920;
    public static final int MAX_FPS = 500;

    //card ratio is 1.4:1
    public static final int CARD_HEIGHT = 560;
    public static final int CARD_WIDTH = 400; 
    public static final int CARD_OFFSET = 500;
    public static final int CARD_TEXT_WIDTH = 250;     // the width of the main text block
    public static final int BUTTON_TEXT_WIDTH = 300;

    //Enemy Types
    public static final int HOUND_ID = 0;
    public static final int HARPIE_ID = 1;
    public static final int HADES_ID = 666;

    //Card Types
    public static final int ON_CHOSEN = 0;          //applies when card is chosen
    public static final int ON_KILL = 1;            //applies when player kills an enemy
    public static final int ON_PLAYER_DMG = 2;      //applies when player takes damage
    public static final int ON_ENEMY_DMG = 3;       //applies when player deals damage 
    public static final int ON_PLAYER_DEATH = 4;    //applies on player death

    //Action types
    public static final int ACTION_MOVE = 0;
    public static final int ACTION_ATTACK = 1;

    public static final int MAXIMUM_BAR_WIDTH = 189;

    //collision detection array is reset every x amount of frames
    public static final int COLLISION_CHECK_INTERVAL = 10;

    public static final float MAP_UNITSCALE = 2f;
}
