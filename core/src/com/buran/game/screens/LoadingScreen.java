package com.buran.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
    private com.buran.game.effects.Oscillator shieldScaleOscillator;
    private com.buran.game.effects.Oscillator shuttleYOscillator;

    public LoadingScreen(SpriteBatch batch) {
        super(batch);
        this.batch = batch;
        pixmap = new Pixmap(com.buran.game.conf.Rules.WORLD_WIDTH, 60, Pixmap.Format.RGB888);
        pixmap.setColor(0.8f, 0.43f, 0.33f, 1f);
        pixmap.fill();
        this.texture = new Texture(pixmap);
        this.shuttle = new TextureRegion(new Texture(com.buran.game.conf.PaintConstants.LOAD_TEXTURES + "menubackgroundshuttle.png"));
        this.shield = new TextureRegion(new Texture(com.buran.game.conf.PaintConstants.LOAD_TEXTURES + "menubackgroundshield.png"));
        this.shieldScaleOscillator = new com.buran.game.effects.Oscillator();
        this.shieldScale = 1.0f;
        this.shuttleYOscillator = new com.buran.game.effects.Oscillator();
        this.shuttleX = com.buran.game.conf.Rules.WORLD_WIDTH / 2 - com.buran.game.conf.PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_WIDTH / 2;
        this.shuttleY = com.buran.game.conf.Rules.WORLD_HEIGHT / 2 - com.buran.game.conf.PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT / 2;
        this.shieldX = com.buran.game.conf.Rules.WORLD_WIDTH / 2 - com.buran.game.conf.PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH / 2;
        this.shieldY = com.buran.game.conf.Rules.WORLD_HEIGHT / 2 - com.buran.game.conf.PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT / 2;
    }

    @Override
    public void show() {
    }

    @Override
    public void createGUI() {
        ScreenManager.getInstance().getAdsController().loadBannerAd();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (com.buran.game.utils.Assets.getInstance().getAssetManager().update()) {
            com.buran.game.utils.Assets.getInstance().makeLinks();
            ScreenManager.getInstance().goToTarget();
        }

        shieldScale = shieldScaleOscillator.oscillate(shieldScale, 0.09f, 0.95f, 1.0f, delta);
        shuttleY = shuttleYOscillator.oscillate(
                shuttleY, 12.0f,
                com.buran.game.conf.Rules.WORLD_HEIGHT / 2 - com.buran.game.conf.PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT / 2,
                com.buran.game.conf.Rules.WORLD_HEIGHT / 2 - com.buran.game.conf.PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT / 2 + 15,
                delta
        );

        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(texture, 0, 0, com.buran.game.conf.Rules.WORLD_WIDTH * com.buran.game.utils.Assets.getInstance().getAssetManager().getProgress(), 60);
        batch.draw(shuttle,
                shuttleX, shuttleY,
                com.buran.game.conf.PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_WIDTH, com.buran.game.conf.PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT
        );
        batch.draw(shield,
                shieldX, shieldY,
                com.buran.game.conf.PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH / 2, com.buran.game.conf.PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT / 2,
                com.buran.game.conf.PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH, com.buran.game.conf.PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT,
                shieldScale, shieldScale,
                0.0f
        );
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        texture.dispose();
        pixmap.dispose();
        shuttle.getTexture().dispose();
        shield.getTexture().dispose();
    }

    @Override
    public void pause() {
    }
}