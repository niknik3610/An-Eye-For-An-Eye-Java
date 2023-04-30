package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraHelper {
    private static OrthographicCamera camera;
    private static ScreenViewport viewport;

    public static OrthographicCamera getCamera() {
        if (camera == null){
            camera = new OrthographicCamera();
		    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            camera.position.set(0,0,0);
            viewport = new ScreenViewport(camera);
        }
        return camera;
    }
    public static OrthographicCamera refreshCamera() {
        camera.position.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
        camera.update();
        return camera;
    }

    public static Viewport getViewport() {
        if (viewport == null) {
            getCamera();
        }
        return viewport;
    }

}
