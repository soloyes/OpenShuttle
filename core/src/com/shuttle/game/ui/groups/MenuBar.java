package com.shuttle.game.ui.groups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.groups.mvc.profiles.ProfilesController;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/24/18.
 */

public class MenuBar extends Group {
    private Button buttonExit;
    private Button buttonPlay;
    private Button buttonSettings;
    private Button buttonResults;
    private Button buttonInfo;
    private BoomBox boomBox;

    public MenuBar() {
        this.boomBox = new BoomBox();
        this.setName(GroupType.MENU_BAR.name());
        this.buttonPlay = new Button(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.PLAY_BUTTON_MENU)));
        buttonPlay.getStyle().disabled = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LOCK_BUTTON));
        this.buttonExit = new Button(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.EXIT_BUTTON_MENU)));
        buttonExit.getStyle().disabled = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LOCK_BUTTON));
        this.buttonSettings = new Button(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.SETTINGS_BUTTON_MENU)));
        buttonSettings.getStyle().disabled = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LOCK_BUTTON));
        this.buttonResults = new Button(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.RESULTS_BUTTON_MENU)));
        buttonResults.getStyle().disabled = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LOCK_BUTTON));
        this.buttonInfo = new Button(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.INFO_BUTTON_MENU)));
        buttonInfo.getStyle().disabled = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LOCK_BUTTON));
        this.buttonPlay.setOrigin(Align.center);
        this.buttonExit.setOrigin(Align.center);
        this.buttonSettings.setOrigin(Align.center);
        this.buttonResults.setOrigin(Align.center);
        this.buttonPlay.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);
        this.buttonExit.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);
        this.buttonSettings.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);
        this.buttonResults.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);
        this.buttonInfo.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);

        buttonPlay.setPosition(24, 24);
        buttonResults.setPosition(254, 24);
        buttonInfo.setPosition(484, 24);
        buttonSettings.setPosition(714, 24);
        buttonExit.setPosition(944, 24);

        buttonExit.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    Gdx.app.exit();
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        buttonPlay.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonPlay.isDisabled()) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                } else if (pointer == 0) {
                    //Workaround. If use BoomBox.playSound(), then Sound is unload while change Screen
                    if (GamePreferences.isEffectsEnabled()) {
                        Sound sound = Gdx.audio.newSound(Gdx.files.internal(MSConstants.UI_MENU));
                        sound.play(GamePreferences.getOverallEffectsVolume());
                    }
                    //
                    if (MenuGroup.isValidUserName()) {
                        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                    } else {
                        Array<Actor> actors = getStage().getActors();
                        for (int i = 0; i < actors.size; i++) {
                            if (actors.get(i).getName().equals(GroupType.MENU.name())) {
                                actors.get(i).setVisible(true);
                            } else if (actors.get(i).getName().equals(GroupType.MENU_BAR.name())) {
                                actors.get(i).setVisible(true);
                            } else {
                                actors.get(i).setVisible(false);
                            }
                        }
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        buttonSettings.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonSettings.isDisabled()) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                } else if (pointer == 0) {
                    Array<Actor> actors = getStage().getActors();
                    Actor tmp = new Actor();
                    for (int i = 0; i < actors.size; i++) {
                        if (actors.get(i).getName().equals(GroupType.CONFIG.name())) {
                            tmp = actors.get(i);
                        }
                    }
                    for (int i = 0; i < actors.size; i++) {
                        if (actors.get(i).getName().equals(GroupType.CONFIG.name())) {
                            actors.get(i).setVisible(!actors.get(i).isVisible());
                        }
                        if (actors.get(i).getName().equals(GroupType.MENU.name())) {
                            actors.get(i).setVisible(tmp.isVisible());
                        }
                        if (actors.get(i).getName().equals(GroupType.RESULTS.name())) {
                            actors.get(i).setVisible(false);
                        }
                        if (actors.get(i).getName().equals(GroupType.SHARE.name())) {
                            actors.get(i).setVisible(false);
                        }
                    }
                    boomBox.playSound(MSConstants.UI_MENU);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        buttonResults.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonResults.isDisabled()) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                } else if (pointer == 0) {
                    Array<Actor> actors = getStage().getActors();
                    Actor tmp = new Actor();
                    for (int i = 0; i < actors.size; i++) {
                        if (actors.get(i).getName().equals(GroupType.RESULTS.name())) {
                            tmp = actors.get(i);
                        }
                    }
                    for (int i = 0; i < actors.size; i++) {
                        if (actors.get(i).getName().equals(GroupType.RESULTS.name())) {
                            actors.get(i).setVisible(!actors.get(i).isVisible());
                        }
                        if (actors.get(i).getName().equals(GroupType.MENU.name())) {
                            actors.get(i).setVisible(tmp.isVisible());
                        }
                        if (actors.get(i).getName().equals(GroupType.CONFIG.name())) {
                            actors.get(i).setVisible(false);
                        }
                        if (actors.get(i).getName().equals(GroupType.SHARE.name())) {
                            actors.get(i).setVisible(false);
                        }
                    }
                    boomBox.playSound(MSConstants.UI_MENU);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        buttonInfo.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonInfo.isDisabled()) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                } else if (pointer == 0) {
                    Array<Actor> actors = getStage().getActors();
                    Actor tmp = new Actor();
                    for (int i = 0; i < actors.size; i++) {
                        if (actors.get(i).getName().equals(GroupType.SHARE.name())) {
                            tmp = actors.get(i);
                        }
                    }
                    for (int i = 0; i < actors.size; i++) {
                        if (actors.get(i).getName().equals(GroupType.SHARE.name())) {
                            actors.get(i).setVisible(!actors.get(i).isVisible());
                        }
                        if (actors.get(i).getName().equals(GroupType.MENU.name())) {
                            actors.get(i).setVisible(tmp.isVisible());
                        }
                        if (actors.get(i).getName().equals(GroupType.CONFIG.name())) {
                            actors.get(i).setVisible(false);
                        }
                        if (actors.get(i).getName().equals(GroupType.RESULTS.name())) {
                            actors.get(i).setVisible(false);
                        }
                    }
                    boomBox.playSound(MSConstants.UI_MENU);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.addActor(buttonExit);
        this.addActor(buttonPlay);
        this.addActor(buttonSettings);
        this.addActor(buttonResults);
        this.addActor(buttonInfo);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (ProfilesController.isEmpty()) {
            buttonInfo.setDisabled(true);
            buttonPlay.setDisabled(true);
            buttonSettings.setDisabled(true);
            buttonResults.setDisabled(true);
        } else {
            buttonInfo.setDisabled(false);
            buttonPlay.setDisabled(false);
            buttonSettings.setDisabled(false);
            buttonResults.setDisabled(false);
        }
    }
}