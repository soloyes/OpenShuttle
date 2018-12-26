package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
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
        this.scan = new Scan(this);
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

        this.labels.get(Label.Layer.BACK).get(0).setConsumable(this);
        this.labels.get(Label.Layer.BACK).get(0).setScaleFactor(2.5f);
        this.labels.get(Label.Layer.BACK).get(0).setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, PaintConstants.LABEL_ALPHA);

        this.labels.get(Label.Layer.LABEL).get(0).setConsumable(this);
        this.labels.get(Label.Layer.LABEL).get(0).setScaleFactor(2.5f);
        this.labels.get(Label.Layer.LABEL).get(0).setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.LABEL_ALPHA);

        this.labels.get(Label.Layer.HIDDEN).get(0).setConsumable(this);
        this.labels.get(Label.Layer.HIDDEN).get(0).setScaleFactor(2.5f);
        this.labels.get(Label.Layer.HIDDEN).get(0).setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.LABEL_ALPHA);

        stickLabel();

        this.shielded = false;
        this.shield.setTime(Rules.CONSUMABLE_SHIELD_TIME);
        this.scanned = false;
        this.scan.setTime(Rules.CONSUMABLE_SCAN_TIME);
    }

    private void stickLabel() {
        int r = MathUtils.random(0, 5);
        switch (r) {
            case 0:
                labels.get(Label.Layer.LABEL).get(0).setSticker(generateDanger());
                break;
            default:
                labels.get(Label.Layer.LABEL).get(0).setSticker(generateSafe());
        }
        if (labels.get(Label.Layer.LABEL).get(0).getSticker() == Label.Sticker.PATTERN) {
            labels.get(Label.Layer.HIDDEN).get(0).setSticker(generateSafe());
        }
    }

    private Label.Sticker generateSafe() {
        int r = MathUtils.random(0, 3);
        boolean radar = false;
        boolean laser = false;
        for (AbstractConsumable consumable : gs.getStorageEmitter().getActiveList()) {
            if (consumable.getLabels().get(Label.Layer.LABEL).get(0).getSticker() == Label.Sticker.LASER) {
                laser = true;
                break;
            }
        }

        for (AbstractConsumable consumable : gs.getStorageEmitter().getActiveList()) {
            if (consumable.getLabels().get(Label.Layer.LABEL).get(0).getSticker() == Label.Sticker.RADAR) {
                radar = true;
                break;
            }
        }

        if (r == 0 && (gs.getPlayer().getRadar().isActive() || radar)) {
            r += MathUtils.random(1, 3);
        }
        if (r == 1 && (gs.getPlayer().isLaser() || laser)) {
            r += MathUtils.random(1, 2);
        }

        switch (r) {
            case 0:
                return Label.Sticker.RADAR;
            case 1:
                return Label.Sticker.LASER;
            default:
                return Label.Sticker.ROCKET;
            //todo:// add many items insight Storage
        }
    }

    private Label.Sticker generateDanger() {
        int r = MathUtils.random(0, 5);
        switch (r) {
            case 1:
                return Label.Sticker.SHABBY;
            default:
                return Label.Sticker.PATTERN;
        }
    }

    @Override
    public void consumed(GameScreen gs) {
        if (labels.get(Label.Layer.LABEL).get(0).getSticker() == Label.Sticker.RADAR) {
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
        }

        if (labels.get(Label.Layer.LABEL).get(0).getSticker() == Label.Sticker.LASER) {
            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            GameInfoSystem.GameInfoSystemPreset.LASER,
                            3.0f,
                            true
                    );
            gs.getPlayer().setLaser(true);
            gs.getPlayer().setLaserChargeLevel(6);
            boomBox.playSound(MSConstants.GET_LASER);
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
        for (int i = 0; i < labels.get(Label.Layer.BACK).size(); i++) {
            labels.get(Label.Layer.BACK).get(i).update(dt);
        }
        for (int i = 0; i < labels.get(Label.Layer.LABEL).size(); i++) {
            labels.get(Label.Layer.LABEL).get(i).update(dt);
        }
        for (int i = 0; i < labels.get(Label.Layer.HIDDEN).size(); i++) {
            labels.get(Label.Layer.HIDDEN).get(i).update(dt);
        }
        shield.update(dt);
        scan.update(dt);
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        showSprayCollision(gs.getParticleEmitter());
    }

    private enum Size {
        SINGLE, DOUBLE, TRIPLE
    }
}
