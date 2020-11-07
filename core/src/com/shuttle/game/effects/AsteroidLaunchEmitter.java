package com.shuttle.game.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.asteroids.MetalAsteroid;
import com.shuttle.game.bot.asteroids.MetalAsteroidEmitter;
import com.shuttle.game.bot.asteroids.MineralAsteroid;
import com.shuttle.game.bot.asteroids.MineralAsteroidEmitter;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.player.Buran;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.TimeFormat;
import com.shuttle.game.utils.pool.ObjectPool;
import com.shuttle.game.utils.pool.Poolable;

/**
 * @author soloyes on 17/05/20.
 */

public class AsteroidLaunchEmitter extends ObjectPool<AsteroidLaunchEmitter.Element> {
    private BoomBox boomBox;

    private AsteroidLaunchEmitter(Class<? extends Element> clazz) {
        super(clazz);
    }

    public AsteroidLaunchEmitter() {
        this(Element.class);
        this.boomBox = new BoomBox();
    }

    public void addToList() {
        getActiveElement().init(Buran.position.x, Buran.position.y);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    @Override
    protected Element newObject() {
        return new Element();
    }

    public class Element implements Poolable {
        private float time;
        private boolean active;
        private GlyphLayout glyphLayout;
        private BitmapFont font48_C;
        private Vector2 position;
        private Vector2 destination;
        private float dx, dy;

        public Element() {
            this.font48_C = Assets.getInstance().getAssetManager().get(PaintConstants.FONT48_C);
            this.glyphLayout = new GlyphLayout(font48_C, "");
            this.position = new Vector2();
            this.destination = new Vector2();
        }

        @Override
        public void init(float x, float y) {
            this.active = true;
            this.time = 3.0f;
            this.position.set(x, y);
        }

        @Override
        public void destroy() {
            this.active = false;
            int N = MathUtils.random(5, 8);
            MineralAsteroidEmitter mineralAsteroidEmitter = (MineralAsteroidEmitter) ObjectPool.EMITTERS.get(MineralAsteroid.class);
            MetalAsteroidEmitter metalAsteroidEmitter = (MetalAsteroidEmitter) ObjectPool.EMITTERS.get(MetalAsteroid.class);
            for (int i = 0; i < N; i++) {
                //out of space
                destination.set(2 * Rules.SPACE_WIDTH, 2 * Rules.SPACE_HEIGHT);
                destination.setAngle(i * 360 / N);
                if (i % 2 == 0) {
                    mineralAsteroidEmitter.getActiveElement().init(position, destination, 1.0f, false);
                } else {
                    metalAsteroidEmitter.getActiveElement().init(position, destination, 1.0f, false);
                }
            }
            Shake.getInstance().shakeCamera(1.0f, 35, 10);
            Vibrator.vibrate(VConstants.TRASH_ASTEROIDS);
            boomBox.playSound(MSConstants.TRASH_ASTEROIDS);
        }

        @Override
        public void destroyWithEffect() {

        }

        @Override
        public boolean isActive() {
            return active;
        }

        public void update(float dt) {
            time -= dt;
            if (time <= 0.0f) {
                time = 0.0f;
                destroy();
            } else {
                glyphLayout.setText(font48_C, TimeFormat.formatMS(time));
            }
            dx = MathUtils.random(-5, 5);
            dy = MathUtils.random(-5, 5);
            checkPool();
        }

        public void render(SpriteBatch batch) {
            font48_C.setColor(Color.WHITE);
            if (time <= 2.5) {
                font48_C.setColor(Color.YELLOW);
            }
            if (time <= 1.5f) {
                font48_C.setColor(Color.RED);
            }
            font48_C.draw(batch,
                    glyphLayout,
                    position.x - glyphLayout.width / 2 + dx, position.y - glyphLayout.height / 2 + dy
            );
        }
    }
}