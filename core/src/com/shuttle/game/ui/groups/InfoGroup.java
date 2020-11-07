package com.shuttle.game.ui.groups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.ShuttleGame;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.ui.groups.mvc.results.Results;
import com.shuttle.game.utils.Assets;

import lombok.Setter;

/**
 * @author soloyes on 03/04/20.
 */

public class InfoGroup extends Group {
    private GlyphLayout glyphLayout;
    private BoomBox boomBox;
    private Results results;
    private BitmapFont font96;

    public InfoGroup() {
        this.setName(GroupType.INFO.name());
        Table table = new Table();
        this.boomBox = new BoomBox();
        this.results = new Results();
        this.setVisible(false);
        Color color = new Color(0.8f, 0.43f, 0.33f, 1f);
        Label.LabelStyle style64 = new Label.LabelStyle(Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class), color);
        font96 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class);
        glyphLayout = new GlyphLayout(font96, "INFO");
        Label shareLabel = new Label("Share Results", style64);
        Label rateLabel = new Label("Rate Buran", style64);
        Label aboutLabel = new Label("About Us", style64);
        Label rulesLabel = new Label("Rules", style64);
        Container<Table> container = new Container<Table>(table);
        container.width(PaintConstants.UI_MENU_CELL_WIDTH);
        container.height(Rules.WORLD_HEIGHT / 2);
        container.setPosition(Rules.WORLD_WIDTH / 2 - PaintConstants.UI_MENU_CELL_WIDTH / 2, 2 * Rules.WORLD_HEIGHT / 3);
        container.align(Align.topLeft);
        table.align(Align.topLeft);

        final Line shareLine = new Line(shareLabel);
        table.add(shareLine).width(PaintConstants.UI_MENU_CELL_WIDTH).height(PaintConstants.UI_CELL_HEIGHT).padBottom(12).align(Align.center);
        table.row();
        final Line rateLine = new Line(rateLabel);
        table.add(rateLine).width(PaintConstants.UI_MENU_CELL_WIDTH).height(PaintConstants.UI_CELL_HEIGHT).padBottom(12).align(Align.center);
        table.row();
        final Line aboutLine = new Line(aboutLabel);
        table.add(aboutLine).width(PaintConstants.UI_MENU_CELL_WIDTH).height(PaintConstants.UI_CELL_HEIGHT).padBottom(12).align(Align.center);
        table.row();
        final Line rulesLine = new Line(rulesLabel);
        table.add(rulesLine).width(PaintConstants.UI_MENU_CELL_WIDTH).height(PaintConstants.UI_CELL_HEIGHT).padBottom(12).align(Align.center);

        shareLine.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                    Vibrator.vibrate(VConstants.UI_CLICK);
                    shareLine.setTouched(true);
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    shareLine.setTouched(false);
                    ShuttleGame.getShare().share(results.shareResults());
                }
            }
        });

        rateLine.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    rateLine.setTouched(true);
                    Vibrator.vibrate(VConstants.UI_CLICK);
                    boomBox.playSound(MSConstants.UI_MENU);
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    rateLine.setTouched(false);
                    Gdx.net.openURI(Rules.RATE_US);
                }
            }
        });

        aboutLine.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    aboutLine.setTouched(true);
                    Vibrator.vibrate(VConstants.UI_CLICK);
                    boomBox.playSound(MSConstants.UI_MENU);
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    aboutLine.setTouched(false);
                    Gdx.net.openURI(Rules.ABOUT_US);
                }
            }
        });

        rulesLine.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    rulesLine.setTouched(true);
                    Vibrator.vibrate(VConstants.UI_CLICK);
                    boomBox.playSound(MSConstants.UI_MENU);
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    setVisible(false);
                    Array<Actor> actors = getStage().getActors();
                    for (int i = 0; i < actors.size; i++) {
                        if (actors.get(i).getName().equals(GroupType.RULES.name())) {
                            actors.get(i).setVisible(true);
                        }
                        if (actors.get(i).getName().equals(GroupType.MENU_BAR.name())) {
                            actors.get(i).setVisible(false);
                        }
                    }
                }
                rulesLine.setTouched(false);
            }
        });

        this.setWidth(Rules.WORLD_WIDTH);
        this.setHeight(Rules.WORLD_HEIGHT);
        this.addActor(container);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font96.draw(batch,
                glyphLayout,
                (Rules.WORLD_WIDTH - glyphLayout.width) / 2,
                2 * Rules.WORLD_HEIGHT / 3 + 2 * glyphLayout.height
        );
    }

    private class Line extends Group {
        private TextureRegion textureRegion;
        @Setter
        private boolean touched;

        public Line(Label label) {
            this.textureRegion = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_INFO_CELL));
            this.setSize(PaintConstants.UI_MENU_CELL_WIDTH, PaintConstants.UI_CELL_HEIGHT);
            this.addActor(label);
            label.setHeight(PaintConstants.UI_CELL_HEIGHT);
            label.setPosition(getWidth() / 2 - label.getWidth() / 2, 0);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = batch.getColor();
            if (!touched) {
                color.a = 0.5f;
            }
            batch.setColor(color);
            batch.draw(textureRegion, getX(), getY());
            color.a = parentAlpha;
            batch.setColor(color);
            super.draw(batch, parentAlpha);
        }
    }
}