package com.shuttle.game.ui.groups.mvc.results;

import com.shuttle.game.conf.Rules;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 7/14/18.
 */

public class ResultDAO {
    @Getter
    @Setter
    private Date date;
    @Getter
    @Setter
    private String level;
    @Getter
    @Setter
    private DetailedResultDAO current;
    @Getter
    @Setter
    private DetailedResultDAO previous;
    @Getter
    @Setter
    private boolean child;

    public ResultDAO() {
        this.date = new Date();
        this.level = "";
        this.current = new DetailedResultDAO();
        this.previous = new DetailedResultDAO();
        this.child = false;
    }

    public ResultDAO(Date date, String level, DetailedResultDAO current, boolean child) {
        this.date = date;
        this.level = level;
        this.current = current;
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultDAO resultDAO = (ResultDAO) o;

        if (child != resultDAO.child) return false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Rules.DATE_FORMAT, Locale.ENGLISH);
        String day1 = simpleDateFormat.format(date);
        String day2 = simpleDateFormat.format(resultDAO.date);
        if (!day1.equals(day2)) return false;
        return level.equals(resultDAO.level);
    }

    @Override
    public int hashCode() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Rules.DATE_FORMAT, Locale.ENGLISH);
        String day = simpleDateFormat.format(date.getTime());
        int result = day.hashCode();
        result = 31 * result + level.hashCode();
        result = 31 * result + (child ? 1 : 0);
        return result;
    }
}