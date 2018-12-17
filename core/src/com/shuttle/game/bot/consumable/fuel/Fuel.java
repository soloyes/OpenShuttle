package com.shuttle.game.bot.consumable.fuel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.bot.consumable.Label;
import com.shuttle.game.bot.consumable.Shield;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.effects.MoveProcessor;
import com.shuttle.game.utils.effects.Vibrator;

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

        this.labels[1].setSticker(Label.Sticker.FUEL);
        for (int i = 0; i < labels.length; i++) {
            this.labels[i].setMaster(this);
            this.labels[i].setColor(Color.valueOf("000000ff"));
            this.labels[i].setScaleFactor(4);
        }
        this.shielded = true;
    }

    @Override
    public void consumed(GameScreen gs) {
        int fuel = Rules.CONSUMABLE_FUEL_BASE_ADD * MathUtils.random(5, 10);
        gs.getPlayer().getStanding().changeFuel(fuel);
        gs.getGameInfoSystem()
                .setup(
                        position.x,
                        position.y,
                        GameInfoSystem.GameInfoSystemPreset.FUEL + "[GREEN] +" + fuel + "[]",
                        3.0f,
                        true
                );
        boomBox.playSound(MSConstants.GET_FUEL);
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
        boomBox.playSound(MSConstants.FUEL_DESTROY);
        Vibrator.vibrate(VConstants.PLAYER_FUEL_DESTROY);
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        moveProcessor.update(this, dt);
        for (int i = 0; i < PaintConstants.FUEL_BUBBLE_COUNT; i++) {
            bubble[i].update(dt);
        }
        boundCheck.checkAndHandleBoundsAndDestroy(this);
        for (int i = 0; i < labels.length; i++) {
            labels[i].update(dt);
        }
        shield.update(dt);
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        gs.getParticleEmitter().getBuilder().buildFuelDestroyFlame(position);
    }

    @Override
    public void render(SpriteBatch batch) {
        substrate.render(batch);
        for (int i = 0; i < PaintConstants.FUEL_BUBBLE_COUNT; i++) {
            bubble[i].render(batch);
        }
        box.render(batch);
        labels[1].render(batch);
        shield.render(batch);
    }
}
