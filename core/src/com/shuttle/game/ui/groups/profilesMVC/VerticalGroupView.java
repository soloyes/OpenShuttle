package com.shuttle.game.ui.groups.profilesMVC;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 17/06/19.
 */

public class VerticalGroupView extends VerticalGroup {
    private final ScrollPane scrollPane;
    private VerticalGroup verticalGroup;
    private VerticalGroupController controller;


    public VerticalGroupView() {
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.verticalGroup = new VerticalGroup();
        this.controller = new VerticalGroupController(this);
        scrollPane = new ScrollPane(verticalGroup, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        //scrollPane.setVariableSizeKnobs(false);
        scrollPane.setVisible(false);
        Container<ScrollPane> container = new Container<ScrollPane>(scrollPane);
        container.setPosition(Rules.WORLD_WIDTH / 2, Rules.WORLD_HEIGHT / 2);
        container.height(Rules.WORLD_HEIGHT / 4);
        container.width(Rules.WORLD_WIDTH / 2);
        container.align(Align.top);
        this.setPosition(Rules.WORLD_WIDTH / 4, Rules.WORLD_HEIGHT / 4);
        this.setWidth(Rules.WORLD_WIDTH / 2);
        this.setHeight(Rules.WORLD_HEIGHT / 4);

        this.addActor(container);
    }

    public void init() {
        verticalGroup.clear();
        verticalGroup.addActor(new Header());
        for (int i = 0; i < Profiles.getProfiles().size(); i++) {
            ProfileDAO profile = Profiles.getProfiles().get(i);
            verticalGroup.addActor(new Line(profile));
        }
    }

    public void switchVisible() {
        scrollPane.setVisible(!scrollPane.isVisible());
    }

    private class Line extends Group {
        private Table table;
        private boolean pressed;
        private boolean confirm;
        private float pressedWait;
        private float deleteWait;
        private ProfileDAO profile;

        @Override
        public void act(float delta) {
            super.act(delta);
            if (pressed) {
                pressedWait += delta;
            }
            if (pressedWait >= Rules.LINE_PRESSED_WAIT) {
                pressedWait = 0.0f;
                pressed = false;
                confirm = true;
            }
            if (confirm) {
                deleteWait += delta;
            }
            if (deleteWait >= Rules.LINE_DELETE_WAIT) {
                deleteWait = 0.0f;
                confirm = false;
                controller.removeProfile(Line.this.profile);
            }
        }

        Line(ProfileDAO profile) {
            this.profile = profile;
            this.table = new Table();
            table.row().padBottom(22).expandX();
            Label.LabelStyle style64 = new Label.LabelStyle(
                    Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                    new Color(0.8f, 0.43f, 0.33f, 1f)
            );
            Label minus = new Label("-", style64);
            minus.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (!confirm) {
                        pressed = true;
                    } else confirm = false;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    pressed = false;
                    deleteWait = 0.0f;
                    pressedWait = 0.0f;
                }
            });

            if (profile.getType() == ProfileDAO.Type.DUMMY) {
                table.add();
                table.add();
                table.add();
            } else {
                table.add(minus);
                table.add(new Label(profile.getUsername(), style64)).center();
                table.add();
            }

            table.align(Align.bottomLeft);
            table.setFillParent(true);
            this.setWidth(Rules.WORLD_WIDTH / 2);
            this.setHeight(90);
            this.addActor(table);
        }
    }

    private class Header extends Group {
        private Table table;

        Header() {
            this.table = new Table();
            table.row().padBottom(22).expandX();
            Label.LabelStyle style64 = new Label.LabelStyle(
                    Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                    new Color(0.8f, 0.43f, 0.33f, 1f)
            );
            Label plus = new Label("+", style64);
            plus.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    controller.addEmptyProfile();
                }
            });
            table.add(plus);
            table.add(new Label("Add New Line", style64)).center();
            table.add();
            table.align(Align.bottomLeft);
            table.setFillParent(true);
            this.setWidth(Rules.WORLD_WIDTH / 2);
            this.setHeight(90);
            this.addActor(table);
        }
    }
}
