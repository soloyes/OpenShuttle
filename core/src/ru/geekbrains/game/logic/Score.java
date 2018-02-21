package ru.geekbrains.game.logic;

/**
 * Created by sol on 2/19/18.
 * Class for store and manipulate with score
 */

public class Score {
    private int score;

    public int getScore() {
        return score;
    }

    public int increaseAndGet(){
        return ++score;
    }

    public int decreaseAndGet(int power){
        score -= power;
        if (score < 0 ){
            score = 0;
        }
        return  score;
    }

    public void initScore(){
        score = 0;
    }
}
