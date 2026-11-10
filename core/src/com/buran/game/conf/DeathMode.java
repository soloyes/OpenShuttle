package com.buran.game.conf;

import com.buran.game.bot.anomaly.AbstractAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.buran.game.bot.anomaly.ElectricalAnomaly;
import com.buran.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.buran.game.bot.anomaly.MagneticAnomaly;
import com.buran.game.bot.anomaly.MagneticAnomalyEmitter;
import com.buran.game.bot.anomaly.RadiationAnomaly;
import com.buran.game.bot.anomaly.RadiationAnomalyEmitter;
import com.buran.game.utils.pool.ObjectPool;

import java.util.List;

/**
 * @author soloyes on 22/11/20.
 */

public class DeathMode {
    private static float maxCountTime;

    /** Grows a cap by the death-mode factor, at least by one (caps of 1-2 never grew with plain truncation). */
    private static int grow(int max) {
        return Math.max(max + 1, Math.round(max * Rules.DEATH_MODE_MAX_COUNT_TIME_FACTOR));
    }

    private static void corrosiveAnomalyRespawnTime() {
        ((CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class)).setRespawnTime(Rules.DEATH_MODE_RESPAWN_TIME_FACTOR);
    }

    private static void radiationAnomalyRespawnTime() {
        ((RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class)).setRespawnTime(Rules.DEATH_MODE_RESPAWN_TIME_FACTOR);
    }

    private static void magneticAnomalyRespawnTime() {
        ((MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class)).setRespawnTime(Rules.DEATH_MODE_RESPAWN_TIME_FACTOR);
    }

    private static void electricAnomalyRespawnTime() {
        ((ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class)).setRespawnTime(Rules.DEATH_MODE_RESPAWN_TIME_FACTOR);
    }

    private static int corrosiveAnomalyMaxCount() {
        return grow(((CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class)).getAnomaliesMaxCount());
    }

    private static int radiationAnomalyMaxCount() {
        return grow(((RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class)).getAnomaliesMaxCount());
    }

    private static int magneticAnomalyMaxCount() {
        return grow(((MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class)).getAnomaliesMaxCount());
    }

    private static int electricAnomalyMaxCount() {
        return grow(((ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class)).getAnomaliesMaxCount());
    }

    public static float corrosiveAnomalyVelocity() {
        if (Rules.DEATH_MODE) {
            return Rules.ANOMALIES_CORROSIVE_VELOCITY * Rules.DEATH_MODE_VELOCITY_FACTOR;
        } else {
            return Rules.ANOMALIES_CORROSIVE_VELOCITY;
        }
    }

    public static float radiationAnomalyVelocity() {
        if (Rules.DEATH_MODE) {
            return Rules.ANOMALIES_RADIATION_VELOCITY * Rules.DEATH_MODE_VELOCITY_FACTOR;
        } else {
            return Rules.ANOMALIES_RADIATION_VELOCITY;
        }
    }

    public static float magneticAnomalyVelocity() {
        if (Rules.DEATH_MODE) {
            return Rules.ANOMALIES_MAGNETIC_VELOCITY * Rules.DEATH_MODE_VELOCITY_FACTOR;
        } else {
            return Rules.ANOMALIES_MAGNETIC_VELOCITY;
        }
    }

    public static float electricAnomalyVelocity() {
        if (Rules.DEATH_MODE) {
            return Rules.ANOMALY_ELECTRIC_VELOCITY * Rules.DEATH_MODE_VELOCITY_FACTOR;
        } else {
            return Rules.ANOMALY_ELECTRIC_VELOCITY;
        }
    }

    public static void activate() {
        Rules.DEATH_MODE = true;
        maxCountTime = 0.0f;
        abstractAnomalyDeactivate(((CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class)).getActiveList());
        abstractAnomalyDeactivate(((RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class)).getActiveList());
        abstractAnomalyDeactivate(((MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class)).getActiveList());
        abstractAnomalyDeactivate(((ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class)).getActiveList());
        corrosiveAnomalyRespawnTime();
        magneticAnomalyRespawnTime();
        radiationAnomalyRespawnTime();
        electricAnomalyRespawnTime();
    }

    public static void update(float dt) {
        maxCountTime += dt;

        if (maxCountTime >= Rules.DEATH_MODE_MAX_COUNT_TIME_PERIOD) {
            maxCountTime = 0.0f;
            ((CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class)).setAnomaliesMaxCount(corrosiveAnomalyMaxCount());
            ((RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class)).setAnomaliesMaxCount(radiationAnomalyMaxCount());
            ((MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class)).setAnomaliesMaxCount(magneticAnomalyMaxCount());
            ((ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class)).setAnomaliesMaxCount(electricAnomalyMaxCount());
        }
    }

    private static void abstractAnomalyDeactivate(List<AbstractAnomaly> anomalies) {
        for (int i = 0; i < anomalies.size(); i++) {
            if (!anomalies.get(i).isActivated()) {
                anomalies.get(i).setActivated(true);
            }
        }
    }
}