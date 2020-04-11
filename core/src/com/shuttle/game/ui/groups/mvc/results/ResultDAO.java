package com.shuttle.game.ui.groups.mvc.results;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 7/14/18.
 */

public class ResultDAO {
    @Getter
    @Setter
    private String date;
    @Getter
    @Setter
    private String level;
    @Getter
    @Setter
    private DetailedResultDAO current;
    @Getter
    @Setter
    private DetailedResultDAO previous;

    public ResultDAO() {
        this.date = "";
        this.level = "";
        this.current = new DetailedResultDAO();
        this.previous = new DetailedResultDAO();
    }

    public ResultDAO(String date, String level, DetailedResultDAO current) {
        this.date = date;
        this.level = level;
        this.current = current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultDAO resultDAO = (ResultDAO) o;

        if (!date.equals(resultDAO.date)) return false;
        return level.equals(resultDAO.level);
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + level.hashCode();
        return result;
    }
}