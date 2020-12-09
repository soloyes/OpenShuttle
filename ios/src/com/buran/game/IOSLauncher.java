package com.buran.game;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

public class IOSLauncher extends IOSApplication.Delegate implements AdsController {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new BuranGame(this, new ShareIos(), true, 1080, 1920), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public void loadBannerAd() {

    }

    @Override
    public void showBannerAd(boolean isTop) {

    }

    @Override
    public void hideBannerAd() {

    }
}