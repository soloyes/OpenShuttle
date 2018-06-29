package com.shuttle.game.buttons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * @author Shuttle on 3/24/18.
 */
public class ImageButtonBuilder {
    private final float SCALE = 2.0f;
    private final float SCALE_OUT = 0.1f;

    private ImageButton myButton;
    private ImageButton.ImageButtonStyle myButtonStyle;
    private Skin skin;

    public ImageButtonBuilder(Skin skin) {
        this.skin = skin;
    }

    public Button getButton(String name) {
        myButtonStyle = new ImageButton.ImageButtonStyle();
        myButtonStyle.imageUp = skin.getDrawable(name);
        myButton = new ImageButton(myButtonStyle);
        myButton.setOrigin(1);
        myButton.setScale(SCALE);
        myButton.setTransform(true);

        final ImageButton btn = myButton;
        myButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btn.setScale(SCALE - SCALE_OUT);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btn.setScale(SCALE);
            }
        });
        return myButton;
    }
}