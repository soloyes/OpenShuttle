package com.buran.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.conf.Rules;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.groups.PauseGroup;
import com.buran.game.ui.groups.selector.SelectorGroup;

/**
 * Gamepad (Xbox controller through the browser Gamepad API) and keyboard input.
 * <p>
 * The game was built for touch. Instead of rewriting every scene2d screen, this class
 * works in two modes:
 * <ul>
 *     <li><b>Flight</b> (game running, not paused): the left stick steers the Buran, the
 *     item carousel and pause get dedicated buttons.</li>
 *     <li><b>Menu</b> (menus, pause, game over): the stick moves an on-screen cursor and
 *     A sends a real touch to the current input processor, so every existing button,
 *     slider and scroll pane works without changes.</li>
 * </ul>
 * Polled once per frame from {@code BuranGame.render()}.
 */
public final class Gamepad {
    private static final Gamepad INSTANCE = new Gamepad();

    private static final float DEAD_ZONE = 0.22f;
    /** Cursor speed at full stick deflection, in screen heights per second. */
    private static final float CURSOR_SPEED = 1.1f;
    private static final float SCROLL_REPEAT = 0.12f;
    /** Synthetic touches use a pointer index real fingers never reach. */
    private static final int CURSOR_POINTER = 0;

    private final Vector2 steer = new Vector2();
    private boolean steering;

    private float cursorX = -1, cursorY = -1;
    private boolean cursorVisible;
    private boolean cursorDown;
    private float scrollTimer;
    private Texture cursorTexture;
    private final Matrix4 screenMatrix = new Matrix4();
    private final Matrix4 savedMatrix = new Matrix4();

    private Controller controller;
    private boolean pulse;
    private float pulseCooldown;
    private final boolean[] prev = new boolean[Action.values().length];
    private final boolean[] now = new boolean[Action.values().length];

    private enum Action {
        A, B, START, BACK, PREV, NEXT, UP, DOWN, LEFT, RIGHT, USE_ALT, PULSE
    }

    /** Menu entries the D-pad can step through (the cursor jumps onto them). */
    private static final com.badlogic.gdx.utils.Array<com.badlogic.gdx.scenes.scene2d.Actor> FOCUSABLES =
            new com.badlogic.gdx.utils.Array<com.badlogic.gdx.scenes.scene2d.Actor>();
    private int focusIndex = -1;
    private final Vector2 tmpFocus = new Vector2();

    public static void addFocusable(com.badlogic.gdx.scenes.scene2d.Actor actor) {
        FOCUSABLES.add(actor);
    }

    private boolean enabled = true;
    private boolean waitRelease;

    /**
     * Platforms can hold input back (the web start overlay). When re-enabled, buttons held at
     * that moment are ignored until released, so the press that closed the overlay does not
     * also click in the menu.
     */
    public void setEnabled(boolean enabled) {
        if (enabled && !this.enabled) {
            waitRelease = true;
        }
        this.enabled = enabled;
    }

    public static Gamepad get() {
        return INSTANCE;
    }

    private Gamepad() {
    }

    // ------------------------------------------------------------------ polling

    public void update(float dt) {
        controller = Controllers.getCurrent();
        System.arraycopy(now, 0, prev, 0, now.length);
        readButtons();
        if (!enabled || waitRelease) {
            boolean any = false;
            for (boolean b : now) {
                any |= b;
            }
            if (enabled && !any) {
                waitRelease = false;
            }
            // Pretend everything is held so nothing registers as a fresh press.
            java.util.Arrays.fill(prev, true);
            java.util.Arrays.fill(now, false);
            steering = false;
            return;
        }

        float lx = 0, ly = 0, ry = 0;
        if (controller != null) {
            ControllerMapping m = controller.getMapping();
            lx = axis(m.axisLeftX);
            ly = axis(m.axisLeftY);
            ry = axis(m.axisRightY);
        }

        pulse = false;
        pulseCooldown = Math.max(0, pulseCooldown - dt);
        if (isFlight()) {
            cursorVisible = false;
            if (pressed(Action.PULSE) && pulseCooldown <= 0) {
                pulse = true;
                pulseCooldown = Rules.PULSE_COOLDOWN;
            }
            releaseCursor();
            com.buran.game.campaign.SectorRun run = com.buran.game.campaign.Campaign.run();
            if (run != null && run.isBriefing()) {
                steering = false;
                if (pressed(Action.A)) {
                    run.advance();
                }
                if (pressed(Action.START) || pressed(Action.B)) {
                    PauseGroup.togglePause();
                }
                return;
            }
            updateFlight(lx, ly);
            if (pressed(Action.A) || pressed(Action.USE_ALT)) {
                SelectorGroup.getInstance().useSelected();
            }
            if (pressed(Action.PREV) || pressed(Action.UP) || pressed(Action.LEFT)) {
                SelectorGroup.getInstance().selectPrevious();
            }
            if (pressed(Action.NEXT) || pressed(Action.DOWN) || pressed(Action.RIGHT)) {
                SelectorGroup.getInstance().selectNext();
            }
            // Menu, View or B: Edge on Xbox may keep Menu/View for itself, B always reaches us.
            if (pressed(Action.START) || pressed(Action.BACK) || pressed(Action.B)) {
                PauseGroup.togglePause();
            }
        } else {
            steering = false;
            // ESC is both BACK in a menu and START; in a menu it must act only once.
            boolean menuHadNav = com.buran.game.ui.menu.MenuNav.get() != null;
            if (com.buran.game.ui.menu.MenuNav.get() != null) {
                cursorVisible = false;
                releaseCursor();
                updateMenuNav(dt, lx, ly);
            } else if (controller != null) {
                updateCursor(dt, lx, ly, ry);
            }
            boolean menuOwnsB = com.buran.game.ui.menu.MenuNav.get() != null;
            if (inGameScreen() && !Rules.GAME_OVER && ((pressed(Action.START) && !(menuHadNav && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)))
                    || (pressed(Action.B) && !menuOwnsB))) {
                PauseGroup.togglePause();
            }
        }
    }

    private void readButtons() {
        Controller c = controller;
        ControllerMapping m = c == null ? null : c.getMapping();
        now[Action.A.ordinal()] = btn(c, m == null ? -1 : m.buttonA) || key(Input.Keys.SPACE) || key(Input.Keys.ENTER);
        now[Action.B.ordinal()] = btn(c, m == null ? -1 : m.buttonB);
        now[Action.START.ordinal()] = btn(c, m == null ? -1 : m.buttonStart) || key(Input.Keys.ESCAPE) || key(Input.Keys.P);
        now[Action.BACK.ordinal()] = btn(c, m == null ? -1 : m.buttonBack);
        now[Action.PREV.ordinal()] = btn(c, m == null ? -1 : m.buttonL1) || key(Input.Keys.Q);
        now[Action.NEXT.ordinal()] = btn(c, m == null ? -1 : m.buttonR1) || key(Input.Keys.E);
        now[Action.UP.ordinal()] = btn(c, m == null ? -1 : m.buttonDpadUp);
        now[Action.DOWN.ordinal()] = btn(c, m == null ? -1 : m.buttonDpadDown);
        now[Action.LEFT.ordinal()] = btn(c, m == null ? -1 : m.buttonDpadLeft);
        now[Action.RIGHT.ordinal()] = btn(c, m == null ? -1 : m.buttonDpadRight);
        // Right trigger also fires the selected item; on the standard mapping it is button 7.
        now[Action.PULSE.ordinal()] = btn(c, m == null ? -1 : m.buttonY) || key(Input.Keys.F);
        now[Action.USE_ALT.ordinal()] = btn(c, m == null ? -1 : m.buttonR2) || btn(c, m == null ? -1 : m.buttonX);
    }

    private static boolean btn(Controller c, int code) {
        return c != null && code >= 0 && c.getButton(code);
    }

    private static boolean key(int keycode) {
        return Gdx.input.isKeyPressed(keycode);
    }

    private boolean pressed(Action a) {
        return now[a.ordinal()] && !prev[a.ordinal()];
    }

    private boolean held(Action a) {
        return now[a.ordinal()];
    }

    private float axis(int code) {
        if (controller == null || code < 0) {
            return 0;
        }
        float v = controller.getAxis(code);
        return Math.abs(v) < DEAD_ZONE ? 0 : v;
    }

    // ------------------------------------------------------------------ flight

    private void updateFlight(float lx, float ly) {
        // Stick Y points down; world Y points up.
        steer.set(lx, -ly);
        float kx = 0, ky = 0;
        if (key(Input.Keys.LEFT) || key(Input.Keys.A)) kx -= 1;
        if (key(Input.Keys.RIGHT) || key(Input.Keys.D)) kx += 1;
        if (key(Input.Keys.UP) || key(Input.Keys.W)) ky += 1;
        if (key(Input.Keys.DOWN) || key(Input.Keys.S)) ky -= 1;
        if (kx != 0 || ky != 0) {
            steer.set(kx, ky).nor();
        }
        steering = steer.len2() > 0;
    }

    /** 0 right after a pulse, 1 when the next one is ready. */
    public float pulseCharge() {
        return 1f - com.badlogic.gdx.math.MathUtils.clamp(pulseCooldown / Rules.PULSE_COOLDOWN, 0f, 1f);
    }

    /** True for the single frame an EMP pulse is fired. */
    public boolean isPulse() {
        return pulse;
    }

    /** Whether this frame's pulse reaches the given world position. */
    public static boolean pulseHits(Vector2 position) {
        return INSTANCE.pulse && position.dst(com.buran.game.player.Buran.position) <= Rules.PULSE_RANGE;
    }

    /** True while a controller is the active input device. */
    public boolean hasController() {
        return controller != null;
    }

    /** True while the left stick (or arrow keys) asks the Buran to fly somewhere. */
    public boolean isSteering() {
        return steering;
    }

    /** Desired heading in degrees, same convention as {@code Vector2.angle()}. */
    public float getSteerAngle() {
        return steer.angleDeg();
    }

    /** Thrust 0..1 from stick deflection. */
    public float getSteerPower() {
        return Math.min(1f, steer.len());
    }

    // ------------------------------------------------------------------ menu cursor

    private void updateCursor(float dt, float lx, float ly, float ry) {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        if (cursorX < 0) {
            cursorX = w / 2f;
            cursorY = h / 2f;
        }

        float dx = curve(lx);
        float dy = curve(ly);
        boolean focusMode = hasVisibleFocusables();
        if (focusMode && focusIndex < 0) {
            moveFocus(1);   // start on the first entry (PLAY)
        }
        if (focusMode && (pressed(Action.UP) || pressed(Action.DOWN))) {
            moveFocus(pressed(Action.DOWN) ? 1 : -1);
        }
        if (held(Action.LEFT)) dx -= 0.6f;
        if (held(Action.RIGHT)) dx += 0.6f;
        if (!focusMode) {
            if (held(Action.UP)) dy -= 0.6f;
            if (held(Action.DOWN)) dy += 0.6f;
        }

        boolean moved = dx != 0 || dy != 0;
        if (moved || ry != 0) {
            cursorVisible = true;
        }

        InputProcessor ip = Gdx.input.getInputProcessor();
        if (moved) {
            float speed = CURSOR_SPEED * h * dt;
            cursorX = clamp(cursorX + dx * speed, 0, w - 1);
            cursorY = clamp(cursorY + dy * speed, 0, h - 1);
            if (ip != null) {
                if (cursorDown) {
                    ip.touchDragged((int) cursorX, (int) cursorY, CURSOR_POINTER);
                } else {
                    ip.mouseMoved((int) cursorX, (int) cursorY);
                }
            }
        }

        if (ip != null && controller != null) {
            if (held(Action.A) && !cursorDown) {
                cursorDown = true;
                ip.touchDown((int) cursorX, (int) cursorY, CURSOR_POINTER, Input.Buttons.LEFT);
            } else if (!held(Action.A) && cursorDown) {
                releaseCursor();
            }

            // Right stick scrolls lists under the cursor.
            scrollTimer -= dt;
            if (ry != 0 && scrollTimer <= 0) {
                ip.mouseMoved((int) cursorX, (int) cursorY);
                ip.scrolled(0, Math.signum(ry));
                scrollTimer = SCROLL_REPEAT;
            }
        }
    }

    private static boolean hasVisibleFocusables() {
        for (com.badlogic.gdx.scenes.scene2d.Actor a : FOCUSABLES) {
            if (a.isVisible() && a.getStage() != null) {
                return true;
            }
        }
        return false;
    }

    /** Steps the D-pad focus and puts the cursor on the entry (so A clicks it). */
    private void moveFocus(int dir) {
        int n = FOCUSABLES.size;
        for (int k = 0; k < n; k++) {
            focusIndex = focusIndex < 0 ? (dir > 0 ? 0 : n - 1) : (focusIndex + dir + n) % n;
            com.badlogic.gdx.scenes.scene2d.Actor a = FOCUSABLES.get(focusIndex);
            if (a.isVisible() && a.getStage() != null) {
                tmpFocus.set(a.getWidth() * 0.3f, a.getHeight() / 2f);
                a.localToStageCoordinates(tmpFocus);
                a.getStage().stageToScreenCoordinates(tmpFocus);
                cursorX = tmpFocus.x;
                cursorY = tmpFocus.y;
                // The entry highlights itself; the cursor would only cover the text.
                cursorVisible = false;
                InputProcessor ip = Gdx.input.getInputProcessor();
                if (ip != null) {
                    ip.mouseMoved((int) cursorX, (int) cursorY);
                }
                return;
            }
        }
    }

    private com.buran.game.ui.menu.Nav heldNav;
    private float navRepeat;

    /** Menus with a focus list get discrete navigation instead of the cursor. */
    private void updateMenuNav(float dt, float lx, float ly) {
        com.buran.game.ui.menu.Nav dir = null;
        if (held(Action.UP) || ly < -0.5f) dir = com.buran.game.ui.menu.Nav.UP;
        else if (held(Action.DOWN) || ly > 0.5f) dir = com.buran.game.ui.menu.Nav.DOWN;
        else if (held(Action.LEFT) || lx < -0.5f) dir = com.buran.game.ui.menu.Nav.LEFT;
        else if (held(Action.RIGHT) || lx > 0.5f) dir = com.buran.game.ui.menu.Nav.RIGHT;

        if (dir != heldNav) {
            heldNav = dir;
            navRepeat = 0.38f;
            if (dir != null) {
                com.buran.game.ui.menu.MenuNav.send(dir);
            }
        } else if (dir != null) {
            navRepeat -= dt;
            if (navRepeat <= 0f) {
                navRepeat = 0.11f;
                com.buran.game.ui.menu.MenuNav.send(dir);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) com.buran.game.ui.menu.MenuNav.send(com.buran.game.ui.menu.Nav.UP);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) com.buran.game.ui.menu.MenuNav.send(com.buran.game.ui.menu.Nav.DOWN);
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) com.buran.game.ui.menu.MenuNav.send(com.buran.game.ui.menu.Nav.LEFT);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) com.buran.game.ui.menu.MenuNav.send(com.buran.game.ui.menu.Nav.RIGHT);

        if (pressed(Action.A)) {
            com.buran.game.ui.menu.MenuNav.send(com.buran.game.ui.menu.Nav.ACCEPT);
        } else if (pressed(Action.B) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            com.buran.game.ui.menu.MenuNav.send(com.buran.game.ui.menu.Nav.BACK);
        }
    }

    private void releaseCursor() {
        if (cursorDown) {
            cursorDown = false;
            InputProcessor ip = Gdx.input.getInputProcessor();
            if (ip != null) {
                ip.touchUp((int) cursorX, (int) cursorY, CURSOR_POINTER, Input.Buttons.LEFT);
            }
        }
    }

    /** Called when the screen changes so a held A does not leak into the next screen. */
    public void onScreenChange() {
        cursorDown = false;
        FOCUSABLES.clear();
        focusIndex = -1;
        cursorVisible = false;
    }

    private static float curve(float v) {
        return v * Math.abs(v);
    }

    private static float clamp(float v, float min, float max) {
        return v < min ? min : (v > max ? max : v);
    }

    // ------------------------------------------------------------------ state

    private static boolean inGameScreen() {
        return ScreenManager.getInstance().getCurrentScreen() == ScreenManager.ScreenType.GAME;
    }

    private static boolean isFlight() {
        com.buran.game.campaign.SectorRun run = com.buran.game.campaign.Campaign.run();
        return inGameScreen() && !PauseGroup.isPause() && !Rules.GAME_OVER && PauseGroup.isActive()
                && (run == null || !run.isClear());
    }

    // ------------------------------------------------------------------ render

    /** Draws the menu cursor on top of everything, in screen pixels. */
    public void render(SpriteBatch batch) {
        if (!cursorVisible || controller == null || isFlight()) {
            return;
        }
        if (cursorTexture == null) {
            cursorTexture = createCursorTexture();
        }
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        float size = Math.max(28, h / 22f);
        screenMatrix.setToOrtho2D(0, 0, w, h);
        savedMatrix.set(batch.getProjectionMatrix());
        batch.setShader(null);
        batch.setProjectionMatrix(screenMatrix);
        batch.begin();
        batch.setColor(cursorDown ? Color.CYAN : Color.WHITE);
        batch.draw(cursorTexture, cursorX - size / 2, (h - cursorY) - size / 2, size, size);
        batch.setColor(Color.WHITE);
        batch.end();
        batch.setProjectionMatrix(savedMatrix);
    }

    private static Texture createCursorTexture() {
        int s = 64;
        Pixmap p = new Pixmap(s, s, Pixmap.Format.RGBA8888);
        p.setBlending(Pixmap.Blending.None);
        p.setColor(0, 0, 0, 0);
        p.fill();
        p.setColor(0, 0, 0, 0.85f);
        p.fillCircle(s / 2, s / 2, s / 2 - 1);
        p.setColor(0, 0, 0, 0);
        p.fillCircle(s / 2, s / 2, s / 2 - 10);
        p.setColor(1, 1, 1, 1);
        for (int r = s / 2 - 4; r >= s / 2 - 7; r--) {
            p.drawCircle(s / 2, s / 2, r);
        }
        p.fillCircle(s / 2, s / 2, 5);
        Texture t = new Texture(p);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        p.dispose();
        return t;
    }
}
