package com.buran.game;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.buran.game.conf.PaintConstants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Collections;

/**
 * @author soloyes on 03/06/18.
 */

public class AndroidLauncher extends AndroidApplication implements AdsController {

    private static final String BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
    private AdView bannerAd;
    private RelativeLayout.LayoutParams params;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAds();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        ShareAndroid shareAndroid = new ShareAndroid();
        shareAndroid.setAndroidApplication(this);
        BuranGame buranGame = new BuranGame(this, shareAndroid, true, 1080, 1920);
        initialize(buranGame, config);
        onWindowFocusChanged(true);

        View gameView = initializeForView(buranGame, config);

        // Define the layout
        layout = new RelativeLayout(this);
        layout.addView(gameView);
        params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        setContentView(layout);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @TargetApi(19)
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        //     | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        //     | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            hideSystemUI();
                        } else {
                        }
                    }
                });
    }

    @Override
    public void loadBannerAd() {
        AdRequest.Builder builder = new AdRequest.Builder();
        AdRequest ad = builder.build();
        bannerAd.loadAd(ad);
    }

    @Override
    @TargetApi(19)
    public void showBannerAd(final boolean isTop) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.removeViewInLayout(bannerAd);
                if (isTop) {
                    params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                } else {
                    params.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                }
                layout.addView(bannerAd, params);
                loadBannerAd();
                bannerAd.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideBannerAd() {
        if (bannerAd.getVisibility() == View.VISIBLE) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bannerAd.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void setupAds() {
        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Collections.singletonList("E5DF21F0D60E82C4991F2034B48C44BE")).build());
        MobileAds.initialize(this);

        bannerAd = new AdView(this);
        bannerAd.setVisibility(View.INVISIBLE);
        bannerAd.setBackgroundColor(PaintConstants.BACKGROUND_COLOR_HEX_INT);
        bannerAd.setAdUnitId(BANNER_AD_UNIT_ID);
        bannerAd.setAdSize(AdSize.SMART_BANNER);
    }
}