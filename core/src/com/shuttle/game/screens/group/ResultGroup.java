package com.shuttle.game.screens.group;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.shuttle.game.conf.Results;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 7/14/18.
 */
public class ResultGroup extends Group {
    private Results results;
    private String[] result;

    public ResultGroup() {
        this.setName(GroupType.RESULTS.name());
        this.setVisible(false);
        Table table = new Table();
        results = new Results();

        Skin skin = Assets.getInstance().getAssetManager().get("uiskin/gameSkin.json");
        ScrollPane scrollPane = new ScrollPane(table, skin);

        Label.LabelStyle style64 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get("fonts/gomarice64.ttf", BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );
        Label.LabelStyle style96 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get("fonts/gomarice96.ttf", BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        Label titleLabel = new Label("Results:", style96);

        table.add(titleLabel).colspan(3);

        if (results.getResults() != null) {
            for (int i = 1; i < results.getResults().size(); i++) {
                result = results.getResults().get(i).split(" ");
                table.row();
                table.add(new Label(result[0], style64)).left();
                table.add(new Label(" " + result[1] + " ", style64)).right();
                table.add(new Label(result[2], style64)).right();
            }
        }

        scrollPane.setClamp(false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setWidth(ScreenManager.getInstance().getViewport().getWorldWidth());
        scrollPane.setHeight(2*ScreenManager.getInstance().getViewport().getWorldHeight()/3);
        scrollPane.setY(ScreenManager.getInstance().getViewport().getWorldHeight()/3);
        this.addActor(scrollPane);
    }
}
