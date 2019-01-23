package com.shuttle.game.bot.bomb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.pool.ObjectPool;

/**
 * @author Shuttle on 19/01/19.
 */
public class BombEmitter extends ObjectPool<Bomb> {
    private BombParticleEffectPool bombParticleEffectPool;
    private BoundCheck boundCheck;
    private GameScreen gs;

    @Override
    protected Bomb newObject() {
        return new Bomb(gs, bombParticleEffectPool.obtain());
    }

    public BombEmitter(GameScreen gs) {
        this.gs = gs;
        this.boundCheck = new BoundCheck();
        this.bombParticleEffectPool = new BombParticleEffectPool.BombParticleBuilder()
                .setInitialCapacity(0)
                .setMaxCapacity(32)
                .setParticleEffect(Gdx.files.internal("particles/flameball"), Assets.getInstance().getAtlas())
                .build();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            Bomb bomb = activeList.get(i);
            bomb.update(dt);
            if (boundCheck.checkAndHandleBounds(bomb)) {
                bomb.destroy();
            }
        }
        checkPool();
    }

    public void createBomb(float x, float y) {
        getActiveElement().init(x, y);
    }

    public void createBomb(Vector2 position) {
        getActiveElement().init(position.x, position.y);
    }

    public void dispose() {
        bombParticleEffectPool.dispose();
    }
}
