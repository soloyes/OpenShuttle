package com.buran.game.player;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.conf.PaintConstants;
import com.buran.game.effects.BoomBox;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.Joystick;
import com.buran.game.utils.Assets;
import com.buran.game.utils.Regions;

import java.util.LinkedList;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 10/24/18.
 */

public class AbstractBuran implements InputProcessor {
    public static Vector2 position, velocity;
    public static float angle;
    public static boolean isTaken;
    protected LinkedList<Integer> pointer;
    protected Joystick joystick;
    protected BoomBox boomBox;
    @Getter
    @Setter
    protected Standing standing;
    protected TextureRegion[] textureRegion;
    protected Vector2 tmpV2, tmp2V2, tmp3V2;
    @Getter
    @Setter
    protected int width, height;
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
        isTaken = false;
    }

    protected AbstractBuran() {
        this.pointer = new LinkedList<Integer>();
        this.boomBox = new BoomBox();
        this.textureRegion = new TextureRegion[4];
        this.textureRegion[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.BURAN_REGION);
        TextureRegion[] tmpRegion = Regions.split(
                Assets.getInstance().getAtlas().findRegion(PaintConstants.BURAN_BLUR_REGION),
                1, 3, 3
        );
        System.arraycopy(tmpRegion, 0, textureRegion, 1, tmpRegion.length);
        this.tmpV2 = new Vector2();
        this.tmp2V2 = new Vector2(1, 1);
        this.tmp3V2 = new Vector2();
        this.width = PaintConstants.BURAN_WIDTH;
        this.height = PaintConstants.BURAN_HEIGHT;
        // The painted ship (assets/sprites/sprite_buran.png, nose right, no padding) when present.
        com.badlogic.gdx.files.FileHandle painted = com.badlogic.gdx.Gdx.files.internal("sprites/sprite_buran.png");
        if (painted.exists()) {
            com.badlogic.gdx.graphics.Texture t = new com.badlogic.gdx.graphics.Texture(painted);
            t.setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Linear, com.badlogic.gdx.graphics.Texture.TextureFilter.Linear);
            this.textureRegion[0] = new TextureRegion(t);
            this.width = 215;
            this.height = 215 * t.getHeight() / t.getWidth();
        }
        this.widthBlur = PaintConstants.BURAN_BLUR_WIDTH;
        this.heightBlur = PaintConstants.BURAN_BLUR_HEIGHT;
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
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }
}