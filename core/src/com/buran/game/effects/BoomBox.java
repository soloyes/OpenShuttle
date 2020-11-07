package com.buran.game.effects;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author soloyes on 7/14/18.
 */

public class BoomBox {
    private static Array<Sound> soundIdList = new Array<Sound>();
    private static Array<Music> musicArray = new Array<Music>();
    private static ArrayList<String> musicEffects = new ArrayList<String>();
    private float t = 0.0f;

    static {
        musicEffects.add(com.buran.game.conf.MSConstants.HUM_MUSIC_EFFECT_1);
        musicEffects.add(com.buran.game.conf.MSConstants.HUM_MUSIC_EFFECT_2);
        musicEffects.add(com.buran.game.conf.MSConstants.HUM_MUSIC_EFFECT_3);
        musicEffects.add(com.buran.game.conf.MSConstants.HUM_MUSIC_EFFECT_4);
        musicEffects.add(com.buran.game.conf.MSConstants.HUM_MUSIC_EFFECT_5);
    }

    public void playRandomMusicEffects(float dt) {
        t += dt;
        if (t >= 1.0f) {
            if (MathUtils.random(0, 100) <= com.buran.game.conf.Rules.MUSIC_EFFECT_PROBABILITY) {
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
        if (com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().isEffects()) {
            volume *= com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().overallEffectsVolume();
            com.buran.game.utils.Assets.getInstance()
                    .getAssetManager()
                    .get(musicEffect, Music.class)
                    .setVolume(volume);
        }
    }

    private boolean isPlaying(String music) {
        return com.buran.game.utils.Assets.getInstance()
                .getAssetManager()
                .get(music, Music.class)
                .isPlaying();
    }

    public void playMusicEffectOnce(String musicEffect) {
        setMusicEffectVolume(musicEffect, com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().overallEffectsVolume());
        if (com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().isEffects()) {
            if (!isPlaying(musicEffect)) {
                com.buran.game.utils.Assets.getInstance()
                        .getAssetManager()
                        .get(musicEffect, Music.class)
                        .play();
            }
        }
    }

    public long playSound(String sound) {
        Sound tmp = com.buran.game.utils.Assets.getInstance()
                .getAssetManager()
                .get(sound, Sound.class);
        soundIdList.add(tmp);
        if (com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().isEffects()) {
            return tmp.play(com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().overallEffectsVolume());
        }
        return -1;
    }

    public void stopAllMusicAndPlayThese(String music, String... musicEffect) {
        Map<String, Float> musicWithVolumes = new HashMap<String, Float>();
        for (int i = 0; i < musicEffect.length; i++) {
            musicWithVolumes.put(musicEffect[i],
                    com.buran.game.utils.Assets.getInstance()
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
        com.buran.game.utils.Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            musicArray.get(i).stop();
        }
    }

    public void pauseAllMusic() {
        musicArray.clear();
        com.buran.game.utils.Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            musicArray.get(i).pause();
        }
    }

    public void resumeAllMusic() {
        musicArray.clear();
        com.buran.game.utils.Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            musicArray.get(i).play();
        }
    }

    public void resumeThisMusic(String music) {
        musicArray.clear();
        com.buran.game.utils.Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            playMusicInLoop(music);
        }
    }

    private void playMusicInLoop(String music) {
        if (com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().isMusic()) {
            com.buran.game.utils.Assets.getInstance()
                    .getAssetManager()
                    .get(music, Music.class)
                    .setVolume(com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().overallMusicVolume());
            com.buran.game.utils.Assets.getInstance()
                    .getAssetManager()
                    .get(music, Music.class)
                    .setLooping(true);
            if (!isPlaying(music)) {
                com.buran.game.utils.Assets.getInstance()
                        .getAssetManager()
                        .get(music, Music.class)
                        .play();
            }
        }
    }

    public void playMusicEffectInLoop(String musicEffect, float volume) {
        if (com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().isEffects()) {
            volume *= com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().overallEffectsVolume();
            com.buran.game.utils.Assets.getInstance()
                    .getAssetManager()
                    .get(musicEffect, Music.class)
                    .setVolume(volume);
            com.buran.game.utils.Assets.getInstance()
                    .getAssetManager()
                    .get(musicEffect, Music.class)
                    .setLooping(true);
            if (!isPlaying(musicEffect)) {
                com.buran.game.utils.Assets.getInstance()
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
        com.buran.game.utils.Assets.getInstance()
                .getAssetManager()
                .get(musicEffect, Music.class)
                .stop();
    }

    public void setAllMusicVolume(float volume) {
        musicArray.clear();
        com.buran.game.utils.Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            musicArray.get(i).setVolume(volume);
        }
    }
}