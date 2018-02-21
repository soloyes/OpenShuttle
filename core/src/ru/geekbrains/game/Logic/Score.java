package ru.geekbrains.game.Logic;

/**
 * Created by sol on 2/19/18.
 */

public class Score {
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int increaseAndGet(){
        return ++score;
    }

    public int decreaseAndGet(int power){
        return score -= power;
    }

    public void initScore(){
        score = 0;
    }
}
