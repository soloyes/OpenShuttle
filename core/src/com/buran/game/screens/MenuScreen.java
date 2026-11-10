package com.buran.game.screens;

import com.buran.game.background.MenuSpace;
import com.buran.game.ui.menu.MenuHero;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.buran.game.background.Background;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.ui.groups.InfoGroup;
import com.buran.game.ui.groups.MenuBackgroundGroup;
import com.buran.game.ui.groups.MenuBar;
import com.buran.game.ui.groups.MenuGroup;
import com.buran.game.ui.groups.RulesGroup;
import com.buran.game.ui.groups.configs.ConfigGroup;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.ui.groups.mvc.results.DetailedResultGroup;
import com.buran.game.ui.groups.mvc.results.ResultGroup;
import com.buran.game.utils.Assets;

/**
 * @author soloyes on 3/24/18.
 */

public class MenuScreen extends BaseScreen {
    private MenuSpace space;
    private MenuGroup menuGroup;
    private ResultGroup resultGroup;
    private ConfigGroup configGroup;
    private MenuHero menuHero;
    private DetailedResultGroup detailedResultGroup;
    private RulesGroup rulesGroup;
    private BitmapFont font175;
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
        menuHero = new MenuHero();
        if (space != null) {
            space.dispose();
        }
        space = new MenuSpace();
        configGroup = new ConfigGroup();
        rulesGroup = new RulesGroup();
        detailedResultGroup = new DetailedResultGroup();
        resultGroup = new ResultGroup(detailedResultGroup);
        menuGroup = new MenuGroup(configGroup, resultGroup);
        MenuBar menuBar = new MenuBar();
        InfoGroup infoGroup = new InfoGroup();
        this.font175 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT175, BitmapFont.class);
        this.font48 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT54, BitmapFont.class);
        Label.LabelStyle labelStyle64 = new Label.LabelStyle();
        labelStyle64.font = font48;
        Label.LabelStyle labelStyle175 = new Label.LabelStyle();
        labelStyle175.font = font175;
        // Title block, left-aligned with the menu column.
        this.head = new Label("BURAN-19", labelStyle175);
        head.setPosition(MenuBar.leftX(), Rules.WORLD_HEIGHT * 0.66f);
        this.underHead = new Label("THE LAST FLIGHT  -  1991", labelStyle64);
        underHead.setColor(Color.valueOf("9fb3ccff"));
        underHead.setPosition(MenuBar.leftX() + 6, head.getY() - underHead.getHeight() - 10);
        stage.addActor(menuHero);
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
        space.render(batch);
        stage.draw();
        batch.begin();
        head.draw(batch, 1.0f);
        underHead.draw(batch, 1.0f);
        batch.end();
    }

    public void update(float delta) {
        space.update(delta);
        stage.act(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        menuGroup.dispose();
        if (space != null) {
            space.dispose();
            space = null;
        }
    }

    @Override
    public void resume() {
        boomBox.resumeAllMusic();
    }
}