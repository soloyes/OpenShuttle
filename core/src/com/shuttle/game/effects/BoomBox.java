package com.shuttle.game.effects;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Shuttle on 7/14/18.
 */

public class BoomBox {
    private static Array<Sound> soundIdList = new Array<Sound>();
    private static Array<Music> musicArray = new Array<Music>();
    private static ArrayList<String> musicEffects = new ArrayList<String>();
    private float t = 0.0f;

    static {
        musicEffects.add(MSConstants.HUM_MUSIC_EFFECT_1);
        musicEffects.add(MSConstants.HUM_MUSIC_EFFECT_2);
        musicEffects.add(MSConstants.HUM_MUSIC_EFFECT_3);
        musicEffects.add(MSConstants.HUM_MUSIC_EFFECT_4);
        musicEffects.add(MSConstants.HUM_MUSIC_EFFECT_5);
    }

    public void playRandomMusicEffects(float dt) {
        t += dt;
        if (t >= 1.0f) {
            if (MathUtils.random(0, 100) <= Rules.MUSIC_EFFECT_PROBABILITY) {
                boolean flag = true;
                for (int i = 0; i < musicEffects.size(); i++) {
                    if (isPlaying(musicEffects.get(i))) {
                        flag = false;
                    }
                }
                if (flag) {
                    String musicEffect = musicEffects.get(MathUtils.random(0, musicEffects.size() - 1));
                    playMusicEffectOnce(musicEffect);
                }
            }
            t = 0.0f;
        }
    }

    public void setMusicEffectVolume(String musicEffect, float volume) {
        if (GamePreferences.isEffectsEnabled()) {
            volume *= GamePreferences.getOverallEffectsVolume();
            Assets.getInstance()
                    .getAssetManager()
                    .get(musicEffect, Music.class)
                    .setVolume(volume);
        }
    }

    private boolean isPlaying(String music) {
        return Assets.getInstance()
                .getAssetManager()
                .get(music, Music.class)
                .isPlaying();
    }

    public void playMusicEffectOnce(String musicEffect) {
        setMusicEffectVolume(musicEffect, GamePreferences.getOverallEffectsVolume());
        if (GamePreferences.isEffectsEnabled()) {
            if (!isPlaying(musicEffect)) {
                Assets.getInstance()
                        .getAssetManager()
                        .get(musicEffect, Music.class)
                        .play();
            }
        }
    }

    public long playSound(String sound) {
        Sound tmp = Assets.getInstance()
                .getAssetManager()
                .get(sound, Sound.class);
        soundIdList.add(tmp);
        if (GamePreferences.isEffectsEnabled()) {
            return tmp.play(GamePreferences.getOverallEffectsVolume());
        }
        return -1;
    }

    public void stopAllMusicAndPlayThese(String music, String... musicEffect) {
        Map<String, Float> musicWithVolumes = new HashMap<String, Float>();
        for (int i = 0; i < musicEffect.length; i++) {
            musicWithVolumes.put(musicEffect[i],
                    Assets.getInstance()
                            .getAssetManager()
                            .get(musicEffect[i], Music.class).getVolume()
            );
        }
        stopAllMusic();
        playMusicInLoop(music);
        for (Map.Entry<String, Float> m : musicWithVolumes.entrySet()) {
            playMusicEffectInLoop(m.getKey(), m.getValue());
        }
    }

    private void stopAllMusic() {
        musicArray.clear();
        Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            musicArray.get(i).stop();
        }
    }

    private void playMusicInLoop(String music) {
        if (GamePreferences.isMusicEnabled()) {
            Assets.getInstance()
                    .getAssetManager()
                    .get(music, Music.class)
                    .setVolume(GamePreferences.getOverallMusicVolume());
            Assets.getInstance()
                    .getAssetManager()
                    .get(music, Music.class)
                    .setLooping(true);
            if (!isPlaying(music)) {
                Assets.getInstance()
                        .getAssetManager()
                        .get(music, Music.class)
                        .play();
            }
        }
    }

    public void playMusicEffectInLoop(String musicEffect, float volume) {
        if (GamePreferences.isEffectsEnabled()) {
            volume *= GamePreferences.getOverallEffectsVolume();
            Assets.getInstance()
                    .getAssetManager()
                    .get(musicEffect, Music.class)
                    .setVolume(volume);
            Assets.getInstance()
                    .getAssetManager()
                    .get(musicEffect, Music.class)
                    .setLooping(true);
            if (!isPlaying(musicEffect)) {
                Assets.getInstance()
                        .getAssetManager()
                        .get(musicEffect, Music.class)
                        .play();
            }
        }
    }

    public void stopAllMusicAndPlayThis(String music) {
        if (!isPlaying(music)) {
            stopAllMusic();
            playMusicInLoop(music);
        }
    }

    public void stopAllSound() {
        for (int i = 0; i < soundIdList.size; i++) {
            soundIdList.get(i).stop();
        }
    }

    public void stopMusicEffect(String musicEffect) {
        Assets.getInstance()
                .getAssetManager()
                .get(musicEffect, Music.class)
                .stop();
    }

    public void setAllMusicVolume(float volume) {
        musicArray.clear();
        Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            musicArray.get(i).setVolume(volume);
        }
    }
}