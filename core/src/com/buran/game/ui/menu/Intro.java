package com.buran.game.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.buran.game.conf.Rules;
import com.buran.game.platform.Radio;

/**
 * The opening as a living comic: pages of panels that appear one by one, each with a slow
 * camera drift, a typed caption and a voice over the radio. A goes to the next panel, B skips.
 * Real history first (1988, 1991), then the secret flight and the rift (tips 12, 29, 36).
 */
public class Intro extends Actor implements MenuNav.Target {
    public interface Done {
        void done();
    }

    private static final String[] INTRO_CAPTION = {
            "BAIKONUR.  15 NOVEMBER 1988",
            "BURAN FLEW ONCE.  NO ONE WAS ABOARD.",
            "TWO ORBITS.  A PERFECT LANDING.",
            "THEN THE PROGRAM WAS CLOSED.",
            "1991",
            "ONE MAN STAYED IN ORBIT WHILE THE COUNTRY BELOW HIM DISAPPEARED.",
            "BUT THE PROGRAM WENT ON.  IN SECRET.",
            "A LIGHT IN ORBIT NO ONE COULD EXPLAIN.  THEY NAMED IT OBJECT-19 AND SENT BURAN TO IT.",
            "THE EXPERIMENT FAILED.",
            "THE CREW WAS SCATTERED ACROSS THE FIELD.",
            "AND AT THE CENTRE, THE RIFT OPENED.",
            "BAIKONUR, THIS IS BURAN-19.|BAIKONUR...|DO YOU COPY?",
            "NO ONE ANSWERS.|BRING THEM HOME.|BUT WHERE IS HOME?",
    };

    /** Per panel: page, column, row, column span, row span on a 2x2 page grid. */
    private static final int[][] INTRO_LAYOUT = {
            {0, 0, 0, 1, 1}, {0, 1, 0, 1, 1}, {0, 0, 1, 1, 1}, {0, 1, 1, 1, 1},
            {1, 0, 0, 2, 1}, {1, 0, 1, 1, 1}, {1, 1, 1, 1, 1},
            {2, 0, 0, 1, 1}, {2, 1, 0, 1, 1}, {2, 0, 1, 1, 1}, {2, 1, 1, 1, 1},
            {3, 0, 0, 2, 2},
            {4, 0, 0, 2, 2},
    };

    /**
     * Captions split by '|' appear part by part, in step with the voice: when each part starts,
     * in seconds after the line begins (matches the pauses in intro12.mp3 and intro13.mp3).
     */
    private static final float[][] INTRO_PART_AT = new float[13][];

    static {
        INTRO_PART_AT[11] = new float[]{0f, 4.78f, 7.82f};
        INTRO_PART_AT[12] = new float[]{0f, 3.14f, 6.06f};
    }

    /**
     * What a comic scene shows: captions ('|' splits parts timed by partAt), the page layout,
     * the pictures (the first path that exists for each panel), the voice line ids and the
     * label of the skip plate. The opening and the chapter endings use the same player.
     */
    public static final class Script {
        final String[] captions;
        final int[][] layout;
        final float[][] partAt;
        final String[][] images;
        final String voice;
        final String skip;
        /** Voice line per panel; null = the pattern with the panel number. */
        String[] voices;

        public Script(String[] captions, int[][] layout, float[][] partAt, String[][] images, String voice, String skip) {
            this.captions = captions;
            this.layout = layout;
            this.partAt = partAt;
            this.images = images;
            this.voice = voice;
            this.skip = skip;
        }
    }

    private static String[][] introImages() {
        String[][] im = new String[13][];
        for (int i = 0; i < 13; i++) {
            im[i] = new String[]{String.format("intro/p%02d.jpg", i + 1)};
        }
        return im;
    }

    public static final Script INTRO = new Script(INTRO_CAPTION, INTRO_LAYOUT, INTRO_PART_AT, introImages(), "intro%02d", "SKIP INTRO");

    /** Chapter one ends: the crew aboard, the rift, and a voice that is not Baikonur. */
    public static final Script CHAPTER_ONE = new Script(
            new String[]{
                    "ALL ABOARD.  THE CABIN IS FULL OF BREATHING AGAIN.",
                    "BUT THE RIFT IS STILL OPEN.  AND IT IS GROWING.",
                    "THEN, THROUGH THE STATIC...|A VOICE.|NOT BAIKONUR.",
                    "BURAN-19... WE SEE YOU.|COME TO THE RIFT.|END OF CHAPTER ONE",
            },
            new int[][]{{0, 0, 0, 2, 2}, {1, 0, 0, 2, 2}, {2, 0, 0, 2, 2}, {3, 0, 0, 2, 2}},
            new float[][]{null, null, {0f, 3.64f, 6.52f}, {0f, 4.28f, 6.8f}},
            new String[][]{
                    {"chapter1/p01.jpg", "ui/clear.jpg"},
                    {"chapter1/p02.jpg", "ui/sector04.jpg", "intro/p11.jpg"},
                    {"chapter1/p03.jpg", "intro/p12.jpg"},
                    {"chapter1/p04.jpg", "intro/p13.jpg"},
            },
            "ch1_%02d", "SKIP");

    /** Chapter two ends: the voice is the commander of Object-19, and the way home is open. */
    public static final Script CHAPTER_TWO = new Script(
            new String[]{
                    "THE VOICE KNOWS OUR NAMES.",
                    "IT IS THE COMMANDER OF OBJECT-19.  HE WENT INTO THE RIFT IN 1991.",
                    "INSIDE, HE SAYS, IT IS STILL 1991.|AND THE WAY HOME IS OPEN.|END OF CHAPTER TWO",
            },
            new int[][]{{0, 0, 0, 2, 2}, {1, 0, 0, 2, 2}, {2, 0, 0, 2, 2}},
            new float[][]{null, null, {0f, 5.52f, 7.6f}},
            new String[][]{
                    {"chapter2/p01.jpg", "intro/p12.jpg"},
                    {"chapter2/p02.jpg", "intro/p08.jpg"},
                    {"chapter2/p03.jpg", "intro/p11.jpg"},
            },
            "ch2_%02d", "SKIP");

    /**
     * The end, depending on how you flew it (VISION: the ending depends on whom you saved):
     * 1 everyone came home, 2 some were lost, 3 one was left behind for the fuel. The extra panel
     * comes before Baikonur answers.
     */
    public static Script ending(int variant) {
        String[] lines = {
                "EVERY ONE OF THEM CAME HOME.",
                "NOT ALL OF US CAME HOME.  WE SAY THEIR NAMES.",
                "AND SOMEWHERE OUT THERE, ONE OF OURS IS STILL WAITING.",
        };
        int v = Math.max(1, Math.min(3, variant));
        String[] c = CHAPTER_THREE.captions;
        Script s = new Script(
                new String[]{c[0], c[1], c[2], lines[v - 1], c[3]},
                new int[][]{{0, 0, 0, 2, 2}, {1, 0, 0, 2, 2}, {2, 0, 0, 2, 2}, {3, 0, 0, 2, 2}, {4, 0, 0, 2, 2}},
                new float[][]{null, null, null, null, CHAPTER_THREE.partAt[3]},
                new String[][]{CHAPTER_THREE.images[0], CHAPTER_THREE.images[1], CHAPTER_THREE.images[2],
                        {"chapter3/p05.jpg", v == 1 ? "ui/clear.jpg" : v == 2 ? "intro/p10.jpg" : "ui/lost.jpg"},
                        CHAPTER_THREE.images[3]},
                "ch3_%02d", "SKIP");
        s.voices = new String[]{"ch3_01", "ch3_02", "ch3_03", "ch3_v" + v, "ch3_04"};
        return s;
    }

    /** The end: into the light, Earth whole, and Baikonur answering at last. */
    public static final Script CHAPTER_THREE = new Script(
            new String[]{
                    "WE WENT IN.  ALL OF US.",
                    "NO SOUND.  NO TIME.  ONLY THE LIGHT.",
                    "THEN... EARTH.  BLUE, AND WHOLE.",
                    "BURAN-19, THIS IS BAIKONUR.|WE HEAR YOU.|WELCOME HOME.|THE END",
            },
            new int[][]{{0, 0, 0, 2, 2}, {1, 0, 0, 2, 2}, {2, 0, 0, 2, 2}, {3, 0, 0, 2, 2}},
            new float[][]{null, null, null, {0f, 5.28f, 7.8f, 9.6f}},
            new String[][]{
                    {"chapter3/p01.jpg", "intro/p11.jpg"},
                    {"chapter3/p02.jpg", "intro/p09.jpg"},
                    {"chapter3/p03.jpg", "intro/p03.jpg"},
                    {"chapter3/p04.jpg", "intro/p01.jpg"},
            },
            "ch3_%02d", "SKIP");

    /** The intro has its own mix, outside the settings: the score under, the voice on top. */
    private static final float MUSIC_VOLUME = 0.5f;
    private static final float VOICE_VOLUME = 1f;

    private static final float FADE = 0.7f;
    private static final float VOICE_AT = 0.45f;
    private static final float MIN_PANEL = 3.6f;
    private static final float AFTER_VOICE = 1.0f;
    private static final float TYPE_SPEED = 32f;

    private final Script script;
    private final String[] CAPTION;
    private final int[][] LAYOUT;
    private final float[][] PART_AT;
    private final Texture[] tex;
    private final float[] age;
    private final BitmapFont caption;
    private final BitmapFont captionBig;
    private final BitmapFont hint;
    private final Done done;
    private int current;
    private float panelTime;
    private float voiceEnded = -1f;
    private float pageFade = 1f;
    private boolean turning;
    private boolean ending;
    private float endTime;
    private float alpha;
    private final com.badlogic.gdx.math.Rectangle skipRect = new com.badlogic.gdx.math.Rectangle();

    /** Called once, when the intro actually starts (the page has shown the game). */
    private final Runnable onStart;
    private boolean begun;
    private com.badlogic.gdx.audio.Music music;

    public Intro(Done done, Runnable onStart) {
        this(INTRO, done, onStart);
    }

    public Intro(Script script, Done done, Runnable onStart) {
        this.script = script;
        this.CAPTION = script.captions;
        this.LAYOUT = script.layout;
        this.PART_AT = script.partAt;
        this.tex = new Texture[CAPTION.length];
        this.age = new float[CAPTION.length];
        this.done = done;
        this.onStart = onStart;
        this.alpha = 1f;
        caption = Theme.font(54);
        captionBig = Theme.font(72);
        hint = Theme.font(54);
        for (int i = 0; i < tex.length; i++) {
            String path = script.images[i][script.images[i].length - 1];
            for (String c : script.images[i]) {
                if (Gdx.files.internal(c).exists()) {
                    path = c;
                    break;
                }
            }
            tex[i] = new Texture(Gdx.files.internal(path));
            tex[i].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        setBounds(0, 0, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        MenuNav.set(this);
    }

    // ------------------------------------------------------------------ flow

    private int page(int i) {
        return LAYOUT[i][0];
    }

    private void next() {
        Radio.stop();
        if (current + 1 >= CAPTION.length) {
            finish();
            return;
        }
        if (page(current + 1) != page(current)) {
            turning = true;
        } else {
            current++;
            panelTime = 0f;
            voiceEnded = -1f;
        }
    }

    private void finish() {
        if (!ending) {
            ending = true;
            endTime = 0f;
            Radio.stop();
        }
    }

    @Override
    public void nav(Nav nav) {
        if (nav == Nav.ACCEPT) {
            next();
        } else if (nav == Nav.BACK) {
            finish();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Hold on black until the page around the game has gone (web boot screen).
        if (!begun) {
            if (!com.buran.game.platform.Shell.shown()) {
                return;
            }
            begun = true;
            if (onStart != null) {
                onStart.run();
            }
            com.badlogic.gdx.assets.AssetManager am = com.buran.game.utils.Assets.getInstance().getAssetManager();
            if (am.isLoaded(com.buran.game.conf.MSConstants.INTRO_SONG)) {
                music = am.get(com.buran.game.conf.MSConstants.INTRO_SONG, com.badlogic.gdx.audio.Music.class);
                music.setLooping(false);
                music.setVolume(MUSIC_VOLUME);
                if (!music.isPlaying()) {
                    music.play();
                }
            }
        }
        if (music != null) {
            // Its own steady level: not the music slider, not ducked under the voice.
            music.setLooping(false);
            music.setVolume(MUSIC_VOLUME * (ending ? MathUtils.clamp(1f - endTime / 1.2f, 0f, 1f) : 1f));
        }
        if (ending) {
            endTime += delta;
            if (endTime > 1.2f) {
                if (music != null) {
                    music.stop();
                }
                dispose();
                remove();
                MenuNav.clear(this);
                if (done != null) {
                    done.done();
                }
            }
            return;
        }
        if (Gdx.input.justTouched() && getStage() != null) {
            com.badlogic.gdx.math.Vector2 v = getStage().screenToStageCoordinates(
                    new com.badlogic.gdx.math.Vector2(Gdx.input.getX(), Gdx.input.getY()));
            if (skipRect.contains(v)) {
                finish();
                return;
            }
            next();
        }
        Radio.update();
        for (int i = 0; i <= current; i++) {
            age[i] += delta;
        }
        if (turning) {
            pageFade -= delta / FADE;
            if (pageFade <= 0f) {
                pageFade = 0f;
                turning = false;
                current++;
                panelTime = 0f;
                voiceEnded = -1f;
            }
            return;
        }
        pageFade = Math.min(1f, pageFade + delta / FADE);
        panelTime += delta;
        if (panelTime >= VOICE_AT && panelTime - delta < VOICE_AT) {
            Radio.play(script.voices != null ? script.voices[current] : String.format(script.voice, current + 1), VOICE_VOLUME);
        }
        if (panelTime > VOICE_AT + 0.3f && !Radio.isPlaying() && voiceEnded < 0f) {
            voiceEnded = panelTime;
        }
        boolean last = current == CAPTION.length - 1;
        float hold = last ? 3f : AFTER_VOICE;
        if (voiceEnded >= 0f && panelTime > MIN_PANEL && panelTime - voiceEnded > hold) {
            if (last) {
                finish();
            } else {
                next();
            }
        }
    }

    // ------------------------------------------------------------------ draw

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float W = Rules.WORLD_WIDTH;
        float H = Rules.WORLD_HEIGHT;
        float out = ending ? MathUtils.clamp(1f - endTime / 1.2f, 0f, 1f) : 1f;
        Theme.rect(batch, 0, 0, W, H, Color.BLACK, alpha * out);
        if (current >= CAPTION.length) {
            return;
        }
        int pg = page(current);
        boolean full = LAYOUT[current][3] == 2 && LAYOUT[current][4] == 2;
        float m = full ? 0f : 80f;
        float g = 40f;
        float bx = m;
        float by = m + (full ? 0f : 40f);
        float bw = W - 2f * m;
        float bh = H - 2f * m - (full ? 0f : 40f);
        float cw = (bw - g) / 2f;
        float ch = (bh - g) / 2f;
        float pa = alpha * out * Interpolation.smooth.apply(pageFade);
        for (int i = 0; i <= current; i++) {
            if (page(i) != pg) {
                continue;
            }
            int[] l = LAYOUT[i];
            float w = l[3] == 2 ? bw : cw;
            float h = l[4] == 2 ? bh : ch;
            float x = bx + l[1] * (cw + g);
            float y = by + bh - (l[2] + 1) * ch - l[2] * g - (l[4] == 2 ? ch + g : 0f);
            float in = MathUtils.clamp(age[i] / FADE, 0f, 1f);
            float dim = i == current ? 1f : 0.45f;
            panel(batch, i, x, y, w, h, pa * Interpolation.smooth.apply(in) * dim);
            if (i == current) {
                captionPlate(batch, full ? captionBig : caption, CAPTION[i], x, y, w, pa * in);
            }
        }
        // Controls: a SKIP plate in the corner (B, ESC, a click or a tap), NEXT beside it.
        float ha = alpha * out;
        String sk = Hints.button("B", "ESC");
        float sw = Theme.hintWidth(hint, sk, script.skip);
        float kh = Theme.keyHeight(hint);
        float pw = sw + kh;
        float ph = kh * 1.6f;
        float px = W - 70f - pw;
        float py = full ? 60f : 30f;
        skipRect.set(px, py, pw, ph);
        Theme.rect(batch, px - 2f, py - 2f, pw + 4f, ph + 4f, Theme.EDGE, 0.9f * ha);
        Theme.rect(batch, px, py, pw, ph, Theme.BODY_LO, 0.85f * ha);
        Theme.hint(batch, hint, sk, script.skip, px + kh / 2f, py + ph / 2f, Theme.TEXT, ha);
        String nk = Hints.button("A", "ENTER");
        float nw = Theme.hintWidth(hint, nk, "NEXT");
        Theme.hint(batch, hint, nk, "NEXT", px - 60f - nw, py + ph / 2f, Theme.DIM, 0.8f * ha);
    }

    /** The image cropped to the panel's shape with a slow push-in, in a thin dark frame. */
    private void panel(Batch batch, int i, float x, float y, float w, float h, float a) {
        if (a <= 0.01f) {
            return;
        }
        Texture t = tex[i];
        float zoom = 1.04f + 0.06f * MathUtils.clamp(age[i] / 14f, 0f, 1f);
        float aspect = w / h;
        float sw = t.getWidth();
        float sh = sw / aspect;
        if (sh > t.getHeight()) {
            sh = t.getHeight();
            sw = sh * aspect;
        }
        sw /= zoom;
        sh /= zoom;
        // Drift a little to one side, alternating per panel.
        float drift = (i % 2 == 0 ? 1f : -1f) * (t.getWidth() - sw) * 0.25f * MathUtils.clamp(age[i] / 14f, 0f, 1f);
        float sx = (t.getWidth() - sw) / 2f + drift;
        float sy = (t.getHeight() - sh) / 2f;
        Theme.rect(batch, x - 4f, y - 4f, w + 8f, h + 8f, Theme.EDGE, a);
        batch.setColor(1f, 1f, 1f, a);
        batch.draw(t, x, y, w, h, (int) sx, (int) sy, (int) sw, (int) sh, false, false);
        batch.setColor(Color.WHITE);
    }

    private final com.badlogic.gdx.graphics.g2d.GlyphLayout layout = new com.badlogic.gdx.graphics.g2d.GlyphLayout();

    /** What of the caption is on screen now: parts that have started, the newest one typed in. */
    private String shownCaption(int i) {
        float t = panelTime - 0.3f;
        float[] at = PART_AT[i];
        if (at == null) {
            String all = CAPTION[i].replace("|", "  ");
            return all.substring(0, MathUtils.clamp((int) (t * TYPE_SPEED), 0, all.length()));
        }
        String[] parts = CAPTION[i].split("\\|");
        StringBuilder b = new StringBuilder();
        for (int k = 0; k < parts.length; k++) {
            float start = k == 0 ? 0f : VOICE_AT + at[Math.min(k, at.length - 1)];
            if (t < start) {
                break;
            }
            if (k > 0) {
                b.append("  ");
            }
            b.append(parts[k], 0, MathUtils.clamp((int) ((t - start) * TYPE_SPEED), 0, parts[k].length()));
        }
        return b.toString();
    }

    /** The caption in a dark plate at the panel's lower left, typed in, wrapped to the panel. */
    private void captionPlate(Batch batch, BitmapFont font, String text, float x, float y, float w, float a) {
        String shown = shownCaption(current);
        if (shown.isEmpty() || a <= 0.01f) {
            return;
        }
        String full = text.replace("|", "  ");
        float maxW = w - 144f;
        layout.setText(font, full, Theme.TEXT, maxW, com.badlogic.gdx.utils.Align.left, true);
        float tw = Math.min(layout.width, maxW);
        float ph = font.getCapHeight() * 1.6f + layout.height;
        float px = x + 40f;
        float py = y + 40f;
        Theme.rect(batch, px - 2f, py - 2f, tw + 64f + 4f, ph + 4f, Theme.EDGE, a * 0.9f);
        Theme.rect(batch, px, py, tw + 64f, ph, Theme.BODY_LO, a * 0.9f);
        Color old = font.getColor().cpy();
        font.setColor(Theme.TEXT.r, Theme.TEXT.g, Theme.TEXT.b, a);
        font.draw(batch, shown, px + 32f, py + ph - font.getCapHeight() * 0.8f, tw, com.badlogic.gdx.utils.Align.left, true);
        font.setColor(old);
    }

    private void dispose() {
        for (Texture t : tex) {
            if (t != null) {
                t.dispose();
            }
        }
    }
}
