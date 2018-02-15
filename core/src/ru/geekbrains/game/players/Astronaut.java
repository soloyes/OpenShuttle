package ru.geekbrains.game.players;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

/**
 * Created by sol on 2/6/18.
 */

public class Astronaut extends InsightRect {

    private float velocity = 0.05f;
    private Vector2 norDirection = new Vector2();
    private Vector2 tmp;
    private Vector2 tmp1;
    private int rotation = 1;
    private Player player;
    private Sound entrySound;

    private Map<String, Object> music;

    public Astronaut(TextureAtlas atlas, Player player, Map<String, Object> music) {
        super(atlas.findRegion("astronaut"));
        tmp = new Vector2();
        tmp1 = new Vector2();
        this.player = player;
        this.music = music;
        entrySound = (Sound) music.get("rogerroll");
        setHeightProportion(0.1f);

        initParkingRect(this);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(norDirection, delta * velocity);
        setPosParkingRect(this);
        this.angle += (0.5f * rotation) % 360;
        checkAndHandleBounds();
    }

    private void generate(Side side){
        float x = 0;
        float y = 0;
        switch (side){
            case NORTH:
                x = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
                y = Rnd.nextFloat(worldBounds.getTop(), worldBounds.getTop());
                break;
            case SOUTH:
                x = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
                y = Rnd.nextFloat(
                        worldBounds.getBottom(),
                        worldBounds.getBottom() - parkingRect.getHeight());
                break;
            case WEST:
                y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
                x = Rnd.nextFloat(
                        worldBounds.getLeft() - parkingRect.getWidth(),
                        worldBounds.getLeft());
                break;
            case EAST:
                y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
                x = Rnd.nextFloat(
                        worldBounds.getRight(),
                        worldBounds.getRight() + parkingRect.getWidth());
        }
        pos.set(x, y);
        setPosParkingRect(this);

        tmp1.set(Rnd.nextFloat(-worldBounds.getWidth() / 4,worldBounds.getWidth() / 4),
                Rnd.nextFloat(-worldBounds.getHeight() / 4,worldBounds.getHeight() / 4));
        tmp.set(pos);
        tmp1.sub(tmp);
        norDirection.set(tmp1.nor());

        this.angle += 360 / (Rnd.nextInt(6) + 1);
        this.rotation = Rnd.nextInt(3) - 1;

        entrySound.play();
    }

     private void checkAndHandleBounds() {
        if (parkingRect.isOutside(worldBounds)) {
            entrySound.stop();
            generate(Side.randomSide());
        }

        if (this.isMe(player.pos)) {
            entrySound.stop();
            generate(Side.randomSide());
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        generate(Side.EAST);
    }

    private enum Side{
        EAST, WEST, SOUTH, NORTH;
        public static Side randomSide(){
            return Side.values()[Rnd.nextInt(Side.values().length)];
        }
    }
}
