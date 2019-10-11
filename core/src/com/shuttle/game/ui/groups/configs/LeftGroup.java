package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.ui.groups.GroupType;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 10/11/19.
 */
public class LeftGroup extends Group {
    private static LeftGroup instance = new LeftGroup();
    private final TextureRegionDrawable circleIn;
    private final ImageButton button1;
    private final ImageButton button2;
    private final ImageButton button3;
    private final ImageButton button4;
    private final ImageButton button5;
    private final ImageButton button6;
    private final ImageButton button7;
    private final ImageButton button8;
    private final ImageButton button9;
    private final ImageButton button10;
    private final ImageButton button11;
    private final ImageButton button12;
    private final float scrollPaneWidth;
    private ConfigScrollPane scrollPane;
    private VerticalGroup group;
    private Skin skin;
    private boolean touched;
    private boolean barClicked;
    private float buttonHeight;
    private float scrollPainHeight;
    private float buttonWidth;
    private float d;

    public LeftGroup() {
        this.setName(GroupType.LEFT.name());
        this.setVisible(false);
        this.skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.group = new VerticalGroup();
        this.scrollPane = new ConfigScrollPane(group, skin);
        this.circleIn = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_FULL_CIRCLE));
        this.button1 = new ImageButton(circleIn);
        this.button2 = new ImageButton(circleIn);
        this.button3 = new ImageButton(circleIn);
        this.button4 = new ImageButton(circleIn);
        this.button5 = new ImageButton(circleIn);
        this.button6 = new ImageButton(circleIn);
        this.button7 = new ImageButton(circleIn);
        this.button8 = new ImageButton(circleIn);
        this.button9 = new ImageButton(circleIn);
        this.button10 = new ImageButton(circleIn);
        this.button11 = new ImageButton(circleIn);
        this.button12 = new ImageButton(circleIn);

        //todo: to total height (with spaces)
        this.buttonHeight = PaintConstants.LEFT_RIGHT_PANEL_CIRCLE_HEIGHT;
        this.buttonWidth = PaintConstants.LEFT_RIGHT_PANEL_CIRCLE_WIDTH + 16;
        this.scrollPainHeight = 5 * buttonHeight;
        this.scrollPaneWidth = buttonWidth;
        scrollPane.setPosition(
                25,
                477
        );
        scrollPane.setSize(scrollPaneWidth, scrollPainHeight);
        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        group.addActor(button1);
        group.addActor(button2);
        group.addActor(button3);
        group.addActor(button4);
        group.addActor(button5);
        group.addActor(button6);
        group.addActor(button7);
        group.addActor(button8);
        group.addActor(button9);
        group.addActor(button10);
        group.addActor(button11);
        group.addActor(button12);
        this.addActor(scrollPane);

        scrollPane.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                barClicked = true;
                d = 0.0f;
                super.clicked(event, x, y);
            }
        });
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
            scrollTo();
            barClicked = false;
            d = 0.0f;
        }
    }

    private void scrollTo() {
        float r = (int) (scrollPane.getVisualScrollY() / buttonHeight) * buttonHeight;
        scrollPane.setScrollY(r);
    }

    public void dispose() {
        instance = null;
    }

    public static LeftGroup getInstance() {
        if (instance == null) {
            instance = new LeftGroup();
        }
        return instance;
    }
}
