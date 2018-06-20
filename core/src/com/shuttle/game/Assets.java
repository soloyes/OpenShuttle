package com.shuttle.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
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
                assetManager.load("pics/game.pack", TextureAtlas.class);
                createStdFont(32);
                createStdFont(96);
                assetManager.load("music/menu.ogg", Music.class);
                break;
            case GAME:
                assetManager.load("pics/game.pack", TextureAtlas.class);
                assetManager.load("music/game.ogg", Music.class);
                createStdFont(64);
                break;
            case CONFIG:
                assetManager.load("pics/game.pack", TextureAtlas.class);
                createStdFont(64);
        }
    }

    public void createStdFont(int size) {
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
        assetManager.load("fonts/gomarice" + size + ".ttf", BitmapFont.class, fontParameter);
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
