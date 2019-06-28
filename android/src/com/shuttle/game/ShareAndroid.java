package com.shuttle.game;

import android.content.Intent;

import com.badlogic.gdx.backends.android.AndroidApplication;

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
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, check my achievements in Shuttle!\n" + out);
        androidApplication.startActivity(Intent.createChooser(shareIntent, "Share results"));
    }

    public void setAndroidApplication(AndroidApplication androidApplication) {
        this.androidApplication = androidApplication;
    }
}
