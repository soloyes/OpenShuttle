package com.shuttle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;

import java.util.LinkedList;

/**
 * Created by FlameXander on 30.09.2017.
 */
public class Joystick extends InputAdapter {
    private TextureRegion background;
    private TextureRegion stick;
    private Rectangle rectangle;
    private float joyCenterX, joyCenterY;
    private Vector2 vs;
    private Vector2 tmp;
    private LinkedList<Integer> pointer;

    public Joystick() {
        TextureRegion texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.JOYSTICK_REGION);
        this.background = new TextureRegion(texture, 0, 0, 200, 200);
        this.stick = new TextureRegion(texture, 0, 200, 50, 50);
        this.rectangle = new Rectangle(50, 50, 200, 200);
        this.joyCenterX = rectangle.x + rectangle.width / 2;
        this.joyCenterY = rectangle.y + rectangle.height / 2;
        this.vs = new Vector2(0, 0);
        this.tmp = new Vector2(0, 0);
        this.pointer = new LinkedList<Integer>();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.pointer.add(pointer);
        setTarget();
        ScreenManager.getInstance().getViewport().unproject(tmp);
        rectangle.x = tmp.x - rectangle.width / 2;
        rectangle.y = tmp.y - rectangle.height / 2;
        joyCenterX = rectangle.x + rectangle.width / 2;
        joyCenterY = rectangle.y + rectangle.height / 2;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (this.pointer.size() == 0) {
            vs.x = 0;
            vs.y = 0;
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
            vs.x = tmp.x - joyCenterX;
            vs.y = tmp.y - joyCenterY;
            if (vs.len() > 75) {
                vs.nor().scl(75);
            }
        }
        return true;
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
            batch.setColor(1, 1, 1, 0.5f);
            batch.draw(background, rectangle.x, rectangle.y);
            batch.setColor(1, 1, 1, 0.7f);
            batch.draw(stick, joyCenterX + vs.x - 25, joyCenterY + vs.y - 25);
            batch.setColor(1, 1, 1, 1f);
        }
    }

    public boolean isActive() {
        return this.pointer.size() > 0 && GamePreferences.isJoyStickEnabled() && !Rules.GAME_OVER;
    }

    public float getAngle() {
        return vs.angle();
    }

    public float getPower() {
        return vs.len() / 75.0f;
    }
}
