package com.shuttle.game.ui.groups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Results;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 7/14/18.
 */
public class ResultGroup extends Group {
    public ResultGroup() {
        this.setName(GroupType.RESULTS.name());
        this.setVisible(false);
        Table table = new Table();
        Results results = new Results();

        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        ScrollPane scrollPane = new ScrollPane(table, skin);

        Label.LabelStyle style32 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT32, BitmapFont.class),
                new Color(1.0f, 1.0f, 0.5f, 1f)
        );
        Label.LabelStyle style64 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );
        Label.LabelStyle style96 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        Label titleLabel = new Label("Results:", style96);
        Label subTitleLabel = new Label("(only  non-child  mode)", style32);

        table.add(titleLabel).colspan(4);
        table.row();
        table.add(subTitleLabel).colspan(4);

        if (results.getResults() != null) {
            for (int i = 1; i < results.getResults().size(); i++) {
                String[] result = results.getResults().get(i).split(" ");
                if (result.length == 4) {
                    table.row();
                    table.add(new Label(result[0], style64)).left();
                    table.add(new Label(" " + result[1] + " ", style64)).right();
                    table.add(new Label(result[2], style64)).right();
                    table.add(new Label(" " + result[3], style64)).right();
                }
            }
        }

        scrollPane.setClamp(false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setWidth(Rules.WORLD_WIDTH);
        scrollPane.setHeight(2 * Rules.WORLD_HEIGHT / 3);
        scrollPane.setY(Rules.WORLD_HEIGHT / 3);
        this.addActor(scrollPane);
    }
}
