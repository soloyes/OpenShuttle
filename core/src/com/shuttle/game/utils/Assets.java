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
                assetManager.load(MSConstants.MENU_MUSIC, Music.class);
                assetManager.load(MSConstants.BLACKHOLE_ROAR_MUSIC, Music.class);
                assetManager.load(PaintConstants.GAME_PACK, TextureAtlas.class);
                assetManager.load(PaintConstants.SKIN_FILE, Skin.class);

                assetManager.load(MSConstants.BUTTON_SOUND, Sound.class);
                assetManager.load(MSConstants.CHECKBOX, Sound.class);
                createSandartFont(32, "");
                createSandartFont(48, "");
                createSandartFont(64, "");
                createSandartFont(96, "");
                break;
            case GAME:
                assetManager.load(PaintConstants.GAME_PACK, TextureAtlas.class);
                assetManager.load(MSConstants.MENU_MUSIC, Music.class);
                assetManager.load(MSConstants.GAME_MUSIC, Music.class);
                assetManager.load(MSConstants.BLACKHOLE_ROAR_MUSIC, Music.class);
                assetManager.load(MSConstants.RADAR_WHITE_NOISE_MUSIC, Music.class);
                assetManager.load(MSConstants.HUM_1, Music.class);
                assetManager.load(MSConstants.HUM_2, Music.class);
                assetManager.load(MSConstants.HUM_3, Music.class);
                assetManager.load(MSConstants.HUM_4, Music.class);
                assetManager.load(MSConstants.HUM_5, Music.class);
                assetManager.load(MSConstants.POWERING_ENGINE, Music.class);
                assetManager.load(MSConstants.POWERING_ROCKET, Music.class);
                assetManager.load(MSConstants.PEEP, Music.class);
                assetManager.load(MSConstants.LASER, Music.class);
                assetManager.load(MSConstants.BUTTON_SOUND, Sound.class);
                assetManager.load(MSConstants.PAUSE_SOUND, Sound.class);
                assetManager.load(MSConstants.ANOMALY_CORROSIVE_DESTROY, Sound.class);
                assetManager.load(MSConstants.ANOMALY_ELECTRIC_DESTROY, Sound.class);
                assetManager.load(MSConstants.GET_ASTRONAUT, Sound.class);
                assetManager.load(MSConstants.KILL_ASTRONAUT, Sound.class);
                assetManager.load(MSConstants.KILL_CRATE, Sound.class);
                assetManager.load(MSConstants.LOST_RADAR, Sound.class);
                assetManager.load(MSConstants.TURBO, Sound.class);
                assetManager.load(MSConstants.UP, Sound.class);
                assetManager.load(MSConstants.GET_FUEL, Sound.class);
                assetManager.load(MSConstants.GET_TRASH, Sound.class);
                assetManager.load(MSConstants.GET_RADAR, Sound.class);
                assetManager.load(MSConstants.GET_FRAGMENT, Sound.class);
                assetManager.load(MSConstants.BLACKHOLE_TELEPORT, Sound.class);
                assetManager.load(MSConstants.PLAYER_KICKED_CORROSIVE, Sound.class);
                assetManager.load(MSConstants.PLAYER_KICKED_ELECTRIC, Sound.class);
                assetManager.load(MSConstants.ASTEROID_DESTROY, Sound.class);
                assetManager.load(MSConstants.ASTRONAUT_ALIVE, Sound.class);
                assetManager.load(MSConstants.PLAYER_LOSE_FUEL, Sound.class);
                assetManager.load(MSConstants.PLAYER_LOSE_SCORE, Sound.class);
                assetManager.load(MSConstants.PLAYER_LOW_LIFE, Sound.class);
                assetManager.load(MSConstants.PLAYER_DESTROYED, Sound.class);
                assetManager.load(MSConstants.PLAYER_REPAIR, Sound.class);
                assetManager.load(MSConstants.PLAYER_OUT_OF_SCREEN, Sound.class);
                assetManager.load(MSConstants.PLAYER_START_ENGINE, Sound.class);
                assetManager.load(MSConstants.ANOMALY_BOOM, Sound.class);
                assetManager.load(MSConstants.ANOMALY_FLUSH, Sound.class);
                assetManager.load(MSConstants.PLAYER_START_ENGINE, Sound.class);
                assetManager.load(MSConstants.FLAME, Sound.class);
                assetManager.load(MSConstants.ASTEROID_SPLASH, Sound.class);
                assetManager.load(MSConstants.FUEL_DESTROY, Sound.class);
                assetManager.load(MSConstants.STORAGE_DESTROY, Sound.class);
                assetManager.load(MSConstants.ANOMALY_CORROSIVE_BURST, Sound.class);
                assetManager.load(MSConstants.GET_STORAGE, Sound.class);
                assetManager.load(MSConstants.CHECKBOX, Sound.class);
                assetManager.load(PaintConstants.SKIN_FILE, Skin.class);
                createSandartFont(32, "");
                createSandartFont(32, "Coloured");
                createSandartFont(48, "");
                createSandartFont(64, "");
                createSandartFont(64, "Coloured");
                createSandartFont(96, "");
                createSandartFont(128, "");
                break;
            case TEST:
                assetManager.load(PaintConstants.GAME_PACK, TextureAtlas.class);
                assetManager.load(PaintConstants.SKIN_FILE, Skin.class);
                createSandartFont(32, "");
                createSandartFont(64, "");
                createSandartFont(96, "");
                break;
        }
    }

    private void createSandartFont(int size, String postfix) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameter.fontFileName = "fonts/gomarice.ttf";
        fontParameter.fontParameters.size = size;
        fontParameter.fontParameters.color = Color.WHITE;
        fontParameter.fontParameters.borderWidth = 1;
        fontParameter.fontParameters.borderColor = Color.BLACK;
        fontParameter.fontParameters.shadowOffsetX = 3;
        fontParameter.fontParameters.shadowOffsetY = 3;
        fontParameter.fontParameters.shadowColor = Color.BLACK;
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
