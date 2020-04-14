package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.StickerResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * @author Shuttle on 10/12/19.
 */

public abstract class AbstractInfoGroup extends Group {
    protected final VerticalGroup group;
    protected final Skin skin;
    protected final List<ConfigImageButton> buttonList;
    protected final ConfigScrollPane scrollPane;
    protected final Table table;
    protected final int cellWidth;
    protected final int cellHeight;
    protected final int delimiter = 24;
    protected final int N = 5;
    protected TextureRegionDrawable up;
    protected StickerResolver stickerResolver;
    protected boolean barClicked;
    protected int x;
    protected boolean isLeft;
    @Getter
    protected final Map<Sticker, ConfigImageButton> buttonMap;
    private float d;
    private boolean touched;
    private BoomBox boomBox;

    public AbstractInfoGroup() {
        this.setVisible(false);
        this.boomBox = new BoomBox();
        this.stickerResolver = new StickerResolver();
        this.skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.group = new VerticalGroup();
        this.buttonList = new ArrayList<ConfigImageButton>();
        this.buttonMap = new HashMap<Sticker, ConfigImageButton>();
        this.scrollPane = new ConfigScrollPane(group, skin);
        this.table = new Table();
        this.cellWidth = PaintConstants.LEFT_RIGHT_UI_CELL_WIDTH;
        this.cellHeight = PaintConstants.LEFT_RIGHT_UI_CELL_HEIGHT + delimiter;
        float scrollPaneWidth = cellWidth + 44;
        float scrollPainHeight = N * cellHeight - delimiter;

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

    public void updateScroll(Sticker sticker) {
        if (buttonMap.containsKey(sticker)) {
            return;
        }
        int i;
        for (i = 0; i < buttonList.size(); i++) {
            ConfigImageButton button = buttonList.get(i);
            if (button.isEmpty()) {
                button.setupButton(stickerResolver.getRegion(sticker), stickerResolver.getOperation(sticker));
                buttonMap.put(sticker, button);
                //button.changeCount(1);
                break;
            }
        }
        if (i == buttonList.size()) {
            ConfigImageButton button = new ConfigImageButton(up, isLeft);
            button.setupButton(stickerResolver.getRegion(sticker), stickerResolver.getOperation(sticker));
            buttonMap.put(sticker, button);
            buttonList.add(button);
            //button.changeCount(1);
            group.addActor(new Line(button, false));
        }
    }

    public class Line extends Table {
        public Line(Actor actor, boolean isTop) {
            if (!isTop) {
                this.row().height(delimiter);
                this.add();
            }
            this.row().width(cellWidth).height(cellHeight - delimiter);
            this.add(actor);
            this.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (pointer == 0) {
                        boomBox.playSound(MSConstants.UI_CLICK);
                    }
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }
    }
}