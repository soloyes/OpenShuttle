package ru.geekbrains.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.game.Background;
import ru.geekbrains.game.players.Player;
import ru.geekbrains.game.star.StarsHandler;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.math.Rect;

public class GameScreen extends Base2DScreen {

    private Background background;
    private StarsHandler stars;
    private TextureAtlas mainAtlas;
    private Player player;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        mainAtlas = new TextureAtlas("mainAtlas.atlas");
        background = new Background(mainAtlas);
        stars = new StarsHandler(mainAtlas);
        player = new Player(mainAtlas, stars);
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
        stars.update(delta);
        player.update(delta);
    }

    public void draw() {
        background.draw(batch);
        stars.draw(batch);
        player.draw(batch);
    }

    @Override
    protected void resize(Rect worldBounds) {
        background.resize(worldBounds);
        stars.resize(worldBounds);
        player.resize(worldBounds);
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        player.setTarget(touch);
    }

    @Override
    protected void touchDragged(Vector2 touch, int pointer) {
        player.setTarget(touch);
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
        player.dispose();
        stars.dispose();
        mainAtlas.dispose();
    }
}