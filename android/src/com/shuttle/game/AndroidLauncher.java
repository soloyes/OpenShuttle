package com.shuttle.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        ShareAndroid shareAndroid = new ShareAndroid();
        shareAndroid.setAndroidApplication(this);
        initialize(new ShuttleGame(shareAndroid), config);
    }
}