package com.shuttle.game.effects;

import java.util.LinkedList;

/**
 * @author Shuttle on 11/7/18.
 */

public class PrettySound {
    private static LinkedList<PrettySound> list = new LinkedList<PrettySound>();
    private BoomBox boomBox;
    private boolean flag;
    private float dt;
    private float delay;

    private PrettySound(float delay) {
        this.boomBox = new BoomBox();
        this.delay = delay;
    }

    public static PrettySound getPretty(float delay) {
        PrettySound p = new PrettySound(delay);
        list.add(p);
        return p;
    }

    public void playSound(String sound) {
        if (flag) {
            boomBox.playSound(sound);
            flag = false;
        }
    }

    public static void update(float dt) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).dt += dt;
            if (list.get(i).dt >= list.get(i).delay) {
                list.get(i).dt = 0.0f;
                list.get(i).flag = true;
            }
        }
    }
}