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

public class Storage extends AbstractConsumable {
    private Size size;
    private Label danderLabel;

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
        while (this.labels.get(Label.Layer.BACK).size() < Size.values().length) {
            this.labels.get(Label.Layer.BACK).add(new Label());
        }
        while (this.labels.get(Label.Layer.LABEL).size() < Size.values().length) {
            this.labels.get(Label.Layer.LABEL).add(new Label());
        }
        while (this.labels.get(Label.Layer.HIDDEN).size() < Size.values().length) {
            this.labels.get(Label.Layer.HIDDEN).add(new Label());
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

        generateSize();

        for (int i = 0; i < labels.get(Label.Layer.BACK).size(); i++) {
            this.labels.get(Label.Layer.BACK).get(i).setConsumable(this);
            this.labels.get(Label.Layer.BACK).get(i).setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, PaintConstants.LABEL_ALPHA);
        }
        for (int i = 0; i < labels.get(Label.Layer.LABEL).size(); i++) {
            this.labels.get(Label.Layer.LABEL).get(i).setConsumable(this);
            this.labels.get(Label.Layer.LABEL).get(i).setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.LABEL_ALPHA);
        }

        for (int i = 0; i < labels.get(Label.Layer.HIDDEN).size(); i++) {
            this.labels.get(Label.Layer.HIDDEN).get(i).setConsumable(this);
            this.labels.get(Label.Layer.HIDDEN).get(i).setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.LABEL_ALPHA);
        }

        stickLabel();

        this.shielded = false;
        this.shield.setTime(Rules.CONSUMABLE_SHIELD_TIME);
        this.scanned = false;
        this.scan.setTime(Rules.CONSUMABLE_SCAN_TIME);
    }

    private void generateSize() {
        int r = MathUtils.random(0, Size.values().length - 1);
        size = Size.values()[r];
    }

    private void stickLabel() {
        for (int i = 0; i <= size.ordinal(); i++) {
            int r = MathUtils.random(0, 10);
            switch (r) {
                case 0:
                    labels.get(Label.Layer.LABEL).get(i).setSticker(generateDanger());
                    break;
                default:
                    labels.get(Label.Layer.LABEL).get(i).setSticker(generateSafe());
            }
        }

        if (size.ordinal() == 1) {
            if (labels.get(Label.Layer.LABEL).get(0).getSticker() == labels.get(Label.Layer.LABEL).get(1).getSticker()) {
                int r = (labels.get(Label.Layer.LABEL).get(0).getSticker().ordinal() + 1) % 7;
                labels.get(Label.Layer.LABEL).get(1).setSticker(Label.Sticker.values()[r]);
            }
        }

        for (int i = 0; i < labels.get(Label.Layer.LABEL).size(); i++) {
            if (labels.get(Label.Layer.LABEL).get(i).getSticker() == Label.Sticker.PATTERN || labels.get(Label.Layer.LABEL).get(i).getSticker() == Label.Sticker.SHABBY) {
                danger = true;
                setLabelForSingle(labels.get(Label.Layer.LABEL).get(i));
                danderLabel = labels.get(Label.Layer.LABEL).get(i);
                for (int j = 0; j <= size.ordinal(); j++) {
                    labels.get(Label.Layer.HIDDEN).get(j).setSticker(generateSafe());
                }
                break;
            }
        }
    }

    private Label.Sticker generateSafe() {
        int r;
        switch (size) {
            case SINGLE:
                r = MathUtils.random(0, 3);
                boolean radar = false;
                boolean laser = false;
                for (AbstractConsumable consumable : gs.getStorageEmitter().getActiveList()) {
                    if (consumable.getLabels().get(Label.Layer.LABEL).get(0).getSticker() == Label.Sticker.LASER ||
                            consumable.getLabels().get(Label.Layer.HIDDEN).get(0).getSticker() == Label.Sticker.LASER) {
                        laser = true;
                        break;
                    }
                }

                for (AbstractConsumable consumable : gs.getStorageEmitter().getActiveList()) {
                    if (consumable.getLabels().get(Label.Layer.LABEL).get(0).getSticker() == Label.Sticker.RADAR ||
                            consumable.getLabels().get(Label.Layer.HIDDEN).get(0).getSticker() == Label.Sticker.RADAR) {
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
                }
            default:
                r = MathUtils.random(0, 6);
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
    }

    private Label.Sticker generateDanger() {
        int r = MathUtils.random(0, 5);
        switch (r) {
            case 0:
                return Label.Sticker.SHABBY;
            default:
                return Label.Sticker.PATTERN;
        }
    }

    @Override
    public void consumed(GameScreen gs) {
        if (danger) {
            switch (danderLabel.getSticker()) {
                case PATTERN:
                    for (int i = 0; i <= size.ordinal(); i++) {
                        Consumer.consume(gs, this, labels.get(Label.Layer.HIDDEN).get(i));
                    }
                    break;
                //todo:// SHABBY Logic here: 1/2 life, 1/2 fuel, new anomaly, disable radar/laser
                case SHABBY:
                    break;
            }
        } else {
            for (int i = 0; i <= size.ordinal(); i++) {
                Consumer.consume(gs, this, labels.get(Label.Layer.LABEL).get(i));
            }
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
    public void render(SpriteBatch batch) {
        basicRender(batch);
        if (labeled) {
            if (danger) {
                if (danderLabel.getSticker() == Label.Sticker.SHABBY) {
                    setLabelForSingle(labels.get(Label.Layer.BACK).get(0));
                    labels.get(Label.Layer.BACK).get(0).render(batch);
                    danderLabel.render(batch);
                } else {
                    if (!scanned) {
                        setLabelForSingle(labels.get(Label.Layer.BACK).get(0));
                        labels.get(Label.Layer.BACK).get(0).render(batch);
                        danderLabel.render(batch);
                    } else {
                        if (taken) {
                            setLabelForSingle(labels.get(Label.Layer.BACK).get(0));
                            labels.get(Label.Layer.BACK).get(0).render(batch);
                            danderLabel.render(batch);
                        } else {
                            switch (size) {
                                case SINGLE:
                                    setLabelForSingle(labels.get(Label.Layer.BACK).get(0));
                                    setLabelForSingle(labels.get(Label.Layer.HIDDEN).get(0));
                                    labels.get(Label.Layer.BACK).get(0).render(batch);
                                    labels.get(Label.Layer.HIDDEN).get(0).render(batch);
                                    break;
                                case DOUBLE:
                                    for (int i = 0; i <= size.ordinal(); i++) {
                                        setLabelForDouble(labels.get(Label.Layer.BACK).get(i), i);
                                        setLabelForDouble(labels.get(Label.Layer.HIDDEN).get(i), i);
                                        labels.get(Label.Layer.BACK).get(i).render(batch);
                                        labels.get(Label.Layer.HIDDEN).get(i).render(batch);
                                    }
                                    break;
                            }
                        }
                    }
                }
            } else {
                switch (size) {
                    case SINGLE:
                        setLabelForSingle(labels.get(Label.Layer.BACK).get(0));
                        setLabelForSingle(labels.get(Label.Layer.LABEL).get(0));
                        labels.get(Label.Layer.BACK).get(0).render(batch);
                        labels.get(Label.Layer.LABEL).get(0).render(batch);
                        break;
                    case DOUBLE:
                        for (int i = 0; i <= size.ordinal(); i++) {
                            setLabelForDouble(labels.get(Label.Layer.BACK).get(i), i);
                            setLabelForDouble(labels.get(Label.Layer.LABEL).get(i), i);
                            labels.get(Label.Layer.BACK).get(i).render(batch);
                            labels.get(Label.Layer.LABEL).get(i).render(batch);
                        }
                        break;
                }
            }
        }

        if (shielded && !taken) {
            shield.render(batch);
        }
        if (scanned && !taken && danderLabel.getSticker() != Label.Sticker.SHABBY) {
            scan.render(batch);
        }
    }

    private void setLabelForSingle(Label label) {
        label.setScaleFactor(PaintConstants.LABEL_SINGLE_SCALE_FACTOR);
        label.setD(PaintConstants.STORAGE_SINGLE_OFFSET);
    }

    private void setLabelForDouble(Label label, int i) {
        label.setScaleFactor(PaintConstants.LABEL_DOUBLE_SCALE_FACTOR);
        label.setD((float) Math.pow(-1, i) * PaintConstants.STORAGE_DOUBLE_OFFSET);
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        showSprayCollision(gs.getParticleEmitter());
    }

    private enum Size {
        SINGLE, DOUBLE
    }
}
