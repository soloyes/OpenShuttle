package com.buran.game.campaign;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.buran.game.conf.Rules;
import com.buran.game.platform.Radio;
import com.buran.game.player.Standing;
import com.buran.game.ui.groups.PauseGroup;

/**
 * The endless run told through the radio: the same voice as the campaign speaks up at the start
 * and at a few steps on the way to the collapse - calm, then rising, then the end (tips 6, 12,
 * 29, 36). One line, a few seconds of text, then silence again.
 */
public final class ArcadeRadio {
    private static final int[] STEPS = {0, 3, 7, 11, 15, 19};
    private static final String[] LINES = {
            "NO MORE BEACONS.  JUST THE FIELD, AND YOU.  HOLD ON AS LONG AS YOU CAN.",
            "THE FIELD IS WAKING UP.  KEEP MOVING.",
            "MORE OF THEM NOW.  THE SHELTERS WILL HIDE YOU, FOR A WHILE.",
            "THE HULL IS SINGING.  SOMETHING OUT THERE IS GETTING STRONGER.",
            "THE RIFT IS GROWING.  EVERYTHING IS FALLING TOWARD IT.",
            "THIS IS THE COLLAPSE.  NOTHING HOLDS NOW.  MAKE IT COUNT.",
    };
    private static final float START_DELAY = 2.5f;
    private static final float SHOW = 7f;

    private int last = -1;
    private float time;
    private String line;
    private float lineTime;

    public void update(float dt) {
        time += dt;
        lineTime = Math.max(0f, lineTime - dt);
        if (Rules.GAME_OVER || PauseGroup.isPause() || time < START_DELAY) {
            return;
        }
        int step = Standing.getInstance().getPlayingLevel();
        for (int i = STEPS.length - 1; i >= 0; i--) {
            if (step >= STEPS[i]) {
                if (i > last) {
                    last = i;
                    line = LINES[i];
                    lineTime = SHOW;
                    com.buran.game.ui.hud.Comms.say(line, SHOW, com.buran.game.ui.hud.Comms.STORY);
                    Radio.play(String.format("arcade%02d", STEPS[i]));
                }
                break;
            }
        }
        Radio.update();
    }

    public void render(Batch batch, BitmapFont font) {
        if (Rules.GAME_OVER) {
            return;
        }
        com.buran.game.ui.hud.Comms.render(batch, font, Rules.WORLD_WIDTH / 2f, Rules.WORLD_HEIGHT - 300f);
    }
}
