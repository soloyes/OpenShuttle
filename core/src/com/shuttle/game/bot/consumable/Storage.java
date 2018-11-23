package com.shuttle.game.bot.consumable;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.effects.MoveProcessor;
import com.shuttle.game.utils.effects.Vibrator;

public class Storage extends AbstractConsumable {
    public Storage(GameScreen gs) {
        super(gs);
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.STORAGE_1_REGION);
        this.width = PaintConstants.STORAGE_1_WIDTH;
        this.height = PaintConstants.STORAGE_1_HEIGHT;
        this.type = Type.STORAGE;
        this.moveProcessor = new MoveProcessor(gs);
        this.level = -3;
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
        gs.getGameInfoSystem()
                .setup(
                        position.x,
                        position.y,
                        GameInfoSystem.GameInfoSystemPreset.STORAGE ,
                        3.0f,
                        true
                );
        //todo: get storage sound
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
        //todo: boom storage sound
        boomBox.playSound(MSConstants.KILL_CRATE);
        Vibrator.vibrate(VConstants.PLAYER_STORAGE_DESTROY);
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        moveProcessor.update(this, dt);
        boundCheck.checkAndHandleBoundsAndDestroy(this);
    }
}
