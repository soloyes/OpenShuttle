package ru.geekbrains.stargame.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.utils.regions.Regions;

public class Sprite extends Rect {

    protected float angle;
    protected float scale = 1f;
    private TextureRegion[] regions;
    protected int frame;

    public Sprite(TextureRegion region) {
        if (region == null) {
            throw new NullPointerException("Texture is null");
        }
        regions = new TextureRegion[1];
        regions[0] = region;
    }

    public Sprite(TextureRegion region, int i, int j, int frames) {
        if (region == null) {
            throw new NullPointerException("Texture is null");
        }
        regions = Regions.split(region, i, j, frames);
    }

    public Sprite(){

    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame], // текущий регион
                getLeft(), getBottom(), // точка отрисовки
                halfWidth, halfHeight, // точка вращения
                getWidth(), getHeight(), // ширина и высота
                scale, scale, // масштаб по x и y
                angle // угол вращения
        );
    }

    public void setWithProportion(float width) {
        setWidth(width);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setHeight(width / aspect);
    }

    protected void setHeightProportion(float height) {
        setHeight(height);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth(height * aspect);
    }

    public void resize(Rect worldBounds) {

    }

    protected void touchDown(Vector2 touch, int pointer) {

    }

    protected void touchUp(Vector2 touch, int pointer) {

    }

    protected void touchDragged(Vector2 touch, int pointer) {

    }

    public void update(float delta) {

    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void dispose(){
        for (int i = 0; i < regions.length; i++) {
            regions[i].getTexture().dispose();
        }
    }
}