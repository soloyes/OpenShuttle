package com.buran.game.ui.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

/**
 * A text entry of the main menu. Highlighted on hover/focus with a marker bar, dimmed when
 * disabled. Behaves like any scene2d Button, so existing click listeners keep working.
 */
public class MenuItem extends Button {
    private static Texture pixel;
    private static final Color IDLE = Color.valueOf("8fa3bdff");
    private static final Color ACTIVE = Color.valueOf("f2f5faff");
    private static final Color MARK = Color.valueOf("ff6a45ff");

    private final BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();
    private final String text;
    private float highlight;
    private final Color tmp = new Color();

    public MenuItem(String text, BitmapFont font) {
        super(new ButtonStyle());
        this.text = text;
        this.font = font;
        layout.setText(font, text);
        setSize(layout.width + 80, layout.height + 50);
        if (pixel == null) {
            Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            p.setColor(Color.WHITE);
            p.fill();
            pixel = new Texture(p);
            p.dispose();
        }
    }

    public boolean isHighlighted() {
        return !isDisabled() && (isOver() || isPressed());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float target = isHighlighted() ? 1f : 0f;
        highlight += (target - highlight) * Math.min(1f, delta * 12f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float a = parentAlpha * getColor().a * (isDisabled() ? 0.35f : 1f);
        float shift = 28f * highlight;
        Color old = batch.getColor().cpy();

        // Marker bar to the left of the highlighted entry.
        if (highlight > 0.01f) {
            batch.setColor(MARK.r, MARK.g, MARK.b, a * highlight);
            batch.draw(pixel, getX(), getY() + getHeight() / 2f - layout.height / 2f, 10f, layout.height);
        }
        batch.setColor(old);

        tmp.set(IDLE).lerp(ACTIVE, highlight);
        tmp.a = a;
        Color fontOld = font.getColor().cpy();
        font.setColor(tmp);
        font.draw(batch, text, getX() + 40f + shift, getY() + getHeight() / 2f + layout.height / 2f);
        font.setColor(fontOld);
    }
}
