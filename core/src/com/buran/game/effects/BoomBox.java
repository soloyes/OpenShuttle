package com.buran.game.effects;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.Rules;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.utils.Assets;

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
        if (Profiles.getSettings().isEffects()) {
            volume *= Profiles.getSettings().overallEffectsVolume();
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
        setMusicEffectVolume(musicEffect, Profiles.getSettings().overallEffectsVolume());
        if (Profiles.getSettings().isEffects()) {
            if (!isPlaying(musicEffect)) {
                Assets.getInstance()
                        .getAssetManager()
                        .get(musicEffect, Music.class)
                        .play();
            }
        }
    }

    public long playSound(String sound) {
        // A click can land right after a screen change cleared the assets; skip, don't crash.
        if (!Assets.getInstance().getAssetManager().isLoaded(sound, Sound.class)) {
            return -1;
        }
        Sound tmp = Assets.getInstance()
                .getAssetManager()
                .get(sound, Sound.class);
        if (!soundIdList.contains(tmp, true)) {
            soundIdList.add(tmp);
        }
        if (Profiles.getSettings().isEffects()) {
            return tmp.play(Profiles.getSettings().overallEffectsVolume());
        }
        return -1;
    }

    private static String lastMusic;
    private static String[] lastEffects = new String[0];

    /**
     * Restarts whatever music the current screen asked for last. Browsers keep audio muted
     * until the first user gesture; music started before that is replayed once it is allowed.
     */
    public static void replayLast() {
        if (lastMusic == null || !Assets.getInstance().getAssetManager().isLoaded(lastMusic, Music.class)) {
            return;
        }
        BoomBox b = new BoomBox();
        b.stopAllMusic();
        b.stopAllMusicAndPlayThese(lastMusic, lastEffects);
    }

    public void stopAllMusicAndPlayThese(String music, String... musicEffect) {
        lastMusic = music;
        lastEffects = musicEffect;
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

    public void pauseAllMusic() {
        musicArray.clear();
        Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            musicArray.get(i).pause();
        }
    }

    public void resumeAllMusic() {
        musicArray.clear();
        Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            musicArray.get(i).play();
        }
    }

    public void resumeThisMusic(String music) {
        musicArray.clear();
        Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
        for (int i = 0; i < musicArray.size; i++) {
            playMusicInLoop(music);
        }
    }

    private void playMusicInLoop(String music) {
        if (Profiles.getSettings().isMusic()) {
            Assets.getInstance()
                    .getAssetManager()
                    .get(music, Music.class)
                    .setVolume(Profiles.getSettings().overallMusicVolume());
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
        if (Profiles.getSettings().isEffects()) {
            volume *= Profiles.getSettings().overallEffectsVolume();
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
        lastMusic = music;
        lastEffects = new String[0];
        if (!isPlaying(music)) {
            stopAllMusic();
            playMusicInLoop(music);
        }
    }

    /**
     * Sounds belong to the AssetManager and are disposed when it is cleared on a screen change.
     * Forget them so stopAllSound() never touches a disposed sound (crashes on the web backend).
     */
    public static void forgetSounds() {
        soundIdList.clear();
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
            // Only songs follow the music slider; looping effects (hum, laser, roar) are effects.
            String name = Assets.getInstance().getAssetManager().getAssetFileName(musicArray.get(i));
            if (name != null && name.startsWith(MSConstants.SONG_FOLDER)) {
                musicArray.get(i).setVolume(volume);
            }
        }
    }
}