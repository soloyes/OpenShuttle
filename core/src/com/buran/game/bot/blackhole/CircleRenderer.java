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
import com.buran.game.conf.Rules;
import com.buran.game.player.Buran;
import com.buran.game.player.navigation.Radar;
import com.buran.game.utils.ListOfListsJoiner;
import com.buran.game.utils.pool.ObjectPool;

/**
 * @author soloyes on 15/11/20.
 */

public class CircleRenderer {
    private ShapeRenderer shapeRenderer;
    private ListOfListsJoiner<AbstractAnomaly> anomalyJoiner;
    private float d1;
    private static float d2;
    private static float d3;
    public static boolean freeze;
    public static boolean scan;
    private static boolean scanRender;

    public CircleRenderer() {
        this.shapeRenderer = new ShapeRenderer();
        freeze = false;
        scan = false;
        scanRender = false;
        shapeRenderer.setColor(Color.DARK_GRAY);
        anomalyJoiner = ListOfListsJoiner.newAbstractAnomalyList(
                ((CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class)).getActiveList(),
                ((RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class)).getActiveList(),
                ((MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class)).getActiveList(),
                ((ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class)).getActiveList())
        ;
    }

    public void dispose() {
        shapeRenderer.dispose();
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
                    shapeRenderer.circle(anomalyJoiner.get(i).getPosition().x, anomalyJoiner.get(i).getPosition().y, d1);
                }
            }
            shapeRenderer.end();
        }

        if (scanRender && !Buran.isTaken){
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(Buran.position.x, Buran.position.y, d2);
            shapeRenderer.end();
        }
    }

    public void update(float dt) {
        if (freeze) {
            d1 += dt * 60 * Rules.CIRCLE_RENDER;
            if (d1 >= 120) {
                d1 = 0.0f;
                freeze = false;
            }
        }

        if (scan){
            d2 += dt * 60 * Rules.CIRCLE_RENDER;
            if (d2 >= 360) {
                d2 = 0.0f;
                scanRender = false;
            }
            d3 += dt;
            if (d3 >= Rules.CONSUMABLE_SCAN_TIME){
                scan = false;
            }
        }
    }

    public static void activateScan(){
        scan = true;
        scanRender = true;
        d2 = 0.0f;
        d3 = 0.0f;
    }
}