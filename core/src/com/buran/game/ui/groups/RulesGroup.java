package com.buran.game.ui.groups;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.ui.UIButton;
import com.shuttle.game.ui.groups.GroupType;
import com.shuttle.game.utils.Assets;

/**
 * @author soloyes on 29/11/20.
 */

public class RulesGroup extends Group {
    private final Label.LabelStyle labelStyle54;
    private Button buttonBack;
    private BoomBox boomBox;
    private ScrollPane scrollPane;
    private Skin skin;
    private float Y;
    private boolean barClicked;
    private int cellHeight;
    private float d;
    private boolean touched;
    private com.buran.game.utils.StickerResolver stickerResolver;
    private BitmapFont font72;
    private Label missionLabel;
    private Label.LabelStyle labelStyle72;

    public RulesGroup() {
        this.setName(com.shuttle.game.ui.groups.GroupType.RULES.name());
        this.setVisible(false);
        this.boomBox = new BoomBox();
        this.setWidth(Rules.WORLD_WIDTH);
        this.setHeight(Rules.WORLD_HEIGHT);
        this.cellHeight = (int) PaintConstants.UI_SETTINGS_HEIGHT;
        this.font72 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT72, BitmapFont.class);
        this.skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.labelStyle72 = new Label.LabelStyle();
        labelStyle72.font = font72;
        this.missionLabel = new Label("Harvest and fly away!", labelStyle72);
        missionLabel.setPosition(Rules.WORLD_WIDTH / 2 - missionLabel.getWidth() / 2, 2 * Rules.WORLD_HEIGHT / 3 - PaintConstants.UI_SETTINGS_HEIGHT - missionLabel.getHeight());

        this.buttonBack = new UIButton(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.BACK_BUTTON)));
        buttonBack.setPosition(Rules.WORLD_WIDTH / 2 - PaintConstants.MENU_BUTTON_WIDTH / 2, 24);
        BitmapFont font = Assets.getInstance().getAssetManager().get(PaintConstants.FONT54, BitmapFont.class);
        font.getData().markupEnabled = true;
        labelStyle54 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT54, BitmapFont.class),
                null
        );

        VerticalGroup verticalGroup = new VerticalGroup();
        scrollPane = new ScrollPane(verticalGroup, skin);
        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                barClicked = true;
                super.clicked(event, x, y);
            }
        });
        Container<ScrollPane> container = new Container<ScrollPane>(scrollPane);
        container.width(PaintConstants.UI_SETTINGS_WIDTH);
        container.height(PaintConstants.UI_SETTINGS_HEIGHT);
        container.setPosition(Rules.WORLD_WIDTH / 2 - PaintConstants.UI_SETTINGS_WIDTH / 2, 2 * Rules.WORLD_HEIGHT / 3);
        container.align(Align.topLeft);

        buttonBack.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                    Vibrator.vibrate(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    Array<Actor> actors = getStage().getActors();
                    for (int i = 0; i < actors.size; i++) {
                        if (actors.get(i).getName().equals(com.shuttle.game.ui.groups.GroupType.INFO.name())) {
                            actors.get(i).setVisible(true);
                        }
                        if (actors.get(i).getName().equals(GroupType.MENU_BAR.name())) {
                            actors.get(i).setVisible(true);
                        }
                    }
                    setVisible(false);
                }
            }
        });

        this.stickerResolver = new com.buran.game.utils.StickerResolver();
        for (int i = 0; i < Sticker.ALL.size(); i++) {
            if (Sticker.ALL.get(i) == Sticker.FUEL) {
                continue;
            }
            if (i == 0) {
                verticalGroup.addActor(new Card(Sticker.ALL.get(i), true));
            } else {
                verticalGroup.addActor(new Card(Sticker.ALL.get(i), false));
            }
        }

        this.addActor(buttonBack);
        this.addActor(container);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        missionLabel.draw(batch, parentAlpha);
    }

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
        Y = 1.0f * scrollPane.getVisualScrollY() / cellHeight;
    }

    private void scrollTo() {
        barClicked = false;
        d = 0.0f;
        int a1 = (int) Y * cellHeight;
        float b = scrollPane.getVisualScrollY() % cellHeight;

        int r = b >= cellHeight / 2 ? 1 : -1;

        int z = 1;
        if (r < 0) {
            z = 0;
        }
        scrollPane.setScrollY(a1 + z * r * cellHeight);
    }

    private class Card extends Group {
        private TextureRegion back;
        private TextureRegion backTop;
        private boolean isTop;
        private Table table;

        public Card(Sticker sticker, boolean isTop) {
            this.back = Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_RULES_BACKGROUND);
            if (isTop) {
                this.backTop = Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_RULES_BACKGROUND_TOP);
                this.isTop = true;
            }
            this.table = new Table();
            Image image = new Image(stickerResolver.getRegion(sticker));
            table.row().height(32).width(PaintConstants.UI_CONFIG_CELL_WIDTH);
            table.add();
            table.row().height(PaintConstants.ASTRONAUT_HEIGHT);
            table.add(image).width(PaintConstants.ASTRONAUT_WIDTH);
            table.row().height(32);
            table.add();
            table.row();
            Label l = new Label(sticker.getDescription(), labelStyle54);
            l.getStyle().font.getData().markupEnabled = true;
            l.setWidth(PaintConstants.UI_CONFIG_CELL_WIDTH);
            l.setWrap(true);
            table.add(l).width(PaintConstants.UI_CONFIG_CELL_WIDTH - 32);
            this.setWidth(PaintConstants.UI_CONFIG_CELL_WIDTH);
            this.setHeight(PaintConstants.UI_SETTINGS_HEIGHT);
            table.align(Align.top);
            table.setFillParent(true);
            this.addActor(table);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(back, getX(), getY());
            if (isTop) {
                batch.draw(backTop, getX(), getY() + PaintConstants.UI_SETTINGS_HEIGHT - PaintConstants.UI_RULES_BACKGROUND_TOP_HEIGHT);
            }
            super.draw(batch, parentAlpha);
        }
    }
}