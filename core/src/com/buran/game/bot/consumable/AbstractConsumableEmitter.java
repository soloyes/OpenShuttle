package com.buran.game.bot.consumable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.buran.game.conf.Rules;
import com.buran.game.effects.particle.ParticleEmitter;
import com.buran.game.ui.help.HandPointer;
import com.buran.game.utils.pool.ObjectPool;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 02/11/18.
 */

public abstract class AbstractConsumableEmitter extends ObjectPool<AbstractConsumable> {
    private float time;
    @Getter
    @Setter
    private float respawnTime;
    @Getter
    @Setter
    private int consumablesAtStart;
    @Getter
    @Setter
    private int consumablesMaxCount;
    protected ParticleEmitter particleEmitter;

    AbstractConsumableEmitter(Class<? extends AbstractConsumable> clazz, ParticleEmitter particleEmitter) {
        super(clazz);
        this.particleEmitter = particleEmitter;
    }

    protected abstract AbstractConsumable newObject();

    public void emit() {
        if (consumablesAtStart != 0) {
            for (int i = 0; i < consumablesAtStart; i++) {
                createConsumable();
            }
        }
    }

    protected void createConsumable() {
        AbstractConsumable abstractConsumable = getActiveElement();
        calculateStat(abstractConsumable);
        abstractConsumable.init(
                MathUtils.random(0, Rules.SPACE_WIDTH),
                MathUtils.random(0, Rules.SPACE_HEIGHT)
        );
    }

    protected abstract void calculateStat(AbstractConsumable consumable);

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
            if (activeList.size() < consumablesMaxCount)
                createConsumable();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}