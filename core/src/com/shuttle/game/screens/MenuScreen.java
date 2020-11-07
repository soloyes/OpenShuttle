package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.shuttle.game.background.Background;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.ui.groups.InfoGroup;
import com.shuttle.game.ui.groups.MenuBackgroundGroup;
import com.shuttle.game.ui.groups.MenuBar;
import com.shuttle.game.ui.groups.MenuGroup;
import com.shuttle.game.ui.groups.RulesGroup;
import com.shuttle.game.ui.groups.configs.ConfigGroup;
import com.shuttle.game.ui.groups.mvc.profiles.Profiles;
import com.shuttle.game.ui.groups.mvc.results.DetailedResultGroup;
import com.shuttle.game.ui.groups.mvc.results.ResultGroup;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/24/18.
 */

public class MenuScreen extends BaseScreen {
    private Background background;
    private MenuGroup menuGroup;
    private ResultGroup resultGroup;
    private ConfigGroup configGroup;
    private MenuBackgroundGroup menuBackgroundGroup;
    private DetailedResultGroup detailedResultGroup;
    private RulesGroup rulesGroup;
    private BitmapFont font188;
    private BitmapFont font48;
    private Label head;
    private Label underHead;

    public MenuScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        super.show();
        boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_SONG);
    }

    @Override
    public void createGUI() {
        Profiles profiles = new Profiles();
        profiles.init();
        menuBackgroundGroup = new MenuBackgroundGroup();
        background = new Background();
        configGroup = new ConfigGroup();
        rulesGroup = new RulesGroup();
        detailedResultGroup = new DetailedResultGroup();
        resultGroup = new ResultGroup(detailedResultGroup);
        menuGroup = new MenuGroup(configGroup, resultGroup);
        MenuBar menuBar = new MenuBar();
        InfoGroup infoGroup = new InfoGroup();
        this.font188 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT188, BitmapFont.class);
        this.font48 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT48, BitmapFont.class);
        Label.LabelStyle labelStyle64 = new Label.LabelStyle();
        labelStyle64.font = font48;
        Label.LabelStyle labelStyle188 = new Label.LabelStyle();
        labelStyle188.font = font188;
        this.head = new Label("SHUTTLE", labelStyle188);
        head.setPosition(Rules.WORLD_WIDTH / 2 - head.getWidth() / 2, Rules.WORLD_HEIGHT - 234);
        this.underHead = new Label("2D GAME ARCADE", labelStyle64);
        underHead.setPosition(Rules.WORLD_WIDTH / 2 - underHead.getWidth() / 2, Rules.WORLD_HEIGHT - 262);
        stage.addActor(menuBackgroundGroup);
        stage.addActor(menuGroup);
        stage.addActor(configGroup);
        stage.addActor(rulesGroup);
        stage.addActor(resultGroup);
        stage.addActor(detailedResultGroup);
        stage.addActor(infoGroup);
        stage.addActor(menuBar);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        batch.begin();
        background.render(batch);
        batch.end();
        stage.draw();
        batch.begin();
        head.draw(batch, 1.0f);
        underHead.draw(batch, 1.0f);
        batch.end();
    }

    public void update(float delta) {
        background.update(delta);
        stage.act();
    }

    @Override
    public void dispose() {
        super.dispose();
        menuGroup.dispose();
    }

    @Override
    public void resume() {
        boomBox.resumeAllMusic();
    }
}