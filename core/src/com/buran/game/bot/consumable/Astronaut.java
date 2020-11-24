package com.buran.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.conf.VConstants;
import com.buran.game.effects.MoveProcessor;
import com.buran.game.effects.Vibrator;
import com.buran.game.effects.particle.ParticleEmitter;
import com.buran.game.labelsticker.Label;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.player.Standing;
import com.buran.game.ui.GameInfoSystem;
import com.buran.game.utils.Assets;
import com.buran.game.utils.Consumer;
import com.buran.game.utils.Regions;

/**
 * @author soloyes on 10/11/18.
 */

public class Astronaut extends AbstractConsumable {
    private TextureRegion[] regions;
    private int multiplier;

    public Astronaut(ParticleEmitter particleEmitter) {
        super(particleEmitter);
        this.particleEmitter = particleEmitter;
        this.regions = Regions.split(
                Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_REGION), 1, 5, 5);
        this.width = PaintConstants.ASTRONAUT_WIDTH;
        this.height = PaintConstants.ASTRONAUT_HEIGHT;
        this.type = Type.ASTRONAUT;
        this.moveProcessor = new MoveProcessor();
        this.labels.get(Label.Layer.LABEL).get(0).setSticker(Sticker.COMMANDER);
    }

    @Override
    public void badEffect() {
        Standing standing = Standing.getInstance();
        int score = (int) (Rules.CONSUMABLE_MURDER_SCORE_DECREASE * Sticker.COMMANDER.getCoefficient());
        standing.changeScore(-score, position, GameInfoSystem.Preset.MURDERER + "\n[RED]-" + (int) (Rules.CONSUMABLE_MURDER_SCORE_DECREASE * Sticker.COMMANDER.getCoefficient()) + "[]", 8.0f, true);
        boomBox.playSound(MSConstants.DESTROY_ASTRONAUT);
        Vibrator.vibrate(VConstants.PLAYER_ASTRONAUT_DESTROY);
    }

    @Override
    public void consumed() {
        int score = Rules.CONSUMABLE_ASTRONAUT_BASE_SCORE_ADD +
                multiplier * Rules.CONSUMABLE_ASTRONAUT_RANGE_SCORE_ADD +
                (int) Standing.getInstance().getRamPower() * Rules.CONSUMABLE_ASTRONAUT_BONUS_SCORE_ADD;
        score = (int) (score * Sticker.COMMANDER.getCoefficient());
        Standing.getInstance().changeScore(score, position, GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]", 3.0f, true);
        boomBox.playSound(MSConstants.GET_ASTRONAUT);
        Consumer.consume(labels.get(Label.Layer.LABEL).get(0).getSticker(), this);
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
        this.harvested = false;
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.spiralRadius = MathUtils.random(20, 60);
        this.atRayPoint = false;
        if (fake) {
            this.multiplier = 0;
        } else {
            this.multiplier = MathUtils.random(0, 4);
        }
        this.labels.get(Label.Layer.LABEL).get(0).setSticker(Sticker.ASTRONAUTS.get(multiplier));
        this.texture = regions[multiplier];
        playSound();
    }

    public Sticker getProfession() {
        return labels.get(Label.Layer.LABEL).get(0).getSticker();
    }

    private void playSound() {
        if (!fake && boundCheck.checkAndHandleBoundsInCamera(this)) {
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
    public void destroyWithEffect() {
        super.destroyWithEffect();
        showSprayCollision();
    }
}