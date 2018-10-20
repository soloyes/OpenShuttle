package com.shuttle.game.utils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.conf.GamePreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Shuttle on 7/14/18.
 */
public class BoomBox {
    private Array<Music> musicArray;

    public BoomBox() {
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

    public void setMusicEffectVolume(String music, float volume) {
        if (GamePreferences.isSoundEnabled()) {
            volume *= GamePreferences.getSoundVolume();
            Assets.getInstance()
                    .getAssetManager()
                    .get(music, Music.class)
                    .setVolume(volume);
        }
    }

    public void stopAllMusicAndPlay(String music, String... musicEffect) {
        Map<String, Float> musicWithVolumes = new HashMap<String, Float>();
        for (int i = 0; i < musicEffect.length; i++) {
            musicWithVolumes.put(musicEffect[i],
                    Assets.getInstance()
                            .getAssetManager()
                            .get(musicEffect[i], Music.class).getVolume()
            );
        }
        stopAllMusic();
        playGameMusic(music);
        for (Map.Entry<String, Float> m : musicWithVolumes.entrySet()) {
            playEffectMusic(m.getKey(), m.getValue());
        }
    }

    private void stopAllMusic() {
        musicArray.clear();
        Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            musicArray.get(i).stop();
        }
    }

    private void playGameMusic(String music) {
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

    public void playEffectMusic(String music, float volume) {
        if (GamePreferences.isSoundEnabled()) {
            volume *= GamePreferences.getSoundVolume();
            Assets.getInstance()
                    .getAssetManager()
                    .get(music, Music.class)
                    .setVolume(volume);
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
            playGameMusic(music);
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
