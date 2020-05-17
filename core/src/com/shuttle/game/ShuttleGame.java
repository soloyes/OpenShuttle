package com.shuttle.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.screens.ScreenManager;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;

import lombok.Getter;

/**
 * @author Shuttle on 03/06/18.
 */

public class ShuttleGame extends Game {
    private SpriteBatch batch;
    private ScreenManager.ScreenType screenType;
    @Getter
    private static Share share;
    private PrintStream printStream;
    private FileHandle fileHandle;
    private boolean mobile;
    private int width;
    private int height;

    public ShuttleGame(Share share, boolean mobile, int width, int height) {
        ShuttleGame.share = share;
        this.mobile = mobile;
        this.width = width;
        this.height = height;
    }

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.fileHandle = Gdx.files.local("exception.txt");
        try {
            this.printStream = new PrintStream(fileHandle.file());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ScreenManager.getInstance().init(this, batch, mobile, width, height);
        if (screenType == null) {
            screenType = ScreenManager.ScreenType.MENU;
        }
        ScreenManager.getInstance().changeScreen(screenType);
    }

    @Override
    public void dispose() {
        batch.dispose();
        getScreen().dispose();
    }

    @Override
    public void render() {
        try {
            float dt = Gdx.graphics.getDeltaTime();
            getScreen().render(dt);
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace(printStream);
            printStream.append("Above trace date: ").append(new Date().toString());
            printStream.close();
            System.exit(0);
        }
    }

    public void setRunScreen(ScreenManager.ScreenType screenType) {
        this.screenType = screenType;
    }
}