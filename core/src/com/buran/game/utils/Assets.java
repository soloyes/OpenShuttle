package com.buran.game.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author soloyes on 03/06/18.
 */

public class Assets {
    private static Assets instance = new Assets();
    private AssetManager assetManager;
    private TextureAtlas atlas;

    private Assets() {
        this.assetManager = new AssetManager();
    }

    public void clear() {
        assetManager.clear();
    }

    public void loadAssets(ScreenManager.ScreenType type) {
        switch (type) {
            case MENU:
                createStandardFont(32, "fonts/college.regular", "");
                createStandardFont(54, "fonts/college.regular", "");
                createStandardFont(48, "fonts/college.regular", "");
                createStandardFont(48, "fonts/college.regular", "");
                createStandardFont(44, "fonts/college.regular", "");
                createStandardFont(64, "fonts/college.regular", "");
                createStandardFont(72, "fonts/college.regular", "");
                createStandardFont(96, "fonts/college.regular", "");
                createStandardFont(128, "fonts/college.regular", "");
                createStandardFont(188, "fonts/college.regular", "");
                createStandardFont(188, "fonts/college.regular", "");
                createStandardFont(256, "fonts/college.regular", "");
                assetManager.load(com.buran.game.conf.MSConstants.MENU_SONG, Music.class);
                assetManager.load(com.buran.game.conf.PaintConstants.GAME_PACK, TextureAtlas.class);
                assetManager.load(com.buran.game.conf.PaintConstants.SKIN_FILE, Skin.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_MENU, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_CHANGED, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_PROFILE_CHANGED, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_PROFILE_SAVED, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_INPUT, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_CLICK, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_EMPTY_NAME, Sound.class);
                break;
            case GAME:
                createStandardFont(32, "fonts/college.regular", "");
                createStandardFont(32, "fonts/college.regular", "Coloured");
                createStandardFont(36, "fonts/college.regular", "");
                createStandardFont(44, "fonts/college.regular", "");
                createStandardFont(48, "fonts/college.regular", "");
                createStandardFont(48, "fonts/college.regular", "Coloured");
                createStandardFont(54, "fonts/college.regular", "");
                createStandardFont(64, "fonts/college.regular", "");
                createStandardFont(64, "fonts/college.regular", "Coloured");
                createStandardFont(60, "fonts/college.regular", "");
                createStandardFont(60, "fonts/college.regular", "Coloured");
                createStandardFont(72, "fonts/college.regular", "");
                createStandardFont(96, "fonts/college.regular", "");
                createStandardFont(128, "fonts/college.regular", "");
                createStandardFont(128, "fonts/college.regular", "Coloured");
                createStandardFont(188, "fonts/college.regular", "");
                createStandardFont(256, "fonts/college.regular", "");
                assetManager.load(com.buran.game.conf.PaintConstants.GAME_PACK, TextureAtlas.class);
                assetManager.load(com.buran.game.conf.MSConstants.MENU_SONG, Music.class);
                assetManager.load(com.buran.game.conf.MSConstants.GAME_SONG, Music.class);
                assetManager.load(com.buran.game.conf.MSConstants.GAME_OVER, Music.class);
                assetManager.load(com.buran.game.conf.MSConstants.BLACKHOLE_ROAR_MUSIC, Music.class);
                assetManager.load(com.buran.game.conf.MSConstants.RADAR_WHITE_NOISE_MUSIC, Music.class);
                assetManager.load(com.buran.game.conf.MSConstants.HUM_MUSIC_EFFECT_1, Music.class);
                assetManager.load(com.buran.game.conf.MSConstants.HUM_MUSIC_EFFECT_2, Music.class);
                assetManager.load(com.buran.game.conf.MSConstants.HUM_MUSIC_EFFECT_3, Music.class);
                assetManager.load(com.buran.game.conf.MSConstants.HUM_MUSIC_EFFECT_4, Music.class);
                assetManager.load(com.buran.game.conf.MSConstants.HUM_MUSIC_EFFECT_5, Music.class);
                assetManager.load(com.buran.game.conf.MSConstants.CHARGING_ROCKETS, Music.class);
                assetManager.load(com.buran.game.conf.MSConstants.LASER, Music.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_MENU, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_PAUSE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_LEFT_RIGHT, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_SWITCH, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_CLICK, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DESTROY_CORROSIVE_ANOMALY, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DESTROY_MAGNETIC_ANOMALY, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DESTROY_RADIATION_ANOMALY, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DESTROY_ELECTRIC_ANOMALY, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GET_ASTRONAUT, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DESTROY_ASTRONAUT, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DESTROY_CRATE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DESTROY_STORAGE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.LOSE_RADAR, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GET_BOOST, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UP, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.LEVEL_UP, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GET_FUEL, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GET_TRASH, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GET_RADAR, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GET_FRAGMENT, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.TELEPORT, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.KICKED_BY_CORROSIVE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.KICKED_BY_ELECTRIC, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DESTROY_ASTEROID, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.NEW_ASTRONAUT, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.LOSE_FUEL, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.LOSE_SCORE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.LOW_LIFE_WARNING, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DOWN, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GET_REPAIR, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.OUT_OF_SCREEN, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.START_ENGINE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.SPLASH_ANOMALY, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GET_SPLASH, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.START_ENGINE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.LAUNCH_ROCKETS, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DESTROY_ASTEROID_RAY, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DESTROY_FUEL, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.BURST_CORROSIVE_ANOMALY, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.TRASH_ASTEROIDS, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_CHANGED, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GET_SCANNER, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GET_SHIELD, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GET_LASER, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GET_ROCKETS, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.KILL_MAGNETIC_RADIATION, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.GENERATE_CRATE_STORAGE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.ADD_SELECTABLE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.ADD_SCORE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.ANOMALY_FLUSH, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DEL_HP_FUEL, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.ANOMALIES_BORN, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DEL_CRATE_STORAGE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DISABLE_LASER_RADAR, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.DEL_SCORE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.ADD_CORROSIVE, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.ADD_HP, Sound.class);
                assetManager.load(com.buran.game.conf.PaintConstants.SKIN_FILE, Skin.class);
                break;
            case TEST:
                assetManager.load(com.buran.game.conf.PaintConstants.GAME_PACK, TextureAtlas.class);
                assetManager.load(com.buran.game.conf.PaintConstants.SKIN_FILE, Skin.class);
                createStandardFont(32, "fonts/college.regular", "");
                createStandardFont(64, "fonts/college.regular", "");
                createStandardFont(96, "fonts/college.regular", "");
                break;
            case ADS:
                assetManager.load(com.buran.game.conf.PaintConstants.GAME_PACK, TextureAtlas.class);
                assetManager.load(com.buran.game.conf.PaintConstants.SKIN_FILE, Skin.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_CLICK, Sound.class);
                assetManager.load(com.buran.game.conf.MSConstants.UI_MENU, Sound.class);
                break;
        }
    }

    private void createStandardFont(int size, String font, String postfix) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameter.fontFileName = font + ".ttf";
        fontParameter.fontParameters.size = size;
        fontParameter.fontParameters.color = Color.valueOf("ffffffff");
        fontParameter.fontParameters.borderWidth = 1;
        fontParameter.fontParameters.borderColor = Color.valueOf("000000ff");
        fontParameter.fontParameters.shadowOffsetX = 3;
        fontParameter.fontParameters.shadowOffsetY = 3;
        fontParameter.fontParameters.shadowColor = Color.valueOf("000000ff");
        assetManager.load(font + size + postfix + ".ttf", BitmapFont.class, fontParameter);
    }

    public void makeLinks() {
        atlas = assetManager.get(com.buran.game.conf.PaintConstants.GAME_PACK, TextureAtlas.class);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public static Assets getInstance() {
        return instance;
    }
}