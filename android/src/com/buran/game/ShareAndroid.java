package com.buran.game;

import android.content.Intent;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.buran.game.conf.Rules;

/**
 * @author soloyes on 16/11/18.
 */

public class ShareAndroid implements Share {
    private AndroidApplication androidApplication;
    private Intent shareIntent;

    public ShareAndroid() {
        shareIntent = new Intent(Intent.ACTION_SEND);
    }

    @Override
    public void share(String text) {
        shareIntent.setType("text/plain");
        String out = text.isEmpty() ? "No results until now..." : text;
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, check my achievements in Free Buran-19!\n" + out + "\n" + Rules.ABOUT_US);
        androidApplication.startActivity(Intent.createChooser(shareIntent, "Share results"));
    }

    public void setAndroidApplication(AndroidApplication androidApplication) {
        this.androidApplication = androidApplication;
    }
}