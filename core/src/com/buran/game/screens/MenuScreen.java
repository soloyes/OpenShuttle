package com.buran.game.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.buran.game.background.Background;

/**
 * @author soloyes on 3/24/18.
 */

public class MenuScreen extends BaseScreen {
    private Background background;
    private MenuGroup menuGroup;
    private mvc.results.ResultGroup resultGroup;
    private configs.ConfigGroup configGroup;
    private MenuBackgroundGroup menuBackgroundGroup;
    private mvc.results.DetailedResultGroup detailedResultGroup;
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
        boomBox.stopAllMusicAndPlayThese(com.buran.game.conf.MSConstants.MENU_SONG);
    }

    @Override
    public void createGUI() {
        com.buran.game.ui.groups.mvc.profiles.Profiles profiles = new com.buran.game.ui.groups.mvc.profiles.Profiles();
        profiles.init();
        menuBackgroundGroup = new com.buran.game.ui.groups.MenuBackgroundGroup();
        background = new Background();
        configGroup = new com.buran.game.ui.groups.configs.ConfigGroup();
        rulesGroup = new com.buran.game.ui.groups.RulesGroup();
        detailedResultGroup = new com.buran.game.ui.groups.mvc.results.DetailedResultGroup();
        resultGroup = new com.buran.game.ui.groups.mvc.results.ResultGroup(detailedResultGroup);
        menuGroup = new com.buran.game.ui.groups.MenuGroup(configGroup, resultGroup);
        com.buran.game.ui.groups.MenuBar menuBar = new com.buran.game.ui.groups.MenuBar();
        com.buran.game.ui.groups.InfoGroup infoGroup = new com.buran.game.ui.groups.InfoGroup();
        this.font188 = com.buran.game.utils.Assets.getInstance().getAssetManager().get(com.buran.game.conf.PaintConstants.FONT188, BitmapFont.class);
        this.font48 = com.buran.game.utils.Assets.getInstance().getAssetManager().get(com.buran.game.conf.PaintConstants.FONT48, BitmapFont.class);
        Label.LabelStyle labelStyle64 = new Label.LabelStyle();
        labelStyle64.font = font48;
        Label.LabelStyle labelStyle188 = new Label.LabelStyle();
        labelStyle188.font = font188;
        this.head = new Label("SHUTTLE", labelStyle188);
        head.setPosition(com.buran.game.conf.Rules.WORLD_WIDTH / 2 - head.getWidth() / 2, com.buran.game.conf.Rules.WORLD_HEIGHT - 234);
        this.underHead = new Label("2D GAME ARCADE", labelStyle64);
        underHead.setPosition(com.buran.game.conf.Rules.WORLD_WIDTH / 2 - underHead.getWidth() / 2, com.buran.game.conf.Rules.WORLD_HEIGHT - 262);
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