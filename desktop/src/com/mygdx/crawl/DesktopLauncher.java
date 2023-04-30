package com.mygdx.crawl;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.helpers.CONSTANTS;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
//DO NOT TOUCH!
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(CONSTANTS.MAX_FPS);
		config.setTitle("An Eye for an Eye");
		config.setWindowedMode(CONSTANTS.WINDOW_WIDTH, CONSTANTS.WINDOW_HEIGHT);
		new Lwjgl3Application(GameDriver.getINSTANCE(), config);
	}
}
