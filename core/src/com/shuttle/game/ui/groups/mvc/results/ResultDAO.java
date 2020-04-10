package com.shuttle.game.ui.groups.mvc.results;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ResultDAO {
    @Getter
    @Setter
    private String date;
    @Getter
    @Setter
    private String score;
    @Getter
    @Setter
    private String level;

    public ResultDAO() {
        this.date = "";
        this.level = "";
        this.score = "";
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

    @Override
    public String toString() {
        return "ResultDAO{" +
                ", date='" + date + '\'' +
                ", score='" + score + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}