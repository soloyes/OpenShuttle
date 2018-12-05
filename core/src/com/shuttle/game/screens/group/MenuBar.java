package com.shuttle.game.screens.group;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Results;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.effects.BoomBox;

/**
 * @author Shuttle on 3/24/18.
 */
public class MenuBar extends Group {
    private Button buttonExit;
    private Button buttonPlay;
    private Button buttonConfig;
    private Button buttonResults;
    private Button buttonShare;
    private MenuGroup group;
    private BoomBox boomBox;
    private Results results;

    public MenuBar() {
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.results = new Results();
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

        buttonExit.setPosition(
                Rules.WORLD_WIDTH - buttonExit.getWidth(),
                0
        );

        buttonConfig.setPosition(
                3 * Rules.WORLD_WIDTH / 4 - buttonExit.getWidth() / 2,
                0
        );

        buttonResults.setPosition(
                Rules.WORLD_WIDTH / 4 - buttonResults.getWidth() / 2,
                0
        );

        buttonShare.setPosition(
                Rules.WORLD_WIDTH / 2 - buttonShare.getWidth() / 2,
                0
        );

        buttonExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Workaround. If use BoomBox.playSound(), then Sound is unload while change Screen
                if (GamePreferences.isSoundEnabled()) {
                    Sound sound = Gdx.audio.newSound(Gdx.files.internal(MSConstants.BUTTON_SOUND));
                    sound.play(GamePreferences.getSoundVolume());
                }
                //

                if (!group.isEmptyUserName()) {
                    group.getFile().writeString(group.getUsername().getText(), false);
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                }
            }
        });

        buttonConfig.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Actor[] actorArray = getStage().getActors().toArray();
                Actor tmp = new Actor();
                for (int i = 0; i < actorArray.length; i++) {
                    if (actorArray[i].getName().equals(GroupType.CONFIG.name())) {
                        tmp = actorArray[i];
                    }
                }
                for (int i = 0; i < actorArray.length; i++) {
                    if (actorArray[i].getName().equals(GroupType.CONFIG.name())) {
                        actorArray[i].setVisible(!actorArray[i].isVisible());
                    }
                    if (actorArray[i].getName().equals(GroupType.MENU.name())) {
                        actorArray[i].setVisible(tmp.isVisible());
                    }
                    if (actorArray[i].getName().equals(GroupType.RESULTS.name())) {
                        actorArray[i].setVisible(false);
                    }
                    if (actorArray[i].getName().equals(GroupType.SHARE.name())) {
                        actorArray[i].setVisible(false);
                    }
                }
                boomBox.playSound(MSConstants.BUTTON_SOUND);
            }
        });

        buttonResults.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Actor[] actorArray = getStage().getActors().toArray();
                Actor tmp = new Actor();
                for (int i = 0; i < actorArray.length; i++) {
                    if (actorArray[i].getName().equals(GroupType.RESULTS.name())) {
                        tmp = actorArray[i];
                    }
                }
                for (int i = 0; i < actorArray.length; i++) {
                    if (actorArray[i].getName().equals(GroupType.RESULTS.name())) {
                        actorArray[i].setVisible(!actorArray[i].isVisible());
                    }
                    if (actorArray[i].getName().equals(GroupType.MENU.name())) {
                        actorArray[i].setVisible(tmp.isVisible());
                    }
                    if (actorArray[i].getName().equals(GroupType.CONFIG.name())) {
                        actorArray[i].setVisible(false);
                    }
                    if (actorArray[i].getName().equals(GroupType.SHARE.name())) {
                        actorArray[i].setVisible(false);
                    }
                }
                boomBox.playSound(MSConstants.BUTTON_SOUND);
            }
        });

        buttonShare.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Actor[] actorArray = getStage().getActors().toArray();
                Actor tmp = new Actor();
                for (int i = 0; i < actorArray.length; i++) {
                    if (actorArray[i].getName().equals(GroupType.SHARE.name())) {
                        tmp = actorArray[i];
                    }
                }
                for (int i = 0; i < actorArray.length; i++) {
                    if (actorArray[i].getName().equals(GroupType.SHARE.name())) {
                        actorArray[i].setVisible(!actorArray[i].isVisible());
                    }
                    if (actorArray[i].getName().equals(GroupType.MENU.name())) {
                        actorArray[i].setVisible(tmp.isVisible());
                    }
                    if (actorArray[i].getName().equals(GroupType.CONFIG.name())) {
                        actorArray[i].setVisible(false);
                    }
                    if (actorArray[i].getName().equals(GroupType.RESULTS.name())) {
                        actorArray[i].setVisible(false);
                    }
                }
                boomBox.playSound(MSConstants.BUTTON_SOUND);
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

    public void setGroup(MenuGroup group) {
        this.group = group;
    }
}