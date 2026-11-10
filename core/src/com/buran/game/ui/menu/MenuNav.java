package com.buran.game.ui.menu;

/**
 * The menu that currently owns directional input. The gamepad/keyboard layer sends
 * {@link Nav} events here instead of moving a cursor.
 */
public final class MenuNav {
    /** Something that reacts to menu navigation. */
    public interface Target {
        void nav(Nav nav);
    }

    private static Target target;

    private MenuNav() {
    }

    public static void set(Target t) {
        target = t;
    }

    public static Target get() {
        return target;
    }

    public static void clear(Target t) {
        if (target == t) {
            target = null;
        }
    }

    public static boolean send(Nav nav) {
        if (target == null) {
            return false;
        }
        target.nav(nav);
        return true;
    }
}
