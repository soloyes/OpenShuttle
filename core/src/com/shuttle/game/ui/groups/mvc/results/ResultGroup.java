package com.shuttle.game.ui.groups.mvc.results;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.ui.groups.GroupType;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 7/14/18.
 */
//todo: to MVC
public class ResultGroup extends Group {
    public ResultGroup() {
        this.setName(GroupType.RESULTS.name());
        this.setVisible(false);
        Table table = new Table();
        com.shuttle.game.ui.groups.mvc.results.Results results = new com.shuttle.game.ui.groups.mvc.results.Results();
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        ScrollPane scrollPane = new ScrollPane(table, skin);
        Label.LabelStyle style64 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );
        Label.LabelStyle style96 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        Label titleLabel = new Label("Results:", style96);

        table.add(titleLabel).colspan(4);
        table.row();

        //todo: use Line.class here
        if (results.getResults() != null) {
            for (ResultDAO r : results.getResults()) {
                if (!r.getUsername().isEmpty()) {
                    table.row();
                    table.add(new Label(r.getUsername(), style64)).left();
                    table.add(new Label(" " + r.getScore() + " ", style64)).right();
                    table.add(new Label(r.getDate(), style64)).right();
                    table.add(new Label(" " + r.getLevel(), style64)).right();
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
