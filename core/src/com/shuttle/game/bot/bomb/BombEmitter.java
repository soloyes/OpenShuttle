package com.shuttle.game.bot.bomb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.bot.pool.ObjectPool;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 19/01/19.
 */
public class BombEmitter extends ObjectPool<Bomb> {
    //private GameScreen gs;
    private BombParticleEffectPool bombParticleEffectPool;
    private boolean f = true;

    @Override
    protected Bomb newObject() {
        return new Bomb(bombParticleEffectPool);
    }

    public BombEmitter() {
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
        if (f) {
            createBomb();
            f = false;
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void createBomb() {
        for (int i = 0; i < 100; i++) {
            getActiveElement().init(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT));
        }
    }

    public void dispose(){
        bombParticleEffectPool.dispose();
    }
}
