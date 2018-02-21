package ru.geekbrains.game.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.geekbrains.game.ui.buttons.ExitButton;
import ru.geekbrains.game.ui.buttons.PlayButton;
import ru.geekbrains.stargame.engine.ActionListener;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.Rect;
import ru.geekbrains.stargame.engine.ui.ScaledTouchUpButton;

/**
 * Created by sol on 2/21/18.
 * Class for store and manipulate with buttons. This menu installing to GameScreen and MenuScreen
 */

public class MenuBar extends Sprite {
    private List<ScaledTouchUpButton> buttons;
    private ExitButton buttonExit;
    private PlayButton buttonPlay;

    private TextureAtlas mainAtlas;
    private Map<String, Object> gameMusic;

    private Base2DScreen screen;

    public void setScreen(Base2DScreen screen) {
        this.screen = screen;
    }

    public  MenuBar(TextureAtlas atlas, Map<String, Object> music){
        buttons = new ArrayList<ScaledTouchUpButton>();
        this.mainAtlas = atlas;
        this.gameMusic = music;
    }

    public void show(ActionListener actionListener){
        buttonExit = new ExitButton(mainAtlas, actionListener);
        buttonPlay = new PlayButton(mainAtlas, actionListener);
        buttons.add(buttonExit);
        buttons.add(buttonPlay);
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).draw(batch);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).resize(worldBounds);
        }
    }

    @Override
    public void dispose() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).dispose();
        }
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).touchUp(touch, pointer);
        }
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).touchDown(touch, pointer);
        }
    }

    public void MenuActionPerformed(Object src) {
        if (src == buttonExit) {
            Gdx.app.exit();
        } else if (src == buttonPlay){
            screen.getGame().setScreen(
                    new GameScreen(screen.getGame(), mainAtlas, gameMusic));
            screen.getBackgroundMusic().stop();
        } else {
            throw new RuntimeException("Unknown src " + src);
        }
    }

    public void GameActionPerformed(Object src) {
        if (src == buttonExit) {
            Gdx.app.exit();
        } else if (src == buttonPlay) {
            ((GameScreen) screen).playAgain();
        } else {
            throw new RuntimeException("Unknown src " + src);
        }
    }
}
