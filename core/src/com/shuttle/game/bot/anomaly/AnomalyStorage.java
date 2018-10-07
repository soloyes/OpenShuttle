package com.shuttle.game.bot.anomaly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * @author Shuttle on 10/7/18.
 */
public class AnomalyStorage {
    private Map<AbstractAnomaly.AnomalyType, ArrayList<AbstractAnomaly>> map;

    public AnomalyStorage() {
        this.map = new HashMap<AbstractAnomaly.AnomalyType, ArrayList<AbstractAnomaly>>();
    }

    public List<AbstractAnomaly> getAnomaliesList(AbstractAnomaly.AnomalyType anomalyType) {
        List<AbstractAnomaly> list = this.map.get(anomalyType);
        if (list == null) {
            return new ArrayList<AbstractAnomaly>();
        }
        return list;
    }

    public void insert(AbstractAnomaly anomaly, AbstractAnomaly.AnomalyType anomalyType) {
        if (anomaly.getType() != anomalyType) return;

        ArrayList<AbstractAnomaly> anomalies;
        if (map.get(anomalyType) == null) {
            anomalies = new ArrayList<AbstractAnomaly>();
            anomalies.add(anomaly);
            map.put(anomalyType, anomalies);
        } else {
            anomalies = map.get(anomalyType);
            anomalies.add(anomaly);
        }

    }
}
