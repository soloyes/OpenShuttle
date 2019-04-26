package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.MoveProcessor;
import com.shuttle.game.effects.Scan;
import com.shuttle.game.effects.Shield;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.labelsticker.Label;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Consumer;

/**
 * @author Shuttle on 10/11/18.
 */
public class Crate extends AbstractConsumable {
    public Crate() {
        this.width = PaintConstants.CRATE_1_WIDTH;
        this.height = PaintConstants.CRATE_1_HEIGHT;
        this.type = Type.CRATE;
        this.moveProcessor = new MoveProcessor();
        this.shield = new Shield(this);
        this.scan = new Scan(this);
    }

    @Override
    public void badEffect(GameScreen gs) {
        GameInfoSystem.setup(
                position.x,
                position.y,
                GameInfoSystem.Preset.EASY,
                3.0f,
                false
        );
        boomBox.playSound(MSConstants.DESTROY_CRATE);
        Vibrator.vibrate(VConstants.PLAYER_CRATE_DESTROY);
    }

    @Override
    public void consumed(GameScreen gs) {
        if (danger) {
            switch (labels.get(Label.Layer.LABEL).get(0).getSticker()) {
                case PATTERN:
                case PATTERN_GOLD:
                    Consumer.consume(gs, labels.get(Label.Layer.HIDDEN).get(0).getSticker(), this);
                    break;
                case SHABBY:
                case SHABBY_GOLD:
                    Consumer.consume(gs, labels.get(Label.Layer.HIDDEN).get(0).getSticker(), this);
                    break;
            }
        } else {
            Consumer.consume(gs, labels.get(Label.Layer.LABEL).get(0).getSticker(), this);
        }
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
        if (fake) {
            golden = false;
        }
        if (golden) {
            this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.CRATE_GOLD_REGION);
        } else {
            this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.CRATE_1_REGION);
        }
    }

    private void stickLabel() {
        boolean r = MathUtils.random(0, Rules.CONSUMABLE_CRATE_DANGER_PROBABILITY) == 0;
        if (r) {
            labels.get(Label.Layer.LABEL).get(0).setSticker(generateDanger());
        } else {
            labels.get(Label.Layer.LABEL).get(0).setSticker(generateSafe());
        }
    }

    private Sticker generateDanger() {
        Sticker sticker;
        int r = MathUtils.random(0, 5);
        switch (r) {
            case 0:
                sticker = golden ? Sticker.SHABBY_GOLD : Sticker.SHABBY;
                break;
            default:
                sticker = golden ? Sticker.PATTERN_GOLD : Sticker.PATTERN;
                break;
        }
        danger = true;
        labels.get(Label.Layer.HIDDEN).get(0).setSticker(generateSafe());
        return sticker;
    }

    private Sticker generateSafe() {
        int r = MathUtils.random(0, 6);
        switch (r) {
            case 0:
                return Sticker.BOOST;
            case 1:
                return Sticker.REPAIR;
            case 2:
                return Sticker.TRASH;
            case 3:
                return Sticker.SPLASH;
            case 5:
                return Sticker.SCAN;
            default:
                return Sticker.SHIELD;
        }
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
    public void render(SpriteBatch batch) {
        basicRender(batch);
        if (labeled) {
            labels.get(Label.Layer.BACK).get(0).render(batch);
            if (danger) {
                if (labels.get(Label.Layer.LABEL).get(0).getSticker() == Sticker.SHABBY || labels.get(Label.Layer.LABEL).get(0).getSticker() == Sticker.SHABBY_GOLD) {
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
        if (scanned && !taken && labels.get(Label.Layer.LABEL).get(0).getSticker() != Sticker.SHABBY && labels.get(Label.Layer.LABEL).get(0).getSticker() != Sticker.SHABBY_GOLD) {
            scan.render(batch);
        }
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        showSprayCollision();
    }
}