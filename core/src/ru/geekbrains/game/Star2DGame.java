package ru.geekbrains.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;
import java.util.Map;

import ru.geekbrains.game.ui.screen.MenuScreen;

/**
 * Created by sol on 1/29/18.
 */

public class Star2DGame extends Game {

    private TextureAtlas atlas;
    private Map<String, Object> music = new HashMap<String, Object>();

    @Override
    public void create() {
        this.atlas = new TextureAtlas("textures/mainAtlas.atlas");
        music.put("menuScreen", Gdx.audio.newMusic(Gdx.files.internal("music/menu.ogg")));
        music.put("gameScreen", Gdx.audio.newMusic(Gdx.files.internal("music/game.ogg")));
        music.put("problem", Gdx.audio.newMusic(Gdx.files.internal("music/problem.ogg")));
        music.put("rogerroll", Gdx.audio.newSound(Gdx.files.internal("music/rogerroll.ogg")));
        music.put("alien", Gdx.audio.newSound(Gdx.files.internal("music/alien.ogg")));
        setScreen(new MenuScreen(this, atlas, music));
    }

    @Override
    public void dispose() {
        super.dispose();
        atlas.dispose();
        for (Map.Entry<String, Object> gm : music.entrySet()) {
            if (gm instanceof Music){
                ((Music) gm).dispose();
            }
            if (gm instanceof Sound)
                ((Sound) gm).dispose();
        }
    }
}
