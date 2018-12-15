package com.shuttle.game.bot.consumable;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.effects.MoveProcessor;
import com.shuttle.game.utils.effects.Vibrator;

public class Storage extends AbstractConsumable {
    public Storage(GameScreen gs) {
        super(gs);
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.STORAGE_1_REGION);
        this.width = PaintConstants.STORAGE_1_WIDTH;
        this.height = PaintConstants.STORAGE_1_HEIGHT;
        this.type = Type.STORAGE;
        this.moveProcessor = new MoveProcessor(gs);
        this.level = -3;
        this.shield = new Shield(this);
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.angle = MathUtils.random(0, 360);
        this.rotationSpeed = MathUtils.random(-20, 20);
        this.active = true;
        this.scaleX = this.saveScaleX = 1.0f;
        this.scaleY = this.saveScaleY = 1.0f;
        this.taken = false;
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.spiralRadius = MathUtils.random(20, 60);
        this.atRayPoint = false;
        this.labeled = true;
        this.labels[0].setScaleFactor(2.0f);
        this.labels[1].setScaleFactor(2.0f);
        this.labels[2].setScaleFactor(0.5f);
        stickLabel();
        this.shielded = true;
    }

    private void stickLabel() {
        int r = MathUtils.random(0, 20);
        switch (r) {
            case 0:
                generateDanger();
                break;
            default:
                generateSafe();
        }
    }

    private void generateSafe() {
        int r = MathUtils.random(0, 3);
        if (gs.getPlayer().getRadar().isActive()) {
            r = 4;
        }
        switch (r) {
            case 0:
                labels[1].setSticker(Label.Sticker.LASER);
                break;
            case 1:
                labels[1].setSticker(Label.Sticker.RADAR);
                break;
            case 2:
                labels[1].setSticker(Label.Sticker.ROCKET);
                break;
            default:
                labels[1].setSticker(Label.Sticker.PATTERN);
        }
    }

    private void generateDanger() {
        int r = MathUtils.random(0, 5);
        switch (r) {
            case 1:
                labels[1].setSticker(Label.Sticker.PATTERN);
                break;
            default:
                labels[1].setSticker(Label.Sticker.SHABBY);
        }
    }

    @Override
    public void consumed(GameScreen gs) {
        if (labels[1].getSticker() == Label.Sticker.RADAR) {
            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            GameInfoSystem.GameInfoSystemPreset.RADAR,
                            3.0f,
                            true
                    );
            boomBox.playSound(MSConstants.GET_RADAR);
            gs.getPlayer().getRadar().setActive(true);
        } else {
            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            GameInfoSystem.GameInfoSystemPreset.STORAGE,
                            3.0f,
                            true
                    );
            boomBox.playSound(MSConstants.GET_STORAGE);
        }
    }

    @Override
    public void badEffect(GameScreen gs) {
        gs.getGameInfoSystem().setup(
                position.x,
                position.y,
                GameInfoSystem.GameInfoSystemPreset.BOOM,
                3.0f,
                false
        );
        boomBox.playSound(MSConstants.STORAGE_DESTROY);
        Vibrator.vibrate(VConstants.PLAYER_STORAGE_DESTROY);
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        moveProcessor.update(this, dt);
        boundCheck.checkAndHandleBoundsAndDestroy(this);
        for (int i = 0; i < labels.length; i++) {
            labels[i].update(dt);
        }
        shield.update(dt);
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        showSprayCollision(gs.getParticleEmitter());
    }
}
