package com.buran.game.ui.groups.selector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

/**
 * @author soloyes on 3/22/19.
 */

public class SelectorGroup extends Group {
    private static SelectorGroup instance;
    private List<Selector> selectors;
    private Selector leftSelector, centerSelector, rightSelector;
    private Button leftButton, centerButton, rightButton;
    private TextureRegionDrawable circleGlow;
    private TextureRegion circleScale;
    private Image circleOut, circleOutBack, leftArrow, rightArrow;
    private int select;
    private boolean paused;
    private BoomBox boomBox;
    @Getter
    private int items;
    private RadialSprite radialSprite;

    public static SelectorGroup getInstance() {
        if (instance == null) {
            instance = new SelectorGroup();
        }
        return instance;
    }

    private SelectorGroup() {
        this.boomBox = new BoomBox();
        this.selectors = new LinkedList<Selector>();
        this.circleGlow = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.CIRCLE_GLOW));
        this.leftButton = new Button(circleGlow);
        this.rightButton = new Button(circleGlow);
        this.centerButton = new Button(circleGlow);
        this.circleScale = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_SCALE));
        this.circleOut = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_OUT));
        this.radialSprite = new RadialSprite(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_OUT_CENTER));
        this.circleOutBack = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_OUT_CENTER_BACK));
        this.leftArrow = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_LEFT));
        this.rightArrow = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_RIGHT));
        int width = PaintConstants.SELECTOR_WIDTH;
        int height = PaintConstants.SELECTOR_HEIGHT;

        float centerWidth = PaintConstants.SELECTOR_CENTER_WIDTH;
        float centerHeight = PaintConstants.SELECTOR_CENTER_HEIGHT;

        centerButton.setSize(centerWidth, centerHeight);
        leftButton.setSize(width, height);
        rightButton.setSize(width, height);

        leftButton.setPosition(Rules.WORLD_WIDTH / 2 - centerWidth / 2 - width - 14, Rules.WORLD_HEIGHT - 323);
        rightButton.setPosition(Rules.WORLD_WIDTH / 2 + centerWidth / 2 + 14, Rules.WORLD_HEIGHT - 323);
        centerButton.setPosition(Rules.WORLD_WIDTH / 2 - centerWidth / 2, Rules.WORLD_HEIGHT - 351);

        leftArrow.setPosition(leftButton.getX(), leftButton.getY());
        rightArrow.setPosition(rightButton.getX(), rightButton.getY());

        leftButton.addListener(new ChangeListener() {
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
        leftButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!com.buran.game.ui.groups.PauseGroup.isPause()) {
                    boomBox.playSound(MSConstants.UI_LEFT_RIGHT);
                    Vibrator.vibrate(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        rightButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!com.buran.game.ui.groups.PauseGroup.isPause()) {
                    boomBox.playSound(MSConstants.UI_LEFT_RIGHT);
                    Vibrator.vibrate(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        centerButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (selectors.size() != 0) {
                    if (centerSelector.isActive() && !com.buran.game.ui.groups.PauseGroup.isPause()) {
                        removeSelector();
                        centerSelector.press();
                        Vibrator.vibrate(VConstants.UI_CLICK);
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        rightButton.addListener(new ChangeListener() {
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

        this.addActor(leftArrow);
        this.addActor(rightArrow);
        this.addActor(leftButton);
        this.addActor(rightButton);
        this.addActor(centerButton);
    }

    @Override
    public void act(float delta) {
        paused = com.buran.game.ui.groups.PauseGroup.isPause();
        if (Rules.GAME_OVER) {
            setVisible(false);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (com.buran.game.ui.groups.PauseGroup.isPause()) {
            batch.setShader(ShaderManager.PAUSE.getProgram());
        } else {
            batch.setShader(null);
        }
        if (!Rules.GAME_OVER) {
            this.drawChildren(batch, 0.5f);
        }
    }

    public void addSelector(Sticker sticker, int amount) {
        com.buran.game.ui.groups.configs.LeftGroup.getInstance().updateScroll(sticker);
        boolean present = false;
        int i;
        for (i = 0; i < selectors.size(); i++) {
            if (selectors.get(i).getSticker() == sticker) {
                present = true;
                break;
            }
        }
        if (present) {
            selectors.get(i).changeCount(amount);
        } else {
            Selector selector = new Selector(sticker);
            selector.changeCount(amount);
            selectors.add(selector);
        }
        items += amount;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(Color.WHITE);
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

        circleOut.setPosition(leftButton.getX(), leftButton.getY());
        circleOut.draw(batch, 1.0f);
        circleOut.setPosition(rightButton.getX(), rightArrow.getY());
        circleOut.draw(batch, 1.0f);

        circleOutBack.setPosition(centerButton.getX(), centerButton.getY());
        circleOutBack.draw(batch, 1.0f);
        if (selectors.size() != 0) {
            radialSprite.draw(batch, centerButton.getX(), centerButton.getY(), centerSelector.getAngle());
        }
    }

    private void renderLeft(SpriteBatch batch) {
        Color color = batch.getColor();
        batch.setColor(color.a, color.g, color.b, PaintConstants.SELECTOR_ALPHA);
        leftSelector.draw(batch,
                leftButton.getX(), leftButton.getY(),
                leftButton.getOriginX(), leftButton.getOriginY(),
                leftButton.getWidth(), leftButton.getHeight(),
                1.0f, 1.0f, 0.0f
        );
        batch.setColor(Color.WHITE);
    }

    private void renderCenter(SpriteBatch batch, int count) {
        float percentage = 1.0f * count / Rules.SELECTOR_MAX_COUNT;
        Color color = batch.getColor();
        batch.setColor(color.a, color.g, color.b, 1.0f);
        batch.draw(
                circleScale.getTexture(),
                centerButton.getX(), centerButton.getY(),
                centerButton.getOriginX(), centerButton.getOriginY(),
                centerButton.getWidth(), centerButton.getHeight() * percentage,
                1.0f, 1.0f,
                0,
                circleScale.getRegionX(), circleScale.getRegionY(),
                circleScale.getRegionWidth(), (int) (circleScale.getRegionHeight() * percentage),
                false, true
        );
        centerSelector.draw(batch,
                centerButton.getX(), centerButton.getY(),
                centerButton.getOriginX(), centerButton.getOriginY(),
                centerButton.getWidth(), centerButton.getHeight(),
                1.0f, 1.0f, 0.0f
        );
        batch.setColor(Color.WHITE);
    }

    private void renderRight(SpriteBatch batch) {
        Color color = batch.getColor();
        batch.setColor(color.a, color.g, color.b, PaintConstants.SELECTOR_ALPHA);
        rightSelector.draw(batch,
                rightButton.getX(), rightButton.getY(),
                rightButton.getOriginX(), rightButton.getOriginY(),
                rightButton.getWidth(), rightButton.getHeight(),
                1.0f, 1.0f, 0.0f
        );
        batch.setColor(Color.WHITE);
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
        } else {
            centerSelector = null;
        }

        com.buran.game.ui.groups.configs.LeftGroup.getInstance().setCenter(centerSelector);
        for (int i = 0; i < selectors.size(); i++) {
            selectors.get(i).update(dt);
        }
    }

    public void removeSelector() {
        Consumer.select(selectors.get(select).getSticker());
        selectors.get(select).changeCount(-1);
        Standing.getInstance().changeUsedItems(selectors.get(select).getSticker());
        if (selectors.get(select).getCount() == 0) {
            selectors.remove(select);
            select--;
            if (select < 0) {
                select = 0;
            }
        }
    }

    public void dispose() {
        instance = null;
    }

    public static void doStart() {
        instance = null;
    }
}