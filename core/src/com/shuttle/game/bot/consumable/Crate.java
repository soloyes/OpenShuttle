package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Consumer;
import com.shuttle.game.utils.effects.MoveProcessor;
import com.shuttle.game.utils.effects.Vibrator;

/**
 * @author Shuttle on 10/11/18.
 */
public class Crate extends AbstractConsumable {

    public Crate(GameScreen gs) {
        super(gs);
        this.width = PaintConstants.CRATE_1_WIDTH;
        this.height = PaintConstants.CRATE_1_HEIGHT;
        this.type = Type.CRATE;
        this.moveProcessor = new MoveProcessor(gs);
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
        this.danger = false;

        this.labels.get(Label.Layer.BACK).get(0).setConsumable(this);
        this.labels.get(Label.Layer.BACK).get(0).setScaleFactor(3.5f);
        this.labels.get(Label.Layer.BACK).get(0).setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, PaintConstants.LABEL_ALPHA);

        this.labels.get(Label.Layer.LABEL).get(0).setConsumable(this);
        this.labels.get(Label.Layer.LABEL).get(0).setScaleFactor(3.5f);
        this.labels.get(Label.Layer.LABEL).get(0).setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.LABEL_ALPHA);

        this.labels.get(Label.Layer.HIDDEN).get(0).setConsumable(this);
        this.labels.get(Label.Layer.HIDDEN).get(0).setScaleFactor(3.5f);
        this.labels.get(Label.Layer.HIDDEN).get(0).setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.LABEL_ALPHA);

        this.golden = MathUtils.random(0, Rules.CONSUMABLE_GOLDEN_PROBABILITY) == 0;
        stickLabel();

        this.shielded = false;
        this.shield.setTime(Rules.CONSUMABLE_SHIELD_TIME);
        this.scanned = false;
        this.scan.setTime(Rules.CONSUMABLE_SCAN_TIME);
        if (!golden) {
            this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.CRATE_1_REGION);
        } else {
            this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.CRATE_GOLD_REGION);
        }
    }

    private void stickLabel() {
        int r = MathUtils.random(0, 20);
        switch (r) {
            case 0:
                labels.get(Label.Layer.LABEL).get(0).setSticker(generateDanger());
                break;
            default:
                labels.get(Label.Layer.LABEL).get(0).setSticker(generateSafe());
                break;
        }
    }

    private Label.Sticker generateSafe() {
        int r = 2;MathUtils.random(0, 6);
        switch (r) {
            case 0:
                return Label.Sticker.BOOST;
            case 1:
                return Label.Sticker.REPAIR;
            case 2:
                return Label.Sticker.TRASH;
            case 3:
                return Label.Sticker.SPLASH;
            case 5:
                return Label.Sticker.SCAN;
            default:
                return Label.Sticker.SHIELD;
        }
    }

    private Label.Sticker generateDanger() {
        Label.Sticker sticker;
        int r = MathUtils.random(0, 5);
        switch (r) {
            case 0:
                sticker = golden ? Label.Sticker.SHABBY_GOLD : Label.Sticker.SHABBY;
                break;
            default:
                sticker = golden ? Label.Sticker.PATTERN_GOLD : Label.Sticker.PATTERN;
                break;
        }
        danger = true;
        labels.get(Label.Layer.HIDDEN).get(0).setSticker(generateSafe());
        return sticker;
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
    public void consumed(GameScreen gs) {
        if (danger) {
            switch (labels.get(Label.Layer.LABEL).get(0).getSticker()) {
                case PATTERN:
                case PATTERN_GOLD:
                    Consumer.consume(gs, this, labels.get(Label.Layer.HIDDEN).get(0));
                    break;
                case SHABBY:
                case SHABBY_GOLD:
                    Consumer.consume(gs, this, labels.get(Label.Layer.LABEL).get(0));
                    break;
            }
        } else {
            Consumer.consume(gs, this, labels.get(Label.Layer.LABEL).get(0));
        }
    }

    @Override
    public void badEffect(GameScreen gs) {
        gs.getGameInfoSystem().setup(
                position.x,
                position.y,
                GameInfoSystem.GameInfoSystemPreset.EASY,
                3.0f,
                false
        );
        boomBox.playSound(MSConstants.KILL_CRATE);
        Vibrator.vibrate(VConstants.PLAYER_CRATE_DESTROY);
    }

    @Override
    public void render(SpriteBatch batch) {
        basicRender(batch);
        if (labeled) {
            labels.get(Label.Layer.BACK).get(0).render(batch);
            if (danger) {
                if (labels.get(Label.Layer.LABEL).get(0).getSticker() == Label.Sticker.SHABBY || labels.get(Label.Layer.LABEL).get(0).getSticker() == Label.Sticker.SHABBY_GOLD) {
                    labels.get(Label.Layer.LABEL).get(0).render(batch);
                } else {
                    if (!scanned) {
                        labels.get(Label.Layer.LABEL).get(0).render(batch);

                    } else {
                        if (taken) {
                            labels.get(Label.Layer.LABEL).get(0).render(batch);
                        } else {
                            labels.get(Label.Layer.HIDDEN).get(0).render(batch);
                        }
                    }
                }
            } else {
                labels.get(Label.Layer.LABEL).get(0).render(batch);
            }
        }

        if (shielded && !taken) {
            shield.render(batch);
        }
        if (scanned && !taken && labels.get(Label.Layer.LABEL).get(0).getSticker() != Label.Sticker.SHABBY && labels.get(Label.Layer.LABEL).get(0).getSticker() != Label.Sticker.SHABBY_GOLD) {
            scan.render(batch);
        }
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        showSprayCollision(gs.getParticleEmitter());
    }
}