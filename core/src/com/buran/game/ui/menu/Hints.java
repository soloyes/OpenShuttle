package com.buran.game.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.buran.game.input.Gamepad;

/** Button hints in one form everywhere ("A  -  SELECT"), for the pad when one is connected. */
public final class Hints {
    private Hints() {
    }

    public static boolean pad() {
        return Gamepad.get().hasController();
    }

    /** A touch screen and no pad: hints about buttons mean nothing. */
    public static boolean touch() {
        return !pad() && Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen);
    }

    /** The key to show: the pad button when a pad is connected, else the keyboard key. */
    public static String button(String padButton, String keyboardKey) {
        return pad() ? padButton : keyboardKey;
    }

    /** "A  -  " with a pad, "ENTER  -  " on a keyboard. */
    public static String key(String padButton, String keyboardKey) {
        return (pad() ? padButton : keyboardKey) + "  -  ";
    }
}
