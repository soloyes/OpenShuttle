package com.buran.game.platform;

/**
 * Voice over the radio (sector briefings). Platforms that can speak install a {@link Speaker};
 * elsewhere the calls do nothing and the typed card carries the message alone.
 */
public final class Radio {
    /** Platform voice. */
    public interface Speaker {
        void say(String text);

        void stop();
    }

    private static Speaker speaker;

    private Radio() {
    }

    public static void install(Speaker s) {
        speaker = s;
    }

    public static void say(String text) {
        if (speaker != null) {
            speaker.say(text);
        }
    }

    public static void stop() {
        if (speaker != null) {
            speaker.stop();
        }
    }
}
