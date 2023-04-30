package com.mygdx.menu;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.helpers.CameraHelper;

public class MenuHelper {
    OrthographicCamera camera;
    private static MenuHelper menuHelper;
    
    public MenuHelper() {
        camera = CameraHelper.getCamera();
    }
   
    /**
     * Checks for clicks on rectangles on screen, returns index
     * @param list The list of rectangles you are checking for
     * @return -1 if nothing is found, otherwise the rectangle's index in the array list (should be it's ID)
     */
    public int getTouch(ArrayList<Rectangle> list) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            int j = 0;
            for (Rectangle rec : list) {
                if (rec.contains(new Vector2(touchPos.x, touchPos.y))) {
                    return j;
                }
                j++;
            }
        }
        return -1;
    }

    public static MenuHelper getMenuHelper() {
        if (menuHelper == null) {
            menuHelper = new MenuHelper();
        }
        return menuHelper;
    }
}
