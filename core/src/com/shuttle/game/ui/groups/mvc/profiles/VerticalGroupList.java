package com.shuttle.game.ui.groups.mvc.profiles;

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
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.ui.groups.MenuGroup;
import com.shuttle.game.ui.groups.mvc.Controller;
import com.shuttle.game.ui.groups.mvc.View;
import com.shuttle.game.utils.Assets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 17/06/19.
 */

//todo: Add, remove and cancel sounds
public class VerticalGroupList extends VerticalGroup implements View<ProfileDAO> {
    private com.shuttle.game.ui.groups.mvc.Controller<ProfileDAO> controller;
    private final ScrollPane scrollPane;
    private VerticalGroup verticalGroup;
    private Map<String, Line> currentLines;
    private Map<String, Line> previousLines;
    private List<String> id;
    private BoomBox boomBox;

    public VerticalGroupList() {
        this.boomBox = new BoomBox();
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.currentLines = new HashMap<String, Line>();
        this.previousLines = new HashMap<String, Line>();
        this.id = new ArrayList<String>();
        this.verticalGroup = new VerticalGroup();
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

    @Override
    public void setController(Controller<ProfileDAO> controller) {
        this.controller = controller;
    }

    @Override
    public void init() {
        verticalGroup.clear();
        verticalGroup.addActor(new Header());
        boolean hasActive = false;
        for (int i = 0; i < controller.getList().size(); i++) {
            ProfileDAO profile = controller.getList().get(i);
            Line line = new Line(profile);
            currentLines.put(profile.getId(), line);
            if (profile.isActive()) {
                MenuGroup.setUsername(profile.getUsername());
                hasActive = true;
            }
            if (!hasActive) {
                MenuGroup.setUsername("");
            }
            verticalGroup.addActor(line);
        }
        id.clear();
        id.addAll(previousLines.keySet());
        for (int i = 0; i < id.size(); i++) {
            Line line = currentLines.get(id.get(i));
            if (line != null) {
                line
                        .getTimeAndFlag()
                        .copy(previousLines.get(id.get(i)).getTimeAndFlag());
            }
        }
        previousLines.clear();
        previousLines.putAll(currentLines);
        currentLines.clear();
    }

    public void switchVisible() {
        scrollPane.setVisible(!scrollPane.isVisible());
    }

    private class TimeAndFlag {
        @Getter
        @Setter
        private boolean pressed;
        @Getter
        @Setter
        private boolean confirm;
        @Getter
        @Setter
        private float pressedWait;
        @Getter
        @Setter
        private float deleteWait;

        void addPressedWait(float amount) {
            pressedWait += amount;
        }

        void addDeleteWait(float amount) {
            deleteWait += amount;
        }

        public void copy(TimeAndFlag timeAndFlag) {
            this.confirm = timeAndFlag.confirm;
            this.pressed = timeAndFlag.pressed;
            this.pressedWait = timeAndFlag.pressedWait;
            this.deleteWait = timeAndFlag.deleteWait;
        }
    }

    private class Line extends Group {
        private Table table;
        private final ProfileDAO profile;
        @Getter
        private TimeAndFlag timeAndFlag;

        @Override
        public void act(float delta) {
            super.act(delta);
            if (timeAndFlag.isPressed()) {
                timeAndFlag.addPressedWait(delta);
            }
            if (timeAndFlag.getPressedWait() >= Rules.LINE_PRESSED_WAIT) {
                timeAndFlag.setPressedWait(0.0f);
                timeAndFlag.setPressed(false);
                timeAndFlag.setConfirm(true);
                MenuGroup.setNotice(PaintConstants.UI_DELETION_PROCESS);
            }
            if (timeAndFlag.isConfirm()) {
                timeAndFlag.addDeleteWait(delta);
            }
            if (timeAndFlag.getDeleteWait() >= Rules.LINE_DELETE_WAIT) {
                timeAndFlag.setDeleteWait(0.0f);
                timeAndFlag.setConfirm(false);
                controller.remove(Line.this.profile);
                MenuGroup.setNotice(PaintConstants.UI_PROFILE_DELETED);
                boomBox.playSound(MSConstants.UI_CHANGED);
            }
        }

        Line(final ProfileDAO profile) {
            this.timeAndFlag = new TimeAndFlag();
            this.profile = profile;
            this.table = new Table();
            //todo: padding to PaintConstants
            table.row().padBottom(22).expandX();
            Label.LabelStyle style64 = new Label.LabelStyle(
                    Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                    new Color(0.8f, 0.43f, 0.33f, 1f)
            );
            Label minus = new Label("-", style64);
            minus.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (MenuGroup.isValidUserName()) {
                        if (!timeAndFlag.isConfirm()) {
                            timeAndFlag.setPressed(true);
                            MenuGroup.setNotice(PaintConstants.UI_PROFILE_DEL);
                        } else {
                            timeAndFlag.setConfirm(false);
                            MenuGroup.setNotice(PaintConstants.UI_CANCEL_DELETION);
                        }
                        boomBox.playSound(MSConstants.UI_CLICK);
                    }
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    timeAndFlag.setPressed(false);
                    timeAndFlag.setDeleteWait(0.0f);
                    timeAndFlag.setPressedWait(0.0f);
                }
            });

            if (profile.getType() == ProfileDAO.Type.DUMMY) {
                table.add();
                table.add();
                table.add();
            } else {
                table.add(minus);
                Label label = new Label(profile.getUsername(), style64);
                label.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (MenuGroup.isValidUserName()) {
                            controller.select(profile);
                            MenuGroup.setNotice(PaintConstants.UI_SELECT_PROFILE);
                            boomBox.playSound(MSConstants.UI_CHANGED);
                        } else {
                            //todo: block sound
                        }
                    }
                });
                table.add(label).center();
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
                    if (MenuGroup.isValidUserName()) {
                        controller.add();
                        MenuGroup.setNotice(PaintConstants.UI_NEW_PROFILE);
                    }
                    boomBox.playSound(MSConstants.UI_CLICK);
                }
            });
            table.add(plus);
            table.add(new Label(PaintConstants.UI_ADD_NEW_LINE, style64)).center();
            table.add();
            table.align(Align.bottomLeft);
            table.setFillParent(true);
            this.setWidth(Rules.WORLD_WIDTH / 2);
            this.setHeight(90);
            this.addActor(table);
        }
    }

    public ProfileDAO addProfile(String username) {
        setVisible(true);
        return controller.add(username);
    }
}
