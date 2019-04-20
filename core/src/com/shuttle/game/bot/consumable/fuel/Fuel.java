package com.shuttle.game.bot.consumable.fuel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.MoveProcessor;
import com.shuttle.game.effects.Shield;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.labelsticker.Label;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.utils.Consumer;

public class Fuel extends AbstractConsumable {
    private Substrate substrate;
    private Box box;
    private Bubble[] bubble;

    public Fuel(GameScreen gs) {
        super(gs);
        this.width = PaintConstants.FUEL_1_WIDTH;
        this.height = PaintConstants.FUEL_1_HEIGHT;
        this.type = Type.FUEL;
        this.moveProcessor = new MoveProcessor(gs);
        this.level = -2;
        this.substrate = new Substrate(this);
        this.box = new Box(this);
        this.bubble = new Bubble[10];
        for (int i = 0; i < PaintConstants.FUEL_BUBBLE_COUNT; i++) {
            this.bubble[i] = new Bubble(this);
        }
        this.shield = new Shield(this);
    }

    @Override
    public void badEffect(GameScreen gs) {
        GameInfoSystem.setup(
                position.x,
                position.y,
                GameInfoSystem.Preset.BOOM,
                3.0f,
                false
        );
        boomBox.playSound(MSConstants.DESTROY_FUEL);
        gs.getPlayer().takeDamage(Rules.CONSUMABLE_FUEL_HP_DAMAGE);
        Vibrator.vibrate(VConstants.PLAYER_FUEL_DESTROY);
    }

    @Override
    public void consumed(GameScreen gs) {
        Consumer.consume(gs, labels.get(Label.Layer.LABEL).get(0).getSticker(), this);
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
        for (int i = 0; i < PaintConstants.FUEL_BUBBLE_COUNT; i++) {
            this.bubble[i].init(width, height);
        }
        this.labeled = true;

        this.labels.get(Label.Layer.LABEL).get(0).setSticker(Sticker.FUEL);
        this.labels.get(Label.Layer.LABEL).get(0).setConsumable(this);
        this.labels.get(Label.Layer.LABEL).get(0).setColor(Color.valueOf("000000ff"));
        this.labels.get(Label.Layer.LABEL).get(0).setScaleFactor(4);

        this.shielded = false;
        this.shield.setTime(Rules.CONSUMABLE_SHIELD_TIME);
        this.golden = MathUtils.random(0, Rules.CONSUMABLE_GOLDEN_PROBABILITY) == 0;
        if (fake) {
            golden = false;
        }
        this.box.init();
    }

    @Override
    public void basicRender(SpriteBatch batch) {
        substrate.render(batch);
        for (int i = 0; i < PaintConstants.FUEL_BUBBLE_COUNT; i++) {
            bubble[i].render(batch);
        }
        box.render(batch);
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        moveProcessor.update(this, dt);
        for (int i = 0; i < PaintConstants.FUEL_BUBBLE_COUNT; i++) {
            bubble[i].update(dt);
        }
        boundCheck.checkAndHandleBoundsAndDestroy(this);
        shield.update(dt);
    }

    @Override
    public void basicUpdate(float dt) {
        super.basicUpdate(dt);
        labels.get(Label.Layer.LABEL).get(0).update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        basicRender(batch);
        labels.get(Label.Layer.LABEL).get(0).render(batch);
        if (shielded && !taken) {
            shield.render(batch);
        }
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        gs.getParticleEmitter().getBuilder().buildFuelDestroyFlame(position);
    }
}
