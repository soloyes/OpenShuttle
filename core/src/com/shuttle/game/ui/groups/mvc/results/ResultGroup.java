package com.shuttle.game.ui.groups.mvc.results;

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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.ui.groups.GroupType;
import com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO;
import com.shuttle.game.ui.groups.mvc.profiles.Profiles;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Rounder;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import lombok.Setter;

/**
 * @author Shuttle on 7/14/18.
 */

public class ResultGroup extends Group {
    private Profiles profiles;
    private VerticalGroup verticalGroup;
    private ScrollPane scrollPane;
    private static ResultGroup instance;
    private GlyphLayout glyphLayout;
    private BitmapFont font;

    public static ResultGroup getInstance() {
        if (instance == null) {
            instance = new ResultGroup();
        }
        return instance;
    }

    private ResultGroup() {
        this.setName(GroupType.RESULTS.name());
        this.profiles = new Profiles();
        this.setVisible(false);
        this.glyphLayout = new GlyphLayout();
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        verticalGroup = new VerticalGroup();
        scrollPane = new ScrollPane(verticalGroup, skin);
        Container<ScrollPane> container = new Container<ScrollPane>(scrollPane);
        container.width(PaintConstants.UI_RESULTS_WIDTH);
        container.height(PaintConstants.UI_RESULTS_HEIGHT);
        container.setPosition(PaintConstants.UI_RESULTS_X, PaintConstants.UI_RESULTS_Y);
        container.align(Align.bottomLeft);

        font = Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class);

        glyphLayout.setText(font, "RESULTS");

        init();

        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setVariableSizeKnobs(true);
        scrollPane.getStyle().vScrollKnob.setMinHeight(100);

        scrollPane.setWidth(PaintConstants.UI_RESULTS_WIDTH);
        scrollPane.setHeight(PaintConstants.UI_RESULTS_HEIGHT);
        this.setWidth(Rules.WORLD_WIDTH);
        this.setHeight(Rules.WORLD_HEIGHT);
        this.addActor(container);
    }

    public void init() {
        verticalGroup.clear();
        int lines = 0;

        Map<ResultDAO, ProfileDAO> map = new TreeMap<ResultDAO, ProfileDAO>(new Comparator<ResultDAO>() {
            @Override
            public int compare(ResultDAO resultDAO1, ResultDAO resultDAO2) {
                return resultDAO2.getDate().compareTo(resultDAO1.getDate());
            }
        });

        for (ProfileDAO p : profiles.getList()) {
            if (!p.getResultSet().isEmpty()) {
                for (final ResultDAO r : p.getResultSet()) {
                    map.put(r, p);
                }
            }
        }
        for (final Map.Entry<ResultDAO, ProfileDAO> m : map.entrySet()) {
            final Line line = new Line(m.getValue(), m.getKey());
            line.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    line.setActive(true);
                    scrollPane.setFlickScroll(false);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    line.setActive(false);
                    scrollPane.setFlickScroll(true);
                    ResultGroup.this.setVisible(false);
                    DetailedResultGroup.getInstance().init(m.getKey());
                }
            });

            verticalGroup.addActor(line);
            lines++;
        }

        if (lines <= PaintConstants.DUMMY_RESULTS) {
            for (int i = 0; i < PaintConstants.DUMMY_RESULTS - lines; i++) {
                verticalGroup.addActor(new Line());
            }
        }
    }

    private static class Line extends Group {
        private Table table;
        private TextureRegion textureRegion;
        @Setter
        private boolean active;
        private static int lines;
        private boolean last;

        public Line(ProfileDAO profileDAO, ResultDAO resultDAO) {
            lines++;
            if (lines == PaintConstants.DUMMY_RESULTS) {
                last = true;
            }
            this.table = new Table();
            this.textureRegion = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_RESULTS_CELL));
            Label.LabelStyle style64 = new Label.LabelStyle(
                    Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                    new Color(0.8f, 0.43f, 0.33f, 1f)
            );
            if (last) {
                table.row();
            } else {
                table.row().padBottom(12);
            }
            makeGroup(new Label(profileDAO.getUsername(), style64), 320);
            makeGroup(new Label(String.valueOf(Rounder.round(resultDAO.getCurrent().getScore())), style64), 138);
            makeGroup(new Label(new SimpleDateFormat(Rules.DATE_FORMAT, Locale.ENGLISH).format(resultDAO.getDate()), style64), 250);
            makeGroup(new Label(resultDAO.getLevel(), style64), 200);

            table.align(Align.bottomLeft);
            table.setFillParent(true);
            table.setWidth(PaintConstants.UI_RESULTS_CELL_WIDTH - 30);
            this.setWidth(PaintConstants.UI_RESULTS_CELL_WIDTH - 30);
            if (last) {
                this.setHeight(82);
            } else {
                this.setHeight(94);
            }
            this.addActor(table);
        }

        public Line() {
            lines++;
            if (lines == PaintConstants.DUMMY_RESULTS) {
                last = true;
            }
            this.table = new Table();
            this.textureRegion = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_RESULTS_CELL_EMPTY));
            if (last) {
                table.row();
            } else {
                table.row().padBottom(12);
            }
            table.align(Align.bottomLeft);
            table.setFillParent(true);
            table.setWidth(PaintConstants.UI_RESULTS_CELL_WIDTH - 30);
            this.setWidth(PaintConstants.UI_RESULTS_CELL_WIDTH - 30);
            if (last) {
                this.setHeight(82);
            } else {
                this.setHeight(94);
            }
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

            if (last) {
                batch.draw(textureRegion, getX(), getY());
            } else {
                batch.draw(textureRegion, getX(), getY() + 12);
            }
            super.draw(batch, parentAlpha);
        }
    }

    public void dispose() {
        instance = null;
    }

    public void switchVisible() {
        Array<Actor> actors = getStage().getActors();
        for (int i = 0; i < actors.size; i++) {
            if (actors.get(i).getName().equals(GroupType.MENU.name())) {
                actors.get(i).setVisible(true);
            } else if (actors.get(i).getName().equals(GroupType.CONFIG.name())) {
                actors.get(i).setVisible(false);
            } else if (actors.get(i).getName().equals(GroupType.SHARE.name())) {
                actors.get(i).setVisible(false);
            } else if (actors.get(i).getName().equals(GroupType.RESULTS_DETAILED.name())) {
                actors.get(i).setVisible(false);
            } else if (actors.get(i).getName().equals(GroupType.RESULTS.name())) {
                actors.get(i).setVisible(false);
            } else if (actors.get(i).getName().equals(GroupType.MENU_BAR.name())) {
                actors.get(i).setVisible(true);
            }
        }
    }

    public void backVisible() {
        Array<Actor> actors = getStage().getActors();
        for (int i = 0; i < actors.size; i++) {
            if (actors.get(i).getName().equals(GroupType.RESULTS_DETAILED.name())) {
                actors.get(i).setVisible(false);
            } else if (actors.get(i).getName().equals(GroupType.RESULTS.name())) {
                actors.get(i).setVisible(true);
            } else if (actors.get(i).getName().equals(GroupType.MENU_BAR.name())) {
                actors.get(i).setVisible(true);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch,
                glyphLayout,
                (Rules.WORLD_WIDTH - glyphLayout.width) / 2,
                PaintConstants.UI_TITLE_LABEL_Y + glyphLayout.height
        );
    }
}