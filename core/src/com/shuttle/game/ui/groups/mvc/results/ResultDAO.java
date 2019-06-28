package com.shuttle.game.ui.groups.mvc.results;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ResultDAO {
    @Getter
    @Setter
    private String username;
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
        this.username = "";
        this.date = "";
        this.level = "";
        this.score = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultDAO resultDAO = (ResultDAO) o;

        if (!username.equals(resultDAO.username)) return false;
        if (!date.equals(resultDAO.date)) return false;
        return level.equals(resultDAO.level);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + level.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ResultDAO{" +
                "username='" + username + '\'' +
                ", date='" + date + '\'' +
                ", score='" + score + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
