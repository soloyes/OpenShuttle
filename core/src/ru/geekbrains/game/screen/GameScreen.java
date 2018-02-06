package ru.geekbrains.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.game.Background;
import ru.geekbrains.game.players.Player;
import ru.geekbrains.game.star.StarsHandler;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.math.Rect;

public class GameScreen extends Base2DScreen {

    private Texture backgroundTexture;
    private Background background;
    private Texture playerTexture;

    Player player;
    Player[] enemies;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        backgroundTexture = new Texture("Grid.png");
        playerTexture = new Texture("Player.png");
        background = new Background(new TextureRegion(backgroundTexture));
        player = new Player(new TextureRegion(playerTexture));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        update(delta);
        draw();

        batch.end();
    }

    public void update(float delta) {
        StarsHandler.update(delta);
        player.update(delta);
    }

    public void draw() {
        background.draw(batch);
        StarsHandler.draw(batch);
        player.draw(batch);
    }

    @Override
    protected void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        StarsHandler.resize(worldBounds);
        player.resize(worldBounds);
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        super.touchDown(touch, pointer);
        player.setTarget(touch);
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        playerTexture.dispose();
    }
}