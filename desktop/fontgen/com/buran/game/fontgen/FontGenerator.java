package com.buran.game.fontgen;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter;

/**
 * Pre-renders the game's TTF into BitmapFont (.fnt + .png) files.
 * <p>
 * The game used to rasterize fonts with FreeType at load time. The web build has no
 * cheap FreeType, so the same sizes and styling are baked once here and committed to
 * assets/fonts. Run with {@code ./gradlew desktop:generateFonts}.
 */
public class FontGenerator extends ApplicationAdapter {
    /** Every size requested by Assets.loadAssets(). */
    static final int[] SIZES = {32, 36, 44, 48, 54, 60, 64, 72, 96, 128, 175};
    static final String TTF = "fonts/college.regular.ttf";
    static final String PREFIX = "college.regular";
    /** Sizes that also need an independent "Coloured" BitmapFont instance (tinted at runtime). */
    static final int[] COLOURED = {32, 48, 60, 128};

    private final FileHandle assets;

    FontGenerator(FileHandle assets) {
        this.assets = assets;
    }

    @Override
    public void create() {
        FileHandle out = assets.child("fonts");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(assets.child(TTF));
        for (int size : SIZES) {
            int pageSize = size >= 96 ? 2048 : 1024;
            PixmapPacker packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 2, false);

            FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
            p.size = size;
            p.color = Color.valueOf("ffffffff");
            p.borderWidth = 1;
            p.borderColor = Color.valueOf("000000ff");
            p.shadowOffsetX = 3;
            p.shadowOffsetY = 3;
            p.shadowColor = Color.valueOf("000000ff");
            p.packer = packer;

            FreeTypeFontGenerator.FreeTypeBitmapFontData data = generator.generateData(p);

            String name = PREFIX + size;
            BitmapFontWriter.FontInfo info = new BitmapFontWriter.FontInfo();
            info.face = PREFIX;
            info.size = size;
            info.padding = new BitmapFontWriter.Padding(0, 0, 0, 0);
            info.spacing = new BitmapFontWriter.Spacing();

            String[] pageRefs = BitmapFontWriter.writePixmaps(packer.getPages(), out, name);
            BitmapFontWriter.writeFont(data, pageRefs, out.child(name + ".fnt"), info, pageSize, pageSize);
            for (int c : COLOURED) {
                if (c == size) {
                    out.child(name + ".fnt").copyTo(out.child(name + "Coloured.fnt"));
                }
            }
            packer.dispose();
            Gdx.app.log("fontgen", name + ".fnt (" + pageRefs.length + " page(s))");
        }
        generator.dispose();
        Gdx.app.exit();
    }

    public static void main(String[] args) {
        FileHandle assets = new FileHandle(args.length > 0 ? args[0] : "assets");
        BitmapFontWriter.setOutputFormat(BitmapFontWriter.OutputFormat.Text);
        new HeadlessApplication(new FontGenerator(assets), new HeadlessApplicationConfiguration());
    }
}
