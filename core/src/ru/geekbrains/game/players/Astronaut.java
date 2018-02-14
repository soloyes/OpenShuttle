package ru.geekbrains.game.players;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

/**
 * Created by sol on 2/6/18.
 */

public class Astronaut extends Sprite{

    private Rect worldBounds;

    private float velocity = 0.05f;
    private Vector2 norDirection = new Vector2();
    private Vector2 tmp;
    private Vector2 tmp1;
    private int rotation = 1;
    private Player player;
    private Sound entrySound;

    //Player sprite is rotating. So we must to be able to get the fact when astronaut is outside.
    //We incapsulate astronaut sprite insight sqare with side = actronaut sprite diagonale.
    //in generate() method we check the outerRect is out of worldBounds or not
    private Rect outerRect;
    //

    private Map<String, Object> music;

    public Astronaut(TextureAtlas atlas, Player player, Map<String, Object> music) {
        super(atlas.findRegion("astronaut"));
        tmp = new Vector2();
        tmp1 = new Vector2();
        this.player = player;
        this.music = music;
        entrySound = (Sound) music.get("rogerroll");

        //Square with size as diagonale of player Rect.
        outerRect = new Rect();
        outerRect.setWidth((float) Math.sqrt(Math.pow(this.getWidth(),2) + Math.pow(this.getHeight(), 2)));
        outerRect.setHeight(outerRect.getWidth());
        outerRect.pos.set(this.pos);
        //

        setHeightProportion(0.1f);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(norDirection, delta * velocity);
        //outer sqare follow to astronaut
        outerRect.pos.set(this.pos);
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
                        worldBounds.getBottom() - outerRect.getHeight());
                break;
            case WEST:
                y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
                x = Rnd.nextFloat(
                        worldBounds.getLeft() - outerRect.getWidth(),
                        worldBounds.getLeft());
                break;
            case EAST:
                y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
                x = Rnd.nextFloat(
                        worldBounds.getRight(),
                        worldBounds.getRight() + outerRect.getWidth());
        }
        pos.set(x, y);
        //outer sqare follow to astronaut
        outerRect.pos.set(this.pos);

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
        if (this.isOutside(worldBounds)) {
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
        this.worldBounds = worldBounds;
        generate(Side.EAST);
    }

    private enum Side{
        EAST, WEST, SOUTH, NORTH;
        public static Side randomSide(){
            return Side.values()[Rnd.nextInt(Side.values().length)];
        }
    }
}
