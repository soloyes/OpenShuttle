package com.shuttle.game.desktop;

import com.shuttle.game.Share;

public class ShareDesktop implements Share {

    public ShareDesktop() {
    }

    @Override
    public void share(String text) {
        String out = text.isEmpty() ? "No results until now..." : text;
        System.out.println("Hey, check my achievements in Shuttle!\n" + out);
    }
}
