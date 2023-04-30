package com.mygdx.helpers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class AudioHelper extends ApplicationAdapter{

    private static AudioHelper theAudioHelper = null;

    Sound sound;
    Music mainMenuTheme;
    Music mainGameTheme;
    Music mainBossTheme;

    private AudioHelper(){
    }    

    public static AudioHelper getInstance() {
        if(theAudioHelper == null){
            theAudioHelper = new AudioHelper();
        }
        return theAudioHelper;
    }

    public void setSound(FileHandle file){
        sound = Gdx.audio.newSound(file);
    }
    
    public long playSound(){
        long id = sound.play(0.4f);
        return id;
    }

    public void disposeSound(){
        sound.dispose();
    }

    public void setMainMenuTheme(FileHandle file) {
        mainMenuTheme = Gdx.audio.newMusic(file);
        mainMenuTheme.setVolume(.2f);
    }

    public void playMainMenuTheme() {
        mainMenuTheme.setLooping(true);
        mainMenuTheme.play();
    }

    public void stopMainMenuTheme() {
        mainMenuTheme.pause();
        mainMenuTheme.dispose();
    }

    public void setMainGameTheme(FileHandle file) {
        mainGameTheme = Gdx.audio.newMusic(file);
        mainGameTheme.setVolume(.2f);
    }

    public void playMainGameTheme() {
        mainGameTheme.setLooping(true);
        mainGameTheme.play();
    }

    public void stopMainGameTheme() {
        mainGameTheme.pause();
        mainGameTheme.dispose();
    }

    public void setBossTheme(FileHandle file) {
        mainBossTheme = Gdx.audio.newMusic(file);
        mainBossTheme.setVolume(.2f);
    }

    public void playBossTheme() {
        mainBossTheme.setLooping(true);
        mainBossTheme.play();
    }

    public void stopBossTheme() {
        mainBossTheme.pause();
        mainBossTheme.dispose();
    }
}
