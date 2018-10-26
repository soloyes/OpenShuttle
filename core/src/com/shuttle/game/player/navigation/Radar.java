package com.shuttle.game.player.navigation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.CorrosiveAnomaly;
import com.shuttle.game.bot.consumable.Consumable;
import com.shuttle.game.bot.anomaly.ElectricalAnomaly;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Shuttle on 7/7/18.
 */
public class Radar {
    private GameScreen gs;
    private TextureRegion backgroundTexture;
    private TextureRegion pointTexture;
    @Getter
    @Setter
    private Vector2 position;
    private Vector2 tmp;
    @Getter
    @Setter
    private boolean active;
    private int width, height;
    private Shuttle player;
    private float scaleX, scaleY;
    private float rotation;
    private boolean oscillator;
    private float o;
    private Color color;

    public Radar(GameScreen gs, Shuttle player) {
        this.gs = gs;
        this.backgroundTexture = Assets.getInstance().getAtlas().findRegion(PaintConstants.RADAR_REGION);
        this.pointTexture = Assets.getInstance().getAtlas().findRegion(PaintConstants.STAR16_REGION);
        this.player = player;
        this.position = new Vector2();
        this.tmp = new Vector2();
        this.width = PaintConstants.RADAR_WIDTH;
        this.height = PaintConstants.RADAR_HEIGHT;
        this.scaleX = this.scaleY = 1;
        this.generatePosition();
        this.oscillator = true;
        this.o = 0.0f;
        this.color = new Color(Color.valueOf(PaintConstants.BACKGROUND_COLOR_HEX));
    }

    //Prevent generation near blackhole
    public final void generatePosition() {
        float x = MathUtils.random(0, Rules.SPACE_WIDTH);
        float y = MathUtils.random(0, Rules.SPACE_HEIGHT);
        float r1 = gs.getBlackhole().getRadius().len();
        float x0 = gs.getBlackhole().getCenter().x;
        float y0 = gs.getBlackhole().getCenter().y;
        while (true) {
            float r2 = (float) Math.sqrt(Math.abs(Math.pow(x - x0, 2) + Math.pow(y - y0, 2)));
            if (Math.abs(r1 - r2) >= Rules.BLACKHOLE_TAKE_RANGE) {
                break;
            }
            x = MathUtils.random(0, Rules.SPACE_WIDTH);
            y = MathUtils.random(0, Rules.SPACE_HEIGHT);
        }
        position.set(x, y);
    }

    public void render(SpriteBatch batch) {
        if (active) {
            if (!player.isTaken()) {
                drawRadar(batch);
            }
        } else {
            batch.draw(backgroundTexture, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, scaleX, scaleY, 0);
        }
    }

    private void drawRadar(SpriteBatch batch) {
        for (int i = 0; i < gs.getCorrosiveAnomalyEmitter().getActiveList().size(); i++) {
            CorrosiveAnomaly tmpAnomaly = (CorrosiveAnomaly) gs.getCorrosiveAnomalyEmitter().getActiveList().get(i);
            drawPoolables(batch, tmpAnomaly);
        }
        for (int i = 0; i < gs.getConsumableEmitter().getActiveList().size(); i++) {
            Consumable tmpConsumable = gs.getConsumableEmitter().getActiveList().get(i);
            drawPoolables(batch, tmpConsumable);
        }
        for (int i = 0; i < gs.getElectricalAnomalyEmitter().getActiveList().size(); i++) {
            ElectricalAnomaly tmpAnomaly = (ElectricalAnomaly) gs.getElectricalAnomalyEmitter().getActiveList().get(i);
            drawPoolables(batch, tmpAnomaly);
        }

        batch.setColor(1, 1, 1, 1);
    }

    private void drawPoolables(SpriteBatch batch, AbstractPoolable abstractPoolable) {
        float d = Vector2.dst(player.getPosition().x, player.getPosition().y, abstractPoolable.getPosition().x, abstractPoolable.getPosition().y);
        if (d < 2000.0f) {
            float sc = (1.0f - d / 2000.0f) * 2.0f + 2.0f + o;
            tmp.set(abstractPoolable.getPosition()).sub(player.getPosition()).nor().scl(d / 2000.0f * 300.0f);
            batch.setColor(setPointColor(abstractPoolable));
            batch.draw(pointTexture, player.getPosition().x + tmp.x, player.getPosition().y + tmp.y, 8f, 8f, 16, 16, sc, sc, 0);
        }
    }

    private Color setPointColor(AbstractPoolable abstractPoolable) {
        float d1 = Vector2.dst(player.getPosition().x, player.getPosition().y, gs.getBlackhole().getPosition().x, gs.getBlackhole().getPosition().y);
        int c;
        if (d1 <= Rules.BLACKHOLE_NOIZE_RANGE) {
            c = MathUtils.random(-1, 4);
            if (MathUtils.random(0, 100) <= 60) {
                return color;
            }
        } else {
            c = abstractPoolable.getLevel();
        }
        switch (c) {
            case 0:
                return Color.TAN;
            case 1:
                return Color.GREEN;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.RED;
            case 4:
                return Color.PURPLE;
            default:
                return Color.WHITE;
        }
    }

    private void updateRadar(float dt) {
        if (oscillator) {
            o += Rules.RADAR_OSCILLATION_FREQUENCY * dt;
            if (o >= Rules.RADAR_OSCILLATION_FACTOR) {
                o = Rules.RADAR_OSCILLATION_FACTOR;
                oscillator = false;
            }
        } else {
            o -= Rules.RADAR_OSCILLATION_FREQUENCY * dt;
            if (o <= -Rules.RADAR_OSCILLATION_FACTOR) {
                o = -Rules.RADAR_OSCILLATION_FACTOR;
                oscillator = true;
            }
        }
    }

    public void update(float dt) {
        if (!active) {
            rotation += dt;
            scaleX = (float) Math.sin(Math.toRadians(rotation * 100));
        } else {
            updateRadar(dt);
        }
    }
}