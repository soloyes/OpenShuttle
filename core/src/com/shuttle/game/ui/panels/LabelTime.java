package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;

import lombok.Getter;
import lombok.Setter;

public class LabelTime {
    @Getter
    @Setter
    private float x, y;
    @Getter
    @Setter
    private float alpha, fleshAlpha;
    @Getter
    @Setter
    private float time;

    public void changeTime(float time) {
        this.time += time;
    }

    public void changeY(float y) {
        this.y += y;
    }

    public void changeAlpha(float alpha) {
        this.alpha += alpha;
    }

    @Getter
    @Setter
    private float scaleX, scaleY;
    @Getter
    private TextureRegion actTexture;

    public LabelTime(TextureRegion textureRegion) {
        this();
        this.actTexture = textureRegion;
    }

    public void init(TextureRegion textureRegion) {
        this.init();
        this.actTexture = textureRegion;
    }

    public LabelTime() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.scaleX = this.scaleY = 1.0f;
        this.alpha = 1.0f;
        this.fleshAlpha = 0.0f;
        this.time = PaintConstants.PANEL_ELEMENT_LIFETIME;
    }

    public void init() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.scaleX = this.scaleY = 1.0f;
        this.alpha = 1.0f;
        this.fleshAlpha = 0.0f;
        this.time = PaintConstants.PANEL_ELEMENT_LIFETIME;
    }
}
