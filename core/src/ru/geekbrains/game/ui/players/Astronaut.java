package ru.geekbrains.game.ui.players;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.Map;

import ru.geekbrains.stargame.engine.Rect;

/**
 * Created by sol on 2/6/18.
 */

public class Astronaut extends InsightRect {

    private final float VELOCITY = 0.1f;

    public Astronaut(TextureAtlas atlas, Player player, Map<String, Object> music) {
        super(atlas.findRegion("astronaut"));
        this.player = player;
        this.music = music;
        itemSound = (Sound) music.get("rogerroll");
        setLoud(true);
        setHeightProportion(0.1f);

        initInsightRect(this.getWidth(), this.getHeight(), 2f);
    }

    private void checkAndHandleBounds() {
        if (parkingRect.isOutside(worldBounds)) {
            itemSound.stop();
            newItem(this);
        }
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(norDirection, delta * VELOCITY);
        setPosInsightRect(this);
        this.angle += (0.5f * rotation) % 360;
        checkAndHandleBounds();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        newItem(this);
    }
}
