package com.shuttle.game.ui.groups.selector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Consumer;

import java.util.LinkedList;

/**
 * @author Shuttle on 3/22/19.
 */
public class SelectorGroup extends Group {
    private GameScreen gs;
    private LinkedList<Selector> selectors;
    private Selector leftSelector, centerSelector, rightSelector;
    private ImageButton leftButton, centerButton, rightButton;
    private TextureRegionDrawable circleIn;
    private TextureRegion circleScale;
    private Image circleOut, leftArrow, rightArrow;
    private float scale = 1.2f;
    private int select;
    private int pressedCount;
    private boolean paused;

    public SelectorGroup(final GameScreen gs) {
        this.gs = gs;
        this.selectors = new LinkedList<Selector>();
        this.circleIn = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_IN));
        this.leftButton = new ImageButton(circleIn);
        this.rightButton = new ImageButton(circleIn);
        this.centerButton = new ImageButton(circleIn);
        this.circleScale = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_SCALE));
        this.circleOut = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_OUT));
        this.leftArrow = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_LEFT));
        this.rightArrow = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_RIGHT));
        int width = PaintConstants.SELECTOR_WIDTH;
        int height = PaintConstants.SELECTOR_HEIGHT;

        leftButton.setOrigin(Align.center);
        rightButton.setOrigin(Align.center);

        float centerWidth = width * scale;
        float centerHeight = height * scale;
        centerButton.getImage().setScale(scale);
        centerButton.setOrigin(Align.center);

        leftButton.setPosition(
                Rules.WORLD_WIDTH / 2 - 3 * width / 2 - 30,
                Rules.WORLD_HEIGHT -
                        PaintConstants.HP_BAR_HEIGHT -
                        PaintConstants.SP_BAR_HEIGHT -
                        height - 64 - 44
        );
        rightButton.setPosition(Rules.WORLD_WIDTH / 2 + width / 2 + 30,
                Rules.WORLD_HEIGHT -
                        PaintConstants.HP_BAR_HEIGHT -
                        PaintConstants.SP_BAR_HEIGHT -
                        height - 64 - 44
        );
        centerButton.setPosition(
                Rules.WORLD_WIDTH / 2 - centerWidth / 2,
                Rules.WORLD_HEIGHT -
                        centerHeight -
                        PaintConstants.TIMER_HEIGHT -
                        56
        );

        leftArrow.setPosition(leftButton.getX(), leftButton.getY());
        rightArrow.setPosition(rightButton.getX(), rightButton.getY());

        leftButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!paused) {
                    select++;
                }
                if (select > selectors.size() - 1) {
                    select = 0;
                }
            }
        });

        rightButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!paused) {
                    select--;
                }
                if (select < 0) {
                    select = selectors.size() == 0 ? 0 : selectors.size() - 1;
                }
            }
        });

        leftButton.setName("leftButton");
        rightButton.setName("rightButton");
        this.addActor(leftArrow);
        this.addActor(rightArrow);
        this.addActor(leftButton);
        this.addActor(rightButton);
        this.addActor(centerButton);
    }

    @Override
    public void act(float delta) {
        paused = gs.getPauseGroup().isPause();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (gs.getPauseGroup().isPause()) {
            batch.setShader(gs.getShaderPause());
        } else {
            batch.setShader(null);
        }
        if (!Rules.GAME_OVER) {
            this.drawChildren(batch, parentAlpha);
        }
    }

    public void addSelector(Sticker sticker) {
        boolean isPresent = false;
        int i;
        for (i = 0; i < selectors.size(); i++) {
            if (selectors.get(i).getSticker() == sticker) {
                isPresent = true;
                break;
            }
        }
        if (isPresent) {
            if (selectors.get(i).getCount() < Rules.SELECTOR_MAX_COUNT)
                selectors.get(i).changeCount(1);
        } else {
            Selector selector = new Selector(sticker);
            selectors.add(selector);
        }
    }

    public void render(SpriteBatch batch) {
        if (selectors.size() >= 3) {
            renderLeft(batch);
            renderCenter(batch, selectors.get(select).getCount());
            renderRight(batch);

        } else if (selectors.size() == 2) {
            if (select == 1) {
                renderLeft(batch);
                renderCenter(batch, selectors.get(select).getCount());
            }
            if (select == 0) {
                renderCenter(batch, selectors.get(select).getCount());
                renderRight(batch);
            }
        } else if (selectors.size() == 1) {
            renderCenter(batch, selectors.get(select).getCount());
        }

        circleOut.setScale(1.0f);
        circleOut.setPosition(leftButton.getX(), leftButton.getY());
        circleOut.draw(batch, 1.0f);
        circleOut.setScale(1.0f);
        circleOut.setPosition(rightButton.getX(), rightArrow.getY());
        circleOut.draw(batch, 1.0f);
        circleOut.setPosition(centerButton.getX(), centerButton.getY());
        circleOut.setScale(scale);
        circleOut.draw(batch, 1.0f);
    }

    private void renderLeft(SpriteBatch batch) {
        Color color = batch.getColor();
        batch.setColor(color.a, color.g, color.b, PaintConstants.SELECTOR_ALPHA);
        batch.draw(leftSelector.getTextureRegion(),
                leftButton.getX(), leftButton.getY(),
                leftButton.getOriginX(), leftButton.getOriginY(),
                leftButton.getWidth(), leftButton.getHeight(),
                0.7f, 0.7f, 0.0f
        );
        batch.setColor(color);
    }

    private void renderCenter(SpriteBatch batch, int count) {
        float percentage = 1.0f * count / Rules.SELECTOR_MAX_COUNT;
        batch.draw(
                circleScale.getTexture(),
                centerButton.getX(), centerButton.getY(),
                centerButton.getOriginX(), centerButton.getOriginY(),
                centerButton.getWidth() * scale, centerButton.getHeight() * scale * percentage,
                1.0f, 1.0f,
                0,
                circleScale.getRegionX(), circleScale.getRegionY(),
                circleScale.getRegionWidth(), (int) (circleScale.getRegionHeight() * percentage),
                false, true
        );
        batch.draw(centerSelector.getTextureRegion(),
                centerButton.getX(), centerButton.getY(),
                centerButton.getOriginX(), centerButton.getOriginY(),
                centerButton.getWidth() * 1.3f, centerButton.getHeight() * 1.3f,
                0.7f, 0.7f, 0.0f
        );
    }

    private void renderRight(SpriteBatch batch) {
        Color color = batch.getColor();
        batch.setColor(color.a, color.g, color.b, PaintConstants.SELECTOR_ALPHA);
        batch.draw(rightSelector.getTextureRegion(),
                rightButton.getX(), rightButton.getY(),
                rightButton.getOriginX(), rightButton.getOriginY(),
                rightButton.getWidth(), rightButton.getHeight(),
                0.7f, 0.7f, 0.0f
        );
        batch.setColor(color);
    }

    public void update(float dt) {
        if (leftButton.isPressed()) {
            leftArrow.setColor(Color.RED);
        } else {
            leftArrow.setColor(Color.WHITE);
        }

        if (rightButton.isPressed()) {
            rightArrow.setColor(Color.RED);
        } else {
            rightArrow.setColor(Color.WHITE);
        }

        if (selectors.size() >= 3) {
            centerSelector = selectors.get(select);
            if (select == 0) {
                leftSelector = selectors.get(selectors.size() - 1);
                rightSelector = selectors.get(select + 1);
            } else if (select == selectors.size() - 1) {
                leftSelector = selectors.get(select - 1);
                rightSelector = selectors.get(0);
            } else {
                rightSelector = selectors.get(select + 1);
                leftSelector = selectors.get(select - 1);
            }

        } else if (selectors.size() == 2) {
            if (select == 1) {
                leftSelector = selectors.get(0);
                centerSelector = selectors.get(1);
            }
            if (select == 0) {
                rightSelector = selectors.get(1);
                centerSelector = selectors.get(0);
            }
        } else if (selectors.size() == 1) {
            centerSelector = selectors.get(0);
        }
        press();
    }

    private void press() {
        if (centerButton.isPressed()) {
            pressedCount++;
        } else {
            pressedCount = 0;
        }
        if (pressedCount == 1) {
            removeSelector();
        }
    }

    public void removeSelector() {
        if (selectors.size() != 0) {
            Consumer.select(gs, selectors.get(select).getSticker());
            selectors.get(select).changeCount(-1);
            if (selectors.get(select).getCount() == 0) {
                selectors.remove(select);
                select--;
                if (select < 0) {
                    select = 0;
                }
            }
        }
    }
}
