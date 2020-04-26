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
import com.shuttle.game.effects.particle.ParticleEmitter;
import com.shuttle.game.labelsticker.Label;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.navigation.Radar;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Consumer;
import com.shuttle.game.utils.pool.ObjectPool;

import java.util.List;

/**
 * @author Shuttle on 22/11/18.
 */

public class Storage extends AbstractConsumable {
    private Size size;
    private Label dangerLabel;

    public Storage(ParticleEmitter particleEmitter) {
        super(particleEmitter);
        this.particleEmitter = particleEmitter;
        this.width = PaintConstants.STORAGE_WIDTH;
        this.height = PaintConstants.STORAGE_HEIGHT;
        this.type = Type.STORAGE;
        this.moveProcessor = new MoveProcessor();
        this.level = -3;
        this.shield = new Shield(this);
        this.scan = new Scan(this);
        //todo: labels.get(Label.Layer.BACK).size() --> labels.getLayer.size() and so on, Do refactor. Everywhere
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
    public void badEffect() {
        GameInfoSystem.setup(
                position.x, position.y,
                GameInfoSystem.Preset.BOOM,
                3.0f,
                false
        );
        boomBox.playSound(MSConstants.DESTROY_STORAGE);
        Vibrator.vibrate(VConstants.PLAYER_STORAGE_DESTROY);
    }

    @Override
    public void consumed() {
        if (danger) {
            switch (dangerLabel.getSticker()) {
                case PATTERN:
                case PATTERN_GOLD:
                    for (int i = 0; i <= size.ordinal(); i++) {
                        Consumer.consume(labels.get(Label.Layer.HIDDEN).get(i).getSticker(), this);
                    }
                    break;
                case SHABBY:
                case SHABBY_GOLD:
                    Consumer.consume(dangerLabel.getSticker(), this);
                    break;
            }
        } else {
            for (int i = 0; i <= size.ordinal(); i++) {
                Consumer.consume(labels.get(Label.Layer.LABEL).get(i).getSticker(), this);
            }
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

        this.golden = MathUtils.random(0, Rules.CONSUMABLE_GOLDEN_PROBABILITY) == 0;

        stickLabel();

        if (size == Size.SINGLE) {
            Sticker stickerFront = labels.get(Label.Layer.LABEL).get(0).getSticker();
            Sticker stickerHidden = labels.get(Label.Layer.HIDDEN).get(0).getSticker();
            if (stickerFront == Sticker.LASER || stickerFront == Sticker.RADAR || stickerHidden == Sticker.LASER || stickerHidden == Sticker.RADAR) {
                golden = false;
            }
        }

        this.shielded = false;
        this.shield.setTime(Rules.CONSUMABLE_SHIELD_TIME);
        this.scanned = false;
        this.scan.setTime(Rules.CONSUMABLE_SCAN_TIME);
        if (fake) {
            golden = false;
        }
        if (golden) {
            this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.STORAGE_GOLD_REGION);
            if (danger) {
                if (dangerLabel.getSticker() == Sticker.PATTERN) {
                    dangerLabel.setSticker(Sticker.PATTERN_GOLD);
                }
                if (dangerLabel.getSticker() == Sticker.SHABBY) {
                    dangerLabel.setSticker(Sticker.SHABBY_GOLD);
                }
            }
        } else {
            this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.STORAGE_REGION);
        }
    }

    private void generateSize() {
        int r = MathUtils.random(0, Size.values().length - 1);
        size = Size.values()[r];
    }

    private void stickLabel() {
        for (int i = 0; i <= size.ordinal(); i++) {
            boolean r = MathUtils.random(0, Rules.CONSUMABLE_STORAGE_DANGER_PROBABILITY) == 0;
            if (r) {
                labels.get(Label.Layer.LABEL).get(i).setSticker(generateDanger());
                danger = true;
                setLabelForSingle(labels.get(Label.Layer.LABEL).get(i));
                dangerLabel = labels.get(Label.Layer.LABEL).get(i);
                for (int j = 0; j <= size.ordinal(); j++) {
                    labels.get(Label.Layer.HIDDEN).get(j).setSticker(generateSafe());
                }
                i = size.ordinal() + 1;
            } else {
                labels.get(Label.Layer.LABEL).get(i).setSticker(generateSafe());
            }
        }

        //Make different labels insight of storage. If add new Sticker, please add to the end of enumeration.
        if (size.ordinal() == 1) {
            if (labels.get(Label.Layer.LABEL).get(0).getSticker() == labels.get(Label.Layer.LABEL).get(1).getSticker()) {
                int r = (labels.get(Label.Layer.LABEL).get(0).getSticker().ordinal() + 1) % 6;
                labels.get(Label.Layer.LABEL).get(1).setSticker(Sticker.values()[r]);
            }
            if (danger) {
                if (labels.get(Label.Layer.HIDDEN).get(0).getSticker() == labels.get(Label.Layer.HIDDEN).get(1).getSticker()) {
                    int r = (labels.get(Label.Layer.HIDDEN).get(0).getSticker().ordinal() + 1) % 6;
                    labels.get(Label.Layer.HIDDEN).get(1).setSticker(Sticker.values()[r]);
                }
            }
        }
        //
    }

    private Sticker generateDanger() {
        return MathUtils.random(0, 5) == 0 ? Sticker.SHABBY : Sticker.PATTERN;
    }

    private void setLabelForSingle(Label label) {
        label.setScaleFactor(PaintConstants.LABEL_SINGLE_SCALE_FACTOR);
        label.setD(PaintConstants.STORAGE_SINGLE_OFFSET);
    }

    private Sticker generateSafe() {
        int r;
        switch (size) {
            case SINGLE:
                r = MathUtils.random(0, 3);
                boolean radar = false;
                boolean laser = false;
                StorageEmitter storageEmitter = (StorageEmitter) ObjectPool.EMITTERS.get(Storage.class);
                List<AbstractConsumable> list = storageEmitter.getActiveList();
                for (AbstractConsumable consumable : list) {
                    if (consumable.getLabels().get(Label.Layer.LABEL).get(0).getSticker() == Sticker.LASER ||
                            consumable.getLabels().get(Label.Layer.HIDDEN).get(0).getSticker() == Sticker.LASER) {
                        laser = true;
                        break;
                    }
                }

                for (AbstractConsumable consumable : list) {
                    if (consumable.getLabels().get(Label.Layer.LABEL).get(0).getSticker() == Sticker.RADAR ||
                            consumable.getLabels().get(Label.Layer.HIDDEN).get(0).getSticker() == Sticker.RADAR) {
                        radar = true;
                        break;
                    }
                }

                if (r == 0 && (Radar.isActive || radar)) {
                    r += MathUtils.random(1, 3);
                }
                if (r == 1 && (Shuttle.isLaser || laser)) {
                    r += MathUtils.random(1, 2);
                }

                switch (r) {
                    case 0:
                        return Sticker.RADAR;
                    case 1:
                        return Sticker.LASER;
                    default:
                        return Sticker.ROCKETS;
                }
            default:
                r = MathUtils.random(0, 6);
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
                if (dangerLabel.getSticker() == Sticker.SHABBY || dangerLabel.getSticker() == Sticker.SHABBY_GOLD) {
                    setLabelForSingle(labels.get(Label.Layer.BACK).get(0));
                    labels.get(Label.Layer.BACK).get(0).render(batch);
                    dangerLabel.render(batch);
                } else {
                    if (!scanned) {
                        setLabelForSingle(labels.get(Label.Layer.BACK).get(0));
                        labels.get(Label.Layer.BACK).get(0).render(batch);
                        dangerLabel.render(batch);
                    } else {
                        if (taken) {
                            setLabelForSingle(labels.get(Label.Layer.BACK).get(0));
                            labels.get(Label.Layer.BACK).get(0).render(batch);
                            dangerLabel.render(batch);
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
        if (scanned && !taken && dangerLabel.getSticker() != Sticker.SHABBY && dangerLabel.getSticker() != Sticker.SHABBY_GOLD) {
            scan.render(batch);
        }
    }

    private void setLabelForDouble(Label label, int i) {
        label.setScaleFactor(PaintConstants.LABEL_DOUBLE_SCALE_FACTOR);
        label.setD((float) Math.pow(-1, i) * PaintConstants.STORAGE_DOUBLE_OFFSET);
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        showSprayCollision();
    }

    private enum Size {
        SINGLE, DOUBLE
    }
}