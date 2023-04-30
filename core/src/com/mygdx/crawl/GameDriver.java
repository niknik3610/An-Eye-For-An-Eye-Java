package com.mygdx.crawl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import screens.GameScreen;
import screens.MainMenuScreen;

//DO NOT TOUCH!
public class GameDriver extends Game {
	private static GameDriver INSTANCE = null;
	private Screen winScreen;
	private Screen mainMenuScreen;
	SpriteBatch batch;
	Texture img;
	private int windowWidth, windowHeight;

	GameDriver() {
		INSTANCE = this; 
	}

	public static GameDriver getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new GameDriver();
		}
		
		return INSTANCE;
	}
	@Override
	public void create () {
		this.windowWidth = Gdx.graphics.getWidth();
		this.windowHeight = Gdx.graphics.getHeight();
		setMainMenuScreen();
	}

	public void setGameScreen(){
		mainMenuScreen.dispose();
		setScreen(new GameScreen());
	}

	public void setMainMenuScreen(){
		if (winScreen != null) winScreen.dispose();
		setScreen(mainMenuScreen= new MainMenuScreen());
	}

	public void setDeathScreen(){}
	public void exit(Screen screen) {
		Gdx.app.exit();
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	@Override
	public void render() {
		super.render();
	}
}
