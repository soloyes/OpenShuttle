package com.buran.game.ui.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayDeque;

/**
 * The one place where the game talks to the player in flight: a single line, top centre under
 * the goal. One message at a time; a more urgent one cuts in, the rest wait their turn, and the
 * same line is never stacked (tips 16, 90: one consistent, uncluttered channel).
 */
public final class Comms {
    /** A hint about controls or a mistake. */
    public static final int TIP = 1;
    /** The sector's story and script. */
    public static final int STORY = 2;
    /** Danger or loss: cuts in at once. */
    public static final int ALERT = 3;

    private static final class Msg {
        final String text;
        final int prio;
        float left;
        final float total;

        Msg(String text, float seconds, int prio) {
            this.text = text;
            this.left = seconds;
            this.total = seconds;
            this.prio = prio;
        }
    }

    private static Msg current;
    private static final ArrayDeque<Msg> QUEUE = new ArrayDeque<>();

    private Comms() {
    }

    public static void say(String text, float seconds, int prio) {
        if (current != null && current.text.equals(text)) {
            current.left = Math.max(current.left, seconds);
            return;
        }
        for (Msg m : QUEUE) {
            if (m.text.equals(text)) {
                return;
            }
        }
        Msg m = new Msg(text, seconds, prio);
        if (current == null || prio > current.prio) {
            if (current != null && current.left > 1.5f && current.prio >= STORY) {
                QUEUE.addFirst(current);   // the interrupted story line comes back after
            }
            current = m;
            return;
        }
        if (prio == TIP && !QUEUE.isEmpty()) {
            return;   // tips do not wait behind a queue: they would come too late to matter
        }
        QUEUE.addLast(m);
        while (QUEUE.size() > 3) {
            QUEUE.removeLast();
        }
    }

    public static void clear() {
        current = null;
        QUEUE.clear();
    }

    public static void update(float dt) {
        if (current == null) {
            current = QUEUE.pollFirst();
            return;
        }
        current.left -= dt;
        if (current.left <= 0f) {
            current = QUEUE.pollFirst();
        }
    }

    /** Draws the line centred on cx at cy. */
    public static void render(Batch batch, BitmapFont font, float cx, float cy) {
        if (current == null) {
            return;
        }
        float a = Math.min(1f, current.left) * Math.min(1f, (current.total - current.left) * 3f + 0.2f);
        Prompt.text(batch, font, current.text, cx, cy, a);
    }
}
