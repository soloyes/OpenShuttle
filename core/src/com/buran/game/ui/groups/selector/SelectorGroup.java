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
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.conf.VConstants;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.Vibrator;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.player.Standing;
import com.buran.game.ui.groups.PauseGroup;
import com.buran.game.ui.groups.configs.LeftGroup;
import com.buran.game.utils.Assets;
import com.buran.game.utils.Consumer;
import com.buran.game.utils.RadialSprite;
import com.buran.game.utils.ShaderManager;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

/**
 * @author soloyes on 3/22/19.
 */

public class SelectorGroup extends Group {
    private static SelectorGroup instance;
    private List<Selector> selectors;
    private Selector upSelector, centerSelector, downSelector;
    private Button upButton, centerButton, downButton;
    private TextureRegionDrawable circleGlow;
    private TextureRegion circleScale;
    private Image circleOut, circleOutBack, upArrow, downArrow, upArrow1, downArrow1;
    private int select;
    private boolean paused;
    private BoomBox boomBox;
    @Getter
    private int items;
    private RadialSprite radialSprite;
    private float centerWidth, centerHeight;
    private int width, height;

    /** Tutorial: the player has used an item / switched items at least once this game. */
    public static boolean usedOnce;
    public static boolean switchedOnce;

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
        this.upButton = new Button(circleGlow);
        this.downButton = new Button(circleGlow);
        this.centerButton = new Button(circleGlow);
        this.circleScale = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_SCALE));
        this.circleOut = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_OUT));
        this.radialSprite = new RadialSprite(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_OUT_CENTER));
        this.circleOutBack = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_OUT_CENTER_BACK));
        this.upArrow = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_UP));
        this.downArrow = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_DOWN));
        this.upArrow1 = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_UP_1));
        this.downArrow1 = new Image(Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_DOWN_1));

        this.width = PaintConstants.SELECTOR_WIDTH;
        this.height = PaintConstants.SELECTOR_HEIGHT;

        this.centerWidth = PaintConstants.SELECTOR_CENTER_WIDTH;
        this.centerHeight = PaintConstants.SELECTOR_CENTER_HEIGHT;

        centerButton.setSize(centerWidth, centerHeight);
        upButton.setSize(width, height);
        downButton.setSize(width, height);

        upButton.addListener(new ChangeListener() {
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
        upButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!PauseGroup.isPause()) {
                    boomBox.playSound(MSConstants.UI_LEFT_RIGHT);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        downButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!PauseGroup.isPause()) {
                    boomBox.playSound(MSConstants.UI_LEFT_RIGHT);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        centerButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (selectors.size() != 0) {
                    if (centerSelector.isActive() && !PauseGroup.isPause()) {
                        removeSelector();
                        centerSelector.press();
                        Vibrator.vibrateUI(VConstants.UI_CLICK);
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        downButton.addListener(new ChangeListener() {
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

        this.addActor(upArrow);
        this.addActor(downArrow);
        this.addActor(upArrow1);
        this.addActor(downArrow1);
        this.addActor(upButton);
        this.addActor(downButton);
        this.addActor(centerButton);
    }

    @Override
    public void act(float delta) {
        paused = PauseGroup.isPause();
        if (Rules.GAME_OVER) {
            setVisible(false);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (PauseGroup.isPause()) {
            batch.setShader(ShaderManager.PAUSE.getProgram());
        } else {
            batch.setShader(null);
        }
        if (!Rules.GAME_OVER && !PauseGroup.isPause()) {
            this.drawChildren(batch, 0.5f);
        }
    }

    /** The item that A would use now, or null. */
    public Selector getCenter() {
        return selectors.size() == 0 ? null : centerSelector;
    }

    /** What LB would select (null when there is nothing else to switch to). */
    public Selector getPrevious() {
        int n = selectors.size();
        return n < 3 ? null : selectors.get((select - 1 + n) % n);
    }

    /** What RB would select. */
    public Selector getNext() {
        int n = selectors.size();
        return n < 2 ? null : selectors.get((select + 1) % n);
    }

    public int getSelectorCount() {
        return selectors.size();
    }

    /** Gamepad/keyboard: same as tapping the upper arrow. */
    public void selectPrevious() {
        if (paused || PauseGroup.isPause() || selectors.size() < 2) {
            return;
        }
        boomBox.playSound(MSConstants.UI_LEFT_RIGHT);
        switchedOnce = true;
        select--;
        if (select < 0) {
            select = selectors.size() - 1;
        }
    }

    /** Gamepad/keyboard: same as tapping the lower arrow. */
    public void selectNext() {
        if (paused || PauseGroup.isPause() || selectors.size() < 2) {
            return;
        }
        boomBox.playSound(MSConstants.UI_LEFT_RIGHT);
        switchedOnce = true;
        select++;
        if (select > selectors.size() - 1) {
            select = 0;
        }
    }

    /** Gamepad/keyboard: same as tapping the centre item. */
    public void useSelected() {
        if (selectors.size() != 0 && centerSelector != null && centerSelector.isActive() && !PauseGroup.isPause()) {
            removeSelector();
            centerSelector.press();
            usedOnce = true;
            Vibrator.vibrateUI(VConstants.UI_CLICK);
        }
    }

    public void addSelector(Sticker sticker, int amount) {
        LeftGroup.getInstance().updateScroll(sticker);
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

        circleOut.setPosition(upButton.getX(), upButton.getY());
        circleOut.draw(batch, 1.0f);
        circleOut.setPosition(downButton.getX(), downButton.getY());
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
        upSelector.draw(batch,
                upButton.getX(), upButton.getY(),
                upButton.getOriginX(), upButton.getOriginY(),
                upButton.getWidth(), upButton.getHeight(),
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
        downSelector.draw(batch,
                downButton.getX(), downButton.getY(),
                downButton.getOriginX(), downButton.getOriginY(),
                downButton.getWidth(), downButton.getHeight(),
                1.0f, 1.0f, 0.0f
        );
        batch.setColor(Color.WHITE);
    }

    public void update(float dt) {
        if (upButton.isPressed()) {
            upArrow.setColor(Color.RED);
            upArrow1.setColor(Color.RED);
        } else {
            upArrow.setColor(Color.WHITE);
            upArrow1.setColor(Color.WHITE);
        }

        if (downButton.isPressed()) {
            downArrow.setColor(Color.RED);
            downArrow1.setColor(Color.RED);
        } else {
            downArrow.setColor(Color.WHITE);
            downArrow1.setColor(Color.WHITE);
        }

        if (selectors.size() >= 3) {
            centerSelector = selectors.get(select);
            if (select == 0) {
                upSelector = selectors.get(selectors.size() - 1);
                downSelector = selectors.get(select + 1);
            } else if (select == selectors.size() - 1) {
                upSelector = selectors.get(select - 1);
                downSelector = selectors.get(0);
            } else {
                downSelector = selectors.get(select + 1);
                upSelector = selectors.get(select - 1);
            }

        } else if (selectors.size() == 2) {
            if (select == 1) {
                upSelector = selectors.get(0);
                centerSelector = selectors.get(1);
            }
            if (select == 0) {
                downSelector = selectors.get(1);
                centerSelector = selectors.get(0);
            }
        } else if (selectors.size() == 1) {
            centerSelector = selectors.get(0);
        } else {
            centerSelector = null;
        }

        LeftGroup.getInstance().setCenter(centerSelector);
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

    public void changePosition(boolean isLeft) {
        if (isLeft) {
            centerButton.setPosition(Rules.SAFE_X + 13, Rules.WORLD_HEIGHT / 3 - centerHeight / 2 + 70);
            upButton.setPosition(Rules.SAFE_X + 13, Rules.WORLD_HEIGHT / 3 - height / 2 + centerHeight + 70);
            downButton.setPosition(Rules.SAFE_X + 13, Rules.WORLD_HEIGHT / 3 - height / 2 - centerHeight + 70);
            upArrow.setVisible(true);
            upArrow1.setVisible(false);
            downArrow.setVisible(true);
            downArrow1.setVisible(false);
        } else {
            centerButton.setPosition(Rules.WORLD_WIDTH - Rules.SAFE_X - 13 - centerWidth, Rules.WORLD_HEIGHT / 3 - centerHeight / 2 + 70);
            upButton.setPosition(Rules.WORLD_WIDTH - Rules.SAFE_X - 13 - width, Rules.WORLD_HEIGHT / 3 - height / 2 + centerHeight + 70);
            downButton.setPosition(Rules.WORLD_WIDTH - Rules.SAFE_X - 13 - width, Rules.WORLD_HEIGHT / 3 - height / 2 - centerHeight + 70);
            upArrow.setVisible(false);
            upArrow1.setVisible(true);
            downArrow.setVisible(false);
            downArrow1.setVisible(true);
        }

        upArrow.setPosition(upButton.getX(), upButton.getY());
        downArrow.setPosition(downButton.getX(), downButton.getY());
        upArrow1.setPosition(upButton.getX(), upButton.getY());
        downArrow1.setPosition(downButton.getX(), downButton.getY());
    }

    public void dispose() {
        instance = null;
    }

    public static void doStart() {
        instance = null;
    }
}