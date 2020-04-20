package com.shuttle.game.ui.groups.mvc.profiles;

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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.ui.groups.MenuGroup;
import com.shuttle.game.ui.groups.mvc.Controller;
import com.shuttle.game.ui.groups.mvc.View;
import com.shuttle.game.ui.groups.mvc.results.ResultGroup;
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

public class ProfilesView extends VerticalGroup implements View<ProfileDAO> {
    private Controller<ProfileDAO> controller;
    private final ScrollPane scrollPane;
    private VerticalGroup verticalGroup;
    private Map<String, Line> currentLines;
    private Map<String, Line> previousLines;
    private List<String> id;
    private BoomBox boomBox;

    public ProfilesView() {
        this.boomBox = new BoomBox();
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.currentLines = new HashMap<String, Line>();
        this.previousLines = new HashMap<String, Line>();
        this.id = new ArrayList<String>();
        this.verticalGroup = new VerticalGroup();
        scrollPane = new ScrollPane(verticalGroup, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(false, false);
        //scrollPane.setVariableSizeKnobs(false);
        scrollPane.setVisible(false);
        Container<ScrollPane> container = new Container<ScrollPane>(scrollPane);
        container.setPosition(Rules.WORLD_WIDTH / 2, Rules.WORLD_HEIGHT / 2);
        container.height(Rules.WORLD_HEIGHT / 4);
        container.width(Rules.WORLD_WIDTH / 2);
        container.align(Align.top);
        this.setPosition(Rules.WORLD_WIDTH / 4, 430);
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
            Line line;
            if (i == controller.getList().size() - 1) {
                line = new Line(profile, true);
            } else {
                line = new Line(profile, false);
            }
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
                line.getTimeAndFlag()
                        .copy(previousLines.get(id.get(i)).getTimeAndFlag());
                line.push();
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
        private TextureRegion background;
        private TextureRegion active;
        private ProfilesCheckBox minus;
        private boolean last;

        @Override
        public void act(float delta) {
            super.act(delta);
            if (timeAndFlag.isPressed()) {
                timeAndFlag.addPressedWait(delta);
                minus.setPressedWait(timeAndFlag.getPressedWait());
            }
            if (timeAndFlag.getPressedWait() >= Rules.LINE_PRESSED_WAIT) {
                timeAndFlag.setPressedWait(0.0f);
                minus.setPressedWait(0.0f);
                minus.setConfirm(true);
                timeAndFlag.setPressed(false);
                timeAndFlag.setConfirm(true);
                MenuGroup.setNotice(PaintConstants.UI_DELETION_PROCESS);
            }
            if (timeAndFlag.isConfirm()) {
                timeAndFlag.addDeleteWait(delta);
                minus.setDeleteWait(timeAndFlag.getDeleteWait());
            }
            if (timeAndFlag.getDeleteWait() >= Rules.LINE_DELETE_WAIT) {
                timeAndFlag.setDeleteWait(0.0f);
                timeAndFlag.setConfirm(false);
                minus.setDeleteWait(0.0f);
                minus.setPressedWait(0.0f);
                minus.setConfirm(false);
                controller.remove(Line.this.profile);
                ResultGroup.getInstance().init();
                MenuGroup.setNotice(PaintConstants.UI_PROFILE_DELETED);
                boomBox.playSound(MSConstants.UI_CHANGED);
                Vibrator.vibrate(VConstants.UI_CHANGED);
            }
        }

        Line(final ProfileDAO profile, boolean last) {
            this.last = last;
            this.timeAndFlag = new TimeAndFlag();
            this.background = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_CELL));
            this.active = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_CELL_ACTIVE));
            this.profile = profile;
            this.table = new Table();
            if (last) {
                table.row();
            } else {
                table.row().padBottom(12);
            }
            Label.LabelStyle style64 = new Label.LabelStyle(
                    Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                    new Color(0.8f, 0.43f, 0.33f, 1f)
            );

            minus = new ProfilesCheckBox(
                    "",
                    new ProfilesCheckBoxStyle(
                            new TextureRegionDrawable(new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_MINUS))),
                            new TextureRegionDrawable(new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_MINUS)))
                    )
            );

            minus.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (MenuGroup.isValidUserName()) {
                        if (!timeAndFlag.isConfirm()) {
                            timeAndFlag.setPressed(true);
                            MenuGroup.setNotice(PaintConstants.UI_PROFILE_DEL);
                        } else {
                            timeAndFlag.setConfirm(false);
                            minus.setConfirm(false);
                            MenuGroup.setNotice(PaintConstants.UI_CANCEL_DELETION);
                        }
                        boomBox.playSound(MSConstants.UI_CLICK);
                        Vibrator.vibrate(VConstants.UI_CLICK);
                    }
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    timeAndFlag.setPressed(false);
                    timeAndFlag.setPressedWait(0.0f);
                    minus.setPressedWait(0.0f);
                    if (!timeAndFlag.isConfirm()) {
                        timeAndFlag.setDeleteWait(0.0f);
                        minus.setDeleteWait(0.0f);
                    }
                }
            });

            if (profile.getType() == ProfileDAO.Type.DUMMY) {
                table.add().width(82).height(PaintConstants.UI_CELL_HEIGHT);
                table.add();
                table.add();
            } else {
                table.add(minus).width(82).height(PaintConstants.UI_CELL_HEIGHT);
                Label label = new Label(profile.getUsername(), style64);
                label.setFillParent(true);
                Group group = new Group();
                group.setSize(PaintConstants.UI_MENU_CELL_WIDTH - PaintConstants.UI_CELL_WIDTH, PaintConstants.UI_CELL_HEIGHT);
                group.addActor(label);
                label.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (MenuGroup.isValidUserName()) {
                            controller.select(profile);
                            MenuGroup.setNotice(PaintConstants.UI_SELECT_PROFILE);
                            boomBox.playSound(MSConstants.UI_CHANGED);
                            Vibrator.vibrate(VConstants.UI_CHANGED);
                        }
                    }
                });
                table.add(group).width(PaintConstants.UI_MENU_CELL_WIDTH - PaintConstants.UI_CELL_WIDTH).height(PaintConstants.UI_CELL_HEIGHT).padLeft(12);
                table.add();
            }

            table.align(Align.bottomLeft);
            table.setFillParent(true);
            this.setWidth(PaintConstants.UI_MENU_CELL_WIDTH);
            if (last) {
                this.setHeight(PaintConstants.UI_CELL_HEIGHT);
            } else {
                this.setHeight(PaintConstants.UI_CELL_HEIGHT + 12);
            }
            this.addActor(table);
        }

        public void push() {
            minus.setConfirm(timeAndFlag.isConfirm());
            minus.setDeleteWait(timeAndFlag.getDeleteWait());
            minus.setPressedWait(timeAndFlag.getPressedWait());
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = batch.getColor();
            if (!profile.isActive()) {
                color.a = 0.3f;
            } else {
                color.a = parentAlpha;
            }
            batch.setColor(color);

            if (profile.isActive()) {
                batch.draw(active, getX(), getY() + (last ? 0 : 12));
            } else {
                batch.draw(background, getX(), getY() + (last ? 0 : 12));
            }
            super.draw(batch, parentAlpha);
        }
    }

    private class Header extends Group {
        private Table table;
        private TextureRegion textureRegion;

        Header() {
            this.table = new Table();
            this.textureRegion = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_CELL_HEAD));
            table.row().padBottom(12);
            Label.LabelStyle style64 = new Label.LabelStyle(
                    Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                    new Color(0.8f, 0.43f, 0.33f, 1f)
            );

            ProfilesCheckBox plus = new ProfilesCheckBox(
                    "",
                    new ProfilesCheckBoxStyle(
                            new TextureRegionDrawable(new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_PLUS))),
                            new TextureRegionDrawable(new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_PLUS)))
                    )
            );

            Label label = new Label(PaintConstants.UI_ADD_NEW_LINE, style64);
            label.setFillParent(true);
            Group group = new Group();
            group.setSize(PaintConstants.UI_MENU_CELL_WIDTH - PaintConstants.UI_CELL_WIDTH, PaintConstants.UI_CELL_HEIGHT);
            group.addActor(label);
            plus.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                    Vibrator.vibrate(VConstants.UI_CLICK);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (MenuGroup.isValidUserName()) {
                        controller.add();
                        MenuGroup.setNotice(PaintConstants.UI_NEW_PROFILE);
                    }
                }
            });
            group.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                    Vibrator.vibrate(VConstants.UI_CLICK);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (MenuGroup.isValidUserName()) {
                        controller.add();
                        MenuGroup.setNotice(PaintConstants.UI_NEW_PROFILE);
                    }
                }
            });

            table.add(plus).width(82).height(PaintConstants.UI_CELL_HEIGHT);
            table.add(group).width(PaintConstants.UI_MENU_CELL_WIDTH - PaintConstants.UI_CELL_WIDTH).height(PaintConstants.UI_CELL_HEIGHT).padLeft(12);
            table.add();
            table.align(Align.bottomLeft);
            table.setFillParent(true);
            this.setWidth(PaintConstants.UI_MENU_CELL_WIDTH);
            this.setHeight(PaintConstants.UI_CELL_HEIGHT + 12);
            this.addActor(table);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(textureRegion, getX(), getY() + 12);
            super.draw(batch, parentAlpha);
        }
    }

    public ProfileDAO addProfile(String username) {
        setVisible(true);
        return controller.add(username);
    }
}