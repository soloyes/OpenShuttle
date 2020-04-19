package com.shuttle.game.ui.groups.mvc.results;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.UIButton;
import com.shuttle.game.ui.groups.GroupType;
import com.shuttle.game.ui.groups.mvc.profiles.Profiles;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Rounder;
import com.shuttle.game.utils.TimeFormat;

/**
 * @author Shuttle on 11/04/18.
 */

public class DetailedResultGroup extends Group {
    private VerticalGroup verticalGroup;
    private static DetailedResultGroup instance;
    private Button buttonBack;
    private BoomBox boomBox;
    private Profiles profiles;
    private GlyphLayout glyphLayout;
    private BitmapFont font;

    public static DetailedResultGroup getInstance() {
        if (instance == null) {
            instance = new DetailedResultGroup();
        }
        return instance;
    }

    private DetailedResultGroup() {
        this.setName(GroupType.RESULTS_DETAILED.name());
        this.setVisible(false);
        this.boomBox = new BoomBox();
        this.profiles = new Profiles();
        this.glyphLayout = new GlyphLayout();
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        verticalGroup = new VerticalGroup();
        ScrollPane scrollPane = new ScrollPane(verticalGroup, skin);
        Container<ScrollPane> container = new Container<ScrollPane>(scrollPane);
        container.width(PaintConstants.UI_RESULTS_DETAILED_WIDTH);
        container.height(PaintConstants.UI_RESULTS_DETAILED_HEIGHT);
        container.setPosition(PaintConstants.UI_RESULTS_DETAILED_X, PaintConstants.UI_RESULTS_DETAILED_Y);
        container.align(Align.bottomLeft);

        this.font = Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class);
        glyphLayout.setText(font, "DETAILS");

        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setVariableSizeKnobs(true);
        scrollPane.getStyle().vScrollKnob.setMinHeight(100);

        scrollPane.setWidth(PaintConstants.UI_RESULTS_DETAILED_WIDTH);
        scrollPane.setHeight(PaintConstants.UI_RESULTS_DETAILED_HEIGHT);

        this.setWidth(Rules.WORLD_WIDTH);
        this.setHeight(Rules.WORLD_HEIGHT);
        this.addActor(container);
        if (ScreenManager.getInstance().getCurrentScreen() == ScreenManager.ScreenType.MENU) {
            this.buttonBack = new UIButton(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.BACK_BUTTON)));
            buttonBack.setPosition(484, 24);
            buttonBack.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (pointer == 0) {
                        boomBox.playSound(MSConstants.UI_MENU);
                        Vibrator.vibrate(VConstants.UI_CLICK);
                    }
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (pointer == 0) {
                        Array<Actor> actors = getStage().getActors();
                        for (int i = 0; i < actors.size; i++) {
                            if (actors.get(i).getName().equals(GroupType.RESULTS.name())) {
                                actors.get(i).setVisible(true);
                            }
                            if (actors.get(i).getName().equals(GroupType.MENU_BAR.name())) {
                                actors.get(i).setVisible(true);
                            }
                        }
                        setVisible(false);
                    }
                }
            });
            this.addActor(buttonBack);
        }
    }

    public void init() {
        ResultDAO resultDAO = profiles.getActiveProfileResultList(Rules.StartLevel.values()[Profiles.getSettings().getLevel()].name()).get(0);
        init(resultDAO);
    }

    public void init(ResultDAO resultDAO) {
        verticalGroup.clear();
        Line.lines = 0;

        Line effectivenessLine = new Line(
                "Effectiveness",
                String.valueOf(resultDAO.getCurrent().getEffectiveness()),
                resultDAO.getPrevious() == null ? 1 :
                        Integer.compare(resultDAO.getCurrent().getEffectiveness(), resultDAO.getPrevious().getEffectiveness())
        );
        verticalGroup.addActor(effectivenessLine);
        long score = resultDAO.getCurrent().getScore();
        Line scoreLine = new Line(
                "Score",
                String.valueOf(score <= Integer.MAX_VALUE ? String.valueOf(score) : String.valueOf(Rounder.round(score))),
                resultDAO.getPrevious() == null ? 1 :
                        Long.compare(resultDAO.getCurrent().getScore(), resultDAO.getPrevious().getScore())
        );
        verticalGroup.addActor(scoreLine);
        Line timeLine = new Line(
                "Time",
                TimeFormat.format(resultDAO.getCurrent().getTime()),
                resultDAO.getPrevious() == null ? 1 :
                        Integer.compare(resultDAO.getCurrent().getTime(), resultDAO.getPrevious().getTime())
        );
        verticalGroup.addActor(timeLine);
        Line dmgDoneLine = new Line(
                "Dmg done",
                String.valueOf(resultDAO.getCurrent().getDmgDone()),
                resultDAO.getPrevious() == null ? 1 :
                        Integer.compare(resultDAO.getCurrent().getDmgDone(), resultDAO.getPrevious().getDmgDone())
        );
        verticalGroup.addActor(dmgDoneLine);
        Line dmgReceivedLine = new Line(
                "Dmg received",
                String.valueOf(resultDAO.getCurrent().getDmgReceived()),
                resultDAO.getPrevious() == null ? 1 :
                        Integer.compare(resultDAO.getCurrent().getDmgReceived(), resultDAO.getPrevious().getDmgReceived())
        );
        verticalGroup.addActor(dmgReceivedLine);
        Line enemiesDestroyedLine = new Line(
                "Enemies destroyed",
                String.valueOf(resultDAO.getCurrent().getEnemiesDestroyed()),
                resultDAO.getPrevious() == null ? 1 :
                        Integer.compare(resultDAO.getCurrent().getEnemiesDestroyed(), resultDAO.getPrevious().getEnemiesDestroyed())
        );
        verticalGroup.addActor(enemiesDestroyedLine);
        Line minedMeteoriteLine = new Line(
                "Mined meteorite",
                String.valueOf(resultDAO.getCurrent().getMinedMeteorite()),
                resultDAO.getPrevious() == null ? 1 :
                        Integer.compare(resultDAO.getCurrent().getMinedMeteorite(), resultDAO.getPrevious().getMinedMeteorite())
        );
        verticalGroup.addActor(minedMeteoriteLine);
        Line pickUpItemsLine = new Line(
                "Pick up items",
                String.valueOf(resultDAO.getCurrent().getPickUpItems()),
                resultDAO.getPrevious() == null ? 1 :
                        Integer.compare(resultDAO.getCurrent().getPickUpItems(), resultDAO.getPrevious().getPickUpItems())
        );
        verticalGroup.addActor(pickUpItemsLine);
        Line usedItemsLine = new Line(
                "Used items",
                String.valueOf(resultDAO.getCurrent().getUsedItems()),
                resultDAO.getPrevious() == null ? 1 :
                        Integer.compare(resultDAO.getCurrent().getUsedItems(), resultDAO.getPrevious().getUsedItems())
        );
        verticalGroup.addActor(usedItemsLine);
        Line astronautsTakenLine = new Line(
                "Astronauts taken",
                String.valueOf(resultDAO.getCurrent().getAstronautsTaken()),
                resultDAO.getPrevious() == null ? 1 :
                        Integer.compare(resultDAO.getCurrent().getAstronautsTaken(), resultDAO.getPrevious().getAstronautsTaken())
        );
        verticalGroup.addActor(astronautsTakenLine);

        setVisible(true);
    }

    public void setOver() {
        setVisible(true);
        glyphLayout.setText(font, "GAME OVER");
    }

    private static class Line extends Group {
        private Table table;
        private TextureRegion textureRegion;
        private TextureRegion sideRegion;
        private Color color;
        private static int lines;
        private boolean last;

        public Line(String item, String value, int side) {
            lines++;
            if (lines == PaintConstants.RESULTS_DETAILED) {
                last = true;
            }
            this.table = new Table();
            this.textureRegion = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_RESULTS_CELL_DETAILED));
            if (side < 0) {
                this.sideRegion = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_RESULTS_CELL_DETAILED_DOWN));
                color = Color.valueOf("ed7616ff");
            } else if (side == 0) {
                this.sideRegion = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_RESULTS_CELL_DETAILED_EQUAL));
                color = Color.valueOf("ebff22ff");
            } else {
                this.sideRegion = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_RESULTS_CELL_DETAILED_UP));
                color = Color.valueOf("7ea000ff");
            }

            Label.LabelStyle style64 = new Label.LabelStyle(Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class), color);
            Label.LabelStyle style64White = new Label.LabelStyle(Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class), Color.WHITE);

            CheckBox checkBox = new CheckBox("", new ResultsCheckBoxStyle(
                    new TextureRegionDrawable(sideRegion),
                    new TextureRegionDrawable(sideRegion))
            );
            if (last) {
                table.row();
            } else {
                table.row().padBottom(12);
            }
            Group group = new Group();
            group.setSize(82, PaintConstants.UI_CELL_HEIGHT);
            group.addActor(checkBox);
            table.add(group).left().width(82);
            makeGroup(new Label(item, style64White));
            makeGroup(new Label(value, style64));

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

        private void makeGroup(Label label) {
            label.setFillParent(true);
            Group group = new Group();
            group.setSize(425, PaintConstants.UI_CELL_HEIGHT);
            group.addActor(label);
            table.add(group).left().width(425).padLeft(12);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if (last) {
                batch.draw(textureRegion, getX(), getY());
            } else {
                batch.draw(textureRegion, getX(), getY() + 12);
            }
            super.draw(batch, parentAlpha);
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

    public void dispose() {
        instance = null;
    }

    public static void doStart() {
        instance = null;
    }
}