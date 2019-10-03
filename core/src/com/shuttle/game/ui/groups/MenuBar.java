package com.shuttle.game.ui.groups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.groups.mvc.profiles.GroupController;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/24/18.
 */
public class MenuBar extends Group {
    private Button buttonExit;
    private Button buttonPlay;
    private Button buttonConfig;
    private Button buttonResults;
    private Button buttonShare;
    private BoomBox boomBox;

    public MenuBar() {
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.boomBox = new BoomBox();
        this.setName(GroupType.MENU_BAR.name());
        this.buttonPlay = new Button(skin, "default");
        this.buttonExit = new Button(skin, "default");
        this.buttonConfig = new Button(skin, "default");
        this.buttonResults = new Button(skin, "default");
        this.buttonShare = new Button(skin, "default");
        this.buttonPlay.setOrigin(Align.center);
        this.buttonExit.setOrigin(Align.center);
        this.buttonConfig.setOrigin(Align.center);
        this.buttonResults.setOrigin(Align.center);
        this.buttonPlay.setSize(128, 128);
        this.buttonExit.setSize(128, 128);
        this.buttonConfig.setSize(128, 128);
        this.buttonResults.setSize(128, 128);
        this.buttonShare.setSize(128, 128);

        buttonPlay.setPosition(22, 22);
        buttonResults.setPosition(249, 22);
        buttonShare.setPosition(476, 22);
        buttonConfig.setPosition(703, 22);
        buttonExit.setPosition(930, 22);

        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
        });

        buttonConfig.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
                        ConfigGroup.getInstance().setNoticeAndAlpha(PaintConstants.UI_STRING_HELPER);
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
        });

        buttonResults.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
        });

        buttonShare.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
        });

        this.addActor(buttonExit);
        this.addActor(buttonPlay);
        this.addActor(buttonConfig);
        this.addActor(buttonResults);
        this.addActor(buttonShare);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        buttonPlay.draw(batch, parentAlpha);
        buttonExit.draw(batch, parentAlpha);
        buttonConfig.draw(batch, parentAlpha);
        buttonResults.draw(batch, parentAlpha);
        buttonShare.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (GroupController.isEmpty()) {
            buttonShare.setDisabled(true);
            buttonPlay.setDisabled(true);
            buttonConfig.setDisabled(true);
            buttonResults.setDisabled(true);
        } else {
            buttonShare.setDisabled(false);
            buttonPlay.setDisabled(false);
            buttonConfig.setDisabled(false);
            buttonResults.setDisabled(false);
        }
    }
}