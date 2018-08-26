package com.shuttle.game.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.shuttle.game.conf.MSConstants;
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
                assetManager.load("pics/game.pack", TextureAtlas.class);
                assetManager.load("uiskin/gameSkin.json", Skin.class);

                assetManager.load(MSConstants.BUTTON_SOUND, Sound.class);
                createStdFont(32, "");
                createStdFont(48, "");
                createStdFont(64, "");
                createStdFont(96, "");
                break;
            case GAME:
                assetManager.load("pics/game.pack", TextureAtlas.class);
                assetManager.load(MSConstants.MENU_MUSIC, Music.class);
                assetManager.load(MSConstants.GAME_MUSIC, Music.class);
                assetManager.load(MSConstants.BUTTON_SOUND, Sound.class);
                assetManager.load(MSConstants.PAUSE_SOUND, Sound.class);
                assetManager.load(MSConstants.E_ALIEN_DESTROY, Sound.class);
                assetManager.load(MSConstants.F_ALIEN_DESTROY, Sound.class);
                assetManager.load(MSConstants.GET_ASTRONAUT, Sound.class);
                assetManager.load(MSConstants.KILL_ASTRONAUT, Sound.class);
                assetManager.load(MSConstants.KILL_CRATE, Sound.class);
                assetManager.load(MSConstants.LOST_RADAR, Sound.class);
                assetManager.load(MSConstants.TURBO, Sound.class);
                assetManager.load(MSConstants.UP, Sound.class);
                assetManager.load(MSConstants.GET_FUEL, Sound.class);
                assetManager.load(MSConstants.GET_TRASH, Sound.class);
                assetManager.load(MSConstants.GET_RADAR, Sound.class);
                assetManager.load("uiskin/gameSkin.json", Skin.class);
                assetManager.load("pics/asteroid.png", Texture.class);
                createStdFont(32, "");
                createStdFont(48, "");
                createStdFont(64, "");
                createStdFont(64, "Coloured");
                createStdFont(96, "");
                createStdFont(128, "");
                break;
            case TEST:
                assetManager.load("pics/game.pack", TextureAtlas.class);
                assetManager.load("uiskin/gameSkin.json", Skin.class);
                createStdFont(32, "");
                createStdFont(64, "");
                createStdFont(96, "");
                break;
        }
    }

    private void createStdFont(int size, String postfix) {
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
        atlas = assetManager.get("pics/game.pack", TextureAtlas.class);
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
