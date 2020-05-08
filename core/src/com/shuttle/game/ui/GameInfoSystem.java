package com.shuttle.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.FlyingText;

/**
 * @author Shuttle on 03/07/18.
 */

public class GameInfoSystem {
    private static FlyingText[] elements;

    public GameInfoSystem() {
        elements = new FlyingText[20];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = new FlyingText();
        }
    }

    @SuppressWarnings("LibGDXFlushInsideLoop")
    public void render(SpriteBatch batch) {
        for (int i = 0; i < elements.length; i++) {
            elements[i].render(batch);
        }
    }

    public static void setup(float x, float y, Preset preset, float scale, boolean colored) {
        setupElement(x, y, preset.getValue(), scale, 2.0f, colored, false);
    }

    private static void setupElement(float x, float y, String text, float scale, float dstScale, boolean colored, boolean giant) {
        for (int i = 0; i < elements.length; i++) {
            float dy;
            if (MathUtils.random(0, 1) == 0) {
                dy = MathUtils.random(-50, -20);
            } else {
                dy = MathUtils.random(20, 50);
            }
            if (!elements[i].isActive()) {
                elements[i].setup(x, y + dy, text, scale, dstScale, colored, giant);
                break;
            }
        }
    }

    public static void setup(float x, float y, String text, float scale, boolean colored) {
        setupElement(x, y, text, scale, 2.0f, colored, false);
    }

    public static void setup(float x, float y, String text, float scale, float dstScale, boolean colored, boolean giant) {
        setupElement(x, y, text, scale, dstScale, colored, giant);
    }

    public void update(float dt) {
        for (int i = 0; i < elements.length; i++) {
            elements[i].update(dt);
        }
    }

    public enum Preset {
        BRAKED("BRAKED"),
        SCREECH("[RED]SCREECH[]"),
        BOOM("BOOOOM"),
        SAD("[RED]SO SAD[]"),
        OUT("[YELLOW]OUT OF RANGE[]"),
        REPAIR("REPAIR [GREEN]+" + Rules.CONSUMABLE_REPAIR_ADD),
        FUEL("FUEL"),
        TURBO("TURBO [GREEN]x" + (int) Rules.PLAYER_ENGINE_POWER_FACTOR + "[]"),
        TRASH("TRASH"),
        SPLASH("[GREEN]SPLASH[]"),
        LOST_SCORE("SCORE"),
        MURDERER("MURDERER" + "\n[RED]-" + Rules.CONSUMABLE_MURDER_SCORE_DECREASE + "[]"),
        EASY("BE EASY"),
        ROCKETS("[GREEN]ROCKETS[]"),
        RADAR("RADAR"),
        SHIELD("SHIELD [GREEN]+" + (int) Rules.CONSUMABLE_SHIELD_TIME + "[]"),
        SCAN("SCAN [GREEN]+" + (int) Rules.CONSUMABLE_SCAN_TIME + "[]"),
        LASER("LASER"),
        BONUS_CRATE("[GREEN]BONUS CRATE[]"),
        BONUS_STORAGE("[GREEN]BONUS STORAGE[]"),
        BIG_BONUS("[GREEN]BIG BONUS[]"),
        SCORE("SCORE"),
        ANOMALY_FLUSH("[GREEN]ANOMALY FLUSH[]"),
        CRATE_FLUSH("[RED]CRATE FLUSH[]"),
        STORAGE_FLUSH("[RED]STORAGE FLUSH[]"),
        UP("1UP"),
        HP("HP"),
        ANOMALY_ARISE("[RED]ANOMALY[]");
        private String value;

        Preset(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}