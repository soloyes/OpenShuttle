package com.buran.game.ui.groups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.conf.VConstants;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.Oscillator;
import com.buran.game.effects.Vibrator;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.UIButton;
import com.buran.game.ui.groups.mvc.global.Global;
import com.buran.game.ui.groups.mvc.profiles.ProfilesController;
import com.buran.game.utils.Assets;

/**
 * @author soloyes on 3/24/18.
 */

public class MenuBar extends Group {
    private Button buttonExit;
    private Button buttonPlay;
    private Button buttonSettings;
    private Button buttonResults;
    private Button buttonInfo;
    private BoomBox boomBox;
    private float a;
    private Oscillator oscillatorA;
    private Global global;

    public MenuBar() {
        this.boomBox = new BoomBox();
        this.global = new Global();
        this.setName(GroupType.MENU_BAR.name());
        this.oscillatorA = new Oscillator();
        this.buttonPlay = new UIButton(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.PLAY_BUTTON_MENU)));
        buttonPlay.getStyle().disabled = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LOCK_BUTTON));
        this.buttonExit = new UIButton(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.EXIT_BUTTON_MENU)));
        buttonExit.getStyle().disabled = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LOCK_BUTTON));
        this.buttonSettings = new UIButton(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.SETTINGS_BUTTON_MENU)));
        buttonSettings.getStyle().disabled = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LOCK_BUTTON));
        this.buttonResults = new UIButton(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.RESULTS_BUTTON_MENU)));
        buttonResults.getStyle().disabled = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LOCK_BUTTON));
        this.buttonInfo = new UIButton(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.INFO_BUTTON_MENU)));
        buttonInfo.getStyle().disabled = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LOCK_BUTTON));
        this.buttonPlay.setOrigin(Align.center);
        this.buttonExit.setOrigin(Align.center);
        this.buttonSettings.setOrigin(Align.center);
        this.buttonResults.setOrigin(Align.center);
        this.buttonPlay.setSize(2 * PaintConstants.MENU_BUTTON_WIDTH, 2 * PaintConstants.MENU_BUTTON_HEIGHT);
        this.buttonExit.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);
        this.buttonSettings.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);
        this.buttonResults.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);
        this.buttonInfo.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);


        buttonInfo.setPosition(24, 24);
        buttonResults.setPosition(Rules.WORLD_WIDTH / 4 - 12, 24);

        buttonPlay.setPosition(Rules.WORLD_WIDTH / 2 - PaintConstants.MENU_BUTTON_WIDTH, 24);

        buttonSettings.setPosition(3 * Rules.WORLD_WIDTH / 4 - PaintConstants.MENU_BUTTON_WIDTH + 12, 24);
        buttonExit.setPosition(Rules.WORLD_WIDTH - PaintConstants.MENU_BUTTON_WIDTH - 24, 24);

        buttonExit.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    Gdx.app.exit();
                }
            }
        });

        buttonPlay.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonPlay.isDisabled()) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                } else if (pointer == 0 && !buttonResults.isDisabled()) {
                    if (MenuGroup.isValidUserName()) {
                        boomBox.playSound(MSConstants.UI_MENU);
                        Vibrator.vibrateUI(VConstants.UI_CLICK);
                    } else {
                        boomBox.playSound(MSConstants.UI_EMPTY_NAME);
                        Vibrator.vibrateUI(VConstants.UI_EMPTY_NAME);
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0 && !buttonResults.isDisabled()) {
                    if (MenuGroup.isValidUserName()) {
                        if (Rules.ADS_ENABLED) {
                            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.ADS);
                        } else {
                            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                        }
                    } else {
                        Array<Actor> actors = getStage().getActors();
                        for (int i = 0; i < actors.size; i++) {
                            if (actors.get(i).getName().equals(GroupType.MENU.name())) {
                                actors.get(i).setVisible(true);
                            } else if (actors.get(i).getName().equals(GroupType.MENU_BAR.name())) {
                                actors.get(i).setVisible(true);
                            } else if (actors.get(i).getName().equals(GroupType.MENU_BACKGROUND.name())) {
                                actors.get(i).setVisible(true);
                            } else {
                                actors.get(i).setVisible(false);
                            }
                        }
                    }
                }
            }
        });

        buttonSettings.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonSettings.isDisabled()) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                } else if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0 && !buttonResults.isDisabled()) {
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
                        if (actors.get(i).getName().equals(GroupType.INFO.name())) {
                            actors.get(i).setVisible(false);
                        }
                    }
                }
            }
        });

        buttonResults.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonResults.isDisabled()) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                } else if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                }

                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0 && !buttonResults.isDisabled()) {
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
                        if (actors.get(i).getName().equals(GroupType.INFO.name())) {
                            actors.get(i).setVisible(false);
                        }
                    }
                }
            }
        });

        buttonInfo.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonInfo.isDisabled()) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                } else if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0 && !buttonResults.isDisabled()) {
                    Array<Actor> actors = getStage().getActors();
                    Actor tmp = new Actor();
                    for (int i = 0; i < actors.size; i++) {
                        if (actors.get(i).getName().equals(GroupType.INFO.name())) {
                            tmp = actors.get(i);
                        }
                    }
                    for (int i = 0; i < actors.size; i++) {
                        if (actors.get(i).getName().equals(GroupType.INFO.name())) {
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
                }
            }
        });

        this.addActor(buttonExit);
        this.addActor(buttonPlay);
        this.addActor(buttonSettings);
        this.addActor(buttonResults);
        this.addActor(buttonInfo);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!global.isGlobalStart()) {
            buttonPlay.setPosition(Rules.WORLD_WIDTH / 2 - PaintConstants.MENU_BUTTON_WIDTH, Rules.WORLD_HEIGHT / 2 - PaintConstants.MENU_BUTTON_WIDTH);
            buttonPlay.getColor().a = a;
            buttonPlay.draw(batch, parentAlpha);
        } else {
            super.draw(batch, parentAlpha);
        }
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

        if (!Gdx.input.isTouched()) {
            a = oscillatorA.oscillate(a, 1.0f, 0.3f, 1.0f, delta);
        }
    }
}