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
import com.shuttle.game.labelsticker.Label;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.player.Standing;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;

/**
 * @author Shuttle on 10/11/18.
 */

public class Astronaut extends AbstractConsumable {
    private TextureRegion[] regions;
    private int multiplier;

    public Astronaut() {
        this.regions = Regions.split(
                Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_REGION), 1, 5, 5);
        this.width = PaintConstants.ASTRONAUT_WIDTH;
        this.height = PaintConstants.ASTRONAUT_HEIGHT;
        this.type = Type.ASTRONAUT;
        this.moveProcessor = new MoveProcessor();
        this.labels.get(Label.Layer.LABEL).get(0).setSticker(Sticker.REGULAR);
    }

    @Override
    public void badEffect() {
        Standing standing = Standing.getInstance();
        standing.changeScore(-Rules.CONSUMABLE_MURDER_SCORE_DECREASE);
        GameInfoSystem.setup(
                position.x,
                position.y,
                GameInfoSystem.Preset.MURDERER,
                8.0f,
                true
        );
        boomBox.playSound(MSConstants.DESTROY_ASTRONAUT);
        Vibrator.vibrate(VConstants.PLAYER_ASTRONAUT_DESTROY);
    }

    @Override
    public void consumed() {
        int score = Rules.CONSUMABLE_ASTRONAUT_BASE_SCORE_ADD +
                multiplier * Rules.CONSUMABLE_ASTRONAUT_RANGE_SCORE_ADD +
                (int) Standing.getInstance().getRamPower() * Rules.CONSUMABLE_ASTRONAUT_BONUS_SCORE_ADD;
        Standing.getInstance().changeScore(score);
        GameInfoSystem.setup(
                position.x,
                position.y,
                GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                3.0f,
                true
        );
        boomBox.playSound(MSConstants.GET_ASTRONAUT);
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