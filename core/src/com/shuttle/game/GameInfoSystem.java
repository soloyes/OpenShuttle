package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.conf.Rules;

public class GameInfoSystem {
    private FlyingText[] elements;

    public GameInfoSystem() {
        this.elements = new FlyingText[20];
        for (int i = 0; i < elements.length; i++) {
            this.elements[i] = new FlyingText();
        }
    }

    @SuppressWarnings("LibGDXFlushInsideLoop")
    public void render(SpriteBatch batch) {
        for (int i = 0; i < elements.length; i++) {
            elements[i].render(batch);
        }
    }

    public void setup(float x, float y, GameInfoSystemPreset preset, float scale, boolean colored) {
        setupElement(x, y, preset.getValue(), scale, colored);
    }

    private void setupElement(float x, float y, String text, float scale, boolean colored) {
        for (int i = 0; i < elements.length; i++) {
            if (!elements[i].isActive()) {
                elements[i].setup(x, y, text, scale, colored);
                break;
            }
        }
    }

    public void setup(float x, float y, String text, float scale, boolean colored) {
        setupElement(x, y, text, scale, colored);
    }

    public void update(float dt) {
        for (int i = 0; i < elements.length; i++) {
            elements[i].update(dt);
        }
    }

    public enum GameInfoSystemPreset {
        BRAKED("BRAKED"),
        SCREECH("[RED]SCREECH[]"),
        BOOM("BOOOOM!"),
        SAD("[YELLOW]SO SAD[]"),
        OUT("[YELLOW]OUT OF RANGE![]"),
        REPAIR("REPAIR [GREEN]+" + Rules.CRATE_REPAIR_ADD),
        FUEL("FUEL [GREEN]+" + Rules.CRATE_FUEL_ADD+"[]"),
        TURBO("TURBO [GREEN]x3[]"),
        TRASH("[YELLOW]TRASH[]"),
        FLUSH("[GREEN]FLUSH[]"),
        LOST_FUEL("[RED]-FUEL[]"),
        LOST_SCORE("[RED]-SCORE[]");
        private String value;

        GameInfoSystemPreset(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
