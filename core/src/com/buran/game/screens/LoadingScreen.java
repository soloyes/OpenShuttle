package com.buran.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.buran.game.background.MenuSpace;
import com.buran.game.conf.Rules;
import com.buran.game.utils.Assets;

/**
 * Between-screen loading: the drifting nebula, the title and a thin telemetry-style
 * progress line. Matches the browser boot screen (web/webapp/index.html).
 */
public class LoadingScreen extends BaseScreen implements Screen {
    private static final Color TITLE = Color.valueOf("f2f5faff");
    private static final Color DIM = Color.valueOf("6f86a3ff");
    private static final Color LINE = Color.valueOf("9fd0ffff");

    private final SpriteBatch batch;
    private MenuSpace space;
    private final Texture pixel;
    private final BitmapFont titleFont;
    private final BitmapFont textFont;
    private final GlyphLayout layout = new GlyphLayout();
    private float progressShown;
    private float time;

    public LoadingScreen(SpriteBatch batch) {
        super(batch);
        this.batch = batch;
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(Color.WHITE);
        p.fill();
        pixel = new Texture(p);
        p.dispose();
        // Loaded directly: the asset manager is being refilled while this screen is up.
        titleFont = new BitmapFont(Gdx.files.internal("fonts/college.regular96.fnt"));
        textFont = new BitmapFont(Gdx.files.internal("fonts/college.regular44.fnt"));
        for (BitmapFont f : new BitmapFont[]{titleFont, textFont}) {
            for (com.badlogic.gdx.graphics.g2d.TextureRegion r : f.getRegions()) {
                r.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            }
        }
    }

    @Override
    public void show() {
        progressShown = 0f;
        if (space == null) {
            space = new MenuSpace();
        }
    }

    @Override
    public void createGUI() {
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        time += delta;
        boolean done = Assets.getInstance().getAssetManager().update();
        float progress = Assets.getInstance().getAssetManager().getProgress();
        progressShown += (progress - progressShown) * Math.min(1f, delta * 8f);

        space.update(delta);
        space.render(batch);

        float x = Rules.WORLD_WIDTH * 0.085f;
        float y = Rules.WORLD_HEIGHT * 0.56f;
        float barW = Rules.WORLD_WIDTH * 0.34f;

        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();
        titleFont.setColor(TITLE);
        titleFont.draw(batch, "BURAN-19", x, y);

        String status = ScreenManager.getInstance().getCurrentScreen() == ScreenManager.ScreenType.GAME
                ? "ENTERING SECTOR" : "RETURNING TO BASE";
        int dots = ((int) (time * 3f)) % 4;
        textFont.setColor(DIM);
        layout.setText(textFont, status + "...".substring(0, dots));
        textFont.draw(batch, layout, x, y - 150);

        float barY = y - 230;
        batch.setColor(LINE.r, LINE.g, LINE.b, 0.18f);
        batch.draw(pixel, x, barY, barW, 4);
        batch.setColor(LINE.r, LINE.g, LINE.b, 0.95f);
        batch.draw(pixel, x, barY, barW * MathUtils.clamp(progressShown, 0f, 1f), 4);
        batch.setColor(Color.WHITE);
        batch.end();

        if (done) {
            Assets.getInstance().makeLinks();
            ScreenManager.getInstance().goToTarget();
        }
    }

    @Override
    public void dispose() {
        // Kept for the whole session; the screen manager reuses it between screens.
    }

    @Override
    public void pause() {
    }
}
