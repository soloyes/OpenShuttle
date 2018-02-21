package ru.geekbrains.stargame.engine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Created by sol on 2/20/18.
 */

public class Font extends BitmapFont {

    public Font(String fontFile, String imageFile) {
        super(Gdx.files.internal(fontFile), Gdx.files.internal(imageFile), false, false);
        getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void setWordSize(float wordSize) {
        System.out.println(getCapHeight());
        System.out.println(getScaleX() + " " + getScaleY());
        getData().setScale(wordSize / getCapHeight());
        System.out.println(getScaleX() + " " + getScaleY());
        System.out.println(getCapHeight());
    }

    public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, int halign) {
        return super.draw(batch, str, x, y, 0f, halign, false);
    }
}
