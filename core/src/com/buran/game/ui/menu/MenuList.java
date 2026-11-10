package com.buran.game.ui.menu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.buran.game.conf.MSConstants;
import com.buran.game.effects.BoomBox;

/**
 * A vertical list of {@link Row}s inside a box, with one focused row and scrolling that
 * keeps the focus in view. Up/Down move the focus; other navigation goes to the row.
 */
public class MenuList extends Group implements MenuNav.Target {
    /** Called on BACK; may be null. */
    public interface BackHandler {
        void back();
    }

    /** Notified whenever the focused row changes. */
    public interface FocusListener {
        void focused(Row row, int index);
    }

    private final Array<Row> rows = new Array<Row>();
    private int focus = -1;
    private float boxX, boxY, boxW, boxH;
    private float scroll;
    private float scrollTarget;
    private BackHandler back;
    private FocusListener focusListener;
    private final BoomBox boomBox = new BoomBox();
    private float gap = 6f;

    public MenuList() {
        setTransform(false);
    }

    public MenuList onBack(BackHandler back) {
        this.back = back;
        return this;
    }

    public MenuList onFocus(FocusListener l) {
        this.focusListener = l;
        return this;
    }

    public <T extends Row> T add(T row) {
        row.list = this;
        rows.add(row);
        addActor(row);
        return row;
    }

    public void clearRows() {
        for (Row r : rows) {
            r.remove();
        }
        rows.clear();
        focus = -1;
        scroll = scrollTarget = 0;
    }

    public Array<Row> rows() {
        return rows;
    }

    public Row focused() {
        return focus >= 0 && focus < rows.size ? rows.get(focus) : null;
    }

    public int focusIndex() {
        return focus;
    }

    public void setBox(float x, float y, float w, float h) {
        boxX = x;
        boxY = y;
        boxW = w;
        boxH = h;
        for (Row r : rows) {
            r.setWidth(w);
        }
    }

    /** Focuses the first focusable row (or keeps the current one). */
    public void focusFirst() {
        if (focus >= 0 && focus < rows.size && rows.get(focus).isFocusable()) {
            applyFocus();
            return;
        }
        for (int i = 0; i < rows.size; i++) {
            if (rows.get(i).isFocusable()) {
                focus = i;
                break;
            }
        }
        applyFocus();
    }

    void focus(Row row, boolean sound) {
        int i = rows.indexOf(row, true);
        if (i >= 0 && i != focus && row.isFocusable()) {
            focus = i;
            applyFocus();
            if (sound) {
                boomBox.playSound(MSConstants.UI_CLICK);
            }
        }
    }

    private void applyFocus() {
        for (int i = 0; i < rows.size; i++) {
            rows.get(i).focused = i == focus;
        }
        if (focusListener != null && focused() != null) {
            focusListener.focused(focused(), focus);
        }
    }

    private void step(int dir) {
        int n = rows.size;
        if (n == 0) {
            return;
        }
        int i = focus;
        for (int k = 0; k < n; k++) {
            i += dir;
            if (i < 0 || i >= n) {
                // Past the end: scroll text-only content instead of wrapping.
                scrollTarget += dir > 0 ? 120f : -120f;
                return;
            }
            if (rows.get(i).isFocusable()) {
                focus = i;
                applyFocus();
                boomBox.playSound(MSConstants.UI_CLICK);
                return;
            }
        }
    }

    @Override
    public void nav(Nav nav) {
        switch (nav) {
            case UP:
                step(-1);
                break;
            case DOWN:
                step(1);
                break;
            case BACK:
                if (back != null) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                    back.back();
                }
                break;
            default:
                Row r = focused();
                if (r != null && r.onNav(nav)) {
                    boomBox.playSound(nav == Nav.ACCEPT ? MSConstants.UI_MENU : MSConstants.UI_CHANGED);
                }
                break;
        }
    }

    /** Total height of all rows. */
    public float contentHeight() {
        float h = 0;
        for (Row r : rows) {
            h += r.getHeight() + gap;
        }
        return h;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Keep the focused row inside the box.
        float y = 0;
        for (int i = 0; i < rows.size; i++) {
            Row r = rows.get(i);
            if (i == focus) {
                float top = y;
                float bottom = y + r.getHeight();
                if (top < scrollTarget) {
                    scrollTarget = top;
                } else if (bottom > scrollTarget + boxH) {
                    scrollTarget = bottom - boxH;
                }
            }
            y += r.getHeight() + gap;
        }
        scrollTarget = Math.max(0, Math.min(scrollTarget, Math.max(0, contentHeight() - boxH)));
        scroll += (scrollTarget - scroll) * Math.min(1f, delta * 12f);

        float cursor = boxY + boxH + scroll;
        for (Row r : rows) {
            r.setWidth(boxW);
            cursor -= r.getHeight();
            r.setPosition(boxX, cursor);
            boolean inside = cursor >= boxY - 2 && cursor + r.getHeight() <= boxY + boxH + 2;
            r.setVisible(inside);
            cursor -= gap;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        // Scroll hints.
        if (scroll > 4f) {
            Theme.rect(batch, boxX + boxW / 2f - 40f, boxY + boxH + 10f, 80f, 6f, Theme.FAINT, parentAlpha);
        }
        if (scroll < contentHeight() - boxH - 4f) {
            Theme.rect(batch, boxX + boxW / 2f - 40f, boxY - 16f, 80f, 6f, Theme.FAINT, parentAlpha);
        }
    }
}
