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
        this.labels[0].setScaleFactor(2.0f);
        this.labels[1].setScaleFactor(2.0f);
        this.labels[2].setScaleFactor(2.0f);
        stickLabel();
        if (labels[1].getSticker() == Label.Sticker.PATTERN) {
            labels[2].setSticker(generateSafe());
        }
        this.shielded = false;
        this.shield.setTime(Rules.CONSUMABLE_SHIELD_TIME);
        this.scanned = false;
        this.scan.setTime(Rules.CONSUMABLE_SCAN_TIME);
    }

    private void stickLabel() {
        int r = MathUtils.random(0, 20);
        switch (r) {
            case 0:
                labels[1].setSticker(generateDanger());
                break;
            default:
                labels[1].setSticker(generateSafe());
        }
    }

    private Label.Sticker generateSafe() {
        int r = MathUtils.random(0, 3);
        //todo: переделать логику. щас криво. 1) Радар не должен появлять, если он уже взят. Две коробки с радаром не должны быть. Аналогично про лазер.
        // тут надо еще попробоватЬ, если звать gs.getPlayer.getRadar.isActive, то нарушается последовательность конструирования объектов и вылетает NPE. Надо как-то переделать. А вообще это днище когда все
        // знают про gs, но уже ничо не поделать. Это не правильно, но с этим будем жить.
        switch (r) {
            case 0:
                return Label.Sticker.LASER;
            case 1:
                return Label.Sticker.RADAR;
            case 2:
                return Label.Sticker.ROCKET;
            default:
                return Label.Sticker.PATTERN;
        }
    }

    private Label.Sticker generateDanger() {
        int r = MathUtils.random(0, 5);
        switch (r) {
            case 1:
                return Label.Sticker.PATTERN;
            default:
                return Label.Sticker.SHABBY;
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
        scan.update(dt);
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        showSprayCollision(gs.getParticleEmitter());
    }
}
