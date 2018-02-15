package ru.geekbrains.game.players;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.Map;

import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

/**
 * Created by sol on 2/15/18.
 */

public class Alien extends InsightRect {
    private final float VELOCITY = 0.4f;

    public Alien(TextureAtlas atlas, Player player, Map<String, Object> music) {
        this(atlas, 1,3,3);
        this.player = player;
        this.music = music;
        itemSound = (Sound) music.get("alien");
    }

    private Alien(TextureAtlas atlas, int i, int j, int frames) {
        super(atlas.findRegion("aliens"), i, j, frames);
        setHeightProportion(0.1f);
        initParkingRect(this.getWidth(), this.getHeight());

    }

    private void checkAndHandleBounds() {
        if (parkingRect.isOutside(worldBounds)) {
            itemSound.stop();
            newItem(this);
            frame = Rnd.nextInt(3);
        }

        if (this.isMe(player.pos)) {
            player.pos.set(0.0f, 1.0f);
        }
    }

    @Override
    public void update(float delta) {
        this.pos.mulAdd(norDirection, delta * VELOCITY);
        this.angle += (0.5f * rotation) % 360;
        setPosParkingRect(this);
        checkAndHandleBounds();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        newItem(this);
        frame ++;
        frame %= 2;
    }
}
