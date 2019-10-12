package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.shuttle.game.background.Background;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.ui.groups.MenuBar;
import com.shuttle.game.ui.groups.MenuGroup;
import com.shuttle.game.ui.groups.ShareGroup;
import com.shuttle.game.ui.groups.configs.ConfigGroup;
import com.shuttle.game.ui.groups.mvc.results.ResultGroup;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/24/18.
 */
public class MenuScreen extends BaseScreen {
    private Background background;
    private MenuGroup menuGroup;
    private BitmapFont font256;
    private BitmapFont font64;
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
        ShareGroup shareGroup = new ShareGroup();
        this.font256 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT256, BitmapFont.class);
        this.font64 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class);
        Label.LabelStyle labelStyle64 = new Label.LabelStyle();
        labelStyle64.font = font64;
        Label.LabelStyle labelStyle256 = new Label.LabelStyle();
        labelStyle256.font = font256;
        this.head = new Label("SHUTTLE", labelStyle256);
        head.setPosition(Rules.WORLD_WIDTH / 2 - head.getWidth() / 2, Rules.WORLD_HEIGHT - head.getHeight());
        this.underHead = new Label("2D GAME ARCADE", labelStyle64);
        underHead.setPosition(Rules.WORLD_WIDTH / 2 - underHead.getWidth() / 2, Rules.WORLD_HEIGHT - head.getHeight() - underHead.getHeight() / 2);
        stage.addActor(menuGroup);
        stage.addActor(ConfigGroup.getInstance());
        stage.addActor(menuBar);
        stage.addActor(resultGroup);
        stage.addActor(shareGroup);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        batch.begin();
        background.render(batch);
        head.draw(batch, 1.0f);
        underHead.draw(batch, 1.0f);
        batch.end();
        stage.draw();
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
    }
}