package com.buran.game.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.effects.Oscillator;
import com.buran.game.player.Buran;
import com.buran.game.screens.ScreenManager;
import com.buran.game.utils.Assets;

import lombok.Getter;

/**
 * @author soloyes on 12/8/18.
 * bc - big cloud
 * mc - middle cloud
 * sc - small cloud
 */

public class CloudSky {
    private int bcW, bcH, mcW, mcH, scW, scH;
    private TextureRegion bc, mc, sc;
    @Getter
    private float bcWidth, bcHeight, mcWidth, mcHeight, scWidth, scHeight;
    private float bcScaleX, bcScaleY, mcScaleX, mcScaleY, scScaleX, scScaleY;
    public static float bcDx, mcDx, scDx;
    public static float bcDy, mcDy, scDy;
    @Getter
    private Oscillator bcOscillator;
    private Oscillator aOscillator;
    private Vector2 normal;
    private float a;
    @Getter
    private float bcFrequency;
    private float mcFrequency;
    private float scFrequency;
    private Color bcColor, mcColor, scColor;

    public CloudSky() {
        this.bc = Assets.getInstance().getAtlas().findRegion(PaintConstants.CLOUD1024_REGION);
        this.mc = Assets.getInstance().getAtlas().findRegion(PaintConstants.CLOUD1024_REGION);
        this.sc = Assets.getInstance().getAtlas().findRegion(PaintConstants.CLOUD1024_REGION);
        this.bcScaleX = this.bcScaleY = 12.0f;
        this.mcScaleX = this.mcScaleY = 8.0f;
        this.scScaleX = this.scScaleY = 4.0f;
        this.bcWidth = PaintConstants.CLOUD1024_WIDTH * bcScaleX;
        this.bcHeight = PaintConstants.CLOUD1024_HEIGHT * bcScaleY;
        this.mcWidth = PaintConstants.CLOUD1024_WIDTH * mcScaleX;
        this.mcHeight = PaintConstants.CLOUD1024_HEIGHT * mcScaleY;
        this.scWidth = PaintConstants.CLOUD1024_WIDTH * scScaleX;
        this.scHeight = PaintConstants.CLOUD1024_HEIGHT * scScaleY;
        this.bcW = (int) Math.ceil(Rules.SPACE_WIDTH / bcWidth);
        this.bcH = (int) Math.ceil(Rules.SPACE_HEIGHT / bcHeight);
        this.mcW = (int) Math.ceil(Rules.SPACE_WIDTH / mcWidth);
        this.mcH = (int) Math.ceil(Rules.SPACE_HEIGHT / mcHeight);
        this.scW = (int) Math.ceil(Rules.SPACE_WIDTH / scWidth);
        this.scH = (int) Math.ceil(Rules.SPACE_HEIGHT / scHeight);
        this.bcOscillator = new Oscillator();
        this.aOscillator = new Oscillator();
        this.a = 0.3f;
        this.normal = new Vector2();
        this.bcColor = Color.valueOf("0024ffe0");
        this.mcColor = Color.valueOf("9e00ff70");
        this.scColor = Color.valueOf("ffffffff");
    }

    public void render(SpriteBatch batch) {
        Color color = batch.getColor();
        batch.setColor(bcColor);
        for (int i = -1 - bcW; i < bcW + 1; i++) {
            for (int j = -1 -bcH; j < bcH + 1; j++) {
                batch.draw(bc, i * bcWidth - bcDx % bcWidth + (Rules.SPACE_WIDTH / 2.0f - bcWidth / 2.0f), j * bcHeight - bcDy % bcHeight, bcWidth, bcHeight);
            }
        }

        batch.setColor(mcColor);
        for (int i = -1 - mcW; i < mcW + 1; i++) {
            for (int j = -1 - mcH; j < mcH + 1; j++) {
                batch.draw(mc, i * mcWidth - mcDx % mcWidth + (Rules.SPACE_WIDTH / 2.0f - mcWidth / 2.0f), j * mcHeight - mcDy % mcHeight, mcWidth, mcHeight);
            }
        }

        scColor.a = a;
        batch.setColor(scColor);
        for (int i = -1 - scW; i < scW + 1; i++) {
            for (int j = -1 - scH; j < scH + 1; j++) {
                batch.draw(sc, i * scWidth - scDx % scWidth + (Rules.SPACE_WIDTH / 2.0f - scWidth / 2.0f), j * scHeight - scDy % scHeight, scWidth, scHeight);
            }
        }
        batch.setColor(color);
    }

    public void update(float dt) {
        if (ScreenManager.getInstance().getCurrentScreen() == ScreenManager.ScreenType.GAME) {
            bcDx += Buran.velocity.x / 1200;
            bcDy += Buran.velocity.y / 1200;
            if (normal.set(Buran.velocity).len() < 150) {
                normal.set(Buran.velocity).nor();
                bcDx += 0.1 * normal.x;
                bcDy += 0.1 * normal.y;
            }
            if (bcDx > (bcW + 1) * bcWidth ) {
                bcDx = bcDx % ( (bcW + 1) * bcWidth );
            }
            if (bcDx < - bcWidth ) {
                bcDx = ( (bcW + 1) * bcWidth ) - bcDx % bcWidth;
            }
            if (bcDy > (bcH + 1) * bcHeight) {
                bcDy = bcDy % ( (bcH + 1) * bcHeight );
            }
            if (bcDy < - bcHeight ) {
                bcDy =  ((bcH + 1) * bcHeight) - bcDy % bcHeight ;
            }
        } else {
            if (dt > 1) dt = 1;
            if (bcDx > bcWidth / 8) {
                mcDx += mcFrequency * dt - 2f * dt * dt / 2;
                scDx += scFrequency * dt - 3f * dt * dt / 2;
            } else {
                mcDx += mcFrequency * dt + 2f * dt * dt / 2;
                scDx += scFrequency * dt + 3f * dt * dt / 2;
            }

            bcDx = bcOscillator.prettyOscillate(bcDx, bcFrequency, 1f, 0, bcWidth / 4, dt);

            if (bcDx > PaintConstants.BACKGROUND_OSCILLATE_WIDTH) {
                bcFrequency -= 1 * dt;
                mcFrequency -= 2 * dt;
                scFrequency -= 3 * dt;
            } else {
                bcFrequency += 1 * dt;
                mcFrequency += 2 * dt;
                scFrequency += 3 * dt;
            }
        }

        if (ScreenManager.getInstance().getCurrentScreen() == ScreenManager.ScreenType.GAME) {
            mcDx += Buran.velocity.x / 900;
            mcDy += Buran.velocity.y / 900;
            if (normal.set(Buran.velocity).len() < 150) {
                normal.set(Buran.velocity).nor();
                mcDx += 0.2 * normal.x;
                mcDy += 0.2 * normal.y;
            }
            if (mcDx > (mcW + 1) * mcWidth ) {
                mcDx = mcDx % ( (mcW + 1) * mcWidth );
            }
            if (mcDx < - mcWidth ) {
                mcDx = ( (mcW + 1) * mcWidth ) - mcDx % mcWidth;
            }
            if (mcDy > (mcH + 1) * mcHeight) {
                mcDy = mcDy % ( (mcH + 1) * mcHeight );
            }
            if (mcDy < - mcHeight ) {
                mcDy =  ((mcH + 1) * mcHeight) - mcDy % mcHeight ;
            }
        }

        if (ScreenManager.getInstance().getCurrentScreen() == ScreenManager.ScreenType.GAME) {
            scDx += Buran.velocity.x / 600;
            scDy += Buran.velocity.y / 600;
            if (normal.set(Buran.velocity).len() < 150) {
                normal.set(Buran.velocity).nor();
                scDx += 0.3 * normal.x;
                scDy += 0.3 * normal.y;
            }
            if (scDx > (scW + 1) * scWidth ) {
                scDx = scDx % ( (scW + 1) * scWidth );
            }
            if (scDx < - scWidth ) {
                scDx = ( (scW + 1) * scWidth ) - scDx % scWidth;
            }
            if (scDy > (scH + 1) * scHeight) {
                scDy = scDy % ( (scH + 1) * scHeight );
            }
            if (scDy < - scHeight ) {
                scDy =  ((scH + 1) * scHeight) - scDy % scHeight ;
            }
        }
        a = aOscillator.oscillate(a, 0.03f, 0.2f, 0.4f, dt);

        if (Rules.DEATH_MODE) {
            changeClouds();
        }
    }

    private void changeClouds() {
        this.bcColor = Color.valueOf("0024ffe0");
        this.mcColor = Color.valueOf("9e000f70");
        this.scColor = Color.valueOf("ffffffff");
    }
}