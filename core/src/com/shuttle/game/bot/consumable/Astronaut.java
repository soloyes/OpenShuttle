package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.MoveProcessor;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;

/**
 * @author Shuttle on 10/11/18.
 */
public class Astronaut extends AbstractConsumable {
    private TextureRegion[] regions;
    private int multiplier;

    public Astronaut(GameScreen gs) {
        super(gs);
        this.regions = Regions.split(
                Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_REGION), 1, 5, 5);
        this.width = PaintConstants.ASTRONAUT_WIDTH;
        this.height = PaintConstants.ASTRONAUT_HEIGHT;
        this.type = Type.ASTRONAUT;
        this.moveProcessor = new MoveProcessor(gs);
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.angle = MathUtils.random(0, 360);
        this.rotationSpeed = MathUtils.random(-20, 20);
        this.scaleX = this.saveScaleX = 1.0f;
        this.scaleY = this.saveScaleY = 1.0f;
        this.active = true;
        this.taken = false;
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.spiralRadius = MathUtils.random(20, 60);
        this.atRayPoint = false;
        this.multiplier = MathUtils.random(0, regions.length - 1);
        this.texture = regions[multiplier];
        playSound();
    }

    private void playSound() {
        if (!fake && boundCheck.checkAndHandleBoundsInCamera(this, gs.getCamera())) {
            boomBox.playSound(MSConstants.NEW_ASTRONAUT);
        }
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        moveProcessor.update(this, dt);
        boundCheck.checkAndHandleBoundsAndDestroy(this);
    }

    @Override
    public void consumed(GameScreen gs) {
        int score = Rules.CONSUMABLE_ASTRONAUT_BASE_SCORE_ADD +
                multiplier * Rules.CONSUMABLE_ASTRONAUT_RANGE_SCORE_ADD +
                gs.getPlayer().getShieldLevel().getLevel() * Rules.CONSUMABLE_ASTRONAUT_BONUS_SCORE_ADD;
        gs.getPlayer().getStanding().changeScore(score);
        gs.getGameInfoSystem().setup(
                position.x,
                position.y,
                GameInfoSystem.GameInfoSystemPreset.SCORE + "[GREEN] +" + score + "[]",
                3.0f,
                true
        );
        boomBox.playSound(MSConstants.GET_ASTRONAUT);
    }

    @Override
    public void badEffect(GameScreen gs) {
        Standing standing = gs.getPlayer().getStanding();
        standing.changeScore(-Rules.CONSUMABLE_MURDER_SCORE_DECREASE);
        gs.getGameInfoSystem().setup(
                position.x,
                position.y,
                GameInfoSystem.GameInfoSystemPreset.MURDERER,
                8.0f,
                true
        );
        boomBox.playSound(MSConstants.DESTROY_ASTRONAUT);
        Vibrator.vibrate(VConstants.PLAYER_ASTRONAUT_DESTROY);
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        showSprayCollision(gs.getParticleEmitter());
    }
}
