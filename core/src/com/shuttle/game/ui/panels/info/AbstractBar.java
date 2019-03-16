package com.shuttle.game.ui.panels.info;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 3/14/19.
 */
public abstract class AbstractBar {
    protected TextureRegion outBar, inBar, icon, scale;
    protected GameScreen gs;
    protected int width, height;
    protected Standing standing;
    protected float alpha;
    protected Color color;

    public AbstractBar(GameScreen gs) {
        this.gs = gs;
        this.standing = gs.getPlayer().getStanding();
        this.color = new Color();
        this.outBar = new TextureRegion();
        this.inBar = new TextureRegion();
        this.icon = new TextureRegion();
        this.scale = new TextureRegion();
        this.alpha = 1.0f;
    }

    protected void update(float dt){

    }

    protected void render(SpriteBatch batch){
//        Color tmpColor = batch.getColor();
//        batch.setColor(color);
        batch.draw(inBar,
                Rules.WORLD_WIDTH /2, Rules.WORLD_HEIGHT /2,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        batch.draw(outBar,
                Rules.WORLD_WIDTH /2, Rules.WORLD_HEIGHT /2,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        batch.draw(scale,
                Rules.WORLD_WIDTH /2, Rules.WORLD_HEIGHT /2,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        batch.draw(icon,
                Rules.WORLD_WIDTH /2, Rules.WORLD_HEIGHT /2,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        //batch.setColor(tmpColor);
    }
}
