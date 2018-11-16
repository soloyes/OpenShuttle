package com.shuttle.game.screens.group;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.ShuttleGame;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Results;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.effects.BoomBox;

import lombok.Getter;

public class ShareGroup extends Group {
    private Table table;
    private Label titleLabel;
    @Getter
    private final Label socialLabel;
    private Label rateLabel;
    private Label aboutLabel;
    private Label introLabel;
    private BoomBox boomBox;
    private Results results;

    public ShareGroup() {
        this.boomBox = new BoomBox();
        this.results = new Results();
        this.setName(GroupType.SHARE.name());
        this.setVisible(false);
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        final Color c = new Color(0.8f, 0.43f, 0.33f, 1f);

        Label.LabelStyle style64 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                c
        );

        Label.LabelStyle style96 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class),
                c
        );

        table = new Table();
        table.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2
        );
        titleLabel = new Label("Shares:", style96);
        socialLabel = new Label("Share Results", style64);
        rateLabel = new Label("Rate Shuttle", style64);
        aboutLabel = new Label("About Us", style64);
        introLabel = new Label("Intro", style64);

        float total = 20;
        titleLabel.setPosition(Rules.WORLD_WIDTH / 2 - titleLabel.getWidth() / 2, Rules.WORLD_HEIGHT / 2);
        total += titleLabel.getHeight();
        socialLabel.setPosition(Rules.WORLD_WIDTH / 2 - socialLabel.getWidth() / 2, Rules.WORLD_HEIGHT / 2 - total);
        total += socialLabel.getHeight();
        rateLabel.setPosition(Rules.WORLD_WIDTH / 2 - rateLabel.getWidth() / 2, Rules.WORLD_HEIGHT / 2 - total);
        total += rateLabel.getHeight();
        aboutLabel.setPosition(Rules.WORLD_WIDTH / 2 - aboutLabel.getWidth() / 2, Rules.WORLD_HEIGHT / 2 - total);
        total += aboutLabel.getHeight();
        introLabel.setPosition(Rules.WORLD_WIDTH / 2 - introLabel.getWidth() / 2, Rules.WORLD_HEIGHT / 2 - total);


        socialLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                boomBox.playSound(MSConstants.BUTTON_SOUND);
                ShuttleGame.getShare().share(results.getTopResults());
                float scale = 0.9f;
                socialLabel.setFontScale(scale);
                socialLabel.setX(Rules.WORLD_WIDTH / 2 - scale * socialLabel.getWidth() / 2);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                socialLabel.setFontScale(1.0f);
                socialLabel.setX(Rules.WORLD_WIDTH / 2 - socialLabel.getWidth() / 2);
            }
        });

        rateLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                boomBox.playSound(MSConstants.BUTTON_SOUND);
                rateLabel.setFontScale(0.9f);
                float scale = 0.9f;
                rateLabel.setFontScale(scale);
                rateLabel.setX(Rules.WORLD_WIDTH / 2 - scale * rateLabel.getWidth() / 2);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rateLabel.setFontScale(1.0f);
                rateLabel.setX(Rules.WORLD_WIDTH / 2 - rateLabel.getWidth() / 2);

            }
        });

        aboutLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                boomBox.playSound(MSConstants.BUTTON_SOUND);
                aboutLabel.setFontScale(0.9f);
                float scale = 0.9f;
                aboutLabel.setFontScale(scale);
                aboutLabel.setX(Rules.WORLD_WIDTH / 2 - scale * aboutLabel.getWidth() / 2);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                aboutLabel.setFontScale(1.0f);
                aboutLabel.setX(Rules.WORLD_WIDTH / 2 - aboutLabel.getWidth() / 2);
            }
        });

        introLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                boomBox.playSound(MSConstants.BUTTON_SOUND);
                introLabel.setFontScale(0.9f);
                float scale = 0.9f;
                introLabel.setFontScale(scale);
                introLabel.setX(Rules.WORLD_WIDTH / 2 - scale * introLabel.getWidth() / 2);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                introLabel.setFontScale(1.0f);
                introLabel.setX(Rules.WORLD_WIDTH / 2 - introLabel.getWidth() / 2);
            }
        });

        this.addActor(socialLabel);
        this.addActor(rateLabel);
        this.addActor(aboutLabel);
        this.addActor(introLabel);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        titleLabel.draw(batch, parentAlpha);
        socialLabel.draw(batch, parentAlpha);
        rateLabel.draw(batch, parentAlpha);
        aboutLabel.draw(batch, parentAlpha);
        introLabel.draw(batch, parentAlpha);
    }
}
