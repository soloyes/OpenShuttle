package com.shuttle.game.player;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.Joystick;
import com.shuttle.game.bot.particle.ParticleEmitter;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.BoomBox;

import java.util.LinkedList;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 10/24/18.
 */
public class AbstractShuttle implements InputProcessor {
    protected LinkedList<Integer> pointer;
    protected Joystick joystick;
    protected ParticleEmitter engineParticleEmitter;
    protected BoomBox boomBox;
    @Getter
    @Setter
    protected GameScreen gs;
    @Getter
    @Setter
    protected Standing standing;
    protected TextureRegion texture;
    protected Vector2 tmp1, tmp2;
    @Getter
    @Setter
    protected Vector2 position, velocity;
    @Getter
    @Setter
    protected int width, height;

    protected AbstractShuttle(GameScreen gs) {
        this.pointer = new LinkedList<Integer>();
        this.boomBox = new BoomBox();
        this.gs = gs;
        this.engineParticleEmitter = new ParticleEmitter();
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_REGION);
        this.position = new Vector2(
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2
        );
        this.velocity = new Vector2();
        this.tmp1 = new Vector2();
        this.tmp2 = new Vector2();
        this.width = PaintConstants.SHUTTLE_WIDTH;
        this.height = PaintConstants.SHUTTLE_HEIGHT;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.pointer.add(pointer);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        this.pointer.remove(Integer.valueOf(pointer));
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
