package com.shuttle.game.ui.groups.mvc.results;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 11/04/20.
 */

@AllArgsConstructor
public class DetailedResultDAO {
    @Getter
    @Setter
    private int effectiveness;
    @Getter
    @Setter
    private long score;
    @Getter
    @Setter
    private int time;
    @Getter
    @Setter
    private int dmgDone;
    @Getter
    @Setter
    private int dmgReceived;
    @Getter
    @Setter
    private int enemiesDestroyed;
    @Getter
    @Setter
    private int minedAsteroids;
    @Getter
    @Setter
    private int pickUpItems;
    @Getter
    @Setter
    private int usedItems;
    @Getter
    @Setter
    private int astronautsTaken;

    public DetailedResultDAO() {
        this.effectiveness = 0;
        this.score = 0L;
        this.time = 0;
        this.dmgDone = 0;
        this.dmgReceived = 0;
        this.enemiesDestroyed = 0;
        this.minedAsteroids = 0;
        this.pickUpItems = 0;
        this.usedItems = 0;
        this.astronautsTaken = 0;
    }

    public DetailedResultDAO(DetailedResultDAO detailedResultDAO) {
        this(
                detailedResultDAO.getEffectiveness(),
                detailedResultDAO.getScore(),
                detailedResultDAO.getTime(),
                detailedResultDAO.getDmgDone(),
                detailedResultDAO.getDmgReceived(),
                detailedResultDAO.getEnemiesDestroyed(),
                detailedResultDAO.getMinedAsteroids(),
                detailedResultDAO.getPickUpItems(),
                detailedResultDAO.getUsedItems(),
                detailedResultDAO.getAstronautsTaken()
        );
    }
}