package com.shuttle.game.utils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.conf.GamePreferences;

/**
 * @author Shuttle on 7/14/18.
 */
public class BoomBox {
    //private GamePreferences preferences;
    private Array<Music> musicArray;

    public BoomBox() {
        //this.preferences = preferences;
        musicArray = new Array<Music>();
    }

    public long playSound(String sound) {
        if (GamePreferences.isSoundEnabled()) {
            return Assets.getInstance()
                    .getAssetManager()
                    .get(sound, Sound.class)
                    .play(GamePreferences.getSoundVolume());
        }
        return -1;
    }

    public void stopAllMusicAndPlay(String music) {
        stopAllMusic();
        playMusic(music);
    }

    public void stopAllMusic() {
        musicArray.clear();
        Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            musicArray.get(i).stop();
        }
    }

    public void playMusic(String music) {
        if (GamePreferences.isMusicEnabled()) {
            Assets.getInstance()
                    .getAssetManager()
                    .get(music, Music.class)
                    .setVolume(GamePreferences.getMusicVolume());
            Assets.getInstance()
                    .getAssetManager()
                    .get(music, Music.class)
                    .play();
            Assets.getInstance()
                    .getAssetManager()
                    .get(music, Music.class)
                    .setLooping(true);
        }
    }

    public void stopAllMusicAndPlayOnce(String music) {
        if (!Assets.getInstance()
                .getAssetManager()
                .get(music, Music.class)
                .isPlaying()) {
            stopAllMusic();
            playMusic(music);
        }
    }

    public void setAllMusicVolume(float volume) {
        musicArray.clear();
        Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            musicArray.get(i).setVolume(volume);
        }
    }
}
