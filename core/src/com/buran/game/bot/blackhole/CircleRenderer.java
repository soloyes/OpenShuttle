package com.buran.game.bot.blackhole;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.buran.game.bot.anomaly.AbstractAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.buran.game.bot.anomaly.ElectricalAnomaly;
import com.buran.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.buran.game.bot.anomaly.MagneticAnomaly;
import com.buran.game.bot.anomaly.MagneticAnomalyEmitter;
import com.buran.game.bot.anomaly.RadiationAnomaly;
import com.buran.game.bot.anomaly.RadiationAnomalyEmitter;
import com.buran.game.conf.Buffs;
import com.buran.game.player.navigation.Radar;
import com.buran.game.utils.ListOfListsJoiner;
import com.buran.game.utils.pool.ObjectPool;

/**
 * @author soloyes on 15/11/20.
 */

public class CircleRenderer {
    private ShapeRenderer shapeRenderer;
    private ListOfListsJoiner<AbstractAnomaly> anomalyJoiner;
    private float d;
    public static boolean freeze;

    public CircleRenderer() {
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.DARK_GRAY);
        anomalyJoiner = ListOfListsJoiner.newAbstractAnomalyList(
                ((CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class)).getActiveList(),
                ((RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class)).getActiveList(),
                ((MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class)).getActiveList(),
                ((ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class)).getActiveList())
        ;
    }

    public void render(Camera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        if (Radar.isActive) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(Blackhole.position.x, Blackhole.position.y, Buffs.getBlackholeTakeRange());
            shapeRenderer.end();
        }

        if (freeze) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (int i = 0; i < anomalyJoiner.size(); i++) {
                if (!anomalyJoiner.get(i).isTaken()) {
                    shapeRenderer.circle(anomalyJoiner.get(i).getPosition().x, anomalyJoiner.get(i).getPosition().y, d);
                }
            }
            shapeRenderer.end();
        }
    }

    public void update(float dt) {
        if (freeze) {
            d += dt * 60 * 5;
            if (d >= 120) {
                d = 0.0f;
                freeze = false;
            }
        }
    }
}
