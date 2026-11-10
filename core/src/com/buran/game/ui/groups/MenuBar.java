package com.buran.game.ui.groups;

import com.buran.game.ui.menu.MenuItem;
import com.buran.game.input.Gamepad;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    /** Left edge of the menu column, shared with the title. */
    public static float leftX() {
        return Rules.WORLD_WIDTH * 0.085f;
    }
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
        // Vertical text menu on the left (TV layout). Order matches the importance of each entry.
        BitmapFont font = Assets.getInstance().getAssetManager().get(PaintConstants.FONT72, BitmapFont.class);
        this.buttonPlay = new MenuItem("PLAY", font);
        this.buttonResults = new MenuItem("RESULTS", font);
        this.buttonSettings = new MenuItem("SETTINGS", font);
        this.buttonInfo = new MenuItem("INFO", font);
        this.buttonExit = new MenuItem("EXIT", font);

        float x = leftX() - 40f;
        float y = Rules.WORLD_HEIGHT * 0.5f;
        float step = 118f;
        Button[] order = Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.WebGL
                ? new Button[]{buttonPlay, buttonResults, buttonSettings, buttonInfo}
                : new Button[]{buttonPlay, buttonResults, buttonSettings, buttonInfo, buttonExit};
        for (Button b : order) {
            b.setPosition(x, y - b.getHeight() / 2f);
            y -= step;
            Gamepad.addFocusable(b);
        }

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

        // A browser tab cannot close itself (Gdx.app.exit() is a no-op on the web).
        buttonExit.setVisible(Gdx.app.getType() != com.badlogic.gdx.Application.ApplicationType.WebGL);
        this.addActor(buttonExit);
        this.addActor(buttonPlay);
        this.addActor(buttonSettings);
        this.addActor(buttonResults);
        this.addActor(buttonInfo);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
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