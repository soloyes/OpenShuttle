package com.buran.game.desktop;

/**
 * @author soloyes on 16/11/18.
 */

public class ShareDesktop implements com.buran.game.Share {

    public ShareDesktop() {
    }

    @Override
    public void share(String text) {
        String out = text.isEmpty() ? "No results until now..." : text;
        System.out.println("Hey, check my achievements in Buran-19!\n" + out);
    }
}