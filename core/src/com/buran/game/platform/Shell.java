package com.buran.game.platform;

/**
 * What the host page around the game is doing (the web boot screen). Desktop and Android have
 * no such page: everything is shown at once.
 */
public final class Shell {
    public interface Bridge {
        /** The boot screen is gone and the game is what the player sees. */
        boolean shown();

        /** Tells the page that the intro comes next (it then skips its own launch animation). */
        void introPending(boolean pending);
    }

    public static Bridge bridge;

    private Shell() {
    }

    public static boolean shown() {
        return bridge == null || bridge.shown();
    }

    public static void introPending(boolean pending) {
        if (bridge != null) {
            bridge.introPending(pending);
        }
    }
}
