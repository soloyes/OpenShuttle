package com.shuttle.game.utils;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.conf.GamePreferences;

/**
 * @author Shuttle on 7/14/18.
 */
public class BoomBox {
    private GamePreferences preferences;

    public BoomBox(GamePreferences preferences) {
        this.preferences = preferences;
    }

    public void play(String music){

        Array<Music> musicArray = new Array<Music>();
        Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            musicArray.get(i).stop();
        }

        if (preferences.isMusicEnabled()) {
            Assets.getInstance()
                    .getAssetManager()
                    .get(music, Music.class)
                    .setVolume(preferences.getMusicVolume());
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
}
