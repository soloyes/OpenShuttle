package ru.geekbrains.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

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
        velocity = 10f;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        target.x = screenX;
        target.y = Gdx.graphics.getHeight() - screenY;
        destanation = target.cpy().sub(playerPosition);
        norDestanation = destanation.cpy().nor();

        System.out.println(target.x + " " + target.y);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (int i = 0; i < Gdx.graphics.getHeight() / background.getHeight() + 2; i++) {
            for (int j = 0; j < Gdx.graphics.getWidth() / background.getWidth() + 2; j++) {
                spriteBatch.begin();
                spriteBatch.draw(background, background.getWidth()*i, background.getHeight()*j);
                spriteBatch.end();
            }
        }

        if ( Math.abs(playerPosition.len() - target.len()) > norDestanation.cpy().scl(velocity).len() ) {
            playerPosition.add(norDestanation.cpy().scl(velocity));
        }

        spriteBatch.begin();
        spriteBatch.draw(player,
                playerPosition.x - player.getWidth() / 2,
                playerPosition.y - player.getHeight() / 2 );
        spriteBatch.end();
        //////
        //logger.log();
        //////
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
        spriteBatch.dispose();
    }
}
