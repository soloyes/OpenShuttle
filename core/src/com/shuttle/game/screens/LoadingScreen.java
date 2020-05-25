package com.shuttle.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.Oscillator;
import com.shuttle.game.utils.Assets;

/**
 * Created by FlameXander on 12.03.2018.
 */

public class LoadingScreen extends BaseScreen implements Screen {
    private SpriteBatch batch;
    private Texture texture;
    private TextureRegion shuttle;
    private TextureRegion shield;
    private Pixmap pixmap;
    private float shuttleX, shuttleY;
    private float shieldX, shieldY;
    private float shieldScale;
    private Oscillator shieldScaleOscillator;
    private Oscillator shuttleYOscillator;

    public LoadingScreen(SpriteBatch batch) {
        super(batch);
        this.batch = batch;
        pixmap = new Pixmap(Rules.WORLD_WIDTH, 60, Pixmap.Format.RGB888);
        pixmap.setColor(0.8f, 0.43f, 0.33f, 1f);
        pixmap.fill();
        this.texture = new Texture(pixmap);
        this.shuttle = new TextureRegion(new Texture(PaintConstants.LOAD_TEXTURES + "menubackgroundshuttle.png"));
        this.shield = new TextureRegion(new Texture(PaintConstants.LOAD_TEXTURES + "menubackgroundshield.png"));
        this.shieldScaleOscillator = new Oscillator();
        this.shieldScale = 1.0f;
        this.shuttleYOscillator = new Oscillator();
        this.shuttleX = Rules.WORLD_WIDTH / 2 - PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_WIDTH / 2;
        this.shuttleY = Rules.WORLD_HEIGHT / 2 - PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT / 2;
        this.shieldX = Rules.WORLD_WIDTH / 2 - PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH / 2;
        this.shieldY = Rules.WORLD_HEIGHT / 2 - PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT / 2;
    }

    @Override
    public void show() {
    }

    @Override
    public void createGUI() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Assets.getInstance().getAssetManager().update()) {
            Assets.getInstance().makeLinks();
            ScreenManager.getInstance().goToTarget();
        }

        shieldScale = shieldScaleOscillator.oscillate(shieldScale, 0.09f, 0.95f, 1.0f, delta);
        shuttleY = shuttleYOscillator.oscillate(
                shuttleY, 12.0f,
                Rules.WORLD_HEIGHT / 2 - PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT / 2,
                Rules.WORLD_HEIGHT / 2 - PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT / 2 + 15,
                delta
        );

        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(texture, 0, 0, Rules.WORLD_WIDTH * Assets.getInstance().getAssetManager().getProgress(), 60);
        batch.draw(shuttle,
                shuttleX, shuttleY,
                PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_WIDTH, PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT
        );
        batch.draw(shield,
                shieldX, shieldY,
                PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH / 2, PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT / 2,
                PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH, PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT,
                shieldScale, shieldScale,
                0.0f
        );
        batch.end();
    }

    @Override
    public void dispose() {
        texture.dispose();
        pixmap.dispose();
        shuttle.getTexture().dispose();
        shield.getTexture().dispose();
    }
}