package com.shuttle.game.player;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.particle.ParticleEmitter;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.Joystick;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;

import java.util.LinkedList;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 10/24/18.
 */
public class AbstractShuttle implements InputProcessor {
    public static Vector2 position, velocity;
    public static float angle;
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
    protected TextureRegion[] textureRegion;
    protected Vector2 tmpV2, tmp2V2;
    @Getter
    @Setter
    protected int width, height;
    @Getter
    @Setter
    protected boolean taken;
    @Getter
    @Setter
    protected float scaleX, scaleY;
    @Getter
    @Setter
    protected float scaleXBlur, scaleYBlur;
    @Getter
    @Setter
    protected int widthBlur, heightBlur;
    static {
        position = new Vector2(
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2
        );
        velocity = new Vector2();
        angle = 0.0f;
    }

    protected AbstractShuttle(GameScreen gs) {
        this.pointer = new LinkedList<Integer>();
        this.boomBox = new BoomBox();
        this.gs = gs;
        this.engineParticleEmitter = new ParticleEmitter();
        this.textureRegion = new TextureRegion[4];
        this.textureRegion[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_REGION);
        TextureRegion[] tmpRegion = Regions.split(
                Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_BLUR_REGION),
                1, 3, 3
        );
        System.arraycopy(tmpRegion, 0, textureRegion, 1, tmpRegion.length);
        this.tmpV2 = new Vector2();
        this.tmp2V2 = new Vector2();
        this.width = PaintConstants.SHUTTLE_WIDTH;
        this.height = PaintConstants.SHUTTLE_HEIGHT;
        this.widthBlur = PaintConstants.SHUTTLE_BLUR_WIDTH;
        this.heightBlur = PaintConstants.SHUTTLE_BLUR_HEIGHT;
        this.taken = false;
        this.scaleX = this.scaleY = 1.0f;
        this.scaleXBlur = this.scaleYBlur = 1.0f;
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