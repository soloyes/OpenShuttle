package com.shuttle.game.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.effects.Oscillator;

import lombok.Getter;

/**
 * @author Shuttle on 12/8/18.
 * mc - big cloud
 * sc - small cloud
 * mc - middle cloud
 */
public class CloudSky {
    private int BCw, BCh, MCw, MCh, SCw, SCh;
    private TextureRegion bc;
    private TextureRegion mc;
    private TextureRegion sc;
    @Getter
    private float widthBC, heightBC, widthMC, heightMC, widthSC, heightSC;
    private float scaleBCx, scaleBCy, scaleMCx, scaleMCy, scaleSCx, scaleSCy;
    @Getter
    private float BCdx, MCdx, SCdx;
    private float BCdy, MCdy, SCdy;
    @Getter
    private Oscillator oscillatorBC;
    private Oscillator oscillatorMC;
    private Oscillator oscillatorSC;
    private Oscillator oscillatorA;
    private Vector2 normal;
    private float a;
    @Getter
    private float frequency1;
    private float frequency2;
    private float frequency3;
    private ScreenManager.ScreenType screenType;
    private Shuttle player;

    public void setPlayer(Shuttle player) {
        this.player = player;
    }

    public CloudSky(ScreenManager.ScreenType screenType) {
        this.screenType = screenType;
        this.bc = Assets.getInstance().getAtlas().findRegion(PaintConstants.CLOUD512_REGION);
        this.mc = Assets.getInstance().getAtlas().findRegion(PaintConstants.CLOUD512_REGION);
        this.sc = Assets.getInstance().getAtlas().findRegion(PaintConstants.CLOUD512_REGION);
        this.scaleBCx = this.scaleBCy = 12.0f;
        this.scaleMCx = this.scaleMCy = 8.0f;
        this.scaleSCx = this.scaleSCy = 4.0f;
        this.widthBC = PaintConstants.CLOUD512_WIDTH * scaleBCx;
        this.heightBC = PaintConstants.CLOUD512_HEIGHT * scaleBCy;
        this.widthMC = PaintConstants.CLOUD512_WIDTH * scaleMCx;
        this.heightMC = PaintConstants.CLOUD512_HEIGHT * scaleMCy;
        this.widthSC = PaintConstants.CLOUD512_WIDTH * scaleSCx;
        this.heightSC = PaintConstants.CLOUD512_HEIGHT * scaleSCy;
        this.BCw = (int) Math.ceil(Rules.SPACE_WIDTH / widthBC);
        this.BCh = (int) Math.ceil(Rules.SPACE_HEIGHT / heightBC);
        this.MCw = (int) Math.ceil(Rules.SPACE_WIDTH / widthMC);
        this.MCh = (int) Math.ceil(Rules.SPACE_HEIGHT / heightMC);
        this.SCw = (int) Math.ceil(Rules.SPACE_WIDTH / widthSC);
        this.SCh = (int) Math.ceil(Rules.SPACE_HEIGHT / heightSC);
        this.oscillatorBC = new Oscillator();
        this.oscillatorMC = new Oscillator();
        this.oscillatorSC = new Oscillator();
        this.oscillatorA = new Oscillator();
        this.a = 0.3f;
        this.normal = new Vector2();
    }

    public void render(SpriteBatch batch) {
        Color color = batch.getColor();
        batch.setColor(Color.valueOf("0024ffe0"));
        for (int i = -1; i < BCw + 1; i++) {
            for (int j = -1; j < BCh + 1; j++) {
                batch.draw(bc, i * widthBC - BCdx % widthBC, j * heightBC - BCdy % heightBC, widthBC, heightBC);
            }
        }

        batch.setColor(Color.valueOf("9e00ff70"));
        for (int i = -1; i < MCw + 1; i++) {
            for (int j = -1; j < MCh + 1; j++) {
                batch.draw(mc, i * widthMC - MCdx % widthMC, j * heightMC - MCdy % heightMC, widthMC, heightMC);
            }
        }

        batch.setColor(1.0f, 1.0f, 1.0f, a);
        for (int i = -1; i < SCw + 1; i++) {
            for (int j = -1; j < SCh + 1; j++) {
                batch.draw(sc, i * widthSC - SCdx % widthSC, j * heightSC - SCdy % heightSC, widthSC, heightSC);
            }
        }
        batch.setColor(color);
    }

    public void update(float dt) {
        if (screenType == ScreenManager.ScreenType.GAME) {
            BCdx += player.getVelocity().x / 1200;
            BCdy += player.getVelocity().y / 1200;
            if (normal.set(player.getVelocity()).len() < 150){
                normal.set(player.getVelocity()).nor();
                BCdx += 0.1 * normal.x;
                BCdy += 0.1 * normal.y;
            }
        } else {

            if (BCdx > widthBC / 8) {
                MCdx += frequency2 * dt - 2f * dt* dt / 2;
                SCdx += frequency3 * dt - 3f * dt* dt / 2;
            } else {
                MCdx += frequency2 * dt + 2f * dt* dt / 2;
                SCdx += frequency3 * dt + 3f * dt* dt / 2;
            }

            BCdx = oscillatorBC.prettyOscillate(BCdx, frequency1, 1f, 0, widthBC/4, dt);

            if (BCdx > widthBC / 8) {
                frequency1 -= 1 * dt;
                frequency2 -= 2 * dt;
                frequency3 -= 3 * dt;
            } else {
                frequency1 += 1 * dt;
                frequency2 += 2 * dt;
                frequency3 += 3 * dt;
            }
        }

        if (screenType == ScreenManager.ScreenType.GAME) {
            MCdx += player.getVelocity().x / 900;
            MCdy += player.getVelocity().y / 900;
            if (normal.set(player.getVelocity()).len() < 150){
                normal.set(player.getVelocity()).nor();
                MCdx += 0.2 * normal.x;
                MCdy += 0.2 * normal.y;
            }
        }

        if (screenType == ScreenManager.ScreenType.GAME) {
            SCdx += player.getVelocity().x / 600;
            SCdy += player.getVelocity().y / 600;
            if (normal.set(player.getVelocity()).len() < 150){
                normal.set(player.getVelocity()).nor();
                SCdx += 0.3 * normal.x;
                SCdy += 0.3 * normal.y;
            }
        }
        a = oscillatorA.oscillate(a, 0.03f, 0.3f, 0.4f, dt);
    }
}
