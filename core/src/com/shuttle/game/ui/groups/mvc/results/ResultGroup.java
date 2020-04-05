package com.shuttle.game.ui.groups.mvc.results;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.ui.groups.GroupType;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Rounder;

import lombok.Setter;

/**
 * @author Shuttle on 7/14/18.
 */
//todo: to MVC
public class ResultGroup extends VerticalGroup {
    public ResultGroup() {
        this.setName(GroupType.RESULTS.name());
        this.setVisible(false);
        Results results = new Results();
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        VerticalGroup verticalGroup = new VerticalGroup();
        ScrollPane scrollPane = new ScrollPane(verticalGroup, skin);
        Container<ScrollPane> container = new Container<ScrollPane>(scrollPane);
        container.width(PaintConstants.UI_RESULTS_WIDTH);
        container.height(PaintConstants.UI_RESULTS_HEIGHT);
        container.align(Align.top);

        Label.LabelStyle style96 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class),
                Color.WHITE
        );

        Label.LabelStyle style54 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT54, BitmapFont.class),
                Color.WHITE
        );

        Label titleLabel = new Label("RESULTS", style96);
        Label space = new Label("", style54);

        for (ResultDAO r : results.getResults()) {
            if (!r.getUsername().isEmpty()) {
                final Line line = new Line(r);
                line.addListener(new ClickListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        line.setActive(true);
                        return super.touchDown(event, x, y, pointer, button);
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchUp(event, x, y, pointer, button);
                        line.setActive(false);
                    }
                });

                verticalGroup.addActor(line);
            }
        }

        if (results.getResults().size() <= 14) {
            for (int i = 0; i < 14 - results.getResults().size(); i++) {
                verticalGroup.addActor(new Line());
            }
        }

        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setVariableSizeKnobs(true);
        scrollPane.getStyle().vScrollKnob.setMinHeight(100);

        scrollPane.setWidth(PaintConstants.UI_RESULTS_WIDTH);
        scrollPane.setHeight(PaintConstants.UI_RESULTS_HEIGHT);
        this.setPosition(41, PaintConstants.UI_RESULTS_Y);
        this.setWidth(PaintConstants.UI_RESULTS_WIDTH);
        this.setHeight(PaintConstants.UI_RESULTS_HEIGHT);
        this.addActor(titleLabel);
        this.addActor(space);
        this.addActor(container);
    }

    private class Line extends Group {
        private Table table;
        private TextureRegion textureRegion;
        @Setter
        private boolean active;

        public Line(ResultDAO resultDAO) {
            this.table = new Table();
            this.textureRegion = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_RESULTS_CELL));
            Label.LabelStyle style64 = new Label.LabelStyle(
                    Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                    new Color(0.8f, 0.43f, 0.33f, 1f)
            );

            table.row().padBottom(12);
            makeGroup(new Label(resultDAO.getUsername(), style64), 320);
            makeGroup(new Label(Rounder.round(Integer.valueOf(resultDAO.getScore())), style64), 138);
            makeGroup(new Label(resultDAO.getDate(), style64), 250);
            makeGroup(new Label(resultDAO.getLevel(), style64), 240);

            table.align(Align.bottomLeft);
            table.setFillParent(true);
            table.setWidth(PaintConstants.UI_RESULTS_CELL_WIDTH - 30);
            this.setWidth(PaintConstants.UI_RESULTS_CELL_WIDTH - 30);
            this.setHeight(94);
            this.addActor(table);
        }

        public Line() {
            this.table = new Table();
            this.textureRegion = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_RESULTS_CELL_EMPTY));
            table.row().padBottom(12);
            table.align(Align.bottomLeft);
            table.setFillParent(true);
            table.setWidth(PaintConstants.UI_RESULTS_CELL_WIDTH - 30);
            this.setWidth(PaintConstants.UI_RESULTS_CELL_WIDTH - 30);
            this.setHeight(94);
            this.addActor(table);
        }

        private void makeGroup(Label label, int width) {
            label.setFillParent(true);
            Group group = new Group();
            group.setSize(width, PaintConstants.UI_CELL_HEIGHT);
            group.addActor(label);
            table.add(group).left().width(width).padLeft(12);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = batch.getColor();
            if (active) {
                color.a = parentAlpha;
            } else {
                color.a = 0.5f;
            }
            batch.setColor(color);
            batch.draw(textureRegion, getX(), getY() + 12);
            super.draw(batch, parentAlpha);
        }
    }
}