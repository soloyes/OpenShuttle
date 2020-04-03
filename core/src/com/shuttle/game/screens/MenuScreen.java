package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.shuttle.game.background.Background;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.ui.groups.InfoGroup;
import com.shuttle.game.ui.groups.MenuBackgroundGroup;
import com.shuttle.game.ui.groups.MenuBar;
import com.shuttle.game.ui.groups.MenuGroup;
import com.shuttle.game.ui.groups.configs.ConfigGroup;
import com.shuttle.game.ui.groups.mvc.results.ResultGroup;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/24/18.
 */
public class MenuScreen extends BaseScreen {
    private Background background;
    private MenuGroup menuGroup;
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
        background = new Background();
        menuGroup = new MenuGroup();
        MenuBar menuBar = new MenuBar();
        ResultGroup resultGroup = new ResultGroup();
        InfoGroup infoGroup = new InfoGroup();
        this.font188 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT188, BitmapFont.class);
        this.font48 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT48, BitmapFont.class);
        Label.LabelStyle labelStyle64 = new Label.LabelStyle();
        labelStyle64.font = font48;
        Label.LabelStyle labelStyle256 = new Label.LabelStyle();
        labelStyle256.font = font188;
        this.head = new Label("SHUTTLE", labelStyle256);
        head.setPosition(285, 1686);
        this.underHead = new Label("2D GAME ARCADE", labelStyle64);
        underHead.setPosition(408, 1658);
        stage.addActor(MenuBackgroundGroup.getInstance());
        stage.addActor(menuGroup);
        stage.addActor(ConfigGroup.getInstance());
        stage.addActor(menuBar);
        stage.addActor(resultGroup);
        stage.addActor(infoGroup);
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
        ConfigGroup.getInstance().dispose();
        MenuBackgroundGroup.getInstance().dispose();
    }
}