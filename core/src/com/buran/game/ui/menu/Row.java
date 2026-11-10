package com.buran.game.ui.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

/**
 * One line of a menu list: a label on the left, an optional value on the right.
 * Subclasses decide what LEFT/RIGHT/ACCEPT do. Touch: tapping the left or right third of a
 * row with a value steps it down/up, tapping elsewhere accepts.
 */
public abstract class Row extends Actor {
    public interface BoolGet {
        boolean get();
    }

    public interface BoolSet {
        void set(boolean v);
    }

    public interface FloatGet {
        float get();
    }

    public interface FloatSet {
        void set(float v);
    }

    public interface IntGet {
        int get();
    }

    public interface IntSet {
        void set(int v);
    }

    protected String label;
    protected final BitmapFont font;
    protected final GlyphLayout layout = new GlyphLayout();
    private final Color tmp = new Color();
    float focusAmount;
    boolean focused;
    MenuList list;
    protected boolean enabled = true;

    protected Row(String label, BitmapFont font) {
        this.label = label;
        this.font = font;
        setHeight(font.getLineHeight() + 34f);
        addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && list != null && isFocusable()) {
                    list.focus(Row.this, false);
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return isFocusable();
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (list == null || x < 0 || y < 0 || x > getWidth() || y > getHeight()) {
                    return;
                }
                list.focus(Row.this, false);
                if (hasValue() && x < getWidth() * 0.33f && x > getWidth() * 0.0f && valueTapZones()) {
                    onNav(Nav.LEFT);
                } else if (hasValue() && x > getWidth() * 0.67f && valueTapZones()) {
                    onNav(Nav.RIGHT);
                } else {
                    onNav(Nav.ACCEPT);
                }
            }
        });
    }

    /** Whether the row can take focus (info rows can't). */
    public boolean isFocusable() {
        return enabled;
    }

    protected boolean hasValue() {
        return value() != null;
    }

    /** Sliders and choices step with left/right taps; toggles just flip. */
    protected boolean valueTapZones() {
        return true;
    }

    /** Text shown on the right, or null. */
    protected String value() {
        return null;
    }

    /** Handles LEFT/RIGHT/ACCEPT. Returns true if something changed. */
    protected abstract boolean onNav(Nav nav);

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float target = focused ? 1f : 0f;
        focusAmount += (target - focusAmount) * Math.min(1f, delta * 14f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float a = parentAlpha * getColor().a * (enabled ? 1f : 0.4f);
        float f = focusAmount;
        if (f > 0.01f) {
            Theme.rect(batch, getX(), getY() + 4, getWidth(), getHeight() - 8, Theme.LINE, 0.07f * f * a);
            Theme.rect(batch, getX(), getY() + 14, 8f, getHeight() - 28, Theme.ACCENT, f * a);
        }
        tmp.set(Theme.DIM).lerp(Theme.TEXT, f);
        tmp.a = a;
        float textY = getY() + getHeight() / 2f + font.getCapHeight() / 2f;
        Color old = font.getColor().cpy();
        font.setColor(tmp);
        font.draw(batch, label, getX() + 36f + 14f * f, textY);
        drawValue(batch, a, textY);
        font.setColor(old);
    }

    protected void drawValue(Batch batch, float alpha, float textY) {
        String v = value();
        if (v == null) {
            return;
        }
        Color old = font.getColor().cpy();
        font.setColor(focusAmount > 0.5f ? Theme.TEXT.r : Theme.DIM.r, focusAmount > 0.5f ? Theme.TEXT.g : Theme.DIM.g,
                focusAmount > 0.5f ? Theme.TEXT.b : Theme.DIM.b, alpha);
        String shown = focusAmount > 0.5f ? "<  " + v + "  >" : v;
        layout.setText(font, shown);
        font.draw(batch, shown, getX() + getWidth() - 36f - layout.width, textY);
        font.setColor(old);
    }

    // ------------------------------------------------------------------ kinds

    /** Runs an action on ACCEPT. */
    public static class Action extends Row {
        private final Runnable run;
        private String value;
        private Color valueColor = Theme.OK;

        public Action(String label, BitmapFont font, Runnable run) {
            super(label, font);
            this.run = run;
        }

        public Action withValue(String value) {
            this.value = value;
            return this;
        }

        public Action withValue(String value, Color color) {
            this.value = value;
            this.valueColor = color;
            return this;
        }

        @Override
        protected String value() {
            return value;
        }

        @Override
        protected boolean valueTapZones() {
            return false;
        }

        @Override
        protected void drawValue(Batch batch, float alpha, float textY) {
            if (value == null) {
                return;
            }
            Color old = font.getColor().cpy();
            font.setColor(valueColor.r, valueColor.g, valueColor.b, alpha);
            layout.setText(font, value);
            font.draw(batch, value, getX() + getWidth() - 36f - layout.width, textY);
            font.setColor(old);
        }

        @Override
        protected boolean onNav(Nav nav) {
            if (nav == Nav.ACCEPT && run != null) {
                run.run();
                return true;
            }
            return false;
        }
    }

    /** ON / OFF. */
    public static class Toggle extends Row {
        private final BoolGet get;
        private final BoolSet set;

        public Toggle(String label, BitmapFont font, BoolGet get, BoolSet set) {
            super(label, font);
            this.get = get;
            this.set = set;
        }

        @Override
        protected String value() {
            return get.get() ? "ON" : "OFF";
        }

        @Override
        protected boolean valueTapZones() {
            return false;
        }

        @Override
        protected boolean onNav(Nav nav) {
            if (nav == Nav.ACCEPT || nav == Nav.LEFT || nav == Nav.RIGHT) {
                set.set(!get.get());
                return true;
            }
            return false;
        }
    }

    /** 0..1 value shown as a bar of ten cells. */
    public static class Slider extends Row {
        private final FloatGet get;
        private final FloatSet set;

        public Slider(String label, BitmapFont font, FloatGet get, FloatSet set) {
            super(label, font);
            this.get = get;
            this.set = set;
        }

        @Override
        protected String value() {
            return "";
        }

        @Override
        protected boolean onNav(Nav nav) {
            float v = get.get();
            if (nav == Nav.LEFT) {
                set.set(MathUtils.clamp(Math.round((v - 0.1f) * 10f) / 10f, 0f, 1f));
                return true;
            }
            if (nav == Nav.RIGHT || nav == Nav.ACCEPT) {
                float n = Math.round((v + 0.1f) * 10f) / 10f;
                if (nav == Nav.ACCEPT && v >= 0.999f) {
                    n = 0f;
                }
                set.set(MathUtils.clamp(n, 0f, 1f));
                return true;
            }
            return false;
        }

        @Override
        protected void drawValue(Batch batch, float alpha, float textY) {
            int cells = 10;
            float cw = 30f;
            float gap = 8f;
            float total = cells * cw + (cells - 1) * gap;
            float x = getX() + getWidth() - 36f - total;
            float h = 26f;
            float y = getY() + getHeight() / 2f - h / 2f;
            int on = Math.round(get.get() * cells);
            for (int i = 0; i < cells; i++) {
                Color c = i < on ? (focusAmount > 0.5f ? Theme.TEXT : Theme.DIM) : Theme.FAINT;
                Theme.rect(batch, x + i * (cw + gap), y, cw, h, c, alpha * (i < on ? 1f : 0.5f));
            }
        }
    }

    /** One of several named options. */
    public static class Choice extends Row {
        private final String[] options;
        private final IntGet get;
        private final IntSet set;

        public Choice(String label, BitmapFont font, String[] options, IntGet get, IntSet set) {
            super(label, font);
            this.options = options;
            this.get = get;
            this.set = set;
        }

        @Override
        protected String value() {
            int i = MathUtils.clamp(get.get(), 0, options.length - 1);
            return options[i];
        }

        @Override
        protected boolean onNav(Nav nav) {
            int i = get.get();
            if (nav == Nav.LEFT) {
                set.set((i - 1 + options.length) % options.length);
                return true;
            }
            if (nav == Nav.RIGHT || nav == Nav.ACCEPT) {
                set.set((i + 1) % options.length);
                return true;
            }
            return false;
        }
    }

    /** Read-only line: label and value, not focusable. */
    public static class Info extends Row {
        private final String value;

        public Info(String label, BitmapFont font, String value) {
            super(label, font);
            this.value = value;
            this.enabled = false;
        }

        @Override
        public boolean isFocusable() {
            return false;
        }

        @Override
        protected String value() {
            return value;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            float a = parentAlpha * getColor().a;
            float textY = getY() + getHeight() / 2f + font.getCapHeight() / 2f;
            Color old = font.getColor().cpy();
            font.setColor(Theme.DIM.r, Theme.DIM.g, Theme.DIM.b, a);
            font.draw(batch, label, getX() + 36f, textY);
            if (value != null) {
                font.setColor(Theme.TEXT.r, Theme.TEXT.g, Theme.TEXT.b, a);
                layout.setText(font, value);
                font.draw(batch, value, getX() + getWidth() - 36f - layout.width, textY);
            }
            font.setColor(old);
        }

        @Override
        protected boolean onNav(Nav nav) {
            return false;
        }
    }

    /** A wrapped paragraph (story, rules). Not focusable. */
    public static class Text extends Row {
        private final String text;
        private final GlyphLayout wrapped = new GlyphLayout();
        private float laidOutWidth = -1;

        public Text(String text, BitmapFont font) {
            super("", font);
            this.text = text;
            this.enabled = false;
        }

        @Override
        public boolean isFocusable() {
            return false;
        }

        @Override
        protected void sizeChanged() {
            relayout();
        }

        private void relayout() {
            if (getWidth() <= 0 || getWidth() == laidOutWidth) {
                return;
            }
            laidOutWidth = getWidth();
            wrapped.setText(font, text, Theme.DIM, getWidth() - 72f, Align.left, true);
            setHeight(wrapped.height + 40f);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            relayout();
            Color old = font.getColor().cpy();
            font.setColor(Theme.DIM.r, Theme.DIM.g, Theme.DIM.b, parentAlpha * getColor().a);
            font.draw(batch, text, getX() + 36f, getY() + getHeight() - 20f, getWidth() - 72f, Align.left, true);
            font.setColor(old);
        }

        @Override
        protected boolean onNav(Nav nav) {
            return false;
        }
    }
}
