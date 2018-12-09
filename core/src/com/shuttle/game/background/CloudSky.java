package com.shuttle.game.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.effects.Oscillator;

import lombok.Getter;

/**
 * @author Shuttle on 12/8/18.
 * bc - big cloud
 * sc - small cloud
 * mc - middle cloud
 */
public class CloudSky {
    private int BCw, BCh, SCw, SCh;
    private TextureRegion bc;
    private TextureRegion sc;
    @Getter
    private float widthBC, heightBC, widthSC, heightSC;
    private float scaleBCx, scaleBCy, scaleSCx, scaleSCy;
    private float BCdx, SCdx;
    private Oscillator oscillatorBC;
    private Oscillator oscillatorSC;
    private Oscillator oscillatorA;
    private float a;

    public CloudSky() {
        this.bc = Assets.getInstance().getAtlas().findRegion(PaintConstants.CLOUD512_REGION);
        this.sc = Assets.getInstance().getAtlas().findRegion(PaintConstants.CLOUD512_REGION);
        this.scaleBCx = this.scaleBCy = 7.0f;
        this.scaleSCx = this.scaleSCy = 4.0f;
        this.widthBC = PaintConstants.CLOUD512_WIDTH * scaleBCx;
        this.heightBC = PaintConstants.CLOUD512_HEIGHT * scaleBCy;
        this.widthSC = PaintConstants.CLOUD512_WIDTH * scaleSCx;
        this.heightSC = PaintConstants.CLOUD512_HEIGHT * scaleSCy;
        this.BCw = (int) Math.ceil(Rules.SPACE_WIDTH / widthBC);
        this.BCh = (int) Math.ceil(Rules.SPACE_HEIGHT / heightBC);
        this.SCw = (int) Math.ceil(Rules.SPACE_WIDTH / widthSC);
        this.SCh = (int) Math.ceil(Rules.SPACE_HEIGHT / heightSC);
        this.oscillatorBC = new Oscillator();
        this.oscillatorSC = new Oscillator();
        this.oscillatorA = new Oscillator();
        this.a = 0.1f;
    }

    public void render(SpriteBatch batch) {
        Color color = batch.getColor();
        batch.setColor(1.0f, 0.0f, 1.0f, 0.1f);
        for (int i = -1; i < BCw + 1; i++) {
            for (int j = 0; j < BCh; j++) {
                batch.draw(bc, i * widthBC - BCdx, j * heightBC, widthBC, heightBC);
            }
        }

        batch.setColor(1.0f, 1.0f, 0.0f, a);
        for (int i = -1; i < SCw + 1; i++) {
            for (int j = 0; j < SCh; j++) {
                batch.draw(sc, i * widthSC - SCdx, j * heightSC, widthSC, heightSC);
            }
        }
        batch.setColor(color);
    }

    public void update(float dt) {
        BCdx = oscillatorBC.oscillate(BCdx, 20, 0, widthBC, dt);
        SCdx = oscillatorSC.oscillate(SCdx, 70, 0, widthSC, dt);
        a = oscillatorA.oscillate(a, 0.03f, 0.1f, 0.2f, dt);
    }
}
