package ru.geekbrains.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;
import java.util.Map;

import ru.geekbrains.game.screen.MenuScreen;

/**
 * Created by sol on 1/29/18.
 */

public class Star2DGame extends Game {

    private TextureAtlas atlas;
    private Map<String, Music> gameMusic = new HashMap<String, Music>();

    @Override
    public void create() {
        this.atlas = new TextureAtlas("textures/mainAtlas.atlas");
        gameMusic.put("menuScreen", Gdx.audio.newMusic(Gdx.files.internal("music/Menu.ogg")));
        gameMusic.put("gameScreen", Gdx.audio.newMusic(Gdx.files.internal("music/Game.ogg")));
        gameMusic.put("problem", Gdx.audio.newMusic(Gdx.files.internal("music/problem.ogg")));
        setScreen(new MenuScreen(this, atlas, gameMusic));
    }

    @Override
    public void dispose() {
        super.dispose();
        atlas.dispose();
        for (Map.Entry<String, Music> gm : gameMusic.entrySet()) {
            gm.getValue().dispose();
        }
    }
}
