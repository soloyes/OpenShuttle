package com.buran.game.bot.anomaly;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.buran.game.conf.Rules;
import com.buran.game.effects.particle.ParticleEmitter;
import com.buran.game.ui.help.HandPointer;
import com.buran.game.utils.pool.ObjectPool;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 9/15/18.
 */

public abstract class AbstractAnomalyEmitter extends ObjectPool<AbstractAnomaly> {
    private float time;
    @Getter
    @Setter
    private float respawnTime;
    @Getter
    @Setter
    private int anomaliesAtStart;
    @Getter
    @Setter
    private int anomaliesMaxCount;
    @Getter
    private static int emitted;
    protected ParticleEmitter particleEmitter;

    AbstractAnomalyEmitter(Class<? extends AbstractAnomaly> clazz, ParticleEmitter particleEmitter) {
        super(clazz);
        this.particleEmitter = particleEmitter;
        // Emitters are rebuilt for every game; the shared counter feeds "effectiveness".
        emitted = 0;
    }

    protected abstract AbstractAnomaly newObject();

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            float d = activeList.get(i).getDissolve();
            boolean dis = d > 0f && com.buran.game.effects.Dissolve.begin(batch, d);
            activeList.get(i).render(batch);
            if (dis) {
                com.buran.game.effects.Dissolve.end(batch);
            }
        }
    }

    public void update(float dt) {
        if (HandPointer.done) {
            time += dt;
        }
        if (time > respawnTime && !Rules.GAME_OVER) {
            time = 0.0f;
            if (activeList.size() < anomaliesMaxCount) {
                createAnomaly();
            }
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    protected void createAnomaly() {
        getActiveElement().init(
                MathUtils.random(0, Rules.SPACE_WIDTH),
                MathUtils.random(0, Rules.SPACE_HEIGHT)
        );
        emitted++;
    }

    protected void emit() {
        if (anomaliesAtStart != 0) {
            for (int i = 0; i <= anomaliesAtStart; i++) {
                createAnomaly();
            }
        }
    }
}