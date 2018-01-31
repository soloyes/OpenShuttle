package ru.geekbrains.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.Base2DScreen;

/**
 * Created by sol on 1/29/18.
 */

public class MenuScreen extends Base2DScreen{

    private SpriteBatch spriteBatch;
    private Texture background;
    private Texture player;
    private FPSLogger logger;
    private float playerX = 0f;
    private float playerY = 0f;
    private Vector2 target;
    private Vector2 destanation;
    private Vector2 playerPosition;
    private float velocity;
    private Vector2 norDestanation;
    private int width;
    private int height;

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        spriteBatch = new SpriteBatch();
        background = new Texture("Grid1.png");
        player = new Texture("Player_Attack.png");
        logger = new FPSLogger();
        target = new Vector2();
        destanation = new Vector2();
        norDestanation = new Vector2();
        playerPosition = new Vector2(playerX, playerY);
        velocity = 20f;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        target.x = screenX;
        target.y = Gdx.graphics.getHeight() - screenY;
        return false;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        destanation = target.cpy().sub(playerPosition);
        norDestanation = destanation.cpy().nor();

        for (int i = 0; i < Gdx.graphics.getHeight() / background.getHeight() + 2; i++) {
            for (int j = 0; j < Gdx.graphics.getWidth() / background.getWidth() + 2; j++) {
                spriteBatch.begin();
                spriteBatch.draw(background, background.getWidth()*i, background.getHeight()*j);
                spriteBatch.end();
            }
        }

        if (destanation.len2() > destanation.cpy().nor().scl(velocity).len2()) {
            playerPosition.add(norDestanation.cpy().scl(velocity));
        }

        spriteBatch.begin();
        spriteBatch.draw(player, playerPosition.x, playerPosition.y);
        spriteBatch.end();
        //////
        logger.log();
        //////
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
        spriteBatch.dispose();
    }
}