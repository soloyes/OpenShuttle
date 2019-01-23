package com.shuttle.game.utils;

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
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.ScreenManager;

public class Assets {
    private static Assets ourInstance = new Assets();
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
                assetManager.load(MSConstants.MENU_SONG, Music.class);
                assetManager.load(MSConstants.BLACKHOLE_ROAR_MUSIC, Music.class);
                assetManager.load(PaintConstants.GAME_PACK, TextureAtlas.class);
                assetManager.load(PaintConstants.SKIN_FILE, Skin.class);

                assetManager.load(MSConstants.UI_MENU, Sound.class);
                assetManager.load(MSConstants.UI_CHECKBOX, Sound.class);
                createStandartFont(32, "");
                createStandartFont(48, "");
                createStandartFont(64, "");
                createStandartFont(96, "");
                break;
            case GAME:
                assetManager.load(PaintConstants.GAME_PACK, TextureAtlas.class);
                assetManager.load(MSConstants.MENU_SONG, Music.class);
                assetManager.load(MSConstants.GAME_SONG, Music.class);
                assetManager.load(MSConstants.BLACKHOLE_ROAR_MUSIC, Music.class);
                assetManager.load(MSConstants.RADAR_WHITE_NOISE_MUSIC, Music.class);
                assetManager.load(MSConstants.HUM_MUSIC_EFFECT_1, Music.class);
                assetManager.load(MSConstants.HUM_MUSIC_EFFECT_2, Music.class);
                assetManager.load(MSConstants.HUM_MUSIC_EFFECT_3, Music.class);
                assetManager.load(MSConstants.HUM_MUSIC_EFFECT_4, Music.class);
                assetManager.load(MSConstants.HUM_MUSIC_EFFECT_5, Music.class);
                assetManager.load(MSConstants.CHARGING_BOMBS, Music.class);
                assetManager.load(MSConstants.LASER, Music.class);
                assetManager.load(MSConstants.UI_MENU, Sound.class);
                assetManager.load(MSConstants.UI_PAUSE, Sound.class);
                assetManager.load(MSConstants.DESTROY_CORROSIVE_ANOMALY, Sound.class);
                assetManager.load(MSConstants.DESTROY_MAGNETIC_ANOMALY, Sound.class);
                assetManager.load(MSConstants.DESTROY_RADIATION_ANOMALY, Sound.class);
                assetManager.load(MSConstants.DESTROY_ELECTRIC_ANOMALY, Sound.class);
                assetManager.load(MSConstants.GET_ASTRONAUT, Sound.class);
                assetManager.load(MSConstants.DESTROY_ASTRONAUT, Sound.class);
                assetManager.load(MSConstants.DESTROY_CRATE, Sound.class);
                assetManager.load(MSConstants.DESTROY_STORAGE, Sound.class);
                assetManager.load(MSConstants.LOSE_RADAR, Sound.class);
                assetManager.load(MSConstants.GET_BOOST, Sound.class);
                assetManager.load(MSConstants.UP, Sound.class);
                assetManager.load(MSConstants.GET_FUEL, Sound.class);
                assetManager.load(MSConstants.GET_TRASH, Sound.class);
                assetManager.load(MSConstants.GET_RADAR, Sound.class);
                assetManager.load(MSConstants.GET_FRAGMENT, Sound.class);
                assetManager.load(MSConstants.TELEPORT, Sound.class);
                assetManager.load(MSConstants.KICKED_BY_CORROSIVE, Sound.class);
                assetManager.load(MSConstants.KICKED_BY_ELECTRIC, Sound.class);
                assetManager.load(MSConstants.DESTROY_ASTEROID, Sound.class);
                assetManager.load(MSConstants.NEW_ASTRONAUT, Sound.class);
                assetManager.load(MSConstants.LOSE_FUEL, Sound.class);
                assetManager.load(MSConstants.LOSE_SCORE, Sound.class);
                assetManager.load(MSConstants.LOW_LIFE_WARNING, Sound.class);
                assetManager.load(MSConstants.DOWN, Sound.class);
                assetManager.load(MSConstants.GET_REPAIR, Sound.class);
                assetManager.load(MSConstants.OUT_OF_SCREEN, Sound.class);
                assetManager.load(MSConstants.START_ENGINE, Sound.class);
                assetManager.load(MSConstants.SPLASH_ANOMALY, Sound.class);
                assetManager.load(MSConstants.GET_SPLASH, Sound.class);
                assetManager.load(MSConstants.START_ENGINE, Sound.class);
                assetManager.load(MSConstants.LAUNCH_BOMBS, Sound.class);
                assetManager.load(MSConstants.DESTROY_ASTEROID_RAY, Sound.class);
                assetManager.load(MSConstants.DESTROY_FUEL, Sound.class);
                assetManager.load(MSConstants.BURST_CORROSIVE_ANOMALY, Sound.class);
                assetManager.load(MSConstants.UI_CHECKBOX, Sound.class);
                assetManager.load(MSConstants.GET_SCANNER, Sound.class);
                assetManager.load(MSConstants.GET_SHIELD, Sound.class);
                assetManager.load(MSConstants.GET_LASER, Sound.class);
                assetManager.load(MSConstants.GET_BOMBS, Sound.class);
                assetManager.load(PaintConstants.SKIN_FILE, Skin.class);
                createStandartFont(32, "");
                createStandartFont(32, "Coloured");
                createStandartFont(48, "");
                createStandartFont(64, "");
                createStandartFont(64, "Coloured");
                createStandartFont(96, "");
                createStandartFont(128, "");
                break;
            case TEST:
                assetManager.load(PaintConstants.GAME_PACK, TextureAtlas.class);
                assetManager.load(PaintConstants.SKIN_FILE, Skin.class);
                createStandartFont(32, "");
                createStandartFont(64, "");
                createStandartFont(96, "");
                break;
        }
    }

    private void createStandartFont(int size, String postfix) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameter.fontFileName = "fonts/gomarice.ttf";
        fontParameter.fontParameters.size = size;
        fontParameter.fontParameters.color = Color.valueOf("ffffffff");
        fontParameter.fontParameters.borderWidth = 1;
        fontParameter.fontParameters.borderColor = Color.valueOf("000000ff");
        fontParameter.fontParameters.shadowOffsetX = 3;
        fontParameter.fontParameters.shadowOffsetY = 3;
        fontParameter.fontParameters.shadowColor = Color.valueOf("000000ff");
        assetManager.load("fonts/gomarice" + size + postfix + ".ttf", BitmapFont.class, fontParameter);
    }

    public void makeLinks() {
        atlas = assetManager.get(PaintConstants.GAME_PACK, TextureAtlas.class);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public static Assets getInstance() {
        return ourInstance;
    }
}
