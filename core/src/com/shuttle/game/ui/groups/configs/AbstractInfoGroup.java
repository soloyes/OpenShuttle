package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shuttle on 10/12/19.
 */
public abstract class AbstractInfoGroup extends Group {
    protected final VerticalGroup group;
    protected final Skin skin;
    protected final List<ConfigImageButton> buttonList;
    protected final ConfigScrollPane scrollPane;
    protected final Table table;
    protected final TextureRegionDrawable up;
    protected final int cellWidth;
    protected final int cellHeight;
    protected boolean barClicked;
    protected final int N = 5;
    private float d;
    protected int x;
    private boolean touched;

    public AbstractInfoGroup() {
        this.setVisible(false);
        this.skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.group = new VerticalGroup();
        this.buttonList = new ArrayList<ConfigImageButton>();
        this.scrollPane = new ConfigScrollPane(group, skin);
        this.table = new Table();
        this.cellWidth = PaintConstants.LEFT_RIGHT_UI_CELL_WIDTH;
        this.cellHeight = PaintConstants.LEFT_RIGHT_UI_CELL_HEIGHT;
        this.up = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_EMPTY_BOX));
        float scrollPaneWidth = cellWidth + 44;
        float scrollPainHeight = N * cellHeight;

        scrollPane.setSize(scrollPaneWidth, scrollPainHeight);
        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                barClicked = true;
                super.clicked(event, x, y);
            }
        });
        setScrollBarPositions();
        setPosition();
    }

    protected abstract void setScrollBarPositions();

    protected abstract void setPosition();

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!scrollPane.isFlinging() && !scrollPane.isPanning() && !scrollPane.isDragging()) {
            if (touched) {
                scrollTo();
                touched = false;
            }
        } else {
            touched = true;
        }

        if (!touched && barClicked) {
            d += delta;
        }
        if (d >= 1.0f) {
            if (barClicked) {
                scrollTo();
            }
        }
        x = (int) (scrollPane.getVisualScrollY() / cellHeight);
    }

    private void scrollTo() {
        barClicked = false;
        d = 0.0f;
        int a1 = x * cellHeight;
        float b = scrollPane.getVisualScrollY() % cellHeight;

        int r = b >= cellHeight / 2 ? 1 : -1;

        int z = 1;
        if (r < 0) {
            z = 0;
        }
        scrollPane.setScrollY(a1 + z * r * cellHeight);
    }
}