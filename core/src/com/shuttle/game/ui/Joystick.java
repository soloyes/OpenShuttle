package com.shuttle.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;

import java.util.LinkedList;

/**
 * Created by FlameXander on 30.09.2017.
 */
public class Joystick extends Actor implements InputProcessor {
    private TextureRegion joy1;
    private TextureRegion stick1;
    private TextureRegion joy2;
    private TextureRegion stick2;

    private Rectangle rectangle;
    private Vector2 vs;
    private Vector2 tmp;
    private Vector2 joyCenter;
    private float stickDeviation;
    private LinkedList<Integer> pointer;
    private Color color;

    public Joystick() {
        TextureRegion[] textureRegions = Regions.split(Assets.getInstance().getAtlas().findRegion(PaintConstants.JOYSTICK_REGION), 1, 2, 2);
        this.joy1 = new TextureRegion(textureRegions[0], 0, 0, PaintConstants.JOYSTICK_WIDTH, PaintConstants.JOYSTICK_HEIGHT);
        this.stick1 = new TextureRegion(textureRegions[0], 0, PaintConstants.JOYSTICK_HEIGHT, PaintConstants.JOYSTICK_STICK_WIDTH, PaintConstants.JOYSTICK_STICK_HEIGHT);
        this.joy2 = new TextureRegion(textureRegions[1], 0, 0, PaintConstants.JOYSTICK_WIDTH, PaintConstants.JOYSTICK_HEIGHT);
        this.stick2 = new TextureRegion(textureRegions[1], 0, PaintConstants.JOYSTICK_HEIGHT, PaintConstants.JOYSTICK_STICK_WIDTH, PaintConstants.JOYSTICK_STICK_HEIGHT);

        this.rectangle = new Rectangle(PaintConstants.JOYSTICK_STICK_WIDTH, PaintConstants.JOYSTICK_STICK_HEIGHT, PaintConstants.JOYSTICK_WIDTH, PaintConstants.JOYSTICK_HEIGHT);
        this.joyCenter = new Vector2(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
        this.vs = new Vector2(0, 0);
        this.tmp = new Vector2(0, 0);
        this.pointer = new LinkedList<Integer>();
        this.stickDeviation = (PaintConstants.JOYSTICK_WIDTH + PaintConstants.JOYSTICK_STICK_WIDTH) / 2;
        this.color = new Color();
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
        setTarget();
        ScreenManager.getInstance().getViewport().unproject(tmp);
        rectangle.x = tmp.x - rectangle.width / 2;
        rectangle.y = tmp.y - rectangle.height / 2;
        joyCenter.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (this.pointer.size() == 0) {
            vs.set(0, 0);
        } else {
            this.pointer.remove(Integer.valueOf(pointer));
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (this.pointer.size() == 0) {
            this.pointer.add(pointer);
        }
        setTarget();
        ScreenManager.getInstance().getViewport().unproject(tmp);
        if (this.pointer.size() != 0) {
            vs.x = tmp.x - joyCenter.x;
            vs.y = tmp.y - joyCenter.y;
            if (vs.len() > stickDeviation) {
                vs.nor().scl(stickDeviation);
            }
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void setTarget() {
        if (this.pointer.size() == 0) {
            tmp.set(Gdx.input.getX(), Gdx.input.getY());
        } else {
            tmp.set(Gdx.input.getX(this.pointer.peekLast()), Gdx.input.getY(this.pointer.peekLast()));
        }
    }

    public void render(SpriteBatch batch) {
        if (isActive()) {
            drawJoystick(batch, rectangle.x, rectangle.y);
        }
    }

    private void drawJoystick(SpriteBatch batch, float x, float y) {
        TextureRegion joy;
        TextureRegion stick;
        int mode = GamePreferences.getJoyStickMode();
        switch (mode) {
            case 0:
                joy = joy1;
                stick = stick1;
                break;
            case 1:
                joy = joy1;
                stick = stick2;
                break;
            case 2:
                joy = joy2;
                stick = stick1;
                break;
            default:
                joy = joy2;
                stick = stick2;
        }

        if (GamePreferences.isJoyStickColorEnabled()) {
            color.set(Color.valueOf("00ffffff"));
        } else {
            color.set(Color.valueOf("ffffffff"));
        }

        color.a = PaintConstants.JOYSTICK_ALPHA;
        batch.setColor(color);
        batch.draw(joy, x, y);
        if (mode > 3) {
            batch.draw(joy1, x, y);
        }
        color.a = PaintConstants.JOYSTICK_STICK_ALPHA;
        batch.draw(stick, joyCenter.x + vs.x - PaintConstants.JOYSTICK_STICK_WIDTH / 2, joyCenter.y + vs.y - PaintConstants.JOYSTICK_STICK_HEIGHT / 2);
        if (mode > 3) {
            batch.draw(stick1, joyCenter.x + vs.x - PaintConstants.JOYSTICK_STICK_WIDTH / 2, joyCenter.y + vs.y - PaintConstants.JOYSTICK_STICK_HEIGHT / 2);
        }
        batch.setColor(1, 1, 1, 1f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        joyCenter.set(getX(), getY());
        vs.set(PaintConstants.JOYSTICK_WIDTH + PaintConstants.JOYSTICK_STICK_WIDTH / 2, PaintConstants.JOYSTICK_HEIGHT / 2);
        drawJoystick((SpriteBatch) batch, getX(), getY());
    }

    public boolean isActive() {
        return this.pointer.size() > 0 && GamePreferences.isJoyStickEnabled() && !Rules.GAME_OVER;
    }

    public float getAngle() {
        return vs.angle();
    }

    public float getPower() {
        return vs.len() / stickDeviation;
    }
}