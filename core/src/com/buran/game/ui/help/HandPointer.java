package com.buran.game.ui.help;

import com.buran.game.input.Gamepad;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.effects.Oscillator;
import com.buran.game.utils.Assets;

public class HandPointer extends Group {
    private TextureRegion hand;
    private TextureRegion handPointer;
    private float scale = 1.0f;
    private Oscillator oscillatorScale;
    private float x, y;
    private float time = 0.5f;
    private float t;
    private float a;

    public static boolean done;
    private final boolean touchDevice;
    private final BitmapFont hintFont;
    private final GlyphLayout hintLayout = new GlyphLayout();

    public HandPointer() {
        done = false;
        this.touchDevice = Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android
                || Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.iOS;
        this.hintFont = Assets.getInstance().getAssetManager().get(PaintConstants.FONT48, BitmapFont.class);
        this.hand = Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_HELP_HAND);
        this.handPointer = Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_HELP_HAND_POINTER);
        this.oscillatorScale = new Oscillator();
        this.x = Rules.WORLD_WIDTH / 2 - PaintConstants.UI_HELP_HAND_WIDTH / 2 - PaintConstants.UI_HELP_HAND_POINTER_WIDTH / 2;
        this.y = Rules.WORLD_HEIGHT / 3 - PaintConstants.UI_HELP_HAND_HEIGHT;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!touchDevice) {
            drawControlsHint(batch);
            return;
        }
        Color color = batch.getColor();
        batch.setColor(Color.WHITE);
        batch.draw(hand,
                x, y,
                PaintConstants.UI_HELP_HAND_WIDTH, PaintConstants.UI_HELP_HAND_HEIGHT,
                PaintConstants.UI_HELP_HAND_WIDTH, PaintConstants.UI_HELP_HAND_HEIGHT,
                scale, scale, 0.0f
        );
        batch.setColor(1.0f, 1.0f, 1.0f, a);
        batch.draw(handPointer,
                x + PaintConstants.UI_HELP_HAND_WIDTH - PaintConstants.UI_HELP_HAND_POINTER_WIDTH / 3, y + PaintConstants.UI_HELP_HAND_HEIGHT - PaintConstants.UI_HELP_HAND_POINTER_HEIGHT / 3,
                PaintConstants.UI_HELP_HAND_POINTER_WIDTH / 2, PaintConstants.UI_HELP_HAND_POINTER_HEIGHT / 2
        );
        batch.setColor(color);
    }

    /** On TV/PC the first-run hint is the control scheme, not a tapping hand. */
    private void drawControlsHint(Batch batch) {
        if (com.buran.game.ui.groups.PauseGroup.isPause() || com.buran.game.conf.Rules.GAME_OVER
                || com.buran.game.campaign.Campaign.current() != null) {
            return;
        }
        String line1 = "{LS} FLY      {X} USE ITEM      {LB} {RB} SWITCH ITEM";
        String line2 = "{A} RAM SHIELD      {Y} EMP      {MENU} PAUSE";
        float a = 0.55f + 0.45f * scale;
        float y = Rules.WORLD_HEIGHT / 3f;
        for (String line : new String[]{line1, line2}) {
            float w = com.buran.game.ui.menu.Theme.richWidth(hintFont, line);
            com.buran.game.ui.menu.Theme.rich(batch, hintFont, line, (Rules.WORLD_WIDTH - w) / 2f, y,
                    com.buran.game.ui.menu.Theme.TEXT, a);
            y -= 100f;
        }
    }

    @Override
    public void act(float delta) {
        scale = oscillatorScale.oscillate(scale, 0.2f, 0.8f, 1.0f, delta);
        t += delta;
        if (t >= 0.0f) {
            a = 1.0f;
        }
        if (t >= time) {
            a = 0.0f;
        }
        if (t >= time * 2) {
            t = 0.0f;
        }
        if ((Gdx.input.justTouched() || Gamepad.get().isSteering()) && !done) {
            setVisible(false);
            done = true;
        }
    }
}
