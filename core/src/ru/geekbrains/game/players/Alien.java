package ru.geekbrains.game.players;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

/**
 * Created by sol on 2/15/18.
 */

public class Alien extends InsightRect {
    private final float VELOCITY = 0.2f;

    private boolean eatable;

    public void setEatable(boolean eatable) {
        this.eatable = eatable;
    }

    public boolean isEatable() {
        return eatable;
    }

    public Alien(TextureAtlas atlas, Player player, Map<String, Object> music) {
        this(atlas, 1,3,3);
        this.player = player;
        this.music = music;
        setEatable(true);
        itemSound = (Sound) music.get("alien");
    }

    private Alien(TextureAtlas atlas, int i, int j, int frames) {
        super(atlas.findRegion("aliens"), i, j, frames);
        setHeightProportion(0.1f);

        initInsightRect(this.getWidth(), this.getHeight(), 2f);
    }

    private void checkAndHandleBounds() {
        if (parkingRect.isOutside(worldBounds)) {
            setEatable(true);
            newItem(this);
            itemSound.stop();
            frame = Rnd.nextInt(3);
        }

        if (isEatable()) {
            if (this.isMe(player.pos)) {
                setEatable(false);
                player.pos.set(0.0f, 0.5f);
                player.setAngle(180);
                player.setTarget(new Vector2(0.0f, 0.0f));
                System.out.println("bbb");
            }

            //
            tmp1.set(pos);
            if (tmp1.sub(player.pos).len() < 0.2f) {
                tmp1.set(player.pos);
                tmp1.sub(pos);
                norDirection.set(tmp1.nor());
            }
            //
        }
    }

    @Override
    public void update(float delta) {
        this.pos.mulAdd(norDirection, delta * VELOCITY);
        this.angle += (0.5f * rotation) % 360;
        setPosInsightRect(this);
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