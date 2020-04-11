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
    private String effectiveness;
    @Getter
    @Setter
    private String score;
    @Getter
    @Setter
    private String time;
    @Getter
    @Setter
    private String dmgDone;
    @Getter
    @Setter
    private String dmgReceived;
    @Getter
    @Setter
    private String enemiesDestroyed;
    @Getter
    @Setter
    private String minedMeteorite;
    @Getter
    @Setter
    private String pickUpItems;
    @Getter
    @Setter
    private String usedItems;
    @Getter
    @Setter
    private String astronautsTaken;

    public DetailedResultDAO() {
        this.effectiveness = "";
        this.score = "";
        this.time = "";
        this.dmgDone = "";
        this.dmgReceived = "";
        this.enemiesDestroyed = "";
        this.minedMeteorite = "";
        this.pickUpItems = "";
        this.usedItems = "";
        this.astronautsTaken = "";
    }

    public DetailedResultDAO(DetailedResultDAO detailedResultDAO) {
        this(
                detailedResultDAO.getEffectiveness(),
                detailedResultDAO.getScore(),
                detailedResultDAO.getTime(),
                detailedResultDAO.getDmgDone(),
                detailedResultDAO.getDmgReceived(),
                detailedResultDAO.getEnemiesDestroyed(),
                detailedResultDAO.getMinedMeteorite(),
                detailedResultDAO.getPickUpItems(),
                detailedResultDAO.getUsedItems(),
                detailedResultDAO.getAstronautsTaken()
        );
    }
}