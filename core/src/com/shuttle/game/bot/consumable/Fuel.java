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
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.effects.MoveProcessor;
import com.shuttle.game.utils.effects.Vibrator;

public class Fuel extends AbstractConsumable {
    public Fuel(GameScreen gs) {
        super(gs);
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_1_REGION);
        this.width = PaintConstants.FUEL_1_WIDTH;
        this.height = PaintConstants.FUEL_1_HEIGHT;
        this.type = Type.FUEL;
        this.moveProcessor = new MoveProcessor(gs);
        this.level = -2;
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
        boundCheck.checkAndHandleBoundsAndDestroy(this);
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        gs.getParticleEmitter().getBuilder().buildFuelDestroyFlame(position);
    }
}
