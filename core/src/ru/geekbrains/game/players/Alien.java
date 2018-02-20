package ru.geekbrains.game.players;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.Map;

import ru.geekbrains.game.Logic.Lives;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

/**
 * Created by sol on 2/15/18.
 */

public class Alien extends InsightRect {

    private final float VELOCITY = 0.2f;
    private Astronaut astronaut;
    private boolean hungry;
    private int power;
    private Lives lives;

    public void setHungry(boolean hungry) {
        this.hungry = hungry;
    }

    public boolean isHungry() {
        return hungry;
    }

    public void setAstronaut(Astronaut astronaut) {
        this.astronaut = astronaut;
    }

    public int getPower() {
        return power;
    }

    public Alien(TextureAtlas atlas, Player player, Map<String, Object> music) {
        this(atlas, 1,3,3);
        this.player = player;
        this.music = music;
        this.power = 2;
        setHungry(true);
        itemSound = (Sound) music.get("alien");
    }

    private Alien(TextureAtlas atlas, int i, int j, int frames) {
        super(atlas.findRegion("aliens"), i, j, frames);
        setHeightProportion(0.1f);

        initInsightRect(this.getWidth(), this.getHeight(), 2f);
    }

    private void checkAndHandleBounds() {
        if (parkingRect.isOutside(worldBounds)) {
            setHungry(true);
            itemSound.stop();
            frame = Rnd.nextInt(3);
            newItem(this);
        }
    }

    @Override
    public void update(float delta) {
        //
        if (isHungry()) {
            tmp1.set(pos);
            if (tmp1.sub(player.pos).len() < 0.2f) {
                tmp1.set(player.pos);
                tmp1.sub(pos);
                norDirection.set(tmp1.nor());
            }
            tmp2.set(pos);
            if (tmp2.sub(astronaut.pos).len() < 0.1f) {
                tmp2.set(astronaut.pos);
                tmp2.sub(pos);
                norDirection.set(tmp2.nor());
            }
        }
        //
        pos.mulAdd(norDirection, delta * VELOCITY);
        angle += (0.5f * rotation) % 360;
        setPosInsightRect(this);
        checkAndHandleBounds();
        checkCollisions();
    }

    private void checkCollisions(){
        if (isHungry()) {
            if (this.isMe(astronaut.pos)) {
                newItem(astronaut);
                score.decreaseAndGet(getPower());
            }
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        newItem(this);
        frame ++;
        frame %= 2;
    }

    public void setLives(Lives lives) {
        this.lives = lives;
    }
}