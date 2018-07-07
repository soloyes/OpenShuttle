package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.screens.GameScreen;

import lombok.Data;

/**
 * @author Shuttle on 7/7/18.
 */
@Data
public class Radar implements Poolable {
    private TextureRegion textureRegion;
    private TextureRegion starTex;
    private Vector2 position;
    private Vector2 tmp;
    private boolean active;
    private int width, height;
    private GameScreen gs;
    private Shuttle player;
    private float scaleX;
    private float rotation;
    private Alien tmpAlien;
    private Consumable tmpConsumable;

    public Radar(GameScreen gs, Shuttle player) {
        this.textureRegion = Assets.getInstance().getAtlas().findRegion("radar");
        this.starTex = Assets.getInstance().getAtlas().findRegion("star16");
        this.gs = gs;
        this.player = player;
        this.active = false;
        this.position = new Vector2(
                MathUtils.random(0, Rules.SPACE_WIDTH),
                MathUtils.random(0, Rules.SPACE_HEIGHT)
        );
        this.tmp = new Vector2();
        this.width = this.height = 128;
        this.scaleX = 1;
    }

    public void render(SpriteBatch batch) {
        if (active) {
            drawRadar(batch);
        } else {
            batch.draw(textureRegion, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, scaleX, 1, 0);
        }
    }

    public void drawRadar(SpriteBatch batch) {
        for (int i = 0; i < gs.getAlienEmitter().getActiveList().size(); i++) {
            tmpAlien = gs.getAlienEmitter().getActiveList().get(i);
            float d = Vector2.dst(player.getPosition().x, player.getPosition().y, tmpAlien.getPosition().x, tmpAlien.getPosition().y);
            if (d < 2000.0f) {
                float sc = (1.0f - d / 2000f) * 2 + 2.0f;
                tmp.set(tmpAlien.getPosition()).sub(player.getPosition()).nor().scl(d / 2000.0f * 300.0f);
                switch (tmpAlien.getLevel()){
                    case 0:
                        batch.setColor(1.0f, 0.8f, 0.69f, 1);
                        break;
                    case 1:
                        batch.setColor(0.36f, 1.0f, 0.34f, 1);
                        break;
                    case 2:
                        batch.setColor(0.99f, 1.0f, 0.34f, 1);
                        break;
                    case 3:
                        batch.setColor(0.91f, 0.07f, 0.14f, 1);
                        break;
                }
                batch.draw(starTex, player.getPosition().x + tmp.x, player.getPosition().y + tmp.y, 8f, 8f, 16, 16, sc, sc, 0);
                batch.setColor(1, 1, 1, 1);
            }
        }

        for (int i = 0; i < gs.getConsumableEmitter().getActiveList().size(); i++) {
            tmpConsumable = gs.getConsumableEmitter().getActiveList().get(i);
            float d = Vector2.dst(player.getPosition().x, player.getPosition().y, tmpConsumable.getPosition().x, tmpConsumable.getPosition().y);
            if (d < 2000.0f) {
                float sc = (1.0f - d / 2000.0f) * 2.0f + 2.0f;
                tmp.set(tmpConsumable.getPosition()).sub(player.getPosition()).nor().scl(d / 2000.0f * 300.0f);
                batch.setColor(1, 1, 1, 1);
                batch.draw(starTex, player.getPosition().x + tmp.x, player.getPosition().y + tmp.y, 8f, 8f, 16, 16, sc, sc, 0);
            }
        }
    }

    public void update(float dt) {
        rotation += dt;
        scaleX = (float) Math.sin(Math.toRadians(rotation * 100));
    }
}