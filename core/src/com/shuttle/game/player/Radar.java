package com.shuttle.game.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.CorrosiveAnomaly;
import com.shuttle.game.bot.Consumable;
import com.shuttle.game.bot.anomaly.ElectricalAnomaly;
import com.shuttle.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;

import lombok.Data;

/**
 * @author Shuttle on 7/7/18.
 */
@Data
public class Radar {
    private GameScreen gs;
    private TextureRegion backgroundTexture;
    private TextureRegion pointTexture;
    private Vector2 position;
    private Vector2 tmp;
    private boolean active;
    private int width, height;
    private Shuttle player;
    private float scaleX;
    private float rotation;

    public Radar(GameScreen gs, Shuttle player) {
        this.backgroundTexture = Assets.getInstance().getAtlas().findRegion(PaintConstants.RADAR_REGION);
        this.pointTexture = Assets.getInstance().getAtlas().findRegion(PaintConstants.STAR16_REGION);
        this.gs = gs;
        this.player = player;
        this.position = new Vector2(
                MathUtils.random(0, Rules.SPACE_WIDTH),
                MathUtils.random(0, Rules.SPACE_HEIGHT)
        );
        this.tmp = new Vector2();
        this.width = PaintConstants.RADAR_WIDTH;
        this.height = PaintConstants.RADAR_HEIGHT;
        this.scaleX = 1;
    }

    public void render(SpriteBatch batch) {
        if (active) {
            drawRadar(batch);
        } else {
            batch.draw(backgroundTexture, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, scaleX, 1, 0);
        }
    }

    private void drawRadar(SpriteBatch batch) {
        for (int i = 0; i < gs.getCorrosiveAnomalyEmitter().getActiveList().size(); i++) {
            CorrosiveAnomaly tmpAnomaly = gs.getCorrosiveAnomalyEmitter().getActiveList().get(i);
            float d = Vector2.dst(player.getPosition().x, player.getPosition().y, tmpAnomaly.getPosition().x, tmpAnomaly.getPosition().y);
            if (d < 2000.0f) {
                float sc = (1.0f - d / 2000f) * 2 + 2.0f;
                tmp.set(tmpAnomaly.getPosition()).sub(player.getPosition()).nor().scl(d / 2000.0f * 300.0f);
                switch (tmpAnomaly.getLevel()) {
                    case 0:
                        batch.setColor(Color.TAN);
                        break;
                    case 1:
                        batch.setColor(Color.GREEN);
                        break;
                    case 2:
                        batch.setColor(Color.YELLOW);
                        break;
                    case 3:
                        batch.setColor(Color.RED);
                        break;
                }
                batch.draw(pointTexture, player.getPosition().x + tmp.x, player.getPosition().y + tmp.y, 8.0f, 8.0f, 16.0f, 16.0f, sc, sc, 0);
            }
        }

        for (int i = 0; i < gs.getConsumableEmitter().getActiveList().size(); i++) {
            Consumable tmpConsumable = gs.getConsumableEmitter().getActiveList().get(i);
            float d = Vector2.dst(player.getPosition().x, player.getPosition().y, tmpConsumable.getPosition().x, tmpConsumable.getPosition().y);
            if (d < 2000.0f) {
                float sc = (1.0f - d / 2000.0f) * 2.0f + 2.0f;
                tmp.set(tmpConsumable.getPosition()).sub(player.getPosition()).nor().scl(d / 2000.0f * 300.0f);
                batch.setColor(1, 1, 1, 1);
                batch.draw(pointTexture, player.getPosition().x + tmp.x, player.getPosition().y + tmp.y, 8f, 8f, 16, 16, sc, sc, 0);
            }
        }

        for (int i = 0; i < gs.getElectricalAnomalyEmitter().getActiveList().size(); i++) {
            ElectricalAnomaly tmpAnomaly = gs.getElectricalAnomalyEmitter().getActiveList().get(i);
            float d = Vector2.dst(player.getPosition().x, player.getPosition().y, tmpAnomaly.getPosition().x, tmpAnomaly.getPosition().y);
            if (d < 2000.0f) {
                float sc = (1.0f - d / 2000.0f) * 2.0f + 2.0f;
                tmp.set(tmpAnomaly.getPosition()).sub(player.getPosition()).nor().scl(d / 2000.0f * 300.0f);
                batch.setColor(Color.PURPLE);
                batch.draw(pointTexture, player.getPosition().x + tmp.x, player.getPosition().y + tmp.y, 8f, 8f, 16, 16, sc, sc, 0);
            }
        }
        batch.setColor(1, 1, 1, 1);
    }

    public void update(float dt) {
        rotation += dt;
        scaleX = (float) Math.sin(Math.toRadians(rotation * 100));
    }
}